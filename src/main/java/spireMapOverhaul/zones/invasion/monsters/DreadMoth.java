package spireMapOverhaul.zones.invasion.monsters;

import basemod.abstracts.CustomMonster;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.RollMoveAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.powers.BarricadePower;
import com.megacrit.cardcrawl.powers.BeatOfDeathPower;
import com.megacrit.cardcrawl.vfx.combat.ShockWaveEffect;
import spireMapOverhaul.SpireAnniversary6Mod;

public class DreadMoth extends CustomMonster {
    public static final String ID = SpireAnniversary6Mod.makeID("DreadMoth");
    private static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID);
    public static final String NAME = monsterStrings.NAME;
    public static final String[] MOVES = monsterStrings.MOVES;
    private static final String IMG = SpireAnniversary6Mod.makeImagePath("monsters/Invasion/DreadMoth/DreadMoth.png");
    private boolean firstMove = true;
    private static final byte WING_WARD_MOVE = 1;
    private static final byte DREAD_WAVE_ATTACK = 2;
    private static final int WING_WARD_BLOCK = 2;
    private static final int A7_WING_WARD_BLOCK = 3;
    private static final int DREAD_WAVE_DAMAGE = 5;
    private static final int A2_DREAD_WAVE_DAMAGE = 6;
    private static final int HP = 8;
    private static final int A7_HP = 9;
    private static final int STARTING_BLOCK = 4;
    private static final int A17_STARTING_BLOCK = 6;
    private static final int BEAT_OF_DEATH_AMOUNT = 1;
    private int wingWardBlock;
    private int dreadWaveDamage;
    private int startingBlock;

    public DreadMoth() {
        this(0.0f, 0.0f);
    }

    public DreadMoth(final float x, final float y) {
        super(DreadMoth.NAME, ID, HP, -5.0F, 0, 155.0f, 150.0f, IMG, x, y);
        this.type = EnemyType.NORMAL;
        if (AbstractDungeon.ascensionLevel >= 7) {
            this.setHp(A7_HP);
            this.wingWardBlock = A7_WING_WARD_BLOCK;
        } else {
            this.setHp(HP);
            this.wingWardBlock = WING_WARD_BLOCK;
        }

        if (AbstractDungeon.ascensionLevel >= 2) {
            this.dreadWaveDamage = A2_DREAD_WAVE_DAMAGE;
        } else {
            this.dreadWaveDamage = DREAD_WAVE_DAMAGE;
        }
        this.damage.add(new DamageInfo(this, this.dreadWaveDamage));

        if (AbstractDungeon.ascensionLevel >= 17) {
            this.startingBlock = A17_STARTING_BLOCK;
        } else {
            this.startingBlock = STARTING_BLOCK;
        }
    }

    @Override
    public void usePreBattleAction() {
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new BeatOfDeathPower(this, BEAT_OF_DEATH_AMOUNT)));
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new BarricadePower(this)));
        AbstractDungeon.actionManager.addToBottom(new GainBlockAction(this, this.startingBlock));
    }

    @Override
    public void takeTurn() {
        if (this.firstMove) {
            this.firstMove = false;
        }
        switch (this.nextMove) {
            case WING_WARD_MOVE:
                AbstractDungeon.actionManager.addToBottom(new GainBlockAction(this, this.wingWardBlock));
                break;
            case DREAD_WAVE_ATTACK:
                AbstractDungeon.actionManager.addToBottom(new VFXAction(this, new ShockWaveEffect(this.hb.cX, this.hb.cY, Settings.BLUE_TEXT_COLOR, ShockWaveEffect.ShockWaveType.CHAOTIC), 0.75F));
                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage.get(0), AbstractGameAction.AttackEffect.NONE));
                break;
        }
        AbstractDungeon.actionManager.addToBottom(new RollMoveAction(this));
    }

    @Override
    protected void getMove(final int num) {
        if (!this.lastMove(WING_WARD_MOVE) || !this.lastMoveBefore(WING_WARD_MOVE)) {
            this.setMove(MOVES[0], WING_WARD_MOVE, Intent.DEFEND);
        }
        else {
            this.setMove(MOVES[1], DREAD_WAVE_ATTACK, Intent.ATTACK, this.dreadWaveDamage);
        }
    }
}