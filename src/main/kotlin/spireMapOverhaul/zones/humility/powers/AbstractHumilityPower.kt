package spireMapOverhaul.zones.humility.powers

import com.megacrit.cardcrawl.core.CardCrawlGame
import com.megacrit.cardcrawl.localization.PowerStrings
import com.megacrit.cardcrawl.powers.AbstractPower
import spireMapOverhaul.SpireAnniversary6Mod

abstract class AbstractHumilityPower(
    id: String,
    regionName: String = id.removePrefix(SpireAnniversary6Mod.makeID(""))
) : AbstractPower() {
    protected val powerStrings: PowerStrings
    protected val NAME: String
    protected val DESCRIPTIONS: Array<String>

    init {
        ID = id
        powerStrings = CardCrawlGame.languagePack.getPowerStrings(ID)
        NAME = powerStrings.NAME
        name = NAME
        DESCRIPTIONS = powerStrings.DESCRIPTIONS
        loadRegion(regionName)
    }
}
