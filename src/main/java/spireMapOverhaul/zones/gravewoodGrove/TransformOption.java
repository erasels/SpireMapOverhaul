package spireMapOverhaul.zones.gravewoodGrove;

import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.ui.campfire.AbstractCampfireOption;
import com.megacrit.cardcrawl.vfx.RainingGoldEffect;
import spireMapOverhaul.util.TexLoader;
import spireMapOverhaul.util.Wiz;
import spireMapOverhaul.zones.gremlincamp.GremlinCamp;
import spireMapOverhaul.zones.gremlincamp.ScavengeEffect;

import static spireMapOverhaul.SpireAnniversary6Mod.makeID;
import static spireMapOverhaul.SpireAnniversary6Mod.makeUIPath;

public class TransformOption extends AbstractCampfireOption {
    private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString(makeID("TransformOption"));
    public static final String[] TEXT = uiStrings.TEXT;

    public TransformOption() {
        this.label = TEXT[0];
        this.description = TEXT[1];
        this.img = TexLoader.getTexture(makeUIPath("GravewoodGrove/transform.png"));
    }


    @Override
    public void useOption() {
        AbstractDungeon.effectList.add(new TransformEffect());
    }
}