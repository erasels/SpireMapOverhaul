package spireMapOverhaul.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch2;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.map.MapRoomNode;
import spireMapOverhaul.abstracts.AbstractZone;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashSet;

import static spireMapOverhaul.SpireAnniversary6Mod.makeID;

@SpirePatch2(
        cls = "sayTheSpire.map.BaseRoomNode",
        method = "getTags",
        requiredModId = "Say_the_Spire",
        optional = true
)
public class SayTheSpireCompatibilityPatch {
    private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString(makeID(SayTheSpireCompatibilityPatch.class.getSimpleName()));

    @SpirePostfixPatch
    public static HashSet<String> addZoneTag(Object __instance, HashSet<String> __result) {
        try {
            Class<?> baseRoomNode = Class.forName("sayTheSpire.map.BaseRoomNode");
            Method m = baseRoomNode.getMethod("getGameNode");
            MapRoomNode node = (MapRoomNode)m.invoke(__instance);
            if (node != null) {
                AbstractZone zone = ZonePatches.Fields.zone.get(node);

                if (zone != null) {
                    String s = uiStrings.TEXT[0].replace("{0}", zone.name);
                    __result.add(s);
                }
            }
        } catch (ClassNotFoundException | InvocationTargetException | NoSuchMethodException | IllegalAccessException e) {
            e.printStackTrace();
            throw new RuntimeException("Error adding Say the Spire tag for map node", e);
        }

        return __result;
    }
}
