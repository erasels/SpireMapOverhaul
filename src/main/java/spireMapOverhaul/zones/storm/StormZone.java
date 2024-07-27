package spireMapOverhaul.zones.storm;

import basemod.helpers.CardModifierManager;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.rooms.MonsterRoomElite;
import spireMapOverhaul.abstracts.AbstractZone;
import spireMapOverhaul.zoneInterfaces.CombatModifyingZone;
import spireMapOverhaul.zoneInterfaces.OnTravelZone;
import spireMapOverhaul.zoneInterfaces.RewardModifyingZone;
import spireMapOverhaul.zones.storm.cardmods.DampModifier;
import spireMapOverhaul.zones.storm.cardmods.ElectricModifier;
import spireMapOverhaul.zones.storm.powers.ConduitPower;

import java.util.ArrayList;

import static spireMapOverhaul.SpireAnniversary6Mod.*;
import static spireMapOverhaul.util.Wiz.*;
import static spireMapOverhaul.zones.storm.StormUtil.*;

public class StormZone extends AbstractZone implements CombatModifyingZone, RewardModifyingZone, OnTravelZone {
    public static final String ID = "Storm";

    public static final String THUNDER_KEY = makeID("Storm_Thunder");
    public static final String THUNDER_MP3 = makePath("audio/storm/thunder.mp3");
    public static final String RAIN_KEY = makeID("Storm_Rain");
    public static final String RAIN_MP3 = makePath("audio/storm/rain.mp3");

    public static ShaderProgram mapShader;
    static FrameBuffer fbo = new FrameBuffer(Pixmap.Format.RGBA8888, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), false, false);

    public StormZone() {
        super(ID, Icons.MONSTER);
        this.width = 2;
        this.maxWidth = 4;
        this.height = 3;
        this.maxHeight = 4;

    }

    @Override
    public AbstractZone copy() {
        return new StormZone();
    }

    @Override
    public Color getColor() {
        return Color.DARK_GRAY.cpy();
    }

    public void onEnterRoom() {
        conduitTarget = null;
        if(StormUtil.rainSoundId == 0L) {
            StormUtil.rainSoundId = CardCrawlGame.sound.playAndLoop(RAIN_KEY);
        }
    }
    public void onExit() {
        CardCrawlGame.sound.stop(RAIN_KEY, StormUtil.rainSoundId);
        StormUtil.rainSoundId = 0L;
        conduitTarget = null;
    }

    @Override
    public void modifyRewardCards(ArrayList<AbstractCard> cards) {
        if(AbstractDungeon.getCurrRoom() instanceof  MonsterRoomElite || AbstractDungeon.cardRng.randomBoolean()) {
            AbstractCard card = cards.get(AbstractDungeon.cardRng.random(cards.size() - 1));
            CardModifierManager.addModifier(card, new ElectricModifier());
        }
    }

    @Override
    public void atTurnStartPostDraw() {
        if (!AbstractDungeon.getMonsters().areMonstersBasicallyDead()) {
            atb(new AbstractGameAction() {
                @Override
                public void update() {
                    int validCards = countValidCardsInHandToMakeDamp();
                    if(validCards > 0) {
                        AbstractCard card = AbstractDungeon.player.hand.getRandomCard(AbstractDungeon.cardRandomRng);
                        while (!cardValidToMakeDamp(card)) { //Get random cards until you get one you can make damp
                            card = AbstractDungeon.player.hand.getRandomCard(AbstractDungeon.cardRandomRng);
                        }
                        CardModifierManager.addModifier(card, new DampModifier());
                        card.superFlash();
                    }
                    isDone = true;
                }
            });
        }

        ArrayList<AbstractMonster> mons = getEnemies();
        int totalActors = mons.size() + 1;

        if (AbstractDungeon.cardRandomRng.random(1, totalActors) == 1) {
            conduitTarget = AbstractDungeon.player;
            applyToSelf(new ConduitPower(AbstractDungeon.player));
        } else {
            AbstractMonster m = AbstractDungeon.getMonsters().getRandomMonster(null, true, AbstractDungeon.cardRandomRng);
            conduitTarget = m;
            applyToEnemy(m, new ConduitPower(m));
        }
    }

    @Override
    public void renderOnMap(SpriteBatch sb, float alpha) {
        if(getShaderConfig()) {
            sb.flush();
            fbo.begin();

            Gdx.gl.glClearColor(0.0F, 0.0F, 0.0F, 0.0F);
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
            super.renderOnMap(sb, alpha);
            sb.flush();
            fbo.end();


            TextureRegion region = new TextureRegion(fbo.getColorBufferTexture());
            region.flip(false, true);

            if (mapShader == null) {
                mapShader = StormUtil.initElectricShader(mapShader);
            }
            sb.setShader(mapShader);
            sb.setColor(Color.WHITE);
            mapShader.setUniformf("u_time", time);
            mapShader.setUniformf("u_bright_time", 0.5f);

            sb.draw(region, 0, 0);
            sb.setShader(null);
            sb.flush();
        } else {
            super.renderOnMap(sb, alpha);
        }
    }
}
