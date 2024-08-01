package spireMapOverhaul;

import basemod.AutoAdd;
import basemod.BaseMod;
import basemod.IUIElement;
import basemod.ModPanel;
import basemod.abstracts.CustomSavable;
import basemod.devcommands.ConsoleCommand;
import basemod.eventUtil.AddEventParams;
import basemod.eventUtil.util.Condition;
import basemod.helpers.RelicType;
import basemod.helpers.TextCodeInterpreter;
import basemod.interfaces.*;
import basemod.patches.com.megacrit.cardcrawl.helpers.TipHelper.HeaderlessTip;
import basemod.patches.com.megacrit.cardcrawl.screens.options.DropdownMenu.DropdownColoring;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.mod.stslib.Keyword;
import com.evacipated.cardcrawl.mod.stslib.icons.CustomIconHelper;
import com.evacipated.cardcrawl.modthespire.Loader;
import com.evacipated.cardcrawl.modthespire.lib.SpireConfig;
import com.evacipated.cardcrawl.modthespire.lib.SpireEnum;
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;
import com.google.gson.Gson;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.AbstractEvent;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import com.megacrit.cardcrawl.localization.*;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rewards.RewardSave;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.screens.options.DropdownMenu;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import javassist.CtClass;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import spireMapOverhaul.abstracts.AbstractSMORelic;
import spireMapOverhaul.abstracts.AbstractZone;
import spireMapOverhaul.cardvars.SecondDamage;
import spireMapOverhaul.cardvars.SecondMagicNumber;
import spireMapOverhaul.interfaces.relics.MaxHPChangeRelic;
import spireMapOverhaul.patches.CustomRewardTypes;
import spireMapOverhaul.patches.ZonePatches;
import spireMapOverhaul.patches.ZonePerFloorRunHistoryPatch;
import spireMapOverhaul.patches.interfacePatches.CampfireModifierPatches;
import spireMapOverhaul.patches.interfacePatches.CombatModifierPatches;
import spireMapOverhaul.patches.interfacePatches.EncounterModifierPatches;
import spireMapOverhaul.patches.interfacePatches.TravelTrackingPatches;
import spireMapOverhaul.rewards.AnyColorCardReward;
import spireMapOverhaul.rewards.HealReward;
import spireMapOverhaul.rewards.SingleCardReward;
import spireMapOverhaul.ui.*;
import spireMapOverhaul.ui.FixedModLabeledToggleButton.FixedModLabeledToggleButton;
import spireMapOverhaul.util.QueueZoneCommand;
import spireMapOverhaul.util.TexLoader;
import spireMapOverhaul.util.Wiz;
import spireMapOverhaul.util.ZoneShapeMaker;
import spireMapOverhaul.zoneInterfaces.CampfireModifyingZone;
import spireMapOverhaul.zoneInterfaces.EncounterModifyingZone;
import spireMapOverhaul.zoneInterfaces.ModifiedEventRateZone;
import spireMapOverhaul.zones.beastslair.BeastsLairZone;
import spireMapOverhaul.zones.brokenspace.BrokenSpaceZone;
import spireMapOverhaul.zones.divinitiesgaze.commands.ClearSetDivinityCommand;
import spireMapOverhaul.zones.divinitiesgaze.commands.SetDivinityCommand;
import spireMapOverhaul.zones.gremlinTown.GremlinTown;
import spireMapOverhaul.zones.gremlinTown.HordeHelper;
import spireMapOverhaul.zones.gremlinTown.potions.*;
import spireMapOverhaul.zones.keymaster.KeymasterZone;
import spireMapOverhaul.zones.manasurge.ui.extraicons.BlightIcon;
import spireMapOverhaul.zones.manasurge.ui.extraicons.EnchantmentIcon;
import spireMapOverhaul.zones.windy.WindyZone;
import spireMapOverhaul.zones.windy.patches.GoldRewardReductionPatch;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.function.Consumer;

import static spireMapOverhaul.util.Wiz.adp;
import static spireMapOverhaul.zones.gremlinTown.GremlinTown.PLATFORM_KEY;
import static spireMapOverhaul.zones.gremlinTown.GremlinTown.PLATFORM_OGG;
import static spireMapOverhaul.zones.manasurge.ManaSurgeZone.ENCHANTBLIGHT_KEY;
import static spireMapOverhaul.zones.manasurge.ManaSurgeZone.ENCHANTBLIGHT_OGG;
import static spireMapOverhaul.zones.storm.StormZone.*;

