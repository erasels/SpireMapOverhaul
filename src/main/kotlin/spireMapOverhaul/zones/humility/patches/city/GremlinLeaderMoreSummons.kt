package spireMapOverhaul.zones.humility.patches.city

import basemod.ReflectionHacks
import com.evacipated.cardcrawl.modthespire.lib.*
import com.megacrit.cardcrawl.actions.unique.SummonGremlinAction
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import com.megacrit.cardcrawl.helpers.MonsterHelper
import com.megacrit.cardcrawl.monsters.AbstractMonster
import com.megacrit.cardcrawl.monsters.MonsterGroup
import com.megacrit.cardcrawl.monsters.city.GremlinLeader
import javassist.CtBehavior
import spireMapOverhaul.zones.humility.HumilityZone

class GremlinLeaderMoreSummons {
    @SpirePatch(
        clz = GremlinLeader::class,
        method = SpirePatch.CONSTRUCTOR
    )
    class MoreSummonSlots {
        companion object {
            @JvmStatic
            fun Postfix(__instance: GremlinLeader) {
                if (HumilityZone.isNotInZone()) return

                __instance.gremlins = __instance.gremlins.copyOf(__instance.gremlins.size + 1)
            }
        }
    }

    @SpirePatch(
        clz = MonsterHelper::class,
        method = "getEncounter"
    )
    class ExtraGremlin {
        companion object {
            @JvmStatic
            fun Postfix(__result: MonsterGroup, key: String): MonsterGroup {
                if (HumilityZone.isNotInZone()) return __result

                if (key == MonsterHelper.GREMLIN_LEADER_ENC) {
                    val spawnMonster = MonsterHelper::class.java.getDeclaredMethod("spawnGremlin", Float::class.java, Float::class.java).also { it.isAccessible = true }
                    __result.addMonster(0, spawnMonster.invoke(null, GremlinLeader.POSX[2], GremlinLeader.POSY[2]) as AbstractMonster)
                }
                return __result
            }
        }
    }

    @SpirePatch(
        clz = GremlinLeader::class,
        method = "usePreBattleAction"
    )
    class MoreStartingGremlins {
        companion object {
            @JvmStatic
            @SpireInsertPatch(
                rloc = 4
            )
            fun Insert(__instance: GremlinLeader) {
                if (HumilityZone.isNotInZone()) return

                __instance.gremlins[2] = AbstractDungeon.getMonsters().monsters[2]
                __instance.gremlins[3] = null
            }
        }
    }

    @SpirePatch(
        clz = GremlinLeader::class,
        method = "takeTurn"
    )
    class RallyMore {
        companion object {
            @JvmStatic
            @SpireInsertPatch(
                locator = Locator::class
            )
            fun Insert(__instance: GremlinLeader) {
                if (HumilityZone.isNotInZone()) return

                AbstractDungeon.actionManager.addToBottom(SummonGremlinAction(__instance.gremlins))
            }
        }

        private class Locator : SpireInsertLocator() {
            override fun Locate(ctBehavior: CtBehavior?): IntArray {
                val finalMatcher = Matcher.NewExprMatcher(SummonGremlinAction::class.java)
                return LineFinder.findInOrder(ctBehavior, finalMatcher)
            }
        }
    }

    @SpirePatch(
        clz = SummonGremlinAction::class,
        method = "getRandomGremlin"
    )
    class UseNewPosition {
        companion object {
            @JvmStatic
            @SpireInsertPatch(
                locator = Locator::class,
                localvars = ["x", "y"]
            )
            fun aasdf(__instance: SummonGremlinAction, slot: Int, @ByRef x: FloatArray, @ByRef y: FloatArray) {
                if (HumilityZone.isNotInZone()) return

                if (slot == 3) {
                    x[0] = -690f
                    y[0] = 2f
                }
            }
        }

        private class Locator : SpireInsertLocator() {
            override fun Locate(ctBehavior: CtBehavior?): IntArray {
                val finalMatcher = Matcher.MethodCallMatcher(MonsterHelper::class.java, "getGremlin")
                return LineFinder.findInOrder(ctBehavior, finalMatcher)
            }
        }
    }
}
