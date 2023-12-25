package spireMapOverhaul.zones.candyland.consumables.uncommon;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.DrawReductionPower;
import com.megacrit.cardcrawl.powers.watcher.EnergyDownPower;
import spireMapOverhaul.zones.candyland.consumables.AbstractConsumable;

import static spireMapOverhaul.SpireAnniversary6Mod.makeID;
import static spireMapOverhaul.util.Wiz.atb;


public class Sugar extends AbstractConsumable {
    public final static String ID = makeID(Sugar.class.getSimpleName());

    public Sugar() {
        super(ID, 0, CardType.SKILL, CardRarity.UNCOMMON, CardTarget.SELF);
        baseMagicNumber = magicNumber = 2;
        baseSecondMagic = secondMagic = 1;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        atb(new GainEnergyAction(magicNumber));
        atb(new DrawCardAction(magicNumber));
        atb(new ApplyPowerAction(p, p, new EnergyDownPower(p, secondMagic)));
        atb(new ApplyPowerAction(p, p, new DrawReductionPower(p, secondMagic)));
    }

    @Override
    public void upp() {
        upgradeMagicNumber(1);
    }
}