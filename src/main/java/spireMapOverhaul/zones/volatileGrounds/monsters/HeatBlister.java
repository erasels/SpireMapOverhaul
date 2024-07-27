package spireMapOverhaul.zones.volatileGrounds.monsters;

import basemod.abstracts.CustomMonster;
import com.badlogic.gdx.math.MathUtils;
import com.esotericsoftware.spine.AnimationState;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.AnimateFastAttackAction;
import com.megacrit.cardcrawl.actions.animations.AnimateJumpAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDiscardAction;
import com.megacrit.cardcrawl.actions.common.RollMoveAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.status.Burn;
import com.megacrit.cardcrawl.cards.status.Slimed;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.powers.MalleablePower;
import spireMapOverhaul.SpireAnniversary6Mod;
import spireMapOverhaul.zones.volatileGrounds.powers.ExplodePlusPower;

public class HeatBlister extends CustomMonster {
    public static final String ID = SpireAnniversary6Mod.makeID("HeatBlister");
    private static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID);
    public static final String NAME = monsterStrings.NAME;
    public static final String[] MOVES = monsterStrings.MOVES;
    private static final String IMG = SpireAnniversary6Mod.makeImagePath("monsters/VolatileGrounds/HeatBlister/skeleton.png");
    private static final String ATLAS = SpireAnniversary6Mod.makeImagePath("monsters/VolatileGrounds/HeatBlister/skeleton.atlas");
    private static final String SKELETON = SpireAnniversary6Mod.makeImagePath("monsters/VolatileGrounds/HeatBlister/skeleton.json");
    private static final byte BUFF = 0;
    private static final byte ATTACK = 1;
    private static final byte ATTACK_AND_DEBUFF = 2;
    private static final int ATTACK_DAMAGE_1 = 15;
    private static final int A2_ATTACK_DAMAGE_1 = 18;
    private static final int ATTACK_DAMAGE_2 = 5;
    private static final int A2_ATTACK_DAMAGE_2 = 8;
    private static final int A17_EXPLOSION_COUNTDOWN = 4;
    private static final int EXPLOSION_COUNTDOWN = 5;
    private static final int EXPLOSION_DAMAGE = 40;
    private static final int A2_EXPLOSION_DAMAGE = 50;
    private static final int HP_MIN = 100;
    private static final int HP_MAX = 110;
    private static final int A7_HP_MIN = 120;
    private static final int A7_HP_MAX = 130;
    private static final int MALLEABLE = 3;
    private boolean firstTurn = true;
    private int damage1;
    private int damage2;
    
    public HeatBlister(final float x, final float y) {
        super(NAME, ID, HP_MAX, -5.0F, 0, 270.0f, 240f, IMG, x, y);
        if (AbstractDungeon.ascensionLevel >= 7) {
            this.setHp(A7_HP_MIN, A7_HP_MAX);
        } else {
            this.setHp(HP_MIN, HP_MAX);
        }
        if(AbstractDungeon.ascensionLevel >= 2) {
            this.damage.add(new DamageInfo(this, A2_ATTACK_DAMAGE_1));
            damage1 = A2_ATTACK_DAMAGE_1;
            this.damage.add(new DamageInfo(this, A2_ATTACK_DAMAGE_2));
            damage2 = A2_ATTACK_DAMAGE_2;
        }
        else
        {
            this.damage.add(new DamageInfo(this, ATTACK_DAMAGE_1));
            damage1 = ATTACK_DAMAGE_1;
            this.damage.add(new DamageInfo(this, ATTACK_DAMAGE_2));
            damage2 = ATTACK_DAMAGE_2;
        }
        this.loadAnimation(ATLAS, SKELETON, 1.00f);
        AnimationState.TrackEntry e = this.state.setAnimation(0, "Idle", true);
        e.setTime(e.getEndTime() * MathUtils.random());
    }
    
    @Override
    public void usePreBattleAction() {
        if(AbstractDungeon.ascensionLevel >= 2) {
            if(AbstractDungeon.ascensionLevel >= 17)
            {
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this,
                        new ExplodePlusPower(this, A17_EXPLOSION_COUNTDOWN,
                        A2_EXPLOSION_DAMAGE)));
            }
            else {
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new ExplodePlusPower(this, EXPLOSION_COUNTDOWN,
                        A2_EXPLOSION_DAMAGE)));
            }
        }
        else
        {
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new ExplodePlusPower(this, EXPLOSION_COUNTDOWN,
                    EXPLOSION_DAMAGE)));
        }
    }
    
    @Override
    public void takeTurn() {
        switch (this.nextMove) {
            case ATTACK:
                AbstractDungeon.actionManager.addToBottom(new AnimateJumpAction(this));
                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage.get(0), AbstractGameAction.AttackEffect.FIRE));
                break;
            case ATTACK_AND_DEBUFF:
                AbstractDungeon.actionManager.addToBottom(new AnimateFastAttackAction(this));
                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage.get(1), AbstractGameAction.AttackEffect.FIRE));
                AbstractDungeon.actionManager.addToBottom(new MakeTempCardInDiscardAction(new Slimed(), 1));
                Burn burn = new Burn();
                if(AbstractDungeon.ascensionLevel >= 17) {
                    burn.upgrade();
                }
                AbstractDungeon.actionManager.addToBottom(new MakeTempCardInDiscardAction(burn, 1));
                break;
            case BUFF:
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new MalleablePower(this, MALLEABLE)));
                break;
        }
        AbstractDungeon.actionManager.addToBottom(new RollMoveAction(this));
    }
    
    @Override
    protected void getMove(final int num) {
        if(firstTurn)
        {
            this.setMove(BUFF, Intent.BUFF);
            firstTurn = false;
        }
        else if ((num < 50 && !lastTwoMoves(ATTACK_AND_DEBUFF)) || lastTwoMoves(ATTACK)) {
            this.setMove(ATTACK_AND_DEBUFF, Intent.ATTACK_DEBUFF, damage2);
        } else {
            this.setMove(ATTACK, Intent.ATTACK, damage1);
        }
    }
}
