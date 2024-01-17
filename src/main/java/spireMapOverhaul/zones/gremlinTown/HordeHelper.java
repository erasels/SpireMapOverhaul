package spireMapOverhaul.zones.gremlinTown;

import basemod.ReflectionHacks;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Interpolation;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.actions.common.RollMoveAction;
import com.megacrit.cardcrawl.actions.common.SpawnMonsterAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.exordium.*;
import com.megacrit.cardcrawl.powers.SurroundedPower;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import com.megacrit.cardcrawl.vfx.SpeechBubble;
import spireMapOverhaul.SpireAnniversary6Mod;
import spireMapOverhaul.actions.WaitMoreAction;
import spireMapOverhaul.util.Wiz;
import spireMapOverhaul.zones.gremlinTown.monsters.*;
import spireMapOverhaul.zones.gremlinTown.patches.HordePatches;

import java.util.ArrayList;
import java.util.Collections;

import static spireMapOverhaul.SpireAnniversary6Mod.makeImagePath;
import static spireMapOverhaul.util.Wiz.*;

public class HordeHelper {
    private static ArrayList<AbstractMonster> groundQueue;
    private static ArrayList<AbstractMonster> platformQueue;
    private static final String HORDE_STRINGS = SpireAnniversary6Mod.makeID("Horde");
    private static final UIStrings uiStrings;
    private static final String[] TEXT;
    private static AbstractMonster monsterLeftFront;
    private static AbstractMonster monsterLeftBack;
    private static AbstractMonster monsterLeftPlatform;
    private static AbstractMonster monsterRightFront;
    private static AbstractMonster monsterRightBack;
    private static AbstractMonster monsterRightPlatform;
    private static AbstractMonster monsterRightThree;
    private static AbstractMonster monsterRightFour;
    private static boolean platforms;
    private static final Texture PLATFORM_IMG;
    private static float platform_Y;
    private static Hitbox left_hb;
    private static Hitbox right_hb;
    private static final float HB_WIDTH = 400F;
    private static final float HB_HEIGHT = 341F;
    private static final float PLATFORM_X_LEFT = Settings.WIDTH * 0.3F - HB_WIDTH/2.0F*Settings.scale;
    private static final float PLATFORM_X_RIGHT = Settings.WIDTH * 0.7F - HB_WIDTH/2.0F*Settings.scale;
    private static final float RIGHT_X_FRONT = Settings.WIDTH * 0.7F;
    private static final float RIGHT_X_BACK = Settings.WIDTH * 0.82F;
    private static final float BRUTE_OFFSET = Settings.WIDTH * 0.06F;
    private static final float LEFT_X_FRONT = Settings.WIDTH * 0.3F;
    private static final float LEFT_X_BACK = Settings.WIDTH * 0.18F;
    private static final float PLATFORM_END_Y = Settings.HEIGHT - HB_HEIGHT * Settings.scale - 50F * Settings.scale;
    private static final float PLATFORM_START_Y = Settings.HEIGHT + 10F * Settings.scale;

    static {
        uiStrings = CardCrawlGame.languagePack.getUIString(HORDE_STRINGS);
        TEXT = uiStrings.TEXT;
        PLATFORM_IMG = new Texture(makeImagePath("vfx/Platform.png"));
    }

    public HordeHelper() {
    }

    public static void initFight() {
        platforms = false;
        platform_Y = PLATFORM_START_Y;
        left_hb = new Hitbox(PLATFORM_X_LEFT, platform_Y, HB_WIDTH, HB_HEIGHT);
        right_hb = new Hitbox(PLATFORM_X_RIGHT, platform_Y, HB_WIDTH, HB_HEIGHT);

        groundQueue = new ArrayList<>();
        platformQueue = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            groundQueue.add(new GremlinWarrior(Settings.WIDTH * 4, 0));
            groundQueue.add(new GremlinThief(Settings.WIDTH * 4, 0));
            groundQueue.add(new GremlinTsundere(Settings.WIDTH * 4, 0));
            groundQueue.add(new GremlinFat(Settings.WIDTH * 4, 0));
            platformQueue.add(new GremlinWizard(Settings.WIDTH * 4, 0));
            platformQueue.add(new GremlinRockTosser(Settings.WIDTH * 4, 0));
            if (i < 2)
                platformQueue.add(new GremlinRockTosser(Settings.WIDTH * 4, 0));
        }

        ArrayList<AbstractMonster> tempQueue = new ArrayList<>();
        for (int i = 0; i < 5; i++)
            tempQueue.add(new GremlinBrute(Settings.WIDTH * 4, 0));

