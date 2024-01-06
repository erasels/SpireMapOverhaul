package spireMapOverhaul;

import basemod.*;
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
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.events.AbstractEvent;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.localization.*;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import com.megacrit.cardcrawl.rewards.RewardSave;
import com.megacrit.cardcrawl.screens.options.DropdownMenu;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import javassist.CtClass;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import spireMapOverhaul.cardvars.SecondDamage;
import spireMapOverhaul.cardvars.SecondMagicNumber;
import spireMapOverhaul.patches.ZonePerFloorRunHistoryPatch;
import spireMapOverhaul.patches.interfacePatches.CampfireModifierPatches;
import spireMapOverhaul.patches.CustomRewardTypes;
import spireMapOverhaul.abstracts.AbstractSMORelic;
import spireMapOverhaul.patches.ZonePatches;
import spireMapOverhaul.patches.interfacePatches.TravelTrackingPatches;
import spireMapOverhaul.patches.interfacePatches.EncounterModifierPatches;
import spireMapOverhaul.rewards.SingleCardReward;
import spireMapOverhaul.ui.*;
import spireMapOverhaul.util.QueueZoneCommand;
import spireMapOverhaul.util.TexLoader;
import spireMapOverhaul.abstracts.AbstractZone;
import spireMapOverhaul.util.Wiz;
import spireMapOverhaul.util.ZoneShapeMaker;
import spireMapOverhaul.zoneInterfaces.CampfireModifyingZone;
import spireMapOverhaul.zoneInterfaces.EncounterModifyingZone;
import spireMapOverhaul.rewards.HealReward;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.function.Consumer;

