package spireMapOverhaul.zones.gremlinTown.patches;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import javassist.CtBehavior;
import spireMapOverhaul.zones.gremlinTown.powers.BellowPower;

import static spireMapOverhaul.util.Wiz.adp;

@SpirePatch(
        clz = GameActionManager.class,
        method = "getNextAction"
)
public class BellowPatch {
    @SpireInsertPatch(
            locator = Locator.class,
            localvars = {"m"}
    )
    public static void Insert(GameActionManager __instance, AbstractMonster m) {
        if (m.getIntentBaseDmg() < 0) {
            AbstractPower pow = adp().getPower(BellowPower.POWER_ID);
            if (pow != null)
                pow.onSpecificTrigger();
        }
    }

    public static class Locator extends SpireInsertLocator {
        private Locator() {}

        @Override
        public int[] Locate(CtBehavior behavior) throws Exception {
            Matcher matcher = new Matcher.MethodCallMatcher(AbstractMonster.class, "takeTurn");
            return LineFinder.findInOrder(behavior, matcher);
        }
    }
}
