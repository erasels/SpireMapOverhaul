package spireMapOverhaul.zones.humidity.events;

import basemod.ReflectionHacks;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.dungeons.Exordium;
import com.megacrit.cardcrawl.events.AbstractEvent;
import com.megacrit.cardcrawl.events.exordium.DeadAdventurer;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.exordium.SpikeSlime_S;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.screens.charSelect.CharacterSelectScreen;
import spireMapOverhaul.zones.gremlinTown.events.GremlinWheel;
import spireMapOverhaul.zones.humidity.HumidityZone;
import spireMapOverhaul.zones.humidity.powers.SplatPower;
import spireMapOverhaul.zones.humidity.relics.Pants;

import java.util.Objects;

import static spireMapOverhaul.SpireAnniversary6Mod.makeID;

public class DeadAdventurerPants {
    public static final String ID = makeID(DeadAdventurerPants.class.getSimpleName());
    private static final EventStrings eventStrings = CardCrawlGame.languagePack.getEventString(ID);

    @SpirePatch(clz = DeadAdventurer.class, method = SpirePatch.CLASS)
    public static class PantsField {
        public static final SpireField<Boolean> receivedPants = new SpireField<>(() -> false);
    }

    @SpirePatch2(clz=DeadAdventurer.class,method=SpirePatch.CONSTRUCTOR)
    public static class DescriptionPatch{
        @SpirePostfixPatch
        public static void Foo(DeadAdventurer __instance){
            final String[] DESCRIPTIONS = eventStrings.DESCRIPTIONS;
            if(HumidityZone.isNotInZone())return;
            String body = DESCRIPTIONS[0];
            switch ((int)ReflectionHacks.getPrivate(__instance, DeadAdventurer.class,"enemy")) {
                case 0:
                    body = body + DeadAdventurer.DESCRIPTIONS[3];
                    break;
                case 1:
                    body = body + DeadAdventurer.DESCRIPTIONS[4];
                    break;
                default:
                    body = body + DeadAdventurer.DESCRIPTIONS[5];
            }
            body = body + DeadAdventurer.DESCRIPTIONS[6];
            ReflectionHacks.setPrivate(__instance,AbstractEvent.class,"body",body);
        }
    }

    @SpirePatch2(clz=DeadAdventurer.class,method="update")
    public static class PantsPatch {
        @SpirePrefixPatch
        public static void Foo(DeadAdventurer __instance) {
            if(HumidityZone.isNotInZone())return;
            if(!PantsField.receivedPants.get(__instance)) {
                PantsField.receivedPants.set(__instance,true);
                AbstractRelic r = new Pants();
                AbstractDungeon.getCurrRoom().spawnRelicAndObtain((float) Settings.WIDTH / 2.0F, (float) Settings.HEIGHT / 2.0F, r);
            }
        }
    }

}
