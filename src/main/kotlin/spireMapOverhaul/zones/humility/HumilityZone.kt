package spireMapOverhaul.zones.humility

import com.badlogic.gdx.graphics.Color
import com.evacipated.cardcrawl.modthespire.Loader
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import com.megacrit.cardcrawl.dungeons.Exordium
import com.megacrit.cardcrawl.dungeons.TheBeyond
import com.megacrit.cardcrawl.dungeons.TheCity
import com.megacrit.cardcrawl.helpers.input.InputHelper
import com.megacrit.cardcrawl.random.Random
import com.megacrit.cardcrawl.relics.AbstractRelic
import com.megacrit.cardcrawl.rooms.AbstractRoom
import com.megacrit.cardcrawl.rooms.MonsterRoom
import com.megacrit.cardcrawl.rooms.MonsterRoomElite
import spireMapOverhaul.SpireAnniversary6Mod
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
        !hasHumilityMod() && arrayOf(Exordium.ID, TheCity.ID, TheBeyond.ID).contains(AbstractDungeon.id)

    override fun canIncludeEarlyRows(): Boolean =
        AbstractDungeon.actNum != 1

    override fun allowSideConnections(): Boolean =
        false

    override fun allowAdditionalEntrances(): Boolean =
        false

    override fun distributeRooms(rng: Random?, roomList: ArrayList<AbstractRoom>?) {
        // Guarantee at least 1 elite and 2 normal combats
        placeRoomRandomly(rng, roomOrDefault(roomList, {it is MonsterRoomElite}, ::MonsterRoomElite))
        placeRoomRandomly(rng, roomOrDefault(roomList, {it is MonsterRoom}, ::MonsterRoom))
        placeRoomRandomly(rng, roomOrDefault(roomList, {it is MonsterRoom}, ::MonsterRoom))
    }

    private fun isValidRelic(relic: AbstractRelic): Boolean {
        // Relics that involve screens on pickup will softlock combats, so prevent giving them out here
        // We do this by excluding every relic that declares its own update method. Might not be perfect,
        // but it should get most things right.
        try {
            val method = relic.javaClass.getMethod("update")
            return method.declaringClass.equals(AbstractRelic::class.java)
        }
        catch (e: NoSuchMethodException) {
            SpireAnniversary6Mod.logger.info("Error in determining valid relics for Humility zone: Relic " + relic.relicId + " does not have an update method.")
            return true
        }
    }

    override fun onEnter() {
        var relic: AbstractRelic? = null
        while (relic == null || !isValidRelic(relic)) {
            relic = AbstractDungeon.returnRandomRelicEnd(AbstractRelic.RelicTier.RARE)
        }
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

        fun hasHumilityMod(): Boolean =
            Loader.isModLoadedOrSideloaded("humility")
    }
}
