package spireMapOverhaul.zones.Junkyard;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.rewards.RewardItem;
import spireMapOverhaul.abstracts.AbstractZone;
import spireMapOverhaul.zoneInterfaces.RewardModifyingZone;

import java.util.ArrayList;

public class Junkyard extends AbstractZone implements RewardModifyingZone {
    public static final String ID = "Junkyard";

    public Junkyard() {
        super(ID, Icons.REWARD, Icons.MONSTER, Icons.EVENT);
        this.width = 3;
        this.height = 3;
        this.cannotSkipRewards = true;
    }

    @Override
    public AbstractZone copy() {
        return new Junkyard();
    }

    @Override
    public Color getColor() {
        return Color.LIGHT_GRAY.cpy();
    }


    @Override
    public ArrayList<RewardItem> getAdditionalRewards() {
        ArrayList<RewardItem> newRewards = new ArrayList<RewardItem>();
        newRewards.add(new RewardItem());
        return newRewards;
    }

}
