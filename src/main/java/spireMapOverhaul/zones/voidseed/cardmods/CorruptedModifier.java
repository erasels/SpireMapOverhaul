package spireMapOverhaul.zones.voidseed.cardmods;

import basemod.abstracts.AbstractCardModifier;
import basemod.helpers.CardModifierManager;
import basemod.helpers.TooltipInfo;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch2;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatches2;
import com.megacrit.cardcrawl.actions.common.LoseHPAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import spireMapOverhaul.zones.voidseed.VoidSeedShaderManager;

import java.util.ArrayList;
import java.util.List;

import static spireMapOverhaul.SpireAnniversary6Mod.makeID;
import static spireMapOverhaul.util.Wiz.adp;
import static spireMapOverhaul.util.Wiz.atb;

public class CorruptedModifier extends AbstractCardModifier {

    public static String ID = makeID("CorruptedModifier");
    public static int AMOUNT = 4;


    public static boolean valid(AbstractCard card) {
        return card.baseMagicNumber > 0 || card.baseBlock > 0 || card.baseDamage > 0;
    }

    @Override
    public AbstractCardModifier makeCopy() {
        return new CorruptedModifier();
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
    public float modifyBaseMagic(float magic, AbstractCard card) {
        return super.modifyBaseMagic(magic, card) * 2;
    }

    @Override
    public float modifyBaseBlock(float block, AbstractCard card) {
        return super.modifyBaseBlock(block, card) * 2;
    }

    @Override
    public float modifyBaseDamage(float damage, DamageInfo.DamageType type, AbstractCard card, AbstractMonster target) {
        return super.modifyBaseDamage(damage, type, card, target) * 2;
    }

    public String modifyName(String cardName, AbstractCard card) {
        return "?" + CardCrawlGame.languagePack.getUIString(makeID("CorruptedModifier")).TEXT[0] + cardName + "?";
    }

    public void onPreRender(AbstractCard card, SpriteBatch sb) {
        VoidSeedShaderManager.StartFbo(sb);

    }

    @Override
    public List<TooltipInfo> additionalTooltips(AbstractCard card) {
        TooltipInfo tooltip = new TooltipInfo(CardCrawlGame.languagePack.getUIString(makeID("CorruptedModifier")).TEXT[1],
                CardCrawlGame.languagePack.getUIString(makeID("CorruptedModifier")).TEXT[2] + AMOUNT + CardCrawlGame.languagePack.getUIString(makeID("CorruptedModifier")).TEXT[3]);
        return new ArrayList<TooltipInfo>() {{
            add(tooltip);
        }};
    }

    @Override
    public void onUse(AbstractCard card, AbstractCreature target, UseCardAction action) {

        atb(new LoseHPAction(adp(), adp(), AMOUNT));
        super.onUse(card, target, action);
    }

    @Override
    public void onRender(AbstractCard card, SpriteBatch sb) {
        VoidSeedShaderManager.StopFbo(sb, 0.0f, 1.0f, true);
        super.onRender(card, sb);
    }

    @SpirePatches2({@SpirePatch2(
            clz = AbstractCard.class,
            method = "render",
            paramtypez = {SpriteBatch.class, boolean.class}
    ), @SpirePatch2(
            clz = AbstractCard.class,
            method = "renderInLibrary"
    )})
    public static class CardModifierRender {


        public static void Prefix(AbstractCard __instance, SpriteBatch sb) {
            if (CardModifierManager.hasModifier(__instance, ID)) {
                CardModifierManager.getModifiers(__instance, ID).forEach(m -> {
                    if (m instanceof CorruptedModifier) {
                        ((CorruptedModifier) m).onPreRender(__instance, sb);
                    }
                });
            }


        }// 535
    }
}
