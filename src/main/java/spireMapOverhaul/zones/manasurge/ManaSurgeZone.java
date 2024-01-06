package spireMapOverhaul.zones.manasurge;

import basemod.abstracts.AbstractCardModifier;
import basemod.helpers.CardModifierManager;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.AbstractEvent;
import com.megacrit.cardcrawl.ui.campfire.AbstractCampfireOption;
import spireMapOverhaul.abstracts.AbstractZone;
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
import spireMapOverhaul.zones.manasurge.ui.EnchantOption;

import java.util.ArrayList;

public class ManaSurgeZone extends AbstractZone implements
        CombatModifyingZone,
        RewardModifyingZone,
        CampfireModifyingZone,
        ShopModifyingZone,
        ModifiedEventRateZone {
    public static final String ID = "ManaSurge";
    public static final float COMMON_CHANCE = 0.8f;

    public ManaSurgeZone() {
        super(ID,Icons.MONSTER,Icons.SHOP,Icons.EVENT,Icons.REST);
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

    private boolean isPositiveModifier(AbstractCard card) {
        for (AbstractCardModifier mod : CardModifierManager.modifiers(card)) {
            if (mod instanceof AbstractManaSurgeModifier && ((AbstractManaSurgeModifier) mod).getModEffect() == AbstractManaSurgeModifier.ModEffect.POSITIVE_MOD) {
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
                    int numberOfCommonModifiers = 8;
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
                    int numberOfUncommonModifiers = 4;
                    int selectedModifierIndex = (int) (Math.random() * numberOfUncommonModifiers);
                    AbstractCardModifier modifier;
                    switch (selectedModifierIndex) {
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
                    int numberOfCommonModifiers = 8;
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
                    int numberOfUncommonModifiers = 4;
                    int selectedModifierIndex = (int) (Math.random() * numberOfUncommonModifiers);
                    AbstractCardModifier modifier;
                    switch (selectedModifierIndex) {
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
        }
        for (AbstractCard card : colorlessCards) {
            if (card.cost != -2 && card.type != AbstractCard.CardType.CURSE && card.type != AbstractCard.CardType.STATUS) {
                if (Math.random() < COMMON_CHANCE) {
                    int numberOfCommonModifiers = 8;
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
                    int numberOfUncommonModifiers = 4;
                    int selectedModifierIndex = (int) (Math.random() * numberOfUncommonModifiers);
                    AbstractCardModifier modifier;
                    switch (selectedModifierIndex) {
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
    public float zoneSpecificEventRate() {
        return 1;
    }

    @Override
    public Color getColor() {
        return new Color(0.45f,0.49f, 0.91f, 1f);
    }
}
