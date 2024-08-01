package spireMapOverhaul.zones.divinitiesgaze;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDrawPileAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.random.Random;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.rooms.EventRoom;
import com.megacrit.cardcrawl.vfx.SpeechBubble;
import spireMapOverhaul.abstracts.AbstractZone;
import spireMapOverhaul.util.Wiz;
import spireMapOverhaul.zoneInterfaces.CombatModifyingZone;
import spireMapOverhaul.zoneInterfaces.ModifiedEventRateZone;
import spireMapOverhaul.zones.divinitiesgaze.divinities.DivineBeing;
import spireMapOverhaul.zones.divinitiesgaze.divinities.DivineBeingManager;
import spireMapOverhaul.zones.divinitiesgaze.events.DivineVisitor;

import java.util.ArrayList;

public class DivinitiesGazeZone extends AbstractZone implements ModifiedEventRateZone, CombatModifyingZone {

  public static final String ID = "DivinitiesGaze";

  public DivinitiesGazeZone() {
    super(ID, Icons.MONSTER, Icons.EVENT);
    this.width = 2;
    this.maxWidth = 3;
    this.height = 4;
    this.maxHeight = 5;
  }

  @Override
  public AbstractZone copy() {
    return new DivinitiesGazeZone();
  }

  @Override
  public Color getColor() {
    return new Color(0.3451f, 0.3137f, 0.9137f, 0.8f);
  }

  @Override
  public String forceEvent() {
    return ModifiedEventRateZone.returnIfUnseen(DivineVisitor.ID);
  }

  @Override
  public void distributeRooms(Random rng, ArrayList<AbstractRoom> roomList) {
    //Guarantee at least one event
    placeRoomRandomly(rng, roomOrDefault(roomList, (room) -> room instanceof EventRoom, EventRoom::new));
  }

  @Override
  public void atPreBattle() {
    DivineBeing being = DivineBeingManager.getDivinityForCombat();
    AbstractCard boon = CardLibrary.getCard(being.getDivinityStrings().getBoonCardId());
    AbstractCard status = CardLibrary.getCard(being.getDivinityStrings().getStatusCardId());
    Wiz.atb(new MakeTempCardInDrawPileAction(boon, 1, true, true));
    Wiz.atb(new MakeTempCardInDrawPileAction(status, AbstractDungeon.actNum + 1, true, true));
    String[] quotes = being.getDivinityStrings().getPreCombatQuotes();
    Wiz.atb(new AbstractGameAction() {
      @Override
      public void update() {
        AbstractDungeon.effectList.add(new SpeechBubble(Settings.WIDTH / 2f, Settings.HEIGHT * 3f / 4f, 3,
            quotes[MathUtils.random(quotes.length - 1)], false));
        isDone = true;
      }
    });
  }
}
