package spireMapOverhaul.zones.thieveshideout.monsters;

import basemod.abstracts.CustomMonster;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.AnimateSlowAttackAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDrawPileAction;
import com.megacrit.cardcrawl.actions.common.RollMoveAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.status.Dazed;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.powers.DrawReductionPower;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import spireMapOverhaul.SpireAnniversary6Mod;

public class BanditLieutenant extends CustomMonster {
    public static final String ID = SpireAnniversary6Mod.makeID("BanditLieutenant");
    private static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID);
    public static final String NAME = monsterStrings.NAME;
    public static final String[] MOVES = monsterStrings.MOVES;
    private static final String IMG = SpireAnniversary6Mod.makeImagePath("monsters/WarGolem/WarGolem.png"); //TODO: Replace with appropriate recolor
    private boolean firstMove = true;
    private static final byte CUDGEL_BLOW_ATTACK = 1;
    private static final byte NOXIOUS_SMOKE_DEBUFF = 2;
    private static final byte BIG_SWING_ATTACK = 3;
    private static final int CUDGEL_BLOW_DAMAGE = 10;
    private static final int A3_CUDGEL_BLOW_DAMAGE = 12;
    private static final int CUDGEL_BLOW_DAZES = 2;
    private static final int A18_CUDGEL_BLOW_DAZES = 3;
    private static final int NOXIOUS_SMOKE_VULNERABLE = 1;
    private static final int A18_NOXIOUS_SMOKE_VULNERABLE = 2;
    private static final int NOXIOUS_SMOKE_DRAW_DOWN = 1;
    private static final int BIG_SWING_DAMAGE = 13;
    private static final int A3_BIG_SWING_DAMAGE = 15;
    private static final int HP_MIN = 74;
    private static final int HP_MAX = 76;
    private static final int A8_HP_MIN = 77;
    private static final int A8_HP_MAX = 79;
    private int cudgelBlowDamage;
    private int cudgelBlowDazes;
    private int noxiousSmokeVulnerable;
    private int bigSwingDamage;

    public BanditLieutenant() {
        this(0.0f, 0.0f);
    }

    public BanditLieutenant(final float x, final float y) {
        super(NAME, ID, HP_MAX, -5.0F, 0, 230.0f, 300.0f, IMG, x, y);
        this.type = EnemyType.ELITE;
        if (AbstractDungeon.ascensionLevel >= 8) {
            this.setHp(A8_HP_MIN, A8_HP_MAX);
        } else {
            this.setHp(HP_MIN, HP_MAX);
        }

        if (AbstractDungeon.ascensionLevel >= 3) {
            this.cudgelBlowDamage = A3_CUDGEL_BLOW_DAMAGE;
            this.bigSwingDamage = A3_BIG_SWING_DAMAGE;
        } else {
            this.cudgelBlowDamage = CUDGEL_BLOW_DAMAGE;
            this.bigSwingDamage = BIG_SWING_DAMAGE;
        }
        this.damage.add(new DamageInfo(this, this.cudgelBlowDamage));
        this.damage.add(new DamageInfo(this, this.bigSwingDamage));

        if (AbstractDungeon.ascensionLevel >= 18) {
            this.cudgelBlowDazes = A18_CUDGEL_BLOW_DAZES;
            this.noxiousSmokeVulnerable = A18_NOXIOUS_SMOKE_VULNERABLE;
        } else {
            this.cudgelBlowDazes = CUDGEL_BLOW_DAZES;
            this.noxiousSmokeVulnerable = NOXIOUS_SMOKE_VULNERABLE;
        }
    }

    @Override
    public void takeTurn() {
        if (this.firstMove) {
            this.firstMove = false;
        }
        switch (this.nextMove) {
            case CUDGEL_BLOW_ATTACK: {
                this.addToBot(new AnimateSlowAttackAction(this));
                this.addToBot(new DamageAction(AbstractDungeon.player, this.damage.get(0), AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
                this.addToBot(new MakeTempCardInDrawPileAction(new Dazed(), this.cudgelBlowDazes, true, true));
                break;
            }
            case NOXIOUS_SMOKE_DEBUFF: {
                this.addToBot(new AnimateSlowAttackAction(this));
                this.addToBot(new DamageAction(AbstractDungeon.player, this.damage.get(1), AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
                this.addToBot(new ApplyPowerAction(AbstractDungeon.player, this, new VulnerablePower(AbstractDungeon.player, this.noxiousSmokeVulnerable, true)));
                this.addToBot(new ApplyPowerAction(AbstractDungeon.player, this, new DrawReductionPower(AbstractDungeon.player, NOXIOUS_SMOKE_DRAW_DOWN)));
                break;
            }
            case BIG_SWING_ATTACK: {
                this.addToBot(new AnimateSlowAttackAction(this));
                this.addToBot(new DamageAction(AbstractDungeon.player, this.damage.get(1), AbstractGameAction.AttackEffect.BLUNT_HEAVY));
                break;
            }
        }
        this.addToBot(new RollMoveAction(this));
    }

    @Override
    protected void getMove(final int num) {
        if (this.firstMove || this.lastMove(BIG_SWING_ATTACK)) {
            this.setMove(MOVES[0], CUDGEL_BLOW_ATTACK, Intent.ATTACK_DEBUFF, this.cudgelBlowDamage);
        } else if (this.lastMove(CUDGEL_BLOW_ATTACK)) {
            this.setMove(MOVES[1], NOXIOUS_SMOKE_DEBUFF, Intent.DEBUFF);
        } else {
            this.setMove(MOVES[2], CUDGEL_BLOW_ATTACK, Intent.ATTACK, this.bigSwingDamage);
        }
    }
}
