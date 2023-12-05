package spireMapOverhaul;

import basemod.AutoAdd;
import basemod.BaseMod;
import basemod.ModPanel;
import basemod.helpers.RelicType;
import basemod.interfaces.*;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.mod.stslib.Keyword;
import com.evacipated.cardcrawl.modthespire.Loader;
import com.evacipated.cardcrawl.modthespire.lib.SpireConfig;
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;
import com.google.gson.Gson;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.localization.*;
import com.megacrit.cardcrawl.rewards.RewardSave;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import spireMapOverhaul.patches.interfacePatches.CampfireModifierPatches;
import spireMapOverhaul.patches.CustomRewardTypes;
import spireMapOverhaul.abstracts.AbstractSMORelic;
import spireMapOverhaul.rewards.SingleCardReward;
import spireMapOverhaul.util.TexLoader;
import spireMapOverhaul.abstracts.AbstractZone;
import spireMapOverhaul.util.Wiz;
import spireMapOverhaul.zoneInterfaces.CampfireModifyingZone;
import spireMapOverhaul.zones.example.PlaceholderZone;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.function.Consumer;

@SuppressWarnings({"unused"})
@SpireInitializer
public class SpireAnniversary6Mod implements
        EditCardsSubscriber,
        EditRelicsSubscriber,
        EditStringsSubscriber,
        EditKeywordsSubscriber,
        PostInitializeSubscriber,
        AddAudioSubscriber,
        PostRenderSubscriber,
        PostCampfireSubscriber {

    public static final Logger logger = LogManager.getLogger("Zonemaster");

    public static Settings.GameLanguage[] SupportedLanguages = {
            Settings.GameLanguage.ENG,
            Settings.GameLanguage.ZHS
    };

    public static SpireAnniversary6Mod thismod;
    public static SpireConfig modConfig = null;

    public static final String modID = "anniv6";

    private static final String ATTACK_S_ART = modID + "Resources/images/512/attack.png";
    private static final String SKILL_S_ART = modID + "Resources/images/512/skill.png";
    private static final String POWER_S_ART = modID + "Resources/images/512/power.png";
    private static final String CARD_ENERGY_S = modID + "Resources/images/512/energy.png";
    private static final String TEXT_ENERGY = modID + "Resources/images/512/text_energy.png";
    private static final String ATTACK_L_ART = modID + "Resources/images/1024/attack.png";
    private static final String SKILL_L_ART = modID + "Resources/images/1024/skill.png";
    private static final String POWER_L_ART = modID + "Resources/images/1024/power.png";

    public static boolean initializedStrings = false;

    public static List<AbstractZone> allZones = new ArrayList<>();
    private List<AbstractZone> localZones = new ArrayList<>();


    public static String makeID(String idText) {
        return modID + ":" + idText;
    }


    public SpireAnniversary6Mod() {
        BaseMod.subscribe(this);
    }

    public static String makePath(String resourcePath) {
        return modID + "Resources/" + resourcePath;
    }

    public static String makeImagePath(String resourcePath) {
        return modID + "Resources/images/" + resourcePath;
    }

    public static String makeRelicPath(String resourcePath) {
        return modID + "Resources/images/relics/" + resourcePath;
    }

    public static String makePowerPath(String resourcePath) {
        return modID + "Resources/images/powers/" + resourcePath;
    }

    public static String makeCardPath(String resourcePath) {
        return modID + "Resources/images/cards/" + resourcePath;
    }

    public static String makeShaderPath(String resourcePath) {
        return modID + "Resources/shaders/" + resourcePath;
    }

    public static String makeOrbPath(String resourcePath) {
        return modID + "Resources/images/orbs/" + resourcePath;
    }

    public static void initialize() {
        thismod = new SpireAnniversary6Mod();

        try {
            Properties defaults = new Properties();
            //defaults.put("Example", "FALSE");
            modConfig = new SpireConfig(modID, "anniv6Config", defaults);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadZones() {
        new AutoAdd(modID)
                .packageFilter(SpireAnniversary6Mod.class)
                .any(AbstractZone.class, (info, zone)->{
                    if (!info.ignore)
                        allZones.add(zone);
                });
        for (int i = 0; i < 10; ++i) {
            allZones.add(new PlaceholderZone());
        }
        logger.info("Found zone classes with AutoAdd: " + allZones.size());
    }

    @Override
    public void receiveEditRelics() {
        new AutoAdd(modID)
                .packageFilter(SpireAnniversary6Mod.class)
                .any(AbstractSMORelic.class, (info, relic) -> {
                    if (relic.color == null) {
                            BaseMod.addRelic(relic, RelicType.SHARED);
                    } else {
                        BaseMod.addRelicToCustomPool(relic, relic.color);
                    }
                    if (!info.seen) {
                        UnlockTracker.markRelicAsSeen(relic.relicId);
                    }
                });
    }

    @Override
    public void receiveEditCards() {
        new AutoAdd(modID)
                .packageFilter(SpireAnniversary6Mod.class)
                .setDefaultSeen(true)
                .cards();

    }

    @Override
    public void receivePostInitialize() {
        initializedStrings = true;
        allZones.sort(Comparator.comparing(c->c.ID));
        addPotions();
        registerCustomRewards();
        initializeConfig();
        initializeSavedData();
        initializeEvents();
    }

    public static void addPotions() {

        if (Loader.isModLoaded("widepotions")) {
            Consumer<String> whitelist = getWidePotionsWhitelistMethod();

        }
    }

    private static Consumer<String> getWidePotionsWhitelistMethod() {
        // To avoid the need for a dependency of any kind, we call Wide Potions through reflection
        try {
            Method whitelistMethod = Class.forName("com.evacipated.cardcrawl.mod.widepotions.WidePotionsMod").getMethod("whitelistSimplePotion", String.class);
            return s -> {
                try {
                    whitelistMethod.invoke(null, s);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                    throw new RuntimeException("Error trying to whitelist wide potion for " + s, e);
                }
            };
        } catch (NoSuchMethodException | ClassNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException("Could not find method WidePotionsMod.whitelistSimplePotion", e);
        }
    }

    @Deprecated
    private String getLangString() {
        for (Settings.GameLanguage lang : SupportedLanguages) {
            if (lang.equals(Settings.language)) {
                return Settings.language.name().toLowerCase(Locale.ROOT);
            }
        }
        return "eng";
    }

    @Override
    public void receiveEditStrings() {
        loadZones();

        loadStrings("eng");
        loadZoneStrings(allZones, "eng");
        if (Settings.language != Settings.GameLanguage.ENG)
        {
            loadStrings(Settings.language.toString().toLowerCase());
            loadZoneStrings(allZones, Settings.language.toString().toLowerCase());
        }
    }


    private void loadStrings(String langKey) {
        if (!Gdx.files.internal(modID + "Resources/localization/" + langKey + "/").exists()) return;
        String filepath = modID + "Resources/localization/" + langKey + "/Cardstrings.json";
        if (Gdx.files.internal(filepath).exists()) {
            BaseMod.loadCustomStringsFile(CardStrings.class, filepath);
        }
        filepath = modID + "Resources/localization/" + langKey + "/Relicstrings.json";
        if (Gdx.files.internal(filepath).exists()) {
            BaseMod.loadCustomStringsFile(RelicStrings.class, filepath);
        }
        filepath = modID + "Resources/localization/" + langKey + "/Powerstrings.json";
        if (Gdx.files.internal(filepath).exists()) {
            BaseMod.loadCustomStringsFile(PowerStrings.class, filepath);
        }
        filepath = modID + "Resources/localization/" + langKey + "/UIstrings.json";
        if (Gdx.files.internal(filepath).exists()) {
            BaseMod.loadCustomStringsFile(UIStrings.class, filepath);
        }
        filepath = modID + "Resources/localization/" + langKey + "/Stancestrings.json";
        if (Gdx.files.internal(filepath).exists()) {
            BaseMod.loadCustomStringsFile(StanceStrings.class, filepath);
        }
        filepath = modID + "Resources/localization/" + langKey + "/Orbstrings.json";
        if (Gdx.files.internal(filepath).exists()) {
            BaseMod.loadCustomStringsFile(OrbStrings.class, filepath);
        }
        filepath = modID + "Resources/localization/" + langKey + "/Potionstrings.json";
        if (Gdx.files.internal(filepath).exists()) {
            BaseMod.loadCustomStringsFile(PotionStrings.class, filepath);
        }
        filepath = modID + "Resources/localization/" + langKey + "/Eventstrings.json";
        if (Gdx.files.internal(filepath).exists()) {
            BaseMod.loadCustomStringsFile(EventStrings.class, filepath);
        }
    }

    public void loadZoneStrings(Collection<AbstractZone> zones, String langKey) {
        for (AbstractZone zone : zones) {
            String languageAndZone = langKey + "/" + zone.BASE_ID + "/";
            String filepath = modID + "Resources/localization/" + languageAndZone;
            if (!Gdx.files.internal(filepath).exists()) {
                continue;
            }
            logger.info("Loading strings for zone " + zone.BASE_ID + "from \"resources/localization/" + languageAndZone + "\"");

            if (Gdx.files.internal(filepath + "Cardstrings.json").exists()) {
                BaseMod.loadCustomStringsFile(CardStrings.class, filepath + "Cardstrings.json");
            }
            if (Gdx.files.internal(filepath + "Relicstrings.json").exists()) {
                BaseMod.loadCustomStringsFile(RelicStrings.class, filepath + "Relicstrings.json");
            }
            if (Gdx.files.internal(filepath + "Powerstrings.json").exists()) {
                BaseMod.loadCustomStringsFile(PowerStrings.class, filepath + "Powerstrings.json");
            }
            if (Gdx.files.internal(filepath + "UIstrings.json").exists()) {
                BaseMod.loadCustomStringsFile(UIStrings.class, filepath + "UIstrings.json");
            }
            if (Gdx.files.internal(filepath + "Stancestrings.json").exists()) {
                BaseMod.loadCustomStringsFile(StanceStrings.class, filepath + "Stancestrings.json");
            }
            if (Gdx.files.internal(filepath + "Orbstrings.json").exists()) {
                BaseMod.loadCustomStringsFile(OrbStrings.class, filepath + "Orbstrings.json");
            }
            if (Gdx.files.internal(filepath + "Potionstrings.json").exists()) {
                BaseMod.loadCustomStringsFile(PotionStrings.class, filepath + "Potionstrings.json");
            }
        }
    }

    @Override
    public void receiveEditKeywords() {
        loadKeywords("eng");
        if (Settings.language != Settings.GameLanguage.ENG) {
            loadKeywords(Settings.language.toString().toLowerCase());
        }
    }

    private void loadKeywords(String langKey) {
        String filepath = modID + "Resources/localization/" + langKey + "/Keywordstrings.json";
        Gson gson = new Gson();
        List<Keyword> keywords = new ArrayList<>();
        if (Gdx.files.internal(filepath).exists()) {
            String json = Gdx.files.internal(filepath).readString(String.valueOf(StandardCharsets.UTF_8));
            keywords.addAll(Arrays.asList(gson.fromJson(json, Keyword[].class)));
        }
        for (AbstractZone zone : allZones) {
            String languageAndZone = langKey + "/" + zone.BASE_ID;
            String zoneJson = modID + "Resources/localization/" + languageAndZone + "/Keywordstrings.json";
            FileHandle handle = Gdx.files.internal(zoneJson);
            if (handle.exists()) {
                logger.info("Loading keywords for zone " + zone.BASE_ID + "from \"resources/localization/" + languageAndZone + "\"");
                zoneJson = handle.readString(String.valueOf(StandardCharsets.UTF_8));
                keywords.addAll(Arrays.asList(gson.fromJson(zoneJson, Keyword[].class)));
            }
        }

        for (Keyword keyword : keywords) {
            BaseMod.addKeyword(modID, keyword.PROPER_NAME, keyword.NAMES, keyword.DESCRIPTION);
        }
    }

    @Override
    public void receiveAddAudio() {

    }

    private void registerCustomRewards() {
        BaseMod.registerCustomReward(CustomRewardTypes.SMO_SINGLECARDREWARD,
                rewardSave -> new SingleCardReward(rewardSave.id),
                reward -> {
                    String s = ((SingleCardReward) reward).card.cardID +
                            "|" +
                            ((SingleCardReward) reward).card.timesUpgraded +
                            "|" +
                            ((SingleCardReward) reward).card.misc;
                    return new RewardSave(CustomRewardTypes.SMO_SINGLECARDREWARD.toString(), s);
                }
        );
    }

    //Due to reward scrolling's orthographic camera and render order of rewards, the card needs to be rendered outside of the render method
    public static SingleCardReward hoverRewardWorkaround;
    @Override
    public void receivePostRender(SpriteBatch sb) {
        if(hoverRewardWorkaround != null) {
            hoverRewardWorkaround.renderCardOnHover(sb);
            hoverRewardWorkaround = null;
        }
    }
    private ModPanel settingsPanel;

    private void initializeConfig() {
        UIStrings configStrings = CardCrawlGame.languagePack.getUIString(makeID("ConfigMenuText"));

        Texture badge = TexLoader.getTexture(makeImagePath("ui/badge.png"));

        settingsPanel = new ModPanel();

        BaseMod.registerModBadge(badge, configStrings.TEXT[0], configStrings.TEXT[1], configStrings.TEXT[2], settingsPanel);
    }

    private void initializeSavedData() {
    }

    public void initializeEvents() {
    }

    @Override
    public boolean receivePostCampfire() {
        boolean allowAdditionalSelection = false;
        AbstractZone zone = Wiz.getCurZone();
        if(zone instanceof CampfireModifyingZone) {
            allowAdditionalSelection = ((CampfireModifyingZone) zone).allowAdditionalOption(CampfireModifierPatches.optionsSelectedAmt);
        }

        if(allowAdditionalSelection) {
            CampfireModifierPatches.optionsSelectedAmt++;
            return false;
        } else {
            return true;
        }
    }
}



