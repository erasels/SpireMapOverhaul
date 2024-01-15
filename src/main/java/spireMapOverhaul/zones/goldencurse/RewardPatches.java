package spireMapOverhaul.zones.goldencurse;

import basemod.abstracts.CustomReward;
import com.evacipated.cardcrawl.modthespire.lib.SpireField;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch2;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rewards.RewardItem;
import spireMapOverhaul.patches.ZonePatches;

import static com.megacrit.cardcrawl.dungeons.AbstractDungeon.player;
import static spireMapOverhaul.SpireAnniversary6Mod.makeID;


public class RewardPatches {
    @SpirePatch2(
            clz = RewardItem.class,
            method = SpirePatch.CLASS
    )
    public static class RewardItemFields {
        public static SpireField<Integer> cost = new SpireField<>(() -> 0);
    }

    @SpirePatch2(
            clz = RewardItem.class,
            method = SpirePatch.CONSTRUCTOR,
            paramtypez = {}

    )
    @SpirePatch2(
            clz = RewardItem.class,
            method = SpirePatch.CONSTRUCTOR,
            paramtypez = {AbstractRelic.class}

    )
    @SpirePatch2(
            clz = RewardItem.class,
            method = SpirePatch.CONSTRUCTOR,
            paramtypez = {RewardItem.class, RewardItem.RewardType.class}

    )
    @SpirePatch2(
            clz = RewardItem.class,
            method = SpirePatch.CONSTRUCTOR,
            paramtypez = {AbstractPotion.class}

    )
    public static class RewardItemConstructors {
        public static void Postfix(RewardItem __instance) {
            if (!(ZonePatches.currentZone() instanceof GoldenCurseZone)) {
                return;
            }
            int goldAmt = GoldenCurseZone.getRewardCost(__instance);

            RewardItemFields.cost.set(__instance, goldAmt);
            if (goldAmt > 0) {
                __instance.text = __instance.text + TEXT[0] + goldAmt;
            }
        }

    }

    @SpirePatch2(
            clz = RewardItem.class,
            method = "update"
    )
    @SpirePatch2(
            clz = CustomReward.class,
            method = "update"
    )
    public static class RewardItemUpdate {
        public static void Postfix(RewardItem __instance) {
            if (!(ZonePatches.currentZone() instanceof GoldenCurseZone)) {
                return;
            }
            if (!(RewardItemFields.cost.get(__instance) <= player.gold)) {
                __instance.isDone = false;

            }
            __instance.redText = !(RewardItemFields.cost.get(__instance) <= player.gold);
            if (__instance.isDone && RewardItemFields.cost.get(__instance) > 0) {
                player.loseGold(RewardItemFields.cost.get(__instance));
                __instance.text = __instance.text.replace(TEXT[0] + RewardItemFields.cost.get(__instance), "");
                RewardItemFields.cost.set(__instance, 0);

            }


        }

    }


    public static String[] TEXT;

    static {
        TEXT = CardCrawlGame.languagePack.getUIString(makeID("GreedCurse")).TEXT;
    }


}
