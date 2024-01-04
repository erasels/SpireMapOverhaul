package spireMapOverhaul.zones.brokenSpace;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.AbstractEvent;
import com.megacrit.cardcrawl.events.AbstractImageEvent;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.map.MapRoomNode;
import com.megacrit.cardcrawl.random.Random;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.rooms.EventRoom;
import com.megacrit.cardcrawl.rooms.RestRoom;
import spireMapOverhaul.zones.WingBootEvent;

import static com.megacrit.cardcrawl.core.CardCrawlGame.dungeon;
import static com.megacrit.cardcrawl.dungeons.AbstractDungeon.currMapNode;
import static com.megacrit.cardcrawl.dungeons.AbstractDungeon.scene;

public class FakeEventRoom extends EventRoom {
    private EventRoom fakeRoom;

    public FakeEventRoom(AbstractImageEvent event) {
        super();
        this.phase = RoomPhase.EVENT;
        this.mapSymbol = "?";

        this.mapImg = ImageMaster.MAP_NODE_EVENT;// 14
        this.mapImgOutline = ImageMaster.MAP_NODE_EVENT_OUTLINE;// 15


        fakeRoom = new EventRoom();
        this.event = event;
    }

    @Override
    public void onPlayerEntry() {
        AbstractDungeon.overlayMenu.proceedButton.hide();
        AbstractDungeon.rs = AbstractDungeon.RenderScene.EVENT;// 16

        fakeRoom.event = event;
        fakeRoom.event.onEnterRoom();
    }

    @Override
    public AbstractCard.CardRarity getCardRarity(int roll) {
        return fakeRoom.getCardRarity(roll);
    }

    @Override
    public void update() {
        fakeRoom.update();
    }

    @Override
    public void render(SpriteBatch sb) {
        fakeRoom.render(sb);
        fakeRoom.renderEventTexts(sb);

    }

    @Override
    public void renderAboveTopPanel(SpriteBatch sb) {
        fakeRoom.renderAboveTopPanel(sb);
    }


}