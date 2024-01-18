package spireMapOverhaul.zones.humility.patches.utils

import com.megacrit.cardcrawl.monsters.AbstractMonster
import javassist.CtBehavior
import javassist.CtMethod
import javassist.CtNewMethod
import javassist.NotFoundException
import javassist.bytecode.DuplicateMemberException
import kotlin.reflect.KFunction1
import kotlin.reflect.jvm.javaMethod

fun <T : AbstractMonster> CtBehavior.addPreBattleAction(callback: KFunction1<T, Unit>) {
    val method = callback.javaMethod!!
    this.addToMethod(
        "usePreBattleAction",
        callback
    )
}

fun <T : AbstractMonster> CtBehavior.addEscape(callback: KFunction1<T, Unit>) {
    val method = callback.javaMethod!!
    this.addToMethod(
        "escape",
        callback
    )
}

fun <T> CtBehavior.addToMethod(methodName: String, callback: KFunction1<T, Unit>) {
    val method = callback.javaMethod!!
    this.addToMethod(
        methodName,
        "${method.declaringClass.declaringClass.name}.${method.name}(this);"
    )
}

fun CtBehavior.addToMethod(methodName: String, src: String) {
    // Find method in superclass*
    var superClass = declaringClass.superclass
    var superMethod: CtMethod? = null
    do {
        try {
            superMethod = superClass.getDeclaredMethod(methodName)
        } catch (_: NotFoundException) {
            superClass = superClass.superclass
        }
    } while (superMethod == null)

    var method = CtNewMethod.delegator(
        superMethod,
        declaringClass
    )
    try {
        declaringClass.addMethod(method)
    } catch (e: DuplicateMemberException) {
        method = declaringClass.getDeclaredMethod(methodName)
    }

    method.insertAfter(src)
}
