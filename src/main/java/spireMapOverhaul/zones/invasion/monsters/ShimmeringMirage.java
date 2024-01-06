package spireMapOverhaul.zones.invasion.monsters;

import basemod.abstracts.CustomMonster;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.actions.common.RollMoveAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.GainStrengthPower;
import com.megacrit.cardcrawl.powers.IntangiblePower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.vfx.combat.LightFlareParticleEffect;
import spireMapOverhaul.SpireAnniversary6Mod;
import spireMapOverhaul.zones.invasion.powers.CleansePower;

public class ShimmeringMirage extends CustomMonster
{
    public static final String ID = SpireAnniversary6Mod.makeID("ShimmeringMirage");
    private static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID);
    public static final String NAME = monsterStrings.NAME;
    public static final String[] MOVES = monsterStrings.MOVES;
    private static final String IMG = SpireAnniversary6Mod.makeImagePath("monsters/Invasion/ShimmeringMirage/ShimmeringMirage.png");
    private boolean firstMove = true;
    private static final byte SHINE_ATTACK = 1;
    private static final byte GLOW_BUFF = 2;
    private static final byte PULSE_ATTACK = 3;
    private static final int SHINE_ATTACK_DAMAGE = 7;
    private static final int A2_SHINE_ATTACK_DAMAGE = 8;
    private static final int GLOW_STRENGTH = 1;
    private static final int A17_GLOW_STRENGTH = 1;
    private static final int GLOW_SELF_STRENGTH = 1;
    private static final int A17_GLOW_SELF_STRENGTH = 2;
    private static final int PULSE_ATTACK_DAMAGE = 4;
    private static final int A2_PULSE_ATTACK_DAMAGE = 5;
    private static final int PULSE_ATTACK_HEAL = 4;
    private static final int CLEANSE_AMOUNT = 2;
    private static final int HP_MIN = 30;
    private static final int HP_MAX = 32;
    private static final int A7_HP_MIN = 31;
    private static final int A7_HP_MAX = 33;
    private int shineDamage;
    private int pulseDamage;
    private int glowStrength;
    private int glowSelfStrength;

    public ShimmeringMirage() {
        this(0.0f, 0.0f);
    }

    public ShimmeringMirage(final float x, final float y) {
        super(ShimmeringMirage.NAME, ID, HP_MAX, -5.0F, 0, 125.0F, 125.0F, IMG, x, y);
        this.type = EnemyType.NORMAL;
        if (AbstractDungeon.ascensionLevel >= 7) {
            this.setHp(A7_HP_MIN, A7_HP_MAX);
        } else {
            this.setHp(HP_MIN, HP_MAX);
        }

        if (AbstractDungeon.ascensionLevel >= 17) {
            this.glowStrength = A17_GLOW_STRENGTH;
            this.glowSelfStrength = A17_GLOW_SELF_STRENGTH;
        } else {
            this.glowStrength = GLOW_STRENGTH;
            this.glowSelfStrength = GLOW_SELF_STRENGTH;
        }

        if (AbstractDungeon.ascensionLevel >= 2) {
            this.shineDamage = A2_SHINE_ATTACK_DAMAGE;
            this.pulseDamage = A2_PULSE_ATTACK_DAMAGE;
        } else {
            this.shineDamage = SHINE_ATTACK_DAMAGE;
            this.pulseDamage = PULSE_ATTACK_DAMAGE;
        }
        this.damage.add(new DamageInfo(this, this.shineDamage));
        this.damage.add(new DamageInfo(this, this.pulseDamage));
    }

    @Override
    public void usePreBattleAction() {
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new CleansePower(this, CLEANSE_AMOUNT, false), CLEANSE_AMOUNT));
    }

    @Override
    public void takeTurn() {
        if (this.firstMove) {
            this.firstMove = false;
        }
        switch (this.nextMove) {
            case SHINE_ATTACK:
                AbstractDungeon.effectsQueue.add(new LightFlareParticleEffect(this.drawX, this.drawY, Color.WHITE));
                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage.get(0), AbstractGameAction.AttackEffect.SLASH_VERTICAL));
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new IntangiblePower(this, 1)));
                break;
            case GLOW_BUFF:
                for (AbstractMonster m : AbstractDungeon.getMonsters().monsters) {
                    if (m == this || !m.isDying) {
                        // For this enemy, those to the left of it (which have already acted), and those that aren't attacking, give strength
                        // For enemies to the right of this enemy, give gain strength at end of turn, so their damage doesn't increase
                        if (m == this) {
                            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(m, this, new StrengthPower(m, this.glowSelfStrength), this.glowSelfStrength));
                        }
                        else if (m.drawX < this.drawX || m.getIntentDmg() == -1) {
                            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(m, this, new StrengthPower(m, this.glowStrength), this.glowStrength));
                        }
                        else {
                            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(m, this, new GainStrengthPower(m, this.glowStrength), this.glowStrength));
                        }
                    }
                }
                break;
            case PULSE_ATTACK:
                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage.get(1), AbstractGameAction.AttackEffect.SLASH_VERTICAL));
                AbstractDungeon.actionManager.addToBottom(new HealAction(this, this, PULSE_ATTACK_HEAL));
                break;
        }
        AbstractDungeon.actionManager.addToBottom(new RollMoveAction(this));
    }

    @Override
    protected void getMove(final int num) {
        if (this.lastMove(PULSE_ATTACK) || this.firstMove) {
            this.setMove(SHINE_ATTACK, Intent.ATTACK_DEFEND, this.shineDamage);
        }
        else if (this.lastMove(SHINE_ATTACK)) {
            this.setMove(GLOW_BUFF, Intent.BUFF);
        }
        else {
            this.setMove(PULSE_ATTACK, Intent.ATTACK_BUFF, this.pulseDamage);
        }
    }

    @Override
    public void damage(DamageInfo info) {
        if (info.output > 0 && this.hasPower(IntangiblePower.POWER_ID)) {
            info.output = 1;
        }

        super.damage(info);
    }
}