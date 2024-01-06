package spireMapOverhaul.zones.manasurge.events;

import basemod.abstracts.events.PhasedEvent;
import basemod.abstracts.events.phases.TextPhase;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.RelicLibrary;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardBrieflyEffect;
import spireMapOverhaul.SpireAnniversary6Mod;
import spireMapOverhaul.zones.manasurge.ManaSurgeZone;

public class ManaCycloneEvent extends PhasedEvent {
    public static final String ID = SpireAnniversary6Mod.makeID("ManaCycloneEvent");
    private static final EventStrings eventStrings = CardCrawlGame.languagePack.getEventString(ID);
    private static final String[] DESCRIPTIONS = eventStrings.DESCRIPTIONS;
    private static final String[] OPTIONS = eventStrings.OPTIONS;
    private static final String title = eventStrings.NAME;
    public static final String IMG = SpireAnniversary6Mod.makeImagePath("events/ManaSurge/ManaCycloneEvent.png");
    private AbstractRelic relicMetric = null;


    public ManaCycloneEvent() {
        super(ID, title, IMG);

        registerPhase("Start", new TextPhase(DESCRIPTIONS[0])
                .addOption(new TextPhase.OptionInfo(OPTIONS[0])
                        .cardSelectOption("Reached Inside",
                                ()->{
                                    CardGroup filteredCards = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
                                    AbstractDungeon.player.masterDeck.group.stream()
                                            .filter(card -> !ManaSurgeZone.hasManaSurgeModifier(card) &&
                                                    card.cost != -2 &&
                                                    card.type != AbstractCard.CardType.CURSE &&
                                                    card.type != AbstractCard.CardType.STATUS)
                                            .forEach(filteredCards::addToTop);
                                    return filteredCards;
                                },
                                "Select a card to enchant.",
                                1, false, false, true, false,
                                (cards)->{
                                    for (AbstractCard c : cards) {
                                        ManaSurgeZone.applyPermanentPositiveModifier(c);
                                        AbstractDungeon.effectList.add(new ShowCardBrieflyEffect(c.makeStatEquivalentCopy()));
                                    }

                                    CardGroup filteredCards = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
                                    AbstractDungeon.player.masterDeck.group.stream()
                                            .filter(card -> !ManaSurgeZone.hasManaSurgeModifier(card) &&
                                                    card.cost != -2 &&
                                                    card.type != AbstractCard.CardType.CURSE &&
                                                    card.type != AbstractCard.CardType.STATUS)
                                            .forEach(filteredCards::addToTop);

                                    AbstractCard randomCard = filteredCards.getRandomCard(true);
                                    if (randomCard != null) {
                                        ManaSurgeZone.applyPermanentNegativeModifier(randomCard);
                                        AbstractDungeon.effectList.add(new ShowCardBrieflyEffect(randomCard.makeStatEquivalentCopy()));
                                        filteredCards.group.remove(randomCard);
                                    }
                                }
                        )
                )
                .addOption(OPTIONS[1],(i) -> {
                    int numberOfRelics = 6;
                    int selectedRelicIndex = (int) (Math.random() * numberOfRelics);
                    switch (selectedRelicIndex) {
                        case 0:
                            if (AbstractDungeon.player.hasRelic("Bottled Flame")) {
                                this.relicMetric = RelicLibrary.getRelic("Circlet").makeCopy();
                            } else {
                                this.relicMetric = RelicLibrary.getRelic("Bottled Flame").makeCopy();
                            }
                            AbstractDungeon.getCurrRoom().spawnRelicAndObtain((float)(Settings.WIDTH / 2), (float)(Settings.HEIGHT / 2), this.relicMetric);
                            break;
                        case 1:
                            if (AbstractDungeon.player.hasRelic("Bottled Tornado")) {
                                this.relicMetric = RelicLibrary.getRelic("Circlet").makeCopy();
                            } else {
                                this.relicMetric = RelicLibrary.getRelic("Bottled Tornado").makeCopy();
                            }
                            AbstractDungeon.getCurrRoom().spawnRelicAndObtain((float)(Settings.WIDTH / 2), (float)(Settings.HEIGHT / 2), this.relicMetric);
                            break;
                        case 2:
                            if (AbstractDungeon.player.hasRelic("Bottled Lightning")) {
                                this.relicMetric = RelicLibrary.getRelic("Circlet").makeCopy();
                            } else {
                                this.relicMetric = RelicLibrary.getRelic("Bottled Lightning").makeCopy();
                            }
                            AbstractDungeon.getCurrRoom().spawnRelicAndObtain((float)(Settings.WIDTH / 2), (float)(Settings.HEIGHT / 2), this.relicMetric);
                            break;
                        case 3:
                            if (AbstractDungeon.player.hasRelic("Frozen Egg 2")) {
                                this.relicMetric = RelicLibrary.getRelic("Circlet").makeCopy();
                            } else {
                                this.relicMetric = RelicLibrary.getRelic("Frozen Egg 2").makeCopy();
                            }
                            AbstractDungeon.getCurrRoom().spawnRelicAndObtain((float)(Settings.WIDTH / 2), (float)(Settings.HEIGHT / 2), this.relicMetric);
                            break;
                        case 4:
                            if (AbstractDungeon.player.hasRelic("Molten Egg 2")) {
                                this.relicMetric = RelicLibrary.getRelic("Circlet").makeCopy();
                            } else {
                                this.relicMetric = RelicLibrary.getRelic("Molten Egg 2").makeCopy();
                            }
                            AbstractDungeon.getCurrRoom().spawnRelicAndObtain((float)(Settings.WIDTH / 2), (float)(Settings.HEIGHT / 2), this.relicMetric);
                            break;
                        case 5:
                            if (AbstractDungeon.player.hasRelic("Toxic Egg 2")) {
                                this.relicMetric = RelicLibrary.getRelic("Circlet").makeCopy();
                            } else {
                                this.relicMetric = RelicLibrary.getRelic("Toxic Egg 2").makeCopy();
                            }
                            AbstractDungeon.getCurrRoom().spawnRelicAndObtain((float)(Settings.WIDTH / 2), (float)(Settings.HEIGHT / 2), this.relicMetric);
                            break;
                    }
                    CardGroup filteredCards = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
                    AbstractDungeon.player.masterDeck.group.stream()
                            .filter(card -> !ManaSurgeZone.hasManaSurgeModifier(card) &&
                                    card.cost != -2 &&
                                    card.type != AbstractCard.CardType.CURSE &&
                                    card.type != AbstractCard.CardType.STATUS)
                            .forEach(filteredCards::addToTop);
                    AbstractCard randomCard1 = filteredCards.getRandomCard(true);
                    filteredCards.removeCard(randomCard1);
                    AbstractCard randomCard2 = filteredCards.getRandomCard(true);
                    if (randomCard1 != null) {
                        ManaSurgeZone.applyPermanentNegativeModifier(randomCard1);
                        AbstractDungeon.effectList.add(new ShowCardBrieflyEffect(randomCard1.makeStatEquivalentCopy()));
                    }
                    if (randomCard2 != null) {
                        ManaSurgeZone.applyPermanentNegativeModifier(randomCard2);
                        AbstractDungeon.effectList.add(new ShowCardBrieflyEffect(randomCard2.makeStatEquivalentCopy()));
                    }
                    transitionKey("Walked Through");
                })
                .addOption(OPTIONS[2],(i)->openMap())
        );


        registerPhase("Reached Inside",new TextPhase(DESCRIPTIONS[1])
                .addOption(OPTIONS[2],(i)->openMap()));

        registerPhase("Walked Through",new TextPhase(DESCRIPTIONS[2])
                .addOption(OPTIONS[2],(i)->openMap()));

        transitionKey("Start");
    }
}