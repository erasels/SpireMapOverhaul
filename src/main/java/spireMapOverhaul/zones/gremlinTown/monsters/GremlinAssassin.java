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
import spireMapOverhaul.zones.gremlinTown.powers.SapPower;
import spireMapOverhaul.zones.invasion.powers.DrawReductionSingleTurnPower;

import static spireMapOverhaul.util.Wiz.*;

public class GremlinAssassin extends CustomMonster
{
    public static final String ID = SpireAnniversary6Mod.makeID(GremlinAssassin.class.getSimpleName());
    public static final String NAME;
    private static final String[] MOVES;
    private static final String SKELETON_ATLAS = SpireAnniversary6Mod.makeImagePath(
            "monsters/GremlinTown/GremlinAssassin/skeleton.atlas");
    private static final String SKELETON_JSON = SpireAnniversary6Mod.makeImagePath(
            "monsters/GremlinTown/GremlinAssassin/skeleton.json");
    private boolean firstMove = true;
    private static final byte HAMSTRING = 1;
    private static final byte SAP = 2;
    private static final int DAMAGE_HAMSTRING = 10;
    private static final int DAMAGE_HAMSTRING_A2 = 11;
    private static final int DAMAGE_SAP = 7;
    private static final int DAMAGE_SAP_A2 = 8;
    private static final int MIN_HP = 35;
    private static final int MAX_HP = 39;
    private static final int MIN_HP_A7 = 38;
    private static final int MAX_HP_A7 = 42;
    private static final int HAMSTRING_AMOUNT = 1;
    private static final int SAP_AMOUNT = 1;

    private final int hamstringDamage;
    private final int sapDamage;

    public GremlinAssassin() {
        this(0.0f, 0.0f);
    }

    public GremlinAssassin(final float x, final float y) {
        super(GremlinAssassin.NAME, ID, MAX_HP, 0, 0, 120.0f/0.7f, 160.0f/0.7f, null, x, y);

        type = EnemyType.NORMAL;
        loadAnimation(SKELETON_ATLAS, SKELETON_JSON, 0.7F);
        AnimationState.TrackEntry e = this.state.setAnimation(0, "animation", true);
        e.setTime(e.getEndTime() * MathUtils.random());

        if (asc() >= 2) {
            hamstringDamage = DAMAGE_HAMSTRING_A2;
            sapDamage = DAMAGE_SAP_A2;
        } else {
            hamstringDamage = DAMAGE_HAMSTRING;
            sapDamage = DAMAGE_SAP;
        }

        if (asc() >= 7)
            setHp(MIN_HP_A7, MAX_HP_A7);
        else
            setHp(MIN_HP, MAX_HP);

        damage.add(new DamageInfo(this, hamstringDamage));
        damage.add(new DamageInfo(this, sapDamage));
    }

    @Override
    public void takeTurn() {
        switch (nextMove) {
            case HAMSTRING:
                atb(new AnimateSlowAttackAction(this));
                atb(new DamageAction(adp(), damage.get(0), getRandomSlash()));
                atb(new ApplyPowerAction(adp(), this, new DrawReductionSingleTurnPower(adp(), HAMSTRING_AMOUNT)));
                break;
            case SAP:
                atb(new AnimateSlowAttackAction(this));
                atb(new DamageAction(adp(), damage.get(1), getRandomSlash()));
                atb(new ApplyPowerAction(adp(), this, new SapPower(adp(), SAP_AMOUNT)));
        }
        atb(new RollMoveAction(this));
    }

    @Override
    protected void getMove(final int num) {
        if (firstMove) {
            firstMove = false;
            if (asc() >= 17)
                setMove(MOVES[1], SAP, Intent.ATTACK_DEBUFF, sapDamage);
            else
                setMove(MOVES[0], HAMSTRING, Intent.ATTACK_DEBUFF, hamstringDamage);

            return;
        }
        if (lastMove(HAMSTRING))
            setMove(MOVES[1], SAP, Intent.ATTACK_DEBUFF, sapDamage);
        else
            setMove(MOVES[0], HAMSTRING, Intent.ATTACK_DEBUFF, hamstringDamage);
    }

    static {
        MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID);
        NAME = monsterStrings.NAME;
        MOVES = monsterStrings.MOVES;
    }
}