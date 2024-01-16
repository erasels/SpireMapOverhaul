package spireMapOverhaul.zones.ambushed.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpireInsertLocator;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.BackAttackPower;
import com.megacrit.cardcrawl.powers.SurroundedPower;
import javassist.CtBehavior;
import javassist.NotFoundException;
import spireMapOverhaul.zones.ambushed.AmbushedUtil;

@SpirePatch(
        clz = AbstractMonster.class,
        method = "die",
        paramtypez = {}
)
public class NewSurroundedMonstersPatch {

    @SpireInsertPatch(locator = Locator.class)
    public static void Insert(AbstractMonster __instance) {
        if (AmbushedUtil.isInAmbushedZone() && AbstractDungeon.player.hasPower(SurroundedPower.POWER_ID)) {
            AbstractMonster otherMonster = null;
            for (AbstractMonster m : AbstractDungeon.getCurrRoom().monsters.monsters) {
                if (m != __instance && !m.isDead && !m.isDying) {
                    otherMonster = m;
                    // Remove BackAttack Power from the other monster
                    if (m.hasPower(BackAttackPower.POWER_ID)) {
                        AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(m, m, BackAttackPower.POWER_ID));
                    }
                }
            }

            if (otherMonster != null) {
                // Flip the player's orientation based on the remaining monster's position
                AbstractDungeon.player.flipHorizontal = otherMonster.drawX < AbstractDungeon.player.drawX;
            }

            // Remove the Surrounded power from the player
            AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(AbstractDungeon.player, AbstractDungeon.player, SurroundedPower.POWER_ID));
        }
    }

    private static class Locator extends SpireInsertLocator {
        @Override
        public int[] Locate(CtBehavior ctMethodToPatch) throws NotFoundException {
            return new int[]{0}; // Insert at the start of the method
        }
    }
}
