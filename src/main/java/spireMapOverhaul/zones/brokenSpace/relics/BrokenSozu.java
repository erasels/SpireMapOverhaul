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
import com.megacrit.cardcrawl.potions.PotionSlot;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.relics.Matryoshka;
import com.megacrit.cardcrawl.relics.Sozu;
import com.megacrit.cardcrawl.relics.Sundial;
import com.megacrit.cardcrawl.ui.panels.TopPanel;
import spireMapOverhaul.zones.brokenSpace.NullPotion;

import java.util.ArrayList;

import static spireMapOverhaul.SpireAnniversary6Mod.makeID;
import static spireMapOverhaul.util.Wiz.*;

public class BrokenSozu extends BrokenRelic {
    public static final String ID = "BrokenSozu";
    public static final int AMOUNT = 10;

    public BrokenSozu() {
        super(ID, RelicTier.SPECIAL, LandingSound.CLINK, Sozu.ID);
    }

    @Override
    public void onEquip() {
        ArrayList<AbstractPotion> potions = new ArrayList<>(adp().potions);

        adp().potionSlots = AMOUNT;
        adp().potions.clear();


        for (int i = 0; i < AMOUNT; i++) {
            adp().potions.add(new PotionSlot(i));
        }

        // re-add original potions
        for (int i = 0; i < potions.size(); i++) {
            AbstractPotion p = potions.get(i);
            adp().potions.set(i, p);
            p.setAsObtained(i);
        }
        // add new potions
        for (int i = potions.size(); i < (AMOUNT / 2) + potions.size(); i++) {
            AbstractPotion p = AbstractDungeon.returnRandomPotion();
            adp().potions.set(i, p);
            p.setAsObtained(i);
        }


    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0] + AMOUNT + DESCRIPTIONS[1];
    }

    @SpirePatch2(clz = TopPanel.class, method = "destroyPotion")
    public static class DestroyPotionPatch {
        @SpirePostfixPatch
        public static void Postfix(TopPanel __instance, int slot) {
            if (adp().hasRelic(makeID(ID))) {
                adp().potionSlots--;
                ArrayList<AbstractPotion> potions = new ArrayList<>(adp().potions);
                adp().potions.clear();
                potions.remove(slot);
                for (int i = 0; i < potions.size(); i++) {
                    AbstractPotion p = potions.get(i);
                    adp().potions.add(i, p);
                    p.setAsObtained(i);
                }
            }
        }
    }
}