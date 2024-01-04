package spireMapOverhaul.zones.brokenSpace.relics;

import basemod.helpers.CardModifierManager;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.relics.SneckoEye;
import spireMapOverhaul.zones.brokenSpace.cardmods.UnreadableCardMod;

import static spireMapOverhaul.util.Wiz.adp;

public class BrokenSneckoEye extends BrokenRelic {
    public static final String ID = "BrokenSneckoEye";
    public static final int AMOUNT = 1;

    public BrokenSneckoEye() {
        super(ID, RelicTier.SPECIAL, LandingSound.CLINK, SneckoEye.ID);

    }

    @Override
    public void atTurnStart() {
        for (int i = 0; i < AMOUNT; i++) {
            AbstractCard c = adp().masterDeck.getRandomCard(true).makeCopy();
            CardModifierManager.addModifier(c, new UnreadableCardMod());
            addToBot(new MakeTempCardInHandAction(c, true));
        }
        super.atTurnStart();
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0] + AMOUNT + DESCRIPTIONS[1];
    }
}
