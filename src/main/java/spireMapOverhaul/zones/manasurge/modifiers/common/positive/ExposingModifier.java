package spireMapOverhaul.zones.manasurge.modifiers.common.positive;

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
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import spireMapOverhaul.util.TexLoader;
import spireMapOverhaul.zones.manasurge.interfaces.ModifierTags;
import spireMapOverhaul.zones.manasurge.utils.ManaSurgeTags;

import java.util.ArrayList;
import java.util.List;

import static spireMapOverhaul.SpireAnniversary6Mod.makeImagePath;

public class ExposingModifier extends AbstractCardModifier implements ModifierTags {
    private static final Texture ICON = TexLoader.getTexture(makeImagePath("ui/extraIcons/PositiveEnchantmentIcon.png"));
    private static final int VULN_AMT = 1;

    @Override
    public boolean isPositiveModifier() {
        return true;
    }

    @Override
    public boolean isCommonModifier() {
        return true;
    }

    @Override
    public void onUse(AbstractCard card, AbstractCreature target, UseCardAction action) {
        if (card.target == AbstractCard.CardTarget.ENEMY) {
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(target, AbstractDungeon.player, new VulnerablePower(target,VULN_AMT,false),VULN_AMT));
        } else {
            AbstractMonster mo = AbstractDungeon.getRandomMonster();
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(mo, AbstractDungeon.player, new VulnerablePower(mo,VULN_AMT,false),VULN_AMT));
        }
    }

    @Override
    public List<TooltipInfo> additionalTooltips(AbstractCard card) {
        List<TooltipInfo> tooltips = new ArrayList<>();
        tooltips.add(new TooltipInfo("[#8c9cff]Exposing[]", "Apply #b1 #yVulnerable to the target enemy, or a random enemy if not a single target."));
        return tooltips;
    }

    @Override
    public boolean removeAtEndOfTurn(AbstractCard card) {
        return !card.retain || !card.hasTag(ManaSurgeTags.PERMANENT_MODIFIER);
    }

    @Override
    public String modifyDescription(String rawDescription, AbstractCard card) {
        return "[#8c9cff]Exposing[]." + " NL " + rawDescription;
    }

    @Override
    public void onRender(AbstractCard card, SpriteBatch sb) {
        ExtraIcons.icon(ICON).render(card);
    }

    @Override
    public AbstractCardModifier makeCopy() {
        return new ExposingModifier();
    }
}