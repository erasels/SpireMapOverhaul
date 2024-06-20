package spireMapOverhaul.zones.humility.patches.exordium

import basemod.ReflectionHacks
import com.badlogic.gdx.math.MathUtils
import com.evacipated.cardcrawl.modthespire.lib.*
import com.megacrit.cardcrawl.actions.common.EscapeAction
import com.megacrit.cardcrawl.actions.common.SpawnMonsterAction
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import com.megacrit.cardcrawl.helpers.MonsterHelper
import com.megacrit.cardcrawl.monsters.AbstractMonster
import com.megacrit.cardcrawl.monsters.exordium.*
import com.megacrit.cardcrawl.powers.AngerPower
import com.megacrit.cardcrawl.powers.MinionPower
import javassist.CtBehavior
import spireMapOverhaul.zones.humility.HumilityZone
import spireMapOverhaul.zones.humility.patches.utils.addToMethod
import kotlin.random.asKotlinRandom

class NobMinions {
    @SpirePatch(
        clz = GremlinNob::class,
        method = "takeTurn"
    )
    class Summon {
        companion object {
            @JvmStatic
            @SpireInsertPatch(
                locator = Locator::class
            )
            fun Insert(__instance: GremlinNob) {
                if (HumilityZone.isNotInZone()) return

                if (AbstractDungeon.getMonsters().monsters.size > 1) {
                    return
                }

                for (i in 0 until 2) {
                    AbstractDungeon.actionManager.addToBottom(
                        object : SpawnMonsterAction(
                            getRandomGremlin(-300f + -185f * i, MathUtils.random(-5f, 25f)),
                            true
                        ) {
                            override fun update() {
                                super.update()
                                if (isDone) {
                                    val m = ReflectionHacks.getPrivate(
                                        this,
                                        SpawnMonsterAction::class.java,
                                        "m"
                                    ) as AbstractMonster
                                    m.usePreBattleAction()
                                }
                            }
                        }
                    )
                }
            }

            private val gremlinPool = listOf(
                GremlinWarrior.ID,
                GremlinWarrior.ID,
                GremlinThief.ID,
                GremlinThief.ID,
                GremlinFat.ID,
                GremlinFat.ID,
                GremlinTsundere.ID,
                GremlinWizard.ID
            )

            fun getRandomGremlin(x: Float, y: Float): AbstractMonster {
                return MonsterHelper.getGremlin(gremlinPool.random(AbstractDungeon.aiRng.random.asKotlinRandom()), x, y)
            }
        }

        private class Locator : SpireInsertLocator() {
            override fun Locate(ctBehavior: CtBehavior?): IntArray {
                val finalMatcher = Matcher.NewExprMatcher(AngerPower::class.java)
                return LineFinder.findAllInOrder(ctBehavior, finalMatcher)
            }
        }
    }

    @SpirePatch(
        clz = GremlinNob::class,
        method = SpirePatch.CONSTRUCTOR,
        paramtypez = [Float::class, Float::class]
    )
    class Die {
        companion object {
            @JvmStatic
            fun Raw(ctBehavior: CtBehavior) {
                ctBehavior.addToMethod("die", Companion::die)
            }

            @JvmStatic
            fun die(__instance: GremlinNob) {
                if (HumilityZone.isNotInZone()) return

                AbstractDungeon.getMonsters().monsters
                    .filterNot { it.isDeadOrEscaped }
                    .filter { it.hasPower(MinionPower.POWER_ID) }
                    .forEach {
                        AbstractDungeon.actionManager.addToBottom(EscapeAction(it))
                    }
            }
        }
    }
}
