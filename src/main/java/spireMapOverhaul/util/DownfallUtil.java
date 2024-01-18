package spireMapOverhaul.util;

import com.evacipated.cardcrawl.modthespire.Loader;

import java.lang.reflect.Field;

public class DownfallUtil {
    public static boolean isDownfallMode() {
        if (Loader.isModLoaded("downfall")) {
            try {
                Class<?> clz = Class.forName("downfall.patches.EvilModeCharacterSelect");
                Field f = clz.getField("evilMode");
                return f.getBoolean(null);
            } catch (ClassNotFoundException | NoSuchFieldException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return false;
    }
}
