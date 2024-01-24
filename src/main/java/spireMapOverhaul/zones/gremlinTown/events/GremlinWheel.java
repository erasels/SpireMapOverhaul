package spireMapOverhaul.zones.gremlinTown.events;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.AbstractImageEvent;
import com.megacrit.cardcrawl.events.GenericEventDialog;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.MathHelper;
import com.megacrit.cardcrawl.helpers.MonsterHelper;
import com.megacrit.cardcrawl.helpers.controller.CInputActionSet;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.vfx.RainingGoldEffect;
import spireMapOverhaul.SpireAnniversary6Mod;
import spireMapOverhaul.zones.gremlinTown.GremlinTown;

import java.util.ArrayList;
import java.util.Collections;

import static spireMapOverhaul.SpireAnniversary6Mod.makeID;
import static spireMapOverhaul.SpireAnniversary6Mod.makeImagePath;
import static spireMapOverhaul.util.Wiz.*;

public class GremlinWheel extends AbstractImageEvent {
    public static final String ID = makeID(GremlinWheel.class.getSimpleName());
    private static final EventStrings eventStrings;
    private static final String NAME;
    private static final String[] DESCRIPTIONS;
    private static final String[] OPTIONS;
    private static final String IMAGE_PATH;
    private CUR_SCREEN screen;
    private boolean startSpinFake;
    private boolean finishSpinFake;
    private boolean startSpinReal;
    private boolean doneSpinning;
    private boolean bounceIn;
    private float bounceTimer;
    private float animTimer;
    private float spinVelocity;
    private boolean buttonPressed;
    private final Hitbox buttonHb;
    private final Texture wheelImg;
    private final Texture arrowImg;
    private final Texture buttonImg;
    private static final float START_Y;
    private static final float TARGET_Y;
    private float imgX;
    private float imgY;
    private float wheelAngle;
    private static final float ARROW_OFFSET_X;
    private Color color;
    private static final float HP_LOSS = 0.1f;
    private static final float HP_LOSS_A15 = 0.15F;
    private static final float RESULT_ANGLE_REAL;
    private static final float RESULT_ANGLE_FAKE;
    private static final String WHEEL_PATH = makeImagePath("events/GremlinTown/Wheel.png");
    private float decisionTimer;
    private boolean deciding;
    int buttonPanic;
    int buttonSubmit;
    int buttonWhat;
    int buttonFight;

    private static final int GOLD_BASE = 100;
    private static final int GOLD_VARIANCE = 25;
    private static final float DECISION_TIME = 4.5F;
    private static final float DECISION_TIME_A15 = 3.0F;
    private final int goldAmount;

    static {
        eventStrings = CardCrawlGame.languagePack.getEventString(ID);
        NAME = eventStrings.NAME;
        DESCRIPTIONS = eventStrings.DESCRIPTIONS;
        OPTIONS = eventStrings.OPTIONS;
        IMAGE_PATH = "images/events/spinTheWheel.jpg";
        START_Y = Settings.OPTION_Y + 1000.0F * Settings.scale;
        TARGET_Y = Settings.OPTION_Y;
        ARROW_OFFSET_X = 300.0F * Settings.scale;
        RESULT_ANGLE_FAKE = 71.6F;
        RESULT_ANGLE_REAL = -55.4F;
    }

    public static boolean bonusCondition() {
        if (adp() == null)
            return false;
        return ((float) adp().currentHealth) / ((float) adp().maxHealth) > 0.15f;
    }

    public GremlinWheel() {
        super(NAME, DESCRIPTIONS[0], IMAGE_PATH);
        goldAmount = GOLD_BASE + AbstractDungeon.miscRng.random(0, GOLD_VARIANCE);
        screen = CUR_SCREEN.INTRO;
        startSpinFake = false;
        finishSpinFake = false;
        startSpinReal = false;
        doneSpinning = false;
        bounceIn = true;
        bounceTimer = 1.0F;
        animTimer = 3.0F;
        spinVelocity = 200.0F;
        buttonPressed = false;
        buttonHb = new Hitbox(450.0F * Settings.scale, 300.0F * Settings.scale);
        imgX = (float)Settings.WIDTH / 2.0F;
        imgY = START_Y;
        wheelAngle = 0.0F;
        color = new Color(1.0F, 1.0F, 1.0F, 0.0F);
        wheelImg = ImageMaster.loadImage(WHEEL_PATH);
        arrowImg = ImageMaster.loadImage("images/events/wheelArrow.png");
        buttonImg = ImageMaster.loadImage("images/events/spinButton.png");

        imageEventText.setDialogOption(OPTIONS[0]);
        hasDialog = true;
        hasFocus = true;
        buttonHb.move(500.0F * Settings.scale, -500.0F * Settings.scale);

        deciding = false;
    }

