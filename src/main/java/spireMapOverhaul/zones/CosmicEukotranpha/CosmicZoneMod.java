package spireMapOverhaul.zones.CosmicEukotranpha;
import basemod.AutoAdd;
import basemod.BaseMod;
import basemod.interfaces.EditCardsSubscriber;
import basemod.interfaces.EditKeywordsSubscriber;
import basemod.interfaces.EditStringsSubscriber;
import basemod.interfaces.PostInitializeSubscriber;
import spireMapOverhaul.SpireAnniversary6Mod;
import spireMapOverhaul.zones.CosmicEukotranpha.cards.BaseCard;
import spireMapOverhaul.zones.CosmicEukotranpha.monsters.*;
import spireMapOverhaul.zones.CosmicEukotranpha.util.CZGAHSaveStuff;
import spireMapOverhaul.zones.CosmicEukotranpha.util.GeneralUtils;
import spireMapOverhaul.zones.CosmicEukotranpha.util.KeywordInfo;
import spireMapOverhaul.zones.CosmicEukotranpha.util.TextureLoader;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.evacipated.cardcrawl.modthespire.Loader;
import com.evacipated.cardcrawl.modthespire.ModInfo;
import com.evacipated.cardcrawl.modthespire.Patcher;
import com.evacipated.cardcrawl.modthespire.lib.SpireEnum;
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;
import com.google.gson.Gson;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.TheBeyond;
import com.megacrit.cardcrawl.localization.*;
import com.megacrit.cardcrawl.monsters.MonsterGroup;
import com.megacrit.cardcrawl.monsters.MonsterInfo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.scannotation.AnnotationDB;

