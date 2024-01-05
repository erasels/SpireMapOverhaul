package spireMapOverhaul.zones.invasion.monsters;

import basemod.abstracts.CustomMonster;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.AnimateFastAttackAction;
import com.megacrit.cardcrawl.actions.animations.FastShakeAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.status.Burn;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.powers.FrailPower;
import com.megacrit.cardcrawl.powers.WeakPower;
import com.megacrit.cardcrawl.vfx.combat.ExplosionSmallEffect;
import spireMapOverhaul.SpireAnniversary6Mod;

public class RoyalProtector extends CustomMonster {
    public static final String ID = SpireAnniversary6Mod.makeID("RoyalProtector");
    private static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID);
    public static final String NAME = monsterStrings.NAME;
    public static final String[] MOVES = monsterStrings.MOVES;
    private static final String IMG = SpireAnniversary6Mod.makeImagePath("monsters/Invasion/RoyalProtector/RoyalProtector.png");
    private boolean firstMove = true;
    private static final byte CARAPACE_MOVE = 1;
    private static final byte BURNING_SPIT_ATTACK = 2;
    private static final byte ACID_EXPLOSION_ATTACK = 3;
    private static final byte FOR_THE_QUEEN_ATTACK = 4;
    private static final int CARAPACE_BLOCK = 2;
    private static final int A18_CARAPACE_BLOCK = 5;
    private static final int BURNING_SPIT_DAMAGE = 7;
    private static final int A3_BURNING_SPIT_DAMAGE = 8;
    private static final int BURNING_SPIT_BURNS = 1;
    private static final int ACID_EXPLOSION_DAMAGE = 22;
    private static final int A3_ACID_EXPLOSION_DAMAGE = 25;
    private static final int FOR_THE_QUEEN_DAMAGE = 2;
    private static final int A3_FOR_THE_QUEEN_DAMAGE = 3;
    private static final int FOR_THE_QUEEN_AMOUNT = 1;
    private static final int A18_FOR_THE_QUEEN_AMOUNT = 2;
    private static final int HP_MIN = 31;
    private static final int HP_MAX = 35;
    private static final int A8_HP_MIN = 33;
    private static final int A8_HP_MAX = 37;
    private int carapaceBlock;
    private int burningSpitDamage;
    private int acidExplosionDamage;
    private int forTheQueenDamage;
    private int forTheQueenAmount;

    public RoyalProtector() {
        this(0.0f, 0.0f);
    }

    public RoyalProtector(final float x, final float y) {
        super(RoyalProtector.NAME, ID, HP_MAX, -5.0F, 0, 135.0f, 135.0f, IMG, x, y);
        this.type = EnemyType.NORMAL;
        if (AbstractDungeon.ascensionLevel >= 8) {
            this.setHp(A8_HP_MIN, A8_HP_MAX);
        } else {
            this.setHp(HP_MIN, HP_MAX);
        }

        if (AbstractDungeon.ascensionLevel >= 3) {
            this.burningSpitDamage = A3_BURNING_SPIT_DAMAGE;
            this.acidExplosionDamage = A3_ACID_EXPLOSION_DAMAGE;
            this.forTheQueenDamage = A3_FOR_THE_QUEEN_DAMAGE;
        } else {
            this.burningSpitDamage = BURNING_SPIT_DAMAGE;
            this.acidExplosionDamage = ACID_EXPLOSION_DAMAGE;
            this.forTheQueenDamage = FOR_THE_QUEEN_DAMAGE;
        }
        this.damage.add(new DamageInfo(this, this.burningSpitDamage));
        this.damage.add(new DamageInfo(this, this.acidExplosionDamage));
        this.damage.add(new DamageInfo(this, this.forTheQueenDamage));

        if (AbstractDungeon.ascensionLevel >= 18) {
            this.carapaceBlock = A18_CARAPACE_BLOCK;
            this.forTheQueenAmount = A18_FOR_THE_QUEEN_AMOUNT;
        } else {
            this.carapaceBlock = CARAPACE_BLOCK;
            this.forTheQueenAmount = FOR_THE_QUEEN_AMOUNT;
        }
    }

    @Override
    public void takeTurn() {
        if (this.firstMove) {
            this.firstMove = false;
        }
        switch (this.nextMove) {
            case CARAPACE_MOVE:
                AbstractDungeon.actionManager.addToBottom(new GainBlockAction(this, this.carapaceBlock));
                break;
            case BURNING_SPIT_ATTACK:
                //TODO animation -- adapt BloodShotEffect
                AbstractDungeon.actionManager.addToBottom(new AnimateFastAttackAction(this));
                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage.get(0), AbstractGameAction.AttackEffect.FIRE));
                if (AbstractDungeon.ascensionLevel >= 18) {
                    AbstractDungeon.actionManager.addToBottom(new MakeTempCardInDrawPileAction(new Burn(), BURNING_SPIT_BURNS, true, true));
                }
                else {
                    AbstractDungeon.actionManager.addToBottom(new MakeTempCardInDiscardAction(new Burn(), BURNING_SPIT_BURNS));
                }
                break;
            case ACID_EXPLOSION_ATTACK:
                //TODO animation -- maybe Fireball effect, just making it green?
                AbstractDungeon.actionManager.addToBottom(new AnimateFastAttackAction(this));
                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage.get(1), AbstractGameAction.AttackEffect.POISON));
                break;
            case FOR_THE_QUEEN_ATTACK:
                //TODO animation -- a green explosion (make ExplosionSmallColorEffect)
                AbstractDungeon.actionManager.addToBottom(new FastShakeAction(this, 0.5F, 0.2F));
                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage.get(2), AbstractGameAction.AttackEffect.BLUNT_LIGHT));
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, this, new WeakPower(AbstractDungeon.player, this.forTheQueenAmount, true), this.forTheQueenAmount));
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, this, new FrailPower(AbstractDungeon.player, this.forTheQueenAmount, true), this.forTheQueenAmount));
                AbstractDungeon.actionManager.addToBottom(new RemoveAllBlockAction(this, this));
                AbstractDungeon.actionManager.addToBottom(new VFXAction(new ExplosionSmallEffect(this.hb.cX, this.hb.cY), 0.1F));
                AbstractDungeon.actionManager.addToBottom(new SuicideAction(this));
                break;
        }
        AbstractDungeon.actionManager.addToBottom(new RollMoveAction(this));
    }

    @Override
    protected void getMove(final int num) {
        if (this.firstMove) {
            this.setMove(MOVES[0], CARAPACE_MOVE, Intent.DEFEND);
        }
        else if (this.lastMove(CARAPACE_MOVE)) {
            this.setMove(MOVES[1], BURNING_SPIT_ATTACK, Intent.ATTACK_DEBUFF, this.burningSpitDamage);
        }
        else if (this.lastMove(BURNING_SPIT_ATTACK)) {
            this.setMove(MOVES[2], ACID_EXPLOSION_ATTACK, Intent.ATTACK, this.acidExplosionDamage);
        }
        else {
            this.setMove(MOVES[3], FOR_THE_QUEEN_ATTACK, Intent.ATTACK_DEBUFF, this.forTheQueenDamage);
        }
    }
}