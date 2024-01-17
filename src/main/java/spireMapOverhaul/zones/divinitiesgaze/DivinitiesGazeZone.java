package spireMapOverhaul.zones.divinitiesgaze;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDrawPileAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.vfx.SpeechBubble;
import spireMapOverhaul.abstracts.AbstractZone;
import spireMapOverhaul.util.Wiz;
import spireMapOverhaul.zoneInterfaces.CombatModifyingZone;
import spireMapOverhaul.zoneInterfaces.ModifiedEventRateZone;
import spireMapOverhaul.zones.divinitiesgaze.divinities.DivineBeing;
import spireMapOverhaul.zones.divinitiesgaze.divinities.DivineBeingManager;
import spireMapOverhaul.zones.divinitiesgaze.divinities.impl.Jurors;
import spireMapOverhaul.zones.divinitiesgaze.events.DivineVisitor;
import spireMapOverhaul.zones.divinitiesgaze.powers.VerdictPower;

import java.util.Random;

public class DivinitiesGazeZone extends AbstractZone implements ModifiedEventRateZone, CombatModifyingZone {

  public static final String ID = "DivinitiesGaze";

  public DivinitiesGazeZone() {
    super(ID, Icons.MONSTER, Icons.EVENT);
    this.width = 3;
    this.height = 4;
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
  public void atPreBattle() {
    DivineBeing being = DivineBeingManager.getDivinityForCombat();
    AbstractCard boon = CardLibrary.getCard(being.getDivinityStrings().getBoonCardId());
    AbstractCard status = CardLibrary.getCard(being.getDivinityStrings().getStatusCardId());
    Wiz.atb(new MakeTempCardInDrawPileAction(boon, 1, true, true));
    Wiz.atb(new MakeTempCardInDrawPileAction(status, AbstractDungeon.actNum, true, true));
    String[] quotes = being.getDivinityStrings().getPreCombatQuotes();
    Wiz.atb(new AbstractGameAction() {
      @Override
      public void update() {
        AbstractDungeon.effectList.add(new SpeechBubble(Settings.WIDTH / 2f, Settings.HEIGHT * 3f / 4f, 3, quotes[new Random().nextInt(quotes.length)], false));
        isDone = true;
      }
    });
  }

  @Override
  public void atBattleStart() {
    if (Jurors.doApplyVerdictOnCombatStart) {
      Jurors.doApplyVerdictOnCombatStart = false;
      Wiz.atb(new AbstractGameAction() {
        @Override
        public void update() {
          AbstractDungeon.getMonsters().monsters.forEach(m -> Wiz.att(new ApplyPowerAction(m, AbstractDungeon.player, new VerdictPower(m,  10), 10)));
          this.isDone = true;
        }
      });
    }
  }
}
