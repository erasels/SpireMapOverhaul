package spireMapOverhaul.zones.volatileGrounds.monsters;

import basemod.abstracts.CustomMonster;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.AnimateFastAttackAction;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.beyond.Exploder;
import com.megacrit.cardcrawl.powers.ExplosivePower;
import com.megacrit.cardcrawl.powers.FlameBarrierPower;
import com.megacrit.cardcrawl.powers.MinionPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import spireMapOverhaul.SpireAnniversary6Mod;

import java.util.Iterator;

public class GremlinArchmage extends CustomMonster {
    public static final String ID = SpireAnniversary6Mod.makeID("GremlinArchmage");
    private static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID);
    public static final String NAME = monsterStrings.NAME;
    public static final String[] MOVES = monsterStrings.MOVES;
    private static final String TEMP_IMG = SpireAnniversary6Mod.makeImagePath("monsters/VolatileGrounds/GremlinArchmage/GremlinArchmage.png");
    //TODO: ADD ANIMATION
    //private static final String IMG = SpireAnniversary6Mod.makeImagePath("monsters/VolatileGrounds/GremlinArchmage/skeleton.png");
    //private static final String ATLAS = SpireAnniversary6Mod.makeImagePath("monsters/VolatileGrounds/GremlinArchmage/skeleton.atlas");
    //private static final String SKELETON = SpireAnniversary6Mod.makeImagePath("monsters/VolatileGrounds/GremlinArchmage/skeleton.json");
    private static final byte SUMMON = 0;
    private static final byte ATTACK = 1;
    private static final byte BLOCK = 2;
    private static final int ATTACK_DAMAGE_1 = 12;
    private static final int A2_ATTACK_DAMAGE_1 = 14;
    private static final int BLOCK_AMOUNT = 8;
    private static final int A17_BLOCK_AMOUNT = 12;
    private static final int A17_STRENGTH_ADD = 3;
    private static final int HP_MIN = 95;
    private static final int HP_MAX = 98;
    private static final int A7_HP_MIN = 110;
    private static final int A7_HP_MAX = 115;
    private int damage1;
    private int blockAmt;
    public AbstractMonster[]  exploders = new AbstractMonster[2];
    public static final float[] POSX;
    public boolean firstTurn = true;
    static
    {
        POSX = new float[]{-430, -200.0F};
    }
    
    public GremlinArchmage(final float x, final float y) {
        super(NAME, ID, HP_MAX, -5.0F, 0, 120.0f, 220f, TEMP_IMG, x, y);
        if (AbstractDungeon.ascensionLevel >= 7) {
            this.setHp(A7_HP_MIN, A7_HP_MAX);
        } else {
            this.setHp(HP_MIN, HP_MAX);
        }
        if(AbstractDungeon.ascensionLevel >= 2) {
            this.damage.add(new DamageInfo(this, ATTACK_DAMAGE_1));
            damage1 = ATTACK_DAMAGE_1;
        }
        else
        {
            this.damage.add(new DamageInfo(this, A2_ATTACK_DAMAGE_1));
            damage1 = A2_ATTACK_DAMAGE_1;
        }
        if(AbstractDungeon.ascensionLevel >= 17)
        {
            blockAmt = A17_BLOCK_AMOUNT;
        }
        else
        {
            blockAmt = BLOCK_AMOUNT;
        }
        
        //TODO: ADD ANIMATION
        /*
        this.loadAnimation(ATLAS, SKELETON, 1.00f);
        AnimationState.TrackEntry e = this.state.setAnimation(0, "Idle", true);
        e.setTime(e.getEndTime() * MathUtils.random());
        */
    }
    
    @Override
    public void usePreBattleAction() {
        for (AbstractMonster m:AbstractDungeon.getCurrRoom().monsters.monsters) {
            if (!m.id.equals(this.id)) {
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(m, m, new MinionPower(this)));
                if(AbstractDungeon.ascensionLevel >= 17)
                {
                    AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(m, this,
                            new StrengthPower(m, A17_STRENGTH_ADD)));
                }
            }
            if (m instanceof Exploder) {
                m.currentHealth *= 0.75;
                m.healthBarUpdatedEvent();
                exploders[AbstractDungeon.getMonsters().monsters.indexOf(m)] = m;
            }
        }
    }
    
    @Override
    public void takeTurn() {
        AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(this, this, FlameBarrierPower.POWER_ID));
        switch (this.nextMove) {
            case ATTACK:
                AbstractDungeon.actionManager.addToBottom(new AnimateFastAttackAction(this));
                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage.get(0), AbstractGameAction.AttackEffect.FIRE));
                break;
            case BLOCK:
                for (AbstractMonster m:AbstractDungeon.getCurrRoom().monsters.monsters) {
                    if (!m.isDying) {
                        AbstractDungeon.actionManager.addToBottom(new GainBlockAction(m, this, this.blockAmt));
                    }
                }
                break;
            case SUMMON:
                AbstractDungeon.actionManager.addToBottom(new WaitAction(0.5F));
                int spawns = 0;
                int i = 0;
    
                while(!(spawns >= 2 || i >= this.exploders.length)) {
                    if (this.exploders[i] == null || this.exploders[i].isDeadOrEscaped()) {
                        Exploder exploderToSpawn = new Exploder(POSX[i], 0);
                        exploders[i] = exploderToSpawn;
                        exploderToSpawn.currentHealth *= 0.75;
                        AbstractDungeon.actionManager.addToBottom(new SpawnMonsterAction(exploderToSpawn, true));
                        if(AbstractDungeon.ascensionLevel >= 17) {
                            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(exploderToSpawn, this,
                                    new StrengthPower(exploderToSpawn, A17_STRENGTH_ADD)));
                        }
                        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(exploderToSpawn, this,
                                new ExplosivePower(exploderToSpawn, 3)));
                        ++spawns;
                    }
                    ++i;
                }
                break;
        }
        AbstractDungeon.actionManager.addToBottom(new RollMoveAction(this));
    }
    
    @Override
    protected void getMove(final int num) {
        if(canSpawn() && !lastMove(SUMMON))
        {
            this.setMove(MOVES[0], SUMMON, Intent.UNKNOWN);
        }
        else if ((num < 50 && !lastTwoMoves(ATTACK) && !firstTurn) || lastTwoMoves(BLOCK)) {
            this.setMove(MOVES[1], ATTACK, Intent.ATTACK, damage1);
        
        } else {
            this.setMove(MOVES[2], BLOCK, Intent.DEFEND);
        }
        firstTurn = false;
    }
    
    private boolean canSpawn() {
        int aliveCount = 0;
        Iterator var2 = AbstractDungeon.getMonsters().monsters.iterator();
        
        while(var2.hasNext()) {
            AbstractMonster m = (AbstractMonster)var2.next();
            if (m != this && !m.isDying) {
                ++aliveCount;
            }
        }
        
        if (aliveCount >= 2) {
            return false;
        } else {
            return true;
        }
    }
    
    @Override
    public void die() {
        super.die();
        for (AbstractMonster m:AbstractDungeon.getCurrRoom().monsters.monsters) {
            if (!(m == this) && !m.isDying) {
                AbstractDungeon.actionManager.addToBottom(new EscapeAction(m));
            }
        }
    }
}