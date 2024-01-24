package spireMapOverhaul.zones.heavenlyClouds;

import basemod.BaseMod;
import basemod.abstracts.AbstractCardModifier;
import basemod.helpers.CardModifierManager;
import basemod.helpers.TooltipInfo;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.UIStrings;
import spireMapOverhaul.SpireAnniversary6Mod;

import java.util.ArrayList;
import java.util.List;

import static spireMapOverhaul.SpireAnniversary6Mod.makeID;
import static spireMapOverhaul.util.Wiz.atb;

public class FlightMod extends AbstractCardModifier {

    public static final String ID = SpireAnniversary6Mod.makeID("FlightMod");
    public static final UIStrings uistrings = CardCrawlGame.languagePack.getUIString(ID);
    public static final String[] TEXT = uistrings.TEXT;

    private boolean used = false;

    @Override
    public AbstractCardModifier makeCopy() {
        return new FlightMod();
    }

    @Override
    public void onDrawn(AbstractCard card) {
        if (!used) {
            used = true;
            card.flash();
            atb(new DrawCardAction(1));
            atb(new AbstractGameAction() {
                @Override
                public void update() {
                    CardModifierManager.removeModifiersById(card, ID, false);
                    isDone = true;
                }
            });
        }
    }

    @Override
    public String identifier(AbstractCard card) {
        return ID;
    }

    @Override
    public String modifyName(String cardName, AbstractCard card) {
        return TEXT[0] + cardName;
    }

    @Override
    public List<TooltipInfo> additionalTooltips(AbstractCard card) {
        ArrayList<TooltipInfo> tooltips = new ArrayList<>();
        tooltips.add(new TooltipInfo(BaseMod.getKeywordTitle(makeID("flighty")), BaseMod.getKeywordDescription(makeID("flighty"))));
        return tooltips;
    }
}
