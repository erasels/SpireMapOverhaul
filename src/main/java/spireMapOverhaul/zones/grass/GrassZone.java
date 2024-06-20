package spireMapOverhaul.zones.grass;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.RelicLibrary;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.relics.Ginger;
import com.megacrit.cardcrawl.relics.HappyFlower;
import com.megacrit.cardcrawl.relics.Turnip;
import com.megacrit.cardcrawl.rewards.RewardItem;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.rooms.MonsterRoom;
import com.megacrit.cardcrawl.shop.ShopScreen;
import com.megacrit.cardcrawl.shop.StoreRelic;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import spireMapOverhaul.abstracts.AbstractZone;
import spireMapOverhaul.util.Wiz;
import spireMapOverhaul.zoneInterfaces.CombatModifyingZone;
import spireMapOverhaul.zoneInterfaces.RenderableZone;
import spireMapOverhaul.zoneInterfaces.RewardModifyingZone;
import spireMapOverhaul.zoneInterfaces.ShopModifyingZone;
import spireMapOverhaul.zones.grass.vegetables.*;

import java.util.ArrayList;

public class GrassZone extends AbstractZone implements CombatModifyingZone, RenderableZone, RewardModifyingZone, ShopModifyingZone {
    public static final int SPAWN_VEGS = 3;
    public static final String ID = "Grass";
    public static final String[] GRASS_RELICS = new String[] {
            Turnip.ID,
            Ginger.ID,
            HappyFlower.ID
    };
    private final ArrayList<AbstractVegetableData> ALL = new ArrayList<>();
    private final ArrayList<AbstractVegetable> vegetables = new ArrayList<>();

    public GrassZone() {
        super(ID, Icons.MONSTER, Icons.SHOP, Icons.CHEST);
        this.width = 2;
        this.maxWidth = 4;
        this.height = 3;
        this.maxHeight = 4;
    }

    private static float getRelicChance() {
        return 1f / GRASS_RELICS.length;
    }

    @Override
    public AbstractZone copy() {
        return new GrassZone();
    }

    @Override
    public Color getColor() {
        return Color.FOREST.cpy();
    }

    @Override
    public void atBattleStart() {
        // Do not actually spawn vegetables in the shop
        AbstractRoom room = AbstractDungeon.getCurrRoom();
        if (room instanceof MonsterRoom) {
            initializeVegetables();
            for (int i = 0; i < AbstractDungeon.cardRandomRng.random(1, 3); i++) {
                spawn(random().create());
            }
        }
    }

    @Override
    public void onVictory() {
        vegetables.clear();
    }

    public void atTurnEnd() {
        if (EnergyPanel.totalCount > 0 && !vegetables.isEmpty()) {
            int upgrades = 0;
            for (int i = 0; i < EnergyPanel.totalCount; i++) {
                AbstractVegetable veg = getRandom();
                if (veg != null && veg.canUpgrade()) {
                    veg.upgrade(1);
                    upgrades++;
                }
            }
            // Use only as much energy as was used for upgrading
            if(upgrades > 0)
                AbstractDungeon.player.energy.use(upgrades);
        }
    }

    @Override
    public String getCombatText() {
        return TEXT[3];
    }

    public int getCount() {
        return vegetables.size();
    }

    public AbstractVegetable getRandom() {
        return Wiz.getRandomItem(vegetables);
    }

    protected void initializeVegetables() {
        ALL.clear();
        ALL.add(BellPepper.DATA);
        ALL.add(Carrot.DATA);
        ALL.add(Leek.DATA);
        ALL.add(Onion.DATA);
        ALL.add(Pumpkin.DATA);
        ALL.add(Radish.DATA);
        ALL.add(Tomato.DATA);
    }

    @Override
    public void modifyReward(RewardItem rewardItem) {
        if (rewardItem.type == RewardItem.RewardType.RELIC && Wiz.isNormalRelicTier(rewardItem.relic.tier)) {
            AbstractRelic origRelic = rewardItem.relic;
            for (String relicID : GRASS_RELICS) {
                AbstractRelic newRelic = RelicLibrary.getRelic(relicID);
                if (newRelic != null) {
                    ArrayList<String> relicPool = Wiz.getRelicPool(newRelic.tier);
                    // Check if the relic is already in the reward or in the players inventory, and check to see if it is actually in the pool
                    if (!relicID.equals(origRelic.relicId)
                            && !AbstractDungeon.player.hasRelic(relicID)
                            && newRelic.canSpawn()
                            && (relicPool == null || relicPool.contains(relicID))
                            && AbstractDungeon.relicRng.randomBoolean(getRelicChance())) {
                        rewardItem.relic = newRelic.makeCopy();
                        rewardItem.text = newRelic.name;
                        if (relicPool != null) {
                            relicPool.remove(relicID);
                        }
                        // Add the old relic back in to the pool so that it can spawn again
                        Wiz.addRelicToPool(origRelic);
                        break;
                    }
                }
            }
        }
    }

    @Override
    public void postCreateShopRelics(ShopScreen screen, ArrayList<StoreRelic> relics) {
        if (!relics.isEmpty()) {
            StoreRelic first = relics.get(0);
            AbstractRelic origRelic = first.relic;
            for (String relicID : GRASS_RELICS) {
                AbstractRelic newRelic = RelicLibrary.getRelic(relicID);
                if (newRelic != null) {
                    ArrayList<String> relicPool = Wiz.getRelicPool(newRelic.tier);
                    // Check if the relic is already in the reward or in the players inventory, and check to see if it is actually in the pool
                    if (!relicID.equals(origRelic.relicId)
                            && !AbstractDungeon.player.hasRelic(relicID)
                            && newRelic.canSpawn()
                            && (relicPool == null || relicPool.contains(relicID))
                            && AbstractDungeon.relicRng.randomBoolean(getRelicChance())) {
                        first.relic = newRelic.makeCopy();
                        if (relicPool != null) {
                            relicPool.remove(relicID);
                        }
                        // Add the old relic back in to the pool so that it can spawn again
                        Wiz.addRelicToPool(origRelic);
                        break;
                    }
                }
            }
        }
    }

    @Override
    public void postRenderBackground(SpriteBatch sb) {
        for (AbstractVegetable veg : vegetables) {
            veg.render(sb);
        }
    }

    public AbstractVegetableData random() {
        return Wiz.getRandomItem(ALL);
    }

    public void spawn(AbstractVegetable vegetable) {
        vegetables.add(vegetable);
        vegetable.onSpawn(getCount());
    }

    @Override
    public void update() {
        for (AbstractVegetable veg : vegetables) {
            veg.update();
        }
        vegetables.removeIf(AbstractVegetable::isPulled);
    }
}
