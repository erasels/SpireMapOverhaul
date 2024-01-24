package spireMapOverhaul.zones.humility.patches

import com.esotericsoftware.spine.Skeleton
import com.evacipated.cardcrawl.modthespire.lib.*
import com.megacrit.cardcrawl.core.AbstractCreature
import com.megacrit.cardcrawl.monsters.AbstractMonster
import com.megacrit.cardcrawl.monsters.beyond.Donu
import com.megacrit.cardcrawl.monsters.city.BookOfStabbing
import com.megacrit.cardcrawl.monsters.city.Champ
import com.megacrit.cardcrawl.monsters.exordium.*
import javassist.CtBehavior
import spireMapOverhaul.zones.humility.HumilityZone
import kotlin.reflect.KClass

class AnimationChanges {
    companion object {
        private val scaleData = mapOf<KClass<*>, List<Pair<String, Float>>>(
            Cultist::class to listOf("weaponleft" to 1.6f, "weaponright" to 1.6f),
            GremlinFat::class to listOf("root" to 1.7f, "weapon" to 1f / 1.7f),
            GremlinTsundere::class to listOf("shield" to 1.4f),
            GremlinWizard::class to listOf("head" to 1.3f),
            GremlinThief::class to listOf("root" to 0.7f, "weapon" to 1f / 0.7f),
            GremlinNob::class to listOf("weapon" to 1.4f),
            Champ::class to listOf("Arm_L_3" to 2f),
            BookOfStabbing::class to listOf("spine9" to 1.1f,"spine10" to 1.1f, "spine11" to 1.1f),
        )

        private val rotationData = mapOf<KClass<*>, List<Pair<String, Float>>>(
            Donu::class to listOf("ArmR_1" to -180f, "ArmL_1" to -180f),
        )

        private val disableData = mapOf<KClass<*>, List<String>>(
            Champ::class to listOf("shield", "handle"),
            BookOfStabbing::class to listOf("hilt", "emp", "blade"),
        )
    }

    @SpirePatch2(
        clz = AbstractMonster::class,
        method = "render"
    )
    class Transform {
        companion object {
            @JvmStatic
            @SpireInsertPatch(
                locator = Locator::class
            )
            fun Insert(__instance: AbstractMonster, ___skeleton: Skeleton) {
                if (HumilityZone.isNotInZone()) return

                scaleData[__instance.javaClass.kotlin]?.forEach {
                    ___skeleton.findBone(it.first)?.setScale(it.second)
                }
                rotationData[__instance.javaClass.kotlin]?.forEach {
                    ___skeleton.findBone(it.first)?.let { bone ->
                        bone.rotation += it.second
                    }
                }
            }
        }

        private class Locator : SpireInsertLocator() {
            override fun Locate(ctBehavior: CtBehavior?): IntArray {
                val finalMatcher = Matcher.MethodCallMatcher(Skeleton::class.java, "updateWorldTransform")
                return LineFinder.findInOrder(ctBehavior, finalMatcher)
            }
        }
    }

    @SpirePatch2(
        clz = AbstractCreature::class,
        method = "loadAnimation"
    )
    class Disable {
        companion object {
            @JvmStatic
            fun Postfix(__instance: AbstractCreature, ___skeleton: Skeleton) {
                if (HumilityZone.isNotInZone()) return

                disableData[__instance.javaClass.kotlin]?.forEach {
                    ___skeleton.findSlot(it)?.attachment = null
                }
            }
        }
    }
}
