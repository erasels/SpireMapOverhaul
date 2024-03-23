package spireMapOverhaul.zones.monsterZoo;

import basemod.ReflectionHacks;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.map.MapRoomNode;
import com.megacrit.cardcrawl.map.RoomTypeAssigner;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.city.Byrd;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.random.Random;
import com.megacrit.cardcrawl.rewards.RewardItem;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.rooms.MonsterRoom;
import spireMapOverhaul.abstracts.AbstractZone;
import spireMapOverhaul.util.Wiz;
import spireMapOverhaul.zoneInterfaces.CombatModifyingZone;
import spireMapOverhaul.zoneInterfaces.RenderableZone;
import spireMapOverhaul.zoneInterfaces.RewardModifyingZone;

import java.util.ArrayList;

public class MonsterZooZone extends AbstractZone implements RewardModifyingZone, CombatModifyingZone, RenderableZone {
    public static final String ID = "MonsterZoo";

    public MonsterZooZone() {
        super(ID, Icons.MONSTER, Icons.MONSTER, Icons.MONSTER, Icons.MONSTER, Icons.MONSTER); //Just having a bit of fun
        this.width = 3;
        this.maxWidth = 4;
        this.height = 3;
    }

    @Override
    public AbstractZone copy() {
        return new MonsterZooZone();
    }

    @Override
    public Color getColor() {
        return Color.SALMON.cpy();
    }

    @Override
    public void distributeRooms(Random rng, ArrayList<AbstractRoom> roomList) {
        // This fills the zone with monster rooms, with half taken from the room distribution list (which means they
        // could be elite rooms instead of normal monster rooms, and that it uses up some of the expected distribution
        // of those room types), and half simply set directly (which means they are always normal monster rooms and do
        // not use up any of the expected distribution of those room types)
        int half = nodes.size() / 2;
        int i;
        ReflectionHacks.RMethod isValidRoomType = ReflectionHacks.privateMethod(RoomTypeAssigner.class, "ruleAssignableToRow", MapRoomNode.class, AbstractRoom.class);
        for (i = 0; i < half; ++i) {
            MapRoomNode node = nodes.get(i);
            node.setRoom(roomOrDefault(roomList, (room) -> room instanceof MonsterRoom && (boolean)isValidRoomType.invoke(null, node, room), MonsterRoom::new));
        }
        for (; i < nodes.size(); ++i) {
            nodes.get(i).setRoom(new MonsterRoom());
        }
    }

    protected boolean allowAdditionalEntrances() {
        return false;
    }

    @Override
    protected boolean canIncludeTreasureRow() {
        return false;
    }

    @Override
    protected boolean canIncludeFinalCampfireRow() {
        return false;
    }

    // Don't spawn strong monsters at the start of the first act
    @Override
    protected boolean canIncludeEarlyRows() {
        return !(AbstractDungeon.actNum == 1);
    }

    // Increase number of cards in reward, upgrade chance and amount of gold.
    @Override
    public int changeNumberOfCardsInReward(int curNumCards) {
        return curNumCards + 1;
    }

    @Override
    public boolean allowUpgradingRareCards() { //Somebody has to use the fancy hook I made :p
        return true;
    }

    @Override
    public float changeCardUpgradeChance(float curChance) {
        return curChance + 0.2f;
    }

    @Override
    public void modifyReward(RewardItem rewardItem) {
        if(rewardItem.type == RewardItem.RewardType.GOLD) {
            rewardItem.incrementGold(rewardItem.goldAmt);
        }
    }

    private static final float BG_MONSTER_MAX = 10f;
    private float bgMonsterTimer = 4f;
    @Override
    public void update() {
        if(AbstractDungeon.getCurrRoom() instanceof MonsterRoom) {
            bgMonsterTimer -= Gdx.graphics.getRawDeltaTime();

            if (bgMonsterTimer <= 0) {
                bgMonsterTimer = BG_MONSTER_MAX * MathUtils.random(0.5f, 1f);
                AbstractDungeon.effectList.add(new BackgroundMonsterEffect(true));
            }
        }
    }

    @Override
    public void onVictory() {
        BackgroundMonsterEffect.masterDispose();
    }

    // All monsters gain an amount of strength
    @Override
    public void atPreBattle() {
        Wiz.forAllMonstersLiving(m -> Wiz.atb(new ApplyPowerAction(m, null, new StrengthPower(m, getStrAmt(m)))));
    }

    private int getStrAmt(AbstractMonster m) {
        int amt = 2;
        if(m instanceof Byrd) amt--;
        return amt;
    }
}
