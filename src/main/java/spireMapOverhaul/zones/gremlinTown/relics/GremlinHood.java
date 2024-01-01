package spireMapOverhaul.zones.gremlinTown.relics;

import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.UpgradeShineEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardBrieflyEffect;
import spireMapOverhaul.abstracts.AbstractSMORelic;
import spireMapOverhaul.zones.gremlinTown.GremlinTown;

import static spireMapOverhaul.SpireAnniversary6Mod.makeID;
import static spireMapOverhaul.util.Wiz.adp;

public class GremlinHood extends AbstractSMORelic {
    public static final String ID = makeID(GremlinHood.class.getSimpleName());

    public GremlinHood() {
        super(ID, RelicTier.SPECIAL, LandingSound.FLAT);
    }

    public void onEquip() {
        int effectCount = 0;
        for (AbstractCard c : adp().masterDeck.group) {
            if (c.canUpgrade() && GremlinTown.getGremlinCardIds().contains(c.cardID)) {
                if (c.canUpgrade()) {
                    ++effectCount;
                    if (effectCount <= 20) {
                        float x = MathUtils.random(0.1F, 0.9F) * (float)Settings.WIDTH;
                        float y = MathUtils.random(0.2F, 0.8F) * (float)Settings.HEIGHT;
                        AbstractDungeon.effectList.add(new ShowCardBrieflyEffect(c.makeStatEquivalentCopy(), x, y));
                        AbstractDungeon.topLevelEffects.add(new UpgradeShineEffect(x, y));
                    }
                    c.upgrade();
                    AbstractDungeon.player.bottledCardUpgradeCheck(c);
                }
            }
        }
    }

    public void onObtainCard(AbstractCard c) {
        if (GremlinTown.getGremlinCardIds().contains(c.cardID) && c.canUpgrade() && !c.upgraded)
            c.upgrade();
    }
}
