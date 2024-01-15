package spireMapOverhaul.zones.mirror.powers;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.LoseHPAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import spireMapOverhaul.SpireAnniversary6Mod;
import spireMapOverhaul.abstracts.AbstractSMOPower;
import spireMapOverhaul.zones.mirror.MirrorZone;
import spireMapOverhaul.zones.mirror.cards.MirrorMove;

public class ShatteredPower extends AbstractSMOPower {
    public static final String POWER_ID = SpireAnniversary6Mod.makeID("Shattered");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    public ShatteredPower(AbstractCreature owner, int amount) {
        super(POWER_ID, NAME, MirrorZone.ID, PowerType.DEBUFF, false, owner, amount);
    }

    @Override
    public void updateDescription() {
        description = DESCRIPTIONS[0].replace("{0}", Integer.toString(amount * 10));
    }

    @Override
    public void onAfterUseCard(AbstractCard card, UseCardAction action) {
        if (card instanceof MirrorMove) {
            flash();
            int powerAmount = this.amount;
            addToBot(new AbstractGameAction() {
                @Override
                public void update() {
                    addToBot(new LoseHPAction(owner, null, (owner.currentHealth * powerAmount + 9) / 10));
                    isDone = true;
                }
            });
        }
    }
}
