package spireMapOverhaul.zones.invasion.monsters;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.AnimateFastAttackAction;
import com.megacrit.cardcrawl.actions.animations.AnimateSlowAttackAction;
import com.megacrit.cardcrawl.actions.animations.FastShakeAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.RollMoveAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.powers.IntangiblePower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import spireMapOverhaul.SpireAnniversary6Mod;
import spireMapOverhaul.abstracts.AbstractSMOMonster;
import spireMapOverhaul.zones.invasion.powers.FixedTextIntangiblePower;

import java.util.Random;

import static spireMapOverhaul.util.Wiz.atb;

public class WhisperingWraith extends AbstractSMOMonster
{
    public static final String ID = SpireAnniversary6Mod.makeID("WhisperingWraith");
    private static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID);
    public static final String NAME = monsterStrings.NAME;
    public static final String[] MOVES = monsterStrings.MOVES;
    private static final String IMG = SpireAnniversary6Mod.makeMonsterPath("Invasion/WhisperingWraith/WhisperingWraith.png");
    private boolean firstMove = true;
    private static final byte FADE_AWAY_ATTACK = 1;
    private static final byte GHOSTLY_TOUCH_ATTACK = 2;
    private static final byte ABSORB_LIFE_MOVE = 3;
    private static final int FADE_AWAY_DAMAGE = 11;
    private static final int A2_FADE_AWAY_DAMAGE = 12;
    private static final int GHOSTLY_TOUCH_DAMAGE = 7;
    private static final int A2_GHOSTLY_TOUCH_DAMAGE = 8;
    private static final int ABSORB_LIFE_STRENGTH = 2;
    private static final int A17_ABSORB_LIFE_STRENGTH = 4;
    private static final int HP_MIN = 43;
    private static final int HP_MAX = 45;
    private static final int A7_HP_MIN = 44;
    private static final int A7_HP_MAX = 46;
    private int fadeAwayDamage;
    private int ghostlyTouchDamage;
    private int absorbLifeStrength;
    private final Random random = new Random();

    public WhisperingWraith() {
        this(0.0f, 0.0f);
    }

    public WhisperingWraith(final float x, final float y) {
        super(WhisperingWraith.NAME, ID, HP_MAX, -5.0F, 0, 145.0f, 205.0f, IMG, x, y);
        type = EnemyType.NORMAL;
        if (AbstractDungeon.ascensionLevel >= 7) {
            setHp(A7_HP_MIN, A7_HP_MAX);
        } else {
            setHp(HP_MIN, HP_MAX);
        }

        if (AbstractDungeon.ascensionLevel >= 2) {
            fadeAwayDamage = A2_FADE_AWAY_DAMAGE;
            ghostlyTouchDamage = A2_GHOSTLY_TOUCH_DAMAGE;
        } else {
            fadeAwayDamage = FADE_AWAY_DAMAGE;
            ghostlyTouchDamage = GHOSTLY_TOUCH_DAMAGE;
        }
        damage.add(new DamageInfo(this, fadeAwayDamage));
        damage.add(new DamageInfo(this, ghostlyTouchDamage));

        if (AbstractDungeon.ascensionLevel >= 17) {
            absorbLifeStrength = A17_ABSORB_LIFE_STRENGTH;
        }
        else {
            absorbLifeStrength = ABSORB_LIFE_STRENGTH;
        }
    }

    @Override
    public void takeTurn() {
        if (firstMove) {
            firstMove = false;
        }
        switch (nextMove) {
            case FADE_AWAY_ATTACK:
                atb(new AnimateSlowAttackAction(this));
                atb(new DamageAction(AbstractDungeon.player, damage.get(0), AbstractGameAction.AttackEffect.NONE));
                atb(new ApplyPowerAction(this, this, new FixedTextIntangiblePower(this, 1)));
                break;
            case GHOSTLY_TOUCH_ATTACK:
                atb(new AnimateFastAttackAction(this));
                atb(new DamageAction(AbstractDungeon.player, damage.get(1), AbstractGameAction.AttackEffect.NONE));
                break;
            case ABSORB_LIFE_MOVE:
                atb(new FastShakeAction(this, 0.5F, 0.2F));
                atb(new ApplyPowerAction(this, this, new StrengthPower(this, absorbLifeStrength), absorbLifeStrength));
                break;
        }
        atb(new RollMoveAction(this));
    }

    @Override
    protected void getMove(final int num) {
        if (firstMove || lastMove(ABSORB_LIFE_MOVE)) {
            setMove(WhisperingWraith.MOVES[0], FADE_AWAY_ATTACK, Intent.ATTACK_DEFEND, fadeAwayDamage);
        }
        else if (lastMove(FADE_AWAY_ATTACK)) {
            setMove(WhisperingWraith.MOVES[1], GHOSTLY_TOUCH_ATTACK, Intent.ATTACK, ghostlyTouchDamage);
        }
        else {
            setMove(WhisperingWraith.MOVES[2], ABSORB_LIFE_MOVE, Intent.BUFF);
        }
    }

    @Override
    public void damage(DamageInfo info) {
        if (info.output > 0 && hasPower(IntangiblePower.POWER_ID)) {
            info.output = 1;
        }

        super.damage(info);
    }
}