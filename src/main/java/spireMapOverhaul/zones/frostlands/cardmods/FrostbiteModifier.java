package spireMapOverhaul.zones.frostlands.cardmods;

import basemod.BaseMod;
import basemod.abstracts.AbstractCardModifier;
import basemod.helpers.CardModifierManager;
import basemod.helpers.TooltipInfo;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.mod.stslib.util.extraicons.ExtraIcons;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import spireMapOverhaul.SpireAnniversary6Mod;
import spireMapOverhaul.util.TexLoader;
import spireMapOverhaul.util.Wiz;
import spireMapOverhaul.zones.frostlands.vfx.TriggerCardActionAndMoveToPile;

import java.util.ArrayList;
import java.util.List;

import static spireMapOverhaul.SpireAnniversary6Mod.makeID;

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