    public void update() {
        super.update();
        if (screen == CUR_SCREEN.COMPLETE)
            return;
        updatePosition();
        if (bounceTimer == 0.0F && startSpinFake) {
            if (!buttonPressed) {
                buttonHb.cY = MathHelper.cardLerpSnap(buttonHb.cY, Settings.OPTION_Y - 330.0F * Settings.scale);
                buttonHb.move(buttonHb.cX, buttonHb.cY);
                buttonHb.update();
                if (buttonHb.hovered && InputHelper.justClickedLeft || CInputActionSet.proceed.isJustPressed()) {
                    buttonPressed = true;
                    buttonHb.hovered = false;
                    CardCrawlGame.sound.play("WHEEL");
                }
            } else
                buttonHb.cY = MathHelper.cardLerpSnap(buttonHb.cY, -500.0F * Settings.scale);
        }

        if (startSpinFake && bounceTimer == 0.0F && buttonPressed) {
            imgY = TARGET_Y;
            if (animTimer > 0.0F) {
                animTimer -= Gdx.graphics.getDeltaTime();
                wheelAngle += spinVelocity * Gdx.graphics.getDeltaTime();
            } else {
                animTimer = 2.0F;
                finishSpinFake = true;
                startSpinFake = false;
            }
            SpireAnniversary6Mod.logger.info("start spin fake");
            SpireAnniversary6Mod.logger.info(wheelAngle);
        } else if (finishSpinFake) {
            if (animTimer > 0.0F) {
                animTimer -= Gdx.graphics.getDeltaTime();
                wheelAngle = Interpolation.pow2In.apply(RESULT_ANGLE_FAKE, -700F, animTimer/2.0F);
                if (animTimer < 0.0F) {
                    wheelAngle = RESULT_ANGLE_FAKE;
                    animTimer = 2.0F;
                    finishSpinFake = false;
                    startSpinReal = true;
                }
            }
        } else if (startSpinReal) {
            if (animTimer > 0.0F) {
                animTimer -= Gdx.graphics.getDeltaTime();
                wheelAngle = Interpolation.pow2.apply(RESULT_ANGLE_REAL, RESULT_ANGLE_FAKE, animTimer/2.0F);
                if (animTimer < 0.0F) {
                    wheelAngle = RESULT_ANGLE_REAL;
                    animTimer = 1.0F;
                    startSpinReal = false;
                    doneSpinning = true;
                }
            }
        } else if (doneSpinning) {
            if (animTimer > 0.0F) {
                animTimer -= Gdx.graphics.getDeltaTime();
                if (animTimer <= 0.0F) {
                    bounceTimer = 1.0F;
                    bounceIn = false;
                }
            } else if (bounceTimer == 0.0F) {
                doneSpinning = false;
                deciding = true;
                if (asc() >= 15)
                    decisionTimer = DECISION_TIME_A15;
                else
                    decisionTimer = DECISION_TIME;
                preApplyDialog();
                GenericEventDialog.show();
                screen = CUR_SCREEN.DECISION;
            }
        } else if (deciding) {
            if (decisionTimer > 0.0f) {
                decisionTimer -= Gdx.graphics.getDeltaTime();
                if (decisionTimer <= 0.0f) {
                    deciding = false;
                    screen = CUR_SCREEN.STABBED;
                    imageEventText.updateBodyText(DESCRIPTIONS[5]);
                    imageEventText.clearAllDialogs();
                    imageEventText.setDialogOption(OPTIONS[5].replace("{0}", String.valueOf(getHpLoss())));
                }
            }
        }

        if (!GenericEventDialog.waitForInput)
            buttonEffect(GenericEventDialog.getSelectedOption());
    }

