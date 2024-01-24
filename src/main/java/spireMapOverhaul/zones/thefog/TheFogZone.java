package spireMapOverhaul.zones.thefog;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDrawPileAction;
import com.megacrit.cardcrawl.cards.status.Dazed;
import com.megacrit.cardcrawl.map.MapRoomNode;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.megacrit.cardcrawl.powers.WeakPower;
import com.megacrit.cardcrawl.random.Random;
import com.megacrit.cardcrawl.rooms.EventRoom;
import spireMapOverhaul.abstracts.AbstractZone;
import spireMapOverhaul.zoneInterfaces.CombatModifyingZone;
import spireMapOverhaul.zoneInterfaces.ModifiedEventRateZone;

import static spireMapOverhaul.util.Wiz.atb;
import static spireMapOverhaul.util.Wiz.forAllMonstersLiving;

public class TheFogZone extends AbstractZone implements CombatModifyingZone, ModifiedEventRateZone {
    public static final String ID = "TheFog";

    public TheFogZone() {
        super(ID, Icons.MONSTER, Icons.EVENT);
        this.width = 3;
        this.height = 4;
    }

    @Override
    public Color getColor() {
        return new Color(0.4f,0.4f, 0.4f, 0.9f);
    }

    @Override
    public AbstractZone copy() {
        return new TheFogZone();
    }

    @Override
    public void atPreBattle() {
        forAllMonstersLiving(m -> {
            atb(new ApplyPowerAction(m, null, new WeakPower(m, 1, false)));
            atb(new ApplyPowerAction(m, null, new VulnerablePower(m, 1, false)));
        });
        atb(new MakeTempCardInDrawPileAction(new Dazed(), 1 , true, true));
    }

    @Override
    public void manualRoomPlacement(Random rng) {
        // Replace every node with a ? room
        for (MapRoomNode node : nodes) node.setRoom(new EventRoom());
    }

    @Override
    public boolean canIncludeTreasureRow() {
        return false;
    }

    @Override
    public boolean canIncludeFinalCampfireRow() {
        return false;
    }

    protected boolean allowAdditionalEntrances() {
        return false;
    }

    @Override
    public float zoneSpecificEventRate() {
        return 0.33f;
    }
}
