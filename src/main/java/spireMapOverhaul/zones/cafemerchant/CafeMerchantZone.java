package spireMapOverhaul.zones.cafemerchant;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.Loader;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.map.MapRoomNode;
import com.megacrit.cardcrawl.random.Random;

import basemod.ReflectionHacks;
import spireMapOverhaul.abstracts.AbstractZone;
import spireMapOverhaul.patches.ZonePatches;
import spireMapOverhaul.zoneInterfaces.ModifiedEventRateZone;
import spireMapOverhaul.zones.cafemerchant.rooms.CafeMerchantEventRoom;

public class CafeMerchantZone extends AbstractZone implements ModifiedEventRateZone {

    private static final String ID = "CafeMerchant";
    
    private static Class<?> cafeUtil;
    private static Class<?> abstractCafeInteractable;
    
    private String merchantID;
    private Object merchantInteractable;
    private String merchantName;


    public CafeMerchantZone() {
        super(ID, Icons.SHOP);
        this.width = 1;
        this.maxWidth = 1;
        this.height = 1;
        this.maxHeight = 1;
        this.merchantName = "";
    }

    public CafeMerchantZone(String merchantID) {
        this();
        this.merchantID = merchantID;
    }

    @Override
    public AbstractZone copy() {
        return new CafeMerchantZone(merchantID);
    }

    @Override
    public Color getColor() {
        Color ret = Color.GOLDENROD;
        ret.a = 1f;
        return ret;
    }

    @Override
    protected boolean canIncludeEarlyRows() {
        return false;
    }

    @Override
    protected boolean canIncludeTreasureRow() {
        return false;
    }

    @Override
    protected boolean canIncludeFinalCampfireRow() {
        return false;
    }

    @Override
    public void manualRoomPlacement(Random rng) {
        for (MapRoomNode node : nodes) {
            node.setRoom(new CafeMerchantEventRoom(() -> {
                CafeMerchantEvent event = new CafeMerchantEvent();
                return event;
            }));
        node.room.setMapSymbol("$");
        node.room.setMapImg(ImageMaster.MAP_NODE_MERCHANT, ImageMaster.MAP_NODE_MERCHANT_OUTLINE);
        }
    }

    @Override
    public boolean canSpawn() {
        if (!Loader.isModLoaded("anniv7")) {
            return false;
        }
        // Looking for the needed CafeUtil class in Spire Cafe to ensure mod is updated
        try {
            @SuppressWarnings("unused")
            Class<?> test = Class.forName("spireCafe.util.CafeUtil");
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }

    @Override
    public void renderOnMap(SpriteBatch sb, float alpha) {
        super.renderOnMap(sb, alpha);
        for (MapRoomNode node : nodes) {
            node.room.setMapSymbol("$");
            node.room.setMapImg(ImageMaster.MAP_NODE_MERCHANT, ImageMaster.MAP_NODE_MERCHANT_OUTLINE);
        }
    }

    @Override
    public void mapGenDone(ArrayList<ArrayList<MapRoomNode>> map) {
        super.mapGenDone(map);

        try {
            abstractCafeInteractable = Class.forName("spireCafe.abstracts.AbstractCafeInteractable");
            cafeUtil = Class.forName("spireCafe.util.CafeUtil");

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException("Error retrieving classes from Spire Cafe", e);
        }

        List<String> merchantIDList = null;
        try {
            merchantIDList = (List<String>) cafeUtil.getMethod("getValidMerchantIDs").invoke(null);
            if (merchantIDList.isEmpty()) {
                cafeUtil.getMethod("clearRunSeenInteractables").invoke(null);
                merchantIDList = (List<String>) cafeUtil.getMethod("getValidMerchantIDs").invoke(null);
            }
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
            e.printStackTrace();
            throw new RuntimeException("Error loading merchants from Spire Cafe", e);
        }
        Collections.shuffle(merchantIDList, new java.util.Random(AbstractDungeon.mapRng.randomLong()));
        merchantID = merchantIDList.get(0);

        try {
            this.merchantInteractable = cafeUtil.getMethod("createInteractable", String.class, float.class, float.class).invoke(null, merchantID, 0f, 0f);
            this.merchantName = (String) ReflectionHacks.getCachedField(abstractCafeInteractable, "name").get(merchantInteractable);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
            e.printStackTrace();
            throw new RuntimeException(String.format("Error retrieving interactable class (%s) from Spire Cafe", merchantID), e);
        }
        
        this.name = this.merchantName;
        this.updateDescription();
    }

    public static String getCurrentZoneMerchantID() {
        AbstractZone currentZone = ZonePatches.currentZone();

        if (!(currentZone instanceof CafeMerchantZone))
            return null;

        return ((CafeMerchantZone) currentZone).merchantID;
    }

    public void markMerchantSeen() {
        try {
            CafeMerchantZone.cafeUtil.getMethod("markInteractableAsSeen", String.class).invoke(null, this.merchantID);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
            throw new RuntimeException(String.format("Error marking interactable (%s) from Spire Cafe seen", this.merchantID), e);
        }
    }
}
