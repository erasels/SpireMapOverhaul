package spireMapOverhaul.zones.gremlinTown.events;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Interpolation;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.AbstractEvent;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.MonsterGroup;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.vfx.ChestShineEffect;
import com.megacrit.cardcrawl.vfx.scene.SpookyChestEffect;
import spireMapOverhaul.util.Wiz;
import spireMapOverhaul.zones.gremlinTown.GremlinTown;
import spireMapOverhaul.zones.gremlinTown.events.misc.Chest;
import spireMapOverhaul.zones.gremlinTown.events.misc.Shell;
import spireMapOverhaul.zones.gremlinTown.monsters.GremlinCannon;
import spireMapOverhaul.zones.gremlinTown.monsters.GremlinRiderRed;

import static spireMapOverhaul.SpireAnniversary6Mod.makeID;
import static spireMapOverhaul.util.Wiz.adp;
import static spireMapOverhaul.util.Wiz.curRoom;

public class Surprise extends AbstractEvent {
    public static final String ID = makeID(Surprise.class.getSimpleName());
    private static final EventStrings eventStrings;
    private static final String NAME;
    private static final String[] DESCRIPTIONS;
    private static final String[] OPTIONS;

    private CUR_SCREEN screen;
    private float animTimer;
    private float shinyTimer;
    private static final float RIDER_A_START_X;
    private static final float RIDER_B_START_X;
    private static final float RIDER_A_MID_X;
    private static final float RIDER_B_MID_X;
    private static final float RIDER_A_END_X;
    private static final float RIDER_B_END_X;
    private static final float RIDER_A_START_Y;
    private static final float RIDER_B_START_Y;
    private static final float RIDER_A_END_Y;
    private static final float RIDER_B_END_Y;
    private static final int GOLD_BASE = 40;
    private static final int GOLD_VARIANCE = 10;
    private static final float AMBUSH_TIME = 3.0F;
    private static final float SOUND_TIME = 0.15F;
    private static final float STARE_TIME = 0.75F;
    public static final float SHELL_FLIGHT_TIME = 1.0F;

    private GremlinRiderRed riderA;
    private GremlinRiderRed riderB;
    private Chest chest;
    private Shell shell;
    public boolean mimic;
    private boolean fired;
    private boolean soundFired;

    static {
        eventStrings = CardCrawlGame.languagePack.getEventString(ID);
        NAME = eventStrings.NAME;
        DESCRIPTIONS = eventStrings.DESCRIPTIONS;
        OPTIONS = eventStrings.OPTIONS;
        RIDER_A_START_X = Settings.WIDTH + 200.0F;
        RIDER_B_START_X = Settings.WIDTH + 600.0F;
        RIDER_A_MID_X = Settings.WIDTH * 0.56F;
        RIDER_B_MID_X = Settings.WIDTH * 0.58F;
        RIDER_A_END_X = Settings.WIDTH * 0.53F;
        RIDER_B_END_X = Settings.WIDTH * 0.43F;
        RIDER_A_START_Y = AbstractDungeon.floorY - 100f;
        RIDER_B_START_Y = AbstractDungeon.floorY - 115f;
        RIDER_A_END_Y = AbstractDungeon.floorY + 40f;
        RIDER_B_END_Y = AbstractDungeon.floorY - 60f;
    }

    public Surprise() {
        super();
        chest = new Chest();
        screen = CUR_SCREEN.INTRO;
        shinyTimer = 0f;
        hasFocus = true;
        hasDialog = false;
        roomEventText.hide();
        mimic = true;
        fired = false;
        soundFired = false;
        curRoom().phase = AbstractRoom.RoomPhase.COMPLETE;
        AbstractDungeon.overlayMenu.proceedButton.show();
        String proceedLabel = CardCrawlGame.languagePack.getUIString("TreasureRoom").TEXT[0];
        AbstractDungeon.overlayMenu.proceedButton.setLabel(proceedLabel);
    }

    public static boolean endsWithRewardsUI() {
        return true;
    }

