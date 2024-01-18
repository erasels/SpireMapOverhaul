package spireMapOverhaul.zones.gremlinTown.patches;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.characters.AnimatedNpc;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.shop.Merchant;
import javassist.CtBehavior;
import spireMapOverhaul.util.Wiz;
import spireMapOverhaul.zones.gremlinTown.GremlinTown;

import static com.megacrit.cardcrawl.shop.Merchant.DRAW_X;

@SpirePatch(
        clz = Merchant.class,
        method = SpirePatch.CONSTRUCTOR,
        paramtypez = {float.class, float.class, int.class}
)
public class MerchantPatch {
    @SpireInsertPatch(
            locator = Locator.class
    )
    public static void Insert(Merchant __instance) {
        if (Wiz.getCurZone() instanceof GremlinTown)
            __instance.anim = new AnimatedNpc(DRAW_X + 256.0F * Settings.scale, AbstractDungeon.floorY + 30.0F * Settings.scale,
                    GremlinTown.MERCHANT_ATLAS, GremlinTown.MERCHANT_SKELETON, "idle");
    }

    public static class Locator extends SpireInsertLocator {
        private Locator() {}

        @Override
        public int[] Locate(CtBehavior behavior) throws Exception {
            Matcher matcher = new Matcher.MethodCallMatcher(AbstractDungeon.class, "getCardFromPool");
            return LineFinder.findInOrder(behavior, matcher);
        }
    }
}
