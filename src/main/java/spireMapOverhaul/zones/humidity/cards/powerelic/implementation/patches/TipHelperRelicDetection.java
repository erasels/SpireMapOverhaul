package spireMapOverhaul.zones.humidity.cards.powerelic.implementation.patches;

import com.badlogic.gdx.graphics.Texture;
import com.evacipated.cardcrawl.modthespire.Loader;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import spireMapOverhaul.util.Wiz;

public class TipHelperRelicDetection {
    public static boolean detectRelics(Texture img) {
        if (Loader.isModLoaded("anniv7")) return false;
        for (AbstractRelic relic : Wiz.adp().relics) {
            if (img == relic.img) {
                return true;
            }
        }
        return false;
    }
}
