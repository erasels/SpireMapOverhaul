package spireMapOverhaul.zones.gremlinTown.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

@SpirePatch(
        clz = AbstractCreature.class,
        method = "brokeBlock"
)
public class HeavyArmorPatch {
    @SpirePrefixPatch
    public static void heavyArmorUpdate(AbstractCreature __instance) {
        AbstractDungeon.onModifyPower();
    }
}
