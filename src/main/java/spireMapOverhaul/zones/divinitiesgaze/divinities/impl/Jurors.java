package spireMapOverhaul.zones.divinitiesgaze.divinities.impl;

import basemod.ReflectionHacks;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.status.Wound;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.daily.mods.BigGameHunter;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ModHelper;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.relics.BlackStar;
import com.megacrit.cardcrawl.rewards.RewardItem;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import spireMapOverhaul.util.Wiz;
import spireMapOverhaul.zones.divinitiesgaze.cards.Verdict;
import spireMapOverhaul.zones.divinitiesgaze.divinities.BaseDivineBeing;
import spireMapOverhaul.zones.divinitiesgaze.powers.VerdictPower;

import java.util.function.Consumer;

@SuppressWarnings("unused")
public class Jurors extends BaseDivineBeing {

  public static final String ID = "Jurors";

  public Jurors() {
    super(ID, Verdict.ID, Wound.ID);
  }

  @Override
  public Consumer<Integer> doEventButtonAction() {
    return (i) -> {};
  }

  @Override
  public String getCombatPhaseKey() {
    if(AbstractDungeon.eliteMonsterList.isEmpty()) {
      ReflectionHacks.privateMethod(AbstractDungeon.class, "generateElites").invoke(CardCrawlGame.dungeon);
    }

    String elite = AbstractDungeon.eliteMonsterList.get(0);
    AbstractDungeon.eliteMonsterList.remove(0);
    return elite;
  }

  @Override
  public Consumer<AbstractRoom> getCombatRewards() {
    return (room) -> {
      AbstractDungeon.getCurrRoom().rewards.add(new RewardItem(AbstractDungeon.returnRandomRelic(getRandomTier())));
      if(AbstractDungeon.player.hasRelic(BlackStar.ID)) {
        AbstractDungeon.getCurrRoom().rewards.add(new RewardItem(AbstractDungeon.returnRandomNonCampfireRelic(getRandomTier())));
      }
    };
  }

  private AbstractRelic.RelicTier getRandomTier() {
    AbstractRelic.RelicTier tier = AbstractRelic.RelicTier.COMMON;
    int roll = AbstractDungeon.relicRng.random(0, 99);
    if (ModHelper.isModEnabled(BigGameHunter.ID)) {
      roll += 10;
    }

    if (roll >= 50) {
      tier = roll > 82 ? AbstractRelic.RelicTier.RARE : AbstractRelic.RelicTier.UNCOMMON;
    }
    return tier;
  }

  @Override
  public void doEnterCombat() {
    super.doEnterCombat();
    Wiz.atb(new AbstractGameAction() {
      @Override
      public void update() {
        Wiz.att(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new VerdictPower(AbstractDungeon.player, 3), 3));
        AbstractDungeon.getMonsters().monsters.forEach(m -> Wiz.att(new ApplyPowerAction(m, AbstractDungeon.player, new VerdictPower(m,  3), 3)));
        this.isDone = true;
      }
    });
  }

  @Override
  public String[] getKeywordsForCardChoice() {
    return new String[]{VerdictPower.KEYWORD};
  }

  @Override
  public String[] getKeywordsForCustomChoice() {
    return new String[]{VerdictPower.KEYWORD};
  }
}