        Collections.shuffle(tempQueue, AbstractDungeon.monsterRng.random);
        Collections.shuffle(groundQueue, AbstractDungeon.monsterRng.random);
        Collections.shuffle(platformQueue, AbstractDungeon.monsterRng.random);

        groundQueue.add(5, tempQueue.get(0));
        groundQueue.add(9, tempQueue.get(1));
        groundQueue.add(13, tempQueue.get(2));
        groundQueue.add(17, tempQueue.get(3));
        groundQueue.add(21, tempQueue.get(4));

        ArrayList<AbstractMonster> tempArray = new ArrayList<>(Wiz.getEnemies());
        tempArray.sort( (m1, m2) -> (int)(m1.hb.x - m2.hb.x) );

        monsterRightFront = tempArray.get(0);
        monsterRightBack = tempArray.get(1);
        monsterRightThree = tempArray.get(2);
        monsterRightFour = tempArray.get(3);

        for (AbstractMonster m : platformQueue)
            HordePatches.GremlinField.horde.set(m, true);
        for (AbstractMonster m : groundQueue)
            HordePatches.GremlinField.horde.set(m, true);
        for (AbstractMonster m : tempArray)
            HordePatches.GremlinField.horde.set(m, true);

        monsterLeftPlatform = null;
        monsterRightPlatform = null;

        monsterLeftFront = null;
        monsterLeftBack = null;

