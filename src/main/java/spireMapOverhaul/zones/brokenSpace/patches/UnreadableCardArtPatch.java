package spireMapOverhaul.zones.brokenSpace.patches;

import basemod.helpers.CardModifierManager;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch2;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.cards.AbstractCard;

import static spireMapOverhaul.SpireAnniversary6Mod.makeID;

public class UnreadableCardArtPatch {
    @SpirePatch2(clz = AbstractCard.class, method = "renderPortrait")
    @SpirePatch2(clz = AbstractCard.class, method = "renderJokePortrait")
    @SpirePatch2(clz = AbstractCard.class, method = "renderPortraitFrame")
    @SpirePatch2(clz = AbstractCard.class, method = "renderType")
    @SpirePatch2(clz = AbstractCard.class, method = "renderDynamicFrame")
    public static class RenderPortraitPatch {
        public static SpireReturn<Void> Prefix(AbstractCard __instance) {
            if (CardModifierManager.hasModifier(__instance, makeID("UnreadableCardMod"))) {
                return SpireReturn.Return();
            }
            return SpireReturn.Continue();
        }
    }
}
