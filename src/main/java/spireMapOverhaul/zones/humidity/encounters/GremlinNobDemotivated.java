package spireMapOverhaul.zones.humidity.encounters;

import com.evacipated.cardcrawl.modthespire.lib.SpireInstrumentPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch2;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.megacrit.cardcrawl.actions.animations.TalkAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.events.city.Colosseum;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.exordium.GremlinNob;
import com.megacrit.cardcrawl.powers.AngerPower;
import com.megacrit.cardcrawl.powers.LoseStrengthPower;
import javassist.CannotCompileException;
import javassist.expr.ExprEditor;
import javassist.expr.NewExpr;
import spireMapOverhaul.SpireAnniversary6Mod;
import spireMapOverhaul.util.Wiz;
import spireMapOverhaul.zones.humidity.HumidityZone;

public class GremlinNobDemotivated {

    @SpirePatch2(clz = AngerPower.class, method = "onUseCard")
    public static class TemporaryStrength {
        @SpirePrefixPatch
        public static void Foo(AngerPower __instance, AbstractCard card, UseCardAction action) {
            if (HumidityZone.isNotInZone()) return;
            if (Wiz.curRoom() != null && Wiz.curRoom().event != null && Wiz.curRoom().event instanceof Colosseum)
                return;
            if (card.type == AbstractCard.CardType.SKILL) {
                Wiz.att(new ApplyPowerAction(__instance.owner, __instance.owner, new LoseStrengthPower(__instance.owner, __instance.amount), __instance.amount));
            }
        }
    }

    @SpirePatch2(clz = AngerPower.class, method = "updateDescription")
    public static class DescriptionPatch {
        @SpirePostfixPatch
        public static void Foo(AngerPower __instance) {
            if (HumidityZone.isNotInZone()) return;
            if (SlaversBecomeSleevers.colosseumInProgress()) return;
            __instance.name = POWERNAME;
            __instance.description = DESCRIPTIONS[0] + __instance.amount + DESCRIPTIONS[1];
        }
    }

    @SpirePatch2(clz = GremlinNob.class, method = "takeTurn")
    public static class TalkReplacement {
        @SpireInstrumentPatch
        public static ExprEditor patch() {
            return new ExprEditor() {
                @Override
                public void edit(NewExpr n) throws CannotCompileException {
                    if (n.getClassName().equals(TalkAction.class.getName())) {
                        //We have to define gremlinNobTerniary in a different class.
                        // Defining it in GremlinNobDemotivated will cause a NPE due to "CardCrawlGame.languagePack.getPowerStrings(ID);" breaking in the static block below.
                        n.replace("{$2 = " + HumidityZone.gremlinNobTerniary() + " $2 : " + GremlinNobDemotivated.class.getName() + ".getCustomMessage(); $_ = $proceed($$);}");
                    }
                }
            };
        }
    }


    public static String getCustomMessage() {
        return ROAR;
    }


    private static final MonsterStrings monsterStrings;
    public static String ROAR;
    private static final PowerStrings powerStrings;
    public static String POWERNAME;
    public static String[] DESCRIPTIONS;

    static {
        {
            String ID = SpireAnniversary6Mod.makeID("CustomAngryPower");
            powerStrings = CardCrawlGame.languagePack.getPowerStrings(ID);
            POWERNAME = powerStrings.NAME;
            DESCRIPTIONS = powerStrings.DESCRIPTIONS;
        }
        {
            String ID = SpireAnniversary6Mod.makeID("CustomGremlinNob");
            monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID);
            ROAR = monsterStrings.DIALOG[0];
        }
    }

}
