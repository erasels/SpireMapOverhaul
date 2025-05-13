package spireMapOverhaul.zones.cafemerchant;

import java.lang.reflect.InvocationTargetException;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon.CurrentScreen;
import com.megacrit.cardcrawl.events.AbstractEvent;
import com.megacrit.cardcrawl.events.RoomEventDialog;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.rooms.ShopRoom;

import basemod.ReflectionHacks;
import spireMapOverhaul.SpireAnniversary6Mod;

public class CafeMerchantEvent extends AbstractEvent {

    public static final String ID = SpireAnniversary6Mod.makeID("CafeMerchantEvent");

    private static final float MERCHANT_X = 1250 * Settings.xScale;
    private static final float MERCHANT_Y = AbstractDungeon.floorY - 15 * Settings.yScale;
    private static Class<?> abstractMerchant;
    private static Class<?> cafeUtil;
    private static Class<?> abstractCafeInteractable;
    private static Class<?> cafeRoom;

    private Object merchant;
    private String merchantID;

    public CafeMerchantEvent() {
        this.merchantID = CafeMerchantZone.getCurrentZoneMerchantID();
        this.body = "";
        this.hasDialog = true;
        this.hasFocus = true;
        AbstractDungeon.getCurrRoom().phase = AbstractRoom.RoomPhase.COMPLETE;
        try {
            abstractMerchant = Class.forName("spireCafe.abstracts.AbstractMerchant");
            abstractCafeInteractable = Class.forName("spireCafe.abstracts.AbstractCafeInteractable");
            cafeUtil = Class.forName("spireCafe.util.CafeUtil");
            cafeRoom = Class.forName("spireCafe.CafeRoom");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException("Error retrieving classes from Spire Cafe", e);
        }
    }

    @Override
    public void onEnterRoom() {
        super.onEnterRoom();
        try {
            this.merchant = cafeUtil.getMethod("createInteractable", String.class, float.class, float.class)
                .invoke(null, this.merchantID, MERCHANT_X, MERCHANT_Y);
            cafeUtil.getMethod("makeFaceLeft", abstractCafeInteractable).invoke(null, merchant);
            abstractMerchant.getMethod("initialize").invoke(merchant);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException | SecurityException | IllegalArgumentException e) {
            throw new RuntimeException("Error initializing Cafe merchant", e);
        }
        AbstractDungeon.overlayMenu.proceedButton.setLabel(ShopRoom.TEXT[0]);
        }

    @Override
    public void update() {
        super.update();
        if (!RoomEventDialog.waitForInput) {
            this.buttonEffect(this.roomEventText.getSelectedOption());
        }
        try {
            abstractMerchant.getMethod("update").invoke(merchant);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
            e.printStackTrace();
            throw new RuntimeException("Error updating Cafe merchant", e);
        }
        // We only have one interactable here, so no need to have this flag set to check for conflicts.
        ReflectionHacks.setPrivateStatic(cafeRoom, "isInteracting", false);

        if (AbstractDungeon.screen == CurrentScreen.NONE) {
            AbstractDungeon.overlayMenu.proceedButton.show();
        } else {
            AbstractDungeon.overlayMenu.proceedButton.hideInstantly();
        }
    }

    @Override
    public void render(SpriteBatch sb) {
        sb.setColor(Color.WHITE);
        sb.setBlendFunction(GL30.GL_SRC_ALPHA, GL30.GL_ONE_MINUS_SRC_ALPHA);
        try {
            abstractMerchant.getMethod("renderAnimation",SpriteBatch.class).invoke(merchant,sb);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
            e.printStackTrace();
            throw new RuntimeException("Error rendering Cafe merchant", e);
        }
    }

    @Override
    protected void buttonEffect(int arg0) {
    }

    //Remove Event Text Shadow
    @Override
    public void updateDialog() {
    }

    @Override
    public void renderText(SpriteBatch sb) {
    }

    @Override
    public void renderRoomEventPanel(SpriteBatch sb) {
    }
    
}
