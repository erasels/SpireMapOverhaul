package spireMapOverhaul.zones.manasurge.ui.extraicons;

import com.badlogic.gdx.graphics.Texture;
import com.evacipated.cardcrawl.mod.stslib.icons.AbstractCustomIcon;
import spireMapOverhaul.SpireAnniversary6Mod;
import spireMapOverhaul.util.TexLoader;

import static spireMapOverhaul.SpireAnniversary6Mod.makeImagePath;

public class BlightIcon extends AbstractCustomIcon {
    public static final String ID = SpireAnniversary6Mod.makeID("ManaSurge:Blight");
    private static final Texture ICON = TexLoader.getTexture(makeImagePath("ui/ManaSurge/extraIcons/NegativeEnchantmentIcon.png"));

    private static BlightIcon singleton;

    public BlightIcon() {
        super(ID, ICON);
    }

    public static BlightIcon get()
    {
        if (singleton == null) {
            singleton = new BlightIcon();
        }
        return singleton;
    }
}