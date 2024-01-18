package spireMapOverhaul.zones.frostlands.cardmods;

import basemod.BaseMod;
import basemod.abstracts.AbstractCardModifier;
import basemod.helpers.CardModifierManager;
import basemod.helpers.TooltipInfo;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.mod.stslib.util.extraicons.ExtraIcons;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.DiscardAction;
import com.megacrit.cardcrawl.actions.common.DiscardSpecificCardAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.actions.utility.ShowCardAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.cards.CardQueueItem;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndAddToDiscardEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardBrieflyEffect;
import spireMapOverhaul.SpireAnniversary6Mod;
import spireMapOverhaul.util.TexLoader;
import spireMapOverhaul.util.Wiz;
import spireMapOverhaul.zones.frostlands.vfx.SnowEffect;
import spireMapOverhaul.zones.frostlands.vfx.TriggerCardActionAndMoveToPile;
import spireMapOverhaul.zones.storm.actions.TogglePlayerElecticShaderAction;
import spireMapOverhaul.zones.storm.vfx.DispelPlayerElectricEffect;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static spireMapOverhaul.SpireAnniversary6Mod.makeID;
import static spireMapOverhaul.SpireAnniversary6Mod.modID;
import static spireMapOverhaul.util.Wiz.atb;

public class FrostbiteModifier extends AbstractCardModifier {

    public static String ID = makeID(FrostbiteModifier.class.getSimpleName());
    private static final Texture tex = TexLoader.getTexture(SpireAnniversary6Mod.makeUIPath("Frostlands/Frostbite.png"));


    public FrostbiteModifier()
    {

    }

    @Override
    public AbstractCardModifier makeCopy() {
        return new FrostbiteModifier();
    }

    @Override
    public boolean shouldApply(AbstractCard card) {
        return !CardModifierManager.hasModifier(card, ID);
    }

    @Override
    public String identifier(AbstractCard card) {
        return ID;
    }

    @Override
    public List<TooltipInfo> additionalTooltips(AbstractCard card) {
        ArrayList<TooltipInfo> tooltips = new ArrayList<>();
        tooltips.add(new TooltipInfo(BaseMod.getKeywordTitle(makeID("frostbite")), BaseMod.getKeywordDescription(makeID("frostbite"))));
        return tooltips;
    }

    @Override
    public void atEndOfTurn(AbstractCard card, CardGroup group) {
        if(AbstractDungeon.player.hand.contains(card))
        {
            Wiz.att(new WaitAction(.1f));
            Wiz.att(new WaitAction(.1f));
            Wiz.att(new VFXAction(new TriggerCardActionAndMoveToPile(card, AbstractDungeon.player.hand, AbstractDungeon.player.discardPile,
                    new DamageAction(AbstractDungeon.player, new DamageInfo(AbstractDungeon.player, 4, DamageInfo.DamageType.THORNS), AbstractGameAction.AttackEffect.SLASH_DIAGONAL))));
        }
    }

    @Override
    public void onRender(AbstractCard card, SpriteBatch sb) {
        ExtraIcons.icon(tex).render(card);
    }

    @Override
    public void onSingleCardViewRender(AbstractCard card, SpriteBatch sb) {
        ExtraIcons.icon(tex).render(card);
    }
}
