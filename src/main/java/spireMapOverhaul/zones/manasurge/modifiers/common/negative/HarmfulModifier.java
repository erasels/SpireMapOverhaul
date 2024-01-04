package spireMapOverhaul.zones.manasurge.modifiers.common.negative;

import basemod.abstracts.AbstractCardModifier;
import basemod.helpers.TooltipInfo;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.mod.stslib.util.extraicons.ExtraIcons;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import spireMapOverhaul.util.TexLoader;
import spireMapOverhaul.zones.manasurge.interfaces.ModifierTags;
import spireMapOverhaul.zones.manasurge.utils.ManaSurgeTags;

import java.util.ArrayList;
import java.util.List;

import static spireMapOverhaul.SpireAnniversary6Mod.makeImagePath;

public class HarmfulModifier extends AbstractCardModifier implements ModifierTags {
    private static final Texture ICON = TexLoader.getTexture(makeImagePath("ui/extraIcons/NegativeEnchantmentIcon.png"));
    private static final int DAMAGE = 5;

    @Override
    public boolean isPositiveModifier() {
        return false;
    }

    @Override
    public boolean isCommonModifier() {
        return true;
    }

    @Override
    public void onUse(AbstractCard card, AbstractCreature target, UseCardAction action) {
        DamageInfo info = new DamageInfo(AbstractDungeon.player, DAMAGE, DamageInfo.DamageType.THORNS);
        AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, info,AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
    }

    @Override
    public List<TooltipInfo> additionalTooltips(AbstractCard card) {
        List<TooltipInfo> tooltips = new ArrayList<>();
        tooltips.add(new TooltipInfo("[#ff8cd5]Harmful[]", "Take #b5 damage."));
        return tooltips;
    }

    @Override
    public boolean removeAtEndOfTurn(AbstractCard card) {
        return !card.retain || !card.hasTag(ManaSurgeTags.PERMANENT_MODIFIER);
    }

    @Override
    public String modifyDescription(String rawDescription, AbstractCard card) {
        return "[#ff8cd5]Harmful[]." + " NL " + rawDescription;
    }

    @Override
    public void onRender(AbstractCard card, SpriteBatch sb) {
        ExtraIcons.icon(ICON).render(card);
    }

    @Override
    public AbstractCardModifier makeCopy() {
        return new HarmfulModifier();
    }
}