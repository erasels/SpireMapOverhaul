package spireMapOverhaul.zones.manasurge.modifiers.common.positive;

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
import com.megacrit.cardcrawl.localization.LocalizedStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import spireMapOverhaul.SpireAnniversary6Mod;
import spireMapOverhaul.util.TexLoader;
import spireMapOverhaul.zones.manasurge.ManaSurgeZone;
import spireMapOverhaul.zones.manasurge.modifiers.AbstractManaSurgeModifier;

import java.util.ArrayList;
import java.util.List;

import static spireMapOverhaul.SpireAnniversary6Mod.makeUIPath;

public class SharpModifier extends AbstractManaSurgeModifier {
    public static final String ID = SpireAnniversary6Mod.makeID("ManaSurge:Sharp");
    private static final ModRarity MOD_RARITY = ModRarity.COMMON_MOD;
    private static final ModEffect MOD_EFFECT = ModEffect.POSITIVE_MOD;

    private static final Texture ICON = TexLoader.getTexture(makeUIPath("ManaSurge/extraIcons/PositiveEnchantmentIcon.png"));
    private static final int DAMAGE = 5;

    public SharpModifier(boolean permanent) {
        super(permanent,MOD_RARITY,MOD_EFFECT);
    }

    @Override
    public void onUse(AbstractCard card, AbstractCreature target, UseCardAction action) {
        DamageInfo info = new DamageInfo(AbstractDungeon.player, DAMAGE, DamageInfo.DamageType.THORNS);
        if (card.target == AbstractCard.CardTarget.ENEMY) {
            AbstractDungeon.actionManager.addToBottom(new DamageAction(target, info, AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
        } else {
            AbstractMonster mo = AbstractDungeon.getRandomMonster();
            AbstractDungeon.actionManager.addToBottom(new DamageAction(mo, info,AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
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
        return new SharpModifier(isPermanent);
    }
}