package spireMapOverhaul.zones.example.monsters;

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

import static spireMapOverhaul.util.Wiz.*;

public class DreadMoth extends CustomMonster
{
    public static final String ID = SpireAnniversary6Mod.makeID("DreadMoth");
    private static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID);
    public static final String NAME = monsterStrings.NAME;;
    public static final String[] MOVES = monsterStrings.MOVES;
    private static final String IMG = SpireAnniversary6Mod.makeImagePath("monsters/DreadMoth/DreadMoth.png");
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
        type = EnemyType.NORMAL;
        if (AbstractDungeon.ascensionLevel >= 7) {
            setHp(A7_HP);
            wingWardBlock = A7_WING_WARD_BLOCK;
        } else {
            setHp(HP);
            wingWardBlock = WING_WARD_BLOCK;
        }

        if (AbstractDungeon.ascensionLevel >= 2) {
            dreadWaveDamage = A2_DREAD_WAVE_DAMAGE;
        } else {
            dreadWaveDamage = DREAD_WAVE_DAMAGE;
        }
        damage.add(new DamageInfo(this, dreadWaveDamage));

        if (AbstractDungeon.ascensionLevel >= 17) {
            startingBlock = A17_STARTING_BLOCK;
        } else {
            startingBlock = STARTING_BLOCK;
        }
    }

    @Override
    public void usePreBattleAction() {
        atb(new ApplyPowerAction(this, this, new BeatOfDeathPower(this, BEAT_OF_DEATH_AMOUNT)));
        atb(new ApplyPowerAction(this, this, new BarricadePower(this)));
        atb(new GainBlockAction(this, startingBlock));
    }

    @Override
    public void takeTurn() {
        if (firstMove) {
            firstMove = false;
        }
        switch (nextMove) {
            case WING_WARD_MOVE:
                atb(new GainBlockAction(this, wingWardBlock));
                break;
            case DREAD_WAVE_ATTACK:
                atb(new VFXAction(this, new ShockWaveEffect(hb.cX, hb.cY, Settings.BLUE_TEXT_COLOR, ShockWaveEffect.ShockWaveType.CHAOTIC), 0.75F));
                atb(new DamageAction(AbstractDungeon.player, damage.get(0), AbstractGameAction.AttackEffect.NONE));
                break;
        }
        atb(new RollMoveAction(this));
    }

    @Override
    protected void getMove(final int num) {
        if (!lastMove(WING_WARD_MOVE) || !lastMoveBefore(WING_WARD_MOVE)) {
            setMove(MOVES[0], WING_WARD_MOVE, Intent.DEFEND);
        }
        else {
            setMove(MOVES[1], DREAD_WAVE_ATTACK, Intent.ATTACK, dreadWaveDamage);
        }
    }
}