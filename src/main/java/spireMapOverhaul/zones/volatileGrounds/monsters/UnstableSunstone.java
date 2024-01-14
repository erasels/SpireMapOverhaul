package spireMapOverhaul.zones.volatileGrounds.monsters;

import basemod.abstracts.CustomMonster;
import com.badlogic.gdx.math.MathUtils;
import com.esotericsoftware.spine.AnimationState;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.AnimateFastAttackAction;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.status.Burn;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import spireMapOverhaul.SpireAnniversary6Mod;
import spireMapOverhaul.zones.volatileGrounds.powers.EruptPower;
import spireMapOverhaul.zones.volatileGrounds.powers.UnstablePower;

public class UnstableSunstone extends CustomMonster {
    public static final String ID = SpireAnniversary6Mod.makeID("UnstableSunstone");
    private static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID);
    public static final String NAME = monsterStrings.NAME;
    public static final String[] MOVES = monsterStrings.MOVES;
    private static final String IMG = SpireAnniversary6Mod.makeImagePath("monsters/UnstableSunstone/skeleton.png");
    private static final String ATLAS = SpireAnniversary6Mod.makeImagePath("monsters/UnstableSunstone/skeleton.atlas");
    private static final String SKELETON = SpireAnniversary6Mod.makeImagePath("monsters/UnstableSunstone/skeleton.json");
    private static final byte BUFF = 0;
    private static final byte ATTACK1 = 1;
    private static final byte ATTACK2 = 2;
    private static final int ATTACK1_DAMAGE = 10;
    private static final int ATTACK2_DAMAGE = 5;
    private static final int BURNS = 1;
    private static final int EXPLOSION_DAMAGE = 6;
    private static final int A3_EXPLOSION_DAMAGE = 10;
    private static final int HP_MIN = 110;
    private static final int HP_MAX = 120;
    private static final int A8_HP_MIN = 140;
    private static final int A8_HP_MAX = 155;
    private boolean firstMove = true;
    
    public UnstableSunstone(final float x, final float y) {
        super(NAME, ID, HP_MAX, -5.0F, 0, 340.0f, 340f, IMG, x, y);
        if (AbstractDungeon.ascensionLevel >= 8) {
            this.setHp(A8_HP_MIN, A8_HP_MAX);
        } else {
            this.setHp(HP_MIN, HP_MAX);
        }
        this.damage.add(new DamageInfo(this, ATTACK1_DAMAGE));
        this.damage.add(new DamageInfo(this, ATTACK2_DAMAGE));
        this.loadAnimation(ATLAS, SKELETON, 1.00f);
        AnimationState.TrackEntry e = this.state.setAnimation(0, "animation0", true);
        e.setTime(e.getEndTime() * MathUtils.random());
    }
    
    @Override
    public void usePreBattleAction() {
        if(AbstractDungeon.ascensionLevel >= 18)
        {
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new UnstablePower(this, A3_EXPLOSION_DAMAGE)));
        }
    }
    
    @Override
    public void takeTurn() {
        switch (this.nextMove) {
            case ATTACK1:
                AbstractDungeon.actionManager.addToBottom(new AnimateFastAttackAction(this));
                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage.get(0), AbstractGameAction.AttackEffect.FIRE));
                break;
            case ATTACK2:
                AbstractDungeon.actionManager.addToBottom(new AnimateFastAttackAction(this));
                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage.get(1), AbstractGameAction.AttackEffect.FIRE));
                if(AbstractDungeon.ascensionLevel < 18) {
                    AbstractDungeon.actionManager.addToBottom(new MakeTempCardInHandAction(new Burn(), BURNS));
                }
                else
                {
                    AbstractDungeon.actionManager.addToBottom(new MakeTempCardInDrawPileAction(new Burn(), 1, false, true));
                }
                break;
            case BUFF:
                if(AbstractDungeon.ascensionLevel >= 3)
                {
                    AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new UnstablePower(this, EXPLOSION_DAMAGE)));
                }
                else {
                    AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new UnstablePower(this, A3_EXPLOSION_DAMAGE)));
                }
                break;
        }
        AbstractDungeon.actionManager.addToBottom(new RollMoveAction(this));
    }
    
    @Override
    protected void getMove(final int num) {
        if (this.firstMove && AbstractDungeon.ascensionLevel < 18) {
            this.setMove(BUFF, Intent.BUFF);
            this.firstMove = false;
        } else {
            if (this.lastMove(ATTACK2) && !firstMove) {
                this.setMove(ATTACK1, Intent.ATTACK, ATTACK1_DAMAGE);
                this.firstMove = false;
            } else {
                this.setMove(ATTACK2, Intent.ATTACK_DEBUFF, ATTACK2_DAMAGE);
            }
        }
    }
}