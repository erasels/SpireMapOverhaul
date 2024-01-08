package spireMapOverhaul.zones.voidseed.powers.cardmods;

import basemod.abstracts.AbstractCardModifier;
import basemod.helpers.CardModifierManager;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch2;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatches2;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import spireMapOverhaul.zones.voidseed.VoidSeedShaderManager;

import static spireMapOverhaul.SpireAnniversary6Mod.makeID;

public class CorruptedModifier extends AbstractCardModifier {

    public static String ID = makeID("CorruptedModifier");

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
        return CardCrawlGame.languagePack.getUIString(makeID("CorruptedModifier")).TEXT[0] + cardName;
    }

    public void onPreRender(AbstractCard card, SpriteBatch sb) {
        VoidSeedShaderManager.StartFbo(sb);

    }

    @Override
    public void onRender(AbstractCard card, SpriteBatch sb) {
        VoidSeedShaderManager.StopFbo(sb, 0.0f, 1.0f);
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
