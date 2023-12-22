package spireMapOverhaul.ui;

import com.evacipated.cardcrawl.mod.stslib.icons.AbstractCustomIcon;
import spireMapOverhaul.SpireAnniversary6Mod;
import spireMapOverhaul.util.TexLoader;

public class EventIcon extends AbstractCustomIcon {
    public static final String ID = SpireAnniversary6Mod.makeID("event");
    private static EventIcon singleton;

    public EventIcon() {
        super(ID, TexLoader.getTexture(SpireAnniversary6Mod.makeUIPath("mapIcons/event.png")));
    }

    public static EventIcon get() {
        if (singleton == null) {
            singleton = new EventIcon();
        }
        return singleton;
    }
}