    private void updatePosition() {
        if (bounceTimer != 0.0F) {
            bounceTimer -= Gdx.graphics.getDeltaTime();
            if (bounceTimer < 0.0F)
                bounceTimer = 0.0F;

            if (bounceIn && startSpinFake) {
                color.a = Interpolation.fade.apply(1.0F, 0.0F, bounceTimer);
                imgY = Interpolation.bounceIn.apply(TARGET_Y, START_Y, bounceTimer);
            } else if (doneSpinning) {
                color.a = Interpolation.fade.apply(0.0F, 1.0F, bounceTimer);
                imgY = Interpolation.swingOut.apply(START_Y, TARGET_Y, bounceTimer);
            }
        }
    }

    private void preApplyDialog() {
        imageEventText.clearAllDialogs();
        imageEventText.updateBodyText(DESCRIPTIONS[1]);
        ArrayList<Integer> ops = new ArrayList<>();
        ops.add(1);
        ops.add(2);
        ops.add(3);
        ops.add(4);
        Collections.shuffle(ops, AbstractDungeon.miscRng.random);

        imageEventText.setDialogOption(OPTIONS[ops.get(0)]);
        imageEventText.setDialogOption(OPTIONS[ops.get(1)]);
        imageEventText.setDialogOption(OPTIONS[ops.get(2)]);
        imageEventText.setDialogOption(OPTIONS[ops.get(3)]);

        for (int i = 0; i < ops.size(); i++) {
            int x = ops.get(i) - 1;
            if (x == 0)
                buttonPanic = i;
            if (x == 1)
                buttonSubmit = i;
            if (x == 2)
                buttonWhat = i;
            else
                buttonFight = i;
        }
    }

    protected void buttonEffect(int buttonPressed) {
        SpireAnniversary6Mod.logger.info("BUTTON PRESSED");
        if (screen == CUR_SCREEN.INTRO) {
            if (buttonPressed == 0) {
                GenericEventDialog.hide();
                wheelAngle = 0.0F;
                startSpinFake = true;
                bounceTimer = 2.0F;
                animTimer = 3.0F;
                spinVelocity = 1500.0F;
            }
        } else if (screen == CUR_SCREEN.DECISION) {
            imageEventText.clearAllDialogs();
            deciding = false;
            if (buttonPressed == buttonPanic) {
                imageEventText.updateBodyText(DESCRIPTIONS[2]);
                imageEventText.setDialogOption(OPTIONS[5].replace("{0}", String.valueOf(getHpLoss())));
                screen = CUR_SCREEN.STABBED;
            }
            else if (buttonPressed == buttonSubmit) {
                imageEventText.updateBodyText(DESCRIPTIONS[3]);
                imageEventText.setDialogOption(OPTIONS[5].replace("{0}", String.valueOf(getHpLoss())));
                screen = CUR_SCREEN.STABBED;
            }
            else if (buttonPressed == buttonWhat) {
                imageEventText.updateBodyText(DESCRIPTIONS[4]);
                imageEventText.setDialogOption(OPTIONS[5].replace("{0}", String.valueOf(getHpLoss())));
                screen = CUR_SCREEN.STABBED;
            } else {
                imageEventText.updateBodyText(DESCRIPTIONS[6]);
                imageEventText.setDialogOption(OPTIONS[4]);
                screen = CUR_SCREEN.PRE_FIGHT;
            }
        } else if (screen == CUR_SCREEN.PRE_FIGHT) {
            AbstractDungeon.getCurrRoom().monsters = MonsterHelper.getEncounter(GremlinTown.GREMLIN_JERK);
            adRoom().rewardAllowed = false;
            screen = CUR_SCREEN.POST_COMBAT;
            AbstractDungeon.lastCombatMetricKey = GremlinTown.GREMLIN_JERK;
            enterCombatFromImage();
        } else if (screen == CUR_SCREEN.STABBED) {
            CardCrawlGame.sound.play("ATTACK_DAGGER_6");
            CardCrawlGame.sound.play("BLOOD_SPLAT");
            adp().damage(new DamageInfo(null, getHpLoss(), DamageInfo.DamageType.HP_LOSS));
            imageEventText.clearAllDialogs();
            imageEventText.setDialogOption(OPTIONS[6]);
            screen = CUR_SCREEN.COMPLETE;
        } else if (screen == CUR_SCREEN.POST_COMBAT) {
            AbstractDungeon.effectList.add(new RainingGoldEffect(goldAmount));
            adp().gainGold(goldAmount);
            imageEventText.clearAllDialogs();
            imageEventText.setDialogOption(OPTIONS[6]);
            screen = CUR_SCREEN.COMPLETE;
        } else if (screen == CUR_SCREEN.COMPLETE)
            openMap();
    }

