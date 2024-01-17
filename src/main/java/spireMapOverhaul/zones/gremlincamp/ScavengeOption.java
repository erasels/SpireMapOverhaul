package spireMapOverhaul.zones.gremlincamp;

import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.ui.campfire.AbstractCampfireOption;
import com.megacrit.cardcrawl.vfx.RainingGoldEffect;
import spireMapOverhaul.util.TexLoader;
import spireMapOverhaul.util.Wiz;

import static spireMapOverhaul.SpireAnniversary6Mod.makeID;
import static spireMapOverhaul.SpireAnniversary6Mod.makeUIPath;

public class ScavengeOption extends AbstractCampfireOption
{
    private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString(makeID("ScavengeOption"));
    public static final String[] TEXT = uiStrings.TEXT;
    protected int minGold, maxGold;

    public ScavengeOption() {
        this.label = TEXT[0];
        minGold = getMinGold();
        maxGold = getMaxGold();
        this.description = String.format(TEXT[1], minGold, maxGold);
        this.img = TexLoader.getTexture(makeUIPath("GremlinCamp/scavenge.png"));
    }

    public int getMinGold() {
        if(Wiz.getCurZone() instanceof GremlinCamp) return 40;
        return 30;
    }

    public int getMaxGold() {
        if(Wiz.getCurZone() instanceof GremlinCamp) return 55;
        return 50;
    }

    @Override
    public void useOption() {
        int monneh = AbstractDungeon.treasureRng.random(minGold, maxGold);
        AbstractDungeon.effectList.add(new RainingGoldEffect(100));
        AbstractDungeon.effectList.add(new ScavengeEffect(monneh));
    }
}