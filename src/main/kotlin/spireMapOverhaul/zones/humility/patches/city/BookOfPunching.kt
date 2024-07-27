package spireMapOverhaul.zones.humility.patches.city

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch
import com.megacrit.cardcrawl.core.CardCrawlGame
import com.megacrit.cardcrawl.localization.MonsterStrings
import com.megacrit.cardcrawl.monsters.city.BookOfStabbing
import com.megacrit.cardcrawl.powers.PainfulStabsPower
import javassist.expr.ExprEditor
import javassist.expr.NewExpr
import spireMapOverhaul.SpireAnniversary6Mod
import spireMapOverhaul.zones.humility.HumilityZone
import spireMapOverhaul.zones.humility.powers.PainfulPunchesPower

class BookOfPunching {
    companion object {
        private val strings: MonsterStrings by lazy { CardCrawlGame.languagePack.getMonsterStrings(SpireAnniversary6Mod.makeID(BookOfStabbing.ID)) }
    }

    @SpirePatch(
        clz = BookOfStabbing::class,
        method = SpirePatch.CONSTRUCTOR
    )
    class Name {
        companion object {
            @JvmStatic
            fun Postfix(__instance: BookOfStabbing) {
                if (HumilityZone.isNotInZone()) return

                __instance.name = strings.NAME
            }
        }
    }

    @SpirePatch(
        clz = BookOfStabbing::class,
        method = "usePreBattleAction"
    )
    class PunchPower {
        companion object {
            @JvmStatic
            fun Instrument(): ExprEditor =
                object : ExprEditor() {
                    override fun edit(e: NewExpr) {
                        if (HumilityZone.hasHumilityMod()) return

                        if (e.className == PainfulStabsPower::class.qualifiedName) {
                            e.replace(
                                "if (${HumilityZone::class.qualifiedName}.isInZone()) {" +
                                        "\$_ = new ${PainfulPunchesPower::class.qualifiedName}(\$\$);" +
                                        "} else {" +
                                        "\$_ = \$proceed(\$\$);" +
                                        "}"
                            )
                        }
                    }
                }
        }
    }
}
