package spireMapOverhaul.zones.gremlinTown;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.MonsterHelper;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.MonsterGroup;
import com.megacrit.cardcrawl.relics.QuestionCard;
import spireMapOverhaul.SpireAnniversary6Mod;
import spireMapOverhaul.abstracts.AbstractZone;
import spireMapOverhaul.zoneInterfaces.EncounterModifyingZone;
import spireMapOverhaul.zoneInterfaces.RewardModifyingZone;
import spireMapOverhaul.zones.gremlinTown.cards.*;
import spireMapOverhaul.zones.gremlinTown.monsters.GremlinRiderBlue;
import spireMapOverhaul.zones.gremlinTown.monsters.GremlinRiderGreen;
import spireMapOverhaul.zones.gremlinTown.monsters.GremlinRiderRed;

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
    private static ArrayList<AbstractCard> gremlinCards;

    public GremlinTown() {
        super(ID, Icons.MONSTER, Icons.ELITE, Icons.EVENT);
        width = 3;
        height = 7;
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
                        new GremlinRiderRed(-350.0F, 0.0F),
                        new GremlinRiderGreen(-100.0F, 0.0F),
                        new GremlinRiderBlue(150.0F, 0.0F)
                }))
        );
    }

    @Override
    public List<ZoneEncounter> getEliteEncounters() {
        return Arrays.asList(
            new ZoneEncounter(MonsterHelper.GREMLIN_LEADER_ENC, 1,
                    () -> MonsterHelper.getEncounter("Gremlin Leader"))
        );
    }

    public ArrayList<AbstractCard> getAdditionalCardReward() {
        if (AbstractDungeon.lastCombatMetricKey.equals(GREMLIN_RIDERS)) {
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
