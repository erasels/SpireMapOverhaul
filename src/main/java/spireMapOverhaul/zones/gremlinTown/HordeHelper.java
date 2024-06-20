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
import spireMapOverhaul.zones.gremlinTown.monsters.ChubbyGremlin;
import spireMapOverhaul.zones.gremlinTown.monsters.GremlinHealer;
import spireMapOverhaul.zones.gremlinTown.monsters.GremlinRockTosser;
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
    private static float PLATFORM_X_LEFT;
    private static float PLATFORM_X_RIGHT;
    private static float RIGHT_X_FRONT;
    private static float RIGHT_X_BACK;
    private static float LEFT_X_FRONT;
    private static float LEFT_X_BACK;
    private static float PLATFORM_END_Y;
    private static float PLATFORM_START_Y;
    private static boolean reinforced;
    public static boolean needsUpdate = false;

    static {
        uiStrings = CardCrawlGame.languagePack.getUIString(HORDE_STRINGS);
        TEXT = uiStrings.TEXT;
        PLATFORM_IMG = new Texture(makeImagePath("vfx/Platform.png"));
    }

    public HordeHelper() {
    }

    public static String getCombatString() {
        if (GremlinTown.GREMLIN_HORDE.equals(AbstractDungeon.lastCombatMetricKey))
            return TEXT[3];
        else
            return null;
    }

    public static void initFight() {
        PLATFORM_X_LEFT = Settings.WIDTH * 0.25F;
        PLATFORM_X_RIGHT = Settings.WIDTH * 0.75F;
        RIGHT_X_FRONT = Settings.WIDTH * 0.7F;
        RIGHT_X_BACK = Settings.WIDTH * 0.82F;
        LEFT_X_FRONT = Settings.WIDTH * 0.3F;
        LEFT_X_BACK = Settings.WIDTH * 0.18F;
        PLATFORM_END_Y = Settings.HEIGHT - HB_HEIGHT/2F*Settings.scale - 50F*Settings.scale;
        PLATFORM_START_Y = Settings.HEIGHT + HB_HEIGHT/2F*Settings.scale + 10F;

        reinforced = false;
        platforms = false;
        needsUpdate = false;
        platform_Y = PLATFORM_START_Y;
        left_hb = new Hitbox(PLATFORM_X_LEFT, platform_Y, HB_WIDTH*Settings.scale, HB_HEIGHT*Settings.scale);
        right_hb = new Hitbox(PLATFORM_X_RIGHT, platform_Y, HB_WIDTH*Settings.scale, HB_HEIGHT*Settings.scale);

        groundQueue = new ArrayList<>();
        platformQueue = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            groundQueue.add(new GremlinWarrior(Settings.WIDTH * 4, 0));
            groundQueue.add(new GremlinWarrior(Settings.WIDTH * 4, 0));
            groundQueue.add(new GremlinThief(Settings.WIDTH * 4, 0));
            groundQueue.add(new GremlinFat(Settings.WIDTH * 4, 0));
        }
        groundQueue.add(new GremlinThief(Settings.WIDTH * 4, 0));
        groundQueue.add(new GremlinFat(Settings.WIDTH * 4, 0));

        platformQueue.add(new GremlinRockTosser(Settings.WIDTH * 4, 0));
        platformQueue.add(new GremlinWizard(Settings.WIDTH * 4, 0));
        platformQueue.add(new GremlinHealer(Settings.WIDTH * 4, 0));
        platformQueue.add(new GremlinWizard(Settings.WIDTH * 4, 0));
        platformQueue.add(new GremlinRockTosser(Settings.WIDTH * 4, 0));

        Collections.shuffle(groundQueue, AbstractDungeon.monsterRng.random);

        groundQueue.add(3, new ChubbyGremlin(Settings.WIDTH * 4, 0));
        groundQueue.add(9, new GremlinNob(Settings.WIDTH * 4, 0));

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
        needsUpdate = false;
        platform_Y = PLATFORM_START_Y;
        left_hb = new Hitbox(PLATFORM_X_LEFT, platform_Y, HB_WIDTH*Settings.scale, HB_HEIGHT*Settings.scale);
        right_hb = new Hitbox(PLATFORM_X_RIGHT, platform_Y, HB_WIDTH* Settings.scale, HB_HEIGHT*Settings.scale);

        groundQueue = new ArrayList<>();
        platformQueue = new ArrayList<>();
    }

    public static void hidePlatforms() {
        platforms = false;
    }

    public static void update() {
        reinforced = false;
        needsUpdate = false;
        if (GameActionManager.turn == 1)
            surround();
        else if (GameActionManager.turn == 2) {
            reinforce();
            lowerPlatforms();
        } else {
            reinforce();
            if (reinforced)
                atb(new WaitMoreAction(1.5F));
        }
        calculateBackAttack();
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

        moveCharacterMiddle(monsterRightFront != null && !monsterRightFront.isDeadOrEscaped());

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

        if (monsterRightFront == null || monsterRightFront.isDeadOrEscaped())
            moveRightGremlinIn();
        else
            retreatGremlinFront(monsterRightFront);

        if (monsterRightBack == null || monsterRightBack.isDeadOrEscaped())
            moveRightGremlinIn();
        else
            retreatGremlinBack(monsterRightBack);

        atb(new WaitAction(0.1F));
        atb(new WaitAction(0.1F));
        atb(new WaitAction(0.1F));

        moveLeftGremlinIn(true);
        moveLeftGremlinIn();

        atb(new WaitMoreAction(2.5F));
    }

    public static void lowerPlatforms() {
        platforms = true;
        monsterLeftPlatform = platformQueue.get(0);
        platformQueue.remove(0);
        monsterRightPlatform = platformQueue.get(0);
        platformQueue.remove(0);

        monsterLeftPlatform.drawX = PLATFORM_X_LEFT;
        monsterRightPlatform.drawX = PLATFORM_X_RIGHT;
        monsterLeftPlatform.drawY = PLATFORM_START_Y;
        monsterRightPlatform.drawY = PLATFORM_START_Y;

        monsterLeftPlatform.flipHorizontal = true;
        monsterLeftPlatform.hb_x = -monsterLeftPlatform.hb_x;
        monsterLeftPlatform.intentOffsetX = -monsterLeftPlatform.intentOffsetX;
        ReflectionHacks.privateMethod(AbstractCreature.class, "refreshHitboxLocation").invoke(monsterLeftPlatform);

        atb(new SpawnMonsterAction(monsterLeftPlatform, false));
        atb(new AbstractGameAction() {
            @Override
            public void update() {
                isDone = true;
                monsterLeftPlatform.createIntent();
                monsterLeftPlatform.usePreBattleAction();
            }
        });
        atb(new SpawnMonsterAction(monsterRightPlatform, false));
        atb(new AbstractGameAction() {
            @Override
            public void update() {
                isDone = true;
                monsterRightPlatform.createIntent();
                monsterRightPlatform.usePreBattleAction();
            }
        });

        atb(new AbstractGameAction() {
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
                    monsterLeftPlatform.drawY = platform_Y - HB_HEIGHT/2F*Settings.scale + 56F*Settings.scale;
                    monsterRightPlatform.drawY = platform_Y - HB_HEIGHT/2F*Settings.scale + 56F*Settings.scale;
                }

                tickDuration();
                if (isDone) {
                    CardCrawlGame.music.unsilenceBGM();
                    AbstractDungeon.scene.fadeOutAmbiance();
                    CardCrawlGame.music.playPrecachedTempBgm();
                }
            }
        });
    }

    public static void reinforce() {
        if (platforms && (monsterLeftPlatform == null || monsterLeftPlatform.isDeadOrEscaped()))
            fillLeftPlatform();
        if (platforms && (monsterRightPlatform == null || monsterRightPlatform.isDeadOrEscaped()))
            fillRightPlatform();

        if (getNextGround() == null)
            return;

        moveRightGremlinUp();
        moveLeftGremlinUp();
        if (fillRight())
            fillRight();
        if (fillLeft())
            fillLeft();
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

    public static boolean fillLeft() {
        AbstractMonster m = getNextGround();
        if (m == null)
            return false;

        if (monsterLeftFront == null || monsterLeftFront.isDeadOrEscaped()) {
            moveLeftGremlinIn();
            return true;
        } else if (monsterLeftBack == null || monsterLeftBack.isDeadOrEscaped())
            moveLeftGremlinIn();
        return false;
    }

    public static boolean fillRight() {
        AbstractMonster m = getNextGround();
        if (m == null)
            return false;

        if (monsterRightFront == null || monsterRightFront.isDeadOrEscaped()) {
            moveRightGremlinIn();
            return true;
        } else if (monsterRightBack == null || monsterRightBack.isDeadOrEscaped())
            moveRightGremlinIn();
        return false;
    }

    public static void calculateBackAttack() {
        atb(new AbstractGameAction() {
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

        atb(new VFXAction(x, 0.6F));
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

        atb(new VFXAction(x));
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

        atb(new VFXAction(x));
    }

    public static void moveLeftGremlinUp() {
        if (monsterLeftFront != null && !monsterLeftFront.isDeadOrEscaped())
            return;
        if (monsterLeftBack == null || monsterLeftBack.isDeadOrEscaped())
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

        atb(new VFXAction(x));
    }

    public static void moveRightGremlinUp() {
        if (monsterRightFront != null && !monsterRightFront.isDeadOrEscaped())
            return;
        if (monsterRightBack == null || monsterRightBack.isDeadOrEscaped())
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

        atb(new VFXAction(x));
    }

    public static void moveLeftGremlinIn() {
        moveLeftGremlinIn(false);
    }

    public static void moveLeftGremlinIn(boolean shout) {
        AbstractMonster m = getNextGround();
        if (m == null)
            return;

        boolean far;
        if (monsterLeftFront == null || monsterLeftFront.isDeadOrEscaped()) {
            far = true;
            monsterLeftFront = m;
        } else if (monsterLeftBack == null || monsterLeftBack.isDeadOrEscaped()) {
            far = false;
            monsterLeftBack = m;
        } else
            return;

        reinforced = true;
        removeNextGround();

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

        atb(new SpawnMonsterAction(m, false));
        atb(new AbstractGameAction() {
            @Override
            public void update() {
                isDone = true;
                m.createIntent();
                m.usePreBattleAction();
            }
        });
        atb(new VFXAction(x));
    }

    public static void moveRightGremlinIn() {
        AbstractMonster m = getNextGround();
        if (m == null)
            return;

        boolean far;
        if (monsterRightFront == null || monsterRightFront.isDeadOrEscaped()) {
            far = true;
            monsterRightFront = m;
        } else if (monsterRightBack == null || monsterRightBack.isDeadOrEscaped()) {
            far = false;
            monsterRightBack = m;
        } else
            return;

        reinforced = true;
        removeNextGround();

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

        atb(new SpawnMonsterAction(m, false));
        atb(new AbstractGameAction() {
            @Override
            public void update() {
                isDone = true;
                m.createIntent();
                m.usePreBattleAction();
            }
        });
        atb(new VFXAction(x));
    }

    public static void fillLeftPlatform() {
        if (platformQueue.isEmpty())
            return;
        monsterLeftPlatform = platformQueue.get(0);
        platformQueue.remove(0);
        reinforced = true;

        final float start_y = Settings.HEIGHT*1.02F;

        monsterLeftPlatform.drawX = PLATFORM_X_LEFT;
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
                monsterLeftPlatform.drawY = Interpolation.linear.apply(start_y,
                        PLATFORM_END_Y - HB_HEIGHT/2F*Settings.scale + 56F*Settings.scale,
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

        atb(new SpawnMonsterAction(monsterLeftPlatform, false));
        atb(new AbstractGameAction() {
            @Override
            public void update() {
                isDone = true;
                monsterLeftPlatform.createIntent();
                monsterLeftPlatform.usePreBattleAction();
            }
        });
        atb(new VFXAction(x));
    }

    public static void fillRightPlatform() {
        if (platformQueue.isEmpty())
            return;
        monsterRightPlatform = platformQueue.get(0);
        platformQueue.remove(0);
        reinforced = true;

        final float start_y = Settings.HEIGHT*1.02F;

        monsterRightPlatform.drawX = PLATFORM_X_RIGHT;
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
                    monsterRightPlatform.drawY = Interpolation.linear.apply(start_y,
                            PLATFORM_END_Y - HB_HEIGHT/2F*Settings.scale + 56 * Settings.scale,
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

        atb(new SpawnMonsterAction(monsterRightPlatform, false));
        atb(new AbstractGameAction() {
            @Override
            public void update() {
                isDone = true;
                monsterRightPlatform.createIntent();
                monsterRightPlatform.usePreBattleAction();
            }
        });
        atb(new VFXAction(x));
    }

    public static void render(SpriteBatch sb) {
        if (!platforms)
            return;

        sb.setColor(Color.WHITE);

        sb.draw(PLATFORM_IMG, PLATFORM_X_LEFT - HB_WIDTH/2F, platform_Y - HB_HEIGHT/2F, HB_WIDTH/2F, HB_HEIGHT/2.0F, HB_WIDTH, HB_HEIGHT,
                Settings.scale, Settings.scale, 0F, 0, 0, (int)HB_WIDTH, (int)HB_HEIGHT, false, false);
        sb.draw(PLATFORM_IMG, PLATFORM_X_RIGHT - HB_WIDTH/2F, platform_Y - HB_HEIGHT/2F, HB_WIDTH/2F, HB_HEIGHT/2.0F, HB_WIDTH, HB_HEIGHT,
                Settings.scale, Settings.scale, 0F, 0, 0, (int)HB_WIDTH, (int)HB_HEIGHT, false, false);

        left_hb.render(sb);
        right_hb.render(sb);
    }
}
