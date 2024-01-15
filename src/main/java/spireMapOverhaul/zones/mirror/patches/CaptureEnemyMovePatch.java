package spireMapOverhaul.zones.mirror.patches;

import basemod.ReflectionHacks;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.utils.ScreenUtils;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.actions.unique.GainBlockRandomMonsterAction;
import com.megacrit.cardcrawl.actions.unique.RemoveDebuffsAction;
import com.megacrit.cardcrawl.actions.unique.SummonGremlinAction;
import com.megacrit.cardcrawl.actions.unique.VampireDamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.status.*;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.EnemyMoveInfo;
import com.megacrit.cardcrawl.powers.AbstractPower;
import javassist.CtBehavior;
import spireMapOverhaul.zones.mirror.MirrorMoveData;
import spireMapOverhaul.zones.mirror.MirrorZone;
import spireMapOverhaul.zones.mirror.powers.MirrorZonePower;

public class CaptureEnemyMovePatch {
    public static SpriteBatch sb = new SpriteBatch();
    public static int WIDTH = 500;
    public static int HEIGHT = 380;
    public static float CAMERA_WIDTH = 350 * Settings.scale;
    public static float CAMERA_HEIGHT = 266 * Settings.scale;

    static MirrorMoveData curMoveData = null;

    // render
    static OrthographicCamera camera;
    static FrameBuffer fbo = new FrameBuffer(Pixmap.Format.RGBA8888, WIDTH, HEIGHT, false);

    static {
        camera = new OrthographicCamera(WIDTH, HEIGHT);
        camera.position.set(WIDTH / 2.0F, HEIGHT / 2.0F, 0.0F);
        camera.update();
        sb.setProjectionMatrix(camera.combined);
        camera.viewportWidth = CAMERA_WIDTH;
        camera.viewportHeight = CAMERA_HEIGHT;
    }

    @SpirePatch2(clz = GameActionManager.class, method = "getNextAction")
    public static class TakeTurnCapture {
        @SpireInsertPatch(locator = BeforeTakeTurnLocator.class)
        public static void Insert(AbstractMonster ___m) {
            // Should only activate while in Mirror Zone
            if (!AbstractDungeon.player.hasPower(MirrorZonePower.POWER_ID)) {
                return;
            }

            /* MirrorMove Data */
            MirrorMoveData moveData = new MirrorMoveData();
            moveData.monster = ___m;

            EnemyMoveInfo enemyMove = ReflectionHacks.getPrivate(___m, AbstractMonster.class, "move");
            moveData.damage = enemyMove.baseDamage;
            if (enemyMove.isMultiDamage) {
                moveData.hits = enemyMove.multiplier;
            }
            curMoveData = moveData;
        }

        @SpirePatch(clz = GameActionManager.class, method = "addToBottom")
        @SpirePatch(clz = GameActionManager.class, method = "addToTop")
        public static class ActionManagerCapture {
            @SpirePrefixPatch
            public static void Prefix(GameActionManager __instance, AbstractGameAction action) {
                if (curMoveData == null) return;

                if (action instanceof GainBlockAction || action instanceof GainBlockRandomMonsterAction) {
                    curMoveData.block = action.amount;
                } else if (action instanceof ApplyPowerAction) {
                    AbstractPower p = ReflectionHacks.getPrivate(action, ApplyPowerAction.class, "powerToApply");
                    if (action.target instanceof AbstractPlayer) {
                        curMoveData.addDebuff(p);
                    } else if (action.target instanceof AbstractMonster) {
                        curMoveData.addBuff(p);
                    }
                } else if (action instanceof MakeTempCardInDiscardAction
                        || action instanceof MakeTempCardInDrawPileAction
                        || action instanceof MakeTempCardInDiscardAndDeckAction) {
                    AbstractCard c;
                    int numCards;
                    if (action instanceof MakeTempCardInDiscardAction) {
                        c = ReflectionHacks.getPrivate(action, MakeTempCardInDiscardAction.class, "c");
                        numCards = ReflectionHacks.getPrivate(action, MakeTempCardInDiscardAction.class, "numCards");
                    } else if (action instanceof MakeTempCardInDrawPileAction) {
                        c = ReflectionHacks.getPrivate(action, MakeTempCardInDrawPileAction.class, "cardToMake");
                        numCards = action.amount;
                    } else {
                        c = ReflectionHacks.getPrivate(action, MakeTempCardInDiscardAndDeckAction.class, "cardToMake");
                        numCards = 2;
                    }
                    if (c instanceof Dazed || c instanceof VoidCard) {
                        curMoveData.draw += numCards;
                    } else if (c instanceof Slimed || c instanceof Wound || c instanceof Burn) {
                        curMoveData.exhaustOther = true;
                    }
                } else if (action instanceof VampireDamageAction) {
                    curMoveData.vampire = true;
                } else if (action instanceof SummonGremlinAction
                        || action instanceof SpawnMonsterAction) {
                    curMoveData.randomOrbs++;
                } else if (action instanceof HealAction) {
                    curMoveData.tempHp = action.amount;
                } else if (action instanceof RemoveDebuffsAction) {
                    curMoveData.removeDebuff = true;
                }
            }
        }

