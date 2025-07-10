package spireMapOverhaul.zones.humidity.encounters;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.evacipated.cardcrawl.modthespire.patcher.PatchingException;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.city.ShelledParasite;
import javassist.CannotCompileException;
import javassist.CtBehavior;
import spireMapOverhaul.zones.humidity.HumidityZone;
import spireMapOverhaul.zones.humidity.relics.AvocadoReward;

public class ShelledParasiteAvocado {
    @SpirePatch(clz = ShelledParasite.class, method = SpirePatch.CLASS)
    public static class Fields {
        public static SpireField<Integer> damageTaken = new SpireField<>(() -> 0);
    }

    @SpirePatch2(clz = AbstractPlayer.class, method = "damage")
    public static class RecordDamageTakenPatch {
        @SpireInsertPatch(locator = Locator.class, localvars = {"damageAmount"})
        public static void Foo(AbstractPlayer __instance, DamageInfo info, int damageAmount) {
            if (info.owner instanceof ShelledParasite) {
                AbstractCreature m = info.owner;
                Fields.damageTaken.set(m, Fields.damageTaken.get(m) + damageAmount);
            }
        }

        private static class Locator extends SpireInsertLocator {
            public int[] Locate(CtBehavior ctMethodToPatch) throws CannotCompileException, PatchingException {
                Matcher finalMatcher = new Matcher.FieldAccessMatcher(GameActionManager.class, "damageReceivedThisCombat");
                return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
            }
        }
    }

    @SpirePatch2(clz = AbstractMonster.class, method = "die", paramtypez = {boolean.class})
    public static class AddAvocadoRelicToRewardsPatch {
        @SpirePostfixPatch
        public static void Foo(AbstractMonster __instance) {
            if (HumidityZone.isNotInZone()) return;
            if (!(__instance instanceof ShelledParasite)) return;
            AvocadoReward reward = new AvocadoReward(Fields.damageTaken.get(__instance));
            AbstractDungeon.getCurrRoom().rewards.add(reward);
        }
    }
}
