package spireMapOverhaul.zones.manasurge.modifiers;

import basemod.abstracts.AbstractCardModifier;
import com.megacrit.cardcrawl.cards.AbstractCard;

public abstract class AbstractManaSurgeModifier extends AbstractCardModifier {

    public boolean isPermanent;

    public enum ModRarity {
        COMMON_MOD,
        UNCOMMON_MOD
    }

    private final ModRarity modRarity;

    public AbstractManaSurgeModifier(boolean permanent, ModRarity modRarity) {
        isPermanent = permanent;
        this.modRarity = modRarity;
    }

    public ModRarity getModRarity() {
        return modRarity;
    }

    @Override
    public boolean removeAtEndOfTurn(AbstractCard card) {
        return !card.retain && !isPermanent;
    }
}
