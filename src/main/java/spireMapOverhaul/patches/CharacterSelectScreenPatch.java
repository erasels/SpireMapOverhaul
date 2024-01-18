package spireMapOverhaul.patches;

import basemod.ReflectionHacks;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireField;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.TipHelper;
import com.megacrit.cardcrawl.helpers.controller.CInputActionSet;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import com.megacrit.cardcrawl.screens.charSelect.CharacterSelectScreen;
import com.megacrit.cardcrawl.ui.panels.SeedPanel;
import spireMapOverhaul.SpireAnniversary6Mod;

public class CharacterSelectScreenPatch {
    private static final String[] TEXT = CardCrawlGame.languagePack.getUIString(SpireAnniversary6Mod.makeID("CharacterSelectScreen")).TEXT;

    private static float getSpacing() {
        float ascensionLevelTextWidth = ReflectionHacks.getPrivateStatic(CharacterSelectScreen.class, "ASC_RIGHT_W");
        return 255.0F * Settings.scale + ascensionLevelTextWidth;
    }

    private static float getCheckboxX() {
        return (float) Settings.WIDTH / 2.0F + getSpacing() + 16.0F;
    }

    private static float getTextWidth() {
        return FontHelper.getSmartWidth(FontHelper.cardTitleFont, TEXT[0], 9999.0F, 0.0F);
    }

    @SpirePatch(clz = CharacterSelectScreen.class, method = SpirePatch.CLASS)
    public static class HitboxField {
        public static final SpireField<Hitbox> hitbox = new SpireField<>(() -> null);
    }

    @SpirePatch(clz = CharacterSelectScreen.class, method = "initialize")
    public static class InitializeHitboxPatch {
        @SpirePostfixPatch
        public static void initializeHitbox(CharacterSelectScreen __instance) {
            float textWidth = FontHelper.getSmartWidth(FontHelper.cardTitleFont, TEXT[0], 9999.0F, 0.0F);
            float checkBoxX = getCheckboxX();
            float hitboxX = checkBoxX + 30.0F * Settings.scale / 2.0F + textWidth / 2.0F + 16.0F;
            Hitbox hitbox = new Hitbox(textWidth + 70.0F * Settings.scale, 35.0F * Settings.scale);
            hitbox.move(hitboxX, 35.0F * Settings.scale);
            HitboxField.hitbox.set(__instance, hitbox);
        }
    }

    @SpirePatch(clz = CharacterSelectScreen.class, method = "renderAscensionMode")
    public static class RenderCheckboxPatch {
        @SpirePostfixPatch
        public static void renderCheckbox(CharacterSelectScreen __instance, SpriteBatch sb, boolean ___anySelected) {
            if (___anySelected) {
                Hitbox hb = HitboxField.hitbox.get(__instance);
                float checkBoxX = getCheckboxX();
                sb.draw(ImageMaster.OPTION_TOGGLE, checkBoxX, hb.cY - 16.0F, 16.0F, 16.0F, 32.0F, 32.0F, Settings.scale, Settings.scale, 0.0F, 0, 0, 32, 32, false, false);

                FontHelper.renderFontCentered(sb, FontHelper.cardTitleFont, TEXT[0], checkBoxX + 30.0F * Settings.scale + getTextWidth() / 2.0F + 16.0F, hb.cY, hb.hovered ? new Color(-2016482305) : new Color(1097458175));
                if (hb.hovered) {
                    TipHelper.renderGenericTip((float) InputHelper.mX - 140.0F * Settings.scale, (float) InputHelper.mY + 340.0F * Settings.scale, TEXT[0], TEXT[1]);
                }

                if (SpireAnniversary6Mod.getActiveConfig()) {
                    sb.setColor(Color.WHITE);
                    sb.draw(ImageMaster.OPTION_TOGGLE_ON, checkBoxX, hb.cY - 16.0F, 16.0F, 16.0F, 32.0F, 32.0F, Settings.scale, Settings.scale, 0.0F, 0, 0, 32, 32, false, false);
                }

                hb.render(sb);
            }
        }
    }

    @SpirePatch(clz = CharacterSelectScreen.class, method = "updateAscensionToggle")
    public static class UpdateCheckboxPatch {
        @SpirePostfixPatch
        public static void updateCheckbox(CharacterSelectScreen __instance, SeedPanel ___seedPanel, boolean ___anySelected) {
            if (!___anySelected) {
                return;
            }

            Hitbox hb = HitboxField.hitbox.get(__instance);
            hb.update();

            if (InputHelper.justClickedLeft) {
                if (hb.hovered) {
                    hb.clickStarted = true;
                }
            }

            if (hb.clicked || CInputActionSet.proceed.isJustPressed()) {
                hb.clicked = false;
                SpireAnniversary6Mod.setActiveConfig(!SpireAnniversary6Mod.getActiveConfig());
            }
        }
    }
}
