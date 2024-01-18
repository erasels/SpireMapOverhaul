package spireMapOverhaul.zones.gremlinTown.relics;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import spireMapOverhaul.abstracts.AbstractSMORelic;
import spireMapOverhaul.zones.gremlinTown.GremlinTown;

import static spireMapOverhaul.SpireAnniversary6Mod.makeID;

public class Bowtie extends AbstractSMORelic {
    public static final String ID = makeID(Bowtie.class.getSimpleName());

    public Bowtie() {
        super(ID, GremlinTown.ID, RelicTier.SPECIAL, LandingSound.FLAT);
    }

    private boolean isInMerchantStock(AbstractCard c) {
        if (!AbstractDungeon.shopScreen.isActive)
            return false;

        if (AbstractDungeon.shopScreen.coloredCards == null)
            return false;
        if (AbstractDungeon.shopScreen.coloredCards.contains(c))
            return true;

        if (AbstractDungeon.shopScreen.colorlessCards == null)
            return false;
        return AbstractDungeon.shopScreen.colorlessCards.contains(c);
    }

    public void onPreviewObtainCard(AbstractCard c) {
        onObtainCard(c);
    }

    public void onObtainCard(AbstractCard c) {
        if (isInMerchantStock(c) && c.canUpgrade())
            c.upgrade();
    }

    public boolean canSpawn() {
        return Settings.isEndless || AbstractDungeon.floorNum <= 48;
    }
}
