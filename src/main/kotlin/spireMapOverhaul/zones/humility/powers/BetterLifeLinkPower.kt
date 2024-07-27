package spireMapOverhaul.zones.humility.powers

import com.badlogic.gdx.math.MathUtils
import com.evacipated.cardcrawl.modthespire.lib.*
import com.megacrit.cardcrawl.actions.AbstractGameAction
import com.megacrit.cardcrawl.actions.GameActionManager
import com.megacrit.cardcrawl.actions.common.HealAction
import com.megacrit.cardcrawl.actions.common.SetMoveAction
import com.megacrit.cardcrawl.actions.utility.SFXAction
import com.megacrit.cardcrawl.actions.utility.TextAboveCreatureAction
import com.megacrit.cardcrawl.core.AbstractCreature
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import com.megacrit.cardcrawl.monsters.AbstractMonster
import com.megacrit.cardcrawl.monsters.beyond.Darkling
import com.megacrit.cardcrawl.powers.RegrowPower
import javassist.CtBehavior
import spireMapOverhaul.SpireAnniversary6Mod

class BetterLifeLinkPower(
    owner: AbstractCreature
) : AbstractHumilityPower(RegrowPower.POWER_ID, "regrow") {
    companion object {
        val POWER_ID = SpireAnniversary6Mod.makeID("LifeLink")
    }

    init {
        ID = POWER_ID
        this.owner = owner
        updateDescription()
    }

    override fun updateDescription() {
        description = DESCRIPTIONS[0]
    }

    override fun onInitialApplication() {
        AbstractDungeon.getCurrMapNode()?.getRoom()?.cannotLose = true
    }

    class Patches {
        companion object {
            const val REVIVE_WAIT = (-67).toByte()
            const val REVIVE= (-68).toByte()
        }

        @SpirePatch2(
            clz = GameActionManager::class,
            method = "getNextAction"
        )
        class TakeReviveTurn {
            companion object {
                @JvmStatic
                @SpireInsertPatch(
                    locator = Locator::class,
                    localvars = ["m"]
                )
                fun Insert(m: AbstractMonster) {
                    when (m.nextMove) {
                        REVIVE_WAIT -> {
                            AbstractDungeon.actionManager.addToBottom(TextAboveCreatureAction(m, Darkling.DIALOG[0]))
                        }
                        REVIVE -> {
                            listOf(
                                SFXAction(
                                    if (MathUtils.randomBoolean()) {
                                        "DARKLING_REGROW_2"
                                    } else {
                                        "DARKLING_REGROW_1"
                                    },
                                    MathUtils.random(-0.1f, 0.1f)
                                ),
                                HealAction(m, m, m.maxHealth / 2),
                                object : AbstractGameAction() {
                                    override fun update() {
                                        m.halfDead = false
                                        isDone = true
                                    }
                                }
                            ).forEach { AbstractDungeon.actionManager.addToBottom(it) }
                            for (r in AbstractDungeon.player.relics) {
                                r.onSpawnMonster(m)
                            }
                        }
                    }
                }
            }

            private class Locator : SpireInsertLocator() {
                override fun Locate(ctBehavior: CtBehavior?): IntArray {
                    val finalMatcher = Matcher.MethodCallMatcher(AbstractMonster::class.java, "takeTurn")
                    return LineFinder.findInOrder(ctBehavior, finalMatcher)
                }
            }
        }

        @SpirePatch2(
            clz = AbstractMonster::class,
            method = "rollMove"
        )
        class GetReviveMove {
            companion object {
                @JvmStatic
                fun Prefix(__instance: AbstractMonster): SpireReturn<Void> {
                    if (__instance.halfDead && __instance.hasPower(POWER_ID)) {
                        __instance.setMove(REVIVE, AbstractMonster.Intent.BUFF)
                        return SpireReturn.Return()
                    }
                    return SpireReturn.Continue()
                }
            }
        }

        @SpirePatch2(
            clz = AbstractMonster::class,
            method = "damage"
        )
        class HandleDamage {
            companion object {
                @JvmStatic
                fun Postfix(__instance: AbstractMonster) {
                    if (__instance.currentHealth <= 0 && !__instance.halfDead && __instance.hasPower(POWER_ID)) {
                        __instance.halfDead = true
                        for (p in __instance.powers) {
                            p.onDeath()
                        }
                        for (r in AbstractDungeon.player.relics) {
                            r.onMonsterDeath(__instance)
                        }
                        __instance.powers.removeAll { it.ID != POWER_ID }
                        val allDead = AbstractDungeon.getMonsters().monsters
                            .all { !it.hasPower(POWER_ID) || it.halfDead }

                        if (!allDead) {
                            if (__instance.nextMove != REVIVE_WAIT) {
                                __instance.setMove(REVIVE_WAIT, AbstractMonster.Intent.UNKNOWN)
                                __instance.createIntent()
                                AbstractDungeon.actionManager.addToBottom(
                                    SetMoveAction(__instance, REVIVE_WAIT, AbstractMonster.Intent.UNKNOWN)
                                )
                            }
                        } else {
                            AbstractDungeon.getCurrMapNode()?.getRoom()?.cannotLose = false
                            __instance.halfDead = false
                            for (m in AbstractDungeon.getMonsters().monsters) {
                                m.die()
                            }
                        }
                    }
                }
            }
        }

        @SpirePatch2(
            clz = AbstractMonster::class,
            method = "die",
            paramtypez = []
        )
        class StopDie {
            companion object {
                @JvmStatic
                fun Prefix(__instance: AbstractMonster): SpireReturn<Void> {
                    if (AbstractDungeon.getCurrMapNode()?.getRoom()?.cannotLose == true && __instance.hasPower(POWER_ID)) {
                        return SpireReturn.Return()
                    }
                    return SpireReturn.Continue()
                }
            }
        }
    }
}
