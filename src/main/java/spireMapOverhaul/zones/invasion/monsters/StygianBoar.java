package spireMapOverhaul.zones.invasion.monsters;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.AnimateFastAttackAction;
import com.megacrit.cardcrawl.actions.animations.AnimateSlowAttackAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.RollMoveAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.powers.AngerPower;
import spireMapOverhaul.SpireAnniversary6Mod;
import spireMapOverhaul.abstracts.AbstractSMOMonster;

public class StygianBoar extends AbstractSMOMonster {
    public static final String ID = SpireAnniversary6Mod.makeID("StygianBoar");
    private static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID);
    public static final String NAME = monsterStrings.NAME;
    public static final String[] MOVES = monsterStrings.MOVES;
    private static final String IMG = SpireAnniversary6Mod.makeImagePath("monsters/Invasion/StygianBoar/StygianBoar.png");
    private boolean firstMove = true;
    private static final byte TUSK_SLASH_ATTACK = 1;
    private static final byte BREATHE_FIRE_ATTACK = 2;
    private static final byte ENRAGING_CHARGE_ATTACK = 3;
    private static final int TUSK_SLASH_DAMAGE = 1;
    private static final int A2_TUSK_SLASH_DAMAGE = 2;
    private static final int TUSK_SLASH_HITS = 2;
    private static final int BREATHE_FIRE_DAMAGE = 7;
    private static final int A2_BREATHE_FIRE_DAMAGE = 8;
    private static final int ENRAGING_CHARGE_DAMAGE = 4;
    private static final int A2_ENRAGING_CHARGE_DAMAGE = 5;
    private static final int HP_MIN = 41;
    private static final int HP_MAX = 44;
    private static final int A7_HP_MIN = 43;
    private static final int A7_HP_MAX = 46;
    private int tuskSlashDamage;
    private int breathFireDamage;
    private int enragingChargeDamage;

    public StygianBoar() {
        this(0.0f, 0.0f);
    }

    public StygianBoar(final float x, final float y) {
        super(StygianBoar.NAME, ID, HP_MAX, -5.0F, 0, 215.0f, 135.0f, IMG, x, y);
        this.type = EnemyType.NORMAL;
        if (AbstractDungeon.ascensionLevel >= 7) {
            this.setHp(A7_HP_MIN, A7_HP_MAX);
        } else {
            this.setHp(HP_MIN, HP_MAX);
        }

        if (AbstractDungeon.ascensionLevel >= 2) {
            this.tuskSlashDamage = A2_TUSK_SLASH_DAMAGE;
            this.breathFireDamage = A2_BREATHE_FIRE_DAMAGE;
            this.enragingChargeDamage = A2_ENRAGING_CHARGE_DAMAGE;
        } else {
            this.tuskSlashDamage = TUSK_SLASH_DAMAGE;
            this.breathFireDamage = BREATHE_FIRE_DAMAGE;
            this.enragingChargeDamage = ENRAGING_CHARGE_DAMAGE;
        }
        this.damage.add(new DamageInfo(this, this.tuskSlashDamage));
        this.damage.add(new DamageInfo(this, this.breathFireDamage));
        this.damage.add(new DamageInfo(this, this.enragingChargeDamage));
    }

    @Override
    public void usePreBattleAction() {
        if (AbstractDungeon.ascensionLevel >= 17) {
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new AngerPower(this, 1), 1));
        }
    }

    @Override
    public void takeTurn() {
        if (this.firstMove) {
            this.firstMove = false;
        }
        switch (this.nextMove) {
            case TUSK_SLASH_ATTACK:
                AbstractDungeon.actionManager.addToBottom(new AnimateFastAttackAction(this));
                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage.get(0), AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
                AbstractDungeon.actionManager.addToBottom(new AnimateFastAttackAction(this));
                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage.get(0), AbstractGameAction.AttackEffect.SLASH_VERTICAL));
                break;
            case BREATHE_FIRE_ATTACK:
                AbstractDungeon.actionManager.addToBottom(new AnimateSlowAttackAction(this));
                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage.get(1), AbstractGameAction.AttackEffect.FIRE));
                break;
            case ENRAGING_CHARGE_ATTACK:
                AbstractDungeon.actionManager.addToBottom(new AnimateSlowAttackAction(this));
                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage.get(0), AbstractGameAction.AttackEffect.SLASH_HEAVY));
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new AngerPower(this, 1), 1));
                break;
        }
        AbstractDungeon.actionManager.addToBottom(new RollMoveAction(this));
    }

    @Override
    protected void getMove(final int num) {
        if (this.firstMove && AbstractDungeon.ascensionLevel < 17) {
            this.setMove(MOVES[2], ENRAGING_CHARGE_ATTACK, Intent.ATTACK_BUFF, this.enragingChargeDamage);
        }
        else if (this.firstMove || this.lastMove(BREATHE_FIRE_ATTACK)) {
            this.setMove(MOVES[0], TUSK_SLASH_ATTACK, Intent.ATTACK, this.tuskSlashDamage, TUSK_SLASH_HITS, true);
        }
        else {
            this.setMove(MOVES[1], BREATHE_FIRE_ATTACK, Intent.ATTACK, this.breathFireDamage);
        }
    }
}