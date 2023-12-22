package spireMapOverhaul.ui;

import com.evacipated.cardcrawl.mod.stslib.icons.AbstractCustomIcon;
import spireMapOverhaul.SpireAnniversary6Mod;
import spireMapOverhaul.util.TexLoader;

public class MonsterIcon extends AbstractCustomIcon {
    public static final String ID = SpireAnniversary6Mod.makeID("monster");
    private static MonsterIcon singleton;

    public MonsterIcon() {
        super(ID, TexLoader.getTexture(SpireAnniversary6Mod.makeUIPath("mapIcons/monster.png")));
    }

    public static MonsterIcon get() {
        if (singleton == null) {
            singleton = new MonsterIcon();
        }
        return singleton;
    }
}
