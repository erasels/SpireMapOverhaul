package spireMapOverhaul.zones.humidity.encounters;

import com.badlogic.gdx.Gdx;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch2;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.MonsterHelper;
import com.megacrit.cardcrawl.monsters.MonsterGroup;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rewards.RewardItem;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import spireMapOverhaul.util.Wiz;
import spireMapOverhaul.zones.humidity.HumidityZone;
import spireMapOverhaul.zones.humidity.encounters.monsters.HumidityBookOfStabbing;

public class BookOfStabbingCursedTome {

    @SpirePatch2(clz = MonsterHelper.class, method = "getEncounter")
    public static class CustomEncounterPatch {
        @SpirePrefixPatch
        public static SpireReturn<MonsterGroup> Foo(String key) {
            if (HumidityZone.isNotInZone())
                return SpireReturn.Continue();
            if (key.equals("Book of Stabbing"))
                return SpireReturn.Return(new MonsterGroup(new HumidityBookOfStabbing()));
            return SpireReturn.Continue();
        }
    }


    @SpirePatch2(clz = AbstractRoom.class, method = "addRelicToRewards", paramtypez = {AbstractRelic.RelicTier.class})
    public static class LootPatch {
        @SpirePrefixPatch
        public static SpireReturn<Void> Foo(AbstractRoom __instance) {
            AbstractRelic relic = null;
            relic = HumidityBookOfStabbing.BookOfStabbingCursedTomeFields.relic.get(__instance);
            if (relic != null) {
                __instance.rewards.add(new RewardItem(relic));
                return SpireReturn.Return();
            } else {
                return SpireReturn.Continue();
            }
        }
    }


    @SpirePatch2(clz = AbstractPlayer.class, method = "updateEscapeAnimation")
    public static class CollectTomeAnimationPatch {
        @SpirePrefixPatch
        public static SpireReturn<Void> Foo(AbstractPlayer __instance) {
            if (Wiz.curRoom() != null) {
                if (HumidityBookOfStabbing.BookOfStabbingCursedTomeFields.escapeRight.get(Wiz.curRoom())) {
                    __instance.drawX += Gdx.graphics.getDeltaTime() * 500.0F * Settings.scale;
                    return SpireReturn.Return();
                }
            }
            return SpireReturn.Continue();
        }
    }

}
