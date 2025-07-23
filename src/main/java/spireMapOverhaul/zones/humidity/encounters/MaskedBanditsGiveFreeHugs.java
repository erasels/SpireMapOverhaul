package spireMapOverhaul.zones.humidity.encounters;

import basemod.ReflectionHacks;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.actions.animations.TalkAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.city.BanditBear;
import com.megacrit.cardcrawl.monsters.city.BanditLeader;
import com.megacrit.cardcrawl.monsters.city.BanditPointy;
import com.megacrit.cardcrawl.powers.DexterityPower;
import spireMapOverhaul.util.Wiz;
import spireMapOverhaul.zones.humidity.HumidityZone;

import static spireMapOverhaul.SpireAnniversary6Mod.makeID;

public class MaskedBanditsGiveFreeHugs {
    public static final String ID = makeID("MaskedBanditsGiveFreeHugs");
    public static final String[] DIALOG = CardCrawlGame.languagePack.getMonsterStrings(ID).DIALOG;

    public static String[] ORIGINAL_DIALOG = ((String[]) (ReflectionHacks.getPrivateStatic(BanditLeader.class, "DIALOG"))).clone();

    @SpirePatch2(clz = BanditLeader.class, method = "takeTurn")
    public static class RomeoSpeechBubbleChange {
        @SpirePrefixPatch
        public static void Foo(AbstractMonster __instance) {
            if (HumidityZone.isNotInZone()) return;
            String[] NEW_DIALOG = ReflectionHacks.getPrivateStatic(BanditLeader.class, "DIALOG");
            ORIGINAL_DIALOG = NEW_DIALOG.clone();
            NEW_DIALOG[0] = DIALOG[0];
            NEW_DIALOG[1] = DIALOG[1];
        }
    }

    @SpirePatch2(clz = BanditLeader.class, method = "takeTurn")
    public static class PointyNewSpeechBubble {
        @SpirePostfixPatch
        public static void Foo(AbstractMonster __instance) {
            if (HumidityZone.isNotInZone()) return;
            ReflectionHacks.setPrivateStatic(BanditLeader.class, "DIALOG", ORIGINAL_DIALOG.clone());
        }
    }

    @SpirePatch2(clz = BanditBear.class, method = SpirePatch.CONSTRUCTOR)
    public static class ApplyABSToDexterityDebuff {
        @SpirePostfixPatch
        public static void Foo(BanditBear __instance) {
            if (HumidityZone.isNotInZone()) return;
            ReflectionHacks.setPrivate(__instance, BanditBear.class, "con_reduction",
                    Math.abs(ReflectionHacks.getPrivate(__instance, BanditBear.class, "con_reduction")));
        }
    }

    @SpirePatch2(clz = BanditBear.class, method = "getMove")
    public static class BearFirstTurnIntentChangesToBuff {
        @SpirePrefixPatch
        public static SpireReturn<Void> Foo(BanditBear __instance) {
            if (HumidityZone.isNotInZone()) return SpireReturn.Continue();
            __instance.setMove((byte) 2, AbstractMonster.Intent.BUFF);
            return SpireReturn.Return();
        }
    }

    @SpirePatch2(clz = BanditBear.class, method = "takeTurn")
    public static class BanditsGetDexterityBuffToo {
        @SpirePostfixPatch
        public static void Foo(BanditBear __instance) {
            if (HumidityZone.isNotInZone()) return;
            if (__instance.nextMove != 2) return;
            for (AbstractMonster m : Wiz.getEnemies()) {
                int con_reduction = ReflectionHacks.getPrivate(__instance, BanditBear.class, "con_reduction");
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(m, __instance, new DexterityPower(m, con_reduction), con_reduction));
            }
            if (!Wiz.getEnemies().isEmpty()) {
                if (Wiz.getEnemies().get(0) instanceof BanditPointy) {
                    AbstractDungeon.actionManager.addToBottom(new TalkAction(Wiz.getEnemies().get(0), DIALOG[2]));
                }
            }
        }

    }


}
