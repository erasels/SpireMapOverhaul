package spireMapOverhaul.zones.invasion.powers;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.ExhaustSpecificCardAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import spireMapOverhaul.SpireAnniversary6Mod;

import java.util.ArrayList;

public class VoidPressurePower extends AbstractInvasionPower {
    public static final String POWER_ID = SpireAnniversary6Mod.makeID("VoidPressure");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    private AbstractCreature source;

    public VoidPressurePower(AbstractCreature owner, AbstractCreature source, int amount) {
        // Deliberately not a debuff -- this an effect for the Void Beast fight, but was to easiest to code as a player power
        super(POWER_ID, NAME, PowerType.BUFF, false, owner, amount);
        this.source = source;
    }

    public void updateDescription() {
        this.description = DESCRIPTIONS[0].replace("{0}", this.amount + "");
    }


    @Override
    public void onExhaust(AbstractCard card) {
        this.damagePlayer();
    }

    @Override
    public void onAfterUseCard(AbstractCard card, UseCardAction action) {
        if (card.type == AbstractCard.CardType.POWER) {
            this.damagePlayer();
        }
    }

    private void damagePlayer() {
        this.flash();
        this.addToBot(new DamageAction(AbstractDungeon.player, new DamageInfo(this.source, this.amount, DamageInfo.DamageType.THORNS), AbstractGameAction.AttackEffect.BLUNT_LIGHT));
    }

    @Override
    public void atEndOfTurn(boolean isPlayer) {
        ArrayList<AbstractCard> cards = new ArrayList<>();
        for (AbstractCard c : AbstractDungeon.player.hand.group) {
            if (c.type != AbstractCard.CardType.CURSE && c.type != AbstractCard.CardType.STATUS && !c.isEthereal) {
                cards.add(c);
            }
        }
        if (!cards.isEmpty()) {
            AbstractCard card = cards.get(AbstractDungeon.cardRng.random(cards.size() - 1));
            this.addToTop(new WaitAction(0.1F));
            this.addToTop(new ExhaustSpecificCardAction(card, AbstractDungeon.player.hand));
            this.addToTop(new WaitAction(0.1F));
        }
    }
}