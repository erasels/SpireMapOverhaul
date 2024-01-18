package spireMapOverhaul.zones.Junkyard;

import basemod.abstracts.events.PhasedEvent;
import basemod.abstracts.events.phases.TextPhase;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.ui.buttons.LargeDialogOptionButton;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndObtainEffect;
import spireMapOverhaul.SpireAnniversary6Mod;

import static spireMapOverhaul.SpireAnniversary6Mod.makeID;

public class CollectingSpree extends PhasedEvent {
    public static final String ID = makeID("CollectingSpree");
    private static final EventStrings eventStrings = CardCrawlGame.languagePack.getEventString(ID);
    private static final String[] DESCRIPTIONS = eventStrings.DESCRIPTIONS;
    private static final String[] OPTIONS = eventStrings.OPTIONS;
    private static final String title = eventStrings.NAME;
    private int chance = 20;
    private int cardsCollected = 0;

    public static boolean bonusCondition() {
        return true;
    }

    public CollectingSpree() {
        super(ID, title, SpireAnniversary6Mod.makeImagePath("events/Junkyard/Junkheap.png"));

        registerPhase(0, new TextPhase(DESCRIPTIONS[0]) {
            @Override
            public void update() {
                if (!AbstractDungeon.isScreenUp && !AbstractDungeon.gridSelectScreen.selectedCards.isEmpty()) {
                    AbstractCard c = AbstractDungeon.gridSelectScreen.selectedCards.get(0);
                    AbstractDungeon.effectList.add(new ShowCardAndObtainEffect(c.makeStatEquivalentCopy(), (float)Settings.WIDTH / 2.0F, (float)Settings.HEIGHT / 2.0F));
                    AbstractDungeon.gridSelectScreen.selectedCards.clear();
                    cardsCollected++;
                    if (cardsCollected >= 10){
                        transitionKey("Exhausted");
                    }
                }
            }
        }.addOption(OPTIONS[0], (i) ->{
            chance += 20;
            CardGroup g = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
                for (AbstractCard card : AbstractDungeon.getRewardCards()){
                    g.addToTop(card);
                }
            AbstractDungeon.gridSelectScreen.open(g, 1, OPTIONS[4], false, false, false, false);
            for (LargeDialogOptionButton o : imageEventText.optionList) {
            }
            if (chance <= 0){
                this.imageEventText.updateDialogOption(1, OPTIONS[5]);
            }
            else {
                this.imageEventText.updateDialogOption(1, OPTIONS[1] + (100-chance) + OPTIONS[2] + chance + OPTIONS[3]);
            }
            this.updateDialog();
        }).addOption(OPTIONS[1] + (100-chance) + OPTIONS[2] + chance + OPTIONS[3], (i) ->{
            int rand = AbstractDungeon.cardRng.random(0, 100);
            if (rand < chance){
                chance -= 20;
                if (chance < 0){
                    chance = 0;
                }
                CardGroup g = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
                for (AbstractCard card : AbstractDungeon.getRewardCards()){
                    g.addToTop(card);
                }
                AbstractDungeon.gridSelectScreen.open(g, 1, OPTIONS[3], false, false, false, false);
                for (LargeDialogOptionButton o : imageEventText.optionList){
                }
                if (chance <= 0){
                    this.imageEventText.updateDialogOption(1, OPTIONS[5]);
                }
                else {
                    this.imageEventText.updateDialogOption(1, OPTIONS[1] + (100-chance) + OPTIONS[2] + chance + OPTIONS[3]);
                }
                this.updateDialog();
                this.update();
            }
            else {
                AbstractDungeon.getCurrRoom().phase = AbstractRoom.RoomPhase.COMPLETE; //If it's not over don't do this
                transitionKey("Leave"); //starting point
            }
        }));
        registerPhase("Leave", new TextPhase(DESCRIPTIONS[1]).addOption(OPTIONS[5], (t)->this.openMap()));
        registerPhase("Exhausted", new TextPhase(DESCRIPTIONS[2]).addOption(OPTIONS[5], (t)->this.openMap()));

        transitionKey(0); //starting point
    }

}
