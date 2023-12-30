package spireMapOverhaul.zones.invasion.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpireInstrumentPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.shop.ShopScreen;
import javassist.CannotCompileException;
import javassist.expr.ExprEditor;
import javassist.expr.MethodCall;
import spireMapOverhaul.zones.invasion.interfaces.CustomPriceCard;

@SpirePatch(clz = ShopScreen.class, method = "initCards")
public class CustomCardPricePatch {
    public static class GetPriceForCorruptedCardsExprEditor extends ExprEditor {
        int hits = 0;
        @Override
        public void edit(MethodCall methodCall) throws CannotCompileException {
            if (methodCall.getClassName().equals(AbstractCard.class.getName()) && methodCall.getMethodName().equals("getPrice")) {
                methodCall.replace(String.format("{ $_ = this.%2$s.get(i) instanceof %1$s ? ((%1$s)this.%2$s.get(i)).getPrice() : $proceed($$); }", CustomPriceCard.class.getName(), hits == 0 ? "coloredCards" : "colorlessCards"));
                hits++;
            }
        }
    }

    @SpireInstrumentPatch
    public static ExprEditor getPriceForCorruptedCards() {
        return new GetPriceForCorruptedCardsExprEditor();
    }
}