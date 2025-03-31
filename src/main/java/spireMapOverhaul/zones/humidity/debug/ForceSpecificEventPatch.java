package spireMapOverhaul.zones.humidity.debug;

import com.evacipated.cardcrawl.modthespire.lib.ByRef;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch2;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.megacrit.cardcrawl.helpers.EventHelper;
import spireMapOverhaul.zones.humidity.HumidityZone;

public class ForceSpecificEventPatch {

    //Forces every event to use the ID given in HumidityZone.DEBUG_FORCE_EVENT_ID, if ID is not empty.
    //For testing whether relic cards are added correctly when entering the Humidity biome on a combat event.
    // * No effect if a non-event room is rolled (monster/treasure).
    @SpirePatch2(clz= EventHelper.class,method="getEvent")
    public static class ReplaceEvent{
        @SpirePrefixPatch
        public static void Foo(@ByRef String[] key){
            if(HumidityZone.isInZone() && !HumidityZone.DEBUG_FORCE_EVENT_ID.isEmpty()) {
                key[0] = HumidityZone.DEBUG_FORCE_EVENT_ID;
            }
        }
    }


}