@SuppressWarnings({"unused"})
@SpireInitializer
public class SpireAnniversary6Mod implements
        EditCardsSubscriber,
        EditRelicsSubscriber,
        EditStringsSubscriber,
        EditKeywordsSubscriber,
        PostInitializeSubscriber,
        OnStartBattleSubscriber,
        AddAudioSubscriber,
        PostRenderSubscriber,
        PostCampfireSubscriber,
        MaxHPChangeSubscriber,
        StartGameSubscriber,
        StartActSubscriber,
        ImGuiSubscriber,
        PostUpdateSubscriber {

    public static final Logger logger = LogManager.getLogger("Zonemaster");

    public static final boolean hasDarkmap;

    static {
        hasDarkmap = Loader.isModLoaded("ojb_DarkMap");
    }

    public static Settings.GameLanguage[] SupportedLanguages = {
            Settings.GameLanguage.ENG,
            Settings.GameLanguage.ZHS
    };

    public static class Enums {
        @SpireEnum
        public static AbstractPotion.PotionRarity ZONE;
        @SpireEnum
        public static AbstractCard.CardTags GREMLIN;
    }

    private static final String[] ZONE_OPTIONS = {"0-1", "1-2", "2-3", "3-4", "4-5"};
    public static SpireAnniversary6Mod thismod;
    public static SpireConfig modConfig = null;
    public static boolean currentRunActive = false;
    public static boolean currentRunNoRepeatZones = false;
    public static HashSet<String> currentRunAllZones = null;
    public static HashSet<String> currentRunSeenZones = null;

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

    public static final Map<String, Keyword> keywords = new HashMap<>();

    public static List<AbstractZone> unfilteredAllZones = new ArrayList<>();
    private static Map<String, AbstractZone> zonePackages = new HashMap<>();
    public static Map<String, Set<String>> zoneEvents = new HashMap<>();


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

    public static String makeUIPath(String resourcePath) {
        return modID + "Resources/images/ui/" + resourcePath;
    }

    public static String makeRelicPath(String resourcePath) {
        return modID + "Resources/images/relics/" + resourcePath;
    }

    public static String makeMonsterPath(String resourcePath) {
        return modID + "Resources/images/monsters/" + resourcePath;
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

    public static String makeEventPath(String resourcePath) {
        return modID + "Resources/images/events/" + resourcePath;
    }

    public static String makeBackgroundPath(String resourcePath) {
        return modID + "Resources/images/backgrounds/" + resourcePath;
    }

    public static void initialize() {
        thismod = new SpireAnniversary6Mod();

        try {
            Properties defaults = new Properties();
            defaults.put("active", "TRUE");
            defaults.put("zoneCountIndex", "3");
            defaults.put("noRepeatZones", "TRUE");
            defaults.put("largeIconsMode", "FALSE");
            defaults.put("enableShaders", "TRUE");
            modConfig = new SpireConfig(modID, "anniv6Config", defaults);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadZones() {
        new AutoAdd(modID)
                .packageFilter(SpireAnniversary6Mod.class)
                .any(AbstractZone.class, (info, zone)->{
                    if (!info.ignore) {
                        String pkg = zone.getClass().getName();
                        int lastSeparator = pkg.lastIndexOf('.');
                        if (lastSeparator >= 0) pkg = pkg.substring(0, lastSeparator);
                        unfilteredAllZones.add(zone);
                        zonePackages.put(pkg, zone);
                    }
                });
        logger.info("Found zone classes with AutoAdd: " + unfilteredAllZones.size());
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

        CustomIconHelper.addCustomIcon(MonsterIcon.get());
        CustomIconHelper.addCustomIcon(EventIcon.get());
        CustomIconHelper.addCustomIcon(ChestIcon.get());
        CustomIconHelper.addCustomIcon(ShopIcon.get());
        CustomIconHelper.addCustomIcon(RestIcon.get());

        // Mana Surge Icons
        CustomIconHelper.addCustomIcon(EnchantmentIcon.get());
        CustomIconHelper.addCustomIcon(BlightIcon.get());

        BaseMod.addDynamicVariable(new SecondMagicNumber());
        BaseMod.addDynamicVariable(new SecondDamage());
    }

    @Override
    public void receivePostInitialize() {
        initializedStrings = true;
        unfilteredAllZones.forEach(AbstractZone::loadStrings);
        unfilteredAllZones.sort(Comparator.comparing(c->c.id));
        addMonsters();
        addPotions();
        addSaveFields();
        registerCustomRewards();
        initializeConfig();
        initializeSavedData();
        initializeEvents();

        ConsoleCommand.addCommand("addzone", QueueZoneCommand.class);
        ConsoleCommand.addCommand("setdivinity", SetDivinityCommand.class);
        ConsoleCommand.addCommand("clearsetdivinity", ClearSetDivinityCommand.class);
        TextCodeInterpreter.addAccessible(ZoneShapeMaker.class);
    }

    @Override
    public void receiveOnBattleStart(AbstractRoom abstractRoom) {
        // I can't have this in my zone because it still needs called if I access this fight via the dev console
        HordeHelper.needsUpdate = false;
        if (AbstractDungeon.lastCombatMetricKey != null && AbstractDungeon.lastCombatMetricKey.equals(GremlinTown.GREMLIN_HORDE))
            HordeHelper.initFight();
    }

    public static void addMonsters() {
        for (AbstractZone zone : unfilteredAllZones) {
            if (zone instanceof EncounterModifyingZone) {
                ((EncounterModifyingZone) zone).registerEncounters();
            }
        }
    }

    public static void addPotions() {

        if (Loader.isModLoaded("widepotions")) {
            Consumer<String> whitelist = getWidePotionsWhitelistMethod();
            whitelist.accept(LouseMilk.POTION_ID);
            whitelist.accept(PreerelxsBlueRibbon.POTION_ID);
            whitelist.accept(NoxiousBrew.POTION_ID);
            whitelist.accept(MushroomSoup.POTION_ID);
            whitelist.accept(GremsFire.POTION_ID);
        }

        BaseMod.addPotion(LouseMilk.class, Color.WHITE.cpy(), null, null, LouseMilk.POTION_ID);
        BaseMod.addPotion(PreerelxsBlueRibbon.class, Color.GOLDENROD.cpy(), null, null, PreerelxsBlueRibbon.POTION_ID);
        BaseMod.addPotion(RitualBlood.class, RitualBlood.POTION_COLOR.cpy(), null, null, RitualBlood.POTION_ID);
        BaseMod.addPotion(NoxiousBrew.class, NoxiousBrew.POTION_COLOR.cpy(), null, Color.DARK_GRAY.cpy(), NoxiousBrew.POTION_ID);
        BaseMod.addPotion(MushroomSoup.class, MushroomSoup.POTION_COLOR.cpy(), null, Color.GRAY.cpy(), MushroomSoup.POTION_ID);
        BaseMod.addPotion(GremsFire.class, Color.RED.cpy(), null, Color.ORANGE.cpy(), GremsFire.POTION_ID);
    }

    public static void addSaveFields() {
        BaseMod.addSaveField(SavableCurrentRunActive.SaveKey, new SavableCurrentRunActive());
        BaseMod.addSaveField(SavableCurrentRunNoRepeatZones.SaveKey, new SavableCurrentRunNoRepeatZones());
        BaseMod.addSaveField(SavableCurrentRunAllZones.SaveKey, new SavableCurrentRunAllZones());
        BaseMod.addSaveField(SavableCurrentRunSeenZones.SaveKey, new SavableCurrentRunSeenZones());
        BaseMod.addSaveField(ZonePerFloorRunHistoryPatch.ZonePerFloorLog.SaveKey, new ZonePerFloorRunHistoryPatch.ZonePerFloorLog());
        BaseMod.addSaveField(EncounterModifierPatches.LastZoneNormalEncounter.SaveKey, new EncounterModifierPatches.LastZoneNormalEncounter());
        BaseMod.addSaveField(EncounterModifierPatches.LastZoneEliteEncounter.SaveKey, new EncounterModifierPatches.LastZoneEliteEncounter());
        BaseMod.addSaveField(GoldRewardReductionPatch.SavableCombatGoldReduction.SaveKey, new GoldRewardReductionPatch.SavableCombatGoldReduction()); //windy zone
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
        loadZoneStrings(unfilteredAllZones, "eng");
        if (Settings.language != Settings.GameLanguage.ENG)
        {
            loadStrings(Settings.language.toString().toLowerCase());
            loadZoneStrings(unfilteredAllZones, Settings.language.toString().toLowerCase());
        }
    }


    private void loadStrings(String langKey) {
        if (!Gdx.files.internal(modID + "Resources/localization/" + langKey + "/").exists()) return;
        loadStringsFile(langKey, CardStrings.class);
        loadStringsFile(langKey, RelicStrings.class);
        loadStringsFile(langKey, PowerStrings.class);
        loadStringsFile(langKey, UIStrings.class);
        loadStringsFile(langKey, StanceStrings.class);
        loadStringsFile(langKey, OrbStrings.class);
        loadStringsFile(langKey, PotionStrings.class);
        loadStringsFile(langKey, EventStrings.class);
        loadStringsFile(langKey, MonsterStrings.class);
    }

    public void loadZoneStrings(Collection<AbstractZone> zones, String langKey) {
        for (AbstractZone zone : zones) {
            String languageAndZone = langKey + "/" + zone.id;
            String filepath = modID + "Resources/localization/" + languageAndZone;
            if (!Gdx.files.internal(filepath).exists()) {
                continue;
            }
            logger.info("Loading strings for zone " + zone.id + "from \"resources/localization/" + languageAndZone + "\"");

            loadStringsFile(languageAndZone, CardStrings.class);
            loadStringsFile(languageAndZone, RelicStrings.class);
            loadStringsFile(languageAndZone, PowerStrings.class);
            loadStringsFile(languageAndZone, UIStrings.class);
            loadStringsFile(languageAndZone, StanceStrings.class);
            loadStringsFile(languageAndZone, OrbStrings.class);
            loadStringsFile(languageAndZone, PotionStrings.class);
            loadStringsFile(languageAndZone, EventStrings.class);
            loadStringsFile(languageAndZone, MonsterStrings.class);
        }
    }

    private void loadStringsFile(String key, Class<?> stringType) {
        String filepath = modID + "Resources/localization/" + key + "/" + stringType.getSimpleName().replace("Strings", "strings") + ".json";
        if (Gdx.files.internal(filepath).exists()) {
            BaseMod.loadCustomStringsFile(stringType, filepath);
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
        for (AbstractZone zone : unfilteredAllZones) {
            String languageAndZone = langKey + "/" + zone.id;
            String zoneJson = modID + "Resources/localization/" + languageAndZone + "/Keywordstrings.json";
            FileHandle handle = Gdx.files.internal(zoneJson);
            if (handle.exists()) {
                logger.info("Loading keywords for zone " + zone.id + "from \"resources/localization/" + languageAndZone + "\"");
                zoneJson = handle.readString(String.valueOf(StandardCharsets.UTF_8));
                keywords.addAll(Arrays.asList(gson.fromJson(zoneJson, Keyword[].class)));
            }
        }

        for (Keyword keyword : keywords) {
            BaseMod.addKeyword(modID, keyword.PROPER_NAME, keyword.NAMES, keyword.DESCRIPTION);
            if (!keyword.ID.isEmpty())
            {
                SpireAnniversary6Mod.keywords.put(keyword.ID, keyword);
            }
        }
    }

    @SuppressWarnings("unchecked")
    public void initializeEvents() {
        Collection<CtClass> foundClasses =
                new AutoAdd(modID)
                        .packageFilter(SpireAnniversary6Mod.class)
                        .findClasses(AbstractEvent.class);

        for (CtClass ctClass : foundClasses) {
            if (ctClass.hasAnnotation(AutoAdd.Ignore.class)) continue;

            try {
                Class<? extends AbstractEvent> eventClass = (Class<? extends AbstractEvent>) Loader.getClassPool().getClassLoader().loadClass(ctClass.getName());
                Field idField = eventClass.getDeclaredField("ID");
                if (Modifier.isStatic(idField.getModifiers()) && String.class.equals(idField.getType())) {
                    idField.setAccessible(true);
                    String eventID = (String) idField.get(null);

                    AddEventParams.Builder eventBuilder = new AddEventParams.Builder(eventID, eventClass);

                    Condition eventCondition = null;
                    boolean endsWithRewardsUI = false;
                    Method[] methods = eventClass.getDeclaredMethods();
                    for (Method m : methods) {
                        if (Modifier.isStatic(m.getModifiers()) && m.getName().equals("bonusCondition")
                                && m.getReturnType().equals(boolean.class) && m.getParameterCount() == 0) {
                            m.setAccessible(true);
                            eventCondition = ()-> {
                                try {
                                    return (boolean)m.invoke(null);
                                } catch (IllegalAccessException | InvocationTargetException e) {
                                    throw new RuntimeException(e);
                                }
                            };
                            break;
                        }
                        else if (Modifier.isStatic(m.getModifiers()) && m.getName().equals("endsWithRewardsUI")
                                && m.getReturnType().equals(boolean.class) && m.getParameterCount() == 0) {
                            m.setAccessible(true);
                            try {
                                endsWithRewardsUI = (boolean)m.invoke(null);
                            } catch (IllegalAccessException | InvocationTargetException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    }

                    AbstractZone zone = null;
                    String pkg = eventClass.getName();
                    while (zone == null) {
                        int separatorIndex = pkg.lastIndexOf('.');
                        if (separatorIndex == -1) break;
                        pkg = pkg.substring(0, separatorIndex);
                        zone = zonePackages.get(pkg);
                    }

                    if (zone != null) {
                        AbstractZone finalZone = zone;

                        Condition old = eventCondition;
                        eventCondition = old == null ? ()->{
                            AbstractZone current = ZonePatches.currentZone();
                            return current != null && current.id.equals(finalZone.id);
                        } : ()->{
                            AbstractZone current = ZonePatches.currentZone();
                            return old.test() && current != null && current.id.equals(finalZone.id);
                        };

                        Set<String> eventList = zoneEvents.computeIfAbsent(zone.id, k -> new HashSet<>());
                        eventList.add(eventID);

                        logger.info("Registered event " + eventClass.getSimpleName() + " | Zone: " + finalZone.id);
                    }
                    else {
                        logger.info("Event " + eventClass.getSimpleName() + " has no linked zone.");
                    }

                    if (eventCondition != null) {
                        eventBuilder.bonusCondition(eventCondition);
                    }
                    eventBuilder.endsWithRewardsUI(endsWithRewardsUI);

                    BaseMod.addEvent(eventBuilder.create());
                }
                else {
                    logger.warn("Failed to find ID on event class " + eventClass.getName());
                }
            } catch (ClassCastException | IllegalAccessException | ClassNotFoundException | NoSuchFieldException e) {
                throw new RuntimeException(e);
            }
        }
        unfilteredAllZones.stream()
                .filter(z -> z instanceof ModifiedEventRateZone)
                .forEach(z -> {
                    Set<String> specEvents =  ((ModifiedEventRateZone) z).addSpecificEvents();
                    if(specEvents != null) {
                        Set<String> eventList = zoneEvents.computeIfAbsent(z.id, k -> new HashSet<>());
                        eventList.addAll(specEvents);
                    }
                });
    }

    @Override
    public void receiveAddAudio() {
        BaseMod.addAudio(THUNDER_KEY, THUNDER_MP3);
        BaseMod.addAudio(RAIN_KEY, RAIN_MP3);

        // Mana Surge Audio
        BaseMod.addAudio(ENCHANTBLIGHT_KEY,ENCHANTBLIGHT_OGG);
        // Windy Audio
        BaseMod.addAudio(WindyZone.WINDY_KEY, WindyZone.WINDY_MP3);

        BaseMod.addAudio(PLATFORM_KEY, PLATFORM_OGG);
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

        BaseMod.registerCustomReward(CustomRewardTypes.HEALREWARD,
                rewardSave -> new HealReward(rewardSave.id, rewardSave.amount),
                reward -> {
                    int i = ((HealReward) reward).amount;
                    return new RewardSave(CustomRewardTypes.HEALREWARD.toString(), ((HealReward) reward).iconPath, i, 0);
                }
        );

        BaseMod.registerCustomReward(CustomRewardTypes.SMO_ANYCOLORCARDREWARD,
                rewardSave -> new AnyColorCardReward(rewardSave.id),
                reward -> {
                    StringBuilder s = new StringBuilder();
                    for (AbstractCard c : reward.cards) {
                        s.append(c.cardID).append("|").append(c.timesUpgraded).append("|").append(c.misc).append("#");
                    }
                    return new RewardSave(CustomRewardTypes.SMO_ANYCOLORCARDREWARD.toString(), s.toString());
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
        BrokenSpaceZone.shaderTimer += Gdx.graphics.getDeltaTime();
    }

    @Override
    public int receiveMaxHPChange(int amount) {
        for (AbstractRelic r : adp().relics) {
            if (r instanceof MaxHPChangeRelic) {
                amount = ((MaxHPChangeRelic) r).onMaxHPChange(amount);
            }
        }
        return amount;
    }

    private ModPanel settingsPanel;
    private static final float NOREPEATZONES_CHECKBOX_X = 400f;
    private static final float NOREPEATZONES_CHECKBOX_Y = 685f;
    private static final float LARGEICONS_CHECKBOX_X = 400f;
    private static final float LARGEICONS_CHECKBOX_Y = 650f;
    private DropdownMenu filterDropdown;
    private static final float DROPDOWN_X = 400f;
    private static final float DROPDOWN_Y = 600f;
    private FixedModLabeledToggleButton filterCheckbox;
    private static final float CHECKBOX_X = 400f;
    private static final float CHECKBOX_Y = 520f;
    private AbstractZone filterViewedZone;
    private static final float DESC_X = 760f;
    private static final float DESC_Y = 575f;
    private FixedModLabeledToggleButton shaderCheckbox;
    private static final float SHADER_CHECKBOX_X = 400f;
    private static final float SHADER_CHECKBOX_Y = 440f;
    private static final float BIOME_AMOUNT_X = 405f;
    private static final float BIOME_AMOUNT_Y = 395f;

    private void initializeConfig() {
        UIStrings configStrings = CardCrawlGame.languagePack.getUIString(makeID("ConfigMenuText"));

        Texture badge = TexLoader.getTexture(makeImagePath("ui/badge.png"));

        settingsPanel = new ModPanel();

        FixedModLabeledToggleButton noRepeatZonesToggle = new FixedModLabeledToggleButton(configStrings.TEXT[5], NOREPEATZONES_CHECKBOX_X, NOREPEATZONES_CHECKBOX_Y, Color.WHITE, FontHelper.tipBodyFont, getNoRepeatZonesConfig(), null,
                (label) -> {},
                (button) -> setNoRepeatZonesConfig(button.enabled));
        settingsPanel.addUIElement(noRepeatZonesToggle);

        FixedModLabeledToggleButton largeIconsModeToggle = new FixedModLabeledToggleButton(configStrings.TEXT[4], LARGEICONS_CHECKBOX_X, LARGEICONS_CHECKBOX_Y, Color.WHITE, FontHelper.tipBodyFont, getLargeIconsModeConfig(), null,
                (label) -> {},
                (button) -> setLargeIconsModeConfig(button.enabled));
        settingsPanel.addUIElement(largeIconsModeToggle);

        ArrayList<String> filterOptions = new ArrayList<>();
        for (AbstractZone z : unfilteredAllZones) {
            filterOptions.add(z.name);
        }

        filterDropdown = new DropdownMenu((dropdownMenu, index, s) -> filterSetViewedZone(index),
                filterOptions, FontHelper.tipBodyFont, Settings.CREAM_COLOR);
        DropdownColoring.RowToColor.function.set(filterDropdown, (index) -> getFilterConfig(unfilteredAllZones.get(index).id) ? null : Settings.RED_TEXT_COLOR);
        IUIElement wrapperDropdown = new IUIElement() {
            public void render(SpriteBatch sb) {
                filterDropdown.render(sb, DROPDOWN_X * Settings.xScale,DROPDOWN_Y * Settings.yScale);
                HeaderlessTip.renderHeaderlessTip(DESC_X * Settings.xScale,DESC_Y * Settings.yScale, filterViewedZone.tooltipBody);
            }
            public void update() {
                filterDropdown.update();
            }
            public int renderLayer() {return 3;}
            public int updateOrder() {return 0;}
        };
        settingsPanel.addUIElement(wrapperDropdown);

        filterCheckbox = new FixedModLabeledToggleButton(configStrings.TEXT[3], CHECKBOX_X, CHECKBOX_Y, Color.WHITE, FontHelper.tipBodyFont, true, null,
                (label) -> {},
                (button) -> setFilterConfig(filterViewedZone.id, button.enabled));
        IUIElement wrapperFilterCheckbox = new IUIElement() {
            @Override
            public void render(SpriteBatch sb) {
                filterCheckbox.render(sb);
            }

            @Override
            public void update() {
                if (!filterDropdown.isOpen) {
                    filterCheckbox.update();
                }
            }

            @Override
            public int renderLayer() {
                return filterCheckbox.renderLayer();
            }

            @Override
            public int updateOrder() {
                return 1;
            }
        };
        settingsPanel.addUIElement(wrapperFilterCheckbox);
        filterSetViewedZone(0);

        shaderCheckbox = new FixedModLabeledToggleButton(configStrings.TEXT[6], SHADER_CHECKBOX_X, SHADER_CHECKBOX_Y, Color.WHITE, FontHelper.tipBodyFont, getShaderConfig(), null,
                (label) -> {},
                (button) -> setShaderConfig(button.enabled));
        IUIElement wrapperShaderCheckbox = new IUIElement() {
            @Override
            public void render(SpriteBatch sb) {
                shaderCheckbox.render(sb);
            }

            @Override
            public void update() {
                if (!filterDropdown.isOpen) {
                    shaderCheckbox.update();
                }
            }

            @Override
            public int renderLayer() {
                return shaderCheckbox.renderLayer();
            }

            @Override
            public int updateOrder() {
                return 1;
            }
        };
        settingsPanel.addUIElement(wrapperShaderCheckbox);

        IUIElement biomeAmountOption = new IUIElement() {
            @Override
            public void render(SpriteBatch sb) {
                // Render the biome amount option label
                FontHelper.renderFontLeft(sb, FontHelper.tipBodyFont, configStrings.TEXT[7], BIOME_AMOUNT_X * Settings.xScale, BIOME_AMOUNT_Y * Settings.yScale, Settings.CREAM_COLOR);

                float leftArrowX = BIOME_AMOUNT_X * Settings.xScale;
                float rightArrowX = (BIOME_AMOUNT_X + 95) * Settings.xScale;
                float arrowY = (BIOME_AMOUNT_Y - 60) * Settings.yScale;
                float arrowWidth = 48f * Settings.scale;
                float arrowHeight = 48f * Settings.scale;

                if (InputHelper.mX >= leftArrowX && InputHelper.mX <= leftArrowX + arrowWidth && InputHelper.mY >= arrowY && InputHelper.mY <= arrowY + arrowHeight) {
                    sb.setColor(Color.WHITE);
                } else {
                    sb.setColor(Color.LIGHT_GRAY);
                }
                sb.draw(ImageMaster.CF_LEFT_ARROW, leftArrowX, arrowY, 0, 0, 48f, 48f, Settings.scale, Settings.scale, 0f, 0, 0, 48, 48, false, false);

                // Render the current biome amount
                FontHelper.renderFontLeft(sb, FontHelper.tipBodyFont, ZONE_OPTIONS[getZoneCountIndex()], leftArrowX + 57 * Settings.xScale, arrowY + 24 * Settings.yScale, Settings.BLUE_TEXT_COLOR);

                // Render the right arrow
                if (InputHelper.mX >= rightArrowX && InputHelper.mX <= rightArrowX + arrowWidth && InputHelper.mY >= arrowY && InputHelper.mY <= arrowY + arrowHeight) {
                    sb.setColor(Color.WHITE);
                } else {
                    sb.setColor(Color.LIGHT_GRAY);
                }
                sb.draw(ImageMaster.CF_RIGHT_ARROW, rightArrowX, arrowY, 0, 0, 48f, 48f, Settings.scale, Settings.scale, 0f, 0, 0, 48, 48, false, false);
            }

            @Override
            public void update() {
                if (filterDropdown.isOpen) {
                    return;
                }
                // Handle input for changing the biome amount
                float leftArrowX = BIOME_AMOUNT_X * Settings.xScale;
                float rightArrowX = (BIOME_AMOUNT_X + 95) * Settings.xScale;
                float arrowY = (BIOME_AMOUNT_Y - 60) * Settings.yScale;
                float arrowWidth = 48f * Settings.scale;
                float arrowHeight = 48f * Settings.scale;

                if (InputHelper.justClickedLeft) {
                    if (InputHelper.mX >= leftArrowX && InputHelper.mX <= leftArrowX + arrowWidth && InputHelper.mY >= arrowY && InputHelper.mY <= arrowY + arrowHeight) {
                        decrementZoneCountIndex();
                    } else if (InputHelper.mX >= rightArrowX && InputHelper.mX <= rightArrowX + arrowWidth && InputHelper.mY >= arrowY && InputHelper.mY <= arrowY + arrowHeight) {
                        incrementZoneCountIndex();
                    }
                }
            }

            @Override
            public int renderLayer() {
                return 2;
            }

            @Override
            public int updateOrder() {
                return 1;
            }
        };
        settingsPanel.addUIElement(biomeAmountOption);

        BaseMod.registerModBadge(badge, configStrings.TEXT[0], configStrings.TEXT[1], configStrings.TEXT[2], settingsPanel);
    }

    private void filterSetViewedZone(int index) {
        filterViewedZone = unfilteredAllZones.get(index);
        filterCheckbox.toggle.enabled = getFilterConfig(filterViewedZone.id);
    }

    private void initializeSavedData() {
        BaseMod.addSaveField(TravelTrackingPatches.Field.ID, new CustomSavable<String>() {
            @Override
            public String onSave() {
                return TravelTrackingPatches.Field.lastZoneID();
            }

            @Override
            public void onLoad(String s) {
                if (s != null) {
                    TravelTrackingPatches.Field.setLastZoneID(s);
                }
            }
        });

        BeastsLairZone.initializeSaveFields();
        KeymasterZone.initializeSaveFields();
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

    @Override
    public void receiveStartGame() {
        // Clean up any zones from before the save and load or from previous runs
        BetterMapGenerator.clearActiveZones();
        if (!CardCrawlGame.loadingSave) {
            BeastsLairZone.clearBossList();
            // Fix crash when you die to Gremlin horde and then start a new run
            AbstractDungeon.lastCombatMetricKey = "";
        }
        HordeHelper.hidePlatforms();
        CombatModifierPatches.hideButton = true;
    }

    @Override
    public void receiveStartAct() {
        KeymasterZone.startOfActHasKeys = Settings.hasSapphireKey && Settings.hasEmeraldKey && Settings.hasRubyKey;
    }

    public static float time = 0f;
    @Override
    public void receivePostUpdate() {
        time += Gdx.graphics.getRawDeltaTime();
    }

    public static class SavableCurrentRunActive implements CustomSavable<Boolean> {
        public final static String SaveKey = "CurrentRunActive";

        @Override
        public Boolean onSave() {
            return currentRunActive;
        }

        @Override
        public void onLoad(Boolean b) {
            currentRunActive = b == null || b;
        }
    }

    public static class SavableCurrentRunNoRepeatZones implements CustomSavable<Boolean> {
        public final static String SaveKey = "CurrentRunNoRepeatZones";

        @Override
        public Boolean onSave() {
            return currentRunNoRepeatZones;
        }

        @Override
        public void onLoad(Boolean b) {
            currentRunNoRepeatZones = b == null || b;
        }
    }

    public static class SavableCurrentRunAllZones implements CustomSavable<HashSet<String>> {
        public final static String SaveKey = "CurrentRunAllZones";

        @Override
        public HashSet<String> onSave() {
            return currentRunAllZones;
        }

        @Override
        public void onLoad(HashSet<String> s) {
            currentRunAllZones = s == null ? new HashSet<>() : s;
        }
    }

    public static class SavableCurrentRunSeenZones implements CustomSavable<HashSet<String>> {
        public final static String SaveKey = "CurrentRunSeenZones";

        @Override
        public HashSet<String> onSave() {
            return currentRunSeenZones;
        }

        @Override
        public void onLoad(HashSet<String> s) {
            currentRunSeenZones = s == null ? new HashSet<>() : s;
        }
    }

    public static boolean getActiveConfig() {
        return modConfig == null || modConfig.getBool("active");
    }

    public static void setActiveConfig(boolean active) {
        if (modConfig != null) {
            modConfig.setBool("active", active);
            try {
                modConfig.save();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static int getZoneCountIndex() {
        return modConfig == null ? 3 : modConfig.getInt("zoneCountIndex");
    }

    public static void setZoneCountIndex(int index) {
        if (modConfig != null) {
            modConfig.setInt("zoneCountIndex", index);
            try {
                modConfig.save();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void incrementZoneCountIndex() {
        int currentIndex = getZoneCountIndex();
        int newIndex = (currentIndex + 1) % ZONE_OPTIONS.length;
        setZoneCountIndex(newIndex);
    }

    public static void decrementZoneCountIndex() {
        int currentIndex = getZoneCountIndex();
        int newIndex = (currentIndex - 1 + ZONE_OPTIONS.length) % ZONE_OPTIONS.length;
        setZoneCountIndex(newIndex);
    }

    public static boolean getNoRepeatZonesConfig() {
        return modConfig != null && modConfig.getBool("noRepeatZones");
    }

    public static void setNoRepeatZonesConfig(boolean bool) {
        if (modConfig != null) {
            modConfig.setBool("noRepeatZones", bool);
            try {
                modConfig.save();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static boolean getLargeIconsModeConfig() {
        return modConfig != null && modConfig.getBool("largeIconsMode");
    }

    public static void setLargeIconsModeConfig(boolean bool) {
        if (modConfig != null) {
            modConfig.setBool("largeIconsMode", bool);
            try {
                modConfig.save();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static boolean getFilterConfig(String zoneId) {
        if (modConfig != null && modConfig.has( zoneId +"_ENABLED")) {
            return modConfig.getBool(zoneId +"_ENABLED");
        } else {
            return true;
        }
    }

    private static void setFilterConfig(String zoneId, boolean enable) {
        if (modConfig != null) {
            modConfig.setBool(zoneId + "_ENABLED", enable);
            try {
                modConfig.save();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static void setShaderConfig(boolean enable) {
        if (modConfig != null) {
            modConfig.setBool("enableShaders", enable);
            try {
                modConfig.save();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static boolean getShaderConfig() {
        return modConfig != null && modConfig.getBool("enableShaders");
    }

    private static ZoneShapeMaker shapeUi = null;
    @Override
    public void receiveImGui() {
        if (shapeUi == null) shapeUi = new ZoneShapeMaker();
        shapeUi.receiveImGui();
    }
}



