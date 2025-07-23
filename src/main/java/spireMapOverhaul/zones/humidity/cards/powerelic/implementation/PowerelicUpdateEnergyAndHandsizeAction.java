package spireMapOverhaul.zones.humidity.cards.powerelic.implementation;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import spireMapOverhaul.util.Wiz;

public class PowerelicUpdateEnergyAndHandsizeAction extends AbstractGameAction {
    int previousMaxEnergy, previousHandSize;

    public PowerelicUpdateEnergyAndHandsizeAction(int previousMaxEnergy, int previousHandSize) {
        this.previousMaxEnergy = previousMaxEnergy;
        this.previousHandSize = previousHandSize;
    }

    public void update() {
        {
            int diff = Wiz.adp().energy.energyMaster - previousMaxEnergy;
            if (diff != 0) {
                Wiz.att(new GainEnergyAction(diff));
                Wiz.adp().energy.energy = Wiz.adp().energy.energyMaster;
            }
        }
        {
            int diff = Wiz.adp().masterHandSize - previousHandSize;
            if (diff != 0) {
                Wiz.adp().gameHandSize = Wiz.adp().masterHandSize;
                if (diff > 0) {
                    Wiz.att(new DrawCardAction(diff));
                }
            }
        }
        isDone = true;
    }
}
