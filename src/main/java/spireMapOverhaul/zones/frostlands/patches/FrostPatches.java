package spireMapOverhaul.zones.frostlands.patches;

import basemod.helpers.CardModifierManager;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.evacipated.cardcrawl.modthespire.patcher.PatchingException;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import javassist.CannotCompileException;
import javassist.CtBehavior;
import spireMapOverhaul.zones.frostlands.cardmods.FrostbiteModifier;
import spireMapOverhaul.zones.manasurge.modifiers.common.negative.FeebleModifier;

public class FrostPatches {
    @SpirePatch(clz = GameActionManager.class, method = "getNextAction")
    public static class CantAffordDontUse {
        public static boolean SERIOUSLY_NO = false;

        @SpireInsertPatch(locator = Locator.class)
        public static void Insert(GameActionManager __instance) {
            AbstractCard c = __instance.cardQueue.get(0).card;
            if (CardModifierManager.hasModifier(c, FrostbiteModifier.ID) && c.dontTriggerOnUseCard) {
                SERIOUSLY_NO = true;
                AbstractDungeon.actionManager.addToBottom(new UseCardAction(c));
            }
        }

        private static class Locator extends SpireInsertLocator {
            public int[] Locate(CtBehavior ctMethodToPatch) throws CannotCompileException, PatchingException {
                Matcher finalMatcher = new Matcher.MethodCallMatcher(AbstractPlayer.class, "useCard");
                return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
            }
        }
    }

    @SpirePatch(clz = AbstractPlayer.class, method = "useCard")
    public static class SeriouslyDontUseIt {
        @SpirePrefixPatch
        public static SpireReturn Prefix(AbstractPlayer __instance, AbstractCard c, AbstractMonster monster, int energyOnUse) {
            if (CantAffordDontUse.SERIOUSLY_NO) {
                CantAffordDontUse.SERIOUSLY_NO = false;
                return SpireReturn.Return();
            }
            return SpireReturn.Continue();
        }
    }
}
