package spireMapOverhaul.zones.brokenSpace.relics;

import basemod.helpers.CardModifierManager;
import basemod.helpers.ScreenPostProcessorManager;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.relics.SneckoEye;
import spireMapOverhaul.zones.brokenSpace.DrawCardWithCallbackAction;
import spireMapOverhaul.zones.brokenSpace.cardmods.UnreadableCardMod;


import static com.megacrit.cardcrawl.dungeons.AbstractDungeon.player;

public class BrokenSneckoEye extends BrokenRelic {
    public static final String ID = "BrokenSneckoEye";
    public static final int AMOUNT = 2;

    public BrokenSneckoEye() {
        super(ID, RelicTier.SPECIAL, LandingSound.CLINK, SneckoEye.ID);

    }

    @Override
    public void atTurnStart() {
        addToBot(new DrawCardWithCallbackAction(AMOUNT, (cards) -> {
            for (AbstractCard card : cards) {
                CardModifierManager.addModifier(card, new UnreadableCardMod());
            }
        }));
        super.atTurnStart();
    }

//    @Override
//    public void onPlayerEndTurn() {
//        for (AbstractCard card : player.hand.group) {
//            CardModifierManager.removeModifiersById(card, UnreadableCardMod.ID, true);
//        }
//        for (AbstractCard card : player.drawPile.group) {
//            CardModifierManager.removeModifiersById(card, UnreadableCardMod.ID, true);
//        }
//        for (AbstractCard card : player.discardPile.group) {
//            CardModifierManager.removeModifiersById(card, UnreadableCardMod.ID, true);
//        }
//
//    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0] + AMOUNT + DESCRIPTIONS[1];
    }
}
