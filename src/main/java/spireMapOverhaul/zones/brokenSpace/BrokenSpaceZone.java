package spireMapOverhaul.zones.brokenSpace;

import basemod.ReflectionHacks;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireField;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch2;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.RelicLibrary;
import com.megacrit.cardcrawl.random.Random;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.relics.Circlet;
import com.megacrit.cardcrawl.rewards.RewardItem;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.rooms.MonsterRoomElite;
import com.megacrit.cardcrawl.screens.DungeonMapScreen;
import spireMapOverhaul.BetterMapGenerator;
import spireMapOverhaul.SpireAnniversary6Mod;
import spireMapOverhaul.abstracts.AbstractZone;
import spireMapOverhaul.zoneInterfaces.RenderableZone;
import spireMapOverhaul.zoneInterfaces.RewardModifyingZone;
import spireMapOverhaul.zoneInterfaces.ShopModifyingZone;
import spireMapOverhaul.zones.brokenSpace.patches.BrokenSpaceRenderPatch;

import java.util.ArrayList;

import static com.megacrit.cardcrawl.dungeons.AbstractDungeon.cardRng;
import static spireMapOverhaul.util.Wiz.adp;

public class BrokenSpaceZone extends AbstractZone implements RewardModifyingZone, ShopModifyingZone, RenderableZone {
    public static final String ID = "BrokenSpace";
    private static final float OFFSET_X = Settings.isMobile ? 496.0F * Settings.xScale : 560.0F * Settings.xScale;
    private static final float OFFSET_Y = 180.0F * Settings.scale;
    private static final float SPACING_X = Settings.isMobile ? (int) (Settings.xScale * 64.0F) * 2.2F : (int) (Settings.xScale * 64.0F) * 2.0F;
    public static ArrayList<String> BrokenRelics = new ArrayList<>();
    private final int width, height;
    private final Color color;
    public static float shaderTimer = 0.0F;

    public BrokenSpaceZone() {
        this("Broken Space 0", 2, 3);

    }

    private BrokenSpaceZone(String name, int width, int height) {
        super(ID);

        this.width = width;
        this.height = height;

        color = Color.WHITE.cpy();
        this.name = name;
    }


    @Override
    public void distributeRooms(Random rng, ArrayList<AbstractRoom> roomList) {
        //Guarantee at least one elite
        placeRoomRandomly(rng, roomOrDefault(roomList, (room) -> room instanceof MonsterRoomElite, MonsterRoomElite::new));

    }

    @Override
    public AbstractZone copy() {
        return new BrokenSpaceZone(name, width, height);
    }

    @Override
    public void renderOnMap(SpriteBatch sb, float alpha) {
        BrokenSpaceRenderPatch.StartFbo(sb);
        super.renderOnMap(sb, alpha);
        BrokenSpaceRenderPatch.StopFbo(sb, 1.0f, 0.0f, 0.03f, 0.2f, 0.5f);
        if (alpha > 0) {
            FontHelper.renderFontCentered(sb, FontHelper.menuBannerFont, name,
                    labelX * SPACING_X + OFFSET_X, labelY * Settings.MAP_DST_Y + OFFSET_Y + DungeonMapScreen.offsetY,
                    Color.WHITE.cpy(), 0.8f
            );
        }
    }

    @Override
    public boolean generateMapArea(BetterMapGenerator.MapPlanner planner) {
        return generateNormalArea(planner, width, height);
    }

    @Override
    protected boolean canIncludeEarlyRows() {
        return false;
    }

    @Override
    protected boolean canIncludeFinalCampfireRow() {
        return false;
    }

    @Override
    protected boolean canIncludeTreasureRow() {
        return false;
    }

    @Override
    public boolean canSpawn() {
        return AbstractDungeon.actNum >= 2;
    }

    @Override
    public Color getColor() { //I considered changing this to a variable, but a method lets you do funky stuff like a rainbow zone that changes colors or something.
        return color;
    }

