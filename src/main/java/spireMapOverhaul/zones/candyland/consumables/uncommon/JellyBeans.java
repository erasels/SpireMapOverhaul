package spireMapOverhaul.zones.candyland.consumables.uncommon;

import basemod.patches.com.megacrit.cardcrawl.dungeons.AbstractDungeon.NoPools;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.megacrit.cardcrawl.powers.WeakPower;
import spireMapOverhaul.zones.candyland.consumables.AbstractConsumable;

import java.util.Iterator;

import static spireMapOverhaul.SpireAnniversary6Mod.makeID;
import static spireMapOverhaul.util.Wiz.atb;


@NoPools
public class JellyBeans extends AbstractConsumable {
    public final static String ID = makeID(JellyBeans.class.getSimpleName());

    public JellyBeans() {
        super(ID, 0, CardType.SKILL, CardRarity.UNCOMMON, CardTarget.ALL_ENEMY);
        baseMagicNumber = magicNumber = 3;
        baseSecondMagic = secondMagic = 1;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        Iterator iter = AbstractDungeon.getCurrRoom().monsters.monsters.iterator();
        while(iter.hasNext()) {
            AbstractMonster mo = (AbstractMonster)iter.next();
            atb(new ApplyPowerAction(mo, p, new VulnerablePower(mo, magicNumber, false)));
        }
        atb(new ApplyPowerAction(p, p, new WeakPower(p, secondMagic, false)));
    }

    @Override
    public void upp() {
        upgradeMagicNumber(1);
    }
}