package spireMapOverhaul.zones.beastslair;

import basemod.BaseMod;
import basemod.abstracts.CustomSavable;
import com.badlogic.gdx.graphics.Color;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch2;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.MonsterHelper;
import com.megacrit.cardcrawl.map.MapRoomNode;
import com.megacrit.cardcrawl.monsters.MonsterGroup;
import com.megacrit.cardcrawl.random.Random;
import com.megacrit.cardcrawl.rewards.RewardItem;
import com.megacrit.cardcrawl.rooms.MonsterRoomElite;
import spireMapOverhaul.abstracts.AbstractZone;
import spireMapOverhaul.zoneInterfaces.EncounterModifyingZone;
import spireMapOverhaul.zoneInterfaces.RewardModifyingZone;

import java.util.ArrayList;
import java.util.List;

import static spireMapOverhaul.SpireAnniversary6Mod.makeID;

public class BeastsLairZone extends AbstractZone implements EncounterModifyingZone, RewardModifyingZone {
    public static final String ID = "BeastsLair";
    public static final String SaveID = makeID("BeastsLairBosses");
    public static ArrayList<String> bossList = new ArrayList<>();

    public BeastsLairZone() {
        super(ID, Icons.MONSTER);
        this.width = 1;
        this.maxWidth = 1;
        this.height = 1;
        this.maxHeight = 1;
        iconTexture = ImageMaster.MAP_NODE_ELITE;

    }

    public static void clearBossList() {
        bossList.clear();
    }


    public static void initializeSaveFields() {
        BaseMod.addSaveField(SaveID, new CustomSavable<ArrayList<String>>() {
            @Override
            public ArrayList<String> onSave() {
                return BeastsLairZone.bossList;
            }

            @Override
            public void onLoad(ArrayList<String> strings) {
                if (strings != null) {
                    BeastsLairZone.bossList = strings;
                }
            }
        });
    }

    @Override
    protected boolean canIncludeTreasureRow() {
        return false;
    }

    @Override
    protected boolean canIncludeFinalCampfireRow() {
        return false;
    }

    @Override
    protected boolean canIncludeEarlyRows() {
        return false;
    }

    @Override
    public void manualRoomPlacement(Random rng) {
        for (MapRoomNode node : nodes) {
            node.setRoom(new MonsterRoomElite());
        }
    }

    @Override
    public MonsterGroup changeEncounter(MonsterGroup monsterGroup) {
        if (bossList.isEmpty()) {
            return monsterGroup;
        }

        return MonsterHelper.getEncounter(bossList.get(AbstractDungeon.monsterRng.random(bossList.size() - 1)));
    }

    @Override
    public int changeRareCardRewardChance(int rareCardRewardChance) {
        return 50;
    }

    @Override
    public int changeUncommonCardRewardChance(int uncommonCardRewardChance) {
        return 50;
    }

    @Override
    public ArrayList<RewardItem> getAdditionalRewards() {
        ArrayList<RewardItem> rewards = new ArrayList<>();
        rewards.add(new RewardItem(AbstractDungeon.returnRandomRelic(AbstractDungeon.returnRandomRelicTier())));

        return rewards;
    }

    @Override
    public List<ZoneEncounter> getEliteEncounters() {
        ArrayList<ZoneEncounter> encounters = new ArrayList<>();
        encounters.add(new ZoneEncounter("Act1FakeElite", 1, (BaseMod.GetMonster) () -> null));
        encounters.add(new ZoneEncounter("Act2FakeElite", 2, (BaseMod.GetMonster) () -> null));
        encounters.add(new ZoneEncounter("Act3FakeElite", 3, (BaseMod.GetMonster) () -> null));
        return encounters;
    }

    @Override
    public AbstractZone copy() {
        return new BeastsLairZone();
    }

    @Override
    public Color getColor() {
        return Color.RED.cpy();
    }

    @Override
    public boolean canSpawn() {
        return AbstractDungeon.actNum >= 2;
    }

    @SpirePatch2(clz = AbstractDungeon.class, method = "dungeonTransitionSetup")
    public static class DungeonTransitionSetupPatch {
        public static void Prefix() {
            bossList.clear();
            bossList.addAll(AbstractDungeon.bossList);
        }
    }


}
