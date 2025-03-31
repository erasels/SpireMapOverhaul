package spireMapOverhaul.zones.humidity.relics;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.PlatedArmorPower;
import spireMapOverhaul.abstracts.AbstractSMORelic;
import spireMapOverhaul.zones.gremlinTown.GremlinTown;
import spireMapOverhaul.zones.humidity.HumidityZone;

import static spireMapOverhaul.SpireAnniversary6Mod.makeID;

public class Pants extends AbstractSMORelic {
    public static final String ID = makeID(Pants.class.getSimpleName());
    private static final int ARMOR_AMT = 2;

    public Pants() {
        super(ID, HumidityZone.ID, RelicTier.SPECIAL, LandingSound.FLAT);
    }

    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0] + ARMOR_AMT + this.DESCRIPTIONS[1];
    }

    public void atBattleStart() {
        this.flash();
        this.addToTop(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new PlatedArmorPower(AbstractDungeon.player, ARMOR_AMT), ARMOR_AMT));
        this.addToTop(new RelicAboveCreatureAction(AbstractDungeon.player, this));
    }

}
