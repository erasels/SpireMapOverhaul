package spireMapOverhaul.zones.invasion.powers;

import com.megacrit.cardcrawl.actions.common.MakeTempCardInDiscardAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import spireMapOverhaul.SpireAnniversary6Mod;
import spireMapOverhaul.zones.invasion.cards.GildedDefend;
import spireMapOverhaul.zones.invasion.cards.GildedEssence;
import spireMapOverhaul.zones.invasion.cards.GildedStrike;

import java.util.Arrays;

public class MidasAuraPower extends AbstractInvasionPower {
    public static final String POWER_ID = SpireAnniversary6Mod.makeID("MidasAura");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;

    public MidasAuraPower(AbstractCreature owner) {
        super(POWER_ID, NAME, PowerType.BUFF, false, owner, 0);
    }

    @Override
    public void onUseCard(AbstractCard card, UseCardAction action) {
        if (card.upgraded && !Arrays.asList(GildedStrike.ID, GildedDefend.ID, GildedEssence.ID).contains(card.cardID)) {
            AbstractCard gildedCard = gildCard(card);
            if (gildedCard != null) {
                this.flash();
                if(card.type != AbstractCard.CardType.POWER) {
                    action.exhaustCard = true;
                }
                AbstractDungeon.actionManager.addToBottom(new MakeTempCardInDiscardAction(gildedCard, 1));
            }
        }
    }

    private static AbstractCard gildCard(AbstractCard card) {
        if (card == null) {
            return null;
        }
        if (card.type == AbstractCard.CardType.ATTACK){
            return new GildedStrike();
        }
        if (card.type == AbstractCard.CardType.SKILL){
            return new GildedDefend();
        }
        if (card.type == AbstractCard.CardType.POWER){
            return new GildedEssence();
        }
        return null;
    }
}

