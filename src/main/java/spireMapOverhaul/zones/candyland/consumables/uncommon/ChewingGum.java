package spireMapOverhaul.zones.candyland.consumables.uncommon;

import basemod.patches.com.megacrit.cardcrawl.dungeons.AbstractDungeon.NoPools;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDrawPileAction;
import com.megacrit.cardcrawl.cards.status.Slimed;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import spireMapOverhaul.zones.candyland.consumables.AbstractConsumable;

import static spireMapOverhaul.SpireAnniversary6Mod.makeID;
import static spireMapOverhaul.util.Wiz.atb;


@NoPools
public class ChewingGum extends AbstractConsumable {
    public final static String ID = makeID(ChewingGum.class.getSimpleName());

    public ChewingGum() {
        super(ID, 0, CardType.SKILL, CardRarity.UNCOMMON, CardTarget.SELF);
        baseMagicNumber = magicNumber = 2;
        cardsToPreview = new Slimed();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        atb(new GainEnergyAction(magicNumber));
        atb(new DrawCardAction(magicNumber));
        atb(new MakeTempCardInDrawPileAction(cardsToPreview.makeCopy(), 1, false, true));
    }

    @Override
    public void upp() {
        upgradeMagicNumber(1);
    }
}