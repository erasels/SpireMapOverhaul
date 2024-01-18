package spireMapOverhaul.zones.gremlinTown.monsters;

import basemod.abstracts.CustomMonster;
import com.badlogic.gdx.math.MathUtils;
import com.esotericsoftware.spine.AnimationState;
import com.megacrit.cardcrawl.actions.common.RollMoveAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import spireMapOverhaul.SpireAnniversary6Mod;
import spireMapOverhaul.zones.gremlinTown.actions.RockThrowAction;

import static spireMapOverhaul.util.Wiz.asc;
import static spireMapOverhaul.util.Wiz.atb;

public class GremlinRockTosser extends CustomMonster
{
    public static final String ID = SpireAnniversary6Mod.makeID(GremlinRockTosser.class.getSimpleName());
    public static final String NAME;
    private static final String[] MOVES;
    private static final String SKELETON_ATLAS = SpireAnniversary6Mod.makeImagePath(
            "monsters/GremlinTown/GremlinRockTosser/skeleton.atlas");
    private static final String SKELETON_JSON = SpireAnniversary6Mod.makeImagePath(
            "monsters/GremlinTown/GremlinRockTosser/skeleton.json");
    private static final byte ATTACK = 1;
    private static final int DAMAGE = 6;
    private static final int DAMAGE_A2 = 7;
    private static final int MIN_HP = 28;
    private static final int MAX_HP = 32;
    private static final int MIN_HP_A7 = 31;
    private static final int MAX_HP_A7 = 35;

    private final int attackDamage;

    public GremlinRockTosser() {
        this(0.0f, 0.0f);
    }

    public GremlinRockTosser(final float x, final float y) {
        super(GremlinRockTosser.NAME, ID, MAX_HP, -4, 12, 130.0f, 174, null, x, y);

        type = EnemyType.NORMAL;
        loadAnimation(SKELETON_ATLAS, SKELETON_JSON, 1F);
        AnimationState.TrackEntry e = state.setAnimation(0, "idle", true);
        e.setTime(e.getEndTime() * MathUtils.random());

        if (asc() >= 2)
            attackDamage = DAMAGE_A2;
        else
            attackDamage = DAMAGE;

        if (asc() >= 7)
            setHp(MIN_HP_A7, MAX_HP_A7);
        else
            setHp(MIN_HP, MAX_HP);

        damage.add(new DamageInfo(this, attackDamage));
    }

    @Override
    public void takeTurn() {
        atb(new RockThrowAction(this, damage.get(0)));
        atb(new RollMoveAction(this));
    }

    @Override
    protected void getMove(final int num) {
        setMove(ATTACK, Intent.ATTACK, attackDamage);
    }

    static {
        MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID);
        NAME = monsterStrings.NAME;
        MOVES = monsterStrings.MOVES;
    }
}