    public AbstractCard getTrulyRandomCard(Random rng) {
        AbstractCard.CardRarity rarity = AbstractCard.CardRarity.COMMON;

        // pick RANDOM rarity
        int roll = rng.random(7);
        switch (roll) {
            case 0:
            case 1:
                rarity = AbstractCard.CardRarity.COMMON;
                break;
            case 2:
            case 3:
                rarity = AbstractCard.CardRarity.UNCOMMON;
                break;
            case 4:
            case 5:
                rarity = AbstractCard.CardRarity.RARE;
                break;
            case 6:
                return AbstractDungeon.returnRandomCurse().makeCopy();

        }


        AbstractCard c = CardLibrary.getAnyColorCard(rarity).makeCopy();

        float upgradeChance = ReflectionHacks.getPrivateStatic(AbstractDungeon.class, "cardUpgradedChance");

        if (c.rarity != AbstractCard.CardRarity.RARE && cardRng.randomBoolean(upgradeChance) && c.canUpgrade()) {// 1857
            c.upgrade();// 1858
        } else {
            for (AbstractRelic r : AbstractDungeon.player.relics) {
                r.onPreviewObtainCard(c);
            }
        }


        return c;


    }

    @Override
    public ArrayList<AbstractCard> getAdditionalCardReward() {
        ArrayList<AbstractCard> cards = new ArrayList<>();
        int amount = getNumberOfCardsInReward();


        for (int i = 0; i < amount; i++) {
            AbstractCard c = getTrulyRandomCard(AbstractDungeon.cardRandomRng);
            cards.add(c);
            UnnaturalCardField.unnatural.set(c, true);


        }

        applyStandardUpgradeLogic(cards);
        return cards;
    }

    @Override
    public void modifyReward(RewardItem rewardItem) {
        if (rewardItem.type == RewardItem.RewardType.RELIC) {
            AbstractRelic origRelic = rewardItem.relic;
            AbstractRelic newRelic = getValidBrokenRelic();

            if (newRelic != null) {
                rewardItem.relic = newRelic;
                rewardItem.text = newRelic.name;

                switch (origRelic.tier) {
                    case COMMON:
                        AbstractDungeon.commonRelicPool.add(origRelic.relicId);
                        break;
                    case UNCOMMON:
                        AbstractDungeon.uncommonRelicPool.add(origRelic.relicId);
                        break;
                    case RARE:
                        AbstractDungeon.rareRelicPool.add(origRelic.relicId);
                        break;
                    case SHOP:
                        AbstractDungeon.shopRelicPool.add(origRelic.relicId);
                        break;
                    case BOSS:
                        AbstractDungeon.bossRelicPool.add(origRelic.relicId);
                        break;
                    default:
                        SpireAnniversary6Mod.logger.info("what.");
                        break;

                }
            }

        }
    }

    public AbstractRelic getValidBrokenRelic() {
        boolean playerHasAllBrokenRelics = true;
        for (String relicID : BrokenRelics) {
            if (!adp().hasRelic(relicID)) {
                playerHasAllBrokenRelics = false;
                break;
            }
        }
        if (playerHasAllBrokenRelics) {
            return new Circlet();
        }

        AbstractRelic r;
        do {
            r = RelicLibrary.getRelic(BrokenRelics.get(AbstractDungeon.relicRng.random(BrokenRelics.size() - 1))).makeCopy();
        } while (!r.canSpawn() || adp().hasRelic(r.relicId));

        return r;
    }

    @Override
    public void modifyRewardCards(ArrayList<AbstractCard> cards) {
        int amount = cards.size();
        cards.clear();

        for (int i = 0; i < amount; i++) {
            AbstractCard c = getTrulyRandomCard(AbstractDungeon.cardRandomRng);
            cards.add(c);
            UnnaturalCardField.unnatural.set(c, true);

        }
        applyStandardUpgradeLogic(cards);

    }


    @SpirePatch2(
            clz = AbstractCard.class,
            method = SpirePatch.CLASS
    )
    public static class UnnaturalCardField {
        public static SpireField<Boolean> unnatural = new SpireField<>(() -> false);
    }

    @Override
    public void renderBackground(SpriteBatch sb) {
        BrokenSpaceRenderPatch.StartFbo(sb);
    }

    @Override
    public void postRenderBackground(SpriteBatch sb) {
        BrokenSpaceRenderPatch.StopFbo(sb, 0.05F, 0.0f, 4f, 0.1f);
    }

    public static void addBrokenRelic(String relicID) {
        if (!BrokenRelics.contains(relicID)) {
            BrokenRelics.add(relicID);
        }
    }
}