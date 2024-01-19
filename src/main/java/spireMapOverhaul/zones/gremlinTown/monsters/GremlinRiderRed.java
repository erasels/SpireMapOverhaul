package spireMapOverhaul.zones.gremlinTown.monsters;

import basemod.abstracts.CustomMonster;
import com.badlogic.gdx.math.MathUtils;
import com.esotericsoftware.spine.AnimationState;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.AnimateSlowAttackAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.RollMoveAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.powers.StrengthPower;
import spireMapOverhaul.SpireAnniversary6Mod;

import static spireMapOverhaul.util.Wiz.*;

public class GremlinRiderRed extends CustomMonster
{
    public static final String ID = SpireAnniversary6Mod.makeID(GremlinRiderRed.class.getSimpleName());
    public static final String NAME;
    private static final String[] MOVES;
    private static final String SKELETON_ATLAS = SpireAnniversary6Mod.makeImagePath(
            "monsters/GremlinTown/GremlinRiderRed/GremlinRider.atlas");
    private static final String SKELETON_JSON = SpireAnniversary6Mod.makeImagePath(
            "monsters/GremlinTown/GremlinRiderRed/GremlinRider.json");
    private boolean firstMove = true;
    private static final byte CHARGE = 1;
    private static final byte TACTICAL_ATTACK = 2;
    private static final int CHARGE_DAMAGE = 7;
    private static final int CHARGE_DAMAGE_A2 = 8;
    private static final int TACTICAL_DAMAGE = 4;
    private static final int TACTICAL_DAMAGE_A2 = 5;
    private static final int TACTICAL_STRENGTH = 3;
    private static final int TACTICAL_STRENGTH_A17 = 2;
    private static final int MIN_HP = 45;
    private static final int MAX_HP = 50;
    private static final int MIN_HP_A7 = 49;
    private static final int MAX_HP_A7 = 54;

    private final int tacticalDamage;
    private final int chargeDamage;

    public GremlinRiderRed() {
        this(0.0f, 0.0f);
    }

    public GremlinRiderRed(final float x, final float y) {
        super(GremlinRiderRed.NAME, ID, MAX_HP, 0, 0, 180.0f, 200.0f, null, x, y);

        type = EnemyType.NORMAL;
        loadAnimation(SKELETON_ATLAS, SKELETON_JSON, 1.0F);
        AnimationState.TrackEntry e = this.state.setAnimation(0, "Idle", true);
        e.setTime(e.getEndTime() * MathUtils.random());

        if (asc() >= 7)
            setHp(MIN_HP_A7, MAX_HP_A7);
        else
            setHp(MIN_HP, MAX_HP);

        if (asc() >= 2) {
            chargeDamage = CHARGE_DAMAGE_A2;
            tacticalDamage = TACTICAL_DAMAGE_A2;
        } else {
            chargeDamage = CHARGE_DAMAGE;
            tacticalDamage = TACTICAL_DAMAGE;
        }

        damage.add(new DamageInfo(this, chargeDamage));
        damage.add(new DamageInfo(this, tacticalDamage));
    }

    @Override
    public void takeTurn() {
        if (firstMove)
            firstMove = false;

        switch (nextMove) {
            case CHARGE:
                atb(new AnimateSlowAttackAction(this));
                atb(new DamageAction(adp(), damage.get(0), AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
                break;
            case TACTICAL_ATTACK:
                atb(new AnimateSlowAttackAction(this));
                atb(new DamageAction(adp(), damage.get(1), AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
                atb(new WaitAction(0.1f));
                if (asc() < 17)
                    applyToEnemy(this, new StrengthPower(this, TACTICAL_STRENGTH));
                else
                    forAllMonstersLiving(m -> applyToEnemy(m, new StrengthPower(m, TACTICAL_STRENGTH_A17)));
        }
        atb(new RollMoveAction(this));
    }

    @Override
    protected void getMove(final int num) {
        if (lastMove(CHARGE) && lastMoveBefore(CHARGE)) {
            setMove(MOVES[1], TACTICAL_ATTACK, Intent.ATTACK_BUFF, tacticalDamage);
            return;
        }
        if (lastMove(TACTICAL_ATTACK) && lastMoveBefore(TACTICAL_ATTACK)) {
            setMove(MOVES[0], CHARGE, Intent.ATTACK, chargeDamage);
            return;
        }

        if (num < 50)
            setMove(MOVES[0], CHARGE, Intent.ATTACK, chargeDamage);
        else
            setMove(MOVES[1], TACTICAL_ATTACK, Intent.ATTACK_BUFF, tacticalDamage);
    }

    static {
        MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID);
        NAME = monsterStrings.NAME;
        MOVES = monsterStrings.MOVES;
    }
}