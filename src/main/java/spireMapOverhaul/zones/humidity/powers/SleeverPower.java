package spireMapOverhaul.zones.humidity.powers;

import com.megacrit.cardcrawl.actions.animations.TalkAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.actions.unique.DiscoveryAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import spireMapOverhaul.SpireAnniversary6Mod;
import spireMapOverhaul.abstracts.AbstractSMOPower;
import spireMapOverhaul.zones.humidity.HumidityZone;

import static spireMapOverhaul.SpireAnniversary6Mod.makeID;

public class SleeverPower extends AbstractSMOPower {
    public static final String POWER_ID = SpireAnniversary6Mod.makeID("SleevingPower");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    public static String ZONE_ID = HumidityZone.ID;
    private boolean triggered=false;

    public SleeverPower(AbstractCreature owner, int amount) {
        super(POWER_ID,NAME,ZONE_ID, PowerType.BUFF, false, owner, amount);
    }
    @Override
    public void updateDescription() {
        this.description = DESCRIPTIONS[0];
    }

    public int onAttacked(DamageInfo info, int damageAmount) {
        if (!this.triggered && damageAmount > 0 && info.owner != null && info.type == DamageInfo.DamageType.NORMAL) {
            this.flash();
            this.triggered = true;
            this.addToBot(new TalkAction(this.owner,SLEEVER_HIT));
            this.addToBot(new RemoveSpecificPowerAction(this.owner, this.owner, this));
            this.addToBot(new DiscoveryAction());
        }
        return damageAmount;
    }
    public static final String MONSTERID = makeID("Sleevers");
    public static final String SLEEVER_HIT = CardCrawlGame.languagePack.getMonsterStrings(MONSTERID).DIALOG[3];
}
