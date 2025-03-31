package spireMapOverhaul.zones.humidity;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.Loader;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.dungeons.Exordium;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.relics.FrozenEgg2;
import com.megacrit.cardcrawl.rooms.MonsterRoom;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndObtainEffect;
import spireMapOverhaul.SpireAnniversary6Mod;
import spireMapOverhaul.abstracts.AbstractZone;
import spireMapOverhaul.util.TexLoader;
import spireMapOverhaul.util.Wiz;
import spireMapOverhaul.zoneInterfaces.CombatModifyingZone;
import spireMapOverhaul.zoneInterfaces.OnTravelZone;
import spireMapOverhaul.zoneInterfaces.RenderableZone;
import spireMapOverhaul.zones.humidity.cards.powerelic.implementation.PowerelicCard;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

public class HumidityZone extends AbstractZone implements OnTravelZone, CombatModifyingZone, RenderableZone {
    public static final String ID = "Humidity";

    public static final boolean DEBUG_PLAYER_IS_ALWAYS_IN_ZONE = false;
    public static final String DEBUG_FORCE_EVENT_ID = "";

    public HumidityZone() {
        super(ID, Icons.MONSTER);
        width = 2;
        height = 4;
        maxWidth = 4;
        maxHeight = 5;
    }

    @Override
    public boolean canSpawn() {
        //return !Loader.isModLoadedOrSideloaded("humility") && Arrays.asList(Exordium.ID, TheCity.ID, TheBeyond.ID).contains(AbstractDungeon.id);
        return !Loader.isModLoadedOrSideloaded("humility") && Arrays.asList(Exordium.ID).contains(AbstractDungeon.id);
    }

    @Override
    public AbstractZone copy() {
        return new HumidityZone();
    }

    @Override
    public Color getColor() {
        return Color.valueOf("3498db");
    }

    protected boolean allowAdditionalEntrances() {
        return false;
    }

    protected boolean allowSideConnections() {
        return false;
    }

    @Override
    protected boolean canIncludeEarlyRows() {
        //return !(AbstractDungeon.actNum == 1);
        return true;
    }

    private static AbstractCard recentlyGeneratedPowerelic;

    @Override
    public void onEnterRoom() {
        recentlyGeneratedPowerelic=null;
    }

    @Override
    public void onEnter() {
        AbstractRelic relic;
        relic = CardedRareRelicList.fetchRelicForCarding();

        relic.instantObtain(Wiz.adp(),Wiz.adp().relics.size(),true);

        AbstractCard card;

        if(!Loader.isModLoaded("anniv7")){
            card = PowerelicCard.fromActiveRelic(relic);
        }else{
            //if Spire Cafe is installed, use the cafe version of the card so both mods will recognize it
            try {
                Class<?> clz = Class.forName("spireCafe.interactables.patrons.powerelic.implementation.PowerelicCard");
                Method m = clz.getMethod("fromActiveRelic",AbstractRelic.class);
                card=(AbstractCard)m.invoke(null,relic);
            } catch (IllegalAccessException | ClassNotFoundException | NoSuchMethodException |
                     InvocationTargetException e) {
                e.printStackTrace();
                throw new RuntimeException("Failed when trying to call spireCafe's PowerelicCard.fromActiveRelic()", e);
            }
        }

        //As a side effect of calling fromActiveRelic, the relic is automatically captured within the new card
        //We must still remove the relic from the player's equipped relic list manually
        AbstractDungeon.effectsQueue.add(new ShowCardAndObtainEffect(card, (float) Settings.WIDTH/2f, (float)Settings.HEIGHT/2f));
        Wiz.adp().relics.remove(relic);
        Wiz.adp().reorganizeRelics();

        //If the first room of the zone is a combat, the powerelic card will not be in the player's deck by the time combat begins, so add it to the draw pile.
        //We don't actually know if the room is a combat at this point, so save the card and check later
        recentlyGeneratedPowerelic = card;

    }

    @Override
    public void atPreBattle(){
        if(AbstractDungeon.getCurrRoom() instanceof MonsterRoom) {
            if (recentlyGeneratedPowerelic != null) {
                AbstractCard card = recentlyGeneratedPowerelic.makeSameInstanceOf();
                //the new card, which has not been added to the deck at this point, has also not been upgraded yet
                if(Wiz.adp().hasRelic(FrozenEgg2.ID))
                    card.upgrade();
                AbstractDungeon.player.drawPile.addToRandomSpot(card);
            }
        }
    }

    public static boolean isInZone() {
        if(DEBUG_PLAYER_IS_ALWAYS_IN_ZONE)return true;
        return Wiz.getCurZone() instanceof HumidityZone;
    }

    public static boolean isNotInZone() {
        return !isInZone();
    }

    public static String instrumentTerniary(){
        return HumidityZone.class.getName()+".isNotInZone() ? ";
    }


}
