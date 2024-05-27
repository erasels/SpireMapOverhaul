package spireMapOverhaul.zones.hailstorm.campfire;

import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.relics.DreamCatcher;
import com.megacrit.cardcrawl.rewards.RewardItem;
import com.megacrit.cardcrawl.ui.campfire.AbstractCampfireOption;
import com.megacrit.cardcrawl.vfx.campfire.CampfireSleepEffect;
import com.megacrit.cardcrawl.vfx.campfire.CampfireSleepScreenCoverEffect;
import com.megacrit.cardcrawl.vfx.campfire.CampfireTokeEffect;
import spireMapOverhaul.util.TexLoader;
import spireMapOverhaul.zones.hailstorm.HailstormZone;
import spireMapOverhaul.zones.hailstorm.patches.CardRewardScreenFlintDreamCatcherInteractionPatch;
import spireMapOverhaul.zones.hailstorm.relics.Flint;

import java.util.ArrayList;

import static spireMapOverhaul.SpireAnniversary6Mod.makeID;
import static spireMapOverhaul.SpireAnniversary6Mod.makeUIPath;
import static spireMapOverhaul.util.Wiz.adp;

public class FlintOption extends AbstractCampfireOption {
    private static final UIStrings uiStrings;
    public static final String[] TEXT;

    private static final Texture IMG = TexLoader.getTexture(makeUIPath("Hailstorm/FlintOption.png"));

    public FlintOption() {
        this.label = TEXT[0];
        this.description = TEXT[1];
        this.img = IMG;
        int healAmt = (int)((float)AbstractDungeon.player.maxHealth * 0.6F);
    }

    public void useOption() {
        CardCrawlGame.sound.play("SLEEP_BLANKET");
        //First time
        AbstractDungeon.effectList.add(new CampfireSleepEffect());

        //Second time + handling Dream Catcher
        if (AbstractDungeon.player.hasRelic(DreamCatcher.ID)) {
            CardRewardScreenFlintDreamCatcherInteractionPatch.trigger = true;
        }
        AbstractDungeon.effectList.add(new CampfireSleepEffect());

        //
        for(int i = 0; i < 30; ++i) {
            AbstractDungeon.topLevelEffects.add(new CampfireSleepScreenCoverEffect());
        }

        if (adp().hasRelic(Flint.ID))
            adp().getRelic(Flint.ID).onTrigger();
    }

    static {
        uiStrings = CardCrawlGame.languagePack.getUIString(makeID(FlintOption.class.getSimpleName()));
        TEXT = uiStrings.TEXT;
    }
}
