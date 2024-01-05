package spireMapOverhaul.zones.manasurge.events;

import basemod.abstracts.AbstractCardModifier;
import basemod.abstracts.events.PhasedEvent;
import basemod.abstracts.events.phases.TextPhase;
import basemod.helpers.CardModifierManager;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.EventStrings;
import spireMapOverhaul.SpireAnniversary6Mod;
import spireMapOverhaul.zones.manasurge.ManaSurgeZone;
import spireMapOverhaul.zones.manasurge.modifiers.common.negative.FeebleModifier;
import spireMapOverhaul.zones.manasurge.modifiers.common.negative.FlawedModifier;
import spireMapOverhaul.zones.manasurge.modifiers.common.negative.FragileModifier;
import spireMapOverhaul.zones.manasurge.modifiers.common.negative.HarmfulModifier;
import spireMapOverhaul.zones.manasurge.modifiers.uncommon.negative.BrittleModifier;
import spireMapOverhaul.zones.manasurge.modifiers.uncommon.negative.PowerlessModifier;

import static spireMapOverhaul.zones.manasurge.ManaSurgeZone.COMMON_CHANCE;

public class ManaCycloneEvent extends PhasedEvent {
    public static final String ID = SpireAnniversary6Mod.makeID("ManaCycloneEvent");
    private static final EventStrings eventStrings = CardCrawlGame.languagePack.getEventString(ID);
    private static final String[] DESCRIPTIONS = eventStrings.DESCRIPTIONS;
    private static final String[] OPTIONS = eventStrings.OPTIONS;
    private static final String title = eventStrings.NAME;
    public static final String IMG = SpireAnniversary6Mod.makeImagePath("events/ManaCycloneEvent.png");

    public ManaCycloneEvent() {
        super(ID, title, IMG);

        registerPhase("Start", new TextPhase(DESCRIPTIONS[0])
                .addOption(OPTIONS[0], (i) -> {
                    CardGroup selectedCards = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
                    AbstractDungeon.player.masterDeck.group.stream()
                            .filter(card -> !ManaSurgeZone.hasManaSurgeModifier(card) &&
                                    card.cost != -2 &&
                                    card.type != AbstractCard.CardType.CURSE &&
                                    card.type != AbstractCard.CardType.STATUS)
                            .forEach(selectedCards::addToTop);
                    AbstractCard randomCard = selectedCards.getRandomCard(true);
                    if (randomCard != null) {
                        if (Math.random() < COMMON_CHANCE) {
                            int numberOfCommonModifiers = 4;
                            int selectedModifierIndex = (int) (Math.random() * numberOfCommonModifiers);
                            AbstractCardModifier modifier;
                            switch (selectedModifierIndex) {
                                case 0:
                                    modifier = new FeebleModifier(true);
                                    break;
                                case 1:
                                    modifier = new FlawedModifier(true);
                                    break;
                                case 2:
                                    modifier = new FragileModifier(true);
                                    break;
                                case 3:
                                    modifier = new HarmfulModifier(true);
                                    break;
                                default:
                                    modifier = null;
                                    break;
                            }
                            if (modifier != null) {
                                CardModifierManager.addModifier(randomCard, modifier);
                            }
                        } else {
                            int numberOfUncommonModifiers = 2;
                            int selectedModifierIndex = (int) (Math.random() * numberOfUncommonModifiers);
                            AbstractCardModifier modifier;
                            switch (selectedModifierIndex) {
                                case 0:
                                    modifier = new BrittleModifier(true);
                                    break;
                                case 1:
                                    modifier = new PowerlessModifier(true);
                                    break;
                                default:
                                    modifier = null;
                                    break;
                            }
                            if (modifier != null) {
                                CardModifierManager.addModifier(randomCard, modifier);
                            }
                        }
                    }
                    transitionKey("Reached Inside");
                })
        );



        registerPhase("Reached Inside",new TextPhase(DESCRIPTIONS[1])
                .addOption(OPTIONS[2],(i)->openMap()));

        registerPhase("Walked Through",new TextPhase(DESCRIPTIONS[2])
                .addOption(OPTIONS[2],(i)->openMap()));

        transitionKey("Start");
    }
}