        CardCrawlGame.music.precacheTempBgm("BOSS_BOTTOM");
    }

    public static boolean areMonstersDead(boolean firstResult) {
        if (groundQueue == null || platformQueue == null || (groundQueue.isEmpty() && platformQueue.isEmpty()))
            return firstResult;
        return false;
    }

    public static boolean areMonstersBasicallyDead(boolean firstResult) {
        if (groundQueue == null || platformQueue == null || (groundQueue.isEmpty() && platformQueue.isEmpty()))
            return firstResult;
        return false;
    }

    public static void onVictory() {
        AbstractDungeon.scene.fadeInAmbiance();
        CardCrawlGame.music.fadeOutTempBGM();

        platforms = false;
        platform_Y = PLATFORM_START_Y;
        left_hb = new Hitbox(PLATFORM_X_LEFT, platform_Y, HB_WIDTH, HB_HEIGHT);
        right_hb = new Hitbox(PLATFORM_X_RIGHT, platform_Y, HB_WIDTH, HB_HEIGHT);

        groundQueue = new ArrayList<>();
        platformQueue = new ArrayList<>();
    }

    public static void hidePlatforms() {
        platforms = false;
    }

    public static void update() {
        calculateBackAttack();
        if (GameActionManager.turn == 2)
            surround();
        else if (GameActionManager.turn == 4) {
            lowerPlatforms();
            reinforce();
        } else
            reinforce();
    }

    public static void surround() {
        ArrayList<AbstractMonster> tempArray = new ArrayList<>(Wiz.getEnemies());
        tempArray.sort( (m1, m2) -> (int)(m1.hb.x - m2.hb.x) );

        if (!tempArray.isEmpty())
            monsterRightFront = tempArray.get(0);
        else
            monsterRightFront = null;

        if (tempArray.size() > 1)
            monsterRightBack = tempArray.get(1);
        else
            monsterRightBack = null;

        if (tempArray.size() > 2)
            monsterRightThree = tempArray.get(2);
        else
            monsterRightThree = null;

        if (tempArray.size() > 3)
            monsterRightFour = tempArray.get(3);
        else
            monsterRightFour = null;

        att(new AbstractGameAction() {
            @Override
            public void update() {
                isDone = true;
                //These are all add to top, so reverse order
                att(new WaitMoreAction(2.5F));

                moveLeftGremlinIn(false, false);
                moveLeftGremlinIn(true, true);

                // WaitMoreAction not stacking is a pain in the ass
                att(new WaitAction(0.1F));
                att(new WaitAction(0.1F));
                att(new WaitAction(0.1F));

                boolean movedFrontIn = false;
                boolean movedBackIn = false;
                if (monsterRightBack == null) {
                    moveRightGremlinIn(false);
                    movedBackIn = true;
                }
                if (monsterRightFront == null) {
                    moveRightGremlinIn(true);
                    movedFrontIn = true;
                }

                if (!movedBackIn)
                    retreatGremlinBack(monsterRightBack);
                if (!movedFrontIn)
                    retreatGremlinFront(monsterRightFront);

                if (monsterRightFour != null) {
                    att(new AbstractGameAction() {
                        @Override
                        public void update() {
                            isDone = true;
                            monsterRightFour.intent = AbstractMonster.Intent.ESCAPE;
                            monsterRightFour.escape();
                        }
                    });
                }
                if (monsterRightThree != null) {
                    att(new AbstractGameAction() {
                        @Override
                        public void update() {
                            isDone = true;
                            monsterRightThree.intent = AbstractMonster.Intent.ESCAPE;
                            monsterRightThree.escape();
                        }
                    });
                }

                moveCharacterMiddle(!movedFrontIn);
            }
        });
    }

    public static void lowerPlatforms() {
        platforms = true;
        monsterLeftPlatform = platformQueue.get(0);
        platformQueue.remove(0);
        monsterRightPlatform = platformQueue.get(0);
        platformQueue.remove(0);

        monsterLeftPlatform.drawX = PLATFORM_X_LEFT + HB_WIDTH/2.0F*Settings.scale;
        monsterRightPlatform.drawX = PLATFORM_X_RIGHT + HB_WIDTH/2.0F*Settings.scale;
        monsterLeftPlatform.drawY = PLATFORM_START_Y + 56*Settings.scale;
        monsterRightPlatform.drawY = PLATFORM_START_Y + 56*Settings.scale;

        monsterLeftPlatform.flipHorizontal = true;
        monsterLeftPlatform.hb_x = -monsterLeftPlatform.hb_x;
        monsterLeftPlatform.intentOffsetX = -monsterLeftPlatform.intentOffsetX;
        ReflectionHacks.privateMethod(AbstractCreature.class, "refreshHitboxLocation").invoke(monsterLeftPlatform);

        att(new AbstractGameAction() {
            boolean first = true;
            boolean lower = false;
            @Override
            public void update() {
                if (first) {
                    first = false;
                    duration = 4.0F;
                    CardCrawlGame.music.silenceBGM();
                } else if (!lower && duration < 2.0F) {
                    lower = true;
                    CardCrawlGame.sound.play(GremlinTown.PLATFORM_KEY);
                }
                if (duration < 2.0F) {
                    platform_Y = Interpolation.linear.apply(PLATFORM_START_Y, PLATFORM_END_Y, (2.0F - duration) / 2.0F);
                    left_hb.move(PLATFORM_X_LEFT, platform_Y);
                    right_hb.move(PLATFORM_X_RIGHT, platform_Y);
                    monsterLeftPlatform.drawY = platform_Y + 56F*Settings.scale;
                    monsterRightPlatform.drawY = platform_Y + 56F*Settings.scale;
                }

                tickDuration();
                if (isDone) {
                    CardCrawlGame.music.unsilenceBGM();
                    AbstractDungeon.scene.fadeOutAmbiance();
                    CardCrawlGame.music.playPrecachedTempBgm();
                }
            }
        });
        att(new AbstractGameAction() {
            @Override
            public void update() {
                isDone = true;
                monsterLeftPlatform.createIntent();
                monsterLeftPlatform.usePreBattleAction();
            }
        });
        att(new RollMoveAction(monsterLeftPlatform));
        att(new SpawnMonsterAction(monsterLeftPlatform, false));
        att(new AbstractGameAction() {
            @Override
            public void update() {
                isDone = true;
                monsterRightPlatform.createIntent();
                monsterRightPlatform.usePreBattleAction();
            }
        });
        att(new RollMoveAction(monsterRightPlatform));
        att(new SpawnMonsterAction(monsterRightPlatform, false));
    }

    public static void reinforce() {
        SpireAnniversary6Mod.logger.info("REINFORCE");
        if (platforms && (monsterLeftPlatform == null || monsterLeftPlatform.isDeadOrEscaped()))
            fillLeftPlatform();
        if (platforms && (monsterRightPlatform == null || monsterRightPlatform.isDeadOrEscaped()))
            fillRightPlatform();

        int leftSpots = 0;
        int rightSpots = 0;

        if (groundQueue.isEmpty())
            return;

        if (monsterRightFront == null || monsterRightFront.isDeadOrEscaped())
            rightSpots++;
        if (monsterRightBack == null || monsterRightBack.isDeadOrEscaped())
            rightSpots++;
        if (monsterLeftFront == null || monsterLeftFront.isDeadOrEscaped())
            leftSpots++;
        if (monsterLeftBack == null || monsterLeftBack.isDeadOrEscaped())
            leftSpots++;
        if (monsterRightFront instanceof GremlinBrute && !monsterRightFront.isDeadOrEscaped())
            rightSpots = 0;
        if (monsterLeftFront instanceof GremlinBrute && !monsterLeftFront.isDeadOrEscaped())
            leftSpots = 0;

        SpireAnniversary6Mod.logger.info("RIGHT SPOTS " + rightSpots);
        SpireAnniversary6Mod.logger.info("LEFT SPOTS " + leftSpots);

        if (leftSpots + rightSpots == 0)
            return;

        boolean brute = getNextGround() instanceof GremlinBrute;
        if (leftSpots + rightSpots == 1 && brute) {
            if (leftSpots == 1)
                fillLeft();
            else
                fillRight();
        } else if (brute) {
            if (leftSpots == 2 && rightSpots < 2) {
                moveLeftGremlinIn(true, false);
                if (rightSpots > 0)
                    fillRight();
            }
            else if (rightSpots == 2 && leftSpots < 2) {
                moveRightGremlinIn(true);
                if (leftSpots > 0)
                    fillLeft();
            } else {
                int x = AbstractDungeon.monsterRng.random(0, 1);
                if (x == 0) {
                    if (leftSpots == 1) {
                        moveLeftGremlinAcross();
                        moveLeftGremlinIn(true, false);
                    } else {
                        moveLeftGremlinIn(true, false);
                        fillRight();
                    }
                } else {
                    if (rightSpots == 1) {
                        moveRightGremlinAcross();
                        moveRightGremlinIn(true);
                    } else {
                        moveRightGremlinIn(true);
                        fillLeft();
                    }
                }
            }
        } else {
            if (rightSpots > 0 && leftSpots == 0) {
                fillRight();
            } else if (rightSpots == 0 && leftSpots > 0) {
                fillLeft();
            } else {
                int x = AbstractDungeon.monsterRng.random(0, 1);
                if (x == 0) {
                    fillLeft();
                    if (getNextGround() instanceof GremlinBrute && rightSpots == 2) {
                        moveRightGremlinIn(true);
                        fillLeft();
                    } else
                        fillRight();
                } else {
                    fillRight();
                    if (getNextGround() instanceof GremlinBrute && leftSpots == 2) {
                        moveLeftGremlinIn(true, false);
                        fillRight();
                    } else
                        fillLeft();
                }
            }
        }
    }

    public static AbstractMonster getNextGround() {
        if (groundQueue.size() > (platformQueue.size() * 2) / 3 )
            return groundQueue.get(0);
        else if (!platformQueue.isEmpty())
            return platformQueue.get(0);
        else
            return null;
    }

    public static void removeNextGround() {
        if (groundQueue.size() > (platformQueue.size() * 2) / 3 )
            groundQueue.remove(0);
        else if (!platformQueue.isEmpty())
            platformQueue.remove(0);
    }

    public static void fillLeft() {
        SpireAnniversary6Mod.logger.info("FILL LEFT");
        if (getNextGround() == null)
            return;
        if (monsterLeftFront != null && !monsterLeftFront.isDeadOrEscaped() && monsterLeftFront instanceof GremlinBrute)
            return;

        if (getNextGround() == null || getNextGround() instanceof GremlinBrute) {
            if (monsterLeftFront == null || monsterLeftFront.isDeadOrEscaped())
                moveLeftGremlinUp();
            return;
        }

        if (monsterLeftFront == null || monsterLeftFront.isDeadOrEscaped()) {
            if (monsterLeftBack != null && !monsterLeftBack.isDeadOrEscaped()) {
                moveLeftGremlinUp();
                moveLeftGremlinIn(false, false);
            }
            else {
                moveLeftGremlinIn(true, false);
                if (groundQueue.size() == 0 || getNextGround() instanceof GremlinBrute)
                    return;
                moveLeftGremlinIn(false, false);
            }
        } else if ((monsterLeftBack == null || monsterLeftBack.isDeadOrEscaped()))
            moveLeftGremlinIn(false, false);
    }

    public static void fillRight() {
        SpireAnniversary6Mod.logger.info("FILL RIGHT");
        if (getNextGround() == null)
            return;
        if (monsterRightFront != null && !monsterRightFront.isDeadOrEscaped() && monsterRightFront instanceof GremlinBrute)
            return;

        if (getNextGround() == null || getNextGround() instanceof GremlinBrute) {
            if (monsterRightFront == null || monsterRightFront.isDeadOrEscaped())
                moveRightGremlinUp();
            return;
        }

        if (monsterRightFront == null || monsterRightFront.isDeadOrEscaped()) {
            if (monsterRightBack != null && !monsterRightBack.isDeadOrEscaped()) {
                moveRightGremlinUp();
                moveRightGremlinIn(false);
            }
            else {
                moveRightGremlinIn(true);
                if (groundQueue.size() == 0 || getNextGround() instanceof GremlinBrute)
                    return;
                moveRightGremlinIn(false);
            }
        } else if ((monsterRightBack == null || monsterRightBack.isDeadOrEscaped()))
            moveRightGremlinIn(false);
    }

    public static void calculateBackAttack() {
        att(new AbstractGameAction() {
            @Override
            public void update() {
                isDone = true;
                boolean left = false;
                boolean right = false;
                for (AbstractMonster m : Wiz.getEnemies()) {
                    if (m.drawX < adp().drawX)
                        left = true;
                    else if (m.drawX > adp().drawX)
                        right = true;
                }
                if (left && right && !adp().hasPower(SurroundedPower.POWER_ID))
                    applyToSelfTop(new SurroundedPower(adp()));
                if (!left || !right) {
                    for (AbstractMonster m : Wiz.getEnemies())
                        m.removeSurroundedPower();
                    if (adp().hasPower(SurroundedPower.POWER_ID))
                        att(new RemoveSpecificPowerAction(adp(), adp(), SurroundedPower.POWER_ID));
                    if (left)
                        adp().flipHorizontal = true;
                    else if (right)
                        adp().flipHorizontal = false;
                }
            }
        });
    }

    public static void moveCharacterMiddle(boolean shout) {
        AbstractGameEffect x = new AbstractGameEffect() {
            private final float start_x = adp().drawX;
            private final float end_x = Settings.WIDTH/2.0F;
            boolean first = true;
            final float DURATION = 1.0F;
            @Override
            public void update() {
                duration -= Gdx.graphics.getDeltaTime();

                if (first) {
                    first = false;
                    adp().dialogX = adp().drawX;
                    duration = DURATION;
                }
                float x = Interpolation.linear.apply(start_x, end_x, (DURATION - duration)/DURATION);
                adp().movePosition(x, adp().drawY);

                if (duration < 0.0F) {
                    if (shout)
                        AbstractDungeon.effectsQueue.add(0, new SpeechBubble(adp().dialogX, adp().dialogY,
                                2.5F, TEXT[1], true));
                    isDone = true;
                }

                for (int i = 0; i < adp().orbs.size(); i++)
                    adp().orbs.get(i).setSlot(i, adp().maxOrbs);
            }

            @Override
            public void render(SpriteBatch spriteBatch) {
            }

            @Override
            public void dispose() {
            }
        };

        att(new VFXAction(x, 0.8F));
    }

    public static void retreatGremlinFront(AbstractMonster m) {
        final float start_x = m.drawX;
        AbstractGameEffect x = new AbstractGameEffect() {
            boolean first = true;
            final float DURATION = 0.6F;

            @Override
            public void update() {
                if (first) {
                    first = false;
                    duration = DURATION;
                } else
                    duration -= Gdx.graphics.getDeltaTime();

                m.drawX = Interpolation.linear.apply(start_x, RIGHT_X_FRONT, (DURATION - duration)/DURATION);

                if (duration < 0.0F)
                    isDone = true;
            }

            @Override
            public void render(SpriteBatch spriteBatch) {
            }

            @Override
            public void dispose() {
            }
        };

        att(new VFXAction(x));
    }

    public static void retreatGremlinBack(AbstractMonster m) {
        final float start_x = m.drawX;
        AbstractGameEffect x = new AbstractGameEffect() {
            boolean first = true;
            final float DURATION = 0.6F;

            @Override
            public void update() {

                if (first) {
                    first = false;
                    duration = DURATION;
                } else
                    duration -= Gdx.graphics.getDeltaTime();

                m.drawX = Interpolation.linear.apply(start_x, RIGHT_X_BACK, (DURATION - duration)/DURATION);

                if (duration < 0.0F) {
                    AbstractDungeon.effectsQueue.add(0, new SpeechBubble(m.hb.cX + m.dialogX, m.hb.cY + m.dialogY, 2.5F, TEXT[0], false));
                    isDone = true;
                }
            }

            @Override
            public void render(SpriteBatch spriteBatch) {
            }

            @Override
            public void dispose() {
            }
        };

        att(new VFXAction(x));
    }

    public static void moveLeftGremlinUp() {
        SpireAnniversary6Mod.logger.info("LEFT GREMLIN UP");
        if (monsterLeftFront != null && !monsterLeftFront.isDeadOrEscaped())
            return;

        monsterLeftFront = monsterLeftBack;
        monsterLeftBack = null;
        AbstractGameEffect x = new AbstractGameEffect() {
            private final float start_x = monsterLeftFront.drawX;
            private final float end_x = LEFT_X_FRONT;
            boolean first = true;
            @Override
            public void update() {
                duration -= Gdx.graphics.getDeltaTime();

                if (first) {
                    first = false;
                    duration = 0.4F;
                }
                monsterLeftFront.drawX = Interpolation.linear.apply(start_x, end_x, (0.4F - duration)/0.4F);

                if (duration < 0.0F)
                    isDone = true;
            }

            @Override
            public void render(SpriteBatch spriteBatch) {
            }

            @Override
            public void dispose() {
            }
        };

        att(new VFXAction(x));
    }

    public static void moveRightGremlinUp() {
        SpireAnniversary6Mod.logger.info("RIGHT GREMLIN UP");
        if (monsterRightFront != null && !monsterRightFront.isDeadOrEscaped())
            return;

        monsterRightFront = monsterRightBack;
        monsterRightBack = null;
        AbstractGameEffect x = new AbstractGameEffect() {
            private final float start_x = monsterRightFront.drawX;
            private final float end_x = RIGHT_X_FRONT;
            boolean first = true;
            @Override
            public void update() {
                duration -= Gdx.graphics.getDeltaTime();

                if (first) {
                    first = false;
                    duration = 0.4F;
                }
                monsterRightFront.drawX = Interpolation.linear.apply(start_x, end_x, (0.4F - duration)/0.4F);

                if (duration < 0.0F)
                    isDone = true;
            }

            @Override
            public void render(SpriteBatch spriteBatch) {
            }

            @Override
            public void dispose() {
            }
        };

        att(new VFXAction(x));
    }

    public static void moveLeftGremlinIn(boolean far, boolean shout) {
        SpireAnniversary6Mod.logger.info("LEFT GREMLIN IN");
        AbstractMonster m = getNextGround();
        if (m == null)
            return;
        removeNextGround();

        if (far)
            monsterLeftFront = m;
        else
            monsterLeftBack = m;
        m.flipHorizontal = true;
        m.hb_x = -m.hb_x;
        m.intentOffsetX = -m.intentOffsetX;

        float start_x = -200.0F*Settings.scale;
        m.drawX = -200.0F*Settings.scale;

        m.drawY = AbstractDungeon.floorY + AbstractDungeon.miscRng.random(-30.0F, 30.0F);

        AbstractGameEffect x = new AbstractGameEffect() {
            boolean first = true;
            float DURATION;
            float end_x = 0;

            @Override
            public void update() {
                duration -= Gdx.graphics.getDeltaTime();

                if (first) {
                    first = false;

                    if (far)
                        DURATION = 1.6F;
                    else
                        DURATION = 1.2F;
                    duration = DURATION;

                    if (far)
                        end_x = LEFT_X_FRONT;
                    else
                        end_x = LEFT_X_BACK;
                    if (m instanceof GremlinBrute)
                        end_x -= BRUTE_OFFSET;
                }
                m.drawX = Interpolation.linear.apply(start_x, end_x, (DURATION - duration)/DURATION);

                if (duration < 0.0F) {
                    isDone = true;
                    if (shout)
                        AbstractDungeon.effectsQueue.add(0, new SpeechBubble(m.hb.cX + m.dialogX, m.hb.cY + m.dialogY, 2.5F, TEXT[2], false));
                }
            }

            @Override
            public void render(SpriteBatch spriteBatch) {
            }

            @Override
            public void dispose() {
            }
        };

        att(new VFXAction(x));
        att(new AbstractGameAction() {
            @Override
            public void update() {
                isDone = true;
                m.createIntent();
                m.usePreBattleAction();
            }
        });
        att(new SpawnMonsterAction(m, false));
    }

    public static void moveRightGremlinIn(boolean far) {
        SpireAnniversary6Mod.logger.info("RIGHT GREMLIN IN");
        AbstractMonster m = getNextGround();
        if (m == null)
            return;
        removeNextGround();

        if (far)
            monsterRightFront = m;
        else
            monsterRightBack = m;

        float start_x = Settings.WIDTH + 200.0F*Settings.scale;

        m.drawY = AbstractDungeon.floorY + AbstractDungeon.miscRng.random(-30.0F, 30.0F);

        AbstractGameEffect x = new AbstractGameEffect() {
            boolean first = true;
            float DURATION;
            float end_x = 0;
            @Override
            public void update() {
                duration -= Gdx.graphics.getDeltaTime();

                if (first) {
                    first = false;
                    if (far)
                        DURATION = 1.2F;
                    else
                        DURATION = 0.8F;
                    duration = DURATION;

                    if (far)
                        end_x = RIGHT_X_FRONT;
                    else
                        end_x = RIGHT_X_BACK;
                    if (m instanceof GremlinBrute)
                        end_x += BRUTE_OFFSET;
                }
                m.drawX = Interpolation.linear.apply(start_x, end_x, (DURATION - duration)/DURATION);

                if (duration < 0.0F)
                    isDone = true;
            }

            @Override
            public void render(SpriteBatch spriteBatch) {
            }

            @Override
            public void dispose() {
            }
        };

        att(new VFXAction(x));
        att(new AbstractGameAction() {
            @Override
            public void update() {
                isDone = true;
                m.createIntent();
                m.usePreBattleAction();
            }
        });
        att(new SpawnMonsterAction(m, false));
    }

    public static void fillLeftPlatform() {
        SpireAnniversary6Mod.logger.info("FILL LEFT PLATFORM");
        if (platformQueue.isEmpty())
            return;
        monsterLeftPlatform = platformQueue.get(0);
        platformQueue.remove(0);

        final float start_y = Settings.HEIGHT*1.02F;

        monsterLeftPlatform.drawX = PLATFORM_X_LEFT + HB_WIDTH/2.0F*Settings.scale;
        monsterLeftPlatform.drawY = start_y;
        monsterLeftPlatform.flipHorizontal = true;
        monsterLeftPlatform.hb_x = -monsterLeftPlatform.hb_x;
        monsterLeftPlatform.intentOffsetX = -monsterLeftPlatform.intentOffsetX;

        AbstractGameEffect x = new AbstractGameEffect() {
            boolean first = true;
            float DURATION;
            @Override
            public void update() {
                duration -= Gdx.graphics.getDeltaTime();

                if (first) {
                    first = false;
                    DURATION = 1.0F;
                    duration = DURATION;
                }
                monsterLeftPlatform.drawY = Interpolation.linear.apply(start_y, PLATFORM_END_Y + 56F*Settings.scale,
                        (DURATION - duration)/DURATION);

                if (duration < 0.0F)
                    isDone = true;
            }

            @Override
            public void render(SpriteBatch spriteBatch) {
            }

            @Override
            public void dispose() {
            }
        };

        att(new VFXAction(x));
        att(new AbstractGameAction() {
            @Override
            public void update() {
                isDone = true;
                monsterLeftPlatform.createIntent();
                monsterLeftPlatform.usePreBattleAction();
            }
        });
        att(new SpawnMonsterAction(monsterLeftPlatform, false));
    }

    public static void fillRightPlatform() {
        SpireAnniversary6Mod.logger.info("FILL RIGHT PLATFORM");
        if (platformQueue.isEmpty())
            return;
        monsterRightPlatform = platformQueue.get(0);
        platformQueue.remove(0);

        final float start_y = Settings.HEIGHT*1.02F;

        monsterRightPlatform.drawX = PLATFORM_X_RIGHT + HB_WIDTH/2.0F*Settings.scale;
        monsterRightPlatform.drawY = start_y;

        AbstractGameEffect x = new AbstractGameEffect() {
            boolean first = true;
            float DURATION;
            @Override
            public void update() {
                duration -= Gdx.graphics.getDeltaTime();

                if (first) {
                    first = false;
                    DURATION = 1.0F;
                    duration = DURATION + 0.15F;
                }
                if (duration <= DURATION) {
                    monsterRightPlatform.drawY = Interpolation.linear.apply(start_y, PLATFORM_END_Y + 56 * Settings.scale,
                            (DURATION - duration) / DURATION);
                }

                if (duration < 0.0F)
                    isDone = true;
            }

            @Override
            public void render(SpriteBatch spriteBatch) {
            }

            @Override
            public void dispose() {
            }
        };

        att(new VFXAction(x));
        att(new AbstractGameAction() {
            @Override
            public void update() {
                isDone = true;
                monsterRightPlatform.createIntent();
                monsterRightPlatform.usePreBattleAction();
            }
        });
        att(new SpawnMonsterAction(monsterRightPlatform, false));
    }

    public static void moveLeftGremlinAcross() {
        SpireAnniversary6Mod.logger.info("MOVE LEFT ACROSS");
        AbstractMonster m;
        if (monsterLeftFront != null && !monsterLeftFront.isDeadOrEscaped()) {
            m = monsterLeftFront;
            monsterLeftFront = null;
        }
        else if (monsterLeftBack != null && !monsterLeftBack.isDeadOrEscaped()) {
            m = monsterLeftBack;
            monsterLeftBack = null;
        }
        else
            return;

        final float end_x;

        if (monsterRightFront == null || monsterRightFront.isDeadOrEscaped()) {
            monsterRightFront = m;
            end_x = RIGHT_X_FRONT;
        } else if (monsterRightBack == null || monsterRightBack.isDeadOrEscaped()) {
            monsterRightBack = m;
            end_x = RIGHT_X_BACK;
        }
        else
            return;

        final float start_x = m.drawX;

        AbstractGameEffect x = new AbstractGameEffect() {
            boolean first = true;
            float DURATION = 0;
            @Override
            public void update() {
                duration -= Gdx.graphics.getDeltaTime();

                if (first) {
                    first = false;
                    DURATION = (end_x - start_x)/Settings.WIDTH*4.3F;
                    duration = DURATION;
                }
                m.drawX = Interpolation.linear.apply(start_x, end_x, (DURATION - duration)/DURATION);

                if (duration < 0.0F) {
                    isDone = true;
                    m.flipHorizontal = false;
                    m.hb_x = -m.hb_x;
                    m.intentOffsetX = -m.intentOffsetX;
                    calculateBackAttack();
                }
            }

            @Override
            public void render(SpriteBatch spriteBatch) {
            }

            @Override
            public void dispose() {
            }
        };

        att(new VFXAction(x));
    }

    public static void moveRightGremlinAcross() {
        SpireAnniversary6Mod.logger.info("MOVE RIGHT ACROSS");
        AbstractMonster m;
        if (monsterRightFront != null && !monsterRightFront.isDeadOrEscaped()) {
            m = monsterRightFront;
            monsterRightFront = null;
        }
        else if (monsterRightBack != null && !monsterRightBack.isDeadOrEscaped()) {
            m = monsterRightBack;
            monsterRightBack = null;
        }
        else
            return;

        final float end_x;

        if (monsterLeftFront == null || monsterLeftFront.isDeadOrEscaped()) {
            monsterLeftFront = m;
            end_x = LEFT_X_FRONT;
        } else if (monsterLeftBack == null || monsterLeftBack.isDeadOrEscaped()) {
            monsterLeftBack = m;
            end_x = LEFT_X_BACK;
        }
        else
            return;

        final float start_x = m.drawX;

        AbstractGameEffect x = new AbstractGameEffect() {
            boolean first = true;
            float DURATION = 0;
            @Override
            public void update() {
                duration -= Gdx.graphics.getDeltaTime();

                if (first) {
                    first = false;
                    DURATION = (start_x - end_x)/Settings.WIDTH*4.3F;
                    duration = DURATION;
                }
                m.drawX = Interpolation.linear.apply(start_x, end_x, (DURATION - duration)/DURATION);

                if (duration < 0.0F) {
                    isDone = true;
                    m.flipHorizontal = true;
                    m.hb_x = -m.hb_x;
                    m.intentOffsetX = -m.intentOffsetX;
                    calculateBackAttack();
                }
            }

            @Override
            public void render(SpriteBatch spriteBatch) {
            }

            @Override
            public void dispose() {
            }
        };

        att(new VFXAction(x));
    }

    public static void render(SpriteBatch sb) {
        if (!platforms)
            return;

        sb.setColor(Color.WHITE);

        sb.draw(PLATFORM_IMG, PLATFORM_X_LEFT, platform_Y, HB_WIDTH/2F, HB_HEIGHT/2.0F, HB_WIDTH, HB_HEIGHT,
                Settings.scale, Settings.scale, 0F, 0, 0, (int)HB_WIDTH, (int)HB_HEIGHT, false, false);
        sb.draw(PLATFORM_IMG, PLATFORM_X_RIGHT, platform_Y, HB_WIDTH/2F, HB_HEIGHT/2.0F, HB_WIDTH, HB_HEIGHT,
                Settings.scale, Settings.scale, 0F, 0, 0, (int)HB_WIDTH, (int)HB_HEIGHT, false, false);

        left_hb.render(sb);
        right_hb.render(sb);
    }
}
