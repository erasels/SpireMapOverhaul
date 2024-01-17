package spireMapOverhaul.zones.candyland.consumables.common;

import basemod.patches.com.megacrit.cardcrawl.dungeons.AbstractDungeon.NoPools;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.DexterityPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import spireMapOverhaul.zones.candyland.consumables.AbstractConsumable;

import static spireMapOverhaul.SpireAnniversary6Mod.makeID;
import static spireMapOverhaul.util.Wiz.atb;


@NoPools
public class Broccoli extends AbstractConsumable {
    public final static String ID = makeID(Broccoli.class.getSimpleName());

    public Broccoli() {
        super(ID, 1, CardType.POWER, CardRarity.COMMON, CardTarget.SELF);
        baseMagicNumber = magicNumber = 2;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        atb(new ApplyPowerAction(p, p, new StrengthPower(p, magicNumber)));
        atb(new ApplyPowerAction(p, p, new DexterityPower(p, magicNumber)));
    }

    @Override
    public void upp() {
        upgradeMagicNumber(1);
    }
}