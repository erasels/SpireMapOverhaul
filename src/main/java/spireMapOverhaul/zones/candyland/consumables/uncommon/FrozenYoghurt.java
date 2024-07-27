package spireMapOverhaul.zones.candyland.consumables.uncommon;

import basemod.patches.com.megacrit.cardcrawl.dungeons.AbstractDungeon.NoPools;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.MetallicizePower;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import spireMapOverhaul.zones.candyland.consumables.AbstractConsumable;

import static spireMapOverhaul.SpireAnniversary6Mod.makeID;
import static spireMapOverhaul.util.Wiz.atb;


@NoPools
public class FrozenYoghurt extends AbstractConsumable {
    public final static String ID = makeID(FrozenYoghurt.class.getSimpleName());

    public FrozenYoghurt() {
        super(ID, 0, CardType.POWER, CardRarity.UNCOMMON, CardTarget.SELF);
        baseMagicNumber = magicNumber = 8;
        baseSecondMagic = secondMagic = 2;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        atb(new ApplyPowerAction(p, p, new MetallicizePower(p, magicNumber)));
        atb(new ApplyPowerAction(p, p, new VulnerablePower(p, secondMagic, false)));
    }

    @Override
    public void upp() {
        upgradeSecondMagic(-1);
    }
}