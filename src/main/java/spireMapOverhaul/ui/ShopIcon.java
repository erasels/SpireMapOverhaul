package spireMapOverhaul.ui;

import com.evacipated.cardcrawl.mod.stslib.icons.AbstractCustomIcon;
import spireMapOverhaul.SpireAnniversary6Mod;
import spireMapOverhaul.util.TexLoader;

public class ShopIcon extends AbstractCustomIcon {
    public static final String ID = SpireAnniversary6Mod.makeID("shop");
    private static ShopIcon singleton;

    public ShopIcon() {
        super(ID, TexLoader.getTexture(SpireAnniversary6Mod.makeUIPath("mapIcons/shop.png")));
    }

    public static ShopIcon get() {
        if (singleton == null) {
            singleton = new ShopIcon();
        }
        return singleton;
    }
}
