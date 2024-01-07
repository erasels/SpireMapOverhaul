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
import com.megacrit.cardcrawl.localization.LocalizedStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.WeakPower;
import spireMapOverhaul.SpireAnniversary6Mod;
import spireMapOverhaul.util.TexLoader;
import spireMapOverhaul.zones.manasurge.ManaSurgeZone;
import spireMapOverhaul.zones.manasurge.modifiers.AbstractManaSurgeModifier;

import java.util.ArrayList;
import java.util.List;

import static spireMapOverhaul.SpireAnniversary6Mod.makeUIPath;

public class CripplingModifier extends AbstractManaSurgeModifier {
    public static final String ID = SpireAnniversary6Mod.makeID("ManaSurge:Crippling");
    private static final ModRarity MOD_RARITY = ModRarity.COMMON_MOD;
    private static final ModEffect MOD_EFFECT = ModEffect.POSITIVE_MOD;

    private static final Texture ICON = TexLoader.getTexture(makeUIPath("ManaSurge/extraIcons/PositiveEnchantmentIcon.png"));
    private static final int WEAK_AMT = 1;

    public CripplingModifier(boolean permanent) {
        super(permanent,MOD_RARITY,MOD_EFFECT);
    }

    @Override
    public void onUse(AbstractCard card, AbstractCreature target, UseCardAction action) {
        if (card.target == AbstractCard.CardTarget.ENEMY) {
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(target, AbstractDungeon.player, new WeakPower(target,WEAK_AMT,false),WEAK_AMT));
        } else {
            AbstractMonster mo = AbstractDungeon.getRandomMonster();
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(mo, AbstractDungeon.player, new WeakPower(mo,WEAK_AMT,false),WEAK_AMT));
        }
    }

    @Override
    public List<TooltipInfo> additionalTooltips(AbstractCard card) {
        List<TooltipInfo> tooltips = new ArrayList<>();
        tooltips.add(new TooltipInfo(ManaSurgeZone.getKeywordProper(ID) + " [#8c9cff]|[] " + ManaSurgeZone.getKeywordProper(ManaSurgeZone.POSITIVE_MOD), ManaSurgeZone.getKeywordDescription(ID)));
        return tooltips;
    }

    @Override
    public String modifyDescription(String rawDescription, AbstractCard card) {
        return ManaSurgeZone.getKeywordProper(ID) + LocalizedStrings.PERIOD + " NL " + rawDescription;
    }

    @Override
    public void onRender(AbstractCard card, SpriteBatch sb) {
        ExtraIcons.icon(ICON).render(card);
    }

    @Override
    public AbstractCardModifier makeCopy() {
        return new CripplingModifier(isPermanent);
    }
}