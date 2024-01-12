package spireMapOverhaul.zones.gremlinTown.monsters;

import basemod.abstracts.CustomMonster;
import com.badlogic.gdx.math.MathUtils;
import com.esotericsoftware.spine.AnimationState;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.AnimateSlowAttackAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.RollMoveAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import spireMapOverhaul.SpireAnniversary6Mod;

import static spireMapOverhaul.util.Wiz.*;

public class GremlinRockTosser extends CustomMonster
{
    public static final String ID = SpireAnniversary6Mod.makeID(GremlinRockTosser.class.getSimpleName());
    public static final String NAME;
    private static final String[] MOVES;
    private static final String SKELETON_ATLAS = SpireAnniversary6Mod.makeImagePath(
            "images/monsters/theBottom/angryGremlin/skeleton.atlas");
    private static final String SKELETON_JSON = SpireAnniversary6Mod.makeImagePath(
            "images/monsters/theBottom/angryGremlin/skeleton.json");
    private boolean firstMove = true;
    private static final byte ATTACK = 1;
    private static final int DAMAGE = 7;
    private static final int DAMAGE_A2 = 8;
    private static final int MIN_HP = 22;
    private static final int MAX_HP = 25;
    private static final int MIN_HP_A7 = 24;
    private static final int MAX_HP_A7 = 27;

    private final int attackDamage;

    public GremlinRockTosser() {
        this(0.0f, 0.0f);
    }

    public GremlinRockTosser(final float x, final float y) {
        super(GremlinRockTosser.NAME, ID, MAX_HP, 0, 0, 130.0f, 194, null, x, y);

        type = EnemyType.NORMAL;
        loadAnimation(SKELETON_ATLAS, SKELETON_JSON, 1F);
        AnimationState.TrackEntry e = this.state.setAnimation(0, "idle", true);
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
        if (firstMove)
            firstMove = false;

        atb(new AnimateSlowAttackAction(this));
        atb(new DamageAction(adp(), damage.get(0), AbstractGameAction.AttackEffect.BLUNT_LIGHT));

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