package spireMapOverhaul.zones.gremlinTown.monsters;

import basemod.abstracts.CustomMonster;
import com.badlogic.gdx.math.MathUtils;
import com.esotericsoftware.spine.AnimationState;
import com.megacrit.cardcrawl.actions.animations.AnimateSlowAttackAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.RollMoveAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import spireMapOverhaul.SpireAnniversary6Mod;
import spireMapOverhaul.zones.gremlinTown.powers.RagingPower;

import static spireMapOverhaul.util.Wiz.*;

public class GremlinBarbarian extends CustomMonster
{
    public static final String ID = SpireAnniversary6Mod.makeID(GremlinBarbarian.class.getSimpleName());
    public static final String NAME;
    private static final String[] MOVES;
    private static final String SKELETON_ATLAS = SpireAnniversary6Mod.makeImagePath(
            "monsters/GremlinTown/GremlinBarbarian/skeleton.atlas");
    private static final String SKELETON_JSON = SpireAnniversary6Mod.makeImagePath(
            "monsters/GremlinTown/GremlinBarbarian/skeleton.json");
    private boolean firstMove = true;
    private static final byte ATTACK = 1;
    private static final int DAMAGE = 3;
    private static final int DAMAGE_A2 = 4;
    private static final int MIN_HP = 50;
    private static final int MAX_HP = 54;
    private static final int MIN_HP_A7 = 54;
    private static final int MAX_HP_A7 = 58;
    private static final int ATTACK_COUNT = 2;
    private static final int RITUAL_AMOUNT = 2;

    private final int attackDamage;

    public GremlinBarbarian() {
        this(0.0f, 0.0f);
    }

    public GremlinBarbarian(final float x, final float y) {
        super(GremlinBarbarian.NAME, ID, MAX_HP, 0, 0, 130.0f/0.7f, 194/0.7f, null, x, y);

        type = EnemyType.NORMAL;
        loadAnimation(SKELETON_ATLAS, SKELETON_JSON, 0.7F);
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

    public void usePreBattleAction() {
        atb(new ApplyPowerAction(this, this, new RagingPower(this, RITUAL_AMOUNT)));
    }

    @Override
    public void takeTurn() {
        if (firstMove)
            firstMove = false;

        atb(new AnimateSlowAttackAction(this));
        for (int i = 0; i < ATTACK_COUNT; i++)
            atb(new DamageAction(adp(), damage.get(0), getRandomSlash()));

        atb(new RollMoveAction(this));
    }

    @Override
    protected void getMove(final int num) {
        setMove(MOVES[0], ATTACK, Intent.ATTACK, attackDamage, ATTACK_COUNT, true);
    }

    static {
        MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID);
        NAME = monsterStrings.NAME;
        MOVES = monsterStrings.MOVES;
    }
}