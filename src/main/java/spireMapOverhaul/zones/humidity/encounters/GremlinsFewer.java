package spireMapOverhaul.zones.humidity.encounters;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.MonsterHelper;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.MonsterGroup;
import spireMapOverhaul.zones.humidity.HumidityZone;

import java.util.ArrayList;

public class GremlinsFewer {

    @SpirePatch(
            clz = MonsterHelper.class,
            method = "spawnGremlins"
    )
    public static class OnlyThreeGremlins {
        @SpirePrefixPatch
        public static SpireReturn<MonsterGroup> Foo() {
            if(HumidityZone.isNotInZone()) return SpireReturn.Continue();
            return SpireReturn.Return(spawnFewerGremlins());
        }
    }

    private static MonsterGroup spawnFewerGremlins() {
        ArrayList<String> gremlinPool = new ArrayList();
        gremlinPool.add("GremlinWarrior");
        gremlinPool.add("GremlinWarrior");
        gremlinPool.add("GremlinThief");
        gremlinPool.add("GremlinThief");
        gremlinPool.add("GremlinFat");
        gremlinPool.add("GremlinFat");
        gremlinPool.add("GremlinTsundere");
        gremlinPool.add("GremlinWizard");
        AbstractMonster[] retVal = new AbstractMonster[3];
        int index = AbstractDungeon.miscRng.random(gremlinPool.size() - 1);
        String key = (String)gremlinPool.get(index);
        gremlinPool.remove(index);
        retVal[0] = MonsterHelper.getGremlin(key, -240.0F, 6.5F);
        index = AbstractDungeon.miscRng.random(gremlinPool.size() - 1);
        key = (String)gremlinPool.get(index);
        gremlinPool.remove(index);
        retVal[1] = MonsterHelper.getGremlin(key, -67.5F, -23.5F);
        index = AbstractDungeon.miscRng.random(gremlinPool.size() - 1);
        key = (String)gremlinPool.get(index);
        gremlinPool.remove(index);
        retVal[2] = MonsterHelper.getGremlin(key, 115.0F, -2.5F);
        return new MonsterGroup(retVal);
    }
}
