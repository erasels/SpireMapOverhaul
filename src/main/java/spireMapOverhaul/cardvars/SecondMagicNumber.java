package spireMapOverhaul.cardvars;

import basemod.abstracts.DynamicVariable;
import com.megacrit.cardcrawl.cards.AbstractCard;
import spireMapOverhaul.abstracts.AbstractSMOCard;

import static spireMapOverhaul.SpireAnniversary6Mod.makeID;

public class SecondMagicNumber extends DynamicVariable {

    @Override
    public String key() {
        return makeID("m2");
    }

    @Override
    public boolean isModified(AbstractCard card) {
        if (card instanceof AbstractSMOCard) {
            return ((AbstractSMOCard) card).isSecondMagicModified;
        }
        return false;
    }

    @Override
    public int value(AbstractCard card) {
        if (card instanceof AbstractSMOCard) {
            return ((AbstractSMOCard) card).secondMagic;
        }
        return -1;
    }

    public void setIsModified(AbstractCard card, boolean v) {
        if (card instanceof AbstractSMOCard) {
            ((AbstractSMOCard) card).isSecondMagicModified = v;
        }
    }

    @Override
    public int baseValue(AbstractCard card) {
        if (card instanceof AbstractSMOCard) {
            return ((AbstractSMOCard) card).baseSecondMagic;
        }
        return -1;
    }

    @Override
    public boolean upgraded(AbstractCard card) {
        if (card instanceof AbstractSMOCard) {
            return ((AbstractSMOCard) card).upgradedSecondMagic;
        }
        return false;
    }
}