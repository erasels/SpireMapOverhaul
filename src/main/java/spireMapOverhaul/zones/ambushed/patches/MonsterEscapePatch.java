package spireMapOverhaul.zones.ambushed.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.BackAttackPower;
import com.megacrit.cardcrawl.powers.SurroundedPower;
import spireMapOverhaul.zones.ambushed.AmbushedUtil;

@SpirePatch(
        clz = AbstractMonster.class,
        method = "escape",
        paramtypez = {}
)
public class MonsterEscapePatch {
    @SpirePostfixPatch
    public static void Postfix(AbstractMonster __instance) {
        if (__instance.hasPower(BackAttackPower.POWER_ID)) {
            __instance.powers.removeIf(p -> p.ID.equals(BackAttackPower.POWER_ID));
        }

        if (AmbushedUtil.isInAmbushedZone() && AbstractDungeon.player.hasPower(SurroundedPower.POWER_ID)) {
            AbstractMonster otherMonster = null;
            for (AbstractMonster m : AbstractDungeon.getCurrRoom().monsters.monsters) {
                if (m != __instance && !m.isDead && !m.isDying && !m.isEscaping) {
                    otherMonster = m;
                    if (m.hasPower(BackAttackPower.POWER_ID)) {
                        AbstractDungeon.actionManager.addToBottom(
                                new RemoveSpecificPowerAction(m, m, BackAttackPower.POWER_ID));
                    }
                }
            }

            if (otherMonster != null) {
                AbstractDungeon.player.flipHorizontal = otherMonster.drawX < AbstractDungeon.player.drawX;
            }

            AbstractDungeon.actionManager.addToBottom(
                    new RemoveSpecificPowerAction(AbstractDungeon.player, AbstractDungeon.player, SurroundedPower.POWER_ID));
        }
    }
}