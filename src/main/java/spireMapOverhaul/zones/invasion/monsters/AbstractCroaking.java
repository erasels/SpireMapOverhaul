package spireMapOverhaul.zones.invasion.monsters;

import basemod.abstracts.CustomMonster;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.AnimateFastAttackAction;
import com.megacrit.cardcrawl.actions.animations.TalkAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.RollMoveAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;

public abstract class AbstractCroaking extends CustomMonster {
    private boolean firstMove = true;
    private static final byte LACERATE_ATTACK = 1;
    private static final byte SECOND_MOVE = 2;
    private static final int LACERATE_DAMAGE = 1;
    private static final int A2_LACERATE_DAMAGE = 2;
    private static final int LACERATE_HITS = 2;
    private static final int BUFF = 1;
    private static final int A17_BUFF = 2;
    private static final int HP_MIN = 50;
    private static final int HP_MAX = 55;
    private static final int A7_HP_MIN = 53;
    private static final int A7_HP_MAX = 58;
    private int lacerateDamage;
    private int buff;

    public AbstractCroaking(String name, String id, float hb_x, float hb_y, float hb_w, float hb_h, String imgUrl, float offsetX, float offsetY) {
        super(name, id, HP_MAX, hb_x, hb_y, hb_w, hb_h, imgUrl, offsetX, offsetY);
        this.type = EnemyType.NORMAL;
        if (AbstractDungeon.ascensionLevel >= 7) {
            this.setHp(A7_HP_MIN, A7_HP_MAX);
        } else {
            this.setHp(HP_MIN, HP_MAX);
        }

        if (AbstractDungeon.ascensionLevel >= 2) {
            this.lacerateDamage = A2_LACERATE_DAMAGE;
        } else {
            this.lacerateDamage = LACERATE_DAMAGE;
        }
        this.damage.add(new DamageInfo(this, this.lacerateDamage));

        if (AbstractDungeon.ascensionLevel >= 17) {
            this.buff = A17_BUFF;
        } else {
            this.buff = BUFF;
        }
    }

    protected abstract AbstractPower getBuffPower(int amount);
    protected abstract String getDialog();
    protected abstract String getFirstMoveName();
    protected abstract AbstractGameAction.AttackEffect getFirstMoveAttackEffect();
    protected abstract void executeSecondMove();
    protected abstract void setSecondMoveIntent();

    @Override
    public void usePreBattleAction() {
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, this.getBuffPower(this.buff), this.buff));
    }

    @Override
    public void takeTurn() {
        if (this.firstMove) {
            this.firstMove = false;
        }
        if (MathUtils.random(99) < 2) {
            AbstractDungeon.actionManager.addToBottom(new TalkAction(this, this.getDialog(), 0.5F, 2.0F));
        }
        switch (this.nextMove) {
            case LACERATE_ATTACK:
                AbstractDungeon.actionManager.addToBottom(new AnimateFastAttackAction(this));
                for (int i=0; i < LACERATE_HITS; i++) {
                    AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage.get(0), this.getFirstMoveAttackEffect()));
                }
                break;
            case SECOND_MOVE:
                this.executeSecondMove();
                break;
        }
        AbstractDungeon.actionManager.addToBottom(new RollMoveAction(this));
    }

    @Override
    protected void getMove(final int num) {
        if (!this.lastMove(LACERATE_ATTACK) && (num < 40 || (!this.firstMove && !this.lastMoveBefore(LACERATE_ATTACK)))) {
            this.setMove(this.getFirstMoveName(), LACERATE_ATTACK, Intent.ATTACK, this.lacerateDamage, LACERATE_HITS, true);
        }
        else {
            this.setSecondMoveIntent();
        }
    }
}