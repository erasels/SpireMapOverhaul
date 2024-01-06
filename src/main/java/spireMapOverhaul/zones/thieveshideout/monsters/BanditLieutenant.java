package spireMapOverhaul.zones.thieveshideout.monsters;

import basemod.abstracts.CustomMonster;
import com.badlogic.gdx.math.MathUtils;
import com.esotericsoftware.spine.AnimationState;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.AnimateShakeAction;
import com.megacrit.cardcrawl.actions.animations.AnimateSlowAttackAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.status.Dazed;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.powers.DrawReductionPower;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.megacrit.cardcrawl.vfx.combat.SmokeBombEffect;
import spireMapOverhaul.SpireAnniversary6Mod;

public class BanditLieutenant extends CustomMonster {
    public static final String ID = SpireAnniversary6Mod.makeID("BanditLieutenant");
    private static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID);
    public static final String NAME = monsterStrings.NAME;
    public static final String[] MOVES = monsterStrings.MOVES;
    private boolean firstMove = true;
    private static final byte CUDGEL_BLOW_ATTACK = 1;
    private static final byte NOXIOUS_SMOKE_DEBUFF = 2;
    private static final byte BIG_SWING_ATTACK = 3;
    private static final int CUDGEL_BLOW_DAMAGE = 10;
    private static final int A3_CUDGEL_BLOW_DAMAGE = 12;
    private static final int CUDGEL_BLOW_DAZES = 1;
    private static final int A18_CUDGEL_BLOW_DAZES = 1;
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
        super(NAME, ID, HP_MAX, -5.0F, -4.0F, 180.0F, 280.0F, null, x, y);
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

        this.loadAnimation(SpireAnniversary6Mod.makeImagePath("monsters/ThievesHideout/BanditLieutenant/skeleton.atlas"), SpireAnniversary6Mod.makeImagePath("monsters/ThievesHideout/BanditLieutenant/skeleton.json"), 1.0F);
        AnimationState.TrackEntry e = this.state.setAnimation(0, "Idle", true);
        e.setTime(e.getEndTime() * MathUtils.random());
        this.stateData.setMix("Hit", "Idle", 0.2F);
        this.state.setTimeScale(1.0F);
    }

    @Override
    public void takeTurn() {
        if (this.firstMove) {
            this.firstMove = false;
        }
        switch (this.nextMove) {
            case CUDGEL_BLOW_ATTACK:
                this.addToBot(new AnimateSlowAttackAction(this));
                this.addToBot(new DamageAction(AbstractDungeon.player, this.damage.get(0), AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
                this.addToBot(new MakeTempCardInDrawPileAction(new Dazed(), this.cudgelBlowDazes, true, true));
                break;
            case NOXIOUS_SMOKE_DEBUFF:
                this.addToBot(new AnimateShakeAction(this, 0.5F, 0.1F));
                this.addToBot(new VFXAction(new SmokeBombEffect(AbstractDungeon.player.hb.cX, AbstractDungeon.player.hb.cY)));
                this.addToBot(new ApplyPowerAction(AbstractDungeon.player, this, new VulnerablePower(AbstractDungeon.player, this.noxiousSmokeVulnerable, true)));
                this.addToBot(new ApplyPowerAction(AbstractDungeon.player, this, new DrawReductionPower(AbstractDungeon.player, NOXIOUS_SMOKE_DRAW_DOWN)));
                break;
            case BIG_SWING_ATTACK:
                this.addToBot(new ChangeStateAction(this, "MAUL"));
                this.addToBot(new WaitAction(0.3F));
                this.addToBot(new DamageAction(AbstractDungeon.player, this.damage.get(1), AbstractGameAction.AttackEffect.BLUNT_HEAVY));
                break;
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

    @Override
    public void changeState(String key) {
        if (key != null && key.equals("MAUL")) {
            this.state.setAnimation(0, "Attack", false);
            this.state.addAnimation(0, "Idle", true, 0.0F);
        }
    }

    @Override
    public void damage(DamageInfo info) {
        super.damage(info);
        if (info.owner != null && info.type != DamageInfo.DamageType.THORNS && info.output > 0) {
            this.state.setAnimation(0, "Hit", false);
            this.state.setTimeScale(1.0F);
            this.state.addAnimation(0, "Idle", true, 0.0F);
        }
    }
}
