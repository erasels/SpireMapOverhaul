package spireMapOverhaul.zones.volatileGrounds.monsters;

import basemod.abstracts.CustomMonster;
import com.badlogic.gdx.math.MathUtils;
import com.esotericsoftware.spine.AnimationState;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.AnimateFastAttackAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDrawPileAction;
import com.megacrit.cardcrawl.actions.common.RollMoveAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.status.Burn;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import spireMapOverhaul.SpireAnniversary6Mod;
import spireMapOverhaul.zones.volatileGrounds.powers.EruptPower;

public class Eruptor extends CustomMonster {
    public static final String ID = SpireAnniversary6Mod.makeID("Eruptor");
    private static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID);
    public static final String NAME = monsterStrings.NAME;
    public static final String[] MOVES = monsterStrings.MOVES;
    private static final String IMG = SpireAnniversary6Mod.makeImagePath("monsters/VolatileGrounds/Eruptor/skeleton.png");
    private static final String ATLAS = SpireAnniversary6Mod.makeImagePath("monsters/VolatileGrounds/Eruptor/skeleton.atlas");
    private static final String SKELETON = SpireAnniversary6Mod.makeImagePath("monsters/VolatileGrounds/Eruptor/skeleton.json");
    private static final byte BRUISE = 0;
    private static final byte BOIL = 1;
    private static final int BOIL_BURNS = 1;
    private static final int BRUISE_DAMAGE = 6;
    private static final int A2_BRUISE_DAMAGE = 8;
    private static final int EXPLOSION_DAMAGE = 15;
    private static final int A2_EXPLOSION_DAMAGE = 20;
    private static final int TRIGGER = 15;
    private static final int A17_TRIGGER = 10;
    private static final int HP_MIN = 22;
    private static final int HP_MAX = 26;
    private static final int A7_HP_MIN = 25;
    private static final int A7_HP_MAX = 30;
    private boolean firstMove = true;
    private int attackDamage;
    
    public Eruptor(final float x, final float y) {
        super(NAME, ID, HP_MAX, -5.0F, 0, 180.0f, 100.0f, IMG, x, y);
        if (AbstractDungeon.ascensionLevel >= 7) {
            this.setHp(A7_HP_MIN, A7_HP_MAX);
        } else {
            this.setHp(HP_MIN, HP_MAX);
        }
        if(AbstractDungeon.ascensionLevel >= 2)
        {
            this.damage.add(new DamageInfo(this, A2_BRUISE_DAMAGE));
            attackDamage = A2_BRUISE_DAMAGE;
        }
        else {
            this.damage.add(new DamageInfo(this, BRUISE_DAMAGE));
            attackDamage = BRUISE_DAMAGE;
        }
        this.loadAnimation(ATLAS, SKELETON, 0.75f);
        AnimationState.TrackEntry e = this.state.setAnimation(0, "animation0", true);
        e.setTimeScale(0.5f);
        e.setTime(e.getEndTime() * MathUtils.random());
    }
    
    @Override
    public void usePreBattleAction() {
        int damage = AbstractDungeon.ascensionLevel >= 2 ? A2_EXPLOSION_DAMAGE : EXPLOSION_DAMAGE;
        int trigger = AbstractDungeon.ascensionLevel >= 17 ? A17_TRIGGER : TRIGGER;
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this,
                new EruptPower(this, trigger, damage), trigger));
    }
    
    @Override
    public void takeTurn() {
        switch (this.nextMove) {
            case BRUISE:
                AbstractDungeon.actionManager.addToBottom(new AnimateFastAttackAction(this));
                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage.get(0), AbstractGameAction.AttackEffect.FIRE));
                break;
            case BOIL:
                AbstractDungeon.actionManager.addToBottom(new AnimateFastAttackAction(this));
                AbstractDungeon.actionManager.addToBottom(new MakeTempCardInDrawPileAction(new Burn(), BOIL_BURNS, true, true));
                break;
        }
        AbstractDungeon.actionManager.addToBottom(new RollMoveAction(this));
    }
    
    @Override
    protected void getMove(final int num) {
        if (this.firstMove) {
            this.setMove(MOVES[0], BRUISE, Intent.ATTACK, attackDamage);
            this.firstMove = false;
        } else {
            if (this.lastMove(BRUISE)) {
                this.setMove(MOVES[1], BOIL, Intent.DEBUFF);
            } else {
                this.setMove(MOVES[0], BRUISE, Intent.ATTACK, attackDamage);
            }
        }
    }
}