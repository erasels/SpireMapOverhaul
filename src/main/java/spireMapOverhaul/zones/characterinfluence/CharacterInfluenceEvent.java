package spireMapOverhaul.zones.characterinfluence;

import basemod.abstracts.events.PhasedEvent;
import basemod.abstracts.events.phases.CombatPhase;
import basemod.abstracts.events.phases.TextPhase;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.MonsterHelper;
import com.megacrit.cardcrawl.helpers.RelicLibrary;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.vfx.UpgradeShineEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardBrieflyEffect;
import spireMapOverhaul.BetterMapGenerator;
import spireMapOverhaul.SpireAnniversary6Mod;
import spireMapOverhaul.abstracts.AbstractZone;
import spireMapOverhaul.patches.ZonePatches;

import java.util.ArrayList;

import static spireMapOverhaul.SpireAnniversary6Mod.makeID;

public class CharacterInfluenceEvent extends PhasedEvent {
    public static final String ID = makeID("CharacterVisit");
    private static final EventStrings eventStrings = CardCrawlGame.languagePack.getEventString(ID);
    private static final String[] DESCRIPTIONS = eventStrings.DESCRIPTIONS;
    private static final String[] OPTIONS = eventStrings.OPTIONS;
    private static final String title = eventStrings.NAME;
    public static boolean didObtainRelics = false;

    public static boolean bonusCondition() {
        return true;
    }

    public CharacterInfluenceEvent() {
        super(ID, title, "images/events/theNest.jpg");  //change title?

        //set up event
        registerPhase(0, new TextPhase(DESCRIPTIONS[0] + "the Visitor" + DESCRIPTIONS[1]) {
            @Override
            public String getBody() {
                if (ZonePatches.currentZone() instanceof CharacterInfluenceZone && ZonePatches.currentZone() != null && ((CharacterInfluenceZone) ZonePatches.currentZone()).classInfluence != null) {
                    return DESCRIPTIONS[0] + ((CharacterInfluenceZone) ZonePatches.currentZone()).classInfluence.title + DESCRIPTIONS[1];
                } else {
                    return super.getBody();
                }
            }
        }.addOption(OPTIONS[0], (i)->transitionKey("GetRelic")) ); //get class name

        registerPhase("GetRelic", new TextPhase(DESCRIPTIONS[2]) {
                    @Override
                    public void update() {
                        if (didObtainRelics) return;
                        ArrayList<String> relicStrings = new ArrayList<>();
                        if (ZonePatches.currentZone() instanceof CharacterInfluenceZone && ZonePatches.currentZone() != null && ((CharacterInfluenceZone) ZonePatches.currentZone()).classInfluence != null) {
                            relicStrings = ((CharacterInfluenceZone) ZonePatches.currentZone()).classInfluence.getStartingRelics();
                        }
                        for (String relicID : relicStrings) {
                            AbstractDungeon.getCurrRoom().spawnRelicAndObtain(Settings.WIDTH / 2.0F, Settings.HEIGHT / 2.0F, RelicLibrary.getRelic(relicID));
                        }
                        didObtainRelics = true;
                    }
                }.addOption(OPTIONS[1], (i) -> this.openMap())
        );

        /*
        //template
        //registerPhase(0, new TextPhase(DESCRIPTIONS[0]).addOption(OPTIONS[0], (i)->transitionKey("Brazil")).addOption(OPTIONS[1], (i)->transitionKey("Japan")));
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

        registerPhase(1, new CombatPhase(MonsterHelper.CULTIST_ENC).setNextKey("Japan"));*/

        transitionKey(0); //starting point
    }
}
