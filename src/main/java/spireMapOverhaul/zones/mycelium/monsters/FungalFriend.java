package spireMapOverhaul.zones.mycelium.monsters;

import com.badlogic.gdx.math.MathUtils;
import com.esotericsoftware.spine.AnimationState;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.AnimateFastAttackAction;
import com.megacrit.cardcrawl.actions.animations.TalkAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.BiteEffect;
import spireMapOverhaul.SpireAnniversary6Mod;
import spireMapOverhaul.abstracts.AbstractSMOMonster;
import spireMapOverhaul.zones.mycelium.powers.ToxinPower;

public class FungalFriend  extends AbstractSMOMonster {
    public static final String ID = SpireAnniversary6Mod.makeID("FungalFriend");
    private static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID);
    public static final String NAME = monsterStrings.NAME;
    public static final String[] MOVES = monsterStrings.MOVES;
    public static final String[] DIALOG = monsterStrings.DIALOG;
    private static final byte RESTORE = 0;
    private static final byte GUARD = 1;
    private static final byte POUNCE = 2;
    private static final int HP_MIN = 42;
    private static final int HP_MAX = 44;
    private static final int A7_HP_MIN = 45;
    private static final int A7_HP_MAX = 47;
    private static final int GUARD_DAMAGE = 10;
    private static final int A2_GUARD_DAMAGE =11;
    private static final int GUARD_BLOCK = 3;
    private static final int A7_GUARD_BLOCK = 5;
    private static final int POUNCE_DAMAGE = 12;
    private static final int A2_POUNCE_DAMAGE = 14;
    
    private static final int RESTORE_HEALING = 8;
    private static final int A17_RESTORE_HEALING = 12;
    private int guardDamage;
    private int pounceDamage;
    private boolean firstTurn = true;
    private static final String IMG = SpireAnniversary6Mod.makeImagePath("monsters/Mycelium/FungalFriend/skeleton.png");
    private static final String ATLAS = SpireAnniversary6Mod.makeImagePath("monsters/Mycelium/FungalFriend/skeleton.atlas");
    private static final String SKELETON = SpireAnniversary6Mod.makeImagePath("monsters/Mycelium/FungalFriend/skeleton.json");
    
    public FungalFriend(final float x, final float y) {
        super(NAME, ID, HP_MAX, 0, 0, 200, 240, IMG, x, y);
        if (AbstractDungeon.ascensionLevel >= 7) {
            this.setHp(A7_HP_MIN, A7_HP_MAX);
        } else {
            this.setHp(HP_MIN, HP_MAX);
        }
        if(AbstractDungeon.ascensionLevel >= 2) {
            this.damage.add(new DamageInfo(this, A2_GUARD_DAMAGE));
            guardDamage = A2_GUARD_DAMAGE;
            this.damage.add(new DamageInfo(this, A2_POUNCE_DAMAGE));
            pounceDamage = A2_POUNCE_DAMAGE;
        }
        else
        {
            this.damage.add(new DamageInfo(this, GUARD_DAMAGE));
            guardDamage = GUARD_DAMAGE;
            this.damage.add(new DamageInfo(this, POUNCE_DAMAGE));
            pounceDamage = POUNCE_DAMAGE;
        }
        this.loadAnimation(ATLAS, SKELETON, 1.00f);
        AnimationState.TrackEntry e = this.state.setAnimation(0, "idle", true);
        e.setTime(e.getEndTime() * MathUtils.random());
        this.stateData.setMix("attack", "idle", 0.1F);
    }
    
    @Override
    public void takeTurn() {
        switch (this.nextMove) {
            case RESTORE:
                for (AbstractMonster m : AbstractDungeon.getMonsters().monsters)
                    if (!m.isDying && !m.isEscaping) {
                        if(AbstractDungeon.ascensionLevel >= 17) {
                            AbstractDungeon.actionManager.addToBottom(new HealAction(m, this, A17_RESTORE_HEALING));
                        }
                        else
                        {
                            AbstractDungeon.actionManager.addToBottom(new HealAction(m, this, RESTORE_HEALING));
                        }
                    }
                break;
            case GUARD:
                AbstractDungeon.actionManager.addToBottom(new ChangeStateAction(this, "ATTACK"));
                AbstractDungeon.actionManager.addToBottom(new WaitAction(0.5F));
                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage.get(0), AbstractGameAction.AttackEffect.BLUNT_LIGHT));
                for (AbstractMonster m : AbstractDungeon.getMonsters().monsters)
                    if (!m.isDying && !m.isEscaping) {
                        if(AbstractDungeon.ascensionLevel >= 7) {
                            AbstractDungeon.actionManager.addToBottom(new GainBlockAction(m, this, A7_GUARD_BLOCK));
                        }
                        else
                        {
                            AbstractDungeon.actionManager.addToBottom(new GainBlockAction(m, this, GUARD_BLOCK));
                        }
                    }
                break;
            case POUNCE:
                AbstractDungeon.actionManager.addToBottom(new ChangeStateAction(this, "ATTACK"));
                AbstractDungeon.actionManager.addToBottom(new WaitAction(0.5F));
                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage.get(1), AbstractGameAction.AttackEffect.BLUNT_HEAVY));
                break;
        }
        AbstractDungeon.actionManager.addToBottom(new RollMoveAction(this));
    }
    
    public void changeState(String stateName) {
        if (stateName.equals("ATTACK")) {
            this.state.setAnimation(0, "attack", false);
            this.state.addAnimation(0, "idle", true, 0.0F);
        }
    }
    
    @Override
    protected void getMove(int i) {
        if(firstTurn || !(lastMove(RESTORE) || lastMoveBefore(RESTORE)))
        {
            this.setMove(RESTORE, Intent.BUFF);
            firstTurn = false;
        }
        else if ((i < 50 && !lastMove(GUARD)) || lastMove(POUNCE)) {
            this.setMove(GUARD, Intent.ATTACK_DEFEND, guardDamage);
        } else {
            this.setMove(POUNCE, Intent.ATTACK, pounceDamage);
        }
    }
}
