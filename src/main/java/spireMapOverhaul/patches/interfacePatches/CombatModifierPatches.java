package spireMapOverhaul.patches.interfacePatches;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.evacipated.cardcrawl.modthespire.patcher.PatchingException;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.OverlayMenu;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.monsters.MonsterGroup;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.ui.panels.DrawPilePanel;
import javassist.CannotCompileException;
import javassist.CtBehavior;
import spireMapOverhaul.SpireAnniversary6Mod;
import spireMapOverhaul.ui.GenericButton;
import spireMapOverhaul.util.TexLoader;
import spireMapOverhaul.util.Wiz;
import spireMapOverhaul.zoneInterfaces.CombatModifyingZone;

public class CombatModifierPatches {
    private static GenericButton combatBtn = new GenericButton(TexLoader.getTexture(SpireAnniversary6Mod.makeUIPath("CombatModifierButton.png")),
            16 * Settings.scale,
            Settings.HEIGHT - 356 * Settings.scale);

    private static UIStrings uiStrings;
    public static boolean hideButton = true;

    @SpirePatch2(clz =AbstractPlayer.class, method = "preBattlePrep")
    public static class BeforePreCombat {
        @SpirePrefixPatch
        public static void patch() {
            Wiz.forCurZone(CombatModifyingZone.class, z -> {
                z.beforePreBattlePrep();
            });
        }
    }

    @SpirePatch2(clz =AbstractPlayer.class, method = "applyPreCombatLogic")
    public static class PreCombat {
        @SpirePostfixPatch
        public static void patch() {
            hideButton = true;
            Wiz.forCurZone(CombatModifyingZone.class, z -> {
                String txt = z.getCombatText();
                if(txt != null) {
                    hideButton = false;
                    if(uiStrings == null)
                        uiStrings = CardCrawlGame.languagePack.getUIString(SpireAnniversary6Mod.makeID("CombatExplainButton"));
                    combatBtn = combatBtn.setHoverTip(uiStrings.TEXT[0], txt);
                }
                z.atPreBattle();
            });
        }
    }

    @SpirePatch2(clz =AbstractPlayer.class, method = "applyStartOfCombatLogic")
    public static class AtBattleStart {
        @SpirePostfixPatch
        public static void patch() {
            Wiz.forCurZone(CombatModifyingZone.class, CombatModifyingZone::atBattleStart);
        }
    }

    @SpirePatch2(clz =AbstractPlayer.class, method = "applyStartOfCombatPreDrawLogic")
    public static class AtBattleStartPreDraw {
        @SpirePostfixPatch
        public static void patch() {
            Wiz.forCurZone(CombatModifyingZone.class, CombatModifyingZone::atBattleStartPreDraw);
        }
    }

    @SpirePatch2(clz =AbstractPlayer.class, method = "applyStartOfTurnRelics")
    public static class AtTurnStart {
        @SpirePostfixPatch
        public static void patch() {
            Wiz.forCurZone(CombatModifyingZone.class, CombatModifyingZone::atTurnStart);
        }
    }

    @SpirePatch2(clz =AbstractPlayer.class, method = "applyStartOfTurnPostDrawRelics")
    public static class AtTurnStartPostDraw {
        @SpirePostfixPatch
        public static void patch() {
            Wiz.forCurZone(CombatModifyingZone.class, CombatModifyingZone::atTurnStartPostDraw);
        }
    }

    @SpirePatch2(clz = AbstractRoom.class, method = "applyEndOfTurnRelics")
    public static class AtTurnEnd {
        @SpirePostfixPatch
        public static void patch() {
            Wiz.forCurZone(CombatModifyingZone.class, CombatModifyingZone::atTurnEnd);
        }
    }

    @SpirePatch2(clz = MonsterGroup.class, method = "applyEndOfTurnPowers")
    public static class AtEndOfRound {
        @SpirePostfixPatch
        public static void patch() {
            Wiz.forCurZone(CombatModifyingZone.class, CombatModifyingZone::atRoundEnd);
        }
    }

    @SpirePatch2(clz = AbstractRoom.class, method = "endBattle")
    public static class OnVictory {
        @SpireInsertPatch(locator = Locator.class)
        public static void patch() {
            Wiz.forCurZone(CombatModifyingZone.class, z -> {
                z.onVictory();
                hideButton = true;
            });
        }

        private static class Locator extends SpireInsertLocator {
            public int[] Locate(CtBehavior ctMethodToPatch) throws CannotCompileException, PatchingException {
                Matcher finalMatcher = new Matcher.FieldAccessMatcher(AbstractRoom.class, "endBattleTimer");
                return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
            }
        }
    }

    @SpirePatch2(clz = DrawPilePanel.class, method = "render")
    public static class RenderCombatBtn {
        @SpirePostfixPatch
        public static void patch(SpriteBatch sb) {
            if(!hideButton)
                combatBtn.render(sb);
        }
    }

    @SpirePatch2(clz = OverlayMenu.class, method = "update")
    public static class UpdateCombatBtn {
        @SpirePostfixPatch
        public static void patch() {
            if(!hideButton)
                combatBtn.update();
        }
    }
}
