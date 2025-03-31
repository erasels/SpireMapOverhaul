package spireMapOverhaul.zones.humidity.powers;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import spireMapOverhaul.SpireAnniversary6Mod;
import spireMapOverhaul.abstracts.AbstractSMOPower;
import spireMapOverhaul.zones.humidity.HumidityZone;

public class HallucinationCloudPower extends AbstractSMOPower {
    public static final String POWER_ID = SpireAnniversary6Mod.makeID("HallucinationCloudPower");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    public static String ZONE_ID = HumidityZone.ID;

    public HallucinationCloudPower(AbstractCreature owner, int amount) {
        super(POWER_ID,NAME,ZONE_ID, PowerType.DEBUFF, false, owner, amount);
    }
    public void updateDescription() {
        this.description = DESCRIPTIONS[0]+amount+DESCRIPTIONS[1];
    }

    public void onDeath() {
        if (!AbstractDungeon.getCurrRoom().isBattleEnding()) {
            CardCrawlGame.sound.play("SPORE_CLOUD_RELEASE");
            this.flashWithoutSound();
            this.addToTop(new ApplyPowerAction(AbstractDungeon.player, null, new HallucinatingPower(AbstractDungeon.player, this.amount), this.amount));
        }
    }
}
