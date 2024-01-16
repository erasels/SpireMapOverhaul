package spireMapOverhaul.zones.divinitiesgaze.divinities.impl;

import basemod.ReflectionHacks;
import com.megacrit.cardcrawl.cards.status.Wound;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import spireMapOverhaul.zones.divinitiesgaze.cards.Verdict;
import spireMapOverhaul.zones.divinitiesgaze.divinities.BaseDivineBeing;

import java.util.function.Consumer;

public class Jurors extends BaseDivineBeing {

  static {
    new Jurors();
  }

  public Jurors() {
    super(Verdict.ID, Wound.ID);
  }

  @Override
  public Consumer<Integer> doEventButtonAction() {
    return (i) -> {};
  }

  @Override
  public String getCombatPhaseKey() {
    if(AbstractDungeon.eliteMonsterList.isEmpty()) {
      ReflectionHacks.privateMethod(AbstractDungeon.class, "generateElites")
          .invoke(CardCrawlGame.dungeon);
    }

    String elite = AbstractDungeon.eliteMonsterList.get(0);
    AbstractDungeon.eliteMonsterList.remove(0);
    return elite;
  }
}
