package spireMapOverhaul.zones.invasion.monsters;

import basemod.abstracts.CustomMonster;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.AnimateFastAttackAction;
import com.megacrit.cardcrawl.actions.animations.AnimateSlowAttackAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.RollMoveAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.BarricadePower;
import com.megacrit.cardcrawl.powers.FrailPower;
import com.megacrit.cardcrawl.powers.MetallicizePower;
import spireMapOverhaul.SpireAnniversary6Mod;
import spireMapOverhaul.zones.invasion.powers.GoldenBulwarkPower;
import spireMapOverhaul.zones.invasion.powers.MidasAuraPower;

public class OpulentOffering extends CustomMonster
{
    public static final String ID = SpireAnniversary6Mod.makeID("OpulentOffering");
    private static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID);
    public static final String NAME = monsterStrings.NAME;
    public static final String[] MOVES = monsterStrings.MOVES;
    private static final String IMG = SpireAnniversary6Mod.makeImagePath("monsters/Invasion/OpulentOffering/OpulentOffering.png");
    private boolean firstMove = true;
    private static final byte GOBLET_BASH_ATTACK = 1;
    private static final byte COIN_BARRAGE_ATTACK = 2;
    private static final byte OPULENT_BARRIER_ATTACK = 3;
    private static final byte OPULENT_OVERFLOW_DEFEND = 4;
    private static final int GOBLET_BASH_DAMAGE = 5;
    private static final int A2_GOBLET_BASH_DAMAGE = 6;
    private static final int GOBLET_BASH_FRAIL_AMOUNT = 1;
    private static final int COIN_BARRAGE_DAMAGE = 2;
    private static final int COIN_BARRAGE_HITS = 4;
    private static final int A2_COIN_BARRAGE_HITS = 5;
    private static final int COIN_BARRAGE_METALLICIZE_AMOUNT = 2;
    private static final int OPULENT_BARRIER_DAMAGE = 6;
    private static final int A2_OPULENT_BARRIER_DAMAGE = 7;
    private static final int OPULENT_BLOCK = 3;
    private static final int STARTING_BLOCK_BASE = 4;
    private static final int GOLD_BLOCK_DIVISOR = 50;
    private static final int A7_GOLD_BLOCK_DIVISOR = 40;
    private static final int HP_MIN = 23;
    private static final int HP_MAX = 25;
    private static final int A7_HP_MIN = 24;
    private static final int A7_HP_MAX = 26;
    private int gobletBashDamage;
    private int coinBarrageHits;
    private int opulentBarrierDamage;

    public OpulentOffering() {
        this(0.0f, 0.0f);
    }

    public OpulentOffering(final float x, final float y) {
        super(OpulentOffering.NAME, ID, HP_MAX, -5.0F, 0, 175.0F, 90.0F, IMG, x, y);
        this.type = EnemyType.NORMAL;
        if (AbstractDungeon.ascensionLevel >= 7) {
            this.setHp(A7_HP_MIN, A7_HP_MAX);
        } else {
            this.setHp(HP_MIN, HP_MAX );
        }

        if (AbstractDungeon.ascensionLevel >= 2) {
            this.gobletBashDamage = A2_GOBLET_BASH_DAMAGE;
            this.coinBarrageHits = A2_COIN_BARRAGE_HITS;
            this.opulentBarrierDamage = A2_OPULENT_BARRIER_DAMAGE;
        } else {
            this.gobletBashDamage = GOBLET_BASH_DAMAGE;
            this.coinBarrageHits = COIN_BARRAGE_HITS;
            this.opulentBarrierDamage = OPULENT_BARRIER_DAMAGE;
        }
        this.damage.add(new DamageInfo(this, this.gobletBashDamage));
        this.damage.add(new DamageInfo(this, COIN_BARRAGE_DAMAGE));
        this.damage.add(new DamageInfo(this, this.opulentBarrierDamage));
    }

    @Override
    public void usePreBattleAction() {
        if (AbstractDungeon.ascensionLevel >= 17) {
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new BarricadePower(this)));
        }
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new MidasAuraPower(this)));
        int blockDivisor = AbstractDungeon.ascensionLevel >= 7 ? A7_GOLD_BLOCK_DIVISOR : GOLD_BLOCK_DIVISOR;
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new GoldenBulwarkPower(this, blockDivisor)));
        int startingBlockAmount = STARTING_BLOCK_BASE + ((AbstractDungeon.player.gold + blockDivisor - 1) / blockDivisor);
        AbstractDungeon.actionManager.addToBottom(new GainBlockAction(this, this, startingBlockAmount));
    }

    @Override
    public void takeTurn() {
        if (this.firstMove) {
            this.firstMove = false;
        }
        switch (this.nextMove) {
            case GOBLET_BASH_ATTACK: {
                AbstractDungeon.actionManager.addToBottom(new AnimateSlowAttackAction(this));
                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage.get(0), AbstractGameAction.AttackEffect.BLUNT_LIGHT));
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, this, new FrailPower(AbstractDungeon.player, GOBLET_BASH_FRAIL_AMOUNT, true), GOBLET_BASH_FRAIL_AMOUNT));
                break;
            }
            case COIN_BARRAGE_ATTACK: {
                AbstractDungeon.actionManager.addToBottom(new AnimateFastAttackAction(this));
                for (int i = 0; i < this.coinBarrageHits; i++) {
                    AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage.get(1), AbstractGameAction.AttackEffect.SLASH_HORIZONTAL));
                }
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new MetallicizePower(this, COIN_BARRAGE_METALLICIZE_AMOUNT), COIN_BARRAGE_METALLICIZE_AMOUNT));

                break;
            }
            case OPULENT_BARRIER_ATTACK: {
                AbstractDungeon.actionManager.addToBottom(new AnimateFastAttackAction(this));
                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage.get(2), AbstractGameAction.AttackEffect.SHIELD));
                AbstractDungeon.actionManager.addToBottom(new GainBlockAction(this, this, OPULENT_BLOCK));
                break;
            }
            case OPULENT_OVERFLOW_DEFEND: {
                for (AbstractMonster m : AbstractDungeon.getMonsters().monsters) {
                    if (m == this || !m.isDying) {
                        AbstractDungeon.actionManager.addToBottom(new GainBlockAction(m, this, OPULENT_BLOCK));
                    }
                }
                break;
            }
        }
        AbstractDungeon.actionManager.addToBottom(new RollMoveAction(this));
    }

    @Override
    protected void getMove(final int num) {
        if (num < 25) {
            this.setMove(MOVES[0], GOBLET_BASH_ATTACK, Intent.ATTACK_DEBUFF, this.gobletBashDamage);
        }
        else if (num < 50) {
            this.setMove(MOVES[1], COIN_BARRAGE_ATTACK, Intent.ATTACK_BUFF, COIN_BARRAGE_DAMAGE, this.coinBarrageHits, true);
        }
        else if (num < 75) {
            this.setMove(MOVES[2], OPULENT_BARRIER_ATTACK, Intent.ATTACK_DEFEND, this.opulentBarrierDamage);
        }
        else {
            this.setMove(MOVES[3], OPULENT_OVERFLOW_DEFEND, Intent.DEFEND);
        }
    }
}