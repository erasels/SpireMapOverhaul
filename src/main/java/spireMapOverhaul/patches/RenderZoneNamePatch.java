package spireMapOverhaul.patches;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch2;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.screens.DungeonMapScreen;
import spireMapOverhaul.BetterMapGenerator;
import spireMapOverhaul.abstracts.AbstractZone;

@SpirePatch2(
        clz = DungeonMapScreen.class,
        method = "renderControllerUi"
)
public class RenderZoneNamePatch
{
    @SpirePrefixPatch
    public static void render(SpriteBatch sb)
    {
        Color old = sb.getColor();

        for (AbstractZone zone : BetterMapGenerator.getActiveZones(AbstractDungeon.map)) {
            zone.renderNameOnMap(sb, old.a);
        }

        sb.setColor(old);
    }
}
