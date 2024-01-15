package spireMapOverhaul.zones.gremlinTown.monsters;

import basemod.abstracts.CustomMonster;
import com.badlogic.gdx.math.MathUtils;
import com.esotericsoftware.spine.AnimationState;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.AnimateSlowAttackAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.actions.common.RollMoveAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import spireMapOverhaul.SpireAnniversary6Mod;
import spireMapOverhaul.util.Wiz;

import static spireMapOverhaul.util.Wiz.*;

public class GremlinHealer extends CustomMonster
{
    public static final String ID = SpireAnniversary6Mod.makeID(GremlinHealer.class.getSimpleName());
    public static final String NAME;
    private static final String[] MOVES;
    private static final String SKELETON_ATLAS = SpireAnniversary6Mod.makeImagePath(
            "monsters/GremlinTown/GremlinHealer/skeleton.atlas");
    private static final String SKELETON_JSON = SpireAnniversary6Mod.makeImagePath(
            "monsters/GremlinTown/GremlinHealer/skeleton.json");
    private boolean firstMove = true;
    private static final byte CURE = 1;
    private static final byte ATTACK = 2;
    private static final int DAMAGE = 10;
    private static final int DAMAGE_A2 = 11;
    private static final int HEAL = 12;
    private static final int HEAL_A17 = 15;
    private static final int MIN_HP = 44;
    private static final int MAX_HP = 49;
    private static final int MIN_HP_A7 = 48;
    private static final int MAX_HP_A7 = 53;

    private final int attackDamage;
    private final int heal;

    public GremlinHealer() {
        this(0.0f, 0.0f);
    }

    public GremlinHealer(final float x, final float y) {
        super(GremlinHealer.NAME, ID, MAX_HP, 40.0f/0.7f, -5f/0.7f, 130.0f/0.7f, 160.0f/0.7f, null, x - 35.0f/0.7f, y);

        type = EnemyType.NORMAL;
        loadAnimation(SKELETON_ATLAS, SKELETON_JSON, 0.7F);
        AnimationState.TrackEntry e = this.state.setAnimation(0, "animation", true);
        e.setTime(e.getEndTime() * MathUtils.random());

        if (asc() >= 7)
            setHp(MIN_HP_A7, MAX_HP_A7);
        else
            setHp(MIN_HP, MAX_HP);

        if (asc() >= 2)
            attackDamage = DAMAGE_A2;
        else
            attackDamage = DAMAGE;

        if (asc() >= 17)
            heal = HEAL_A17;
        else
            heal = HEAL;

        damage.add(new DamageInfo(this, attackDamage));
    }

    @Override
    public void takeTurn() {
        if (firstMove)
            firstMove = false;

        switch (nextMove) {
            case CURE:
                forAllMonstersLiving(m -> atb(new HealAction(m, this, heal)));
                break;
            case ATTACK:
                atb(new AnimateSlowAttackAction(this));
                atb(new DamageAction(adp(), damage.get(0), AbstractGameAction.AttackEffect.BLUNT_LIGHT));
        }
        atb(new RollMoveAction(this));
    }

    @Override
    protected void getMove(final int num) {
        if (Wiz.getEnemies().size() > 1)
            setMove(MOVES[0], CURE, Intent.BUFF);
        else
            setMove(ATTACK, Intent.ATTACK, attackDamage);
    }

    static {
        MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID);
        NAME = monsterStrings.NAME;
        MOVES = monsterStrings.MOVES;
    }
}