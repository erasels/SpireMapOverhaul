package spireMapOverhaul.zones.manasurge;

import basemod.abstracts.AbstractCardModifier;
import basemod.helpers.CardModifierManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.mod.stslib.Keyword;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.random.Random;
import com.megacrit.cardcrawl.ui.campfire.AbstractCampfireOption;
import spireMapOverhaul.SpireAnniversary6Mod;
import spireMapOverhaul.abstracts.AbstractZone;
import spireMapOverhaul.util.TexLoader;
import spireMapOverhaul.util.Wiz;
import spireMapOverhaul.zoneInterfaces.*;
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
import java.util.List;

import static spireMapOverhaul.SpireAnniversary6Mod.makeID;
import static spireMapOverhaul.SpireAnniversary6Mod.makePath;

public class ManaSurgeZone extends AbstractZone implements
        CombatModifyingZone,
        RewardModifyingZone,
        CampfireModifyingZone,
        ShopModifyingZone,
        ModifiedEventRateZone,
        RenderableZone {
    public static final String ID = "ManaSurge";
    private Texture bg = TexLoader.getTexture(SpireAnniversary6Mod.makeBackgroundPath("manasurge/bg.png"));
    public static final String NEGATIVE_MOD = SpireAnniversary6Mod.makeID("ManaSurge:Blight");
    public static final String POSITIVE_MOD = SpireAnniversary6Mod.makeID("ManaSurge:Enchantment");
    public static final String ENCHANTBLIGHT_KEY = makeID("ManaSurge:EnchantBlight");
    public static final String ENCHANTBLIGHT_OGG = makePath("audio/ManaSurge/enchantblight.ogg");
    public static final float COMMON_CHANCE = 0.8f;
    public static final float ENCHANT_CHANCE = 0.65f;

    public ManaSurgeZone() {
        super(ID, Icons.MONSTER, Icons.SHOP, Icons.EVENT, Icons.REST);
        this.width = 3;
        this.height = 3;
        this.maxHeight = 4;
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

    public static List<AbstractManaSurgeModifier> getPositiveCommonModifierList(boolean permanent) {
        List<AbstractManaSurgeModifier> positiveCommonModifierList = new ArrayList<>();
        positiveCommonModifierList.add(new SharpModifier(permanent));
        positiveCommonModifierList.add(new ToughModifier(permanent));
        positiveCommonModifierList.add(new ExposingModifier(permanent));
        positiveCommonModifierList.add(new CripplingModifier(permanent));
        return positiveCommonModifierList;
    }

    public static List<AbstractManaSurgeModifier> getNegativeCommonModifierList(boolean permanent) {
        List<AbstractManaSurgeModifier> negativeCommonModifierList = new ArrayList<>();
        negativeCommonModifierList.add(new FeebleModifier(permanent));
        negativeCommonModifierList.add(new FlawedModifier(permanent));
        negativeCommonModifierList.add(new FragileModifier(permanent));
        negativeCommonModifierList.add(new HarmfulModifier(permanent));
        return negativeCommonModifierList;
    }

    public static List<AbstractManaSurgeModifier> getPositiveUncommonModifierList(boolean permanent) {
        List<AbstractManaSurgeModifier> positiveUncommonModifierList = new ArrayList<>();
        positiveUncommonModifierList.add(new PowerfulModifier(permanent));
        positiveUncommonModifierList.add(new ProtectiveModifier(permanent));
        return positiveUncommonModifierList;
    }

    public static List<AbstractManaSurgeModifier> getNegativeUncommonModifierList(boolean permanent) {
        List<AbstractManaSurgeModifier> negativeUncommonModifierList = new ArrayList<>();
        negativeUncommonModifierList.add(new BrittleModifier(permanent));
        negativeUncommonModifierList.add(new PowerlessModifier(permanent));
        return negativeUncommonModifierList;
    }

    public static void applyRandomTemporaryModifier(AbstractCard card) {
        applyRandomModifier(card, AbstractDungeon.cardRng, false, null);
    }

    public static void applyRandomPermanentModifier(AbstractCard card) {
        applyRandomModifier(card, AbstractDungeon.cardRng, true, null);
    }

    public static void applyPermanentPositiveModifier(AbstractCard card) {
        applyRandomModifier(card, AbstractDungeon.cardRng, true, true);
    }

    public static void applyPermanentNegativeModifier(AbstractCard card) {
        applyRandomModifier(card, AbstractDungeon.cardRng, true, false);
    }

    private static void applyRandomModifier(AbstractCard card, Random rng, boolean permanent, Boolean positiveNegative) {
        boolean positive = Boolean.TRUE.equals(positiveNegative) || (positiveNegative == null && rng.randomBoolean(ENCHANT_CHANCE));
        boolean allowUncommonModifiers = card.type == AbstractCard.CardType.POWER || (card.exhaust && cardUpgradeExhausts(card));
        boolean common = !allowUncommonModifiers || rng.randomBoolean(COMMON_CHANCE);
        List<AbstractManaSurgeModifier> modifiers =
                positive && common ? getPositiveCommonModifierList(permanent)
                        : positive && !common ? getPositiveUncommonModifierList(permanent)
                        : !positive && common ? getNegativeCommonModifierList(permanent)
                        : getNegativeUncommonModifierList(permanent);
        AbstractManaSurgeModifier modifier = modifiers.get(rng.random(modifiers.size() - 1));
        CardModifierManager.addModifier(card, modifier);
    }

    private static boolean cardUpgradeExhausts(AbstractCard card) {
        if (!card.canUpgrade()) {
            return true;
        }
        AbstractCard copy = card.makeStatEquivalentCopy();
        copy.upgrade();
        return copy.exhaust;
    }

    @Override
    public AbstractZone copy() {
        return new ManaSurgeZone();
    }

    @Override
    public void atBattleStartPreDraw() {
        Wiz.applyToSelf(new ManaSurgePower(AbstractDungeon.player, 0));
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
                    return baseCost - 40;
                }
            } else {
                if (isPositiveModifier(c)) {
                    return baseCost + 80;
                } else {
                    return baseCost - 80;
                }
            }
        }
        return baseCost;
    }

    @Override
    public float zoneSpecificEventRate() {
        return 1;
    }

    @Override
    public Color getColor() {
        return new Color(0.3f, 0.15f, 0.85f, 0.55f);
    }

    @Override
    public void postRenderCombatBackground(SpriteBatch sb) {
        sb.draw(bg, 0, 0, Settings.WIDTH, Settings.HEIGHT);
    }
}
