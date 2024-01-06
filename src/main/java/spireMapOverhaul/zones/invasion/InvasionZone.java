package spireMapOverhaul.zones.invasion;

import basemod.BaseMod;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.MonsterGroup;
import com.megacrit.cardcrawl.random.Random;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rewards.RewardItem;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.rooms.MonsterRoomElite;
import spireMapOverhaul.SpireAnniversary6Mod;
import spireMapOverhaul.abstracts.AbstractZone;
import spireMapOverhaul.zoneInterfaces.EncounterModifyingZone;
import spireMapOverhaul.zoneInterfaces.RewardModifyingZone;
import spireMapOverhaul.zoneInterfaces.ShopModifyingZone;
import spireMapOverhaul.zones.invasion.cards.HandOfTheAbyss;
import spireMapOverhaul.zones.invasion.monsters.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class InvasionZone extends AbstractZone implements EncounterModifyingZone, RewardModifyingZone, ShopModifyingZone {
    public static final String ID = "Invasion";
    private static final String STYGIAN_BOAR_AND_WHISPERING_WRAITH = SpireAnniversary6Mod.makeID("STYGIAN_BOAR_AND_WHISPERING_WRAITH");
    private static final String DREAD_MOTH_AND_GRAFTED_WORMS = SpireAnniversary6Mod.makeID("DREAD_MOTH_AND_GRAFTED_WORMS");
    private static final String THREE_ELEMENTALS = SpireAnniversary6Mod.makeID("THREE_ELEMENTALS");
    private static final String VOID_CORRUPTION_AND_ORB_OF_FIRE = SpireAnniversary6Mod.makeID("VOID_CORRUPTION_AND_ORB_OF_FIRE");
    private static final String THREE_HATCHLINGS = SpireAnniversary6Mod.makeID("THREE_HATCHLINGS");
    private static final String CROAKING_TRIO = SpireAnniversary6Mod.makeID("CROAKING_TRIO");

    private static final int ELITE_EXTRA_GOLD = 100;

    public InvasionZone() {
        super(ID, Icons.MONSTER, Icons.SHOP);
        this.width = 3;
        this.height = 4;
    }

    @Override
    public AbstractZone copy() {
        return new InvasionZone();
    }

    @Override
    public Color getColor() {
        return new Color(0.3f,0.5f, 0.35f, 1f);
    }

    @Override
    public boolean canSpawn() {
        //The enemies in this zone are invaders from these acts, so it doesn't make sense to spawn the zone in them
        return !Arrays.asList("Menagerie:Menagerie", "Elementarium:Elementarium", "Abyss:Abyss").contains(AbstractDungeon.id);
    }

    @Override
    protected boolean canIncludeEarlyRows() {
        //Since this zone always has an elite, we don't want it to show up in the rows of the act that never have elites
        //It also has normal fights that are tuned as hard pool fights, so that's another reason to prevent early spawns
        return false;
    }

    @Override
    public void distributeRooms(Random rng, ArrayList<AbstractRoom> roomList) {
        //Guarantee at least one elite
        placeRoomRandomly(rng, roomOrDefault(roomList, (room)->room instanceof MonsterRoomElite, MonsterRoomElite::new));
    }

    @Override
    public void modifyReward(RewardItem rewardItem) {
        if (AbstractDungeon.getCurrRoom().eliteTrigger && rewardItem.type == RewardItem.RewardType.GOLD) {
            rewardItem.incrementGold(ELITE_EXTRA_GOLD);
        }
    }

    @Override
    public void modifyRewards(ArrayList<RewardItem> rewards) {
        for (int i = 0; i < rewards.size(); i++) {
            if (rewards.get(i).type == RewardItem.RewardType.RELIC) {
                RewardItem rewardItem = new RewardItem();
                rewardItem.cards = InvasionUtil.getRewardCards(this.getNumberOfCardsInReward());
                //Note that this reward does not get the normal act-dependent chance of cards being upgraded
                //The cards are powerful enough already, so we're essentially treating them they're all rares
                //(Which are never upgraded by the normal act-dependent upgrade logic)
                for (AbstractRelic r : AbstractDungeon.player.relics) {
                    for (AbstractCard c : rewardItem.cards) {
                        r.onPreviewObtainCard(c);
                    }
                }
                rewards.set(i, rewardItem);
            }
        }
    }

    @Override
    public void postCreateShopCards(ArrayList<AbstractCard> coloredCards, ArrayList<AbstractCard> colorlessCards) {
        ArrayList<AbstractCard> cards = new ArrayList<>();
        cards.addAll(InvasionUtil.getSpells());
        cards.addAll(InvasionUtil.getBlades());
        cards.add(new HandOfTheAbyss());
        Collections.shuffle(cards, new java.util.Random(AbstractDungeon.merchantRng.randomLong()));
        colorlessCards.set(0, cards.get(0));
        colorlessCards.set(1, cards.get(1));
    }

    @Override
    public List<ZoneEncounter> getNormalEncounters() {
        return Arrays.asList(
            new ZoneEncounter(Hexasnake.ID, 1, () -> new Hexasnake(0.0f, 0.0f)),
            new ZoneEncounter(STYGIAN_BOAR_AND_WHISPERING_WRAITH, 1, () -> new MonsterGroup(
                new AbstractMonster[] {
                    new WhisperingWraith(-350.0F, 0.0F),
                    new StygianBoar(0.0F, 0.0F)
                })),
            new ZoneEncounter(DREAD_MOTH_AND_GRAFTED_WORMS, 1, () -> new MonsterGroup(
                new AbstractMonster[] {
                    new GraftedWorm(-550.0F, 0.0F),
                    new GraftedWorm(-300.0F, 0.0F),
                    new GraftedWorm(-50.0F, 0.0F),
                    new DreadMoth(200.0F, 125.0F),
                })),
            new ZoneEncounter(VoidBeast.ID, 2, () -> new VoidBeast(0.0f, 0.0f)),
            new ZoneEncounter(THREE_ELEMENTALS, 2, () -> new MonsterGroup(InvasionUtil.generateElementalGroup())),
            new ZoneEncounter(VOID_CORRUPTION_AND_ORB_OF_FIRE, 2,  () -> new MonsterGroup(
                new AbstractMonster[] {
                    new OrbOfFire(-350.0F, 125.0F),
                    new VoidCorruption(0.0F, 0.0F)
                })),
            new ZoneEncounter(UnboundAbyssal.ID, 3, (BaseMod.GetMonster)UnboundAbyssal::new),
            new ZoneEncounter(THREE_HATCHLINGS, 3, () -> new MonsterGroup(
                new AbstractMonster[] {
                    new Hatchling(-450.0F, 10.0F, false),
                    new Hatchling(-150.0F, -30.0F, false),
                    new Hatchling(150.0F, 20.0F, false)
                })),
            new ZoneEncounter(CROAKING_TRIO, 3, () -> new MonsterGroup(
                new AbstractMonster[] {
                    new CroakingBrute(-450.0F, 0.0F),
                    new CroakingPelter(-150.0F, 0.0F),
                    new CroakingSeer(150.0F, 0.0F)
                }))
        );
    }

    @Override
    public List<ZoneEncounter> getEliteEncounters() {
        return Arrays.asList(
            new ZoneEncounter(Hydra.ID, 1, (BaseMod.GetMonster)Hydra::new),
            new ZoneEncounter(VoidReaper.ID, 1, (BaseMod.GetMonster)VoidReaper::new),
            new ZoneEncounter(WarGolem.ID, 2, (BaseMod.GetMonster)WarGolem::new),
            new ZoneEncounter(ElementalPortal.ID, 2, () -> new ElementalPortal(150.0F, 0.0F)),
            new ZoneEncounter(PrimevalQueen.ID, 3, () -> new PrimevalQueen(250.0F, 0.0F)),
            new ZoneEncounter(Behemoth.ID, 3, (BaseMod.GetMonster)Behemoth::new)
        );
    }
}
