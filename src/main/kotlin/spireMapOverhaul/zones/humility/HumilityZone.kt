package spireMapOverhaul.zones.humility

import com.badlogic.gdx.graphics.Color
import com.evacipated.cardcrawl.modthespire.Loader
import com.megacrit.cardcrawl.core.Settings
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import com.megacrit.cardcrawl.helpers.input.InputHelper
import com.megacrit.cardcrawl.relics.AbstractRelic
import com.megacrit.cardcrawl.relics.ThreadAndNeedle
import spireMapOverhaul.abstracts.AbstractZone
import spireMapOverhaul.util.Wiz
import spireMapOverhaul.zoneInterfaces.OnTravelZone

class HumilityZone : AbstractZone(ID, Icons.MONSTER), OnTravelZone {
    init {
        width = 2
        height = 4
        maxWidth = 4
        maxHeight = 5
    }

    override fun copy(): AbstractZone =
        HumilityZone()

    override fun getColor(): Color =
        Companion.color

    override fun canSpawn(): Boolean =
        !Loader.isModLoadedOrSideloaded("humility")

    override fun canIncludeEarlyRows(): Boolean =
        AbstractDungeon.actNum != 1

    override fun allowSideConnections(): Boolean =
        false

    override fun allowAdditionalEntrances(): Boolean =
        true

    override fun onEnter() {
        val relic = AbstractDungeon.returnRandomRelicEnd(AbstractRelic.RelicTier.RARE)
        AbstractDungeon.getCurrMapNode()?.getRoom()?.spawnRelicAndObtain(
            InputHelper.mX.toFloat(),
            InputHelper.mY.toFloat(),
            relic
        )
    }

    companion object {
        const val ID = "Humility"
        private val color = Color.valueOf("3498db")

        @JvmStatic
        fun isInZone(): Boolean =
            Wiz.getCurZone() is HumilityZone

        @JvmStatic
        fun isNotInZone(): Boolean =
            !isInZone()
    }
}