        @SpirePostfixPatch
        public static void Postfix(GameActionManager __instance) {
            if (curMoveData == null) return;
            // Finalize the mirror move card data
            do {
                if (curMoveData.isEmpty()) {
                    break;
                }
                MirrorZone.addMoveData(curMoveData);

                AbstractMonster m = curMoveData.monster;

                if (MirrorZone.textureMissing(m.id)) {
                    // Create pixmap
                    float scale = Math.min(1, m.hb.width * 1.2f / CAMERA_WIDTH);
                    scale = Math.min(scale, m.hb.width * 2.0f / CAMERA_HEIGHT);
                    scale = Math.max(scale, m.hb.width / CAMERA_WIDTH);
                    scale = Math.max(scale, m.hb.height / CAMERA_HEIGHT);

                    camera.viewportWidth = CAMERA_WIDTH * scale;
                    camera.viewportHeight = CAMERA_HEIGHT * scale;
                    camera.position.set(
                            m.hb.cX,
                            m.hb.cY - m.hb.height * 0.1f + Math.max((m.hb.height - CAMERA_HEIGHT * scale * Settings.scale) / 2, 0),
                            0.0f);
                    camera.update();

                    Matrix4 sbMat = sb.getProjectionMatrix().cpy();
                    sb.setProjectionMatrix(camera.combined);
                    Matrix4 psbMat = CardCrawlGame.psb.getProjectionMatrix().cpy();
                    CardCrawlGame.psb.setProjectionMatrix(camera.combined);

                    fbo.begin();
                    Gdx.gl.glClearColor(0.0F, 0.0F, 0.0F, 0.0F);
                    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
                    Gdx.gl.glColorMask(true, true, true, true);

                    sb.begin();
                    MonsterRenderHack.RenderForMonsterCardPatch.shouldPatch = true;
                    m.render(sb);
                    MonsterRenderHack.RenderForMonsterCardPatch.shouldPatch = false;
                    sb.end();

                    CardCrawlGame.psb.setProjectionMatrix(psbMat);
                    sb.setProjectionMatrix(sbMat);

                    Pixmap pixmap = ScreenUtils.getFrameBufferPixmap(0, 0, fbo.getWidth(), fbo.getHeight());
                    Texture texture = new Texture(pixmap);
                    pixmap.dispose();
                    fbo.end();

                    fbo.begin();
                    sb.begin();
                    Gdx.gl.glClearColor(0.0F, 0.0F, 0.0F, 0.0F);
                    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
                    sb.draw(texture, WIDTH * 0.2f, HEIGHT * 0.2f, WIDTH * 0.6f, HEIGHT * 0.6f);
                    sb.end();

                    pixmap = ScreenUtils.getFrameBufferPixmap(0, 0, fbo.getWidth(), fbo.getHeight());
                    fbo.end();

                    MirrorZone.putPixmap(m.id, pixmap);
                }
            } while (false);

            curMoveData = null;
        }

        private static class BeforeTakeTurnLocator extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
                Matcher finalMatcher = new Matcher.FieldAccessMatcher(AbstractMonster.class, "intent");
                return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
            }
        }
    }
}
