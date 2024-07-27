package spireMapOverhaul.zones.candyland.consumables.uncommon;

import basemod.patches.com.megacrit.cardcrawl.dungeons.AbstractDungeon.NoPools;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.ArtifactPower;
import spireMapOverhaul.zones.candyland.consumables.AbstractConsumable;

import static spireMapOverhaul.SpireAnniversary6Mod.makeID;
import static spireMapOverhaul.util.Wiz.atb;


@NoPools
public class Milk extends AbstractConsumable {
    public final static String ID = makeID(Milk.class.getSimpleName());

    public Milk() {
        super(ID, 0, CardType.SKILL, CardRarity.UNCOMMON, CardTarget.ENEMY);
        baseMagicNumber = magicNumber = 2;
        baseSecondMagic = secondMagic = 1;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        atb(new ApplyPowerAction(p, p, new ArtifactPower(p, magicNumber)));
        atb(new ApplyPowerAction(m, p, new ArtifactPower(m, secondMagic)));
    }

    @Override
    public void upp() {
        upgradeMagicNumber(1);
    }
}