package spireMapOverhaul.cardvars;

import basemod.abstracts.DynamicVariable;
import com.megacrit.cardcrawl.cards.AbstractCard;
import spireMapOverhaul.abstracts.AbstractSMOCard;

import static spireMapOverhaul.SpireAnniversary6Mod.makeID;

public class SecondDamage extends DynamicVariable {

    @Override
    public String key() {
        return makeID("sd");
    }

    @Override
    public boolean isModified(AbstractCard card) {
        if (card instanceof AbstractSMOCard) {
            return ((AbstractSMOCard) card).isSecondDamageModified;
        }
        return false;
    }

    public void setIsModified(AbstractCard card, boolean v) {
        if (card instanceof AbstractSMOCard) {
            ((AbstractSMOCard) card).isSecondDamageModified = v;
        }
    }

    @Override
    public int value(AbstractCard card) {
        if (card instanceof AbstractSMOCard) {
            return ((AbstractSMOCard) card).secondDamage;
        }
        return -1;
    }

    @Override
    public int baseValue(AbstractCard card) {
        if (card instanceof AbstractSMOCard) {
            return ((AbstractSMOCard) card).baseSecondDamage;
        }
        return -1;
    }

    @Override
    public boolean upgraded(AbstractCard card) {
        if (card instanceof AbstractSMOCard) {
            return ((AbstractSMOCard) card).upgradedSecondDamage;
        }
        return false;
    }
}