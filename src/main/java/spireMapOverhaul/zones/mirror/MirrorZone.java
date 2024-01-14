package spireMapOverhaul.zones.mirror;

import basemod.abstracts.CustomCard;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import spireMapOverhaul.SpireAnniversary6Mod;
import spireMapOverhaul.abstracts.AbstractZone;
import spireMapOverhaul.zoneInterfaces.CombatModifyingZone;
import spireMapOverhaul.zoneInterfaces.ModifiedEventRateZone;
import spireMapOverhaul.zones.mirror.cards.MirrorMove;
import spireMapOverhaul.zones.mirror.events.WeirdMirrorsEvent;
import spireMapOverhaul.zones.mirror.powers.MirrorZonePower;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MirrorZone extends AbstractZone implements CombatModifyingZone, ModifiedEventRateZone {
    public static final String ID = "Mirror";

    public static ArrayList<MirrorMoveData> previousMoveData = new ArrayList<>();
    public static MirrorMoveData chosenMoveData = null;
    public static HashMap<String, Texture> largeTextures = new HashMap<>();
    public static HashMap<String, Texture> smallTextures = new HashMap<>();

    public MirrorZone() {
        super(ID, Icons.MONSTER, Icons.EVENT);
        width = 3;
        height = 3;
        maxHeight = 4;
    }

    public static void resetPixmap() {
        for (Texture texture : largeTextures.values()) {
            texture.dispose();
        }
        largeTextures.clear();
        for (Map.Entry<String, Texture> entry : smallTextures.entrySet()) {
            entry.getValue().dispose();
            CustomCard.imgMap.remove(MirrorMove.getPortraitId(entry.getKey()));
        }
        smallTextures.clear();
    }

    public static void resetMoveData() {
        if (!previousMoveData.isEmpty()) {
            AbstractDungeon.player.gameHandSize++;
        }
        previousMoveData.clear();
        chosenMoveData = null;
    }

    public static void addMoveData(MirrorMoveData data) {
        if (previousMoveData.isEmpty()) {
            AbstractDungeon.player.gameHandSize--;
        }
        previousMoveData.add(data);
    }

    public static void putPixmap(String id, Pixmap pixmap) {
        Pixmap pixmapHalf = new Pixmap(250, 190, pixmap.getFormat());
        pixmapHalf.drawPixmap(pixmap,
                0, 0, pixmap.getWidth(), pixmap.getHeight(),
                0, 0, pixmapHalf.getWidth(), pixmapHalf.getHeight()
        );
        Texture texture = new Texture(pixmap);
        Texture textureHalf = new Texture(pixmapHalf);
        pixmapHalf.dispose();
        pixmap.dispose();

        largeTextures.put(id, texture);
        smallTextures.put(id, textureHalf);
        CustomCard.imgMap.put(MirrorMove.getPortraitId(id), textureHalf);
    }

    public static boolean textureMissing(String id) {
        return !largeTextures.containsKey(id);
    }

    @Override
    public AbstractZone copy() {
        return new MirrorZone();
    }

    @Override
    public Color getColor() {
        return new Color(0.4f, 0.5f, 0.8f, 0.75f);
    }

    @Override
    public void loadStrings() {
        super.loadStrings();
        if (SpireAnniversary6Mod.initializedStrings) {
            MirrorMove.initializeDescriptionParts();
        }
    }

    @Override
    public void atBattleStartPreDraw() {
        resetPixmap();
        // Not calling resetMoveData() here because gameHandSize shouldn't increase when loading a save file
        previousMoveData.clear();
        chosenMoveData = null;

        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new MirrorZonePower(AbstractDungeon.player)));
    }

    @Override
    public void atTurnStart() {
        if (!previousMoveData.isEmpty()) {
            chosenMoveData = previousMoveData.get(AbstractDungeon.cardRandomRng.random(previousMoveData.size() - 1));
            MirrorMove c = new MirrorMove();
            c.applyPowers();
            AbstractDungeon.actionManager.addToBottom(new MakeTempCardInHandAction(c));
            AbstractPower pow = AbstractDungeon.player.getPower(MirrorZonePower.POWER_ID);
            if (pow != null) {
                pow.flash();
            }
        }
    }

    @Override
    public void atTurnEnd() {
        resetMoveData();
    }

    @Override
    public void onVictory() {
        resetPixmap();
        resetMoveData();
    }

    @Override
    public String getCombatText() {
        return MirrorZonePower.DESCRIPTIONS[0];
    }

    @Override
    public String forceEvent() {
        return ModifiedEventRateZone.returnIfUnseen(WeirdMirrorsEvent.ID);
    }
}
