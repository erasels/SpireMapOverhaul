package spireMapOverhaul.zones.goldencurse;

import basemod.abstracts.CustomReward;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireField;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch2;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.rewards.RewardItem;
import spireMapOverhaul.patches.ZonePatches;

import static com.megacrit.cardcrawl.dungeons.AbstractDungeon.player;
import static spireMapOverhaul.SpireAnniversary6Mod.makeID;


public class RewardPatches {

    private static final float GOLD_TEXT_X = 1135.0F * Settings.scale;
    private static final float GOLD_IMG_X = GOLD_TEXT_X - 66.0f * Settings.scale;
    private static final float GOLD_IMG_SIZE = (float) ImageMaster.UI_GOLD.getWidth() * Settings.scale;

    @SpirePatch2(
            clz = RewardItem.class,
            method = SpirePatch.CLASS
    )
    public static class RewardItemFields {
        public static SpireField<Integer> cost = new SpireField<>(() -> 0);
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

    @SpirePatch2(
            clz = RewardItem.class,
            method = "render"
    )
    @SpirePatch2(
            clz = CustomReward.class,
            method = "render"
    )
    public static class RewardItemRender {
        public static void Postfix(RewardItem __instance, SpriteBatch sb) {
            if (!(ZonePatches.currentZone() instanceof GoldenCurseZone)) {
                return;
            }
            int goldAmt = RewardItemFields.cost.get(__instance);

            sb.setColor(Color.WHITE);
            if (goldAmt > 0) {
                sb.draw(ImageMaster.UI_GOLD, GOLD_IMG_X, __instance.y - 9.0F * Settings.scale, GOLD_IMG_SIZE, GOLD_IMG_SIZE);
                Color c = Color.WHITE.cpy();
                if (goldAmt >= AbstractDungeon.player.gold) {
                    c = Color.SALMON.cpy();
                }
                FontHelper.renderSmartText(sb, FontHelper.tipHeaderFont, Integer.toString(goldAmt), GOLD_TEXT_X, __instance.y + 30.0F * Settings.scale, 1000.0F * Settings.scale, 0.0F, c);
            }


        }

    }




    public static String[] TEXT;

    static {
        TEXT = CardCrawlGame.languagePack.getUIString(makeID("GreedCurse")).TEXT;
    }


}
