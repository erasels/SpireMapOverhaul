package spireMapOverhaul.zones.gremlinTown;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Interpolation;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.actions.common.SpawnMonsterAction;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.helpers.ModHelper;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.exordium.*;
import com.megacrit.cardcrawl.powers.BackAttackPower;
import com.megacrit.cardcrawl.powers.SlowPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.powers.SurroundedPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import com.megacrit.cardcrawl.vfx.SpeechBubble;
import spireMapOverhaul.SpireAnniversary6Mod;
import spireMapOverhaul.util.Wiz;
import spireMapOverhaul.zones.gremlinTown.monsters.*;

import java.util.ArrayList;
import java.util.Collections;

import static spireMapOverhaul.SpireAnniversary6Mod.makeImagePath;
import static spireMapOverhaul.util.Wiz.*;

public class HordeHelper {
    private static ArrayList<AbstractMonster> groundQueue;
    private static ArrayList<AbstractMonster> platformQueue;
    private static float timer;
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
    private static final float PLATFORM_X_LEFT = Settings.WIDTH * 0.3F;
    private static final float PLATFORM_X_RIGHT = Settings.WIDTH * 0.7F - 600*Settings.scale;
    private static float platform_Y;
    private static Hitbox left_hb;
    private static Hitbox right_hb;
    private static final float HB_WIDTH = 600F;
    private static final float HB_HEIGHT = 512F;
    private static final float LEFT_X_FRONT = Settings.WIDTH * 0.675F;
    private static final float LEFT_X_BACK = Settings.WIDTH * 0.85F;
    private static final float RIGHT_X_FRONT = Settings.WIDTH * 0.325F;
    private static final float RIGHT_X_BACK = Settings.WIDTH * 0.15F;
    private static final float PLATFORM_END_Y = Settings.HEIGHT - 510F * Settings.scale;

    static {
        uiStrings = CardCrawlGame.languagePack.getUIString(HORDE_STRINGS);
        TEXT = uiStrings.TEXT;
        PLATFORM_IMG = new Texture(makeImagePath("vfx/GremlinTown/Platform.png"));
    }

    public HordeHelper() {
    }

    public static void initFight() {
        platforms = false;
        platform_Y = Settings.HEIGHT*2.0F;
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
            if (i < 3)
                platformQueue.add(new GremlinRockTosser(Settings.WIDTH * 4, 0));
        }
        platformQueue.add(new GremlinHealer(Settings.WIDTH * 4, 0));

        ArrayList<AbstractMonster> tempQueue = new ArrayList<>();
        tempQueue.add(new GremlinBarbarian(Settings.WIDTH * 4, 0));
        tempQueue.add(new GremlinAssassin(Settings.WIDTH * 4, 0));
        tempQueue.add(new ChubbyGremlin(Settings.WIDTH * 4, 0));
        tempQueue.add(new ArmoredGremlin(Settings.WIDTH * 4, 0));

        Collections.shuffle(tempQueue, AbstractDungeon.monsterRng.random);
        Collections.shuffle(groundQueue, AbstractDungeon.monsterRng.random);
        Collections.shuffle(platformQueue, AbstractDungeon.monsterRng.random);

        groundQueue.add(5, tempQueue.get(0));
        groundQueue.add(9, tempQueue.get(1));
        groundQueue.add(13, tempQueue.get(2));
        groundQueue.add(17, tempQueue.get(3));

        ArrayList<AbstractMonster> tempArray = new ArrayList<>(Wiz.getEnemies());
        tempArray.sort( (m1, m2) -> (int)(m1.hb.x - m2.hb.x) );

        monsterRightFront = tempArray.get(0);
        monsterRightBack = tempArray.get(1);
        monsterRightThree = tempArray.get(2);
        monsterRightFour = tempArray.get(3);

        monsterLeftPlatform = null;
        monsterRightPlatform = null;

        monsterLeftFront = null;
        monsterLeftBack = null;

