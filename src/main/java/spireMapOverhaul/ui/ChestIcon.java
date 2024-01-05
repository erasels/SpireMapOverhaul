package spireMapOverhaul.ui;

import com.evacipated.cardcrawl.mod.stslib.icons.AbstractCustomIcon;
import spireMapOverhaul.SpireAnniversary6Mod;
import spireMapOverhaul.util.TexLoader;

public class ChestIcon extends AbstractCustomIcon {
    public static final String ID = SpireAnniversary6Mod.makeID("chest");
    private static ChestIcon singleton;

    public ChestIcon() {
        super(ID, TexLoader.getTexture(SpireAnniversary6Mod.makeUIPath("mapIcons/chest.png")));
    }

    public static ChestIcon get() {
        if (singleton == null) {
            singleton = new ChestIcon();
        }
        return singleton;
    }
}
