package spireMapOverhaul.zones.keymaster;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.*;
import spireMapOverhaul.abstracts.AbstractZone;
import spireMapOverhaul.zoneInterfaces.CombatModifyingZone;
import spireMapOverhaul.zoneInterfaces.RewardModifyingZone;
import spireMapOverhaul.zoneInterfaces.ShopModifyingZone;

import java.util.ArrayList;

import static spireMapOverhaul.util.Wiz.atb;

public class KeymasterZone extends AbstractZone implements CombatModifyingZone, RewardModifyingZone, ShopModifyingZone {
    public static final String ID = "Keymaster";

    public KeymasterZone() {
        super(ID, Icons.MONSTER, Icons.SHOP);
        this.width = 3;
        this.height = 3;
    }

    @Override
    public AbstractZone copy() {
        return new KeymasterZone();
    }

    @Override
    public Color getColor() {
        return new Color(0.396f, 1.0f, 0.0f, 0.8f);
    }

    @Override
    public boolean canSpawn() {
        return (Settings.hasSapphireKey && Settings.hasEmeraldKey && Settings.hasRubyKey);
    }

    @Override
    protected boolean canIncludeEarlyRows() {
        return true;
    }

    @Override
    public void atBattleStart() {
        atb(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new StrengthPower(AbstractDungeon.player, 1), 1));
        atb(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new DexterityPower(AbstractDungeon.player, 1), 1));
        atb(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new FocusPower(AbstractDungeon.player, 1), 1));
    }

    @Override
    public float changeCardUpgradeChance(float curChance) {
        return 1.0f;
    }

    @Override
    public boolean allowUpgradingRareCards() {
        return true;
    }

    @Override
    public void postCreateShopCards(ArrayList<AbstractCard> coloredCards, ArrayList<AbstractCard> colorlessCards) {
        // Upgrade colored cards
        for (AbstractCard card : coloredCards) {
            if (card.canUpgrade()) {
                card.upgrade();
            }
        }

        // Upgrade colorless cards
        for (AbstractCard card : colorlessCards) {
            if (card.canUpgrade()) {
                card.upgrade();
            }
        }
    }

    @Override
    public void postAddIdleMessages(ArrayList<String> idleMessages) {
        String localizedMessage = TEXT[3];
        idleMessages.add(localizedMessage);
    }

}