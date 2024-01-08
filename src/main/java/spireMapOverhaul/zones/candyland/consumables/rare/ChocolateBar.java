package spireMapOverhaul.zones.candyland.consumables.rare;

import basemod.patches.com.megacrit.cardcrawl.dungeons.AbstractDungeon.NoPools;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.RegenPower;
import spireMapOverhaul.zones.candyland.consumables.AbstractConsumable;

import static spireMapOverhaul.SpireAnniversary6Mod.makeID;
import static spireMapOverhaul.util.Wiz.atb;


@NoPools
public class ChocolateBar extends AbstractConsumable {
    public final static String ID = makeID(ChocolateBar.class.getSimpleName());

    public ChocolateBar() {
        super(ID, 0, CardType.POWER, CardRarity.RARE, CardTarget.SELF);
        baseMagicNumber = magicNumber = 4;
        tags.add(CardTags.HEALING);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        atb(new ApplyPowerAction(p, p, new RegenPower(p, magicNumber)));
    }

    @Override
    public void upp() {
        upgradeMagicNumber(1);
    }
}