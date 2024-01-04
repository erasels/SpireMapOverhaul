package spireMapOverhaul.zones.brokenSpace.relics;

import basemod.CustomEventRoom;
import com.evacipated.cardcrawl.modthespire.lib.ByRef;
import com.evacipated.cardcrawl.modthespire.lib.SpireInstrumentPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch2;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.map.DungeonMap;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.relics.Sundial;
import com.megacrit.cardcrawl.relics.WingBoots;
import com.megacrit.cardcrawl.rooms.EventRoom;
import javassist.CannotCompileException;
import javassist.expr.ExprEditor;
import javassist.expr.FieldAccess;
import javassist.expr.NewExpr;
import spireMapOverhaul.BetterMapGenerator;
import spireMapOverhaul.SpireAnniversary6Mod;
import spireMapOverhaul.patches.BetterMapGenPatch;
import spireMapOverhaul.zones.WingBootEvent;
import spireMapOverhaul.zones.brokenSpace.FakeEventRoom;

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
            clz = AbstractDungeon.class,
            method = "generateRoom"
    )
    @SpirePatch2(
            clz = AbstractDungeon.class,
            method = "generateRoomTypes"
    )
    public static class WingBootEventPatch {
        @SpireInstrumentPatch
        public static ExprEditor patch() {
            return new ExprEditor() {
                @Override
                public void edit(NewExpr e) throws CannotCompileException {
                    if (e.getClassName().equals(EventRoom.class.getName())) {
                        e.replace("{" +
                                "if (" + AbstractDungeon.class.getName() + ".player.hasRelic(" + SpireAnniversary6Mod.class.getName() + ".makeID(" + BrokenWingBoots.class.getName() + ".ID))) {" +
                                "$_ = new " + FakeEventRoom.class.getName() + "(new " + WingBootEvent.class.getName() + "());" +
                                SpireAnniversary6Mod.class.getName() + ".logger.info(\"WingBootEventPatch\");" +
                                "} else {" +
                                "$_ = $proceed($$);" +
                                "}" +
                                "}");
                    }
                }
            };
        }
    }
}
