package spireMapOverhaul.zones.gremlinTown.monsters;

import basemod.abstracts.CustomMonster;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.RollMoveAction;
import com.megacrit.cardcrawl.actions.utility.TextAboveCreatureAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.megacrit.cardcrawl.powers.WeakPower;
import spireMapOverhaul.SpireAnniversary6Mod;
import spireMapOverhaul.util.Wiz;
import spireMapOverhaul.zones.gremlinTown.events.Surprise;
import spireMapOverhaul.zones.gremlinTown.events.misc.Shell;

import static spireMapOverhaul.util.Wiz.*;

public class GremlinCannon extends CustomMonster
{
    public static final String ID = SpireAnniversary6Mod.makeID(GremlinCannon.class.getSimpleName());
    public static final String NAME;
    private static final String[] MOVES;
    private static final String[] DIALOG;
    private static final String IMAGE_PATH = SpireAnniversary6Mod.makeImagePath("events/GremlinTown/EventChestOpened.png");
    private boolean firstMove = true;
    private static final byte LOAD = 1;
    private static final byte FIRE_VULN = 2;
    private static final byte FIRE_WEAK = 3;
    private static final byte FIRE_EXPLODE = 4;
    private static final int MIN_HP = 48;
    private static final int MAX_HP = 53;
    private static final int MIN_HP_A7 = 52;
    private static final int MAX_HP_A7 = 58;
    private static final int VULN_AMOUNT = 3;
    private static final int VULN_AMOUNT_A17 = 4;
    private static final int WEAK_AMOUNT = 3;
    private static final int WEAK_AMOUNT_A17 = 4;
    private static final int DAMAGE = 10;
    private static final int DAMAGE_A2 = 12;
    private static final float SHELL_DURATION = 1.3F;
    private static final float SOUND_TIME = 0.15F;

    private Shell shell;
    private final int attackDamage;

    public GremlinCannon() {
        this(0.0f, 0.0f);
    }

    public GremlinCannon(final float x, final float y) {
        super(GremlinCannon.NAME, ID, MAX_HP, 0, 0, 300.0F, 256.0F, IMAGE_PATH, x, y);

        type = EnemyType.NORMAL;

        if (asc() >= 7)
            setHp(MIN_HP_A7, MAX_HP_A7);
        else
            setHp(MIN_HP, MAX_HP);

        if (asc() >= 2)
            attackDamage = DAMAGE_A2;
        else
            attackDamage = DAMAGE;

        damage.add(new DamageInfo(this, attackDamage));
    }

    @Override
    public void usePreBattleAction() {
        // source is monster but flagged false since applied on player turn
        atb(new ApplyPowerAction(adp(), this, new VulnerablePower(adp(), VULN_AMOUNT, false)));
    }

    @Override
    public void takeTurn() {
        switch (nextMove) {
            case LOAD:
                atb(new TextAboveCreatureAction(this, DIALOG[0]));
                break;
            case FIRE_VULN:
                atb(new AbstractGameAction() {
                    boolean started = false;
                    boolean fired = false;
                    @Override
                    public void update() {
                        if (!started) {
                            started = true;
                            duration = SHELL_DURATION;
                            CardCrawlGame.sound.play("BLUNT_HEAVY");
                        } else
                            tickDuration();

                        if (SHELL_DURATION - duration >= SOUND_TIME && !fired) {
                            fired = true;
                            shell = new Shell(1198F * Settings.scale,
                                    AbstractDungeon.floorY + 124F * Settings.scale,
                                    adp().hb.cX, adp().hb.y, Surprise.SHELL_FLIGHT_TIME);
                        } else if (fired) {
                            if (shell == null) {
                                isDone = true;
                                return;
                            }
                            shell.update();
                        }

                        if (duration < 0)
                            shell = null;
                    }
                });
                if (asc() <= 17)
                    atb(new ApplyPowerAction(adp(), this, new VulnerablePower(adp(), VULN_AMOUNT, true)));
                else
                    atb(new ApplyPowerAction(adp(), this, new VulnerablePower(adp(), VULN_AMOUNT_A17, true)));
                break;
            case FIRE_WEAK:
                atb(new AbstractGameAction() {
                    boolean started = false;
                    boolean fired = false;
                    @Override
                    public void update() {
                        if (!started) {
                            started = true;
                            duration = SHELL_DURATION;
                            CardCrawlGame.sound.play("BLUNT_HEAVY");
                        } else
                            tickDuration();

                        if (SHELL_DURATION - duration >= SOUND_TIME && !fired) {
                            fired = true;
                            shell = new Shell(1198F * Settings.scale,
                                    AbstractDungeon.floorY + 124F * Settings.scale,
                                    adp().hb.cX, adp().hb.y, Surprise.SHELL_FLIGHT_TIME);
                        } else if (fired) {
                            if (shell == null) {
                                isDone = true;
                                return;
                            }
                            shell.update();
                        }

                        if (duration < 0)
                            shell = null;
                    }
                });
                if (asc() <= 17)
                    atb(new ApplyPowerAction(adp(), this, new WeakPower(adp(), WEAK_AMOUNT, true)));
                else
                    atb(new ApplyPowerAction(adp(), this, new WeakPower(adp(), WEAK_AMOUNT_A17, true)));
                break;
            case FIRE_EXPLODE:
                atb(new AbstractGameAction() {
                    boolean started = false;
                    boolean fired = false;
                    @Override
                    public void update() {
                        if (!started) {
                            started = true;
                            duration = SHELL_DURATION;
                            CardCrawlGame.sound.play("BLUNT_HEAVY");
                        } else
                            tickDuration();

                        if (SHELL_DURATION - duration >= SOUND_TIME && !fired) {
                            fired = true;
                            shell = new Shell(1198F * Settings.scale,
                                    AbstractDungeon.floorY + 124F * Settings.scale,
                                    adp().hb.cX, adp().hb.cY, Surprise.SHELL_FLIGHT_TIME, true);
                        } else if (fired) {
                            if (shell == null) {
                                isDone = true;
                                return;
                            }
                            shell.update();
                        }

                        if (duration < 0)
                            shell = null;
                    }
                });
                atb(new DamageAction(adp(), damage.get(0), AbstractGameAction.AttackEffect.NONE));
        }
        atb(new RollMoveAction(this));
    }

    @Override
    protected void getMove(final int num) {
        boolean lastMonster = (Wiz.getEnemies().size() == 1);
        if (!lastMove(LOAD)) {
            setMove(LOAD, Intent.UNKNOWN);
        }
        else if (lastMonster)
            setMove(FIRE_EXPLODE, Intent.ATTACK, attackDamage);
        else if (lastMoveBefore(FIRE_WEAK))
            setMove(FIRE_VULN, Intent.DEBUFF);
        else
            setMove(FIRE_WEAK, Intent.DEBUFF);
    }

    @Override
    public void render(SpriteBatch sb) {
        super.render(sb);
        if (shell != null)
            shell.render(sb);
    }

    static {
        MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID);
        NAME = monsterStrings.NAME;
        MOVES = monsterStrings.MOVES;
        DIALOG = monsterStrings.DIALOG;
    }
}