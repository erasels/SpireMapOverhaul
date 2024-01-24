package spireMapOverhaul.zones.humility.patches.city

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch2
import com.megacrit.cardcrawl.cards.AbstractCard
import com.megacrit.cardcrawl.core.CardCrawlGame
import com.megacrit.cardcrawl.localization.PowerStrings
import com.megacrit.cardcrawl.powers.HexPower
import javassist.expr.ExprEditor
import javassist.expr.FieldAccess
import spireMapOverhaul.SpireAnniversary6Mod
import spireMapOverhaul.zones.humility.HumilityZone

@SpirePatch(
    clz = HexPower::class,
    method = "onUseCard"
)
class ChosenStrongerHex {
    companion object {
        private val strings: PowerStrings by lazy { CardCrawlGame.languagePack.getPowerStrings(SpireAnniversary6Mod.makeID(HexPower.POWER_ID)) }

        @JvmStatic
        fun Instrument(): ExprEditor =
            object : ExprEditor() {
                override fun edit(f: FieldAccess) {
                    if (HumilityZone.hasHumilityMod()) return

                    if (f.isReader && f.className == AbstractCard::class.qualifiedName && f.fieldName == "type") {
                        f.replace(
                            "if (${HumilityZone::class.qualifiedName}.isInZone()) {" +
                                    "\$_ = ${AbstractCard.CardType::class.qualifiedName}.SKILL;" +
                                    "} else {" +
                                    "\$_ = \$proceed(\$\$);" +
                                    "}"
                        )
                    }
                }
            }
    }

    @SpirePatch2(
        clz = HexPower::class,
        method = SpirePatch.CONSTRUCTOR
    )
    class HexPowerName {
        companion object {
            @JvmStatic
            fun Postfix(__instance: HexPower) {
                if (HumilityZone.isNotInZone()) return

                __instance.description = strings.DESCRIPTIONS[0] + __instance.amount + strings.DESCRIPTIONS[1]
            }
        }
    }
}
