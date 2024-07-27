package spireMapOverhaul.zones.humility.patches.events

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch
import com.megacrit.cardcrawl.actions.animations.TalkAction
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction
import com.megacrit.cardcrawl.core.CardCrawlGame
import com.megacrit.cardcrawl.core.Settings
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import com.megacrit.cardcrawl.monsters.city.BanditBear
import com.megacrit.cardcrawl.monsters.city.BanditLeader
import com.megacrit.cardcrawl.powers.AngerPower
import com.megacrit.cardcrawl.powers.StrengthPower
import javassist.CtBehavior
import spireMapOverhaul.SpireAnniversary6Mod
import spireMapOverhaul.zones.humility.HumilityZone
import spireMapOverhaul.zones.humility.patches.utils.addToMethod

class RomeoDeadBuffs {
    @SpirePatch(
        clz = BanditLeader::class,
        method = SpirePatch.CONSTRUCTOR
    )
    class RomeoDies {
        companion object {
            @JvmStatic
            fun Raw (ctBehavior: CtBehavior) {
                ctBehavior.addToMethod("die", Companion::die)
            }

            @JvmStatic
            fun die(__instance: BanditLeader) {
                if (HumilityZone.isNotInZone()) return

                AbstractDungeon.getMonsters().monsters
                    .filterIsInstance<BanditBear>()
                    .forEach { it.deathReact() }
            }
        }
    }

    @SpirePatch(
        clz = BanditBear::class,
        method = SpirePatch.CONSTRUCTOR
    )
    class DeathReact {
        companion object {
            private val DIALOG by lazy { CardCrawlGame.languagePack.getMonsterStrings(SpireAnniversary6Mod.makeID(BanditBear.ID))?.DIALOG ?: arrayOf("[MISSING TEXT]") }

            @JvmStatic
            fun Postfix(__instance: BanditBear, x: Float, y: Float) {
                if (HumilityZone.isNotInZone()) return

                __instance.dialogX = 0f * Settings.scale
                __instance.dialogY = 50f * Settings.scale
            }

            @JvmStatic
            fun Raw(ctBehavior: CtBehavior) {
                ctBehavior.addToMethod("deathReact", Companion::deathReact)
            }

            @JvmStatic
            fun deathReact(__instance: BanditBear) {
                if (HumilityZone.isNotInZone()) return

                if (!__instance.isDeadOrEscaped) {
                    AbstractDungeon.actionManager.addToBottom(TalkAction(__instance, DIALOG[0]))
                    AbstractDungeon.actionManager.addToBottom(ApplyPowerAction(__instance, __instance, AngerPower(__instance, 2), 2))
                    AbstractDungeon.actionManager.addToBottom(ApplyPowerAction(__instance, __instance, StrengthPower(__instance, 2), 2))
                }
            }
        }
    }
}