import static spireMapOverhaul.zones.storm.StormZone.*;

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
        PostCampfireSubscriber,
        StartGameSubscriber,
        ImGuiSubscriber,
        PostUpdateSubscriber {

    public static final Logger logger = LogManager.getLogger("Zonemaster");

    public static Settings.GameLanguage[] SupportedLanguages = {
            Settings.GameLanguage.ENG,
            Settings.GameLanguage.ZHS
    };

    public static class Enums {
        @SpireEnum
        public static AbstractPotion.PotionRarity ZONE;
    }

    public static SpireAnniversary6Mod thismod;
    public static SpireConfig modConfig = null;
    public static SpireConfig currentRunConfig = null;
    public static boolean currentRunActive = false;

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
    public static List<AbstractZone> allZones = new ArrayList<>();
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
            defaults.put("active", "TRUE");
            defaults.put("largeIconsMode", "FALSE");
            modConfig = new SpireConfig(modID, "anniv6Config", defaults);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            currentRunConfig = new SpireConfig(modID, "anniv6ConfigCurrentRun");
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
                        if (getCurrentRunFilterConfig(zone.id)) {
                            allZones.add(zone);
                        }
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
        TextCodeInterpreter.addAccessible(ZoneShapeMaker.class);
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

        }
    }

    public static void addSaveFields() {
        BaseMod.addSaveField(SavableCurrentRunActive.SaveKey, new SavableCurrentRunActive());
        BaseMod.addSaveField(ZonePerFloorRunHistoryPatch.ZonePerFloorLog.SaveKey, new ZonePerFloorRunHistoryPatch.ZonePerFloorLog());
        BaseMod.addSaveField(EncounterModifierPatches.LastZoneNormalEncounter.SaveKey, new EncounterModifierPatches.LastZoneNormalEncounter());
        BaseMod.addSaveField(EncounterModifierPatches.LastZoneEliteEncounter.SaveKey, new EncounterModifierPatches.LastZoneEliteEncounter());
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
        filepath = modID + "Resources/localization/" + langKey + "/Monsterstrings.json";
        if (Gdx.files.internal(filepath).exists()) {
            BaseMod.loadCustomStringsFile(MonsterStrings.class, filepath);
        }
    }

    public void loadZoneStrings(Collection<AbstractZone> zones, String langKey) {
        for (AbstractZone zone : zones) {
            String languageAndZone = langKey + "/" + zone.id + "/";
            String filepath = modID + "Resources/localization/" + languageAndZone;
            if (!Gdx.files.internal(filepath).exists()) {
                continue;
            }
            logger.info("Loading strings for zone " + zone.id + "from \"resources/localization/" + languageAndZone + "\"");

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
            if (Gdx.files.internal(filepath + "Monsterstrings.json").exists()) {
                BaseMod.loadCustomStringsFile(MonsterStrings.class, filepath + "Monsterstrings.json");
            }
            if (Gdx.files.internal(filepath + "Eventstrings.json").exists()) {
                BaseMod.loadCustomStringsFile(EventStrings.class, filepath + "Eventstrings.json");
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

                    if (eventCondition != null)
                        eventBuilder.bonusCondition(eventCondition);

                    BaseMod.addEvent(eventBuilder.create());
                }
                else {
                    logger.warn("Failed to find ID on event class " + eventClass.getName());
                }
            } catch (ClassCastException | IllegalAccessException | ClassNotFoundException | NoSuchFieldException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public void receiveAddAudio() {
        BaseMod.addAudio(THUNDER_KEY, THUNDER_MP3);
        BaseMod.addAudio(RAIN_KEY, RAIN_MP3);
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
    private static final float LARGEICONS_CHECKBOX_X = 400f;
    private static final float LARGEICONS_CHECKBOX_Y = 650f;
    private DropdownMenu filterDropdown;
    private static final float DROPDOWN_X = 400f;
    private static final float DROPDOWN_Y = 600f;
    private ModLabeledToggleButton filterCheckbox;
    private static final float CHECKBOX_X = 400f;
    private static final float CHECKBOX_Y = 520f;
    private AbstractZone filterViewedZone;
    private static final float DESC_X = 760f;
    private static final float DESC_Y = 575f;

    private void initializeConfig() {
        UIStrings configStrings = CardCrawlGame.languagePack.getUIString(makeID("ConfigMenuText"));

        Texture badge = TexLoader.getTexture(makeImagePath("ui/badge.png"));

        settingsPanel = new ModPanel();

        ModLabeledToggleButton largeIconsModeToggle = new ModLabeledToggleButton(configStrings.TEXT[4], LARGEICONS_CHECKBOX_X, LARGEICONS_CHECKBOX_Y, Color.WHITE, FontHelper.tipBodyFont, getLargeIconsModeConfig(), null,
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
        filterCheckbox = new ModLabeledToggleButton(configStrings.TEXT[3], CHECKBOX_X, CHECKBOX_Y, Color.WHITE, FontHelper.tipBodyFont, true, null,
                (label) -> {},
                (button) -> setFilterConfig(filterViewedZone.id, button.enabled));
        settingsPanel.addUIElement(filterCheckbox);
        filterSetViewedZone(0);

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
        if (!CardCrawlGame.loadingSave) {
            updateZoneList(); //only updated on new games to not mess anything up if settings are changed and a game is loaded
        }
    }

    private void updateZoneList() {
        allZones.clear();
        currentRunConfig.clear();
        for (AbstractZone z : unfilteredAllZones) {
            if (getFilterConfig(z.id)) {
                allZones.add(z);
                setCurrentRunFilterConfig(z.id, true);
            } else {
                setCurrentRunFilterConfig(z.id, false);
            }
        }
        try {
            currentRunConfig.save();
        } catch (IOException e) {
            e.printStackTrace();
        }
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

    private boolean getFilterConfig(String zoneId) {
        if (modConfig != null && modConfig.has( zoneId +"_ENABLED")) {
            return modConfig.getBool(zoneId +"_ENABLED");
        } else {
            return true;
        }
    }

    private void setFilterConfig(String zoneId, boolean enable) {
        if (modConfig != null) {
            modConfig.setBool(zoneId + "_ENABLED", enable);
            try {
                modConfig.save();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private boolean getCurrentRunFilterConfig(String zoneId) {
        if (currentRunConfig != null && currentRunConfig.has( zoneId +"_ONCURRENTRUN")) {
            return currentRunConfig.getBool(zoneId +"_ONCURRENTRUN");
        } else {
            return false;
        }
    }

    private void setCurrentRunFilterConfig(String zoneId, boolean enable) {
        if (currentRunConfig != null) {
            currentRunConfig.setBool(zoneId + "_ONCURRENTRUN", enable);
        }
    }

    private static ZoneShapeMaker shapeUi = null;
    @Override
    public void receiveImGui() {
        if (shapeUi == null) shapeUi = new ZoneShapeMaker();
        shapeUi.receiveImGui();
    }
}



