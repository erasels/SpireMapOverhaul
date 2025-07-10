package spireMapOverhaul.zones.humidity.cards.powerelic.implementation;

import basemod.ReflectionHacks;
import basemod.abstracts.CustomSavable;
import basemod.patches.com.megacrit.cardcrawl.screens.compendium.CardLibraryScreen.NoCompendium;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.evacipated.cardcrawl.mod.stslib.cards.interfaces.OnObtainCard;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.RelicLibrary;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.relics.Circlet;
import com.megacrit.cardcrawl.screens.SingleCardViewPopup;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import spireMapOverhaul.abstracts.AbstractSMOCard;
import spireMapOverhaul.util.Wiz;
import spireMapOverhaul.zones.humidity.HumidityZone;
import spireMapOverhaul.zones.humidity.cards.powerelic.PowerelicAllowlist;
import spireMapOverhaul.zones.humidity.cards.powerelic.implementation.patches.CardedRelicSaveData;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static spireMapOverhaul.SpireAnniversary6Mod.makeID;

@NoCompendium
public class PowerelicCard extends AbstractSMOCard implements OnObtainCard, CustomSavable<CardedRelicSaveData> {

    public static final Logger logger = LogManager.getLogger("anniv6:Powerelic");

    public static final String ID = makeID(PowerelicCard.class.getSimpleName());
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

    public AbstractRelic capturedRelic = null;

    public boolean cardIsFromCardReward = false;

    //DO NOT USE THESE CONSTRUCTORS except to add a PowerelicCard to the compendium.
    //Other cards and relics should use one of the static factory methods with the appropriate side effects.
    public PowerelicCard() {
        super(ID, HumidityZone.ID, 1, CardType.POWER, CardRarity.SPECIAL, AbstractCard.CardTarget.SELF);
        rawDescription = cardStrings.DESCRIPTION;
    }

    public PowerelicCard(final String cardID, final int cost, final CardType type, final CardRarity rarity, final CardTarget target) {
        super(cardID, cost, type, rarity, target);
        rawDescription = cardStrings.DESCRIPTION;
    }

    //USE THESE INSTEAD.
    public static PowerelicCard fromActiveRelic(AbstractRelic relic) {
        //called only during the powerelic event
        PowerelicCard card = new PowerelicCard();
        card.setRelicInfoForNewlyConvertedCard(relic);
        return card;
    }

    public static PowerelicCard fromCopy(AbstractRelic relic) {
        //copy can be permanent (from master deck) or temporary (from duplication effect)
        PowerelicCard card = new PowerelicCard();
        card.setRelicInfoForCopiedCard(relic);
        return card;
    }

    public static PowerelicCard fromViolescentShard(AbstractRelic relic) {
        //called only from card rewards.
        //Violescent Shard swaps an existing card reward for a brand new PowerelicCard.
        //The game is expecting the card reward function to return the *master recording*
        //and will automatically produce a *new copy* of the card later, which will lead to a fromCopy call.
        //In order to connect the relic to the new copy, we need to set this card's capturedRelic field
        //but leave the relic itself untouched, as the relic will stick to the first copy it is assigned to.
        PowerelicCard card = new PowerelicCard();
        card.capturedRelic = relic;
        return card;
    }


    @Override
    public void upp() {
        upgradeBaseCost(0);
    }

    ////for "ESSENTIAL-EQUIP" relics:
    //run onUnequip when the relic is first converted to a card and removed from the relics list
    //run onEquip when the relic is played (in addition to everything else that happens)
    //run onUnequip when the relic is deactivated at start of next combat
    //if the card is removed from the deck *and* the relic is currently activated, run loseRelic (which will automatically onUnequip it for us)
    //      if the relic isn't currently activated, it was already unequipped and removed from the player relic list
    //if the card is duplicated, duplicate the relic and do nothing else (wait until the card is played)
    //do nothing special if a saved game is loaded, regardless of "activated" status

    ////for all other relics, including "NONESSENTIAL-EQUIP" relics and relics which do not override OnEquip:
    //do nothing when the relic is first converted to card (its onEquip setup code was already run, and we do not need to onUnequip this type of relic)
    //if the card is removed from the deck, run loseRelic (which will automatically onUnequip it for us)
    //if the card is duplicated, duplicate the relic and immediately run its OnEquip* (do not otherwise set its counter)
    //          * populateSkipEquipIfTempRelics exception: run Necronomicon's OnEquip/OnUnequip ONLY if the duplicated card is permanently added to the deck
    //do nothing special if a saved game is loaded, regardless of "activated" status


