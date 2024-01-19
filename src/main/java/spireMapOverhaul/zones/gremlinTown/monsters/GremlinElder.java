package spireMapOverhaul.zones.gremlinTown.monsters;

import basemod.abstracts.CustomMonster;
import com.badlogic.gdx.math.MathUtils;
import com.esotericsoftware.spine.AnimationState;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.AnimateSlowAttackAction;
import com.megacrit.cardcrawl.actions.animations.TalkAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.RollMoveAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.actions.utility.TextAboveCreatureAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.curses.Decay;
import com.megacrit.cardcrawl.cards.curses.Pain;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndObtainEffect;
import spireMapOverhaul.SpireAnniversary6Mod;
import spireMapOverhaul.zones.gremlinTown.powers.SpitefulPower;

import static spireMapOverhaul.util.Wiz.*;

public class GremlinElder extends CustomMonster
{
    public static final String ID = SpireAnniversary6Mod.makeID(GremlinElder.class.getSimpleName());
    public static final String NAME;
    private static final String[] MOVES;
    private static final String[] DIALOG;
    private static final String SKELETON_ATLAS = SpireAnniversary6Mod.makeImagePath(
            "monsters/GremlinTown/GremlinElder/skeleton.atlas");
    private static final String SKELETON_JSON = SpireAnniversary6Mod.makeImagePath(
            "monsters/GremlinTown/GremlinElder/skeleton.json");
    public static final byte SLAM = 1;
    public static final byte SLAP = 2;
    public static final byte CHARGE = 3;
    public static final byte DYING_CURSE = 4;
    private static final int HP_MIN = 223;
    private static final int HP_MAX = 231;
    private static final int HP_MIN_A8 = 242;
    private static final int HP_MAX_A8 = 251;
    private static final int SLAM_DMG = 22;
    private static final int SLAM_DMG_A3 = 24;
    private static final int SLAP_DMG = 12;
    private static final int SLAP_DMG_A3 = 13;

    private boolean firstTurn;
    private boolean curseTriggered;

    public GremlinElder(float x, float y) {
        super(NAME, ID, HP_MAX, 100.0F, -12.5F, 325.0F, 450.0F, null, x -87.5F, y);
        type = EnemyType.ELITE;
        dialogX = 0F * Settings.scale;
        dialogY = 125.0F * Settings.scale;

        firstTurn = true;

        if (AbstractDungeon.ascensionLevel >= 8) {
            setHp(HP_MIN_A8, HP_MAX_A8);
        } else {
            setHp(HP_MIN, HP_MAX);
        }

        int slamDmg;
        int slapDmg;
        if (AbstractDungeon.ascensionLevel >= 3) {
            slamDmg = SLAM_DMG_A3;
            slapDmg = SLAP_DMG_A3;
        } else {
            slamDmg = SLAM_DMG;
            slapDmg = SLAP_DMG;
        }

        damage.add(new DamageInfo(this, slamDmg));
        damage.add(new DamageInfo(this, slapDmg));

        loadAnimation(SKELETON_ATLAS, SKELETON_JSON, 0.4F);
        AnimationState.TrackEntry e = state.setAnimation(0, "animation", true);
        e.setTime(e.getEndTime() * MathUtils.random());
    }

    @Override
    public void usePreBattleAction() {
        applyToEnemy(this, new SpitefulPower(this));
    }

