package spireMapOverhaul.zones.invasion.monsters;

import basemod.abstracts.CustomMonster;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.AnimateSlowAttackAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.RollMoveAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.powers.*;
import spireMapOverhaul.SpireAnniversary6Mod;

public class WarGolem extends CustomMonster {
    public static final String ID = SpireAnniversary6Mod.makeID("WarGolem");
    private static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID);
    public static final String NAME = monsterStrings.NAME;
    public static final String[] MOVES = monsterStrings.MOVES;
    private static final String IMG = SpireAnniversary6Mod.makeImagePath("monsters/Invasion/WarGolem/WarGolem.png");
    private boolean firstMove = true;
    private static final byte VULNERABLE_ATTACK = 1;
    private static final byte FRAIL_ATTACK = 2;
    private static final byte DEX_ATTACK = 3;
    private static final int VULNERABLE_ATTACK_DAMAGE = 18;
    private static final int A3_VULNERABLE_ATTACK_DAMAGE = 20;
    private static final int VULNERABLE_AMOUNT = 1;
	private static final int A18_VULNERABLE_AMOUNT = 2;
    private static final int FRAIL_ATTACK_DAMAGE = 15;
    private static final int A3_FRAIL_ATTACK_DAMAGE = 17;
    private static final int FRAIL_AMOUNT = 1;
	private static final int A18_FRAIL_AMOUNT = 2;
    private static final int DEX_ATTACK_DAMAGE = 13;
    private static final int A3_DEX_ATTACK_DAMAGE = 15;
    private static final int DEX_AMOUNT = 1;
    private static final int A18_DEX_AMOUNT = 2;
    private static final int FOCUS_AMOUNT = 0;
    private static final int A18_FOCUS_AMOUNT = 1;
    private static final int METALLICIZE = 6;
    private static final int A8_METALLICIZE = 9;
    private static final int HP_MIN = 110;
    private static final int HP_MAX = 112;
    private static final int A8_HP_MIN = 114;
    private static final int A8_HP_MAX = 116;
    private int vulnerableDamage;
    private int frailDamage;
    private int dexDamage;
	private int vulnerableAmount;
	private int frailAmount;
    private int dexAmount;
    private int focusAmount;
    private int metallicize;
    private boolean skipStartingBlock;

    public WarGolem() {
        this(0.0f, 0.0f, false);
    }

    public WarGolem(final float x, final float y, boolean skipStartingBlock) {
        super(WarGolem.NAME, ID, HP_MAX, -5.0F, 0, 230.0f, 300.0f, IMG, x, y);
        this.type = EnemyType.ELITE;
        this.skipStartingBlock = skipStartingBlock;
        if (AbstractDungeon.ascensionLevel >= 8) {
            this.setHp(A8_HP_MIN, A8_HP_MAX);
            this.metallicize = A8_METALLICIZE;
        } else {
            this.setHp(HP_MIN, HP_MAX);
            this.metallicize = METALLICIZE;
        }

        if (AbstractDungeon.ascensionLevel >= 3) {
            this.vulnerableDamage = A3_VULNERABLE_ATTACK_DAMAGE;
            this.frailDamage = A3_FRAIL_ATTACK_DAMAGE;
			this.dexDamage = A3_DEX_ATTACK_DAMAGE;
        } else {
            this.vulnerableDamage = VULNERABLE_ATTACK_DAMAGE;
            this.frailDamage = FRAIL_ATTACK_DAMAGE;
			this.dexDamage = DEX_ATTACK_DAMAGE;
        }
        this.damage.add(new DamageInfo(this, this.vulnerableDamage));
        this.damage.add(new DamageInfo(this, this.frailDamage));
        this.damage.add(new DamageInfo(this, this.dexDamage));
		
        if (AbstractDungeon.ascensionLevel >= 18) {
            this.vulnerableAmount = A18_VULNERABLE_AMOUNT;
            this.frailAmount = A18_FRAIL_AMOUNT;
            this.dexAmount = A18_DEX_AMOUNT;
            this.focusAmount = A18_FOCUS_AMOUNT;
        } else {
            this.vulnerableAmount = VULNERABLE_AMOUNT;
            this.frailAmount = FRAIL_AMOUNT;
            this.dexAmount = DEX_AMOUNT;
            this.focusAmount = FOCUS_AMOUNT;
        }
    }

    @Override
    public void usePreBattleAction() {
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new MetallicizePower(this, this.metallicize), this.metallicize));
        if (!this.skipStartingBlock) {
            AbstractDungeon.actionManager.addToBottom(new GainBlockAction(this, this, this.metallicize));
        }
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new ArtifactPower(this, 1), 1));
    }
    
    @Override
    public void takeTurn() {
        if (this.firstMove) {
            this.firstMove = false;
        }
        switch (this.nextMove) {
            case VULNERABLE_ATTACK: {
                AbstractDungeon.actionManager.addToBottom(new AnimateSlowAttackAction(this));
                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage.get(0), AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, this, new VulnerablePower(AbstractDungeon.player, this.vulnerableAmount, true), this.vulnerableAmount));
                break;
            }
            case FRAIL_ATTACK: {
                AbstractDungeon.actionManager.addToBottom(new AnimateSlowAttackAction(this));
                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage.get(1), AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, this, new FrailPower(AbstractDungeon.player, this.frailAmount, true), this.frailAmount));
                break;
            }
            case DEX_ATTACK: {
                AbstractDungeon.actionManager.addToBottom(new AnimateSlowAttackAction(this));
				AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage.get(2), AbstractGameAction.AttackEffect.BLUNT_LIGHT));
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, this, new DexterityPower(AbstractDungeon.player, -this.dexAmount), -this.dexAmount));
                if (this.focusAmount > 0 && !AbstractDungeon.player.orbs.isEmpty()){
                    AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, this, new FocusPower(AbstractDungeon.player, -this.focusAmount), -this.focusAmount));
                }
                break;
            }
        }
        AbstractDungeon.actionManager.addToBottom(new RollMoveAction(this));
    }

    @Override
    protected void getMove(final int num) {
        if (this.firstMove || (!this.lastMove(VULNERABLE_ATTACK) && !this.lastMoveBefore(VULNERABLE_ATTACK))) {
            this.setMove(MOVES[0], VULNERABLE_ATTACK, Intent.ATTACK_DEBUFF, this.vulnerableDamage);
        } else {
            if (this.lastMove(DEX_ATTACK) || (num < 50 && !this.lastMove(FRAIL_ATTACK))) {
                this.setMove(WarGolem.MOVES[1], FRAIL_ATTACK, Intent.ATTACK_DEBUFF, this.frailDamage);
            } else {
                this.setMove(WarGolem.MOVES[2], DEX_ATTACK, Intent.ATTACK_DEBUFF, this.dexDamage);
            }
        }
    }
}