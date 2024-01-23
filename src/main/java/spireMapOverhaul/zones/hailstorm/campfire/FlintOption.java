//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package spireMapOverhaul.zones.hailstorm.campfire;

import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.ui.campfire.AbstractCampfireOption;
import com.megacrit.cardcrawl.vfx.campfire.CampfireSleepEffect;
import com.megacrit.cardcrawl.vfx.campfire.CampfireSleepScreenCoverEffect;
import com.megacrit.cardcrawl.vfx.campfire.CampfireTokeEffect;
import spireMapOverhaul.zones.hailstorm.HailstormZone;
import spireMapOverhaul.zones.hailstorm.relics.Flint;

import static spireMapOverhaul.SpireAnniversary6Mod.makeID;

public class FlintOption extends AbstractCampfireOption {
    private static final UIStrings uiStrings;
    public static final String[] TEXT;

    public FlintOption() {
        this.label = TEXT[0];
        this.description = TEXT[1];
        this.img = ImageMaster.CAMPFIRE_REST_BUTTON;
        int healAmt = (int)((float)AbstractDungeon.player.maxHealth * 0.5F);
    }

    public void useOption() {
        CardCrawlGame.sound.play("SLEEP_BLANKET");
        AbstractDungeon.effectList.add(new CampfireSleepEffect());

        for(int i = 0; i < 30; ++i) {
            AbstractDungeon.topLevelEffects.add(new CampfireSleepScreenCoverEffect());
        }
    }

    static {
        uiStrings = CardCrawlGame.languagePack.getUIString(makeID(FlintOption.class.getSimpleName()));
        TEXT = uiStrings.TEXT;
    }
}
