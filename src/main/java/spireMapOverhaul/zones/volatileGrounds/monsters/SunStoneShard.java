package spireMapOverhaul.zones.volatileGrounds.monsters;

import basemod.abstracts.CustomMonster;
import com.badlogic.gdx.math.MathUtils;
import com.esotericsoftware.spine.AnimationState;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.AnimateFastAttackAction;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.powers.FlameBarrierPower;
import spireMapOverhaul.SpireAnniversary6Mod;
import spireMapOverhaul.zones.volatileGrounds.powers.ChargedPower;

public class SunStoneShard extends CustomMonster {
    public static final String ID = SpireAnniversary6Mod.makeID("SunStoneShard");
    private static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID);
    public static final String NAME = monsterStrings.NAME;
    public static final String[] MOVES = monsterStrings.MOVES;
    private static final String IMG = SpireAnniversary6Mod.makeImagePath("monsters/VolatileGrounds/SunStoneShard/skeleton.png");
    private static final String ATLAS = SpireAnniversary6Mod.makeImagePath("monsters/VolatileGrounds/SunStoneShard/skeleton.atlas");
    private static final String SKELETON = SpireAnniversary6Mod.makeImagePath("monsters/VolatileGrounds/SunStoneShard/skeleton.json");
    private static final byte BUFF = 0;
    private static final byte ATTACK = 1;
    private static final byte BARRIER = 2;
    private static final int BUFF_AMOUNT = 3;
    private static final int A18_BUFF_AMOUNT = 5;
    private static final int ATTACK_DAMAGE = 7;
    private static final int A3_ATTACK_DAMAGE = 9;
    private static final int BARRIER_BLOCK = 5;
    private static final int BARRIER_AMOUNT = 5;
    private static final int A18_BARRIER_AMOUNT = 8;
    private static final int EXPLOSION_DAMAGE = 5;
    private static final int A3_EXPLOSION_DAMAGE = 8;
    private static final int HP_MIN = 60;
    private static final int HP_MAX = 65;
    private static final int A8_HP_MIN = 75;
    private static final int A8_HP_MAX = 85;
    private int attackDamage;
    
    public SunStoneShard(final float x, final float y) {
        super(NAME, ID, HP_MAX, -5.0F, 0, 130.0f, 220f, IMG, x, y);
        if (AbstractDungeon.ascensionLevel >= 8) {
            this.setHp(A8_HP_MIN, A8_HP_MAX);
        } else {
            this.setHp(HP_MIN, HP_MAX);
        }
        if(AbstractDungeon.ascensionLevel >= 3) {
            this.damage.add(new DamageInfo(this, A3_ATTACK_DAMAGE));
            attackDamage = A3_ATTACK_DAMAGE;
        }
        else
        {
            this.damage.add(new DamageInfo(this, ATTACK_DAMAGE));
            attackDamage = ATTACK_DAMAGE;
        }
        this.loadAnimation(ATLAS, SKELETON, 1.00f);
        AnimationState.TrackEntry e = this.state.setAnimation(0, "animation0", true);
        e.setTime(e.getEndTime() * MathUtils.random());
        this.type = EnemyType.ELITE;
    }
    
    @Override
    public void usePreBattleAction() {
        if(AbstractDungeon.ascensionLevel >= 3) {
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new ChargedPower(this, A3_EXPLOSION_DAMAGE)));
        }
        else
        {
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new ChargedPower(this, EXPLOSION_DAMAGE)));
        }
    }
    
    @Override
    public void takeTurn() {
        AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(this, this, FlameBarrierPower.POWER_ID));
        switch (this.nextMove) {
            case ATTACK:
                AbstractDungeon.actionManager.addToBottom(new AnimateFastAttackAction(this));
                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage.get(0), AbstractGameAction.AttackEffect.FIRE));
                break;
            case BARRIER:
                AbstractDungeon.actionManager.addToBottom(new GainBlockAction(this, BARRIER_BLOCK));
                if(AbstractDungeon.ascensionLevel >= 18)
                {
                    AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new FlameBarrierPower(this, A18_BARRIER_AMOUNT)));
                }
                else
                {
                    AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new FlameBarrierPower(this, BARRIER_AMOUNT)));
                }
                break;
            case BUFF:
                if(AbstractDungeon.ascensionLevel >= 18)
                {
                    AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new ChargedPower(this, A18_BUFF_AMOUNT)));
                }
                else {
                    AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new ChargedPower(this, BUFF_AMOUNT)));
                }
                break;
        }
        AbstractDungeon.actionManager.addToBottom(new RollMoveAction(this));
    }
    
    @Override
    protected void getMove(final int num) {
        if (!lastMove(BUFF) && !lastMoveBefore(BUFF)) {
            this.setMove(BUFF, Intent.BUFF);
        } else {
            if ((num > 50 && lastMove(BUFF)) || lastMove(BARRIER)) {
                this.setMove(ATTACK, Intent.ATTACK, attackDamage);
            } else {
                this.setMove(BARRIER, Intent.DEFEND_BUFF);
            }
        }
    }
}
