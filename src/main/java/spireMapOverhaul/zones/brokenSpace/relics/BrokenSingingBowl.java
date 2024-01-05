package spireMapOverhaul.zones.brokenSpace.relics;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.SingingBowl;
import com.megacrit.cardcrawl.vfx.UpgradeShineEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardBrieflyEffect;
import spireMapOverhaul.interfaces.relics.MaxHPChangeRelic;

import java.util.ArrayList;

public class BrokenSingingBowl extends BrokenRelic implements MaxHPChangeRelic {
    public static final String ID = "BrokenSingingBowl";
    public static final int AMOUNT = 1;

    public BrokenSingingBowl() {
        super(ID, RelicTier.SPECIAL, LandingSound.CLINK, SingingBowl.ID);
    }

    @Override
    public int onMaxHPChange(int amount) {
        ArrayList<AbstractCard> upgradableCards = new ArrayList<>();
        flash();

        for (AbstractCard c : AbstractDungeon.player.masterDeck.group) {
            if (c.canUpgrade()) {// 102
                upgradableCards.add(c);// 103
            }
        }

        for (int i = 0; i < AMOUNT; i++) {
            if (!upgradableCards.isEmpty()) {
                AbstractCard c = upgradableCards.remove(AbstractDungeon.cardRandomRng.random(upgradableCards.size() - 1));
                c.upgrade();
                AbstractDungeon.player.bottledCardUpgradeCheck(c);
                AbstractDungeon.topLevelEffectsQueue.add(new ShowCardBrieflyEffect(c.makeStatEquivalentCopy()));
                AbstractDungeon.topLevelEffectsQueue.add(new UpgradeShineEffect((float) Settings.WIDTH / 2.0F, (float) Settings.HEIGHT / 2.0F));
            }
        }
        return amount;
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0] + AMOUNT + DESCRIPTIONS[1];
    }
}
