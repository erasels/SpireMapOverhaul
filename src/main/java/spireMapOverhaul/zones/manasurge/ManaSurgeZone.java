package spireMapOverhaul.zones.manasurge;

import basemod.abstracts.AbstractCardModifier;
import basemod.helpers.CardModifierManager;
import com.badlogic.gdx.graphics.Color;
import com.evacipated.cardcrawl.mod.stslib.Keyword;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.AbstractEvent;
import com.megacrit.cardcrawl.ui.campfire.AbstractCampfireOption;
import spireMapOverhaul.SpireAnniversary6Mod;
import spireMapOverhaul.abstracts.AbstractZone;
import spireMapOverhaul.util.Wiz;
import spireMapOverhaul.zoneInterfaces.*;
import spireMapOverhaul.zones.manasurge.events.ManaCycloneEvent;
import spireMapOverhaul.zones.manasurge.modifiers.AbstractManaSurgeModifier;
import spireMapOverhaul.zones.manasurge.modifiers.common.negative.FeebleModifier;
import spireMapOverhaul.zones.manasurge.modifiers.common.negative.FlawedModifier;
import spireMapOverhaul.zones.manasurge.modifiers.common.negative.FragileModifier;
import spireMapOverhaul.zones.manasurge.modifiers.common.negative.HarmfulModifier;
import spireMapOverhaul.zones.manasurge.modifiers.common.positive.CripplingModifier;
import spireMapOverhaul.zones.manasurge.modifiers.common.positive.ExposingModifier;
import spireMapOverhaul.zones.manasurge.modifiers.common.positive.SharpModifier;
import spireMapOverhaul.zones.manasurge.modifiers.common.positive.ToughModifier;
import spireMapOverhaul.zones.manasurge.modifiers.uncommon.negative.BrittleModifier;
import spireMapOverhaul.zones.manasurge.modifiers.uncommon.negative.PowerlessModifier;
import spireMapOverhaul.zones.manasurge.modifiers.uncommon.positive.PowerfulModifier;
import spireMapOverhaul.zones.manasurge.modifiers.uncommon.positive.ProtectiveModifier;
import spireMapOverhaul.zones.manasurge.powers.ManaSurgePower;
import spireMapOverhaul.zones.manasurge.ui.campfire.EnchantOption;

import java.util.ArrayList;

import static spireMapOverhaul.SpireAnniversary6Mod.makeID;
import static spireMapOverhaul.SpireAnniversary6Mod.makePath;

