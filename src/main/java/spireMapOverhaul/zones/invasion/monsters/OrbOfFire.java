package spireMapOverhaul.zones.invasion.monsters;

import basemod.abstracts.CustomMonster;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.AnimateFastAttackAction;
import com.megacrit.cardcrawl.actions.animations.AnimateSlowAttackAction;
import com.megacrit.cardcrawl.actions.animations.FastShakeAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDrawPileAction;
import com.megacrit.cardcrawl.actions.common.RollMoveAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.status.Dazed;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.powers.*;
import com.megacrit.cardcrawl.vfx.combat.LightFlareParticleEffect;
import spireMapOverhaul.SpireAnniversary6Mod;

import java.util.ArrayList;
import java.util.Collections;

public class OrbOfFire extends CustomMonster
{
    public static final String ID = SpireAnniversary6Mod.makeID("OrbOfFire");
    private static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID);
    public static final String NAME = monsterStrings.NAME;
    public static final String[] MOVES = monsterStrings.MOVES;
    private static final String IMG = SpireAnniversary6Mod.makeImagePath("monsters/Invasion/OrbOfFire/OrbOfFire.png");
    private boolean firstMove = true;
    private static final byte FIREBOLT_ATTACK = 1;
    private static final byte FLARE_ATTACK = 2;
    private static final byte IRRADIATE_DEBUFF = 3;
    private static final int FIREBOLT_ATTACK_DAMAGE = 5;
    private static final int A2_FIREBOLT_ATTACK_DAMAGE = 6;
    private static final int FLARE_ATTACK_DAMAGE = 11;
    private static final int A2_FLARE_ATTACK_DAMAGE = 13;
    private static final int IRRADIATE_DEBUFF_COUNT = 2;
    private static final int A17_IRRADIATE_DEBUFF_COUNT = 3;
    private static final int IRRADIATE_DEBUFF_AMOUNT = 1;
    private static final int IRRADIATE_STAT_AMOUNT = 1;
    private static final int HP_MIN = 35;
    private static final int HP_MAX = 37;
    private static final int A7_HP_MIN = 36;
    private static final int A7_HP_MAX = 38;
    private int fireboltDamage;
    private int flareDamage;

    public OrbOfFire() {
        this(0.0f, 0.0f);
    }

    public OrbOfFire(final float x, final float y) {
        super(OrbOfFire.NAME, ID, HP_MAX, -5.0F, 0, 125.0F, 125.0F, IMG, x, y);
        this.type = EnemyType.NORMAL;
        if (AbstractDungeon.ascensionLevel >= 7) {
            this.setHp(A7_HP_MIN, A7_HP_MAX);
        } else {
            this.setHp(HP_MIN, HP_MAX);
        }

        if (AbstractDungeon.ascensionLevel >= 2) {
            this.fireboltDamage = A2_FIREBOLT_ATTACK_DAMAGE;
            this.flareDamage = A2_FLARE_ATTACK_DAMAGE;
        } else {
            this.fireboltDamage = FIREBOLT_ATTACK_DAMAGE;
            this.flareDamage = FLARE_ATTACK_DAMAGE;
        }
        this.damage.add(new DamageInfo(this, this.fireboltDamage));
        this.damage.add(new DamageInfo(this, this.flareDamage));
    }

    @Override
    public void takeTurn() {
        if (this.firstMove) {
            this.firstMove = false;
        }
        switch (this.nextMove) {
            case FIREBOLT_ATTACK:
                AbstractDungeon.actionManager.addToBottom(new AnimateFastAttackAction(this));
                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage.get(0), AbstractGameAction.AttackEffect.FIRE));

                if (AbstractDungeon.ascensionLevel >= 17) {
                    AbstractDungeon.actionManager.addToBottom(new MakeTempCardInDrawPileAction(new Dazed(), 1, false, true));
                } else {
                    AbstractDungeon.actionManager.addToBottom(new MakeTempCardInDrawPileAction(new Dazed(), 1, true, true));
                }
                break;
            case FLARE_ATTACK:
                AbstractDungeon.effectsQueue.add(new LightFlareParticleEffect(this.drawX, this.drawY, Color.WHITE));
                AbstractDungeon.actionManager.addToBottom(new AnimateSlowAttackAction(this));
                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage.get(1), AbstractGameAction.AttackEffect.FIRE));
                break;
            case IRRADIATE_DEBUFF:
                AbstractDungeon.actionManager.addToBottom(new FastShakeAction(this, 0.3F, 0.5F));
                int debuffCount = AbstractDungeon.ascensionLevel >= 17 ? A17_IRRADIATE_DEBUFF_COUNT : IRRADIATE_DEBUFF_COUNT;
                ArrayList<String> debuffs = new ArrayList<>();
                debuffs.add(FrailPower.POWER_ID);
                debuffs.add(WeakPower.POWER_ID);
                debuffs.add(StrengthPower.POWER_ID);
                debuffs.add(DexterityPower.POWER_ID);
                if (!AbstractDungeon.player.orbs.isEmpty()) {
                    debuffs.add(FocusPower.POWER_ID);
                }
                Collections.shuffle(debuffs, new java.util.Random(AbstractDungeon.aiRng.randomLong()));

                for (int i = 0; i < debuffCount; i++) {
                    switch (debuffs.get(i)) {
                        case FrailPower.POWER_ID:
                            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, this, new FrailPower(AbstractDungeon.player, IRRADIATE_DEBUFF_AMOUNT, true), IRRADIATE_DEBUFF_AMOUNT));
                            break;
                        case WeakPower.POWER_ID:
                            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, this, new WeakPower(AbstractDungeon.player, IRRADIATE_DEBUFF_AMOUNT, true), IRRADIATE_DEBUFF_AMOUNT));
                            break;
                        case StrengthPower.POWER_ID:
                            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, this, new StrengthPower(AbstractDungeon.player, -IRRADIATE_STAT_AMOUNT), -IRRADIATE_STAT_AMOUNT));
                            break;
                        case DexterityPower.POWER_ID:
                            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, this, new DexterityPower(AbstractDungeon.player, -IRRADIATE_STAT_AMOUNT), -IRRADIATE_STAT_AMOUNT));
                            break;
                        case FocusPower.POWER_ID:
                            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, this, new FocusPower(AbstractDungeon.player, -IRRADIATE_STAT_AMOUNT), -IRRADIATE_STAT_AMOUNT));
                            break;
                    }
                }
                break;
        }
        AbstractDungeon.actionManager.addToBottom(new RollMoveAction(this));
    }

    @Override
    protected void getMove(final int num) {
        if (this.firstMove || this.lastMove(FLARE_ATTACK)) {
            if (num < 50){
                this.setMove(MOVES[0], FIREBOLT_ATTACK, Intent.ATTACK_DEBUFF, this.fireboltDamage);
            }
            else {
                this.setMove(MOVES[2], IRRADIATE_DEBUFF, Intent.DEBUFF);
            }
        } else {
            this.setMove(MOVES[1], FLARE_ATTACK, Intent.ATTACK, this.flareDamage);
        }
    }
}