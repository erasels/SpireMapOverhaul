package spireMapOverhaul.zones.gremlinTown.events;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
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
import com.megacrit.cardcrawl.vfx.combat.SmokeBombEffect;
import com.megacrit.cardcrawl.vfx.scene.SpookyChestEffect;
import spireMapOverhaul.util.Wiz;
import spireMapOverhaul.zones.gremlinTown.GremlinTown;
import spireMapOverhaul.zones.gremlinTown.events.misc.Chest;
import spireMapOverhaul.zones.gremlinTown.events.misc.Shell;
import spireMapOverhaul.zones.gremlinTown.monsters.GremlinCannon;
import spireMapOverhaul.zones.gremlinTown.monsters.GremlinRiderRed;

import static spireMapOverhaul.SpireAnniversary6Mod.makeID;
import static spireMapOverhaul.util.Wiz.adRoom;
import static spireMapOverhaul.util.Wiz.adp;

public class Surprise extends AbstractEvent {
    public static final String ID = makeID(Surprise.class.getSimpleName());
    private static final EventStrings eventStrings;

    private CUR_SCREEN screen;
    private Chest chest;
    private float animTimer;
    private float shinyTimer;
    private static final float RIDER_A_START_X;
    private static final float RIDER_A_TARGET_X;
    private static final float RIDER_B_START_X;
    private static final float RIDER_B_TARGET_X;
    private static final float SHELL_START_X;
    private static final float SHELL_START_Y;
    private static final int GOLD_BASE = 40;
    private static final int GOLD_VARIANCE = 10;

    private GremlinRiderRed riderA;
    private GremlinRiderRed riderB;
    private Shell shell;
    private float shellTargetX;
    private float shellTargetY;
    private boolean burst;
    public boolean mimic;

    static {
        eventStrings = CardCrawlGame.languagePack.getEventString(ID);
        NAME = eventStrings.NAME;
        OPTIONS = eventStrings.OPTIONS;
        RIDER_A_START_X = Settings.WIDTH + 200.0F;
        RIDER_B_START_X = Settings.WIDTH + 600.0F;
        RIDER_A_TARGET_X = Settings.WIDTH*0.75F - 320.0F;
        RIDER_B_TARGET_X = Settings.WIDTH*0.75F - 100.0F;
        SHELL_START_X = Chest.CHEST_LOC_X;
        SHELL_START_Y = Chest.CHEST_LOC_Y;
    }

    public static boolean bonusCondition() {
        return true;
    }

    public Surprise() {
        super();
        chest = new Chest();
        screen = CUR_SCREEN.INTRO;
        shinyTimer = 0f;
        burst = false;
        hasFocus = true;
        hasDialog = false;
        roomEventText.hide();
        mimic = true;
        adRoom().phase = AbstractRoom.RoomPhase.COMPLETE;
        AbstractDungeon.overlayMenu.proceedButton.show();
        String proceedLabel = CardCrawlGame.languagePack.getUIString("TreasureRoom").TEXT[0];
        AbstractDungeon.overlayMenu.proceedButton.setLabel(proceedLabel);
    }

    public void update() {
        super.update();
        chest.update();
        updateShiny();
        if (chest.isOpen && screen == CUR_SCREEN.INTRO) {
            mimic = false;
            screen = CUR_SCREEN.AMBUSH;
            adRoom().phase = AbstractRoom.RoomPhase.EVENT;
            animTimer = 6.0F;
            riderA = new GremlinRiderRed(RIDER_A_START_X - Settings.WIDTH*0.75F, -50.0F);
            riderB = new GremlinRiderRed(RIDER_B_START_X - Settings.WIDTH*0.75F, -50.0F);
            Wiz.adRoom().monsters = new MonsterGroup(new AbstractMonster[]{
                    new GremlinCannon(Chest.CHEST_LOC_X - Settings.WIDTH*0.75F, Chest.CHEST_LOC_Y - AbstractDungeon.floorY - 256.0F),
                    riderA, riderB
            });
            shell = new Shell(chest.hb.cX, chest.hb.cY, 0F);
            shellTargetX = adp().hb.cX;
            shellTargetY = adp().hb.y;
            // CardCrawlGame.sound.play("BLUNT_HEAVY");
        } else if (screen == CUR_SCREEN.AMBUSH) {
            if (animTimer > 0.0F) {
                animTimer -= Gdx.graphics.getDeltaTime();
                if (animTimer >= 3.0F) {
                    float x = Interpolation.linear.apply(shellTargetX, SHELL_START_X, (animTimer - 3.0F)/3.0F);
                    float y = Interpolation.linear.apply(shellTargetY, SHELL_START_Y, (animTimer - 3.0F)/3.0F);
                    shell.hb.move(x, y);
                    shell.rotation = Interpolation.linear.apply(0F, 360.0F * 3, (animTimer - 3.0F)/3.0F);
                } else if (animTimer < 3.0f) {
                    if (!burst) {
                        burst = true;
                        shell.hide = true;
                        chest.hide = true;
                        CardCrawlGame.sound.play("ATTACK_FIRE");
                        AbstractDungeon.effectsQueue.add(new SmokeBombEffect(shellTargetX, shellTargetY));
                    }
                    float xa = Interpolation.linear.apply(RIDER_A_TARGET_X, RIDER_A_START_X, animTimer/3.0F);
                    float xb = Interpolation.linear.apply(RIDER_B_TARGET_X, RIDER_B_START_X, animTimer/3.0F);
                    riderA.drawX = xa;
                    riderB.drawX = xb;
                }
                if (animTimer < 0.0F) {
                    screen = CUR_SCREEN.COMBAT;
                    adRoom().addGoldToRewards(GOLD_BASE + AbstractDungeon.miscRng.random(0, GOLD_VARIANCE));
                    adRoom().addRelicToRewards(GremlinTown.getRandomGRelic());
                    enterCombat();
                }
            }
        }
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
        if (chest != null)
            chest.render(sb);
        if (shell != null)
            shell.render(sb);
        sb.setColor(Color.WHITE);
    }

    private enum CUR_SCREEN {
        INTRO,
        AMBUSH,
        COMBAT
    }
}
