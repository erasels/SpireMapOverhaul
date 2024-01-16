package spireMapOverhaul.zones.candyland.consumables.uncommon;

import basemod.patches.com.megacrit.cardcrawl.dungeons.AbstractDungeon.NoPools;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.PoisonPower;
import spireMapOverhaul.zones.candyland.consumables.AbstractConsumable;

import static spireMapOverhaul.SpireAnniversary6Mod.makeID;
import static spireMapOverhaul.util.Wiz.atb;


@NoPools
public class PoisonedApple extends AbstractConsumable {
    public final static String ID = makeID(PoisonedApple.class.getSimpleName());

    public PoisonedApple() {
        super(ID, 0, CardType.SKILL, CardRarity.UNCOMMON, CardTarget.SELF_AND_ENEMY);
        baseMagicNumber = magicNumber = 9;
        baseSecondMagic = secondMagic = 3;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        atb(new ApplyPowerAction(m, p, new PoisonPower(m, p, magicNumber)));
        atb(new DamageAction(p, new DamageInfo(p, secondMagic, DamageInfo.DamageType.THORNS)));
    }

    @Override
    public void upp() {
        upgradeMagicNumber(3);
    }
}