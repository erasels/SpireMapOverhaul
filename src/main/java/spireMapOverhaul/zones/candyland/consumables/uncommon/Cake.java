package spireMapOverhaul.zones.candyland.consumables.uncommon;

import basemod.patches.com.megacrit.cardcrawl.dungeons.AbstractDungeon.NoPools;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.BlurPower;
import spireMapOverhaul.zones.candyland.consumables.AbstractConsumable;

import static spireMapOverhaul.SpireAnniversary6Mod.makeID;
import static spireMapOverhaul.util.Wiz.atb;


@NoPools
public class Cake extends AbstractConsumable {
    public final static String ID = makeID(Cake.class.getSimpleName());

    public Cake() {
        super(ID, 3, CardType.SKILL, CardRarity.UNCOMMON, CardTarget.SELF);
        baseBlock = block = 40;
        baseMagicNumber = magicNumber = 1;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        blck();
        atb(new ApplyPowerAction(p, p, new BlurPower(p, magicNumber)));
    }

    @Override
    public void upp() {
        upgradeBlock(10);
    }
}