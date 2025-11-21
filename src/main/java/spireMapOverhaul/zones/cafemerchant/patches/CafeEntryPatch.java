package spireMapOverhaul.zones.cafemerchant.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch2;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import spireMapOverhaul.patches.ZonePatches;
import spireMapOverhaul.zones.cafemerchant.CafeMerchantZone;

import java.util.Collection;

@SpirePatch2(cls = "spireCafe.CafeRoom", method = "onEnterRoom", requiredModId = "anniv7")
public class CafeEntryPatch {
    @SpirePrefixPatch
    public static void cafeEntryMarkMerchantSeen() {
        // We only mark the merchant from the zone as seen when the cafe is about to be entered, because marking it
        // as seen any earlier would cause save/load instability (since each time you reloaded the game after saving,
        // the previously selected merchant would now be marked as seen, so you'd get a different merchant instead)
        AbstractDungeon.map.stream()
                .flatMap(Collection::stream)
                .map(node -> ZonePatches.Fields.zone.get(node))
                .filter(z -> z instanceof CafeMerchantZone)
                .map(z -> (CafeMerchantZone) z)
                .findFirst()
                .ifPresent(CafeMerchantZone::markMerchantSeen);
    }
}
