package spireMapOverhaul.zones.Junkyard.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.unique.AddCardToDeckAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;
import spireMapOverhaul.SpireAnniversary6Mod;

import java.util.ArrayList;

public class BuyCardAction extends AbstractGameAction {

    public ArrayList<AbstractCard> cards = new ArrayList<AbstractCard>();
    public int cost = 0;
    private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString(SpireAnniversary6Mod.makeID("Misc"));
    private float startingDuration;

    public BuyCardAction(ArrayList<AbstractCard> cardList, int goldCost){
        cards = cardList;
        cost = goldCost;
        this.duration = Settings.ACTION_DUR_FAST;
        this.startingDuration = Settings.ACTION_DUR_FAST;
    }

    @Override
    public void update() {
        if (this.duration == startingDuration) {
            AbstractDungeon.player.loseGold(cost);
            CardCrawlGame.sound.play("GOLD_GAIN", 0.1F);
            CardGroup tmpGroup = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
            for (AbstractCard c : cards) {
                tmpGroup.addToTop(c);
            }
            AbstractDungeon.gridSelectScreen.open(tmpGroup, 1, false, uiStrings.TEXT[0]);
        }
        else if (!AbstractDungeon.gridSelectScreen.selectedCards.isEmpty()) {
            addToBot(new AddCardToDeckAction(AbstractDungeon.gridSelectScreen.selectedCards.get(0)));
            AbstractDungeon.gridSelectScreen.selectedCards.clear();
            this.isDone = true;
        }
        this.tickDuration();
    }
}
