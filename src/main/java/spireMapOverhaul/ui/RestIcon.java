package spireMapOverhaul.ui;

import com.evacipated.cardcrawl.mod.stslib.icons.AbstractCustomIcon;
import spireMapOverhaul.SpireAnniversary6Mod;
import spireMapOverhaul.util.TexLoader;

public class RestIcon extends AbstractCustomIcon {
    public static final String ID = SpireAnniversary6Mod.makeID("rest");
    private static RestIcon singleton;

    public RestIcon() {
        super(ID, TexLoader.getTexture(SpireAnniversary6Mod.makeUIPath("mapIcons/rest.png")));
    }

    public static RestIcon get() {
        if (singleton == null) {
            singleton = new RestIcon();
        }
        return singleton;
    }
}
