package spireMapOverhaul.zones.beastslair;

import basemod.AutoAdd;
import basemod.ReflectionHacks;
import basemod.abstracts.events.PhasedEvent;
import basemod.abstracts.events.phases.CombatPhase;
import basemod.abstracts.events.phases.TextPhase;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.SpawnMonsterAction;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.ArtifactPower;
import javassist.CtBehavior;
import spireMapOverhaul.actions.AllEnemyApplyPowerAction;
import spireMapOverhaul.util.Wiz;
import spireMapOverhaul.zones.beastslair.powers.FuryPower;

import static spireMapOverhaul.SpireAnniversary6Mod.makeID;
@AutoAdd.Ignore
public class BeastsLairEvent extends PhasedEvent {
        public static final String ID = makeID("BeastsLairEvent");
        //These eventStrings should be defined in a json file and loaded in your main mod file. See https://github.com/daviscook477/BaseMod/wiki/Custom-Strings
        private static final EventStrings eventStrings = CardCrawlGame.languagePack.getEventString(ID);
        private static final String[] DESCRIPTIONS = eventStrings.DESCRIPTIONS;
        private static final String[] OPTIONS = eventStrings.OPTIONS;
        private static final String title = eventStrings.NAME;

        public BeastsLairEvent(String bossID) {
            super(ID, title, "img/events/eventpicture.png");

            registerPhase("Start", new TextPhase(DESCRIPTIONS[0]).addOption(OPTIONS[0], (i)->transitionKey("Phase 2")));

            registerPhase("Phase 2", new CombatPhase(bossID)
                    .addRewards(true, (room) -> room.addRelicToRewards(AbstractDungeon.returnRandomRelicTier())));



            transitionKey("Start");
        }

    @Override
    public void enterCombat() {
        super.enterCombat();
        AbstractDungeon.actionManager.addToBottom(new AllEnemyApplyPowerAction(null, 2, (m) -> new ArtifactPower(m, 2)));
        AbstractDungeon.actionManager.addToBottom(new AllEnemyApplyPowerAction(null, 10, (m) -> new FuryPower(m, 10)));
        Wiz.forAllMonstersLiving((m)->{
            m.maxHealth = (int) (m.maxHealth * 1.75f);
            m.currentHealth = (int) (m.currentHealth * 1.75f);
        }
        );
    }

    @SpirePatch2(clz = SpawnMonsterAction.class, method = "update")
    public static class SpawnMonsterActionPatch {
        @SpireInsertPatch(locator = Locator.class)
        public static void patch(SpawnMonsterAction __instance) {
            boolean used = ReflectionHacks.getPrivate(__instance, SpawnMonsterAction.class, "used");
            if (!used && AbstractDungeon.getCurrRoom().event instanceof BeastsLairEvent) {
                AbstractMonster m = ReflectionHacks.getPrivate(__instance, SpawnMonsterAction.class, "m");
                m.maxHealth = (int) (m.maxHealth * 1.75f);
                m.currentHealth = (int) (m.currentHealth * 1.75f);
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(m, null, new ArtifactPower(m, 2), 2));
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(m, null, new FuryPower(m, 10), 10));
            }
        }

        private static class Locator extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
                Matcher finalMatcher = new Matcher.MethodCallMatcher(AbstractMonster.class, "showHealthBar");
                return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
            }
        }
    }
}
