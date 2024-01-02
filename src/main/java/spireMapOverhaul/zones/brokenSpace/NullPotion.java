//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package spireMapOverhaul.zones.brokenSpace;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch2;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.localization.PotionStrings;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import com.megacrit.cardcrawl.potions.AbstractPotion.PotionColor;
import com.megacrit.cardcrawl.potions.AbstractPotion.PotionRarity;
import com.megacrit.cardcrawl.potions.AbstractPotion.PotionSize;
import com.megacrit.cardcrawl.ui.panels.PotionPopUp;
import com.megacrit.cardcrawl.ui.panels.TopPanel;

import java.util.ArrayList;

import static spireMapOverhaul.util.Wiz.adp;

public class NullPotion extends AbstractPotion {
    public static final String POTION_ID = "Potion Slot";
    private static final PotionStrings potionStrings;

    public NullPotion(int slot) {
        super(potionStrings.NAME, "Potion Slot", PotionRarity.PLACEHOLDER, PotionSize.T, PotionColor.NONE);// 18
        this.isObtained = true;// 19
        this.description = "";// 20
        this.name = "";// 21
        //this.tips.add(new PowerTip(this.name, this.description));// 22
        this.adjustPosition(slot);// 23
    }// 24

    public void use(AbstractCreature target) {
    }// 28

    @Override
    public void render(SpriteBatch sb) {
    }
    @Override
    public void renderOutline(SpriteBatch sb) {
    }
    @Override
    public void renderLightOutline(SpriteBatch sb) {
    }
    @Override
    public void renderOutline(SpriteBatch sb, Color c) {

    }

    public boolean shouldDestroy = false;

    @Override
    public void update() {
        for (AbstractPotion p : adp().potions) {
            if (p.slot == this.slot) {
                shouldDestroy = true;

            }
        }

        hb.hovered = false;
    }



    public int getPotency(int ascensionLevel) {
        return 0;// 32
    }

    public AbstractPotion makeCopy() {
        return null;// 37
    }

    @SpirePatch2(clz = PotionPopUp.class, method = "open")
    public static class OpenPatch {
        public static SpireReturn<Void> Prefix(PotionPopUp __instance, AbstractPotion potion) {
            if (potion instanceof NullPotion) {
                potion.update();

                return SpireReturn.Return();
            } else {
                return SpireReturn.Continue();
            }
        }
    }



    static {
        potionStrings = CardCrawlGame.languagePack.getPotionString("Potion Slot");// 10
    }
}
