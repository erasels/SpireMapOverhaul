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
import spireMapOverhaul.zones.manasurge.modifiers.common.positive.CripplingModifier;
import spireMapOverhaul.zones.manasurge.modifiers.common.positive.ExposingModifier;
import spireMapOverhaul.zones.manasurge.modifiers.common.positive.SharpModifier;
import spireMapOverhaul.zones.manasurge.modifiers.common.positive.ToughModifier;
import spireMapOverhaul.zones.manasurge.modifiers.uncommon.positive.PowerfulModifier;
import spireMapOverhaul.zones.manasurge.modifiers.uncommon.positive.ProtectiveModifier;
import spireMapOverhaul.zones.manasurge.powers.ManaSurgePower;
import spireMapOverhaul.zones.manasurge.ui.EnchantOption;
import spireMapOverhaul.zones.manasurge.utils.ManaSurgeTags;

import java.util.ArrayList;

public class ManaSurgeZone extends AbstractZone implements CombatModifyingZone, RewardModifyingZone, CampfireModifyingZone {
    public static final String ID = "ManaSurge";
    public static final float COMMON_CHANCE = 0.8f;

    public ManaSurgeZone() {
        super(ID,Icons.MONSTER,Icons.ELITE,Icons.SHOP,Icons.EVENT,Icons.REST,Icons.REWARD);
        this.width = 3;
        this.height = 4;
    }

    @Override
    public AbstractZone copy() {
        return new ManaSurgeZone();
    }

    @Override
    public void atBattleStartPreDraw() {
        for (AbstractCard card : AbstractDungeon.player.drawPile.group) {
            if (!card.tags.contains(ManaSurgeTags.PERMANENT_MODIFIER)) {
                CardModifierManager.removeAllModifiers(card, false);
            }
        }
        for (AbstractCard card : AbstractDungeon.player.discardPile.group) {
            if (!card.tags.contains(ManaSurgeTags.PERMANENT_MODIFIER)) {
                CardModifierManager.removeAllModifiers(card, false);
            }
        }
        for (AbstractCard card : AbstractDungeon.player.hand.group) {
            if (!card.tags.contains(ManaSurgeTags.PERMANENT_MODIFIER)) {
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
                card.tags.add(ManaSurgeTags.PERMANENT_MODIFIER);
                if (Math.random() < COMMON_CHANCE) {
                    int numberOfCommonModifiers = 4;
                    int selectedModifierIndex = (int) (Math.random() * numberOfCommonModifiers);
                    AbstractCardModifier modifier;
                    switch (selectedModifierIndex) {
                        case 0:
                            modifier = new SharpModifier();
                            break;
                        case 1:
                            modifier = new ToughModifier();
                            break;
                        case 2:
                            modifier = new ExposingModifier();
                            break;
                        case 3:
                            modifier = new CripplingModifier();
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
                            modifier = new PowerfulModifier();
                            break;
                        case 1:
                            modifier = new ProtectiveModifier();
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

    public void postAddButtons(ArrayList<AbstractCampfireOption> buttons) {
        buttons.add(new EnchantOption(!CardGroup.getGroupWithoutBottledCards(AbstractDungeon.player.masterDeck.getPurgeableCards()).isEmpty()));
    }

    @Override
    public Color getColor() { //I considered changing this to a variable, but a method lets you do funky stuff like a rainbow zone that changes colors or something.
        return new Color(0.45f,0.49f, 0.91f, 1f);
    }
}
