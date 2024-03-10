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
    private static final String[] ZONE_OPTIONS = {"0-1", "1-2", "2-3", "3-4", "4-5"};

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
    public static class HitboxFields {
        public static final SpireField<Hitbox> enableHitbox = new SpireField<>(() -> null);
        public static final SpireField<Hitbox> zoneLeftHb = new SpireField<>(() -> null);
        public static final SpireField<Hitbox> zoneRightHb = new SpireField<>(() -> null);
    }

    @SpirePatch(clz = CharacterSelectScreen.class, method = "initialize")
    public static class InitializeHitboxPatch {
        @SpirePostfixPatch
        public static void initializeHitboxes(CharacterSelectScreen __instance) {
            float textWidth = getTextWidth();
            float checkBoxX = getCheckboxX();
            float amountHitboxY = 40.0F * Settings.scale; // Adjust the Y position for the Amount Option
            float enableHitboxY = amountHitboxY + 60.0F * Settings.scale; // Adjust the Y position for the Enable Option

            // Amount Option hitboxes
            float zoneTextWidth = FontHelper.getSmartWidth(FontHelper.cardTitleFont, TEXT[2], 9999.0F, 0.0F);
            float zoneHitboxX = checkBoxX + 30.0F * Settings.scale + textWidth / 2.0F + 16.0F;

            Hitbox zoneLeftHb = new Hitbox(40.0F * Settings.scale, 40.0F * Settings.scale);
            zoneLeftHb.move(zoneHitboxX - 60.0F * Settings.scale, amountHitboxY);
            HitboxFields.zoneLeftHb.set(__instance, zoneLeftHb);

            Hitbox zoneRightHb = new Hitbox(40.0F * Settings.scale, 40.0F * Settings.scale);
            zoneRightHb.move(zoneHitboxX + 60.0F * Settings.scale, amountHitboxY);
            HitboxFields.zoneRightHb.set(__instance, zoneRightHb);

            // Enable Option hitbox
            Hitbox enableHitbox = new Hitbox(textWidth + 70.0F * Settings.scale, 35.0F * Settings.scale);
            enableHitbox.move(checkBoxX + 30.0F * Settings.scale / 2.0F + textWidth / 2.0F + 16.0F, enableHitboxY);
            HitboxFields.enableHitbox.set(__instance, enableHitbox);
        }
    }

    @SpirePatch(clz = CharacterSelectScreen.class, method = "renderAscensionMode")
    public static class RenderPatch {
        @SpirePostfixPatch
        public static void render(CharacterSelectScreen __instance, SpriteBatch sb, boolean ___anySelected) {
            if (___anySelected) {
                // Render zone count selection
                Hitbox zoneLeftHb = HitboxFields.zoneLeftHb.get(__instance);
                Hitbox zoneRightHb = HitboxFields.zoneRightHb.get(__instance);

                FontHelper.renderFontCentered(sb, FontHelper.cardTitleFont, TEXT[2], zoneLeftHb.cX + 60.0F * Settings.scale, zoneLeftHb.cY + 25.0F * Settings.scale, Settings.GOLD_COLOR);
                FontHelper.renderFontCentered(sb, FontHelper.cardTitleFont, ZONE_OPTIONS[SpireAnniversary6Mod.getZoneCountIndex()], zoneLeftHb.cX + 60.0F * Settings.scale, zoneLeftHb.cY - 10.0F * Settings.scale, Settings.BLUE_TEXT_COLOR);

                if (!zoneLeftHb.hovered && !Settings.isControllerMode) {
                    sb.setColor(Color.LIGHT_GRAY);
                } else {
                    sb.setColor(Color.WHITE);
                }
                sb.draw(ImageMaster.CF_LEFT_ARROW, zoneLeftHb.cX - 24.0F, zoneLeftHb.cY - 24.0F, 24.0F, 24.0F, 48.0F, 48.0F, Settings.scale, Settings.scale, 0.0F, 0, 0, 48, 48, false, false);

                if (!zoneRightHb.hovered && !Settings.isControllerMode) {
                    sb.setColor(Color.LIGHT_GRAY);
                } else {
                    sb.setColor(Color.WHITE);
                }
                sb.draw(ImageMaster.CF_RIGHT_ARROW, zoneRightHb.cX - 24.0F, zoneRightHb.cY - 24.0F, 24.0F, 24.0F, 48.0F, 48.0F, Settings.scale, Settings.scale, 0.0F, 0, 0, 48, 48, false, false);

                zoneLeftHb.render(sb);
                zoneRightHb.render(sb);

                Hitbox enableHb = HitboxFields.enableHitbox.get(__instance);
                float checkBoxX = getCheckboxX();

                // Render enable toggle
                sb.draw(ImageMaster.OPTION_TOGGLE, checkBoxX, enableHb.cY - 16.0F, 16.0F, 16.0F, 32.0F, 32.0F, Settings.scale, Settings.scale, 0.0F, 0, 0, 32, 32, false, false);
                FontHelper.renderFontCentered(sb, FontHelper.cardTitleFont, TEXT[0], checkBoxX + 30.0F * Settings.scale + getTextWidth() / 2.0F + 16.0F, enableHb.cY, enableHb.hovered ? new Color(-2016482305) : new Color(1097458175));

                if (enableHb.hovered) {
                    TipHelper.renderGenericTip((float) InputHelper.mX - 140.0F * Settings.scale, (float) InputHelper.mY + 340.0F * Settings.scale, TEXT[0], TEXT[1]);
                }

                if (SpireAnniversary6Mod.getActiveConfig()) {
                    sb.setColor(Color.WHITE);
                    sb.draw(ImageMaster.OPTION_TOGGLE_ON, checkBoxX, enableHb.cY - 16.0F, 16.0F, 16.0F, 32.0F, 32.0F, Settings.scale, Settings.scale, 0.0F, 0, 0, 32, 32, false, false);
                }

                enableHb.render(sb);
            }
        }
    }

    @SpirePatch(clz = CharacterSelectScreen.class, method = "updateAscensionToggle")
    public static class UpdatePatch {
        @SpirePostfixPatch
        public static void update(CharacterSelectScreen __instance, boolean ___anySelected) {
            if (!___anySelected) {
                return;
            }

            Hitbox enableHb = HitboxFields.enableHitbox.get(__instance);
            enableHb.update();

            if (InputHelper.justClickedLeft) {
                if (enableHb.hovered) {
                    enableHb.clickStarted = true;
                }
            }

            if (enableHb.clicked || CInputActionSet.proceed.isJustPressed()) {
                enableHb.clicked = false;
                SpireAnniversary6Mod.setActiveConfig(!SpireAnniversary6Mod.getActiveConfig());
            }

            Hitbox zoneLeftHb = HitboxFields.zoneLeftHb.get(__instance);
            Hitbox zoneRightHb = HitboxFields.zoneRightHb.get(__instance);
            zoneLeftHb.update();
            zoneRightHb.update();

            if (InputHelper.justClickedLeft) {
                if (zoneLeftHb.hovered) {
                    zoneLeftHb.clickStarted = true;
                } else if (zoneRightHb.hovered) {
                    zoneRightHb.clickStarted = true;
                }
            }

            if (zoneLeftHb.clicked || CInputActionSet.pageLeftViewDeck.isJustPressed()) {
                zoneLeftHb.clicked = false;
                SpireAnniversary6Mod.decrementZoneCountIndex();
            }

            if (zoneRightHb.clicked || CInputActionSet.pageRightViewExhaust.isJustPressed()) {
                zoneRightHb.clicked = false;
                SpireAnniversary6Mod.incrementZoneCountIndex();
            }
        }
    }
}