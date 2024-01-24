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
import com.megacrit.cardcrawl.relics.*;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardBrieflyEffect;
import spireMapOverhaul.SpireAnniversary6Mod;
import spireMapOverhaul.util.EventTooltipCreator;
import spireMapOverhaul.zones.manasurge.ManaSurgeZone;
import spireMapOverhaul.zones.manasurge.ui.campfire.EnchantOption;
import spireMapOverhaul.zones.manasurge.vfx.EnchantBlightEffect;

import java.util.ArrayList;

public class ManaCycloneEvent extends PhasedEvent {
    public static final String ID = SpireAnniversary6Mod.makeID("ManaCycloneEvent");
    private static final EventStrings eventStrings = CardCrawlGame.languagePack.getEventString(ID);
    private static final String[] DESCRIPTIONS = eventStrings.DESCRIPTIONS;
    private static final String[] OPTIONS = eventStrings.OPTIONS;
    private static final String title = eventStrings.NAME;
    public static final String IMG = SpireAnniversary6Mod.makeImagePath("events/ManaSurge/ManaCycloneEvent.png");
    public static final String ENCHANT_ID = SpireAnniversary6Mod.makeID("ManaSurge:Enchant");
    public static final String BLIGHTED_ID = SpireAnniversary6Mod.makeID("ManaSurge:Blighted");

    private static final ArrayList<String> relicIds = new ArrayList<>();
    static {
        relicIds.add(FrozenEgg2.ID);
        relicIds.add(MoltenEgg2.ID);
        relicIds.add(ToxicEgg2.ID);
    }

    public static boolean bonusCondition() {
        boolean anyRelicCanSpawn = relicIds.stream().anyMatch(relicId -> !AbstractDungeon.player.hasRelic(relicId) && RelicLibrary.getRelic(relicId).canSpawn());
        long matchingCards = AbstractDungeon.player.masterDeck.group
                .stream()
                .filter(card -> !ManaSurgeZone.hasManaSurgeModifier(card) &&
                        card.cost != -2 &&
                        card.type != AbstractCard.CardType.CURSE &&
                        card.type != AbstractCard.CardType.STATUS)
                .count();
        return anyRelicCanSpawn && matchingCards >= 2;
    }

