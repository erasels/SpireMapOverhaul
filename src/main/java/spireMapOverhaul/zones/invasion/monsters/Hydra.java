package spireMapOverhaul.zones.invasion.monsters;

import basemod.abstracts.CustomMonster;
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
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.powers.WeakPower;
import spireMapOverhaul.SpireAnniversary6Mod;
import spireMapOverhaul.zones.invasion.powers.HydraHeadsPower;

public class Hydra extends CustomMonster {
    public static final String ID = SpireAnniversary6Mod.makeID("Hydra");
    private static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID);
    public static final String NAME = monsterStrings.NAME;
    public static final String[] MOVES = monsterStrings.MOVES;
    private static final String IMG = SpireAnniversary6Mod.makeImagePath("monsters/Invasion/Hydra/Hydra.png");
    private boolean firstMove = true;
    private static final byte TAIL_WHIP_ATTACK = 1;
    private static final byte SWIPE_ATTACK = 2;
    private static final byte RAVENOUS_HUNGER_ATTACK = 3;
    private static final int TAIL_WHIP_DAMAGE = 8;
    private static final int TAIL_WHIP_WEAK = 1;
    private static final int SWIPE_DAMAGE = 9;
    private static final int RAVENOUS_HUNGER_DAMAGE = 0;
    private static final int RAVENOUS_HUNGER_HITS = 5;
    private static final int STRENGTH = 4;
    private static final int A3_STRENGTH = 5;
    private static final int HEAD_DAMAGE_THRESHOLD = 7;
    private static final int HP_MIN = 94;
    private static final int HP_MAX = 97;
    private static final int A8_HP_MIN = 96;
    private static final int A8_HP_MAX = 99;
    private int strength;

    public Hydra() {
        this(0.0f, 0.0f);
    }

    public Hydra(final float x, final float y) {
        super(Hydra.NAME, ID, HP_MAX, -5.0F, 0, 505.0f, 360.0f, IMG, x, y);
        this.type = EnemyType.ELITE;
        if (AbstractDungeon.ascensionLevel >= 8) {
            this.setHp(A8_HP_MIN, A8_HP_MAX);
        } else {
            this.setHp(HP_MIN, HP_MAX);
        }

        if (AbstractDungeon.ascensionLevel >= 3) {
            this.strength = A3_STRENGTH;
        } else {
            this.strength = STRENGTH;
        }
        this.damage.add(new DamageInfo(this, TAIL_WHIP_DAMAGE));
        this.damage.add(new DamageInfo(this, SWIPE_DAMAGE));
        this.damage.add(new DamageInfo(this, RAVENOUS_HUNGER_DAMAGE));
    }

    @Override
    public void usePreBattleAction() {
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new StrengthPower(this, this.strength), this.strength));
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new HydraHeadsPower(this, HEAD_DAMAGE_THRESHOLD)));
    }

    @Override
    public void takeTurn() {
        if (this.firstMove) {
            this.firstMove = false;
        }
        switch (this.nextMove) {
            case TAIL_WHIP_ATTACK:
                AbstractDungeon.actionManager.addToBottom(new AnimateSlowAttackAction(this));
                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage.get(0), AbstractGameAction.AttackEffect.BLUNT_LIGHT));
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, this, new WeakPower(AbstractDungeon.player, TAIL_WHIP_WEAK, true), TAIL_WHIP_WEAK));
                break;
            case SWIPE_ATTACK:
                AbstractDungeon.actionManager.addToBottom(new AnimateSlowAttackAction(this));
                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage.get(1), AbstractGameAction.AttackEffect.SLASH_HORIZONTAL));
                break;
            case RAVENOUS_HUNGER_ATTACK:
                for (int i = 0; i < RAVENOUS_HUNGER_HITS; i++) {
                    AbstractDungeon.actionManager.addToBottom(new AnimateFastAttackAction(this));
                    AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage.get(2), AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
                }
                break;
        }
        AbstractDungeon.actionManager.addToBottom(new RollMoveAction(this));
    }

    @Override
    protected void getMove(final int num) {
        if (this.firstMove) {
            this.setMove(MOVES[0], TAIL_WHIP_ATTACK, Intent.ATTACK_DEBUFF, TAIL_WHIP_DAMAGE);
        } else {
            if (this.lastMove(RAVENOUS_HUNGER_ATTACK) && (AbstractDungeon.ascensionLevel < 18 || this.lastMoveBefore(RAVENOUS_HUNGER_ATTACK))) {
                this.setMove(Hydra.MOVES[1], SWIPE_ATTACK, Intent.ATTACK, SWIPE_DAMAGE);
            } else {
                this.setMove(Hydra.MOVES[2], RAVENOUS_HUNGER_ATTACK, Intent.ATTACK, RAVENOUS_HUNGER_DAMAGE, RAVENOUS_HUNGER_HITS, true);
            }
        }
    }
}