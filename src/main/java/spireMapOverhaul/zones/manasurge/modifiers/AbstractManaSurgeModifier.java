package spireMapOverhaul.zones.manasurge.modifiers;

import basemod.abstracts.AbstractCardModifier;
import com.megacrit.cardcrawl.cards.AbstractCard;

public abstract class AbstractManaSurgeModifier extends AbstractCardModifier {
    public boolean isPermanent;

    public AbstractManaSurgeModifier(boolean permanent) {
        isPermanent = permanent;
    }

    @Override
    public boolean removeAtEndOfTurn(AbstractCard card) {
        return !card.retain && !isPermanent;
    }
}
