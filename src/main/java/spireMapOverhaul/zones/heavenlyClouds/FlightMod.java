package spireMapOverhaul.zones.heavenlyClouds;

import basemod.abstracts.AbstractCardModifier;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;
import spireMapOverhaul.SpireAnniversary6Mod;

public class FlightMod extends AbstractCardModifier {

    public static final String ID = SpireAnniversary6Mod.makeID("FlightMod");
    public static final UIStrings uistrings = CardCrawlGame.languagePack.getUIString(ID);
    public static final String[] TEXT = uistrings.TEXT;

    @Override
    public AbstractCardModifier makeCopy() {
        return new FlightMod();
    }

    @Override
    public void onUse(AbstractCard card, AbstractCreature target, UseCardAction action) {
        AbstractDungeon.actionManager.addToBottom(new DrawCardAction(1));
    }

    @Override
    public String identifier(AbstractCard card) {
        return ID;
    }

    @Override
    public String modifyName(String cardName, AbstractCard card) {
        return TEXT[1] + cardName;
    }

    @Override
    public String modifyDescription(String rawDescription, AbstractCard card) {
        return rawDescription + TEXT[0];
    }
}
