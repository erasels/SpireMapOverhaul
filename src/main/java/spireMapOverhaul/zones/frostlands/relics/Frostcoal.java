package spireMapOverhaul.zones.frostlands.relics;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import spireMapOverhaul.abstracts.AbstractSMORelic;
import spireMapOverhaul.util.Wiz;
import spireMapOverhaul.zones.frostlands.FrostlandsZone;
import spireMapOverhaul.zones.frostlands.powers.ChillPower;

import static spireMapOverhaul.SpireAnniversary6Mod.makeID;

public class Frostcoal extends AbstractSMORelic{
    public static final String ID = makeID(Frostcoal.class.getSimpleName());
    public static final int amount = 6;
    public Frostcoal() {
        super(ID, FrostlandsZone.ID, RelicTier.SPECIAL, LandingSound.CLINK);
    }

    @Override
    public void atBattleStart() {
        for (AbstractMonster m: AbstractDungeon.getCurrRoom().monsters.monsters)
            Wiz.atb(new ApplyPowerAction(m, Wiz.adp(), new ChillPower(m, amount)));
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0] + amount + DESCRIPTIONS[1];
    }

    @Override
    public AbstractRelic makeCopy() {
        return new Frostcoal();
    }

}
