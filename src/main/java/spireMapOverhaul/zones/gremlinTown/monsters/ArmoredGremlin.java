package spireMapOverhaul.zones.gremlinTown.monsters;

import basemod.abstracts.CustomMonster;
import com.badlogic.gdx.math.MathUtils;
import com.esotericsoftware.spine.AnimationState;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.AnimateSlowAttackAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.RollMoveAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.powers.BarricadePower;
import spireMapOverhaul.SpireAnniversary6Mod;
import spireMapOverhaul.zones.gremlinTown.powers.RagingPower;

import static spireMapOverhaul.util.Wiz.*;

public class ArmoredGremlin extends CustomMonster
{
    public static final String ID = SpireAnniversary6Mod.makeID(ArmoredGremlin.class.getSimpleName());
    public static final String NAME;
    private static final String[] MOVES;
    private static final String SKELETON_ATLAS = SpireAnniversary6Mod.makeImagePath(
            "monsters/GremlinTown/ArmoredGremlin/skeleton.atlas");
    private static final String SKELETON_JSON = SpireAnniversary6Mod.makeImagePath(
            "monsters/GremlinTown/ArmoredGremlin/skeleton.json");
    private boolean firstMove = true;
    private static final byte ATTACK = 1;
    private static final int DAMAGE = 6;
    private static final int DAMAGE_A2 = 7;
    private static final int MIN_HP = 35;
    private static final int MAX_HP = 39;
    private static final int MIN_HP_A7 = 38;
    private static final int MAX_HP_A7 = 42;
    private static final int BLOCK_AMOUNT = 10;
    private static final int BLOCK_AMOUNT_A17 = 12;
    private static final int RAGE_AMOUNT = 2;

    private final int attackDamage;
    private final int blockAmount;

    public ArmoredGremlin() {
        this(0.0f, 0.0f);
    }

    public ArmoredGremlin(final float x, final float y) {
        super(ArmoredGremlin.NAME, ID, MAX_HP, 0, 0, 120.0f/0.7f, 200.0f/0.7f, null, x, y);

        type = EnemyType.NORMAL;
        loadAnimation(SKELETON_ATLAS, SKELETON_JSON, 0.7F);
        AnimationState.TrackEntry e = this.state.setAnimation(0, "idle", true);
        e.setTime(e.getEndTime() * MathUtils.random());
        setHp(MIN_HP, MAX_HP);

        if (asc() >= 2)
            attackDamage = DAMAGE_A2;
        else
            attackDamage = DAMAGE;

        if (asc() >= 7)
            setHp(MIN_HP_A7, MAX_HP_A7);
        else
            setHp(MIN_HP, MAX_HP);

        if (asc() >= 17)
            blockAmount = BLOCK_AMOUNT_A17;
        else
            blockAmount = BLOCK_AMOUNT;

        damage.add(new DamageInfo(this, attackDamage));
    }

    public void usePreBattleAction() {
        atb(new ApplyPowerAction(this, this, new BarricadePower(this)));
        atb(new ApplyPowerAction(this, this, new RagingPower(this, RAGE_AMOUNT)));
        SpireAnniversary6Mod.logger.info(name);
        atb(new GainBlockAction(this, blockAmount));
    }

    @Override
    public void takeTurn() {
        if (firstMove)
            firstMove = false;

        atb(new AnimateSlowAttackAction(this));
        atb(new DamageAction(adp(), damage.get(0), AbstractGameAction.AttackEffect.BLUNT_LIGHT));
        atb(new GainBlockAction(this, blockAmount));
        atb(new RollMoveAction(this));
    }

    @Override
    protected void getMove(final int num) {
        setMove(MOVES[0], ATTACK, Intent.ATTACK_DEFEND, attackDamage);
    }

    static {
        MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID);
        NAME = monsterStrings.NAME;
        MOVES = monsterStrings.MOVES;
    }
}