package spireMapOverhaul.zones.divinitiesgaze.divinities.impl;

import basemod.ReflectionHacks;
import com.megacrit.cardcrawl.cards.status.Wound;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ModHelper;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rewards.RewardItem;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import spireMapOverhaul.zones.divinitiesgaze.cards.Verdict;
import spireMapOverhaul.zones.divinitiesgaze.divinities.BaseDivineBeing;

import java.util.function.Consumer;

public class Jurors extends BaseDivineBeing {
  public static boolean doApplyVerdictOnCombatStart = false;

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
    doApplyVerdictOnCombatStart = true;
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
      if(AbstractDungeon.player.hasRelic("Black Star")) {
        AbstractDungeon.getCurrRoom().rewards.add(new RewardItem(AbstractDungeon.returnRandomNonCampfireRelic(getRandomTier())));
      }
    };
  }

  private AbstractRelic.RelicTier getRandomTier() {
    AbstractRelic.RelicTier tier = AbstractRelic.RelicTier.COMMON;
    int roll = AbstractDungeon.relicRng.random(0, 99);
    if (ModHelper.isModEnabled("Elite Swarm")) {
      roll += 10;
    }

    if (roll >= 50) {
      tier = roll > 82 ? AbstractRelic.RelicTier.RARE : AbstractRelic.RelicTier.UNCOMMON;
    }
    return tier;
  }
}