    /// /if any relic disappears due to an event, *first* unlink the relic from the card, then remove the card
    // we don't need to make any other adjustments as loseRelic will unequip everything for us
    public void setRelicInfoForNewlyConvertedCard(AbstractRelic relic) {
        setRelicInfo(relic);
        if (PowerelicAllowlist.isEssentialEquipRelic(capturedRelic)) {
            capturedRelic.onUnequip();
        }
    }

    public void setRelicInfoForCopiedCard(AbstractRelic relic) {
        setRelicInfo(relic);
    }

    public void setRelicInfoFromSavedIndexData(int index) {
        //Data is saved in card.misc, pointing to a specific relic index.
        // We also add 999999999 to misc if the relic was currently active at time of saving,
        // so "index" is the original value of misc before these flags were added.
        AbstractRelic relic = Wiz.adp().relics.get(index);
        setRelicInfo(relic);
    }

    public void setRelicInfo(AbstractRelic relic) {
        if (relic != null) {
            this.capturedRelic = relic;
            //we must check if the relic is already assigned to a card.
            //if it is, we are probably mid- makeCopy method and we don't want the original relic to be reassigned to the new card.
            //(It is still permissible for the new card to point to the same relic as the old one. It will be reassigned upon attempting to play the card.)
            if (!PowerelicRelicContainmentFields.isContained.get(relic)) {
                PowerelicRelicContainmentFields.isContained.set(relic, true);
                PowerelicRelicContainmentFields.withinCard.set(relic, this);
            }
            relic.description = relic.getUpdatedDescription();
            relic.setCounter(relic.counter);
            rawDescription = relic.description;
            rawDescription = rawDescription.replace("#r", "");
            rawDescription = rawDescription.replace("#y", "");
            rawDescription = rawDescription.replace("#g", "");
            rawDescription = rawDescription.replace("#b", "");
            rawDescription = rawDescription.replace("#p", "");
            initializeDescription();
            name = relic.name;
        }

    }


    @Override
    public void use(AbstractPlayer abstractPlayer, AbstractMonster abstractMonster) {
        Wiz.atb(new ActivatePowerelicAction(this));
    }


    //start-of-next-combat removal patch
    @SpirePatch2(clz = AbstractPlayer.class, method = "preBattlePrep")
    public static class RelicCardRecontainmentPatches {
        @SpirePrefixPatch
        public static void patch(AbstractPlayer __instance) {
            for (AbstractRelic relic : Wiz.adp().relics) {
                if (PowerelicRelicContainmentFields.isContained.get(relic)) {
                    if (PowerelicAllowlist.isEssentialEquipRelic(relic)) {
                        int previousMaxEnergy = Wiz.adp().energy.energyMaster;
                        int previousHandSize = Wiz.adp().masterHandSize;
                        relic.onUnequip();
                        if (PowerelicAllowlist.isImmediateOnequipRelic(relic)) {
                            Wiz.att(new PowerelicUpdateEnergyAndHandsizeAction(previousMaxEnergy, previousHandSize));
                        }
                    }
                }
            }
            __instance.relics.removeIf(relic -> PowerelicRelicContainmentFields.isContained.get(relic));
            // If any relics were just obtained at the start of the battle (like what the Humility zone does), they'll
            // have isDone set to false, and we have to preserve that for the relic gaining process to work correctly
            // (reorganizeRelics sets isDone to true, which would result in onEquip being skipped for new relics).
            List<AbstractRelic> justObtainedRelics = __instance.relics.stream().filter(r -> !r.isDone).collect(Collectors.toList());
            Wiz.adp().reorganizeRelics();
            justObtainedRelics.forEach(r -> r.isDone = false);
        }
    }


    @Override
    public void onObtainCard() {
        //if a card is added to the deck
        //AND is a duplicate of another card
        //AND the card is flagged as BOTH "nonessential-equip" and "skip equip if temp"
        //...then the card (and captured relic) is not temp and should be equipped.
        //      (i.e. add another Necronomicurse to the deck.)
        //In addition to the previous condition, also do this if the card was from a ViolescentShard card reward.
        boolean cardIsCopy = false;
        for (AbstractCard card : Wiz.deck().group) {
            if (card != this) {
                if (card instanceof PowerelicCard) {
                    if (((PowerelicCard) card).capturedRelic == capturedRelic) {
                        cardIsCopy = true;
                        replaceThisCardsRelicWithNewCopy();
                    }
                }
            }
        }
        if (cardIsCopy || cardIsFromCardReward) {
            cardIsFromCardReward = false;
            if (PowerelicAllowlist.isNonessentialEquipRelic(capturedRelic)) {
                if (PowerelicAllowlist.isSkipEquipIfTempRelic(capturedRelic)) {
                    capturedRelic.onEquip();
                }
            }
        }
    }