    @Override
    public void reopen() {
        AbstractDungeon.resetPlayer();
        adp().drawX = (float)Settings.WIDTH * 0.25F;
        adp().preBattlePrep();
        enterImageFromCombat();
        imageEventText.updateBodyText(DESCRIPTIONS[7]);
        imageEventText.clearAllDialogs();
        imageEventText.setDialogOption(OPTIONS[7].replace("{0}", String.valueOf(goldAmount)));
    }

    public void render(SpriteBatch sb) {
        sb.setColor(color);
        sb.draw(wheelImg, imgX - 512.0F, imgY - 512.0F, 512.0F, 512.0F, 1024.0F, 1024.0F, Settings.scale, Settings.scale, wheelAngle, 0, 0, 1024, 1024, false, false);
        sb.draw(arrowImg, imgX - 256.0F + ARROW_OFFSET_X + 180.0F * Settings.scale, imgY - 256.0F, 256.0F, 256.0F, 512.0F, 512.0F, Settings.scale, Settings.scale, 0.0F, 0, 0, 512, 512, false, false);
        if (buttonHb.hovered)
            sb.draw(buttonImg, buttonHb.cX - 256.0F, buttonHb.cY - 256.0F, 256.0F, 256.0F, 512.0F, 512.0F, Settings.scale * 1.05F, Settings.scale * 1.05F, 0.0F, 0, 0, 512, 512, false, false);
        else
            sb.draw(buttonImg, buttonHb.cX - 256.0F, buttonHb.cY - 256.0F, 256.0F, 256.0F, 512.0F, 512.0F, Settings.scale, Settings.scale, 0.0F, 0, 0, 512, 512, false, false);

        sb.setBlendFunction(770, 1);
        if (buttonHb.hovered)
            sb.setColor(new Color(1.0F, 1.0F, 1.0F, 0.25F));
        else
            sb.setColor(new Color(1.0F, 1.0F, 1.0F, (MathUtils.cosDeg((float)(System.currentTimeMillis() / 5L % 360L)) + 1.25F) / 3.5F));

        if (buttonHb.hovered)
            sb.draw(buttonImg, buttonHb.cX - 256.0F, buttonHb.cY - 256.0F, 256.0F, 256.0F, 512.0F, 512.0F, Settings.scale * 1.05F, Settings.scale * 1.05F, 0.0F, 0, 0, 512, 512, false, false);
        else
            sb.draw(buttonImg, buttonHb.cX - 256.0F, buttonHb.cY - 256.0F, 256.0F, 256.0F, 512.0F, 512.0F, Settings.scale, Settings.scale, 0.0F, 0, 0, 512, 512, false, false);

        if (Settings.isControllerMode)
            sb.draw(CInputActionSet.proceed.getKeyImg(), buttonHb.cX - 32.0F - 160.0F * Settings.scale, buttonHb.cY - 32.0F - 70.0F * Settings.scale, 32.0F, 32.0F, 64.0F, 64.0F, Settings.scale, Settings.scale, 0.0F, 0, 0, 64, 64, false, false);

        sb.setBlendFunction(770, 771);
        buttonHb.render(sb);
    }

    public void dispose() {
        super.dispose();
        if (wheelImg != null) {
            wheelImg.dispose();
        }

        if (arrowImg != null) {
            arrowImg.dispose();
        }

        if (buttonImg != null) {
            buttonImg.dispose();
        }

    }

    private static int getHpLoss() {
        if (asc() < 15)
            return (int)(adp().maxHealth * HP_LOSS);
        else
            return (int)(adp().maxHealth * HP_LOSS_A15);
    }

    private enum CUR_SCREEN {
        INTRO,
        DECISION,
        PRE_FIGHT,
        STABBED,
        POST_COMBAT,
        COMPLETE;
    }
}
