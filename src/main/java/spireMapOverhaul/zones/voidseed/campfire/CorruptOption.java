package spireMapOverhaul.zones.voidseed.campfire;

import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.ui.campfire.AbstractCampfireOption;
import spireMapOverhaul.util.TexLoader;

import static spireMapOverhaul.SpireAnniversary6Mod.makeID;
import static spireMapOverhaul.SpireAnniversary6Mod.makeUIPath;

public class CorruptOption extends AbstractCampfireOption {
    private static final UIStrings uiStrings;
    public static final String[] TEXT;

    public CorruptOption(boolean active) {
        this.label = TEXT[0];// 14
        this.usable = active;// 15
        this.updateUsability(active);// 16
    }// 17

    public void updateUsability(boolean canUse) {
        this.description = canUse ? TEXT[1] : TEXT[2];// 20
        this.img = TexLoader.getTexture(makeUIPath("VoidSeed/campfire/corrupt" + (canUse ? "" : "Disabled") + ".png"));
    }// 22

    public void useOption() {
        if (this.usable) {// 26
            AbstractDungeon.effectList.add(new CampfireCorruptEffect());// 27
        }

    }// 29

    static {
        uiStrings = CardCrawlGame.languagePack.getUIString(makeID("CorruptOption"));// 9
        TEXT = uiStrings.TEXT;// 11
    }
}