    @Override
    public void onRemoveFromMasterDeck() {
        if (capturedRelic != null) {
            PowerelicRelicContainmentFields.isContained.set(capturedRelic, false);
            PowerelicRelicContainmentFields.withinCard.set(capturedRelic, null);
            //Essential: if the card is removed from the deck *and* the relic is currently activated, run loseRelic (which will automatically onUnequip it for us)
            //      if the relic isn't currently activated, it was already unequipped and removed from the player relic list
            //Any other card: if the card is removed from the deck, run loseRelic (which will automatically onUnequip it for us)
            //      note that for those cards, we call loseRelic even if the relic isn't currently activated
            if (!PowerelicAllowlist.isEssentialEquipRelic(capturedRelic)
                    || Wiz.adp().relics.contains(capturedRelic)) {
                boolean success = Wiz.adp().loseRelic(capturedRelic.relicId);
                if (!success) {
                    //if we're here, then loseRelic failed because the relic wasn't in the player relic list
                    // (inactive) and we need to onUnequip it manually.
                    capturedRelic.onUnequip();
                }
            }
            //Also note that if for whatever reason we have a permanent non-Powerelic relic in the player relic bar
            // and the player removes a card with the same relic ID, the above logic will become very confused
            // and probably remove the permanent relic.  So avoid allowing duplicate relics.
        }
    }


    @Override
    public AbstractCard makeStatEquivalentCopy() {
        AbstractCard newCard = super.makeStatEquivalentCopy();
        if (capturedRelic != null) {
            setRelicInfoForCopiedCard(capturedRelic);
        }
        return newCard;
    }


    public AbstractRelic replaceThisCardsRelicWithNewCopy() {
        if (this.capturedRelic == null) return null;
        AbstractRelic newRelic = this.capturedRelic.makeCopy();
        newRelic.setCounter(this.capturedRelic.counter);
        this.capturedRelic = newRelic;
        this.setRelicInfoForCopiedCard(newRelic);
        ReflectionHacks.privateMethod(AbstractRelic.class, "initializeTips").invoke(newRelic);
        if (PowerelicAllowlist.isNonessentialEquipRelic(newRelic)) {
            if (!PowerelicAllowlist.isSkipEquipIfTempRelic(newRelic)) {
                newRelic.onEquip();
            }
        }
        return newRelic;
    }

    @SpirePatch(clz = AbstractRelic.class, method = SpirePatch.CLASS)
    public static class PowerelicRelicContainmentFields {
        public static SpireField<Boolean> isContained = new SpireField<>(() -> false);
        public static SpireField<PowerelicCard> withinCard = new SpireField<>(() -> null);
        public static SpireField<Boolean> isActiveBetweenCombats = new SpireField<>(() -> false);
    }


    @SpirePatch2(clz = AbstractCard.class, method = "renderPortrait")
    public static class RenderPortraitPatches {
        @SpirePostfixPatch
        public static void patch(AbstractCard __instance, SpriteBatch sb) {
            if (__instance instanceof PowerelicCard) {
                ((PowerelicCard) __instance).renderRelicImage(sb);
            }
        }
    }

    @SpirePatch2(clz = AbstractCard.class, method = "renderJokePortrait")
    public static class RenderJokePortraitPatches {
        @SpirePostfixPatch
        public static void patch(AbstractCard __instance, SpriteBatch sb) {
            if (__instance instanceof PowerelicCard) {
                ((PowerelicCard) __instance).renderRelicImage(sb);
            }
        }
    }

    public void renderRelicImage(SpriteBatch sb) {
        if (this.capturedRelic != null) {
            float drawX = this.current_x - 125.0F;
            float drawY = this.current_y - 95.0F;
            //this.capturedRelic.loadLargeImg();
            Texture img = this.capturedRelic.largeImg;
            Color renderColor = ReflectionHacks.getPrivate(this, AbstractCard.class, "renderColor");
            if (false) {
                //for later: add support for large images (draw parameters haven't been set yet)
                // note that loadLargeImg causes logger WARNING spam if no such image exists!
                sb.setColor(renderColor);
                //sb.draw(img, drawX, drawY + 72.0F, 125.0F, 23.0F, 250.0F, 190.0F, 2*this.drawScale * Settings.scale, 2*this.drawScale * Settings.scale, this.angle, -60, -64, 250, 190, false, false);
            } else {
                img = this.capturedRelic.img;
                sb.draw(img, drawX, drawY + 72.0F, 125.0F, 23.0F, 250.0F, 190.0F, 2 * this.drawScale * Settings.scale, 2 * this.drawScale * Settings.scale, this.angle, -60, -64, 250, 190, false, false);
            }
            if (this.capturedRelic.counter > -1) {
                String text = Integer.toString(this.capturedRelic.counter);
                topPanelInfoFont_L.getData().setScale(this.drawScale);
                FontHelper.renderRotatedText(sb, topPanelInfoFont_L, text,
                        this.current_x, this.current_y, 87.0F * Settings.scale * this.drawScale / 2.0F, 90.0F * Settings.scale * this.drawScale / 2.0F, this.angle, true, Color.WHITE);
            }
        }
    }


