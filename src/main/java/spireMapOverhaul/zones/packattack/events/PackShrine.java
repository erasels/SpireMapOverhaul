package spireMapOverhaul.zones.packattack.events;

import basemod.ReflectionHacks;
import com.badlogic.gdx.math.MathUtils;
import com.evacipated.cardcrawl.modthespire.Loader;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.AbstractImageEvent;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import com.megacrit.cardcrawl.vfx.RainingGoldEffect;
import com.megacrit.cardcrawl.vfx.cardManip.PurgeCardEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndObtainEffect;
import spireMapOverhaul.SpireAnniversary6Mod;
import spireMapOverhaul.zones.packattack.PackAttackZone;

import java.util.*;
import java.util.stream.Collectors;

public class PackShrine extends AbstractImageEvent {
    public static final String ID = SpireAnniversary6Mod.makeID("PackShrine");
    private static final EventStrings eventStrings = CardCrawlGame.languagePack.getEventString(ID);
    private static final String NAME = eventStrings.NAME;
    private static final String[] DESCRIPTIONS = eventStrings.DESCRIPTIONS;
    private static final String[] OPTIONS = eventStrings.OPTIONS;
    public static final String IMG_1 = SpireAnniversary6Mod.makeImagePath("events/PackAttack/PackShrine1.png");
    public static final String IMG_2 = SpireAnniversary6Mod.makeImagePath("events/PackAttack/PackShrine2.png");
    private static final int CARDS = 20;
    private static final int COMMON_GOLD = 30;
    private static final int A15_COMMON_GOLD = 20;
    private static final int UNCOMMON_GOLD = 60;
    private static final int A15_UNCOMMON_GOLD = 50;
    private static final int RARE_GOLD = 120;
    private static final int A15_RARE_GOLD = 100;

    private int commonGold;
    private int uncommonGold;
    private int rareGold;
    private boolean pickCard = false;
    private boolean remove = false;

    private int screenNum = 0;

    public PackShrine() {
        super(NAME, DESCRIPTIONS[0], IMG_1);
        if (MathUtils.randomBoolean()) {
            this.imageEventText.loadImage(IMG_2);
        }

        if (!Loader.isModLoaded("anniv5")) {
            SpireAnniversary6Mod.logger.error("This event requires Packmaster to be loaded");
            this.imageEventText.setDialogOption(OPTIONS[2]);
            return;
        }

        this.commonGold = AbstractDungeon.ascensionLevel >= 15 ? A15_COMMON_GOLD : COMMON_GOLD;
        this.uncommonGold = AbstractDungeon.ascensionLevel >= 15 ? A15_UNCOMMON_GOLD : UNCOMMON_GOLD;
        this.rareGold = AbstractDungeon.ascensionLevel >= 15 ? A15_RARE_GOLD : RARE_GOLD;

        this.imageEventText.setDialogOption(OPTIONS[0].replace("{0}", CARDS + ""));
        this.imageEventText.setDialogOption(OPTIONS[1]);
        this.imageEventText.setDialogOption(OPTIONS[2]);
    }

    @Override
    public void onEnterRoom() {
        CardCrawlGame.music.playTempBGM("SHRINE");
    }

    @Override
    public void update() {
        super.update();
        if (this.pickCard && !AbstractDungeon.isScreenUp && !AbstractDungeon.gridSelectScreen.selectedCards.isEmpty()) {
            AbstractCard c = AbstractDungeon.gridSelectScreen.selectedCards.get(0);
            if (this.remove) {
                AbstractDungeon.topLevelEffects.add(new PurgeCardEffect(c, (float) (Settings.WIDTH / 2), (float) (Settings.HEIGHT / 2)));
                AbstractDungeon.player.masterDeck.removeCard(c);
                int gold = c.rarity == AbstractCard.CardRarity.COMMON ? this.commonGold
                        : c.rarity == AbstractCard.CardRarity.UNCOMMON ? this.uncommonGold
                        : c.rarity == AbstractCard.CardRarity.RARE ? this.rareGold
                        : 5;
                AbstractDungeon.effectList.add(new RainingGoldEffect(gold));
                AbstractDungeon.player.gainGold(gold);
                logMetric(ID, "Offer", null, Collections.singletonList(c.cardID), null, null, null, null, null, 0, 0, 0, 0, gold, 0);
                this.imageEventText.updateBodyText(gold == 0 ? DESCRIPTIONS[2] : DESCRIPTIONS[3]);
                this.screenNum = 1;
                this.imageEventText.updateDialogOption(0, OPTIONS[2]);
                this.imageEventText.clearRemainingOptions();
            }
            else {
                AbstractDungeon.effectList.add(new ShowCardAndObtainEffect(c, (float)Settings.WIDTH / 2.0F, (float)Settings.HEIGHT / 2.0F));
                logMetricObtainCard(ID, "Learn", c);
            }

            AbstractDungeon.gridSelectScreen.selectedCards.clear();
            this.pickCard = false;
        }
    }

