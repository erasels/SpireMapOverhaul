package spireMapOverhaul.zones.voidseed.campfire;

import basemod.helpers.CardModifierManager;
import com.evacipated.cardcrawl.modthespire.lib.SpireInstrumentPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch2;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.screens.select.GridCardSelectScreen;
import javassist.CannotCompileException;
import javassist.expr.ExprEditor;
import javassist.expr.MethodCall;
import spireMapOverhaul.zones.voidseed.cardmods.CorruptedModifier;

public class CorruptScreenPatch {

    public static boolean active = false;

    @SpirePatch2(clz = GridCardSelectScreen.class, method = "update")
    public static class CorruptScreenPatchChangeCard {

        @SpireInstrumentPatch
        public static ExprEditor patch() {
            return new ExprEditor() {
                @Override
                public void edit(MethodCall m) throws CannotCompileException {
                    if (m.getClassName().equals(AbstractCard.class.getName()) && m.getMethodName().equals("makeStatEquivalentCopy")) {
                        m.replace(
                                "{" +
                                        "$_ = " + CorruptScreenPatch.class.getName() + ".getCard($0);" +
                                        "}"

                        );
                    }
                }
            };
        }
    }

    @SpirePatch2(clz = GridCardSelectScreen.class, method = "update")
    public static class CorruptScreenPatchKILLUpgrade {

        @SpireInstrumentPatch
        public static ExprEditor patch() {
            return new ExprEditor() {
                @Override
                public void edit(MethodCall m) throws CannotCompileException {
                    if (m.getClassName().equals(AbstractCard.class.getName())) {
                        String method = m.getMethodName();

                        if (method.equals("upgrade")) {
                            m.replace(
                                    "{" +
                                            CorruptScreenPatch.class.getName() + ".checkUpgrade($0);" +
                                            "}"

                            );
                        }
                        if (method.equals("displayUpgrades")) {
                            m.replace(
                                    "{" +
                                            CorruptScreenPatch.class.getName() + ".checkShowUpgrade($0);" +
                                            "}"

                            );
                        }
                    }
                }
            };
        }
    }

    @SpirePatch2(clz = AbstractDungeon.class, method = "closeCurrentScreen")
    public static class CorruptScreenPatchOnClose {

        @SpirePrefixPatch
        public static void patch() {
            if (AbstractDungeon.screen == AbstractDungeon.CurrentScreen.GRID) {
                active = false;
            }
        }
    }

    public static void checkUpgrade(AbstractCard c) {
        if (active) {
            return;
        }
        c.upgrade();
    }

    public static void checkShowUpgrade(AbstractCard c) {
        if (active) {
            return;
        }
        c.displayUpgrades();
    }


    public static AbstractCard getCard(AbstractCard c) {
        AbstractCard copy = c.makeStatEquivalentCopy();
        if (active) {

            CardModifierManager.addModifier(copy, new CorruptedModifier());
        }
        return copy;
    }

}