    @SpirePatch2(clz = SingleCardViewPopup.class, method = "renderPortrait")
    public static class CardInspectScreenPatch {
        @SpirePostfixPatch
        public static void patch(SingleCardViewPopup __instance, SpriteBatch sb) {
            AbstractCard card = ReflectionHacks.getPrivate(__instance, SingleCardViewPopup.class, "card");
            if (card instanceof PowerelicCard) {
                PowerelicCard prCard = ((PowerelicCard) card);
                if (prCard.capturedRelic != null) {
                    float drawX = Settings.WIDTH / 2.0F - 250.0F;
                    float drawY = Settings.HEIGHT / 2.0F - 100.0F;
                    //this.capturedRelic.loadLargeImg();
                    Texture img = prCard.capturedRelic.largeImg;
                    Color renderColor = ReflectionHacks.getPrivate(prCard, AbstractCard.class, "renderColor");
                    if (false) {
                        //for later: add support for large images (draw parameters haven't been set yet)
                        // note that loadLargeImg causes logger WARNING spam if no such image exists!
                        sb.setColor(renderColor);
                        //sb.draw(img, drawX, drawY + 72.0F, 125.0F, 23.0F, 250.0F, 190.0F, 2*this.drawScale * Settings.scale, 2*this.drawScale * Settings.scale, this.angle, -60, -64, 250, 190, false, false);
                    } else {
                        img = prCard.capturedRelic.img;
                        sb.draw(img, drawX + 125F, drawY + 72.0F, 125.0F, 23.0F, 250.0F, 190.0F, 4 * Settings.scale, 4 * Settings.scale, 0.0F, -60, -64, 250, 190, false, false);
                    }
                    if (prCard.capturedRelic.counter > -1) {
                        String text = Integer.toString(prCard.capturedRelic.counter);
                        float current_x = ReflectionHacks.getPrivate(__instance, SingleCardViewPopup.class, "current_x");
                        float current_y = ReflectionHacks.getPrivate(__instance, SingleCardViewPopup.class, "current_y");
                        float drawScale = ReflectionHacks.getPrivate(__instance, SingleCardViewPopup.class, "drawScale");
                        topPanelInfoFont_L.getData().setScale(drawScale);
                        FontHelper.renderRotatedText(sb, topPanelInfoFont_L, text,
                                current_x, current_y, 82.0F * Settings.scale * drawScale / 2.0F, 385.0F * Settings.scale * drawScale / 2.0F, 0.0F, true, Color.WHITE);
                    }
                }
            }
        }
    }


    @SpirePatch2(clz = AbstractPlayer.class, method = "loseRelic")
    public static class RemovingRelicAlsoRemovesCardPatch {
        @SpirePrefixPatch
        public static void patch(AbstractPlayer __instance, String targetID) {
            if (!__instance.hasRelic(targetID)) {
                return;
            }
            //As loseRelic takes a string instead of an object, we have to use a Prefix patch here.
            //If we use Postfix, we won't be able to tell which relic (of duplicates) was removed!
            //Note that this patch also depends on LoseRelicPatch to onUnequip only ONE relic
            //when loseRelic is called on duplicated relics.
            for (AbstractCard card : Wiz.deck().group) {
                if (card instanceof PowerelicCard) {
                    if (((PowerelicCard) card).capturedRelic.relicId.equals(targetID)) {
                        //first unlink the card or we'll end up calling onUnequip twice
                        ((PowerelicCard) card).capturedRelic = null;
                        Wiz.deck().removeCard(card);
                        break;
                    }
                }
            }
        }
    }


    @Override
    public CardedRelicSaveData onSave() {
        logger.info("PowerelicCard.onSave " + this.capturedRelic);
        if (this.capturedRelic == null) {
            return new CardedRelicSaveData("EMPTY", 0, false);
        }
        return new CardedRelicSaveData(this.capturedRelic.relicId, this.capturedRelic.counter, Wiz.adp().relics.contains(this.capturedRelic));
    }