public class ManaSurgeZone extends AbstractZone implements
        CombatModifyingZone,
        RewardModifyingZone,
        CampfireModifyingZone,
        ShopModifyingZone,
        ModifiedEventRateZone {
    public static final String ID = "ManaSurge";
    public static final String NEGATIVE_MOD = SpireAnniversary6Mod.makeID("ManaSurge:Blight");
    public static final String POSITIVE_MOD = SpireAnniversary6Mod.makeID("ManaSurge:Enchantment");
    public static final String ENCHANTBLIGHT_KEY = makeID("ManaSurge:EnchantBlight");
    public static final String ENCHANTBLIGHT_OGG = makePath("audio/ManaSurge/enchantblight.ogg");
    public static final float COMMON_CHANCE = 0.8f;

    public ManaSurgeZone() {
        super(ID,Icons.MONSTER,Icons.SHOP,Icons.EVENT,Icons.REST);
        this.width = 3;
        this.height = 4;
    }

    public static String getKeywordProper(String keywordId) {
        Keyword keywordStrings = SpireAnniversary6Mod.keywords.get(keywordId);
        return (keywordStrings != null) ? keywordStrings.PROPER_NAME : null;
    }

    public static String getKeywordDescription(String keywordId) {
        Keyword keywordStrings = SpireAnniversary6Mod.keywords.get(keywordId);
        return (keywordStrings != null) ? keywordStrings.DESCRIPTION : null;
    }

    public static boolean hasManaSurgeModifier(AbstractCard card) {
        for (AbstractCardModifier mod : CardModifierManager.modifiers(card)) {
            if (mod instanceof AbstractManaSurgeModifier) {
                return true;
            }
        }
        return false;
    }

    private boolean isCommonModifier(AbstractCard card) {
        for (AbstractCardModifier mod : CardModifierManager.modifiers(card)) {
            if (mod instanceof AbstractManaSurgeModifier && ((AbstractManaSurgeModifier) mod).getModRarity() == AbstractManaSurgeModifier.ModRarity.COMMON_MOD) {
                return true;
            }
        }
        return false;
    }

    private boolean isPositiveModifier(AbstractCard card) {
        for (AbstractCardModifier mod : CardModifierManager.modifiers(card)) {
            if (mod instanceof AbstractManaSurgeModifier && ((AbstractManaSurgeModifier) mod).getModEffect() == AbstractManaSurgeModifier.ModEffect.POSITIVE_MOD) {
                return true;
            }
        }
        return false;
    }

    public static void applyRandomTemporaryModifier(AbstractCard card) {
        if (AbstractDungeon.cardRandomRng.randomBoolean(COMMON_CHANCE)) {
            AbstractCardModifier modifier;
            switch (AbstractDungeon.cardRandomRng.random(8)) {
                case 0:
                    modifier = new SharpModifier(false);
                    break;
                case 1:
                    modifier = new HarmfulModifier(false);
                    break;
                case 2:
                    modifier = new ToughModifier(false);
                    break;
                case 3:
                    modifier = new FragileModifier(false);
                    break;
                case 4:
                    modifier = new ExposingModifier(false);
                    break;
                case 5:
                    modifier = new FeebleModifier(false);
                    break;
                case 6:
                    modifier = new CripplingModifier(false);
                    break;
                case 7:
                    modifier = new FlawedModifier(false);
                    break;
                default:
                    modifier = null;
                    break;
            }
            if (modifier != null) {
                CardModifierManager.addModifier(card, modifier);
            }
        } else {
            AbstractCardModifier modifier;
            switch (AbstractDungeon.cardRandomRng.random(4)) {
                case 0:
                    modifier = new PowerfulModifier(false);
                    break;
                case 1:
                    modifier = new PowerlessModifier(false);
                    break;
                case 2:
                    modifier = new ProtectiveModifier(false);
                    break;
                case 3:
                    modifier = new BrittleModifier(false);
                    break;
                default:
                    modifier = null;
                    break;
            }
            if (modifier != null) {
                CardModifierManager.addModifier(card, modifier);
            }
        }
    }

    public static void applyRandomPermanentModifier(AbstractCard card) {
        if (AbstractDungeon.cardRng.randomBoolean(COMMON_CHANCE)) {
            AbstractCardModifier modifier;
            switch (AbstractDungeon.cardRng.random(8)) {
                case 0:
                    modifier = new SharpModifier(true);
                    break;
                case 1:
                    modifier = new ToughModifier(true);
                    break;
                case 2:
                    modifier = new ExposingModifier(true);
                    break;
                case 3:
                    modifier = new CripplingModifier(true);
                    break;
                case 4:
                    modifier = new FeebleModifier(true);
                    break;
                case 5:
                    modifier = new FlawedModifier(true);
                    break;
                case 6:
                    modifier = new FragileModifier(true);
                    break;
                case 7:
                    modifier = new HarmfulModifier(true);
                    break;
                default:
                    modifier = null;
                    break;
            }
            if (modifier != null) {
                CardModifierManager.addModifier(card, modifier);
            }
        } else {
            AbstractCardModifier modifier;
            switch (AbstractDungeon.cardRng.random(4)) {
                case 0:
                    modifier = new PowerfulModifier(true);
                    break;
                case 1:
                    modifier = new ProtectiveModifier(true);
                    break;
                case 2:
                    modifier = new BrittleModifier(true);
                    break;
                case 3:
                    modifier = new PowerlessModifier(true);
                    break;
                default:
                    modifier = null;
                    break;
            }
            if (modifier != null) {
                CardModifierManager.addModifier(card, modifier);
            }
        }
    }

    public static void applyPermanentPositiveModifier(AbstractCard card) {
        if (AbstractDungeon.cardRng.randomBoolean(COMMON_CHANCE)) {
            AbstractCardModifier modifier;
            switch (AbstractDungeon.cardRng.random(4)) {
                case 0:
                    modifier = new SharpModifier(true);
                    break;
                case 1:
                    modifier = new ToughModifier(true);
                    break;
                case 2:
                    modifier = new ExposingModifier(true);
                    break;
                case 3:
                    modifier = new CripplingModifier(true);
                    break;
                default:
                    modifier = null;
                    break;
            }
            if (modifier != null) {
                CardModifierManager.addModifier(card, modifier);
            }
        } else {
            AbstractCardModifier modifier;
            switch (AbstractDungeon.cardRng.random(2)) {
                case 0:
                    modifier = new PowerfulModifier(true);
                    break;
                case 1:
                    modifier = new ProtectiveModifier(true);
                    break;
                default:
                    modifier = null;
                    break;
            }
            if (modifier != null) {
                CardModifierManager.addModifier(card, modifier);
            }
        }
    }

    public static void applyPermanentNegativeModifier(AbstractCard card) {
        if (AbstractDungeon.cardRng.randomBoolean(COMMON_CHANCE)) {
            AbstractCardModifier modifier;
            switch (AbstractDungeon.cardRng.random(4)) {
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
                CardModifierManager.addModifier(card, modifier);
            }
        } else {
            AbstractCardModifier modifier;
            switch (AbstractDungeon.cardRng.random(2)) {
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
                CardModifierManager.addModifier(card, modifier);
            }
        }
    }

    @Override
    public AbstractZone copy() {
        return new ManaSurgeZone();
    }

    @Override
    public void atBattleStartPreDraw() {
        Wiz.applyToSelf(new ManaSurgePower(AbstractDungeon.player,0));
    }

    @Override
    public void modifyRewardCards(ArrayList<AbstractCard> cards) {
        for (AbstractCard card : cards) {
            if (card.cost != -2 && card.type != AbstractCard.CardType.CURSE && card.type != AbstractCard.CardType.STATUS) {
                applyRandomPermanentModifier(card);
            }
        }
    }

    @Override
    public void postAddButtons(ArrayList<AbstractCampfireOption> buttons) {
        buttons.add(new EnchantOption(!CardGroup.getGroupWithoutBottledCards(AbstractDungeon.player.masterDeck.getPurgeableCards()).isEmpty()));
    }

    @Override
    public void postCreateShopCards(ArrayList<AbstractCard> coloredCards, ArrayList<AbstractCard> colorlessCards) {
        for (AbstractCard card : coloredCards) {
            if (card.cost != -2 && card.type != AbstractCard.CardType.CURSE && card.type != AbstractCard.CardType.STATUS) {
                applyRandomPermanentModifier(card);
            }
        }
        for (AbstractCard card : colorlessCards) {
            if (card.cost != -2 && card.type != AbstractCard.CardType.CURSE && card.type != AbstractCard.CardType.STATUS) {
                applyRandomPermanentModifier(card);
            }
        }
    }

    @Override
    public float modifyCardBaseCost(AbstractCard c, float baseCost) {
        if (hasManaSurgeModifier(c)) {
            if (isCommonModifier(c)) {
                if (isPositiveModifier(c)) {
                    return baseCost + 40;
                } else {
                    return baseCost - 20;
                }
            } else {
                if (isPositiveModifier(c)) {
                    return baseCost + 80;
                } else {
                    return baseCost - 40;
                }
            }
        }
        return baseCost;
    }

    @Override
    public AbstractEvent forceEvent() {
        return ModifiedEventRateZone.returnIfUnseen(ManaCycloneEvent.ID);
    }

    @Override
    public Color getColor() {
        return new Color(0.45f,0.49f, 0.91f, 1f);
    }
}
