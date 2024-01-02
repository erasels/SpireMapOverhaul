package spireMapOverhaul.zones.brokenSpace.relics;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.Omamori;
import com.megacrit.cardcrawl.vfx.UpgradeHammerImprintEffect;
import com.megacrit.cardcrawl.vfx.UpgradeShineEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardBrieflyEffect;

import java.util.*;

import static spireMapOverhaul.util.Wiz.adp;

public class BrokenOmamori extends BrokenRelic {
    public static final String ID = "BrokenOmamori";

    public static final int AMOUNT = 3;
    private boolean triggered = false;

    public BrokenOmamori() {
        super(ID, RelicTier.SPECIAL, LandingSound.CLINK, Omamori.ID);
    }

    @Override
    public void onEquip() {
        super.onEquip();
        for (int i = 0; i < AMOUNT; i++) {
            AbstractCard c = AbstractDungeon.returnRandomCurse();
            AbstractDungeon.player.masterDeck.addToTop(c);
            AbstractDungeon.effectList.add(new ShowCardBrieflyEffect(c.makeStatEquivalentCopy()));
        }
    }

    @Override
    public void onRest() {
        triggered = true;

    }

    @Override
    public void update() {
        super.update();
        if (triggered) {
            triggered = false;
            AbstractDungeon.topLevelEffects.add(new UpgradeShineEffect((float) Settings.WIDTH / 2.0F, (float) Settings.HEIGHT / 2.0F));// 99
            ArrayList<AbstractCard> upgradableCards = new ArrayList<>();


            for (AbstractCard c : AbstractDungeon.player.masterDeck.group) {
                if (c.canUpgrade()) {// 102
                    upgradableCards.add(c);// 103
                }
            }

            Collections.shuffle(upgradableCards, new Random(AbstractDungeon.miscRng.randomLong()));// 109
            if (!upgradableCards.isEmpty()) {// 111
                upgradableCards.get(0).upgrade();// 114
                AbstractDungeon.player.bottledCardUpgradeCheck(upgradableCards.get(0));// 116
                AbstractDungeon.effectList.add(new ShowCardBrieflyEffect(upgradableCards.get(0).makeStatEquivalentCopy()));// 117
            }
        }
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0] + AMOUNT + DESCRIPTIONS[1];
    }
}