    public void update() {
        super.update();
        chest.update();
        if (shell != null)
            shell.update();
        updateShiny();
        if (chest.isOpen && screen == CUR_SCREEN.INTRO) {
            mimic = false;
            screen = CUR_SCREEN.AMBUSH;
            curRoom().phase = AbstractRoom.RoomPhase.EVENT;
            animTimer = AMBUSH_TIME;
            riderA = new GremlinRiderRed(RIDER_A_START_X - Settings.WIDTH*0.75F, RIDER_A_START_Y - AbstractDungeon.floorY);
            riderB = new GremlinRiderRed(RIDER_B_START_X - Settings.WIDTH*0.75F, RIDER_B_START_Y - AbstractDungeon.floorY);
            Wiz.curRoom().monsters = new MonsterGroup(new AbstractMonster[]{
                    new GremlinCannon((Chest.CHEST_LOC_X - Settings.WIDTH*0.75F)/Settings.scale,
                            (Chest.CHEST_LOC_Y - AbstractDungeon.floorY - 256.0F*Settings.scale)/Settings.yScale),
                    riderA, riderB
            });
            chest.hide = true;
        } else if (screen == CUR_SCREEN.AMBUSH) {
            animTimer -= Gdx.graphics.getDeltaTime();
            updateRiderA();
            updateRiderB();
            if (animTimer <= AMBUSH_TIME - STARE_TIME + SOUND_TIME && !soundFired) {
                soundFired = true;
                CardCrawlGame.sound.play("BLUNT_HEAVY");
            }
            if (animTimer <= AMBUSH_TIME - STARE_TIME && !fired) {
                fired = true;
                float shellTargetX = adp().hb.cX;
                float shellTargetY = adp().hb.y;
                shell = new Shell(chest.hb.x + 60f*Settings.scale, chest.hb.cY + 52f*Settings.scale,
                        shellTargetX, shellTargetY, SHELL_FLIGHT_TIME);
            }
            if (animTimer < 0.0F) {
                screen = CUR_SCREEN.COMBAT;
                curRoom().addGoldToRewards(GOLD_BASE + AbstractDungeon.miscRng.random(0, GOLD_VARIANCE));
                curRoom().addRelicToRewards(GremlinTown.getRandomGRelic());
                // For render order
                AbstractMonster tmp = curRoom().monsters.monsters.get(0);
                curRoom().monsters.monsters.add(tmp);
                curRoom().monsters.monsters.remove(0);
                AbstractDungeon.lastCombatMetricKey = GremlinTown.SURPRISE;
                enterCombat();
            }
        }
    }

    public void updateRiderA() {
        float t = AMBUSH_TIME - STARE_TIME - animTimer;
        float x, y;
        if (t < 0) {
            x = RIDER_A_START_X;
            y = RIDER_A_START_Y;
        } else if (t < 1.5F) {
            x = Interpolation.linear.apply(RIDER_A_START_X, RIDER_A_MID_X, t / 1.5F);
            y = RIDER_A_START_Y;
        }
        else if (t < 1.82F) {
            x = Interpolation.linear.apply(RIDER_A_MID_X, RIDER_A_END_X, (t - 1.5F)/0.32F);
            y = Interpolation.linear.apply(RIDER_A_START_Y, RIDER_A_END_Y, (t - 1.5F)/0.32F);
        } else {
            x = RIDER_A_END_X;
            y = RIDER_A_END_Y;
        }
        riderA.drawX = x;
        riderA.drawY = y;
    }

    public void updateRiderB() {
        float t = AMBUSH_TIME - STARE_TIME - animTimer;
        float x, y;
        if (t < 0) {
            x = RIDER_B_START_X;
            y = RIDER_B_START_Y;
        } else if (t < 1.75F) {
            x = Interpolation.linear.apply(RIDER_B_START_X, RIDER_B_MID_X, t / 1.75F);
            y = RIDER_B_START_Y;
        }
        else if (t < 2.15F) {
            x = Interpolation.linear.apply(RIDER_B_MID_X, RIDER_B_END_X, (t - 1.75F)/0.4F);
            y = Interpolation.linear.apply(RIDER_B_START_Y, RIDER_B_END_Y, (t - 1.75F)/0.4F);
        } else {
            x = RIDER_B_END_X;
            y = RIDER_B_END_Y;
        }
        riderB.drawX = x;
        riderB.drawY = y;
    }

    private void updateShiny() {
        if (chest != null && !chest.isOpen && screen == CUR_SCREEN.INTRO) {
            shinyTimer -= Gdx.graphics.getDeltaTime();
            if (shinyTimer < 0.0F && !Settings.DISABLE_EFFECTS) {
                shinyTimer = 0.2F;
                AbstractDungeon.topLevelEffects.add(new ChestShineEffect());
                AbstractDungeon.effectList.add(new SpookyChestEffect());
                AbstractDungeon.effectList.add(new SpookyChestEffect());
            }
        }
    }

    protected void buttonEffect(int buttonPressed) {
    }

    @Override
    public void render(SpriteBatch sb) {
        super.render(sb);
        if (chest != null)
            chest.render(sb);
        if (shell != null)
            shell.render(sb);
    }

    private enum CUR_SCREEN {
        INTRO,
        AMBUSH,
        COMBAT
    }
}
