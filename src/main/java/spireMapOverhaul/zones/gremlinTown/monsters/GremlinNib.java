package spireMapOverhaul.zones.gremlinTown.monsters;

import basemod.abstracts.CustomMonster;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.esotericsoftware.spine.AnimationState;
import com.esotericsoftware.spine.Bone;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.AnimateSlowAttackAction;
import com.megacrit.cardcrawl.actions.animations.TalkAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.RollMoveAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.megacrit.cardcrawl.vfx.AwakenedEyeParticle;
import spireMapOverhaul.SpireAnniversary6Mod;
import spireMapOverhaul.actions.WaitMoreAction;
import spireMapOverhaul.zones.gremlinTown.powers.GremlinNibPower;

import static spireMapOverhaul.util.Wiz.*;

public class GremlinNib extends CustomMonster
{
    public static final String ID = SpireAnniversary6Mod.makeID(GremlinNib.class.getSimpleName());
    public static final String NAME;
    private static final String[] MOVES;
    private static final String[] DIALOG;
    private static final String SKELETON_ATLAS = SpireAnniversary6Mod.makeImagePath(
            "monsters/GremlinTown/GremlinNib/skeleton.atlas");
    private static final String SKELETON_JSON = SpireAnniversary6Mod.makeImagePath(
            "monsters/GremlinTown/GremlinNib/skeleton.json");
    public static final byte SLAM = 1;
    public static final byte DOUBLE = 2;
    public static final byte FRENZY = 3;
    public static final byte CRIT = 4;
    private static final int HP_MIN = 171;
    private static final int HP_MAX = 180;
    private static final int HP_MIN_A7 = 185;
    private static final int HP_MAX_A7 = 195;
    private static final int SLAM_DMG = 25;
    private static final int DOUBLE_DMG = 10;
    private static final int FRENZY_DMG = 5;
    private static final int CRIT_DMG = 45;
    private static final int SLAM_DMG_A2 = 27;
    private static final int DOUBLE_DMG_A2 = 11;
    private static final int FRENZY_DMG_A2 = 6;
    private static final int CRIT_DMG_A2 = 50;
    private static final int VULN_AMOUNT = 3;
    private final int slamDmg;
    private final int doubleDmg;
    private final int frenzyDmg;
    public final int critDmg;

    public boolean isWoke = false;
    public static Bone wokePosLeft;
    public static float wokeTimer = 0.0f;

    private boolean firstMove;

    public GremlinNib(float x, float y) {
        super(NAME, ID, HP_MAX, -70.0F, -10.0F, 270.0F, 380.0F, null, x, y);
        intentOffsetX = -30.0F * Settings.scale;
        type = EnemyType.NORMAL;
        dialogX = -60.0F * Settings.scale;
        dialogY = 50.0F * Settings.scale;
        if (AbstractDungeon.ascensionLevel >= 7)
            setHp(HP_MIN_A7, HP_MAX_A7);
        else
            setHp(HP_MIN, HP_MAX);

        if (AbstractDungeon.ascensionLevel >= 2) {
            critDmg = CRIT_DMG_A2;
            slamDmg = SLAM_DMG_A2;
            doubleDmg = DOUBLE_DMG_A2;
            frenzyDmg = FRENZY_DMG_A2;
        } else {
            critDmg = CRIT_DMG;
            slamDmg = SLAM_DMG;
            doubleDmg = DOUBLE_DMG;
            frenzyDmg = FRENZY_DMG;
        }

        damage.add(new DamageInfo(this, slamDmg));
        damage.add(new DamageInfo(this, doubleDmg));
        damage.add(new DamageInfo(this, frenzyDmg));
        damage.add(new DamageInfo(this, critDmg));
        loadAnimation(SKELETON_ATLAS, SKELETON_JSON, 1.0F);
        AnimationState.TrackEntry e = state.setAnimation(0, "animation", true);
        e.setTime(e.getEndTime() * MathUtils.random());

        if(CardCrawlGame.isInARun()) {
            isWoke = false;
            wokePosLeft = skeleton.findBone("hornleft");
        }

        firstMove = true;
    }

    @Override
    public void usePreBattleAction() {
        if (asc() >= 17)
            atb(new ApplyPowerAction(this, this, new GremlinNibPower(this, 1)));
        else
            atb(new ApplyPowerAction(this, this, new GremlinNibPower(this, 0)));
    }

