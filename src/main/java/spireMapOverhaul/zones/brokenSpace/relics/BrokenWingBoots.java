package spireMapOverhaul.zones.brokenSpace.relics;

import com.evacipated.cardcrawl.modthespire.lib.ByRef;
import com.evacipated.cardcrawl.modthespire.lib.SpireInstrumentPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch2;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.map.DungeonMap;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.relics.Sundial;
import com.megacrit.cardcrawl.relics.WingBoots;
import javassist.CannotCompileException;
import javassist.expr.ExprEditor;
import javassist.expr.FieldAccess;
import javassist.expr.NewExpr;
import spireMapOverhaul.BetterMapGenerator;
import spireMapOverhaul.patches.BetterMapGenPatch;

import static spireMapOverhaul.SpireAnniversary6Mod.makeID;

public class BrokenWingBoots extends BrokenRelic {
    public static final String ID = "BrokenWingBoots";

    public BrokenWingBoots() {
        super(ID, RelicTier.SPECIAL, LandingSound.CLINK, WingBoots.ID);
    }

    @Override
    public void onEquip() {
        counter = -AbstractDungeon.actNum;
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

    @Override
    public boolean canSpawn() {
        return AbstractDungeon.actNum < 3 || Settings.isEndless;
    }

    @SpirePatch2(
            clz = BetterMapGenPatch.class,
            method = "altGen"
    )
    public static class BrokenWingBootsPatch {
        public static void Prefix(@ByRef int[] pathDensity) {
            if (AbstractDungeon.player.hasRelic(makeID(ID))) {
                for (AbstractRelic r : AbstractDungeon.player.relics) {
                    if (r instanceof BrokenWingBoots) {
                        BrokenWingBoots brokenWingBoots = (BrokenWingBoots) r;
                        // if it is any act after the one where the relic was obtained
                        if (AbstractDungeon.actNum > -brokenWingBoots.counter) {
                            pathDensity[0] += 500;
                        }
                    }
                }

            }
        }
    }
}
