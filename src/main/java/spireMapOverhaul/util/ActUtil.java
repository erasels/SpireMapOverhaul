package spireMapOverhaul.util;

import com.evacipated.cardcrawl.modthespire.Loader;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ActUtil {
    public static int getRealActNum() {
        if (Loader.isModLoaded("actlikeit")) {
            // If ActLikeIt is loaded, it has the real act number for us
            try {
                Class<?> clz = Class.forName("actlikeit.savefields.BehindTheScenesActNum");
                Method m = clz.getMethod("getActNum");
                return (int)m.invoke(null);
            } catch (IllegalAccessException | ClassNotFoundException | NoSuchMethodException | InvocationTargetException e) {
                e.printStackTrace();
                throw new RuntimeException("Failed when trying to call BehindTheScenesActNum.getActNum()", e);
            }
        }
        else {
            // If ActLikeIt isn't loaded, we can safely enumerate the possible acts and use that to determine the real act number
            switch (AbstractDungeon.id) {
                case Exordium.ID:
                    return 1;
                case TheCity.ID:
                case "theJungle:Jungle":
                    return 2;
                case TheBeyond.ID:
                    return 3;
                case TheEnding.ID:
                case "EYB:TheUnnamedReign":
                    return 4;
                default:
                    throw new RuntimeException("Unrecognized act ID: " + AbstractDungeon.id + ". When ActLikeIt isn't loaded, the only possible acts should be the vanilla four and a few special cases.");
            }
        }
    }
}
