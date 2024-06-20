package spireMapOverhaul.zones.frostlands.monsters;

import basemod.abstracts.CustomMonster;
import com.megacrit.cardcrawl.actions.animations.AnimateSlowAttackAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.RollMoveAction;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.WeakPower;
import spireMapOverhaul.SpireAnniversary6Mod;
import spireMapOverhaul.util.Wiz;
import spireMapOverhaul.zones.frostlands.powers.FrigidPower;
import spireMapOverhaul.zones.frostlands.powers.LeavePower;

import static spireMapOverhaul.SpireAnniversary6Mod.modID;

public class Cole extends CustomMonster
{
    public static final String ID = SpireAnniversary6Mod.makeID("Cole");
    private static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID);
    public static final String NAME = monsterStrings.NAME;
    public static final String[] MOVES = monsterStrings.MOVES;
    private static final String IMG = SpireAnniversary6Mod.makeImagePath("monsters/Frostlands/Cole.png");
    public int move = 0, buff = 0, blk = 0;
    private static final byte FIRST_MOVE = 0, PRIMARY = 1, SECONDARY = 2;
    private static final int HP = 30, BLK = 10;

    public Cole() {
        this(0.0f, 0.0f);
    }

    public Cole(final float x, final float y) {
        super(Cole.NAME, ID, HP, 20.0F, -10.0F, 171.0F, 283.0f, IMG, x, y);
        loadAnimation(modID + "Resources/images/monsters/Frostlands/Cole.atlas", modID + "Resources/images/monsters/Frostlands/Cole.json", 1.75F);
        state.setAnimation(0, "idle", true);
        type = EnemyType.ELITE;
        buff = 0;
        int hp = HP;
        blk = BLK;
        if (AbstractDungeon.ascensionLevel >= 8)
            hp+=10;
        if (AbstractDungeon.ascensionLevel >= 3)
            blk += 2;
        if (AbstractDungeon.ascensionLevel >= 18)
        {
            buff++;
            blk += 2;
        }
        setHp(hp, hp+10);
        move = AbstractDungeon.aiRng.random(1, 2);
    }

    @Override
    public void usePreBattleAction() {
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new LeavePower(this)));
    }

    @Override
    public void takeTurn() {
        switch (move)
        {
            case 1:
                for (AbstractMonster m: AbstractDungeon.getMonsters().monsters) {
                    if(!m.isDeadOrEscaped())
                        Wiz.atb(new GainBlockAction(m, blk));
                }
                break;
            case 2:
                Wiz.atb(new AnimateSlowAttackAction(this));
                Wiz.atb(new ApplyPowerAction(AbstractDungeon.player, this, new WeakPower(AbstractDungeon.player, 1+buff, true)));
                Wiz.atb(new ApplyPowerAction(AbstractDungeon.player, this, new FrigidPower(AbstractDungeon.player, 1)));
                break;
            default:
                //Speech bubble
        }

        move++;
        Wiz.atb(new RollMoveAction(this));
    }

    @Override
    protected void getMove(final int num) {
        while(move > 2)
            move -= 2;
        if(move < 0)
            move = 0;
        switch (move)
        {
            case 1:
                setMove(MOVES[1], PRIMARY, Intent.DEFEND);
                break;
            case 2:
                setMove(MOVES[2], SECONDARY, Intent.DEBUFF);
                break;
            default:
                setMove(MOVES[0], FIRST_MOVE, Intent.UNKNOWN, 0);
        }
    }
}