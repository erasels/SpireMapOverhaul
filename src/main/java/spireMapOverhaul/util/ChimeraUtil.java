package spireMapOverhaul.util;

import basemod.ReflectionHacks;
import com.evacipated.cardcrawl.modthespire.Loader;
import com.megacrit.cardcrawl.cards.AbstractCard;

public class ChimeraUtil {
    private static Class<?> chimera = null;

    private static Class<?> getChimera() {
        try {
            if (chimera == null && isLoaded()) {
                chimera = Class.forName("CardAugments.CardAugmentsMod");
            }
            return chimera;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed when trying to set up Chimera Cards compatibility", e);
        }
    }

    public static boolean isLoaded() {
        return Loader.isModLoaded("CardAugments");
    }

    public static void rollCardAugment(AbstractCard c) {
        if (isLoaded()) {
            ReflectionHacks.RMethod m = ReflectionHacks.privateMethod(getChimera(), "rollCardAugment", AbstractCard.class);
            m.invoke(null, c);
        }
    }

    public static void rollCardAugmentForShop(AbstractCard c) {
        if (isLoaded() && (boolean)ReflectionHacks.getPrivateStatic(getChimera(), "modifyShop")) {
            rollCardAugment(c);
        }
    }
}