        CardCrawlGame.music.precacheTempBgm("BOSS_BOTTOM");
    }

    public static boolean areMonstersDead(boolean firstResult) {
        if (groundQueue.isEmpty() && platformQueue.isEmpty())
            return firstResult;
        return false;
    }

    public static boolean areMonstersBasicallyDead(boolean firstResult) {
        if (groundQueue.isEmpty() && platformQueue.isEmpty())
            return firstResult;
        return false;
    }

    public static void onVictory() {
        AbstractDungeon.scene.fadeInAmbiance();
        CardCrawlGame.music.fadeOutTempBGM();
    }

    public static void update() {
        if (GameActionManager.turn == 1)
            surround();
        else if (GameActionManager.turn == 3) {
            reinforce();
            lowerPlatforms();
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

        atb(new AbstractGameAction() {
            boolean first = true;
            boolean movePlayer = false;
            boolean moveLeft = false;
            @Override
            public void update() {
                if (first) {
                    duration = 2.0F;
                    first = false;

                    moveCharacterMiddle();
                    if (monsterRightBack != null)
                        retreatGremlinBack(monsterRightBack);
                    if (monsterRightFront != null)
                        retreatGremlinFront(monsterLeftBack);

                    if (monsterRightFour != null)
                        monsterRightFour.escape();
                    if (monsterRightThree != null)
                        monsterRightThree.escape();
                } else if (!movePlayer && duration < 1.7F) {
                    movePlayer = true;
                    moveCharacterMiddle();
                } else if (!moveLeft && duration < 1.4F) {
                    moveLeft = true;
                    moveLeftGremlinIn(true, true);
                    moveLeftGremlinIn(false, true);
                    if (monsterRightFront == null)
                        moveRightGremlinIn(true);
                    if (monsterRightBack == null)
                        moveRightGremlinIn(false);
                }
                tickDuration();

                if (isDone) {
                    applyToEnemyTop(monsterLeftBack, new BackAttackPower(monsterLeftBack));
                    applyToEnemyTop(monsterLeftFront, new BackAttackPower(monsterLeftFront));
                    applyToSelfTop(new SurroundedPower(adp()));
                    calculateBackAttack();
                    CardCrawlGame.music.unsilenceBGM();
                    AbstractDungeon.scene.fadeOutAmbiance();
                    CardCrawlGame.music.playPrecachedTempBgm();
                }
            }
        });
    }

    public static void lowerPlatforms() {
        platforms = true;
        monsterLeftPlatform = platformQueue.get(0);
        platformQueue.remove(0);
        monsterRightPlatform = platformQueue.get(0);
        platformQueue.remove(0);
        atb(new SpawnMonsterAction(monsterLeftPlatform, false));
        atb(new SpawnMonsterAction(monsterRightPlatform, false));
        atb(new AbstractGameAction() {
            boolean first = true;
            boolean lower = false;
            final float PLATFORM_START_Y = Settings.HEIGHT + 100F * Settings.scale;
            final float PLATFORM_X_LEFT = Settings.WIDTH * 0.3F;
            final float PLATFORM_X_RIGHT = Settings.WIDTH * 0.7F - 600*Settings.scale;
            @Override
            public void update() {
                isDone = true;
                if (first) {
                    CardCrawlGame.music.silenceBGM();
                    first = false;
                    duration = 3.0F;
                } else if (!lower && duration < 2.0F) {
                    lower = true;
                    CardCrawlGame.sound.play(GremlinTown.PLATFORM_KEY);
                    monsterLeftPlatform.drawX = PLATFORM_X_LEFT + HB_WIDTH/2.0F*Settings.scale - monsterLeftPlatform.hb.width/2.0F;
                    monsterRightPlatform.drawX = PLATFORM_X_RIGHT + HB_WIDTH/2.0F*Settings.scale - monsterLeftPlatform.hb.width/2.0F;
                }

                if (duration < 2.0F) {
                    platform_Y = Interpolation.linear.apply(PLATFORM_START_Y, PLATFORM_END_Y, (2.0F - duration) / 2.0F);
                    left_hb.move(PLATFORM_X_LEFT, platform_Y);
                    right_hb.move(PLATFORM_X_RIGHT, platform_Y);
                    monsterLeftPlatform.drawY = platform_Y + 80F*Settings.scale;
                    monsterRightPlatform.drawY = platform_Y + 80F*Settings.scale;
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

        if (monsterRightFront == null || monsterRightFront.isDeadOrEscaped()) {
            if (monsterRightBack != null && !monsterRightBack.isDeadOrEscaped()) {
                moveRightGremlinUp();
                moveRightGremlinIn(false);
            }
            else {
                moveRightGremlinIn(true);
                moveRightGremlinIn(false);
            }
        } else if (monsterRightBack == null || monsterRightBack.isDeadOrEscaped())
            moveRightGremlinIn(false);

        if (monsterLeftFront == null || monsterLeftFront.isDeadOrEscaped()) {
            if (monsterLeftBack != null && !monsterLeftBack.isDeadOrEscaped()) {
                moveLeftGremlinUp();
                moveLeftGremlinIn(false, false);
            }
            else {
                moveLeftGremlinIn(true, false);
                moveLeftGremlinIn(false, false);
            }
        } else if (monsterLeftBack == null || monsterLeftBack.isDeadOrEscaped())
            moveLeftGremlinIn(false, false);

        calculateBackAttack();
    }

    public static void calculateBackAttack() {
        boolean left = false;
        boolean right = false;
        for (AbstractMonster m : Wiz.getEnemies()) {
            if (m.drawX < adp().drawX)
                left = true;
            else if (m.drawX > adp().drawX)
                right = true;
        }
        if (left && right && !adp().hasPower(SurroundedPower.POWER_ID)) {
            applyToSelf(new SurroundedPower(adp()));
            adp().hand.refreshHandLayout();
        }
        if (!left || !right) {
            atb(new RemoveSpecificPowerAction(adp(), adp(), SurroundedPower.POWER_ID));
            for (AbstractMonster m : Wiz.getEnemies()) {
                m.removeSurroundedPower();
            }
            if (left)
                adp().flipHorizontal = true;
            else if (right)
                adp().flipHorizontal = false;
        }
    }

    public static void moveCharacterMiddle() {
        AbstractDungeon.effectsQueue.add(new AbstractGameEffect() {
            private final float start_x = adp().drawX;
            private final float end_x = Settings.WIDTH/2.0F;
            boolean first = true;
            @Override
            public void update() {
                duration -= Gdx.graphics.getDeltaTime();

                if (first) {
                    first = false;
                    adp().dialogX = adp().drawX;
                    duration = 1.0F;
                }
                adp().drawX = Interpolation.linear.apply(start_x, end_x, 1.0F - duration);

                if (duration < 0.0F) {
                    AbstractDungeon.effectsQueue.add(new SpeechBubble(adp().dialogX, adp().dialogY, 2.5F, TEXT[1], true));
                    isDone = true;
                }
            }

            @Override
            public void render(SpriteBatch spriteBatch) {
            }

            @Override
            public void dispose() {
            }
        });
    }

    public static void retreatGremlinFront(AbstractMonster m) {
        AbstractDungeon.effectsQueue.add(new AbstractGameEffect() {
            private final float start_x = m.drawX;
            private final float end_x = RIGHT_X_FRONT;
            boolean first = true;
            @Override
            public void update() {
                duration -= Gdx.graphics.getDeltaTime();

                if (first) {
                    first = false;
                    duration = 1.0F;
                }
                m.drawX = Interpolation.linear.apply(start_x, end_x, 1.0F - duration);

                if (duration < 0.0F)
                    isDone = true;
            }

            @Override
            public void render(SpriteBatch spriteBatch) {
            }

            @Override
            public void dispose() {
            }
        });
    }

    public static void retreatGremlinBack(AbstractMonster m) {
        AbstractDungeon.effectsQueue.add(new AbstractGameEffect() {
            private final float start_x = m.drawX;
            private final float end_x = RIGHT_X_BACK;
            boolean first = true;
            @Override
            public void update() {
                duration -= Gdx.graphics.getDeltaTime();

                if (first) {
                    first = false;
                    duration = 1.0F;
                }
                m.drawX = Interpolation.linear.apply(start_x, end_x, 1.0F - duration);

                if (duration < 0.0F) {
                    AbstractDungeon.effectsQueue.add(new SpeechBubble(m.hb.cX + m.dialogX, m.hb.cY + m.dialogY, 2.5F, TEXT[1], false));
                    isDone = true;
                }
            }

            @Override
            public void render(SpriteBatch spriteBatch) {
            }

            @Override
            public void dispose() {
            }
        });
    }

    public static void moveLeftGremlinUp() {
        monsterLeftFront = monsterLeftBack;
        AbstractMonster m = monsterLeftFront;
        AbstractDungeon.effectsQueue.add(new AbstractGameEffect() {
            private final float start_x = m.drawX;
            private final float end_x = LEFT_X_FRONT;
            boolean first = true;
            @Override
            public void update() {
                duration -= Gdx.graphics.getDeltaTime();

                if (first) {
                    first = false;
                    duration = 0.4F;
                }
                m.drawX = Interpolation.linear.apply(start_x, end_x, (0.4F - duration)/0.4F);

                if (duration < 0.0F)
                    isDone = true;
            }

            @Override
            public void render(SpriteBatch spriteBatch) {
            }

            @Override
            public void dispose() {
            }
        });
    }

    public static void moveRightGremlinUp() {
        monsterRightFront = monsterRightBack;
        AbstractMonster m = monsterRightFront;
        AbstractDungeon.effectsQueue.add(new AbstractGameEffect() {
            private final float start_x = m.drawX;
            private final float end_x = RIGHT_X_FRONT;
            boolean first = true;
            @Override
            public void update() {
                duration -= Gdx.graphics.getDeltaTime();

                if (first) {
                    first = false;
                    duration = 0.4F;
                }
                m.drawX = Interpolation.linear.apply(start_x, end_x, (0.4F - duration)/0.4F);

                if (duration < 0.0F)
                    isDone = true;
            }

            @Override
            public void render(SpriteBatch spriteBatch) {
            }

            @Override
            public void dispose() {
            }
        });

    }

    public static void moveLeftGremlinIn(boolean far, boolean shout) {
        AbstractMonster m;
        if (groundQueue.size() > platformQueue.size() / 3) {
            m = groundQueue.get(0);
            groundQueue.remove(0);
        } else if (!platformQueue.isEmpty()) {
            m = platformQueue.get(0);
            platformQueue.remove(0);
        } else
            return;

        if (far)
            monsterLeftFront = m;
        else
            monsterLeftBack = m;
        spawnMonsterInstantly(m);

        final float end_x;
        if (far)
            end_x = LEFT_X_FRONT;
        else
            end_x = LEFT_X_BACK;

        m.drawY = AbstractDungeon.floorY + AbstractDungeon.miscRng.random(-30.0F, 30.0F);

        AbstractDungeon.effectsQueue.add(new AbstractGameEffect() {
            private final float start_x = m.drawX;
            boolean first = true;
            float DURATION;
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
                }
                m.drawX = Interpolation.linear.apply(start_x, end_x, (DURATION - duration)/DURATION);

                if (duration < 0.0F) {
                    isDone = true;
                    if (shout)
                        AbstractDungeon.effectsQueue.add(new SpeechBubble(m.hb.cX + m.dialogX, m.hb.cY + m.dialogY, 2.5F, TEXT[2], false));
                }
            }

            @Override
            public void render(SpriteBatch spriteBatch) {
            }

            @Override
            public void dispose() {
            }
        });
    }

    public static void moveRightGremlinIn(boolean far) {
        AbstractMonster m;
        if (groundQueue.size() > platformQueue.size() / 3) {
            m = groundQueue.get(0);
            groundQueue.remove(0);
        } else if (!platformQueue.isEmpty()) {
            m = platformQueue.get(0);
            platformQueue.remove(0);
        } else
            return;

        if (far)
            monsterRightFront = m;
        else
            monsterRightBack = m;
        spawnMonsterInstantly(m);

        final float end_x;
        if (far)
            end_x = RIGHT_X_FRONT;
        else
            end_x = RIGHT_X_BACK;

        m.drawY = AbstractDungeon.floorY + AbstractDungeon.miscRng.random(-30.0F, 30.0F);

        AbstractDungeon.effectsQueue.add(new AbstractGameEffect() {
            private final float start_x = m.drawX;
            boolean first = true;
            float DURATION;
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
        });

    }

    public static void fillLeftPlatform() {
        AbstractMonster m;
        if (!platformQueue.isEmpty()) {
            m = platformQueue.get(0);
            platformQueue.remove(0);
        } else
            return;

        monsterLeftPlatform = m;
        spawnMonsterInstantly(m);
        m.drawX = PLATFORM_X_LEFT;

        AbstractDungeon.effectsQueue.add(new AbstractGameEffect() {
            private final float start_y = Settings.HEIGHT*1.05F + m.drawY;
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
                m.drawY = Interpolation.linear.apply(start_y, PLATFORM_END_Y, (DURATION - duration)/DURATION);

                if (duration < 0.0F) {
                    CardCrawlGame.sound.play("BLUNT_FAST");
                    isDone = true;
                }
            }

            @Override
            public void render(SpriteBatch spriteBatch) {
            }

            @Override
            public void dispose() {
            }
        });
    }

    public static void fillRightPlatform() {
        AbstractMonster m;
        if (!platformQueue.isEmpty()) {
            m = platformQueue.get(0);
            platformQueue.remove(0);
        } else
            return;

        monsterRightPlatform = m;
        spawnMonsterInstantly(m);
        m.drawX = PLATFORM_X_RIGHT;

        AbstractDungeon.effectsQueue.add(new AbstractGameEffect() {
            private final float start_y = Settings.HEIGHT*1.05F + m.drawY;
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
                if (duration <= DURATION)
                    m.drawY = Interpolation.linear.apply(start_y, PLATFORM_END_Y, (DURATION - duration)/DURATION);

                if (duration < 0.0F) {
                    CardCrawlGame.sound.play("BLUNT_FAST");
                    isDone = true;
                }
            }

            @Override
            public void render(SpriteBatch spriteBatch) {
            }

            @Override
            public void dispose() {
            }
        });
    }

    private static void spawnMonsterInstantly(AbstractMonster m) {
        for (AbstractRelic r : adp().relics)
            r.onSpawnMonster(m);

        m.init();
        m.applyPowers();
        AbstractDungeon.getCurrRoom().monsters.addMonster(Wiz.getEnemies().size(), m);
        m.showHealthBar();

        if (ModHelper.isModEnabled("Lethality"))
            atb(new ApplyPowerAction(m, m, new StrengthPower(m, 3), 3));

        if (ModHelper.isModEnabled("Time Dilation"))
            atb(new ApplyPowerAction(m, m, new SlowPower(m, 0)));
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
