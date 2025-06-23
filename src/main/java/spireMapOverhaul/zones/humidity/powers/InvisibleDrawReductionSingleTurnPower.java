package spireMapOverhaul.zones.humidity.powers;

import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.InvisiblePower;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import spireMapOverhaul.SpireAnniversary6Mod;
import spireMapOverhaul.zones.humidity.encounters.monsters.joustimplementation.JoustManagerPower;
import spireMapOverhaul.zones.invasion.powers.DrawReductionSingleTurnPower;

public class InvisibleDrawReductionSingleTurnPower extends DrawReductionSingleTurnPower implements InvisiblePower {
    public static final String POWER_ID = SpireAnniversary6Mod.makeID("DrawReductionSingleTurn");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    public InvisibleDrawReductionSingleTurnPower(AbstractCreature owner, int amount) {
        super(owner, amount);
    }
    public void atStartOfTurn() {
        //If centurion or mystic KO themselves via thorns, behavior goes back to normal.
        //Make sure the player can still draw cards on turn 2 in that case!
        if(!JoustManagerPower.preJoustMonstersAreValid()) {
            //If we tried to addToTop RemoveSpecificPowerAction, it wouldn't get called until after cards were already drawn.
            //So instead, call onRemove to change the player's hand size NOW.
            onRemove();
            //Then, set the power's amount to 0
            //so the player's hand size doesn't get adjusted a second time when the power is removed automatically.
            this.amount=0;
        }
    }
}
