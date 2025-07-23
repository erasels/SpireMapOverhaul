package spireMapOverhaul.zones.humidity.encounters;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch2;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.helpers.MonsterHelper;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.MonsterGroup;
import spireMapOverhaul.zones.humidity.HumidityZone;
import spireMapOverhaul.zones.humidity.encounters.monsters.HumidityCenturion;
import spireMapOverhaul.zones.humidity.encounters.monsters.HumidityHealer;

public class CenturionJoust {
    @SpirePatch2(clz = MonsterHelper.class, method = "getEncounter")
    public static class CustomEncounterPatch {
        @SpirePrefixPatch
        public static SpireReturn<MonsterGroup> Foo(String key) {
            if (HumidityZone.isNotInZone())
                return SpireReturn.Continue();
            if (key.equals("Centurion and Healer"))
                return SpireReturn.Return(new MonsterGroup(new AbstractMonster[]{new HumidityCenturion(-200.0F, 15.0F), new HumidityHealer(120.0F, 0.0F)}));
            return SpireReturn.Continue();
        }
    }
}
