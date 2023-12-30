package spireMapOverhaul.zones.gremlinTown;

import basemod.BaseMod;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.MonsterHelper;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.MonsterGroup;
import com.megacrit.cardcrawl.monsters.exordium.AcidSlime_S;
import com.megacrit.cardcrawl.relics.QuestionCard;
import com.megacrit.cardcrawl.rewards.RewardItem;
import spireMapOverhaul.SpireAnniversary6Mod;
import spireMapOverhaul.abstracts.AbstractZone;
import spireMapOverhaul.zoneInterfaces.EncounterModifyingZone;
import spireMapOverhaul.zoneInterfaces.RewardModifyingZone;
import spireMapOverhaul.zones.gremlinTown.potions.*;
import spireMapOverhaul.zones.gremlinTown.cards.*;
import spireMapOverhaul.zones.gremlinTown.monsters.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static spireMapOverhaul.util.Wiz.adp;
import static spireMapOverhaul.util.Wiz.asc;

public class GremlinTown extends AbstractZone
        implements EncounterModifyingZone,
        RewardModifyingZone {
    public static final String ID = GremlinTown.class.getSimpleName();
    public static final String GREMLIN_RIDERS = SpireAnniversary6Mod.makeID("Gremlin_Riders");
    public static final String GREMLIN_GANG_TWO = SpireAnniversary6Mod.makeID("Gremlin_Gang_Two");
    public static final String NIB = SpireAnniversary6Mod.makeID("Nib");
    private static ArrayList<AbstractCard> gremlinCards;

    private ZoneEncounter GREMLIN_LEADER_ENCOUNTER = new ZoneEncounter(MonsterHelper.GREMLIN_LEADER_ENC, 2,
            () -> MonsterHelper.getEncounter("Gremlin Leader"));

    public GremlinTown() {
        super(ID, Icons.MONSTER, Icons.ELITE, Icons.EVENT);
        width = 5;
        height = 5;
    }

    @Override
    public AbstractZone copy() {
        return new GremlinTown();
    }

    @Override
    public Color getColor() {
        return Color.FIREBRICK.cpy();
    }

    @Override
    public boolean canSpawn() {
        return isAct(2);
    }

    @Override
    protected boolean canIncludeEarlyRows() {
        return false;
    }

    @Override
    protected boolean canIncludeFinalCampfireRow() { return true; }

    @Override
    protected boolean canIncludeTreasureRow() { return true; }

    @Override
    public List<ZoneEncounter> getNormalEncounters() {
        // This example has new custom enemies in act 1, remixes of base game enemies in act 2, and isn't valid in act 3
        // (Ignore the lack of any thematic connection, it's just an example)
        return Arrays.asList(
                new ZoneEncounter(GREMLIN_RIDERS, 2, () -> new MonsterGroup(
                        new AbstractMonster[] {
                                new GremlinRiderGreen(-320.0F, -12.0F),
                                new GremlinRiderRed(-100.0F, -30.0F),
                                new GremlinRiderBlue(150.0F, 15.0F)
                        })),
                new ZoneEncounter(GREMLIN_GANG_TWO, 2, GremlinTown::getGremlinGangGroup),
                new ZoneEncounter(NIB, 2, () -> new MonsterGroup(new GremlinNib(0f, 0f)))
        );
    }

    private static MonsterGroup getGremlinGangGroup() {
        ArrayList<String> gremlinPool = new ArrayList();
        gremlinPool.add("ArmoredGremlin");
        gremlinPool.add("ChubbyGremlin");
        gremlinPool.add("GremlinAssassin");
        gremlinPool.add("GremlinBarbarian");
        gremlinPool.add("GremlinHealer");

        AbstractMonster[] retVal = new AbstractMonster[4];
        int index = AbstractDungeon.miscRng.random(gremlinPool.size() - 1);
        String key = gremlinPool.get(index);
        gremlinPool.remove(index);
        retVal[0] = getGremlin(key, -530.0F, -25.0F);
        index = AbstractDungeon.miscRng.random(gremlinPool.size() - 1);
        key = gremlinPool.get(index);
        gremlinPool.remove(index);
        retVal[1] = getGremlin(key, -270.0F, 21.0F);
        index = AbstractDungeon.miscRng.random(gremlinPool.size() - 1);
        key = gremlinPool.get(index);
        gremlinPool.remove(index);
        retVal[2] = getGremlin(key, 15.0F, -30.0F);
        index = AbstractDungeon.miscRng.random(gremlinPool.size() - 1);
        key = gremlinPool.get(index);
        gremlinPool.remove(index);
        retVal[3] = getGremlin(key, 245.0F, 30.0F);

        return new MonsterGroup(retVal);
    }

    public static AbstractMonster getGremlin(String key, float xPos, float yPos) {
        switch (key) {
            case "GremlinAssassin":
                return new GremlinAssassin(xPos, yPos);
            case "GremlinBarbarian":
                return new GremlinBarbarian(xPos, yPos);
            case "GremlinHealer":
                return new GremlinHealer(xPos, yPos);
            case "ChubbyGremlin":
                return new ChubbyGremlin(xPos, yPos);
            case "ArmoredGremlin":
                return new ArmoredGremlin(xPos, yPos);
            default:
                return new AcidSlime_S(xPos, yPos, 0);
        }
    }

    @Override
    public List<ZoneEncounter> getEliteEncounters() {
        // Override that one method so the gremlin leader encounter isn't double registered
        return Arrays.asList(
                GREMLIN_LEADER_ENCOUNTER
        );
    }

    @Override
    public void registerEncounters() {
        List<ZoneEncounter> normalEncounters = this.getNormalEncounters();
        List<ZoneEncounter> encounters = new ArrayList<>();
        if (normalEncounters != null)
            encounters.addAll(normalEncounters);

        //  Will need to manually add all Elite encounters!
        //  encounters.addAll(eliteEncounters);

        for (ZoneEncounter ze : encounters)
            BaseMod.addMonster(ze.getID(), ze.getName(), () -> ze.getMonsterSupplier().get());
    }

    public ArrayList<AbstractCard> getAdditionalCardReward() {
        if (AbstractDungeon.lastCombatMetricKey.equals(GREMLIN_RIDERS)
                || AbstractDungeon.lastCombatMetricKey.equals(NIB)) {
            shuffleCards();
            ArrayList<AbstractCard> cardReward = new ArrayList<AbstractCard>() {
                {
                    add(gremlinCards.get(0).makeCopy());
                    add(gremlinCards.get(1).makeCopy());
                    add(gremlinCards.get(2).makeCopy());
                    if (adp() != null && adp().hasRelic(QuestionCard.ID))
                        add(gremlinCards.get(3).makeCopy());
                }
            };
            for (AbstractCard c : cardReward) {
                float x = AbstractDungeon.cardRng.random(1F);
                if (asc() < 12 && x < 0.25F)
                    c.upgrade();
                else if (x < 0.125F)
                    c.upgrade();
            }
            return cardReward;
        }

        return null;
    }

    @Override
    public RewardItem getAdditionalReward() {
        if (AbstractDungeon.lastCombatMetricKey.equals(GREMLIN_GANG_TWO)
                || AbstractDungeon.lastCombatMetricKey.equals(NIB)) {
            int x = AbstractDungeon.potionRng.random(0, 99);
            if (x < 65) {
                int y = AbstractDungeon.potionRng.random(0, 2);
                if (y == 0)
                    return new RewardItem(new LouseMilk());
                else if (y == 1)
                    return new RewardItem(new MushroomSoup());
                else
                    return new RewardItem(new PreerelxsBlueRibbon());
            }
            if (x < 90) {
                if (x % 2 == 0)
                    return new RewardItem(new GremsFire());
                else
                    return new RewardItem(new NoxiousBrew());
            }
            return new RewardItem(new RitualBlood());
        }

        return null;
    }

    private static void shuffleCards() {
        if (gremlinCards == null)
            initGremlinCards();
        Collections.shuffle(gremlinCards);
    }

    private static void initGremlinCards() {
        gremlinCards = new ArrayList<>();
        gremlinCards.add(new SneakAttack());
        gremlinCards.add(new AngryStrike());
        gremlinCards.add(new Charge());
        gremlinCards.add(new MeatMallet());
        gremlinCards.add(new Shield());
    }
}
