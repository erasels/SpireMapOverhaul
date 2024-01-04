package spireMapOverhaul.zones.manasurge;

import basemod.abstracts.AbstractCardModifier;
import basemod.helpers.CardModifierManager;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.ui.campfire.AbstractCampfireOption;
import spireMapOverhaul.abstracts.AbstractZone;
import spireMapOverhaul.zoneInterfaces.CampfireModifyingZone;
import spireMapOverhaul.zoneInterfaces.CombatModifyingZone;
import spireMapOverhaul.zoneInterfaces.RewardModifyingZone;
import spireMapOverhaul.zoneInterfaces.ShopModifyingZone;
import spireMapOverhaul.zones.manasurge.modifiers.AbstractManaSurgeModifier;
import spireMapOverhaul.zones.manasurge.modifiers.common.positive.CripplingModifier;
import spireMapOverhaul.zones.manasurge.modifiers.common.positive.ExposingModifier;
import spireMapOverhaul.zones.manasurge.modifiers.common.positive.SharpModifier;
import spireMapOverhaul.zones.manasurge.modifiers.common.positive.ToughModifier;
import spireMapOverhaul.zones.manasurge.modifiers.uncommon.positive.PowerfulModifier;
import spireMapOverhaul.zones.manasurge.modifiers.uncommon.positive.ProtectiveModifier;
import spireMapOverhaul.zones.manasurge.powers.ManaSurgePower;
import spireMapOverhaul.zones.manasurge.ui.EnchantOption;

import java.util.ArrayList;

public class ManaSurgeZone extends AbstractZone implements
        CombatModifyingZone,
        RewardModifyingZone,
        CampfireModifyingZone,
        ShopModifyingZone {
    public static final String ID = "ManaSurge";
    public static final float COMMON_CHANCE = 0.8f;

    public ManaSurgeZone() {
        super(ID,Icons.MONSTER,Icons.ELITE,Icons.SHOP,Icons.EVENT,Icons.REST,Icons.REWARD);
        this.width = 3;
        this.height = 4;
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

    @Override
    public AbstractZone copy() {
        return new ManaSurgeZone();
    }

    @Override
    public void atBattleStartPreDraw() {
        for (AbstractCard card : AbstractDungeon.player.drawPile.group) {
            if (!hasManaSurgeModifier(card)) {
                CardModifierManager.removeAllModifiers(card, false);
            }
        }
        for (AbstractCard card : AbstractDungeon.player.discardPile.group) {
            if (!hasManaSurgeModifier(card)) {
                CardModifierManager.removeAllModifiers(card, false);
            }
        }
        for (AbstractCard card : AbstractDungeon.player.hand.group) {
            if (!hasManaSurgeModifier(card)) {
                CardModifierManager.removeAllModifiers(card, false);
            }
        }
        AbstractDungeon.player.hand.applyPowers();
        AbstractDungeon.actionManager.addToBottom((new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new ManaSurgePower(AbstractDungeon.player, 0), 0)));
    }

    @Override
    public void modifyRewardCards(ArrayList<AbstractCard> cards) {
        for (AbstractCard card : cards) {
            if (card.cost != -2 && card.type != AbstractCard.CardType.CURSE && card.type != AbstractCard.CardType.STATUS) {
                if (Math.random() < COMMON_CHANCE) {
                    int numberOfCommonModifiers = 4;
                    int selectedModifierIndex = (int) (Math.random() * numberOfCommonModifiers);
                    AbstractCardModifier modifier;
                    switch (selectedModifierIndex) {
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
                    int numberOfUncommonModifiers = 2;
                    int selectedModifierIndex = (int) (Math.random() * numberOfUncommonModifiers);
                    AbstractCardModifier modifier;
                    switch (selectedModifierIndex) {
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
                if (Math.random() < COMMON_CHANCE) {
                    int numberOfCommonModifiers = 4;
                    int selectedModifierIndex = (int) (Math.random() * numberOfCommonModifiers);
                    AbstractCardModifier modifier;
                    switch (selectedModifierIndex) {
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
                    int numberOfUncommonModifiers = 2;
                    int selectedModifierIndex = (int) (Math.random() * numberOfUncommonModifiers);
                    AbstractCardModifier modifier;
                    switch (selectedModifierIndex) {
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
        }
        for (AbstractCard card : colorlessCards) {
            if (card.cost != -2 && card.type != AbstractCard.CardType.CURSE && card.type != AbstractCard.CardType.STATUS) {
                if (Math.random() < COMMON_CHANCE) {
                    int numberOfCommonModifiers = 4;
                    int selectedModifierIndex = (int) (Math.random() * numberOfCommonModifiers);
                    AbstractCardModifier modifier;
                    switch (selectedModifierIndex) {
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
                    int numberOfUncommonModifiers = 2;
                    int selectedModifierIndex = (int) (Math.random() * numberOfUncommonModifiers);
                    AbstractCardModifier modifier;
                    switch (selectedModifierIndex) {
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
        }
    }

    @Override
    public float modifyCardBaseCost(AbstractCard c, float baseCost) {
        if (hasManaSurgeModifier(c)) {
            if (isCommonModifier(c)) {
                return baseCost + 30;
            } else {
                return baseCost + 80;
            }
        }
        return baseCost;
    }

    @Override
    public Color getColor() {
        return new Color(0.45f,0.49f, 0.91f, 1f);
    }
}
