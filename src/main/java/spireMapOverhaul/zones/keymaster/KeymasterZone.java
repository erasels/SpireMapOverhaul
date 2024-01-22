package spireMapOverhaul.zones.keymaster;

import basemod.BaseMod;
import basemod.abstracts.CustomSavable;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.DexterityPower;
import com.megacrit.cardcrawl.powers.FocusPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import spireMapOverhaul.abstracts.AbstractZone;
import spireMapOverhaul.zoneInterfaces.CombatModifyingZone;
import spireMapOverhaul.zoneInterfaces.RewardModifyingZone;
import spireMapOverhaul.zoneInterfaces.ShopModifyingZone;

import java.util.ArrayList;

import static spireMapOverhaul.SpireAnniversary6Mod.makeID;
import static spireMapOverhaul.util.Wiz.atb;

public class KeymasterZone extends AbstractZone implements CombatModifyingZone, RewardModifyingZone, ShopModifyingZone {
    public static final String ID = "Keymaster";
    public static final String SaveID = makeID("KeymasterStartOfActHasKeys");
    public static boolean startOfActHasKeys = false;

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
        // We record whether the player has the three keys at the start of each act in order to maintain save/load
        // stability, because otherwise this condition could change during an act. (Because the map is regenerated
        // from scratch every time you save and load, the process must happen the exact same way regardless of whether
        // the player is entering the act after beating the previous one or loading a save.)
        return startOfActHasKeys;
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


    public static void initializeSaveFields() {
        BaseMod.addSaveField(SaveID, new CustomSavable<Boolean>() {
            @Override
            public Boolean onSave() {
                return KeymasterZone.startOfActHasKeys;
            }

            @Override
            public void onLoad(Boolean b) {
                KeymasterZone.startOfActHasKeys = b != null && b;
            }
        });
    }

}