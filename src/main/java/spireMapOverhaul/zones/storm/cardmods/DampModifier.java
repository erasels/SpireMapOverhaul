package spireMapOverhaul.zones.storm.cardmods;

import basemod.BaseMod;
import basemod.abstracts.AbstractCardModifier;
import basemod.helpers.CardModifierManager;
import basemod.helpers.TooltipInfo;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import spireMapOverhaul.SpireAnniversary6Mod;

import java.util.ArrayList;
import java.util.List;

import static spireMapOverhaul.SpireAnniversary6Mod.makeID;

public class DampModifier extends AbstractCardModifier {

    public static String ID = makeID("DampModifier");

    @Override
    public AbstractCardModifier makeCopy() {
        return new DampModifier();
    }

    @Override
    public boolean shouldApply(AbstractCard card) {
        return !CardModifierManager.hasModifier(card, ID) && !CardModifierManager.hasModifier(card, ElectricModifier.ID);
    }

    @Override
    public String identifier(AbstractCard card) {
        return ID;
    }


    public String modifyName(String cardName, AbstractCard card) {
        return CardCrawlGame.languagePack.getUIString(makeID("Damp")).TEXT[0] + cardName;
    }

    @Override
    public List<TooltipInfo> additionalTooltips(AbstractCard card) {
        ArrayList<TooltipInfo> tooltips = new ArrayList<>();
        tooltips.add(new TooltipInfo(BaseMod.getKeywordTitle(makeID("damp")), BaseMod.getKeywordDescription(makeID("damp"))));
        return tooltips;
    }

    @Override
    public void onInitialApplication(AbstractCard card) {
        card.exhaust = true;
        card.selfRetain = true;
    }
}