    public void takeTurn() {
        switch (nextMove) {
            case SLAM:
                atb(new AnimateSlowAttackAction(this));
                atb(new DamageAction(adp(), damage.get(0), AbstractGameAction.AttackEffect.BLUNT_HEAVY));
                break;
            case SLAP:
                atb(new AnimateSlowAttackAction(this));
                atb(new DamageAction(adp(), damage.get(1), AbstractGameAction.AttackEffect.BLUNT_HEAVY));
                atb(new ApplyPowerAction(adp(), this, new StrengthPower(adp(), -1)));
                break;
            case CHARGE:
                if (lastMoveBefore(CHARGE))
                    atb(new TalkAction(this, DIALOG[1], 0.3F, 3.0F));
                else
                    atb(new TalkAction(this, DIALOG[0], 0.3F, 3.0F));
                playSfx();
                break;
            case DYING_CURSE:
                atb(new TalkAction(this, DIALOG[2], 0.3F, 3.0F));
                playSfx();
                GremlinElder m = this;
                final boolean[] first = {true};
                atb(new AbstractGameAction() {
                    @Override
                    public void update() {
                        if (first[0]) {
                            first[0] = false;
                            duration = 1.5f;

                            AbstractDungeon.effectList.add(new ShowCardAndObtainEffect(new Decay(),
                                    (float)Settings.WIDTH * 0.3F, (float)Settings.HEIGHT / 2.0F));
                            if (asc() >= 18) {
                                AbstractDungeon.effectList.add(new ShowCardAndObtainEffect(new Pain(),
                                        (float) Settings.WIDTH * 0.5F, (float) Settings.HEIGHT / 2.0F));
                            } else {
                                AbstractDungeon.effectList.add(new ShowCardAndObtainEffect(new Decay(),
                                        (float) Settings.WIDTH * 0.5F, (float) Settings.HEIGHT / 2.0F));
                            }
                            AbstractDungeon.effectList.add(new ShowCardAndObtainEffect(new Decay(),
                                    (float)Settings.WIDTH * 0.7F, (float)Settings.HEIGHT / 2.0F));
                        }

                        tickDuration();
                    }
                });
                atb(new AbstractGameAction() {
                    @Override
                    public void update() {
                        isDone = true;
                        m.playDeathSfx();
                        m.die();
                    }
                });
        }

        atb(new RollMoveAction(this));
    }

    protected void getMove(int num) {
        if (firstTurn) {
            firstTurn = false;
            setMove(SLAM, Intent.ATTACK, damage.get(0).base);
        }
        else if (currentHealth > maxHealth/2f) {
            if (lastTwoMoves(SLAM))
                setMove(SLAP, Intent.ATTACK_DEBUFF, damage.get(1).base);
            else if (lastTwoMoves(SLAP))
                setMove(SLAM, Intent.ATTACK, damage.get(0).base);
            else if (num < 50)
                setMove(SLAM, Intent.ATTACK, damage.get(0).base);
            else
                setMove(SLAP, Intent.ATTACK_DEBUFF, damage.get(1).base);
        }
        else {
            if (lastTwoMoves(CHARGE))
                setMove(DYING_CURSE, Intent.STRONG_DEBUFF);
            else
                setMove(CHARGE, Intent.UNKNOWN);
        }
    }

    public void damage(DamageInfo info) {
        super.damage(info);
        if (!isDying && currentHealth <= maxHealth / 2 && nextMove != CHARGE && !curseTriggered) {
            setMove(CHARGE, Intent.UNKNOWN);
            createIntent();
            atb(new TextAboveCreatureAction(this, TextAboveCreatureAction.TextType.INTERRUPTED));
            curseTriggered = true;
        }
    }

    private void playSfx() {
        int roll = MathUtils.random(1);
        if (roll == 0)
            atb(new SFXAction("VO_GREMLINSPAZZY_1A"));
        else
            atb(new SFXAction("VO_GREMLINSPAZZY_1B"));
    }

    public void playDeathSfx() {
        int roll = MathUtils.random(2);
        if (roll == 0)
            CardCrawlGame.sound.play("VO_GREMLINFAT_2A");
        else if (roll == 1)
            CardCrawlGame.sound.play("VO_GREMLINFAT_2B");
        else
            CardCrawlGame.sound.play("VO_GREMLINFAT_2C");
    }

    static {
        MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID);
        NAME = monsterStrings.NAME;
        MOVES = monsterStrings.MOVES;
        DIALOG = monsterStrings.DIALOG;
    }
}