package spireMapOverhaul.zones.goldencurse;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.rewards.RewardItem;
import spireMapOverhaul.abstracts.AbstractZone;
import spireMapOverhaul.zoneInterfaces.RewardModifyingZone;

import java.util.ArrayList;

public class GoldenCurseZone extends AbstractZone implements RewardModifyingZone {
    public static final String ID = "GoldenCurse";


    private final Color color = Color.GOLD.cpy();

    public GoldenCurseZone() {
        super(ID);
        this.width = 2;
        this.maxWidth = 3;
        this.height = 3;
        this.maxHeight = 4;
        iconTexture = ImageMaster.MAP_NODE_ELITE;

    }

    public static int getRewardCost(RewardItem instance) {

        switch (instance.type) {
            case CARD:
                return 50;
            case RELIC:
                return instance.relic.getPrice() / 2;
            case POTION:
                return instance.potion.getPrice() / 2;
            case GOLD:
            case SAPPHIRE_KEY:
            case EMERALD_KEY:
                return 0;
        }


        return 50;
    }

    @Override
    public ArrayList<RewardItem> getAdditionalRewards() {
        ArrayList<RewardItem> rewards = new ArrayList<>();
        rewards.add(new RewardItem());
        if (AbstractDungeon.getCurrRoom().rewards.stream().anyMatch(r -> r.type == RewardItem.RewardType.RELIC)) {
            rewards.add(new RewardItem(AbstractDungeon.returnRandomRelic(AbstractDungeon.returnRandomRelicTier())));
        }
        return rewards;
    }

    @Override
    public void modifyRewards(ArrayList<RewardItem> rewards) {
        rewards.removeIf(r -> r.type == RewardItem.RewardType.GOLD || r.type == RewardItem.RewardType.STOLEN_GOLD);
    }

    @Override
    public AbstractZone copy() {
        return new GoldenCurseZone();
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
    public Color getColor() { //I considered changing this to a variable, but a method lets you do funky stuff like a rainbow zone that changes colors or something.
        return color;
    }

}