    @Override
    public void onLoad(CardedRelicSaveData cardedRelicSaveData) {
        if (cardedRelicSaveData == null) {
            logger.info("PowerelicCard.onLoad, but savedata is null");
            return;
        }
        logger.info("PowerelicCard.onLoad " + cardedRelicSaveData.relicID + " " + cardedRelicSaveData.counter + " " + cardedRelicSaveData.active);
        boolean matchFound = false;
        AbstractRelic relic = null;
        if (cardedRelicSaveData.active) {
            //find the first relic in the player's relic list that
            //  1) matches the saved data and
            //  2) is not already captured, then
            //      flag it as temporary
            for (AbstractRelic playerRelic : Wiz.adp().relics) {
                if (Objects.equals(playerRelic.relicId, cardedRelicSaveData.relicID)) {
                    if (playerRelic.counter == cardedRelicSaveData.counter) {
                        if (!PowerelicRelicContainmentFields.isContained.get(playerRelic)) {
                            logger.info(cardedRelicSaveData.relicID + " with counter " + cardedRelicSaveData.counter + " will be restored to temporary status");
                            relic = playerRelic;
                            matchFound = true;
                            break;
                        }
                    }
                }
            }
            if (!matchFound) {
                logger.info("WARNING: " + cardedRelicSaveData.relicID + " reports that it is temporary, but we couldn't find a matching relic in player's list with counter " + cardedRelicSaveData.counter);
            }
        }
        if (!matchFound) {
            relic = RelicLibrary.getRelic(cardedRelicSaveData.relicID).makeCopy();
            //note that if relicID was not found, RelicLibrary will return a Circlet
            if (relic instanceof Circlet && !Objects.equals(cardedRelicSaveData.relicID, Circlet.ID)) {
                logger.info("WARNING: " + cardedRelicSaveData.relicID + " became a Circlet after loading");
            }
            relic.setCounter(cardedRelicSaveData.counter);
        }

        this.setRelicInfo(relic);
    }


    public AbstractCard makeCopy() {
        PowerelicCard copy = PowerelicCard.fromCopy(capturedRelic);
        copy.cardIsFromCardReward = this.cardIsFromCardReward;
        return copy;
    }

    public static BitmapFont topPanelInfoFont_L;

    static {
        FileHandle fontFile;
        float fontScale = 1.0f;
        switch (Settings.language) {
            case ZHS:
                fontFile = Gdx.files.internal("font/zhs/SourceHanSerifSC-Bold.otf");
                break;
            case ZHT:
                fontFile = Gdx.files.internal("font/zht/NotoSansCJKtc-Bold.otf");
                break;
            case EPO:
                fontFile = Gdx.files.internal("font/epo/Andada-Bold.otf");
                break;
            case GRE:
                fontFile = Gdx.files.internal("font/gre/Roboto-Bold.ttf");
                break;
            case JPN:
                fontFile = Gdx.files.internal("font/jpn/NotoSansCJKjp-Bold.otf");
                break;
            case KOR:
                fontFile = Gdx.files.internal("font/kor/GyeonggiCheonnyeonBatangBold.ttf");
                break;
            case POL:
            case RUS:
            case UKR:
                fontFile = Gdx.files.internal("font/rus/FiraSansExtraCondensed-Bold.ttf");
                break;
            case SRP:
            case SRB:
                fontFile = Gdx.files.internal("font/srb/InfluBG-Bold.otf");
                break;
            case THA:
                fontScale = 0.95F;
                fontFile = Gdx.files.internal("font/tha/CSChatThaiUI.ttf");
                break;
            case VIE:
                fontFile = Gdx.files.internal("font/vie/Grenze-SemiBold.ttf");
                break;
            default:
                fontFile = Gdx.files.internal("font/Kreon-Bold.ttf");
        }
        ReflectionHacks.setPrivateStatic(FontHelper.class, "fontFile", fontFile);
        ReflectionHacks.setPrivateStatic(FontHelper.class, "fontScale", fontScale);

        FreeTypeFontGenerator.FreeTypeFontParameter param = ReflectionHacks.getPrivateStatic(FontHelper.class, "param");
        param.shadowColor = new Color(0.0F, 0.0F, 0.0F, 0.33F);// 454
        param.gamma = 2.0F;
        param.borderGamma = 2.0F;
        param.borderStraight = true;
        param.borderColor = Color.DARK_GRAY;
        param.borderWidth = 4.0F * Settings.scale;
        param.shadowOffsetX = 2;
        param.shadowOffsetY = 2;
        topPanelInfoFont_L = FontHelper.prepFont(42.0F, true);
    }
}