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

        fun hasHumilityMod(): Boolean =
            Loader.isModLoadedOrSideloaded("humility")
    }
}
