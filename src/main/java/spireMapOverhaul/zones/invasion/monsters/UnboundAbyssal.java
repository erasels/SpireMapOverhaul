package spireMapOverhaul.zones.invasion.monsters;

import basemod.abstracts.CustomMonster;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.AnimateFastAttackAction;
import com.megacrit.cardcrawl.actions.animations.TalkAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDrawPileAction;
import com.megacrit.cardcrawl.actions.common.RollMoveAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.status.Slimed;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.powers.RegenerateMonsterPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import spireMapOverhaul.SpireAnniversary6Mod;
import spireMapOverhaul.zones.invasion.powers.BrokenCrystalSealPower;

public class UnboundAbyssal extends CustomMonster {
    public static final String ID = SpireAnniversary6Mod.makeID("UnboundAbyssal");
    private static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID);
    public static final String NAME = monsterStrings.NAME;
    public static final String[] MOVES = monsterStrings.MOVES;
    public static final String[] DIALOG = monsterStrings.DIALOG;
    private static final String IMG = SpireAnniversary6Mod.makeImagePath("monsters/Invasion/UnboundAbyssal/UnboundAbyssal.png");
    private boolean firstMove = true;
    private static final byte SLUDGE_ATTACK = 1;
    private static final byte WONT_GO_BACK_MOVE = 2;
    private static final byte TENDRILS_ATTACK = 3;
    private static final int SLUDGE_DAMAGE = 13;
    private static final int A2_SLUDGE_DAMAGE = 15;
    private static final int SLUDGE_SLIMES = 2;
    private static final int A17_SLUDGE_SLIMES = 2;
    private static final int WONT_GO_BACK_DAMAGE = 10;
    private static final int A17_WONT_GO_BACK_DAMAGE = 12;
    private static final int WONT_GO_BACK_STRENGTH = 4;
    private static final int A17_WONT_GO_BACK_STRENGTH = 4;
    private static final int TENDRILS_DAMAGE = 4;
    private static final int A2_TENDRILS_DAMAGE = 5;
    private static final int TENDRILS_HITS = 2;
    private static final int HP_MIN = 135;
    private static final int HP_MAX = 135;
    private static final int A7_HP_MIN = 150;
    private static final int A7_HP_MAX = 150;
    private static final int REGEN = 45;
    private static final int A17_REGEN = 60;
    private static final int BROKEN_CRYSTAL_SEAL_AMOUNT = 2;
    private int sludgeDamage;
    private int sludgeSlimes;
    private int wontGoBackDamage;
    private int wontGoBackStrength;
    private int tendrilsDamage;
    private int regen;

    private boolean usedDialog;

    public UnboundAbyssal() {
        this(0.0f, 0.0f);
    }

    public UnboundAbyssal(final float x, final float y) {
        super(UnboundAbyssal.NAME, ID, HP_MAX, -5.0F, 0, 300, 300, IMG, x, y);
        this.type = EnemyType.NORMAL;
        this.dialogX = -100;
        this.dialogY = 50;
        if (AbstractDungeon.ascensionLevel >= 7) {
            this.setHp(A7_HP_MIN, A7_HP_MAX);
        } else {
            this.setHp(HP_MIN, HP_MAX);
        }

        if (AbstractDungeon.ascensionLevel >= 2) {
            this.sludgeDamage = A2_SLUDGE_DAMAGE;
            this.tendrilsDamage = A2_TENDRILS_DAMAGE;
        } else {
            this.sludgeDamage = SLUDGE_DAMAGE;
            this.tendrilsDamage = TENDRILS_DAMAGE;
        }

        if (AbstractDungeon.ascensionLevel >= 17) {
            this.sludgeSlimes = A17_SLUDGE_SLIMES;
            this.wontGoBackDamage = A17_WONT_GO_BACK_DAMAGE;
            this.wontGoBackStrength = A17_WONT_GO_BACK_STRENGTH;
            this.regen = A17_REGEN;
        } else {
            this.sludgeSlimes = SLUDGE_SLIMES;
            this.wontGoBackDamage = WONT_GO_BACK_DAMAGE;
            this.wontGoBackStrength = WONT_GO_BACK_STRENGTH;
            this.regen = REGEN;
        }

        this.damage.add(new DamageInfo(this, this.sludgeDamage));
        this.damage.add(new DamageInfo(this, this.wontGoBackDamage));
        this.damage.add(new DamageInfo(this, this.tendrilsDamage));

        this.usedDialog = false;
    }

    @Override
    public void usePreBattleAction() {
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new RegenerateMonsterPower(this, this.regen), this.regen));
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new BrokenCrystalSealPower(this, BROKEN_CRYSTAL_SEAL_AMOUNT), BROKEN_CRYSTAL_SEAL_AMOUNT));
    }

    @Override
    public void takeTurn() {
        if (this.firstMove) {
            this.firstMove = false;
        }
        switch (this.nextMove) {
            case SLUDGE_ATTACK:
                AbstractDungeon.actionManager.addToBottom(new AnimateFastAttackAction(this));
                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage.get(0), AbstractGameAction.AttackEffect.POISON));
                AbstractDungeon.actionManager.addToBottom(new MakeTempCardInDrawPileAction(new Slimed(), this.sludgeSlimes, true, true));
                break;
            case WONT_GO_BACK_MOVE:
                if (!this.usedDialog) {
                    AbstractDungeon.actionManager.addToBottom(new TalkAction(this, DIALOG[0], 0.5F, 2.0F));
                    this.usedDialog = true;
                }
                if (AbstractDungeon.ascensionLevel >= 17) {
                    AbstractDungeon.actionManager.addToBottom(new AnimateFastAttackAction(this));
                    AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage.get(1), AbstractGameAction.AttackEffect.POISON));
                }
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new StrengthPower(this, this.wontGoBackStrength), this.wontGoBackStrength));
                break;
            case TENDRILS_ATTACK:
                AbstractDungeon.actionManager.addToBottom(new AnimateFastAttackAction(this));
                for (int i=0; i < TENDRILS_HITS; i++) {
                    AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage.get(2), AbstractGameAction.AttackEffect.POISON));
                }
                break;
        }
        AbstractDungeon.actionManager.addToBottom(new RollMoveAction(this));
    }

    @Override
    protected void getMove(final int num) {
        if (this.firstMove || this.lastMove(TENDRILS_ATTACK)) {
            this.setMove(MOVES[0], SLUDGE_ATTACK, Intent.ATTACK_DEBUFF, this.sludgeDamage);
        }
        else if (this.lastMove(SLUDGE_ATTACK)) {
            // Deliberately left nameless
            this.setMove(WONT_GO_BACK_MOVE, AbstractDungeon.ascensionLevel >= 17 ? Intent.ATTACK_BUFF : Intent.BUFF, this.wontGoBackDamage);
        }
        else {
            this.setMove(MOVES[2], TENDRILS_ATTACK, Intent.ATTACK, this.tendrilsDamage, TENDRILS_HITS, true);
        }
    }
}