import java.nio.charset.StandardCharsets;
import java.util.*;
public class CosmicZoneMod{
public static final String modID="anniv6";
public static final Logger logger=LogManager.getLogger(modID); //Used to output to the console.
public static String makeID(String id){return modID+":"+id;}
private static final String resourcesFolder="anniv6Resources";
@SpireEnum public static AbstractCard.CardTags CosmicZoneCosmicCard;
public static String localizationPath(String lang,String file){return resourcesFolder+"/localization/"+lang+"/CosmicEukotranpha/"+file;}
public static String cardPath(String file){return resourcesFolder+"/images/CosmicEukotranpha/"+file;}
public static String imagePath(String file){return resourcesFolder+"/images/CosmicEukotranpha/"+file;}
public static String characterPath(String file){return resourcesFolder+"/images/CosmicEukotranpha/character/"+file;}
public static String powerPath(String file){return resourcesFolder+"/images/CosmicEukotranpha/powers/"+file;}
public static String relicPath(String file){return resourcesFolder+"/images/CosmicEukotranpha/relics/"+file;}


}
/*
@SpireInitializer
public class CosmicZoneMod implements
        EditStringsSubscriber,
        EditKeywordsSubscriber,
        PostInitializeSubscriber,EditCardsSubscriber{
    public static ModInfo info;
    public static String modID; //Edit your pom.xml to change this
    static { loadModInfo(); }
    public static final Logger logger = LogManager.getLogger(modID); //Used to output to the console.
    private static final String resourcesFolder = "basicmod";
    
    //This is used to prefix the IDs of various objects like cards and relics,
    //to avoid conflicts between different mods using the same name for things.
    public static String makeID(String id) {
        return modID + ":" + id;
    }

    //This will be called by ModTheSpire because of the @SpireInitializer annotation at the top of the class.
    public static void initialize() {
        new CosmicZoneMod();
    }


    @Override public void receiveEditCards(){new AutoAdd(modID).packageFilter(BaseCard.class).setDefaultSeen(true).cards();}

@SpireEnum public static AbstractCard.CardTags CosmicZoneCosmicCard;
    public CosmicZoneMod() {
        BaseMod.subscribe(this); //This will make BaseMod trigger all the subscribers at their appropriate times.
        logger.info(modID + " subscribed to BaseMod.");
        logger.info("Adding Save Fields (Cosmic Zone)");BaseMod.addSaveField("CosmicZoneMod:CZGAHSaveStuff",new CZGAHSaveStuff());logger.info("Done Adding Save Fields (Cosmic Zone)");
    }

    @Override
    public void receivePostInitialize() {
        //This loads the image used as an icon in the in-game mods menu.
        Texture badgeTexture = TextureLoader.getTexture(imagePath("badge.png"));
        //Set up the mod information displayed in the in-game mods menu.
        //The information used is taken from your pom.xml file.
        BaseMod.registerModBadge(badgeTexture, info.Name, GeneralUtils.arrToString(info.Authors), info.Description, null);



        BaseMod.addMonster(Claumissier.ID,()->new Claumissier());
        BaseMod.addStrongMonsterEncounter(TheBeyond.ID,new MonsterInfo(Claumissier.ID,0));
        BaseMod.addMonster(ConstellationNeck.ID,()->new ConstellationNeck());
        BaseMod.addEliteEncounter(TheBeyond.ID,new MonsterInfo(ConstellationNeck.ID,0));
        BaseMod.addMonster(HairClipApple.ID,()->new HairClipApple());
        BaseMod.addEliteEncounter(TheBeyond.ID,new MonsterInfo(HairClipApple.ID,0));
        BaseMod.addMonster(Man.ID,()->new Man());
        BaseMod.addStrongMonsterEncounter(TheBeyond.ID,new MonsterInfo(Man.ID,0));
        BaseMod.addMonster(SaventStars.ID,()->new SaventStars());
        BaseMod.addMonsterEncounter(TheBeyond.ID,new MonsterInfo(SaventStars.ID,0));
        BaseMod.addMonster(SevenHandedTwinhead.ID,()->new SevenHandedTwinhead());
        BaseMod.addMonsterEncounter(TheBeyond.ID,new MonsterInfo(SaventStars.ID,0));

        BaseMod.addMonster(LunaFloraAstrellia.ID,()->new LunaFloraAstrellia());
        BaseMod.addMonsterEncounter(TheBeyond.ID,new MonsterInfo(LunaFloraAstrellia.ID,0));
        
        BaseMod.addEvent(CubeOfReflectionsEvent.ID,CubeOfReflectionsEvent.class,TheBeyond.ID);
        BaseMod.addEvent(TightropeOfDeserts.ID,TightropeOfDeserts.class,TheBeyond.ID);
        BaseMod.addEvent(UnknownVoice.ID,UnknownVoice.class,TheBeyond.ID);
        
        BaseMod.addEvent(AstralightRoads.ID,AstralightRoads.class,TheBeyond.ID);
        BaseMod.addEvent(CorpseColumn.ID,CorpseColumn.class,TheBeyond.ID);
        BaseMod.addEvent(CrystalsEvent.ID,CrystalsEvent.class,TheBeyond.ID);
        BaseMod.addEvent(DancingDevil.ID,DancingDevil.class,TheBeyond.ID);
        BaseMod.addEvent(DarknessInTheLight.ID,DarknessInTheLight.class,TheBeyond.ID);
        BaseMod.addEvent(FallingInvitation.ID,FallingInvitation.class,TheBeyond.ID);
        BaseMod.addEvent(MonochromeRift.ID,MonochromeRift.class,TheBeyond.ID);
        BaseMod.addEvent(PinkRainEvent.ID,PinkRainEvent.class,TheBeyond.ID);
        BaseMod.addEvent(SleepInTheTrueNight.ID,SleepInTheTrueNight.class,TheBeyond.ID);
    }

    /*----------Localization----------
    
 

    //This is used to load the appropriate localization files based on language.
    private static String getLangString()
    {
        return Settings.language.name().toLowerCase();
    }
    private static final String defaultLanguage = "eng";

    public static final Map<String, KeywordInfo> keywords = new HashMap<>();

    @Override
    public void receiveEditStrings() {
        /*
            First, load the default localization.
            Then, if the current language is different, attempt to load localization for that language.
            This results in the default localization being used for anything that might be missing.
            The same process is used to load keywords slightly below.
        
        
        loadLocalization(defaultLanguage); //no exception catching for default localization; you better have at least one that works.
        if (!defaultLanguage.equals(getLangString())) {
            try {
                loadLocalization(getLangString());
            }
            catch (GdxRuntimeException e) {
                e.printStackTrace();
            }
        }
    }

    private void loadLocalization(String lang) {
        //While this does load every type of localization, most of these files are just outlines so that you can see how they're formatted.
        //Feel free to comment out/delete any that you don't end up using.
        BaseMod.loadCustomStringsFile(CardStrings.class,
                localizationPath(lang, "CardStrings.json"));
        BaseMod.loadCustomStringsFile(CharacterStrings.class,
                localizationPath(lang, "CharacterStrings.json"));
        BaseMod.loadCustomStringsFile(EventStrings.class,
                localizationPath(lang, "EventStrings.json"));
        BaseMod.loadCustomStringsFile(MonsterStrings.class,localizationPath(lang,"MonsterStrings.json"));
        BaseMod.loadCustomStringsFile(OrbStrings.class,
                localizationPath(lang, "OrbStrings.json"));
        BaseMod.loadCustomStringsFile(PotionStrings.class,
                localizationPath(lang, "PotionStrings.json"));
        BaseMod.loadCustomStringsFile(PowerStrings.class,
                localizationPath(lang, "PowerStrings.json"));
        BaseMod.loadCustomStringsFile(RelicStrings.class,
                localizationPath(lang, "RelicStrings.json"));
        BaseMod.loadCustomStringsFile(UIStrings.class,
                localizationPath(lang, "UIStrings.json"));
    }

    @Override
    public void receiveEditKeywords()
    {
        Gson gson = new Gson();
        String json = Gdx.files.internal(localizationPath(defaultLanguage, "Keywords.json")).readString(String.valueOf(StandardCharsets.UTF_8));
        KeywordInfo[] keywords = gson.fromJson(json, KeywordInfo[].class);
        for (KeywordInfo keyword : keywords) {
            keyword.prep();
            registerKeyword(keyword);
        }

        if (!defaultLanguage.equals(getLangString())) {
            try
            {
                json = Gdx.files.internal(localizationPath(getLangString(), "Keywords.json")).readString(String.valueOf(StandardCharsets.UTF_8));
                keywords = gson.fromJson(json, KeywordInfo[].class);
                for (KeywordInfo keyword : keywords) {
                    keyword.prep();
                    registerKeyword(keyword);
                }
            }
            catch (Exception e)
            {
                logger.warn(modID + " does not support " + getLangString() + " keywords.");
            }
        }
    }

    private void registerKeyword(KeywordInfo info) {
        BaseMod.addKeyword(modID.toLowerCase(), info.PROPER_NAME, info.NAMES, info.DESCRIPTION);
        if (!info.ID.isEmpty())
        {
            keywords.put(info.ID, info);
        }
    }

    


    //This determines the mod's ID based on information stored by ModTheSpire.
    private static void loadModInfo() {
        Optional<ModInfo> infos = Arrays.stream(Loader.MODINFOS).filter((modInfo)->{
            AnnotationDB annotationDB = Patcher.annotationDBMap.get(modInfo.jarURL);
            if (annotationDB == null)
                return false;
            Set<String> initializers = annotationDB.getAnnotationIndex().getOrDefault(SpireInitializer.class.getName(), Collections.emptySet());
            return initializers.contains(CosmicZoneMod.class.getName());
        }).findFirst();
        if (infos.isPresent()) {
            info = infos.get();
            modID = info.ID;
        }
        else {
            throw new RuntimeException("Failed to determine mod info/ID based on initializer.");
        }
    }
}
*/