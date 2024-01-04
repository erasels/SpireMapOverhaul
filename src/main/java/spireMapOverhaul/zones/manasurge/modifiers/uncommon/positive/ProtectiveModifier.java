package spireMapOverhaul.zones.manasurge.modifiers.uncommon.positive;

import basemod.abstracts.AbstractCardModifier;
import basemod.helpers.TooltipInfo;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.mod.stslib.util.extraicons.ExtraIcons;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.DexterityPower;
import spireMapOverhaul.util.TexLoader;
import spireMapOverhaul.zones.manasurge.interfaces.ModifierTags;
import spireMapOverhaul.zones.manasurge.modifiers.AbstractManaSurgeModifier;

import java.util.ArrayList;
import java.util.List;

import static spireMapOverhaul.SpireAnniversary6Mod.makeImagePath;

public class ProtectiveModifier extends AbstractManaSurgeModifier implements ModifierTags {
    private static final Texture ICON = TexLoader.getTexture(makeImagePath("ui/extraIcons/PositiveEnchantmentIcon.png"));
    private static final int DEX_AMT = 1;

    public ProtectiveModifier(boolean permanent) {
        super(permanent);
    }

    @Override
    public boolean isPositiveModifier() {
        return true;
    }

    @Override
    public boolean isCommonModifier() {
        return false;
    }

    @Override
    public void onUse(AbstractCard card, AbstractCreature target, UseCardAction action) {
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new DexterityPower(AbstractDungeon.player,DEX_AMT),DEX_AMT));
    }

    @Override
    public List<TooltipInfo> additionalTooltips(AbstractCard card) {
        List<TooltipInfo> tooltips = new ArrayList<>();
        tooltips.add(new TooltipInfo("[#8c9cff]Protective[]", "Gain #b1 #yDexterity."));
        return tooltips;
    }

    @Override
    public String modifyDescription(String rawDescription, AbstractCard card) {
        return "[#8c9cff]Protective[]." + " NL " + rawDescription;
    }

    @Override
    public void onRender(AbstractCard card, SpriteBatch sb) {
        ExtraIcons.icon(ICON).render(card);
    }

    @Override
    public AbstractCardModifier makeCopy() {
        return new ProtectiveModifier(isPermanent);
    }
}