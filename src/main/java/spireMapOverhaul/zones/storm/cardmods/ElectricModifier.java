package spireMapOverhaul.zones.storm.cardmods;

import basemod.abstracts.AbstractCardModifier;
import basemod.helpers.CardModifierManager;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import spireMapOverhaul.zones.storm.actions.TogglePlayerElecticShaderAction;
import spireMapOverhaul.zones.storm.vfx.DispelPlayerElectricEffect;

import static spireMapOverhaul.SpireAnniversary6Mod.makeID;
import static spireMapOverhaul.util.Wiz.atb;

public class ElectricModifier extends AbstractCardModifier {

    public static String ID = makeID("ElectricModifier");

    @Override
    public AbstractCardModifier makeCopy() {
        return new ElectricModifier();
    }

    @Override
    public boolean shouldApply(AbstractCard card) {
        return !CardModifierManager.hasModifier(card, ID);
    }

    @Override
    public String identifier(AbstractCard card) {
        return ID;
    }


    public String modifyName(String cardName, AbstractCard card) {
        return CardCrawlGame.languagePack.getUIString(makeID("Electric")).TEXT[0] + cardName;
    }

    public String modifyDescription(String rawDescription, AbstractCard card) {
        return rawDescription + CardCrawlGame.languagePack.getUIString(makeID("Electric")).TEXT[1];
    }

    @Override
    public void onUse(AbstractCard card, AbstractCreature target, UseCardAction action) {
        atb(new TogglePlayerElecticShaderAction());
        atb(new VFXAction(new DispelPlayerElectricEffect(AbstractDungeon.player.hb.cX, AbstractDungeon.player.hb.cY)));
        atb(new SFXAction("ORB_PLASMA_CHANNEL", 0.1f));
        atb(new GainEnergyAction(1));
    }
}
