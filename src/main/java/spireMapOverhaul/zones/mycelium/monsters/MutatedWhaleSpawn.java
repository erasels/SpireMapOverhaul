package spireMapOverhaul.zones.mycelium.monsters;

import com.badlogic.gdx.math.MathUtils;
import com.esotericsoftware.spine.AnimationState;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.AnimateFastAttackAction;
import com.megacrit.cardcrawl.actions.animations.TalkAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDrawPileAction;
import com.megacrit.cardcrawl.actions.common.RollMoveAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.status.Burn;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.vfx.combat.BiteEffect;
import spireMapOverhaul.SpireAnniversary6Mod;
import spireMapOverhaul.abstracts.AbstractSMOMonster;
import spireMapOverhaul.zones.mycelium.powers.ToxinPower;
import spireMapOverhaul.zones.volatileGrounds.powers.EruptPower;

public class MutatedWhaleSpawn extends AbstractSMOMonster {
    public static final String ID = SpireAnniversary6Mod.makeID("MutatedWhaleSpawn");
    private static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID);
    public static final String NAME = monsterStrings.NAME;
    public static final String[] MOVES = monsterStrings.MOVES;
    public static final String[] DIALOG = monsterStrings.DIALOG;
    private static final byte TOXINS = 0;
    private static final byte CHOMP = 1;
    private static final byte SCRATCH = 2;
    private static final int HP_MIN = 60;
    private static final int HP_MAX = 62;
    private static final int A7_HP_MIN = 64;
    private static final int A7_HP_MAX = 67;
    public static final int CHOMP_DAMAGE = 12;
    public static final int A2_CHOMP_DAMAGE = 13;
    public static final int SCRATCH_DAMAGE = 4;
    public static final int A2_SCRATCH_DAMAGE = 5;
    public static final int SCRATCH_COUNT = 3;
    public static final int TOXIN_DEBUFF_AMOUNT = 3;
    public static final int A17_DEBUFF_AMOUNT = 4;
    private int chompDamage;
    private int scratchDamage;
    private boolean firstTurn = true;
    private static final String IMG = SpireAnniversary6Mod.makeImagePath("monsters/Mycelium/MutatedWhaleSpawn/skeleton.png");
    private static final String ATLAS = SpireAnniversary6Mod.makeImagePath("monsters/Mycelium/MutatedWhaleSpawn/skeleton.atlas");
    private static final String SKELETON = SpireAnniversary6Mod.makeImagePath("monsters/Mycelium/MutatedWhaleSpawn/skeleton.json");
    
    public MutatedWhaleSpawn() {
        super(NAME, ID, HP_MAX, 0, 0, 240, 240, IMG, 0, 0);
        if (AbstractDungeon.ascensionLevel >= 7) {
            this.setHp(A7_HP_MIN, A7_HP_MAX);
        } else {
            this.setHp(HP_MIN, HP_MAX);
        }
        if(AbstractDungeon.ascensionLevel >= 2) {
            this.damage.add(new DamageInfo(this, A2_CHOMP_DAMAGE));
            chompDamage = A2_CHOMP_DAMAGE;
            this.damage.add(new DamageInfo(this, A2_SCRATCH_DAMAGE));
            scratchDamage = A2_SCRATCH_DAMAGE;
        }
        else
        {
            this.damage.add(new DamageInfo(this, CHOMP_DAMAGE));
            chompDamage = CHOMP_DAMAGE;
            this.damage.add(new DamageInfo(this, SCRATCH_DAMAGE));
            scratchDamage = SCRATCH_DAMAGE;
        }
        this.loadAnimation(ATLAS, SKELETON, 1.00f);
        AnimationState.TrackEntry e = this.state.setAnimation(0, "NewCollection.001", true);
        e.setTime(e.getEndTime() * MathUtils.random());
        this.dialogX = -120.0F * Settings.scale;
        this.dialogY = 150.0F * Settings.scale;
    }
    
    @Override
    public void takeTurn() {
        switch (this.nextMove) {
            case TOXINS:
                AbstractDungeon.actionManager.addToBottom(new TalkAction(this, DIALOG[0], 0.5F, 2.0F));
                if(AbstractDungeon.ascensionLevel >= 17) {
                    AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, this, new ToxinPower(AbstractDungeon.player, A17_DEBUFF_AMOUNT)));
                }
                else
                {
                    AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, this, new ToxinPower(AbstractDungeon.player, TOXIN_DEBUFF_AMOUNT)));
                }
                break;
            case CHOMP:
                AbstractDungeon.actionManager.addToBottom(new AnimateFastAttackAction(this));
                if (Settings.FAST_MODE) {
                    this.addToBot(new VFXAction(new BiteEffect(AbstractDungeon.player.hb.cX, AbstractDungeon.player.hb.cY - 40.0F * Settings.scale, Settings.GOLD_COLOR.cpy()), 0.1F));
                } else {
                    this.addToBot(new VFXAction(new BiteEffect(AbstractDungeon.player.hb.cX, AbstractDungeon.player.hb.cY - 40.0F * Settings.scale, Settings.GOLD_COLOR.cpy()), 0.3F));
                }
                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage.get(0), AbstractGameAction.AttackEffect.NONE));
                break;
            case SCRATCH:
                AbstractDungeon.actionManager.addToBottom(new AnimateFastAttackAction(this));
                for (int i = 0; i < SCRATCH_COUNT; i++) {
                    AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage.get(1), AbstractGameAction.AttackEffect.NONE));
                }
                break;
        }
        AbstractDungeon.actionManager.addToBottom(new RollMoveAction(this));
    }
    
    @Override
    protected void getMove(int i) {
        if(firstTurn)
        {
            this.setMove(TOXINS, Intent.STRONG_DEBUFF);
            firstTurn = false;
        }
        else if ((i < 50 && !lastTwoMoves(CHOMP)) || lastTwoMoves(SCRATCH)) {
            this.setMove(CHOMP, Intent.ATTACK, chompDamage);
        } else {
            this.setMove(SCRATCH, Intent.ATTACK, scratchDamage, SCRATCH_COUNT, true);
        }
    }
}
