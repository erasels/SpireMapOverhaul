package spireMapOverhaul.zones.hailstorm.monsters;

import basemod.abstracts.CustomMonster;
import com.badlogic.gdx.math.MathUtils;
import com.esotericsoftware.spine.AnimationState;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.AnimateShakeAction;
import com.megacrit.cardcrawl.actions.animations.AnimateSlowAttackAction;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.actions.unique.CanLoseAction;
import com.megacrit.cardcrawl.actions.unique.CannotLoseAction;
import com.megacrit.cardcrawl.actions.utility.HideHealthBarAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.actions.utility.TextAboveCreatureAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.status.Slimed;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.SlimeAnimListener;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.exordium.AcidSlime_M;
import com.megacrit.cardcrawl.powers.BarricadePower;
import com.megacrit.cardcrawl.powers.NoBlockPower;
import com.megacrit.cardcrawl.powers.PoisonPower;
import com.megacrit.cardcrawl.powers.SplitPower;
import spireMapOverhaul.SpireAnniversary6Mod;

public class FrostSlimeM extends CustomMonster {
    public static final String ID = SpireAnniversary6Mod.makeID(FrostSlimeM.class.getSimpleName());
    private static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID);
    public static final String NAME = monsterStrings.NAME;
    public static final String[] MOVES = monsterStrings.MOVES;
    private static final String IMG = SpireAnniversary6Mod.makeImagePath("monsters/Hailstorm/FrostSlimeM.png");

    private static final String WOUND_NAME;
    private static final String SPLIT_NAME;
    private static final String BP_NAME;
    private boolean firstMove = true;
    public static final int HP_MIN = 28;
    public static final int HP_MAX = 32;
    public static final int A_7_HP_MIN = 29;
    public static final int A_7_HP_MAX = 34;
    public static final int W_TACKLE_DMG = 7;
    public static final int N_TACKLE_DMG = 10;
    public static final int A_2_W_TACKLE_DMG = 8;
    public static final int A_2_N_TACKLE_DMG = 12;
    public static final int BP_TURNS = 1;
    public static final int WOUND_COUNT = 1;
    private static final byte SLIME_TACKLE = 1;
    private static final byte NORMAL_TACKLE = 2;
    private static final byte SPLIT = 3;
    private static final byte BP_LICK = 4;
    private float saveX;
    private float saveY;
    private boolean splitTriggered;
    private int slimeTackleDamage;
    private int slimeTackleSlimed;
    private int lickBlockPreventionTurns;
    private int normalTackleDamage;

    public FrostSlimeM() {
        this(0.0f, 0.0f);
    }

    public FrostSlimeM(final float x, final float y) {
        super(NAME, ID, HP_MAX, -5.0F, -4.0F, 90.0F, 80.0F, IMG, x, y);
        this.type = EnemyType.NORMAL;
        if (AbstractDungeon.ascensionLevel >= 7) {
            this.setHp(A_7_HP_MIN, A_7_HP_MAX);
        } else {
            this.setHp(HP_MIN, HP_MAX);
        }

        if (AbstractDungeon.ascensionLevel >= 2) {
            this.slimeTackleDamage = A_2_W_TACKLE_DMG;
            this.normalTackleDamage = A_2_N_TACKLE_DMG;
        } else {
            this.slimeTackleDamage = W_TACKLE_DMG;
            this.normalTackleDamage = N_TACKLE_DMG;
        }
        this.damage.add(new DamageInfo(this, this.slimeTackleDamage));
        this.damage.add(new DamageInfo(this, this.normalTackleDamage));

        this.slimeTackleSlimed = WOUND_COUNT;
        this.lickBlockPreventionTurns = BP_TURNS;

        this.powers.add(new BarricadePower(this));
    }

    public FrostSlimeM(float x, float y, int newHealth) {
        super(NAME, ID, newHealth, 0.0F, 0.0F, 170.0F, 130.0F, (String)null, x, y, true);
        if (AbstractDungeon.ascensionLevel >= 2) {
            this.damage.add(new DamageInfo(this, A_2_W_TACKLE_DMG));
            this.damage.add(new DamageInfo(this, A_2_N_TACKLE_DMG));
        } else {
            this.damage.add(new DamageInfo(this, W_TACKLE_DMG));
            this.damage.add(new DamageInfo(this, N_TACKLE_DMG));
        }

        this.slimeTackleSlimed = WOUND_COUNT;
        this.lickBlockPreventionTurns = BP_TURNS;

        this.powers.add(new BarricadePower(this));

        this.img = ImageMaster.loadImage(IMG);

    }

    @Override
    public void takeTurn() {
        switch (this.nextMove) {
            case SLIME_TACKLE:
                AbstractDungeon.actionManager.addToBottom(new AnimateSlowAttackAction(this));
                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, (DamageInfo)this.damage.get(0), AbstractGameAction.AttackEffect.BLUNT_HEAVY));
                AbstractDungeon.actionManager.addToBottom(new MakeTempCardInDiscardAction(new Slimed(), this.slimeTackleSlimed));
                AbstractDungeon.actionManager.addToBottom(new RollMoveAction(this));
                break;
            case NORMAL_TACKLE:
                AbstractDungeon.actionManager.addToBottom(new AnimateSlowAttackAction(this));
                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, (DamageInfo)this.damage.get(1), AbstractGameAction.AttackEffect.BLUNT_HEAVY));
                AbstractDungeon.actionManager.addToBottom(new RollMoveAction(this));
                break;
            case SPLIT:
            default:
                break;
            case BP_LICK:
                AbstractDungeon.actionManager.addToBottom(new AnimateSlowAttackAction(this));
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, this, new NoBlockPower(AbstractDungeon.player, this.lickBlockPreventionTurns, true), this.lickBlockPreventionTurns));
                AbstractDungeon.actionManager.addToBottom(new RollMoveAction(this));
        }

    }

    //Dirty Cc/Cv from base game, i didn't read through it :X
    protected void getMove(int num) {
        if (AbstractDungeon.ascensionLevel >= 17) {
            if (num < 40) {
                if (this.lastTwoMoves((byte)1)) {
                    if (AbstractDungeon.aiRng.randomBoolean(0.6F)) {
                        this.setMove((byte)2, Intent.ATTACK, ((DamageInfo)this.damage.get(1)).base);
                    } else {
                        this.setMove(BP_NAME, (byte)4, Intent.DEBUFF);
                    }
                } else {
                    this.setMove(WOUND_NAME, (byte)1, Intent.ATTACK_DEBUFF, ((DamageInfo)this.damage.get(0)).base);
                }
            } else if (num < 70) {
                if (this.lastTwoMoves((byte)2)) {
                    if (AbstractDungeon.aiRng.randomBoolean(0.6F)) {
                        this.setMove(WOUND_NAME, (byte)1, Intent.ATTACK_DEBUFF, ((DamageInfo)this.damage.get(0)).base);
                    } else {
                        this.setMove(BP_NAME, (byte)4, Intent.DEBUFF);
                    }
                } else {
                    this.setMove((byte)2, Intent.ATTACK, ((DamageInfo)this.damage.get(1)).base);
                }
            } else if (this.lastMove((byte)4)) {
                if (AbstractDungeon.aiRng.randomBoolean(0.4F)) {
                    this.setMove(WOUND_NAME, (byte)1, Intent.ATTACK_DEBUFF, ((DamageInfo)this.damage.get(0)).base);
                } else {
                    this.setMove((byte)2, Intent.ATTACK, ((DamageInfo)this.damage.get(1)).base);
                }
            } else {
                this.setMove(BP_NAME, (byte)4, Intent.DEBUFF);
            }
        } else if (num < 30) {
            if (this.lastTwoMoves((byte)1)) {
                if (AbstractDungeon.aiRng.randomBoolean()) {
                    this.setMove((byte)2, Intent.ATTACK, ((DamageInfo)this.damage.get(1)).base);
                } else {
                    this.setMove(BP_NAME, (byte)4, Intent.DEBUFF);
                }
            } else {
                this.setMove(WOUND_NAME, (byte)1, Intent.ATTACK_DEBUFF, ((DamageInfo)this.damage.get(0)).base);
            }
        } else if (num < 70) {
            if (this.lastMove((byte)2)) {
                if (AbstractDungeon.aiRng.randomBoolean(0.4F)) {
                    this.setMove(WOUND_NAME, (byte)1, Intent.ATTACK_DEBUFF, ((DamageInfo)this.damage.get(0)).base);
                } else {
                    this.setMove(BP_NAME, (byte)4, Intent.DEBUFF);
                }
            } else {
                this.setMove((byte)2, Intent.ATTACK, ((DamageInfo)this.damage.get(1)).base);
            }
        } else if (this.lastTwoMoves((byte)4)) {
            if (AbstractDungeon.aiRng.randomBoolean(0.4F)) {
                this.setMove(WOUND_NAME, (byte)1, Intent.ATTACK_DEBUFF, ((DamageInfo)this.damage.get(0)).base);
            } else {
                this.setMove((byte)2, Intent.ATTACK, ((DamageInfo)this.damage.get(1)).base);
            }
        } else {
            this.setMove(BP_NAME, (byte)4, Intent.DEBUFF);
        }

    }

    static {
        WOUND_NAME = MOVES[0];
        SPLIT_NAME = MOVES[1];
        BP_NAME = MOVES[2];
    }

}
