package spireMapOverhaul.zones.hailstorm.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rewards.RewardItem;
import com.megacrit.cardcrawl.screens.CardRewardScreen;
import spireMapOverhaul.zones.hailstorm.relics.Flint;

import java.util.ArrayList;

import static spireMapOverhaul.util.Wiz.atb;

@SpirePatch(clz = CardRewardScreen.class, method = "onClose")


public class CardRewardScreenFlintDreamCatcherInteractionPatch {

    public static boolean trigger = false;
    @SpirePostfixPatch
    public static void postfix(CardRewardScreen __instance) {
        if (trigger) {
            atb(new AbstractGameAction() {
                @Override
                public void update() {
                    ArrayList<AbstractCard> rewardCards = AbstractDungeon.getRewardCards();
                    if (rewardCards != null && !rewardCards.isEmpty()) {
                        AbstractDungeon.cardRewardScreen.open(rewardCards, (RewardItem)null, "TEXT[0]");
                    }
                    trigger = false;

                    isDone=true;
                }
            });

        }
    }

}
