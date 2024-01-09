package spireMapOverhaul.zones.manasurge.modifiers;

import basemod.abstracts.AbstractCardModifier;
import com.megacrit.cardcrawl.cards.AbstractCard;

public abstract class AbstractManaSurgeModifier extends AbstractCardModifier {

    public boolean isPermanent;

    public enum ModRarity {
        COMMON_MOD,
        UNCOMMON_MOD
    }

    public enum ModEffect {
        POSITIVE_MOD,
        NEGATIVE_MOD
    }

    private final ModRarity modRarity;
    private final ModEffect modEffect;


    public AbstractManaSurgeModifier(boolean permanent, ModRarity modRarity, ModEffect modEffect) {
        isPermanent = permanent;
        this.modRarity = modRarity;
        this.modEffect = modEffect;
    }

    public ModRarity getModRarity() {
        return modRarity;
    }

    public ModEffect getModEffect() {
        return modEffect;
    }


    @Override
    public boolean removeAtEndOfTurn(AbstractCard card) {
        return !card.selfRetain && !card.retain && !isPermanent;
    }
}
