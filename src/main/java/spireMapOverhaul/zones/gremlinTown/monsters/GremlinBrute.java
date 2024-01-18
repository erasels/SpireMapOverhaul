package spireMapOverhaul.zones.gremlinTown.monsters;

import basemod.abstracts.CustomMonster;
import com.badlogic.gdx.math.MathUtils;
import com.esotericsoftware.spine.AnimationState;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.AnimateSlowAttackAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.RollMoveAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.powers.FrailPower;
import spireMapOverhaul.SpireAnniversary6Mod;

import static spireMapOverhaul.util.Wiz.*;

public class GremlinBrute extends CustomMonster
{
    public static final String ID = SpireAnniversary6Mod.makeID(GremlinBrute.class.getSimpleName());
    public static final String NAME;
    private static final String[] MOVES;
    private static final String[] DIALOG;
    private static final String SKELETON_ATLAS = SpireAnniversary6Mod.makeImagePath(
            "monsters/GremlinTown/GremlinNib/skeleton.atlas");
    private static final String SKELETON_JSON = SpireAnniversary6Mod.makeImagePath(
            "monsters/GremlinTown/GremlinNib/skeleton.json");
    public static final byte SLAM = 1;
    public static final byte BREAK = 2;
    private static final int HP_MIN = 67;
    private static final int HP_MAX = 73;
    private static final int HP_MIN_A8 = 72;
    private static final int HP_MAX_A8 = 78;
    private static final int SLAM_DMG = 10;
    private static final int BREAK_DMG = 6;
    private static final int SLAM_DMG_A3 = 12;
    private static final int BREAK_DMG_A3 = 7;
    private static final int FRAIL_AMOUNT = 2;
    private static final int FRAIL_AMOUNT_A18 = 3;
    private final int slamDmg;
    public final int breakDmg;

    private boolean firstMove;

    public GremlinBrute(float x, float y) {
        super(NAME, ID, HP_MAX, -70.0F*0.6F, -10.0F*0.6F, 270.0F*0.6F, 380.0F*0.6F, null, x, y);
        intentOffsetX = -30.0F * Settings.scale;
        type = EnemyType.NORMAL;
        dialogX = -60.0F * Settings.scale;
        dialogY = 50.0F * Settings.scale;
        if (AbstractDungeon.ascensionLevel >= 8)
            setHp(HP_MIN_A8, HP_MAX_A8);
        else
            setHp(HP_MIN, HP_MAX);

        if (AbstractDungeon.ascensionLevel >= 3) {
            breakDmg = BREAK_DMG_A3;
            slamDmg = SLAM_DMG_A3;
        } else {
            breakDmg = BREAK_DMG;
            slamDmg = SLAM_DMG;
        }

        damage.add(new DamageInfo(this, slamDmg));
        damage.add(new DamageInfo(this, breakDmg));
        
        this.loadAnimation("images/monsters/theBottom/nobGremlin/skeleton.atlas", "images/monsters/theBottom/nobGremlin/skeleton.json", 1F/0.6F);
        AnimationState.TrackEntry e = state.setAnimation(0, "animation", true);
        e.setTime(e.getEndTime() * MathUtils.random());

        firstMove = true;
    }

    public void takeTurn() {
        switch (nextMove) {
            case SLAM:
                atb(new AnimateSlowAttackAction(this));
                atb(new DamageAction(adp(), damage.get(0), AbstractGameAction.AttackEffect.BLUNT_HEAVY));
                break;
            case BREAK:
                atb(new AnimateSlowAttackAction(this));
                atb(new DamageAction(adp(), damage.get(1), AbstractGameAction.AttackEffect.SLASH_HEAVY));
                if (asc() >= 18)
                    atb(new ApplyPowerAction(adp(), this, new FrailPower(adp(), FRAIL_AMOUNT_A18, true)));
                else
                    atb(new ApplyPowerAction(adp(), this, new FrailPower(adp(), FRAIL_AMOUNT, true)));
                break;
        }

        atb(new RollMoveAction(this));
    }

    protected void getMove(int num) {
        if (firstMove || lastMove(BREAK)) {
            firstMove = false;
            setMove(SLAM, Intent.ATTACK, slamDmg);
        } else
            setMove(BREAK, Intent.ATTACK_DEBUFF, breakDmg);
    }

    static {
        MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID);
        NAME = monsterStrings.NAME;
        MOVES = monsterStrings.MOVES;
        DIALOG = monsterStrings.DIALOG;
    }
}