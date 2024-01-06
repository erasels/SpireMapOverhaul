package spireMapOverhaul.zones.invasion.monsters;

import basemod.abstracts.CustomMonster;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.AnimateFastAttackAction;
import com.megacrit.cardcrawl.actions.animations.FastShakeAction;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.status.Dazed;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.powers.DrawCardNextTurnPower;
import com.megacrit.cardcrawl.powers.FrailPower;
import spireMapOverhaul.SpireAnniversary6Mod;
import spireMapOverhaul.zones.invasion.powers.VoidPressurePower;

public class VoidBeast extends CustomMonster {
    public static final String ID = SpireAnniversary6Mod.makeID("VoidBeast");
    private static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID);
    public static final String NAME = monsterStrings.NAME;
    public static final String[] MOVES = monsterStrings.MOVES;
    private static final String IMG = SpireAnniversary6Mod.makeImagePath("monsters/Invasion/VoidBeast/VoidBeast.png");
    private boolean firstMove = true;
    private boolean usedVoidGaze = false;
    private static final byte CORRUPTING_BITE_ATTACK = 1;
    private static final byte VOID_GAZE_DEBUFF = 2;
    private static final byte SHADOW_CLAW_ATTACK = 3;
    private static final int CORRUPTING_BITE_DAMAGE = 8;
    private static final int A2_CORRUPTING_BITE_DAMAGE = 10;
    private static final int CORRUPTING_BITE_DAZES = 2;
    private static final int VOID_GAZE_FRAIL = 2;
    private static final int A17_VOID_GAZE_FRAIL = 3;
    private static final int VOID_GAZE_DAZES = 2;
    private static final int A17_VOID_GAZE_DAZES = 3;
    private static final int SHADOW_CLAW_DAMAGE = 6;
    private static final int A2_SHADOW_CLAW_DAMAGE = 7;
    private static final int SHADOW_CLAW_HITS = 2;
    private static final int VOID_PRESSURE_AMOUNT = 2;
    private static final int A17_VOID_PRESSURE_AMOUNT = 3;
    private static final int HP_MIN = 110;
    private static final int HP_MAX = 116;
    private static final int A7_HP_MIN = 116;
    private static final int A7_HP_MAX = 122;
    private int corruptingBiteDamage;
    private int voidGazeFrail;
    private int voidGazeDazes;
    private int shadowClawDamage;
    private int voidPressureAmount;

    public VoidBeast() {
        this(0.0f, 0.0f);
    }

    public VoidBeast(final float x, final float y) {
        super(VoidBeast.NAME, ID, HP_MAX, -5.0F, 0, 400.0f, 250.0f, IMG, x, y);
        this.type = EnemyType.NORMAL;
        if (AbstractDungeon.ascensionLevel >= 7) {
            this.setHp(A7_HP_MIN, A7_HP_MAX);
        } else {
            this.setHp(HP_MIN, HP_MAX);
        }

        if (AbstractDungeon.ascensionLevel >= 2) {
            this.corruptingBiteDamage = A2_CORRUPTING_BITE_DAMAGE;
            this.shadowClawDamage = A2_SHADOW_CLAW_DAMAGE;
        } else {
            this.corruptingBiteDamage = CORRUPTING_BITE_DAMAGE;
            this.shadowClawDamage = SHADOW_CLAW_DAMAGE;
        }

        if (AbstractDungeon.ascensionLevel >= 17) {
            this.voidGazeFrail = A17_VOID_GAZE_FRAIL;
            this.voidGazeDazes = A17_VOID_GAZE_DAZES;
            this.voidPressureAmount = A17_VOID_PRESSURE_AMOUNT;
        }
        else {
            this.voidGazeFrail = VOID_GAZE_FRAIL;
            this.voidGazeDazes = VOID_GAZE_DAZES;
            this.voidPressureAmount = VOID_PRESSURE_AMOUNT;
        }

        this.damage.add(new DamageInfo(this, this.corruptingBiteDamage));
        this.damage.add(new DamageInfo(this, this.shadowClawDamage));
    }

    @Override
    public void usePreBattleAction() {
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, this, new VoidPressurePower(AbstractDungeon.player, this, this.voidPressureAmount), this.voidPressureAmount));
    }

    @Override
    public void takeTurn() {
        if (this.firstMove) {
            this.firstMove = false;
        }
        switch (this.nextMove) {
            case CORRUPTING_BITE_ATTACK:
                AbstractDungeon.actionManager.addToBottom(new AnimateFastAttackAction(this));
                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage.get(0), AbstractGameAction.AttackEffect.SLASH_HEAVY));
                AbstractDungeon.actionManager.addToBottom(new MakeTempCardInDrawPileAction(new Dazed(), CORRUPTING_BITE_DAZES, false, true));
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, this, new DrawCardNextTurnPower(AbstractDungeon.player, CORRUPTING_BITE_DAZES)));
                break;
            case VOID_GAZE_DEBUFF:
                this.usedVoidGaze = true;
                AbstractDungeon.actionManager.addToBottom(new FastShakeAction(this, 0.5F, 0.2F));
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, this, new FrailPower(AbstractDungeon.player, this.voidGazeFrail, true), this.voidGazeFrail));
                AbstractDungeon.actionManager.addToBottom(new MakeTempCardInDiscardAction(new Dazed(), this.voidGazeDazes));
                break;
            case SHADOW_CLAW_ATTACK:
                AbstractDungeon.actionManager.addToBottom(new AnimateFastAttackAction(this));
                for (int i = 0; i < SHADOW_CLAW_HITS; i++) {
                    AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage.get(1), AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
                }
                break;
        }
        AbstractDungeon.actionManager.addToBottom(new RollMoveAction(this));
    }

    @Override
    protected void getMove(final int num) {
        if (!this.firstMove && !this.usedVoidGaze) {
            this.setMove(MOVES[1], VOID_GAZE_DEBUFF, Intent.STRONG_DEBUFF);
        }
        else if (!this.firstMove && (
                    this.lastMove(VOID_GAZE_DEBUFF)
                    || this.lastTwoMoves(CORRUPTING_BITE_ATTACK)
                    || (!this.lastTwoMoves(SHADOW_CLAW_ATTACK) && num < 40))) {
            this.setMove(MOVES[2], SHADOW_CLAW_ATTACK, Intent.ATTACK, this.shadowClawDamage, SHADOW_CLAW_HITS, true);
        }
        else {
            this.setMove(MOVES[0], CORRUPTING_BITE_ATTACK, Intent.ATTACK_DEBUFF, this.corruptingBiteDamage);
        }
    }
}