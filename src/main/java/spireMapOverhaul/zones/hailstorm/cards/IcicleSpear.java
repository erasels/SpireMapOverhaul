package spireMapOverhaul.zones.hailstorm.cards;

import basemod.AutoAdd;
import com.evacipated.cardcrawl.mod.stslib.fields.cards.AbstractCard.FleetingField;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import spireMapOverhaul.SpireAnniversary6Mod;
import spireMapOverhaul.abstracts.AbstractSMOCard;

//Unused idea, can be deleted if needed, along with its art
@AutoAdd.Ignore
public class IcicleSpear extends AbstractSMOCard {
    public static final String ID = SpireAnniversary6Mod.makeID(IcicleSpear.class.getSimpleName());
    private static final int COST = 1;

    public IcicleSpear() {
        super(ID, COST, CardType.ATTACK, CardRarity.SPECIAL, CardTarget.ENEMY, CardColor.COLORLESS);
        FleetingField.fleeting.set(this, true);
        baseDamage = 30;
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        dmg(m, AbstractGameAction.AttackEffect.SLASH_VERTICAL);
    }

    public void upp() {
        upgradeDamage(20);
    }
}
