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
import com.megacrit.cardcrawl.powers.AngerPower;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.megacrit.cardcrawl.vfx.AwakenedEyeParticle;
import spireMapOverhaul.SpireAnniversary6Mod;
import spireMapOverhaul.actions.WaitMoreAction;
import spireMapOverhaul.zones.gremlinTown.powers.GremlinNibPower;

import static spireMapOverhaul.util.Wiz.adp;
import static spireMapOverhaul.util.Wiz.atb;

public class GremlinNib extends CustomMonster
{
    public static final String ID = SpireAnniversary6Mod.makeID(GremlinNib.class.getSimpleName());
    public static final String NAME;
    private static final String SKELETON_ATLAS = SpireAnniversary6Mod.makeImagePath(
            "monsters/GremlinTown/GremlinNib/skeleton.atlas");
    private static final String SKELETON_JSON = SpireAnniversary6Mod.makeImagePath(
            "monsters/GremlinTown/GremlinNib/skeleton.json");
    public static final byte BELLOW = 1;
    public static final byte RUSH = 2;
    public static final byte CRIT = 3;
    private static final int HP_MIN = 121;
    private static final int HP_MAX = 127;
    private static final int A_2_HP_MIN = 130;
    private static final int A_2_HP_MAX = 136;
    private static final int CRIT_DMG = 40;
    private static final int RUSH_DMG = 14;
    private static final int A_3_CRIT_DMG = 45;
    private static final int A_3_RUSH_DMG = 16;
    private static final int ANGER_AMOUNT = 1;
    private static final int PEN_AMOUNT_A18 = 8;
    private static final int PEN_AMOUNT = 10;
    private static final int VULN_AMOUNT = 2;
    private final int rushDmg;
    public final int critDmg;
    private boolean usedBellow;

    public static boolean isWoke = false;
    public static Bone wokePosLeft;
    public static float wokeTimer = 0.0f;

    public GremlinNib(float x, float y) {
        super(NAME, "GremlinNib", HP_MAX, -70.0F, -10.0F, 270.0F, 380.0F, (String)null, x, y);
        usedBellow = false;
        intentOffsetX = -30.0F * Settings.scale;
        type = EnemyType.ELITE;
        dialogX = -60.0F * Settings.scale;
        dialogY = 50.0F * Settings.scale;
        if (AbstractDungeon.ascensionLevel >= 8) {
            setHp(A_2_HP_MIN, A_2_HP_MAX);
        } else {
            setHp(HP_MIN, HP_MAX);
        }

        if (AbstractDungeon.ascensionLevel >= 3) {
            critDmg = A_3_CRIT_DMG;
            rushDmg = A_3_RUSH_DMG;
        } else {
            critDmg = CRIT_DMG;
            rushDmg = RUSH_DMG;
        }

        damage.add(new DamageInfo(this, rushDmg));
        damage.add(new DamageInfo(this, critDmg));
        loadAnimation(SKELETON_ATLAS, SKELETON_JSON, 1.0F);
        AnimationState.TrackEntry e = state.setAnimation(0, "animation", true);
        e.setTime(e.getEndTime() * MathUtils.random());

        if(CardCrawlGame.isInARun()) {
            isWoke = false;
            wokePosLeft = skeleton.findBone("hornleft");
        }
    }

    public void takeTurn() {
        switch (nextMove) {
            case BELLOW:
                playSfx();
                atb(new TalkAction(this, DIALOG[0], 1.0F, 3.0F));
                atb(new ApplyPowerAction(this, this, new AngerPower(this, ANGER_AMOUNT)));
                if (AbstractDungeon.ascensionLevel >= 18)
                    atb(new ApplyPowerAction(this, this, new GremlinNibPower(this, PEN_AMOUNT_A18)));
                else
                    atb(new ApplyPowerAction(this, this, new GremlinNibPower(this, PEN_AMOUNT)));
                break;
            case RUSH:
                atb(new AnimateSlowAttackAction(this));
                atb(new DamageAction(adp(), damage.get(0), AbstractGameAction.AttackEffect.BLUNT_HEAVY));
                break;
            case CRIT:
                playSfx();
                atb(new TalkAction(this, DIALOG[0], 1.0F, 3.0F));
                atb(new WaitMoreAction(1f));
                atb(new AnimateSlowAttackAction(this));
                atb(new DamageAction(adp(), damage.get(1), AbstractGameAction.AttackEffect.BLUNT_HEAVY));
                atb(new ApplyPowerAction(adp(), this, new VulnerablePower(adp(), VULN_AMOUNT, true)));
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

    @Override
    public void setMove(String moveName, byte nm, Intent intent, int baseDamage, int multiplier, boolean isMultiDamage) {
        if (nm == CRIT)
            isWoke = true;
        else
            isWoke = false;

        super.setMove(moveName, nm, intent, baseDamage, multiplier, isMultiDamage);
    }

    protected void getMove(int num) {
        if (!usedBellow) {
            usedBellow = true;
            setMove(BELLOW, Intent.BUFF);
        } else
          setMove(RUSH, Intent.ATTACK, damage.get(0).base);
    }

    static {
        MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID);
        NAME = monsterStrings.NAME;
        MOVES = monsterStrings.MOVES;
        DIALOG = monsterStrings.DIALOG;
    }
}