    @Override
    protected void buttonEffect(int buttonPressed) {
        if (!Loader.isModLoaded("anniv5")) {
            this.openMap();
            return;
        }
        switch (screenNum) {
            case 0:
                switch (buttonPressed) {
                    case 0: // Learn
                        this.imageEventText.updateBodyText(DESCRIPTIONS[1]);
                        this.screenNum = 1;
                        this.imageEventText.updateDialogOption(0, OPTIONS[2]);
                        this.imageEventText.clearRemainingOptions();
                        this.pickCard = true;
                        this.getCardsAndOpenSelectScreen();
                        break;
                    case 1: // Offer
                        if (CardGroup.getGroupWithoutBottledCards(AbstractDungeon.player.masterDeck.getPurgeableCards()).size() > 0) {
                            this.pickCard = true;
                            this.remove = true;
                            AbstractDungeon.gridSelectScreen.open(CardGroup.getGroupWithoutBottledCards(AbstractDungeon.player.masterDeck.getPurgeableCards()), 1, OPTIONS[4], false, false, false, true);
                        }
                        break;
                    default: // Leave
                        this.imageEventText.updateBodyText(DESCRIPTIONS[4]);
                        this.screenNum = 1;
                        this.imageEventText.updateDialogOption(0, OPTIONS[2]);
                        this.imageEventText.clearRemainingOptions();
                        logMetricIgnored(ID);
                        break;
                }
                break;
            default:
                this.openMap();
                break;
        }
    }

    private void getCardsAndOpenSelectScreen() {
        ArrayList<AbstractCard> allCards = this.getAllPackmasterCards();
        ArrayList<AbstractCard> commons = allCards.stream().filter(c -> c.rarity == AbstractCard.CardRarity.COMMON).collect(Collectors.toCollection(ArrayList::new));
        ArrayList<AbstractCard> uncommons = allCards.stream().filter(c -> c.rarity == AbstractCard.CardRarity.UNCOMMON).collect(Collectors.toCollection(ArrayList::new));
        ArrayList<AbstractCard> rares = allCards.stream().filter(c -> c.rarity == AbstractCard.CardRarity.RARE).collect(Collectors.toCollection(ArrayList::new));
        CardGroup group = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
        for(int i = 0; i < CARDS; ++i) {
            AbstractCard.CardRarity rarity = AbstractDungeon.rollRarity();
            ArrayList<AbstractCard> list = rarity == AbstractCard.CardRarity.RARE ? rares : rarity == AbstractCard.CardRarity.UNCOMMON ? uncommons : commons;
            AbstractCard card = list.get(AbstractDungeon.cardRng.random(list.size() - 1)).makeCopy();
            boolean containsDupe = true;

            while(containsDupe) {
                containsDupe = false;

                for (AbstractCard c : group.group) {
                    if (c.cardID.equals(card.cardID)) {
                        containsDupe = true;
                        rarity = AbstractDungeon.rollRarity();
                        list = rarity == AbstractCard.CardRarity.RARE ? rares : rarity == AbstractCard.CardRarity.UNCOMMON ? uncommons : commons;
                        card = list.get(AbstractDungeon.cardRng.random(list.size() - 1)).makeCopy();
                        break;
                    }
                }
            }

            if (group.contains(card)) {
                i--;
            } else {
                for (AbstractRelic r : AbstractDungeon.player.relics) {
                    r.onPreviewObtainCard(card);
                }
                group.addToBottom(card);
            }
        }

        for (AbstractCard c : group.group) {
            UnlockTracker.markCardAsSeen(c.cardID);
        }

        AbstractDungeon.gridSelectScreen.open(group, 1, OPTIONS[3], false);
    }

    @SuppressWarnings("unchecked")
    private ArrayList<AbstractCard> getAllPackmasterCards() {
        List<AbstractCard.CardRarity> validRarities = Arrays.asList(AbstractCard.CardRarity.COMMON, AbstractCard.CardRarity.UNCOMMON, AbstractCard.CardRarity.RARE);
        ArrayList<Object> allPacks = new ArrayList<>(ReflectionHacks.getPrivateStatic(PackAttackZone.anniv5, "allPacks"));
        return allPacks
                .stream()
                .map(pack -> {
                    try {
                        return (ArrayList<AbstractCard>)ReflectionHacks.getCachedField(PackAttackZone.abstractCardPack, "cards").get(pack);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                        throw new RuntimeException("Error getting cards for Pack Shrine event");
                    }
                })
                .flatMap(Collection::stream)
                .filter(c -> validRarities.contains(c.rarity))
                .collect(Collectors.toCollection(ArrayList::new));
    }
}
