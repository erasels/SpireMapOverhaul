package spireMapOverhaul.zones.humidity.encounters.monsters.joustimplementation;

import com.badlogic.gdx.Gdx;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch2;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.city.Centurion;
import spireMapOverhaul.zones.humidity.HumidityZone;
import spireMapOverhaul.zones.humidity.encounters.monsters.HumidityCenturion;

public class LeftCenturionMovesIntoPosition {
    @SpirePatch2(clz = AbstractMonster.class, method = "update")
    public static class UpdatePatch {
        @SpirePostfixPatch
        public static void Foo(AbstractMonster __instance) {
            if (HumidityZone.isNotInZone()) return;
            if (!(__instance instanceof Centurion)) return;
            if (!HumidityCenturion.Fields.isSolo.get(__instance)) return;
            int direction = 1;
            __instance.drawX += direction * Gdx.graphics.getDeltaTime() * 400.0F * Settings.scale;
            float NEGATIVE_SIXTWENTYFIVE = (float) Settings.WIDTH * 0.75F + -625 * Settings.xScale;
            if (__instance.drawX > NEGATIVE_SIXTWENTYFIVE) {
                __instance.drawX = NEGATIVE_SIXTWENTYFIVE;
            }
        }
    }
}
