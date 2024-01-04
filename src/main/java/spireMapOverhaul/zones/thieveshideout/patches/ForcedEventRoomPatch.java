package spireMapOverhaul.zones.thieveshideout.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpireInstrumentPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.saveAndContinue.SaveFile;
import javassist.CannotCompileException;
import javassist.expr.ExprEditor;
import javassist.expr.MethodCall;
import spireMapOverhaul.zones.thieveshideout.rooms.ForcedEventRoom;

@SpirePatch(clz = AbstractDungeon.class, method = "nextRoomTransition", paramtypez = { SaveFile.class })
public class ForcedEventRoomPatch {
    public static class ForcedEventRoomExprEditor extends ExprEditor {
        @Override
        public void edit(MethodCall methodCall) throws CannotCompileException {
            if (methodCall.getClassName().equals(AbstractDungeon.class.getName()) && methodCall.getMethodName().equals("generateRoom")) {
                methodCall.replace(String.format("{ if (%1$s.isForcedEventRoom(nextRoom.room)) { $_ = nextRoom.room; } else { $_ = $proceed($$); } }", ForcedEventRoomPatch.class.getName()));
            }
        }
    }

    @SpireInstrumentPatch
    public static ExprEditor forcedEventRoomPatch() {
        return new ForcedEventRoomExprEditor();
    }

    public static boolean isForcedEventRoom(AbstractRoom room) {
        return room instanceof ForcedEventRoom;
    }
}
