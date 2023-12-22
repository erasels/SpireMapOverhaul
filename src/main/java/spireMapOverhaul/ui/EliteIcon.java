package spireMapOverhaul.ui;

import com.evacipated.cardcrawl.mod.stslib.icons.AbstractCustomIcon;
import spireMapOverhaul.SpireAnniversary6Mod;
import spireMapOverhaul.util.TexLoader;

public class EliteIcon extends AbstractCustomIcon {
    public static final String ID = SpireAnniversary6Mod.makeID("elite");
    private static EliteIcon singleton;

    public EliteIcon() {
        super(ID, TexLoader.getTexture(SpireAnniversary6Mod.makeUIPath("mapIcons/elite.png")));
    }

    public static EliteIcon get() {
        if (singleton == null) {
            singleton = new EliteIcon();
        }
        return singleton;
    }
}
