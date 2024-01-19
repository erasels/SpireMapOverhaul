package spireMapOverhaul.zones.gremlinTown.events.misc;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.helpers.controller.CInputActionSet;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import com.megacrit.cardcrawl.localization.UIStrings;

import static spireMapOverhaul.SpireAnniversary6Mod.makeImagePath;

public class Chest {
    private static final UIStrings uiStrings;
    public static final String[] TEXT;
    public static final float CHEST_LOC_X;
    public static final float CHEST_LOC_Y;
    private static final int RAW_W = 512;
    public Hitbox hb;
    private static final Texture img;
    private static final Texture openedImg;
    public boolean isOpen;
    public boolean hide;

    static {
        uiStrings = CardCrawlGame.languagePack.getUIString("AbstractChest");
        TEXT = uiStrings.TEXT;
        CHEST_LOC_X = (float)Settings.WIDTH / 2.0F + 348.0F * Settings.scale;
        CHEST_LOC_Y = AbstractDungeon.floorY + 192.0F * Settings.scale;
        img = new Texture("images/npcs/largeChest.png");
        openedImg = new Texture(makeImagePath("events/GremlinTown/EventChestOpened.png"));
    }

    public Chest() {
        isOpen = false;
        hide = false;
        hb = new Hitbox(340.0F * Settings.scale, 200.0F * Settings.scale);
        hb.move(CHEST_LOC_X, CHEST_LOC_Y - 120.0F * Settings.scale);
    }

    public void open() {
        isOpen = true;
        CardCrawlGame.sound.play("CHEST_OPEN");
        AbstractDungeon.overlayMenu.proceedButton.hideInstantly();
    }

    public void update() {
        hb.update();
        if ((hb.hovered && InputHelper.justClickedLeft || CInputActionSet.select.isJustPressed())
                && !AbstractDungeon.isScreenUp && !isOpen) {
            InputHelper.justClickedLeft = false;
            open();
        }
    }

    public void render(SpriteBatch sb) {
        if (hide)
            return;
        sb.setColor(Color.WHITE);
        float angle = 0.0F;

        float xxx = (float)Settings.WIDTH / 2.0F + 348.0F * Settings.scale;
        if (isOpen) {
            sb.draw(openedImg, xxx - 256.0F, CHEST_LOC_Y - 256.0F, 256.0F, 256.0F, RAW_W, RAW_W,
                    Settings.scale, Settings.scale, angle, 0, 0, 512, 512, false, false);
        } else {
            sb.draw(img, CHEST_LOC_X - 256.0F, CHEST_LOC_Y - 256.0F, 256.0F, 256.0F, RAW_W, RAW_W,
                    Settings.scale, Settings.scale, angle, 0, 0, 512, 512, false, false);
            if (hb.hovered) {
                sb.setBlendFunction(770, 1);
                sb.setColor(new Color(1.0F, 1.0F, 1.0F, 0.3F));
                sb.draw(img, CHEST_LOC_X - 256.0F, CHEST_LOC_Y - 256.0F, 256.0F, 256.0F, RAW_W, RAW_W,
                        Settings.scale, Settings.scale, angle, 0, 0, 512, 512, false, false);
                sb.setBlendFunction(770, 771);
            }
        }

        if (Settings.isControllerMode && !isOpen) {
            sb.setColor(Color.WHITE);
            sb.draw(CInputActionSet.select.getKeyImg(), CHEST_LOC_X - 32.0F - 150.0F * Settings.scale,
                    CHEST_LOC_Y - 32.0F - 210.0F * Settings.scale, 32.0F, 32.0F, 64.0F, 64.0F,
                    Settings.scale, Settings.scale, 0.0F, 0, 0, 64, 64, false, false);
        }

        hb.render(sb);
    }
}
