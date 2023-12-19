package spireMapOverhaul.util;

import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class ActUtil {
    public static int getRealActNum() {
        if (Settings.isEndless) return AbstractDungeon.actNum % 3;
        return AbstractDungeon.actNum;
    }
}
