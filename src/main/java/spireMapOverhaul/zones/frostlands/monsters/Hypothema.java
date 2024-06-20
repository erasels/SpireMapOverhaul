package spireMapOverhaul.zones.frostlands.monsters;

import basemod.abstracts.CustomMonster;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.AnimateSlowAttackAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.RollMoveAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.StrengthPower;
import spireMapOverhaul.SpireAnniversary6Mod;
import spireMapOverhaul.util.Wiz;
import spireMapOverhaul.zones.frostlands.powers.LeavePower;

import static spireMapOverhaul.SpireAnniversary6Mod.modID;

public class Hypothema extends CustomMonster
{
    public static final String ID = SpireAnniversary6Mod.makeID("Hypothema");
    private static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID);
    public static final String NAME = monsterStrings.NAME;
    public static final String[] MOVES = monsterStrings.MOVES;
    private static final String IMG = SpireAnniversary6Mod.makeImagePath("monsters/Frostlands/Hypothema.png");
    public int move = 0, buff = 0;
    private static final byte FIRST_MOVE = 0, PRIMARY = 1, SECONDARY = 2;
    private static final int HP = 30, DMG = 6;

    public Hypothema() {
        this(0.0f, 0.0f);
    }

    public Hypothema(final float x, final float y) {
        super(Hypothema.NAME, ID, HP, 20.0F, -10.0F, 145.0F, 286, IMG, x, y);
        loadAnimation(modID + "Resources/images/monsters/Frostlands/Hypothema.atlas", modID + "Resources/images/monsters/Frostlands/Hypothema.json", 1.75F);
        state.setAnimation(0, "idle", true);
        type = EnemyType.ELITE;
        buff = 0;
        int hp = HP;
        int dmg = DMG;
        if (AbstractDungeon.ascensionLevel >= 8)
            hp+=10;
        if (AbstractDungeon.ascensionLevel >= 3)
            dmg++;
        if (AbstractDungeon.ascensionLevel >= 18)
        {
            buff++;
            dmg++;
        }
        setHp(hp, hp+10);
        damage.add(new DamageInfo(this, dmg));
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
                Wiz.atb(new AnimateSlowAttackAction(this));
                Wiz.atb(new DamageAction(AbstractDungeon.player, damage.get(damage.size()-1), AbstractGameAction.AttackEffect.SLASH_HORIZONTAL));
                Wiz.atb(new DamageAction(AbstractDungeon.player, damage.get(damage.size()-1), AbstractGameAction.AttackEffect.SLASH_HORIZONTAL));
                break;
            case 2:
                //Speech bubble?
                for (AbstractMonster m: AbstractDungeon.getMonsters().monsters) {
                    if(!m.isDeadOrEscaped())
                        Wiz.atb(new ApplyPowerAction(m, this, new StrengthPower(m, 1+buff)));
                }
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
                setMove(MOVES[1], PRIMARY, Intent.ATTACK, damage.get(0).base, 2, true);
                break;
            case 2:
                setMove(MOVES[2], SECONDARY, Intent.BUFF);
                break;
            default:
                setMove(MOVES[0], FIRST_MOVE, Intent.UNKNOWN, 0);
        }
    }
}