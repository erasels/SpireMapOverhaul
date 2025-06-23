package spireMapOverhaul.zones.humidity;

import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.dungeons.Exordium;
import com.megacrit.cardcrawl.helpers.RelicLibrary;
import com.megacrit.cardcrawl.relics.*;
import spireMapOverhaul.util.Wiz;

import java.util.*;

public class CardedRareRelicList {

    public static final ArrayList<String> dTierRareRelics;
    public static final ArrayList<String> cTierRareRelics;
    public static final ArrayList<String> bTierRareRelics;
    public static final ArrayList<String> aTierRareRelics;
    public static final ArrayList<String> sTierRareRelics;

    static{
        //Rare relics only.
        //Feel free to rearrange these as you see fit.
        //Note that tier placement is based on how these relics function *when carded*.
        dTierRareRelics = new ArrayList<>(Arrays.asList(
                DuVuDoll.ID,Girya.ID,PeacePipe.ID,Shovel.ID
        ));
        cTierRareRelics = new ArrayList<>(Arrays.asList(
                CaptainsWheel.ID,Ginger.ID,MagicFlower.ID,StoneCalendar.ID,Torii.ID,ToughBandages.ID,Turnip.ID,UnceasingTop.ID
        ));
        bTierRareRelics = new ArrayList<>(Arrays.asList(
                BirdFacedUrn.ID,Calipers.ID,ChampionsBelt.ID,CharonsAshes.ID,CloakClasp.ID,EmotionChip.ID,GamblingChip.ID,GoldenEye.ID,Pocketwatch.ID,TheSpecimen.ID,ThreadAndNeedle.ID,Tingsha.ID,TungstenRod.ID
        ));
        aTierRareRelics = new ArrayList<>(Arrays.asList(
                IceCream.ID,IncenseBurner.ID,LizardTail.ID,PrayerWheel.ID,WingBoots.ID
        ));
        sTierRareRelics = new ArrayList<>(Arrays.asList(
                DeadBranch.ID,FossilizedHelix.ID
        ));
    }

    public static AbstractRelic fetchRelicForCarding(){
        //Balance change:
        // because entering Humidity is typically a net loss,
        // the player can now receive ANY rare relic with equal chance.
        // Let's see how this goes and make further changes later if necessary.

        ArrayList<String> standardRelics=new ArrayList<>();
        standardRelics.addAll(dTierRareRelics);
        standardRelics.addAll(cTierRareRelics);
        standardRelics.addAll(bTierRareRelics);
        standardRelics.addAll(aTierRareRelics);
        standardRelics.addAll(sTierRareRelics);

        String relicID="";
        relicID= getUncollectedRelicFromPool(standardRelics);
        if(relicID.isEmpty()) {
            //if we have somehow exhausted the entire rare relic pool, return another Turnip. it won't break anything.
            return new Turnip();
        }
        Wiz.getRelicPool(AbstractRelic.RelicTier.RARE).remove(relicID);
        AbstractRelic finalRelic = RelicLibrary.getRelic(relicID).makeCopy();
        return finalRelic;
    }

    public static String getUncollectedRelicFromPool(List<String> relics){
        Collections.shuffle(relics, new Random(AbstractDungeon.cardRng.randomLong()));
        while(!relics.isEmpty()){
            String relicID = relics.remove(0);
            if(isRelicInPlayerRelicPool(relicID)){
                return relicID;
            }
        }
        return "";
    }

    public static boolean isRelicInPlayerRelicPool(String relicID){
        return Wiz.getRelicPool(AbstractRelic.RelicTier.RARE).contains(relicID);
    }

}
