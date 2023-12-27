package spireMapOverhaul.zones.invasion;

import basemod.BaseMod;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.colorless.Apotheosis;
import com.megacrit.cardcrawl.cards.colorless.MasterOfStrategy;
import com.megacrit.cardcrawl.cards.colorless.Mayhem;
import com.megacrit.cardcrawl.cards.colorless.SecretTechnique;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.MonsterGroup;
import com.megacrit.cardcrawl.monsters.exordium.LouseNormal;
import com.megacrit.cardcrawl.random.Random;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rewards.RewardItem;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.rooms.MonsterRoomElite;
import spireMapOverhaul.SpireAnniversary6Mod;
import spireMapOverhaul.abstracts.AbstractSMOCard;
import spireMapOverhaul.abstracts.AbstractZone;
import spireMapOverhaul.util.ActUtil;
import spireMapOverhaul.zoneInterfaces.EncounterModifyingZone;
import spireMapOverhaul.zoneInterfaces.RewardModifyingZone;
import spireMapOverhaul.zones.invasion.cards.*;
import spireMapOverhaul.zones.invasion.monsters.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class InvasionZone extends AbstractZone implements EncounterModifyingZone, RewardModifyingZone {
    public static final String ID = "Invasion";
    private static final String STYGIAN_BOAR_AND_WHISPERING_WRAITH = SpireAnniversary6Mod.makeID("STYGIAN_BOAR_AND_WHISPERING_WRAITH");
    private static final String DREAD_MOTH_AND_GRAFTED_WORMS = SpireAnniversary6Mod.makeID("DREAD_MOTH_AND_GRAFTED_WORMS");
    private static final String THREE_ELEMENTALS = SpireAnniversary6Mod.makeID("THREE_ELEMENTALS");
    private static final String VOID_CORRUPTION_AND_ORB_OF_FIRE = SpireAnniversary6Mod.makeID("VOID_CORRUPTION_AND_ORB_OF_FIRE");
    private static final String THREE_HATCHLINGS = SpireAnniversary6Mod.makeID("THREE_HATCHLINGS");

    private static final int ELITE_EXTRA_GOLD = 100;

    public InvasionZone() {
        super(ID, Icons.MONSTER, Icons.ELITE, Icons.SHOP);
        this.width = 3;
        this.height = 3;
    }

    @Override
    public AbstractZone copy() {
        return new InvasionZone();
    }

    @Override
    public Color getColor() {
        return Color.BLUE.cpy();
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
            rewardItem.goldAmt += ELITE_EXTRA_GOLD;
        }
    }

    @Override
    public void modifyRewards(ArrayList<RewardItem> rewards) {
        for (int i = 0; i < rewards.size(); i++) {
            if (rewards.get(i).type == RewardItem.RewardType.RELIC) {
                RewardItem rewardItem = new RewardItem();
                rewardItem.cards = getRewardCards();
                for (AbstractRelic r : AbstractDungeon.player.relics) {
                    for (AbstractCard c : rewardItem.cards) {
                        r.onPreviewObtainCard(c);
                    }
                }
                rewards.set(i, rewardItem);
            }
        }
    }

    private static ArrayList<AbstractCard> getRewardCards() {
        List<AbstractCard> spells = Arrays.asList(
            new DarkRitual(),
            new Foresee(),
            new Languish(),
            new LightningBolt(),
            new LightningHelix(),
            new MirarisWake(),
            new Staggershock(),
            new SteelWall(),
            new WallOfBlossoms()
        );
        List<AbstractCard> blades = Arrays.asList(
            new EarthblessedBlade(),
            new FireblessedBlade(),
            new IceblessedBlade(),
            new VoidblessedBlade(),
            new WindblessedBlade()
        );
        ArrayList<AbstractCard> cards = new ArrayList<>();
        switch (ActUtil.getRealActNum()) {
            case 1:
                cards.addAll(spells);
                break;
            case 2:
                cards.addAll(blades);
                break;
            case 3:
                cards.add(new Apotheosis());
                cards.add(new MasterOfStrategy());
                cards.add(new Mayhem());
                cards.add(new SecretTechnique());
                cards.add(new HandOfTheAbyss());
                cards.add(spells.get(AbstractDungeon.cardRng.random(spells.size() - 1)));
                cards.add(blades.get(AbstractDungeon.cardRng.random(blades.size() - 1)));
                break;
        }

        int numCards = 3;
        for (AbstractRelic r : AbstractDungeon.player.relics) {
            numCards = r.changeNumberOfCardsInReward(numCards);
        }
        numCards = Math.min(numCards, cards.size());

        Collections.shuffle(cards, new java.util.Random(AbstractDungeon.cardRng.randomLong()));
        return new ArrayList<>(cards.subList(0, numCards));
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
            new ZoneEncounter(THREE_ELEMENTALS, 2, () -> new MonsterGroup(generateElementalGroup())),
            new ZoneEncounter(VOID_CORRUPTION_AND_ORB_OF_FIRE, 2,  () -> new MonsterGroup(
                new AbstractMonster[] {
                        new OrbOfFire(-350.0F, 125.0F),
                        new VoidCorruption(0.0F, 0.0F)
                })),
            new ZoneEncounter(THREE_HATCHLINGS, 3, () -> new MonsterGroup(
                new AbstractMonster[] {
                        new Hatchling(-450.0F, 10.0F, false),
                        new Hatchling(-150.0F, -30.0F, false),
                        new Hatchling(150.0F, 20.0F, false)
                }))
        );
    }

    private static AbstractMonster[] generateElementalGroup() {
        float[] groupPositions = {-450.0F, -200.0F, 50.0F};
        ArrayList<String> monstersList = new ArrayList<>();
        monstersList.add(OrbOfFire.ID);
        monstersList.add(LivingStormcloud.ID);
        monstersList.add(OpulentOffering.ID);
        monstersList.add(ShimmeringMirage.ID);
        Collections.shuffle(monstersList, new java.util.Random(AbstractDungeon.monsterRng.randomLong()));

        AbstractMonster[] monsters = new AbstractMonster[3];
        for (int i = 0; i < 3; i++) {
            monsters[i] = getElemental(monstersList.get(i), groupPositions[i], 125.0F);
        }

        return monsters;
    }

    public static AbstractMonster getElemental(String elementalID, float x, float y) {
        if (elementalID.equals(OrbOfFire.ID)) {
            return new OrbOfFire(x, y);
        }
        if (elementalID.equals(LivingStormcloud.ID)) {
            return new LivingStormcloud(x, y);
        }
        if (elementalID.equals(OpulentOffering.ID)) {
            return new OpulentOffering(x, y);
        }
        if (elementalID.equals(ShimmeringMirage.ID)) {
            return new ShimmeringMirage(x, y);
        }
        SpireAnniversary6Mod.logger.warn("Didn't match any elemental. ElementalID:" + elementalID);
        return new OrbOfFire(x, y);
    }

    @Override
    public List<ZoneEncounter> getEliteEncounters() {
        return Arrays.asList(
            new ZoneEncounter(Hydra.ID, 1, (BaseMod.GetMonster)Hydra::new),
            new ZoneEncounter(VoidReaper.ID, 1, (BaseMod.GetMonster)VoidReaper::new),
            new ZoneEncounter(WarGolem.ID, 1, (BaseMod.GetMonster)WarGolem::new),
            new ZoneEncounter(ElementalPortal.ID, 1, () -> new ElementalPortal(150.0F, 0.0F))
        );
    }
}
