package spireMapOverhaul.zones.brokenSpace.relics;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch2;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.actions.common.EmptyDeckShuffleAction;
import com.megacrit.cardcrawl.actions.common.ShuffleAction;
import com.megacrit.cardcrawl.actions.defect.ShuffleAllAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.relics.Matryoshka;
import com.megacrit.cardcrawl.relics.Sozu;
import com.megacrit.cardcrawl.relics.Sundial;
import com.megacrit.cardcrawl.ui.panels.TopPanel;

import static spireMapOverhaul.SpireAnniversary6Mod.makeID;
import static spireMapOverhaul.util.Wiz.*;

public class BrokenSozu extends BrokenRelic {
    public static final String ID = "BrokenSozu";
    public static final int AMOUNT = 10;

    public BrokenSozu() {
        super(ID, RelicTier.SPECIAL, LandingSound.CLINK, Sozu.ID);
        counter = 0;
    }

    @Override
    public void onEquip() {
        int prevAmount = adp().potionSlots;

        adp().potionSlots = 0;
        adp().potions.clear();


        for (int i = 0; i < AMOUNT; i++) {
            AbstractPotion p = AbstractDungeon.returnRandomPotion();
            adp().potions.add(i, p);
            p.setAsObtained(i);
        }

    }

    @SpirePatch2(clz = TopPanel.class, method = "destroyPotion")
    public static class DestroyPotionPatch {
        @SpirePostfixPatch
        public static void Postfix(TopPanel __instance, int slot) {
            if (adp().hasRelic(makeID(ID))) {
                adp().potions.remove(slot);
            }
        }
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0] + AMOUNT + DESCRIPTIONS[1];
    }
}