package spireMapOverhaul.zones.humidity.encounters;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch2;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.helpers.MonsterHelper;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.MonsterGroup;
import com.megacrit.cardcrawl.monsters.city.Byrd;
import com.megacrit.cardcrawl.monsters.exordium.JawWorm;
import spireMapOverhaul.zones.humidity.HumidityZone;

public class JawWormEarlyByrd {
    @SpirePatch2(clz = MonsterHelper.class, method = "getEncounter")
    public static class EncounterPatch {
        @SpirePrefixPatch
        public static SpireReturn<MonsterGroup> Foo(String key) {
            if (HumidityZone.isNotInZone()) return SpireReturn.Continue();
            if (key.equals("Jaw Worm")) {
                return SpireReturn.Return(
                        new MonsterGroup(new AbstractMonster[]{new JawWorm(-460.0F, 25.0F), new Byrd(0.0F, 70.0F)})
                );
            }
            return SpireReturn.Continue();
        }
    }
}
