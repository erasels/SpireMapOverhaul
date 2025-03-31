package spireMapOverhaul.zones.humidity.cards.powerelic.implementation.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import spireMapOverhaul.SpireAnniversary6Mod;
import spireMapOverhaul.zones.humidity.cards.powerelic.implementation.PowerelicCard;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;

import static java.lang.System.currentTimeMillis;

public class RelicRenderPatches {

    public static boolean relicsAreBlinking(){
        if(currentTimeMillis()%1000<500){
            return true;
        }
        return false;
    }

    @SpirePatch(clz = AbstractRelic.class, method = "renderInTopPanel")
    public static class RenderPatch {
        @SpirePrefixPatch
        public static SpireReturn<Void> Patch(AbstractRelic __instance) {
            if(PowerelicCard.PowerelicRelicContainmentFields.isContained.get(__instance)){
                if(relicsAreBlinking()) {
                    return SpireReturn.Return();
                }
            }
            return SpireReturn.Continue();
        }
    }

    public static void moveInvisibleRelicsToEnd(AbstractPlayer __instance){
        ArrayList<AbstractRelic> invisibleRelics = new ArrayList<>();
        for(AbstractRelic relic : __instance.relics){
            if(RelicContainmentDetection.isContained(relic)){
                invisibleRelics.add(relic);
            }
        }
        __instance.relics.removeIf(relic -> RelicContainmentDetection.isContained(relic));
        __instance.relics.addAll(invisibleRelics);
    }

    @SpirePatch(clz = AbstractPlayer.class, method = "reorganizeRelics")
    public static class ReorganizePrefix {
        @SpirePrefixPatch
        public static void Patch(AbstractPlayer __instance) {
            try {
                moveInvisibleRelicsToEnd(__instance);
            }catch(ConcurrentModificationException e){
                SpireAnniversary6Mod.logger.info("RelicRenderPatches: comodexception while trying to rearrange relics.");
            }
        }
    }

}