    public void takeTurn() {
        switch (nextMove) {
            case SLAM:
                atb(new AnimateSlowAttackAction(this));
                atb(new DamageAction(adp(), damage.get(0), AbstractGameAction.AttackEffect.BLUNT_HEAVY));
                break;
            case DOUBLE:
                atb(new AnimateSlowAttackAction(this));
                atb(new DamageAction(adp(), damage.get(1), AbstractGameAction.AttackEffect.SLASH_HEAVY));
                atb(new DamageAction(adp(), damage.get(1), AbstractGameAction.AttackEffect.SLASH_HEAVY));
                break;
            case FRENZY:
                atb(new AnimateSlowAttackAction(this));
                atb(new DamageAction(adp(), damage.get(2), AbstractGameAction.AttackEffect.BLUNT_LIGHT));
                atb(new DamageAction(adp(), damage.get(2), AbstractGameAction.AttackEffect.BLUNT_LIGHT));
                atb(new DamageAction(adp(), damage.get(2), AbstractGameAction.AttackEffect.BLUNT_LIGHT));
                break;
            case CRIT:
                playSfx();
                atb(new TalkAction(this, DIALOG[0], 1.0F, 3.0F));
                atb(new WaitMoreAction(1f));
                atb(new AnimateSlowAttackAction(this));
                atb(new DamageAction(adp(), damage.get(3), AbstractGameAction.AttackEffect.BLUNT_HEAVY));
                atb(new ApplyPowerAction(adp(), this, new VulnerablePower(adp(), VULN_AMOUNT, true)));
                isWoke = false;
                break;
        }

        atb(new RollMoveAction(this));
    }

    private void playSfx() {
        int roll = MathUtils.random(2);
        if (roll == 0) {
            atb(new SFXAction("VO_GREMLINNOB_1A"));
        } else if (roll == 1) {
            atb(new SFXAction("VO_GREMLINNOB_1B"));
        } else {
            atb(new SFXAction("VO_GREMLINNOB_1C"));
        }
    }

    @Override
    public void update() {
        if (!isDeadOrEscaped() && isWoke) {
            wokeTimer = wokeTimer - Gdx.graphics.getDeltaTime();
            if (wokeTimer < 0.0F) {
                wokeTimer = 0.1F;

                AbstractDungeon.effectList.add(new AwakenedEyeParticle(
                        skeleton.getX() + wokePosLeft.getWorldX() + 20.0f*Settings.scale,
                        skeleton.getY() + wokePosLeft.getWorldY() + 30.0f*Settings.scale));
            }
        }

        super.update();
    }

    protected void getMove(int num) {
        int nibs = 0;
        GremlinNibPower pow = (GremlinNibPower) this.getPower(GremlinNibPower.POWER_ID);
        if (pow != null)
            nibs = pow.amount2;

        if (nibs == 7) {
            setMove(DOUBLE, Intent.ATTACK, doubleDmg, 2, true);
        } else if (nibs == 8) {
            setMove(SLAM, Intent.ATTACK, slamDmg);
        } else if (firstMove || lastMove(CRIT)) {
            firstMove = false;
            int x = AbstractDungeon.monsterRng.random(0, 2);
            if (x == 0)
                setMove(SLAM, Intent.ATTACK, slamDmg);
            if (x == 1)
                setMove(DOUBLE, Intent.ATTACK, doubleDmg, 2, true);
            else
                setMove(FRENZY, Intent.ATTACK, frenzyDmg, 3, true);
        } else if (lastMove(SLAM)) {
            int x = AbstractDungeon.monsterRng.random(0, 1);
            if (x == 0)
                setMove(DOUBLE, Intent.ATTACK, doubleDmg, 2, true);
            else
                setMove(FRENZY, Intent.ATTACK, frenzyDmg, 3, true);
        } else if (lastMove(DOUBLE)) {
            int x = AbstractDungeon.monsterRng.random(0, 1);
            if (x == 0)
                setMove(SLAM, Intent.ATTACK, slamDmg);
            else
                setMove(FRENZY, Intent.ATTACK, frenzyDmg, 3, true);
        } else {
            int x = AbstractDungeon.monsterRng.random(0, 1);
            if (x == 0)
                setMove(DOUBLE, Intent.ATTACK, doubleDmg, 2, true);
            else
                setMove(SLAM, Intent.ATTACK, slamDmg);
        }
    }

    static {
        MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID);
        NAME = monsterStrings.NAME;
        MOVES = monsterStrings.MOVES;
        DIALOG = monsterStrings.DIALOG;
    }
}