package spireMapOverhaul.zones.manasurge.ui.extraicons;

import com.badlogic.gdx.graphics.Texture;
import com.evacipated.cardcrawl.mod.stslib.icons.AbstractCustomIcon;
import spireMapOverhaul.SpireAnniversary6Mod;
import spireMapOverhaul.util.TexLoader;

import static spireMapOverhaul.SpireAnniversary6Mod.makeUIPath;

public class EnchantmentIcon extends AbstractCustomIcon {
    public static final String ID = SpireAnniversary6Mod.makeID("ManaSurge:Enchantment");
    private static final Texture ICON = TexLoader.getTexture(makeUIPath("ManaSurge/extraIcons/PositiveEnchantmentIcon.png"));

    private static EnchantmentIcon singleton;

    public EnchantmentIcon() {
        super(ID, ICON);
    }

    public static EnchantmentIcon get()
    {
        if (singleton == null) {
            singleton = new EnchantmentIcon();
        }
        return singleton;
    }
}