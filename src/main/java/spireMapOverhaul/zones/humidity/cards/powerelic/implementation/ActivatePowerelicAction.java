package spireMapOverhaul.zones.humidity.cards.powerelic.implementation;

import basemod.ReflectionHacks;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.relics.SlaversCollar;
import com.megacrit.cardcrawl.vfx.SmokePuffEffect;
import spireMapOverhaul.util.Wiz;
import spireMapOverhaul.zones.humidity.cards.powerelic.PowerelicAllowlist;

public class ActivatePowerelicAction extends AbstractGameAction {

    private final PowerelicCard card;

    public ActivatePowerelicAction(PowerelicCard card) {
        this.card = card;
    }

    @Override
    public void update() {
        if (card.capturedRelic == null) {
            //if we're here, we somehow have a powerelic card without a relic inside.
            //this is probably due to some save file glitch we overlooked.  or a Prismatic effect of some sort.
            isDone = true;
            return;
        }

        activateRelicFromCard(card, card.capturedRelic);
        //note that if the card was duplicated, capturedRelic might point to a different relic now!
        int previousMaxEnergy = Wiz.adp().energy.energyMaster;
        int previousHandSize = Wiz.adp().masterHandSize;
        if (PowerelicAllowlist.isEssentialEquipRelic(card.capturedRelic)) {
            card.capturedRelic.onEquip();
        }
        if (card.capturedRelic instanceof SlaversCollar)
            ((SlaversCollar) card.capturedRelic).beforeEnergyPrep();
        //Logic change 2025/01/27: check for "immediate equip" status independently of "essential equip" status
        if (PowerelicAllowlist.isImmediateOnequipRelic(card.capturedRelic)) {
            Wiz.att(new PowerelicUpdateEnergyAndHandsizeAction(previousMaxEnergy, previousHandSize));
        }


        isDone = true;
    }

    public void activateRelicFromCard(PowerelicCard card, AbstractRelic relic) {
        if (!Wiz.adp().relics.contains(relic)) {
            //can't use spawnRelicAndObtain as that will automatically onEquip even if we don't want to
            Wiz.att(new ApplyPowerAction(Wiz.adp(), Wiz.adp(), new PowerelicPower(relic)));
            relic.instantObtain(Wiz.adp(), Wiz.adp().relics.size(), false);
            AbstractDungeon.effectsQueue.add(new SmokePuffEffect(relic.targetX, relic.targetY));
            if (Wiz.curRoom() != null) relic.justEnteredRoom(Wiz.curRoom());
            relic.atPreBattle();
            relic.atBattleStart();
            relic.atBattleStartPreDraw();
            relic.atTurnStart();
            relic.atTurnStartPostDraw();
            relic.tips.clear();
            relic.tips.add(new PowerTip(relic.name, relic.description));
            ReflectionHacks.privateMethod(AbstractRelic.class, "initializeTips").invoke(relic);
        } else {
            AbstractRelic newRelic = card.replaceThisCardsRelicWithNewCopy();
            if (!Wiz.adp().relics.contains(newRelic)) {
                activateRelicFromCard(card, newRelic);
            } else {
                //if we got here, then replaceThisCardsRelicWithNewCopy failed somehow
            }
        }

    }
}
