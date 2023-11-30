package spireMapOverhaul.events;

import basemod.abstracts.events.PhasedEvent;
import basemod.abstracts.events.phases.CombatPhase;
import basemod.abstracts.events.phases.TextPhase;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.MonsterHelper;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.vfx.UpgradeShineEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardBrieflyEffect;

import static spireMapOverhaul.SpireAnniversary6Mod.makeID;

public class CoolExampleEvent extends PhasedEvent {
    public static final String ID = makeID("Example");
    private static final EventStrings eventStrings = CardCrawlGame.languagePack.getEventString(ID);
    private static final String[] DESCRIPTIONS = eventStrings.DESCRIPTIONS;
    private static final String[] OPTIONS = eventStrings.OPTIONS;
    private static final String title = eventStrings.NAME;

    public CoolExampleEvent() {
        super(ID, title, "images/events/theNest.jpg");

        //set up event
        registerPhase(0, new TextPhase(DESCRIPTIONS[0]).addOption(OPTIONS[0], (i)->transitionKey("Brazil")).addOption(OPTIONS[1], (i)->transitionKey("Japan")));
        registerPhase("Brazil", new TextPhase(DESCRIPTIONS[1]).addOption(OPTIONS[2], (i)->transitionKey("Antarctica")).addOption(OPTIONS[5], (i)->transitionKey(1)));
        registerPhase("Japan", new TextPhase(DESCRIPTIONS[2]).addOption(OPTIONS[0], (i)->transitionKey("Brazil")).addOption(OPTIONS[3], (i)->transitionKey("This The End")));
        registerPhase("Antarctica", new TextPhase(DESCRIPTIONS[3]).addOption(OPTIONS[3], (i)->transitionKey("This The End"))
                .addOption(new TextPhase.OptionInfo(AbstractDungeon.player.masterDeck.hasUpgradableCards() ? "Upgrade Option Test" : "Can't Upgrade").enabledCondition(AbstractDungeon.player.masterDeck::hasUpgradableCards), (i)->transitionKey("UPGRADE")));
        registerPhase("This The End", new TextPhase(DESCRIPTIONS[4]).addOption(OPTIONS[4], (t)->this.openMap()));

        registerPhase("UPGRADE", new TextPhase("Card Select Example (Upgrading)")
                {
                    @Override
                    public void update() {
                        if (!AbstractDungeon.isScreenUp && !AbstractDungeon.gridSelectScreen.selectedCards.isEmpty()) {
                            AbstractCard c = AbstractDungeon.gridSelectScreen.selectedCards.get(0);
                            c.upgrade();
                            AbstractDungeon.player.bottledCardUpgradeCheck(c);
                            AbstractDungeon.effectsQueue.add(new ShowCardBrieflyEffect(c.makeStatEquivalentCopy()));
                            AbstractDungeon.topLevelEffects.add(new UpgradeShineEffect((float) Settings.WIDTH / 2.0F, (float)Settings.HEIGHT / 2.0F));
                            AbstractDungeon.gridSelectScreen.selectedCards.clear();
                            transitionKey("This The End");
                        }
                    }
                }.addOption("Upgrade a card", (i)->{
                    AbstractDungeon.getCurrRoom().phase = AbstractRoom.RoomPhase.COMPLETE; //If it's not over don't do this
                    AbstractDungeon.gridSelectScreen.open(AbstractDungeon.player.masterDeck.getUpgradableCards(), 1, OPTIONS[2], true, false, false, false);
                }));

        registerPhase(1, new CombatPhase(MonsterHelper.CULTIST_ENC).setNextKey("Japan"));

        transitionKey(0); //starting point
    }
}
