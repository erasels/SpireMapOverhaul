package spireMapOverhaul.zones.beastslair;

import basemod.BaseMod;
import basemod.abstracts.CustomSavable;
import basemod.eventUtil.EventUtils;
import basemod.patches.com.megacrit.cardcrawl.events.AbstractEvent.AdditionalEventParameters;
import com.badlogic.gdx.graphics.Color;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch2;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.AbstractEvent;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.map.MapRoomNode;
import com.megacrit.cardcrawl.random.Random;
import com.megacrit.cardcrawl.rewards.RewardItem;
import spireMapOverhaul.abstracts.AbstractZone;
import spireMapOverhaul.zoneInterfaces.EncounterModifyingZone;
import spireMapOverhaul.zoneInterfaces.RewardModifyingZone;
import spireMapOverhaul.zones.thieveshideout.rooms.ForcedEventRoom;

import java.util.ArrayList;

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
            node.setRoom(new ForcedEventRoom(() -> {
                // Because Beast's Lair needs a constructor parameter, we can't use the event we get back from EventUtils
                // directly (since it instantiates a new instance with the parameterless constructor)
                // It also isn't enough to make a new instance ourselves, since the only good way to properly set the
                // additionalParameters SpireField that PhasedEvent relies on is to go through EventUtils
                // So we grab the additionalParameters from the EventUtils instance, then attach them to our own instance
                // Maybe there's a way to refactor BeastsLairEvent to not need a constructor parameter, but PhasedEvent
                // makes it hard. The alternative would be to abandon PhasedEvent entirely and just extend Colosseum, like
                // ThiefKingEvent does (which avoided all these problems).
                AbstractEvent eventInfo = EventUtils.getEvent(BeastsLairEvent.ID);
                BeastsLairEvent event = new BeastsLairEvent(bossList.get(rng.random(bossList.size() - 1)));
                AdditionalEventParameters.additionalParameters.set(event, AdditionalEventParameters.additionalParameters.get(eventInfo));
                return event;
            }));
        }
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
    public AbstractZone copy() {
        return new BeastsLairZone();
    }

    @Override
    public Color getColor() {
        return Color.RED.cpy();
    }

    @Override
    public boolean canSpawn() {
        return AbstractDungeon.actNum >= 2 && !bossList.isEmpty();
    }

    @SpirePatch2(clz = AbstractDungeon.class, method = "dungeonTransitionSetup")
    public static class DungeonTransitionSetupPatch {
        public static void Prefix() {
            bossList.clear();
            bossList.addAll(AbstractDungeon.bossList);
        }
    }


}