    public ManaCycloneEvent() {
        super(ID, title, IMG);
        this.noCardsInRewards = true;
        float cardWidth = AbstractCard.IMG_WIDTH;
        float horizontalOffset = cardWidth * 0.75F;

        registerPhase("Start", new TextPhase(DESCRIPTIONS[0])
                .addOption(new TextPhase.OptionInfo(OPTIONS[0], EventTooltipCreator.createRelicForTootlips(ENCHANT_ID, BLIGHTED_ID))
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
                                EnchantOption.TEXT[2],
                                1, false, false, true, false,
                                (cards)->{
                                    for (AbstractCard c : cards) {
                                        ManaSurgeZone.applyPermanentPositiveModifier(c);
                                        CardCrawlGame.sound.play(ManaSurgeZone.ENCHANTBLIGHT_KEY);
                                        AbstractDungeon.topLevelEffectsQueue.add(new EnchantBlightEffect(
                                                Settings.WIDTH / 2.0F - horizontalOffset,
                                                Settings.HEIGHT / 2.0F));
                                        AbstractDungeon.effectList.add(new ShowCardBrieflyEffect(c.makeStatEquivalentCopy(),
                                                Settings.WIDTH / 2.0F - horizontalOffset,
                                                Settings.HEIGHT / 2.0F));
                                    }
                                    CardGroup filteredCards = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
                                    AbstractDungeon.player.masterDeck.group.stream()
                                            .filter(card -> !ManaSurgeZone.hasManaSurgeModifier(card) &&
                                                    card.cost != -2 &&
                                                    card.type != AbstractCard.CardType.CURSE &&
                                                    card.type != AbstractCard.CardType.STATUS)
                                            .forEach(filteredCards::addToTop);

                                    AbstractCard randomCard = filteredCards.getRandomCard(AbstractDungeon.miscRng);
                                    if (randomCard != null) {
                                        ManaSurgeZone.applyPermanentNegativeModifier(randomCard);
                                        AbstractDungeon.topLevelEffectsQueue.add(new EnchantBlightEffect(
                                                Settings.WIDTH / 2.0F + horizontalOffset,
                                                Settings.HEIGHT / 2.0F));
                                        AbstractDungeon.effectList.add(new ShowCardBrieflyEffect(randomCard.makeStatEquivalentCopy(),
                                                Settings.WIDTH / 2.0F + horizontalOffset,
                                                Settings.HEIGHT / 2.0F));
                                        filteredCards.group.remove(randomCard);
                                    }
                                }
                        )
                )
                .addOption(OPTIONS[1], EventTooltipCreator.createRelicForTootlips(BLIGHTED_ID), (i) -> {
                    CardGroup filteredCards = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
                    AbstractDungeon.player.masterDeck.group.stream()
                            .filter(card -> !ManaSurgeZone.hasManaSurgeModifier(card) &&
                                    card.cost != -2 &&
                                    card.type != AbstractCard.CardType.CURSE &&
                                    card.type != AbstractCard.CardType.STATUS)
                            .forEach(filteredCards::addToTop);
                    AbstractCard randomCard1 = filteredCards.getRandomCard(AbstractDungeon.miscRng);
                    filteredCards.removeCard(randomCard1);
                    AbstractCard randomCard2 = filteredCards.getRandomCard(AbstractDungeon.miscRng);

                    CardCrawlGame.sound.play(ManaSurgeZone.ENCHANTBLIGHT_KEY);
                    if (randomCard1 != null) {
                        ManaSurgeZone.applyPermanentNegativeModifier(randomCard1);
                        AbstractDungeon.topLevelEffectsQueue.add(new EnchantBlightEffect(
                                Settings.WIDTH / 2.0F - horizontalOffset,
                                Settings.HEIGHT / 2.0F));
                        AbstractDungeon.effectList.add(new ShowCardBrieflyEffect(randomCard1.makeStatEquivalentCopy(),
                                Settings.WIDTH / 2.0F - horizontalOffset,
                                Settings.HEIGHT / 2.0F));
                    }
                    if (randomCard2 != null) {
                        ManaSurgeZone.applyPermanentNegativeModifier(randomCard2);
                        AbstractDungeon.topLevelEffectsQueue.add(new EnchantBlightEffect(
                                Settings.WIDTH / 2.0F + horizontalOffset,
                                Settings.HEIGHT / 2.0F));
                        AbstractDungeon.effectList.add(new ShowCardBrieflyEffect(randomCard2.makeStatEquivalentCopy(),
                                Settings.WIDTH / 2.0F + horizontalOffset,
                                Settings.HEIGHT / 2.0F));
                    }

                    transitionKey("Walked Through");
                })
                .addOption(OPTIONS[2],(i)->openMap()));


        registerPhase("Reached Inside",new TextPhase(DESCRIPTIONS[1])
                .addOption(OPTIONS[2],(i)->openMap()));

        registerPhase("Walked Through",new TextPhase(DESCRIPTIONS[2])
                .addOption(OPTIONS[3],(i) -> {
                    ArrayList<String> availableRelics = new ArrayList<>();
                    for (String relicId : relicIds) {
                        if (!AbstractDungeon.player.hasRelic(relicId) && RelicLibrary.getRelic(relicId).canSpawn()) {
                            availableRelics.add(relicId);
                        }
                    }
                    String chosenRelicId;
                    if (!availableRelics.isEmpty()) {
                        chosenRelicId = availableRelics.get(AbstractDungeon.miscRng.random(availableRelics.size() - 1));
                    } else {
                        chosenRelicId = Circlet.ID;
                    }
                    AbstractRelic chosenRelic = RelicLibrary.getRelic(chosenRelicId).makeCopy();
                    AbstractDungeon.getCurrRoom().rewards.clear();
                    AbstractDungeon.getCurrRoom().addRelicToRewards(chosenRelic);
                    AbstractDungeon.uncommonRelicPool.removeIf(s -> s.equals(chosenRelicId));
                    AbstractDungeon.getCurrRoom().phase = AbstractRoom.RoomPhase.COMPLETE;
                    AbstractDungeon.combatRewardScreen.open();

                    transitionKey("Walked Through Cont.");
                }));

        registerPhase("Walked Through Cont.",new TextPhase(DESCRIPTIONS[3])
                .addOption(OPTIONS[2],(i)->openMap()));

        transitionKey("Start");
    }
}