package spireMapOverhaul.zones.humility.patches.beyond

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import com.megacrit.cardcrawl.monsters.beyond.Transient
import com.megacrit.cardcrawl.powers.FadingPower
import com.megacrit.cardcrawl.powers.RegenerateMonsterPower
import javassist.expr.ExprEditor
import javassist.expr.NewExpr
import spireMapOverhaul.zones.humility.HumilityZone

class TransientBuffs {
    @SpirePatch(
        clz = Transient::class,
        method = "usePreBattleAction"
    )
    class AddBuffs {
        companion object {
            @JvmStatic
            fun Instrument(): ExprEditor =
                object : ExprEditor() {
                    override fun edit(e: NewExpr) {
                        if (HumilityZone.hasHumilityMod()) return

                        if (e.className == FadingPower::class.qualifiedName) {
                            e.replace(
                                "if (${HumilityZone::class.qualifiedName}.isInZone()) {" +
                                        "\$_ = \$proceed(\$1, \$2 + 1);" +
                                        "} else {" +
                                        "\$_ = \$proceed(\$\$);" +
                                        "}"
                            )
                        }
                    }
                }

            @JvmStatic
            fun Postfix(__instance: Transient) {
                if (HumilityZone.isNotInZone()) return

                AbstractDungeon.actionManager.addToBottom(
                    ApplyPowerAction(
                        __instance,
                        __instance,
                        RegenerateMonsterPower(__instance, 999),
                        999
                    )
                )
            }
        }
    }
}
