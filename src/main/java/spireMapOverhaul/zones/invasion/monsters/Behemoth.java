package spireMapOverhaul.zones.invasion.monsters;

import basemod.abstracts.CustomMonster;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.AnimateSlowAttackAction;
import com.megacrit.cardcrawl.actions.animations.FastShakeAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.RollMoveAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.powers.BarricadePower;
import spireMapOverhaul.SpireAnniversary6Mod;
import spireMapOverhaul.zones.invasion.powers.EvolvingPower;

public class Behemoth extends CustomMonster {
    public static final String ID = SpireAnniversary6Mod.makeID("Behemoth");
    private static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID);
    public static final String NAME = monsterStrings.NAME;
    public static final String[] MOVES = monsterStrings.MOVES;
    private static final String IMG = SpireAnniversary6Mod.makeImagePath("monsters/Invasion/Behemoth/Behemoth.png");
    private boolean firstMove = true;
    private static final byte BIDE_MOVE = 1;
    private static final byte EVOLVE_MOVE = 2;
    private static final byte CLAW_SWIPE_ATTACK = 3;
    private static final int CLAW_SWIPE_DAMAGE = 13;
    private static final int A3_CLAW_SWIPE_DAMAGE = 15;
    private static final int STARTING_BLOCK = 90;
    private static final int A8_STARTING_BLOCK = 100;
    private static final int HP_MIN = 330;
    private static final int HP_MAX = 330;
    private static final int A8_HP_MIN = 360;
    private static final int A8_HP_MAX = 360;
    private int clawSwipeDamage;
    private int startingBlock;

    public Behemoth() {
        this(0.0f, 0.0f);
    }

    public Behemoth(final float x, final float y) {
        super(Behemoth.NAME, ID, HP_MAX, -5.0F, 0, 580.0f, 330.0f, IMG, x, y);
        this.type = EnemyType.ELITE;
        if (AbstractDungeon.ascensionLevel >= 8) {
            this.setHp(A8_HP_MIN, A8_HP_MAX);
            this.startingBlock = A8_STARTING_BLOCK;
        } else {
            this.setHp(HP_MIN, HP_MAX);
            this.startingBlock = STARTING_BLOCK;
        }

        if (AbstractDungeon.ascensionLevel >= 3) {
            this.clawSwipeDamage = A3_CLAW_SWIPE_DAMAGE;
        } else {
            this.clawSwipeDamage = CLAW_SWIPE_DAMAGE;
        }
        this.damage.add(new DamageInfo(this, this.clawSwipeDamage));
    }

    @Override
    public void usePreBattleAction() {
        this.addToBot(new GainBlockAction(this, this.startingBlock));
        this.addToBot(new ApplyPowerAction(this, this, new BarricadePower(this)));
    }

    @Override
    public void takeTurn() {
        if (this.firstMove) {
            this.firstMove = false;
        }
        switch (this.nextMove) {
            case BIDE_MOVE:
                AbstractDungeon.actionManager.addToBottom(new FastShakeAction(this, 0.5F, 0.2F));
                break;
            case EVOLVE_MOVE:
                this.addToBot(new ApplyPowerAction(this, this, new EvolvingPower(this)));
                break;
            case CLAW_SWIPE_ATTACK:
                AbstractDungeon.actionManager.addToBottom(new AnimateSlowAttackAction(this));
                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage.get(0), AbstractGameAction.AttackEffect.SLASH_HEAVY));
                break;
        }
        AbstractDungeon.actionManager.addToBottom(new RollMoveAction(this));
    }

    @Override
    protected void getMove(final int num) {
        if (this.firstMove && AbstractDungeon.ascensionLevel < 19) {
            this.setMove(MOVES[0], BIDE_MOVE, Intent.UNKNOWN);
        }
        else if (this.firstMove || this.lastMove(BIDE_MOVE)) {
            this.setMove(MOVES[1], EVOLVE_MOVE, Intent.MAGIC);
        }
        else {
            this.setMove(MOVES[2], CLAW_SWIPE_ATTACK, Intent.ATTACK, this.clawSwipeDamage);
        }
    }
}