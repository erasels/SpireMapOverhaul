package spireMapOverhaul.zones.ambushed;

import static spireMapOverhaul.util.Wiz.getCurZone;

public class AmbushedUtil {
    public static boolean isInAmbushedZone() {
        return getCurZone() instanceof AmbushedZone;
    }
}
