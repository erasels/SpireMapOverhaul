package spireMapOverhaul.zones.CosmicEukotranpha.util;
import basemod.abstracts.CustomSavableRaw;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import spireMapOverhaul.zones.CosmicEukotranpha.CosmicZoneMod;

import java.util.ArrayList;
public class CZGAHSaveStuff implements CustomSavableRaw{
    static class GahData{Integer cosmicMonstersMet;Integer monstersGenned;Integer cosmicPercentage;
        GahData(Integer cosmicMonstersMet,Integer monstersGenned,Integer cosmicPercentage){this.cosmicMonstersMet=cosmicMonstersMet;this.monstersGenned=monstersGenned;this.cosmicPercentage=cosmicPercentage;}}private ArrayList<GahData>gahData=null;private Gson saveFileGson=new Gson();
    public JsonElement onSaveRaw(){this.gahData=new ArrayList<>();Integer cosmicmonstersmet=CosmicZoneGameActionHistory.cosmicMonstersMet;Integer monstersgenned=CosmicZoneGameActionHistory.monstersGenned;Integer cosmicpercentage=CosmicZoneGameActionHistory.cosmicPercentage;
        this.gahData.add(new GahData(cosmicmonstersmet,monstersgenned,cosmicpercentage));return this.saveFileGson.toJsonTree(this.gahData);}
    public void onLoadRaw(JsonElement jsonElement){try{this.gahData=(ArrayList<GahData>)this.saveFileGson.fromJson(jsonElement,(new TypeToken<ArrayList<GahData>>(){}).getType());}catch(JsonSyntaxException e){this.gahData=null;}
        if(jsonElement==null){CosmicZoneMod.logger.warn("CZGAHSaveStuff found no JSON element to load");}else if(this.gahData==null){CosmicZoneMod.logger.error("CZGAHSaveStuff failed to parse JSON");
        }else{CosmicZoneGameActionHistory.cosmicMonstersMet=gahData.get(0).cosmicMonstersMet;CosmicZoneGameActionHistory.monstersGenned=gahData.get(0).monstersGenned;CosmicZoneGameActionHistory.cosmicPercentage=gahData.get(0).cosmicPercentage;}}}



