package spireMapOverhaul.zones.candyland.consumables.rare;

import basemod.patches.com.megacrit.cardcrawl.dungeons.AbstractDungeon.NoPools;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import spireMapOverhaul.zones.candyland.actions.ChangeMaxHPAction;
import spireMapOverhaul.zones.candyland.consumables.AbstractConsumable;

import static spireMapOverhaul.SpireAnniversary6Mod.makeID;
import static spireMapOverhaul.util.Wiz.atb;


@NoPools
public class Banana extends AbstractConsumable {
    public final static String ID = makeID(Banana.class.getSimpleName());

    public Banana() {
        super(ID, 0, CardType.SKILL, CardRarity.RARE, CardTarget.SELF);
        baseMagicNumber = magicNumber = 2;
        tags.add(CardTags.HEALING);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        atb(new ChangeMaxHPAction(p, p, this.magicNumber));
    }

    @Override
    public void upp() {
        upgradeMagicNumber(1);
    }
}