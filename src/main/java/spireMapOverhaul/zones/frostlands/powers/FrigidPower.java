package spireMapOverhaul.zones.frostlands.powers;

import basemod.helpers.CardModifierManager;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import spireMapOverhaul.abstracts.AbstractSMOPower;
import spireMapOverhaul.zones.frostlands.cardmods.FrostbiteModifier;

import static spireMapOverhaul.SpireAnniversary6Mod.makeID;

public class FrigidPower extends AbstractSMOPower {
    public static String POWER_ID = makeID(FrigidPower.class.getSimpleName());
    public static PowerStrings strings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static String[] DESCRIPTIONS = strings.DESCRIPTIONS;

    private int numCards = 0;

    public FrigidPower(AbstractCreature owner, int amount) {
        super(POWER_ID, strings.NAME, "Frostlands", AbstractPower.PowerType.BUFF, false, owner, amount);
        numCards = 0;
    }

    @Override
    public void atStartOfTurn() {
        numCards = 0;
    }

    @Override
    public void onCardDraw(AbstractCard card) {
        if (numCards < amount && !CardModifierManager.hasModifier(card, FrostbiteModifier.ID)) {
            numCards++;
            flashWithoutSound();
            updateDescription();
            CardModifierManager.addModifier(card, new FrostbiteModifier());
        }
    }

    @Override
    public void updateDescription() {
        if (amount == 1) {
            description = DESCRIPTIONS[0];
        } else {
            description = DESCRIPTIONS[1] + amount + DESCRIPTIONS[2];
        }
    }
}
