package spireMapOverhaul.ui;

import com.evacipated.cardcrawl.mod.stslib.icons.AbstractCustomIcon;
import spireMapOverhaul.SpireAnniversary6Mod;
import spireMapOverhaul.util.TexLoader;

public class RewardIcon extends AbstractCustomIcon {
    public static final String ID = SpireAnniversary6Mod.makeID("reward");
    private static RewardIcon singleton;

    public RewardIcon() {
        super(ID, TexLoader.getTexture(SpireAnniversary6Mod.makeUIPath("mapIcons/reward.png")));
    }

    public static RewardIcon get() {
        if (singleton == null) {
            singleton = new RewardIcon();
        }
        return singleton;
    }
}
