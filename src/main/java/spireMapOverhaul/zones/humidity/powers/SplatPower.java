package spireMapOverhaul.zones.humidity.powers;

import com.evacipated.cardcrawl.mod.stslib.actions.common.StunMonsterAction;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch2;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.GainStrengthPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import spireMapOverhaul.SpireAnniversary6Mod;
import spireMapOverhaul.abstracts.AbstractSMOPower;
import spireMapOverhaul.util.Wiz;
import spireMapOverhaul.zones.humidity.HumidityZone;

import static com.megacrit.cardcrawl.cards.AbstractCard.CardRarity.*;

public class SplatPower extends AbstractSMOPower {
    public static final String POWER_ID = SpireAnniversary6Mod.makeID("SplatPower");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    public static String ZONE_ID = HumidityZone.ID;

    public SplatPower(AbstractCreature owner, int amount) {
        super(POWER_ID,NAME,ZONE_ID, PowerType.BUFF, false, owner, amount);
    }
    @Override
    public void updateDescription() {
        this.description = DESCRIPTIONS[0];
    }

    @SpirePatch2(clz= AbstractMonster.class,method="damage")
    public static class DamagePatch{
        @SpirePostfixPatch
        public static void Foo(AbstractMonster __instance){
            if(!__instance.hasPower(SplatPower.POWER_ID))return;
            if (!__instance.isDying && (float)__instance.currentHealth <= (float)__instance.maxHealth / 2.0F) {
                Wiz.att(new StunMonsterAction(__instance, Wiz.adp()));
                Wiz.att(new RemoveSpecificPowerAction(__instance, __instance, SplatPower.POWER_ID));
            }
        }
    }
}
