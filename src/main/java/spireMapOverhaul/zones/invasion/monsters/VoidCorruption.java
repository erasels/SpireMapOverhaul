package spireMapOverhaul.zones.invasion.monsters;

import basemod.abstracts.CustomMonster;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.AnimateFastAttackAction;
import com.megacrit.cardcrawl.actions.animations.AnimateSlowAttackAction;
import com.megacrit.cardcrawl.actions.animations.FastShakeAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDiscardAction;
import com.megacrit.cardcrawl.actions.common.RollMoveAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.status.VoidCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.powers.ArtifactPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.powers.WeakPower;
import spireMapOverhaul.SpireAnniversary6Mod;
import spireMapOverhaul.zones.invasion.powers.EnergyLimitPower;
import spireMapOverhaul.zones.invasion.powers.FixedTextDrawPower;

public class VoidCorruption extends CustomMonster {
    public static final String ID = SpireAnniversary6Mod.makeID("VoidCorruption");
    private static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID);
    public static final String NAME = monsterStrings.NAME;
    public static final String[] MOVES = monsterStrings.MOVES;
    private static final String IMG = SpireAnniversary6Mod.makeImagePath("monsters/Invasion/VoidCorruption/VoidCorruption.png");
    private boolean firstMove = true;
    private static final byte ENERGY_DRAIN_DEBUFF = 1;
    private static final byte VOIDS_ATTACK = 2;
    private static final byte WEAK_DEBUFF = 3;
    private static final byte BIG_ATTACK = 4;
    private static final byte MULTI_ATTACK = 5;
    private static final int WEAK_DEBUFF_AMOUNT = 2;
    private static final int VOIDS_ATTACK_DAMAGE = 13;
    private static final int A2_VOIDS_ATTACK_DAMAGE = 15;
    private static final int BIG_ATTACK_DAMAGE = 19;
    private static final int A2_BIG_ATTACK_DAMAGE = 22;
    private static final int MULTI_ATTACK_DAMAGE = 4;
    private static final int MULTI_ATTACK_COUNT = 2;
    private static final int A2_MULTI_ATTACK_COUNT = 3;
    private static final int HP_MIN = 97;
    private static final int HP_MAX = 101;
    private static final int A7_HP_MIN = 99;
    private static final int A7_HP_MAX = 103;
    private int voidsAttackDamage;
    private int bigAttackDamage;
    private int multiAttackCount;
    private boolean usedEnergyDrain = false;

    public VoidCorruption() {
        this(0.0f, 0.0f);
    }

    public VoidCorruption(final float x, final float y) {
        super(VoidCorruption.NAME, ID, HP_MAX, -5.0F, 0, 285.0f, 250.0f, IMG, x, y);
        this.type = EnemyType.NORMAL;
        if (AbstractDungeon.ascensionLevel >= 7) {
            this.setHp(A7_HP_MIN, A7_HP_MAX);
        } else {
            this.setHp(HP_MIN, HP_MAX);
        }

        if (AbstractDungeon.ascensionLevel >= 2) {
            this.voidsAttackDamage = A2_VOIDS_ATTACK_DAMAGE;
            this.bigAttackDamage = A2_BIG_ATTACK_DAMAGE;
            this.multiAttackCount = A2_MULTI_ATTACK_COUNT;
        } else {
            this.voidsAttackDamage = VOIDS_ATTACK_DAMAGE;
            this.bigAttackDamage = BIG_ATTACK_DAMAGE;
            this.multiAttackCount = MULTI_ATTACK_COUNT;
        }
        this.damage.add(new DamageInfo(this, this.voidsAttackDamage));
        this.damage.add(new DamageInfo(this, this.bigAttackDamage));
        this.damage.add(new DamageInfo(this, MULTI_ATTACK_DAMAGE));
    }
    
    @Override
    public void takeTurn() {
        if (this.firstMove) {
            this.firstMove = false;
        }
        switch (this.nextMove) {
            case ENERGY_DRAIN_DEBUFF:
                this.usedEnergyDrain = true;
                AbstractDungeon.actionManager.addToBottom(new FastShakeAction(this, 0.5F, 0.2F));
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, this, new EnergyLimitPower(AbstractDungeon.player)));
                if (!AbstractDungeon.player.hasPower(ArtifactPower.POWER_ID)) {
                    AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, this, new FixedTextDrawPower(AbstractDungeon.player, -1)));
                }
                break;
            case VOIDS_ATTACK:
                AbstractDungeon.actionManager.addToBottom(new AnimateSlowAttackAction(this));
                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage.get(0), AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
                AbstractDungeon.actionManager.addToBottom(new MakeTempCardInDiscardAction(new VoidCard(), 1));
                break;
            case WEAK_DEBUFF:
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, this, new WeakPower(AbstractDungeon.player, WEAK_DEBUFF_AMOUNT, true), WEAK_DEBUFF_AMOUNT));
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new StrengthPower(this, WEAK_DEBUFF_AMOUNT), WEAK_DEBUFF_AMOUNT));
                break;
            case BIG_ATTACK:
                AbstractDungeon.actionManager.addToBottom(new AnimateSlowAttackAction(this));
                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage.get(1), AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
                break;
            case MULTI_ATTACK:
                AbstractDungeon.actionManager.addToBottom(new AnimateFastAttackAction(this));
                for (int i = 0; i<this.multiAttackCount; i++){
                    AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage.get(2), AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
                }
                break;
        }
        AbstractDungeon.actionManager.addToBottom(new RollMoveAction(this));
    }

    @Override
    protected void getMove(final int num) {
        if (!this.usedEnergyDrain && (!this.firstMove || AbstractDungeon.ascensionLevel >= 17)){
            this.setMove(MOVES[0], ENERGY_DRAIN_DEBUFF, Intent.STRONG_DEBUFF);
        }
        else if (this.firstMove || this.lastMove(BIG_ATTACK) || this.lastMove(MULTI_ATTACK)) {
            if (num < 50) {
                this.setMove(MOVES[1], VOIDS_ATTACK, Intent.ATTACK_DEBUFF, this.voidsAttackDamage);
            }
            else {
                this.setMove(MOVES[2], WEAK_DEBUFF, Intent.DEBUFF);
            }
        }
        else {
            if (num < 50) {
                this.setMove(MOVES[3], BIG_ATTACK, Intent.ATTACK, this.bigAttackDamage);
            }
            else {
                this.setMove(MOVES[4], MULTI_ATTACK, Intent.ATTACK, MULTI_ATTACK_DAMAGE, this.multiAttackCount, true);
            }
        }
    }
}