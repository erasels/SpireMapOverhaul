package spireMapOverhaul.zones.beastslair;

import basemod.ReflectionHacks;
import basemod.abstracts.events.PhasedEvent;
import basemod.abstracts.events.phases.CombatPhase;
import basemod.abstracts.events.phases.TextPhase;
import com.evacipated.cardcrawl.mod.stslib.actions.common.AllEnemyApplyPowerAction;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.SpawnMonsterAction;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.exordium.*;
import com.megacrit.cardcrawl.powers.ArtifactPower;
import javassist.CtBehavior;
import spireMapOverhaul.util.Wiz;
import spireMapOverhaul.zones.beastslair.powers.FuryPower;

import java.util.ArrayList;
import java.util.Arrays;

import static spireMapOverhaul.SpireAnniversary6Mod.makeID;
public class BeastsLairEvent extends PhasedEvent {
        public static final String ID = makeID("BeastsLairEvent");
        private static final EventStrings eventStrings = CardCrawlGame.languagePack.getEventString(ID);
        private static final String[] DESCRIPTIONS = eventStrings.DESCRIPTIONS;
        private static final String[] OPTIONS = eventStrings.OPTIONS;
        private static final String title = eventStrings.NAME;

        public BeastsLairEvent() {
            this(null);
        }

        public BeastsLairEvent(String bossID) {
            super(ID, title, "img/events/eventpicture.png");

            registerPhase("Start", new TextPhase(DESCRIPTIONS[0]).addOption(OPTIONS[0], (i)->transitionKey("Phase 2")));

            registerPhase("Phase 2", new CombatPhase(bossID)
                    .addRewards(true, (room) -> room.addRelicToRewards(AbstractDungeon.returnRandomRelicTier())));



            transitionKey("Start");
        }

        public static boolean endsWithRewardsUI() {
            return true;
        }

    @Override
    public void enterCombat() {
        super.enterCombat();
        Wiz.atb(new AllEnemyApplyPowerAction(null, 2, (m) -> new ArtifactPower(m, 2)));
        Wiz.atb(new AllEnemyApplyPowerAction(null, 10, (m) -> new FuryPower(m, 10)));

        Wiz.forAllMonstersLiving((m)->{
            m.maxHealth = (int) (m.maxHealth * 1.25f);
            m.currentHealth = (int) (m.currentHealth * 1.25f);
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
                if (!isSlimebossSpawn(m)) {
                    m.maxHealth = (int) (m.maxHealth * 1.25f);
                    m.currentHealth = (int) (m.currentHealth * 1.25f);
                }

                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(m, null, new ArtifactPower(m, 2), 2));
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(m, null, new FuryPower(m, 10), 10));
            }
        }

        private static boolean isSlimebossSpawn(AbstractMonster m) {
            return (AbstractDungeon.lastCombatMetricKey.equals("Slime Boss") && slimes.contains(m.id));
        }

        private static final ArrayList<String> slimes = new ArrayList<>(Arrays.asList(SpikeSlime_L.ID, SpikeSlime_M.ID,
                SpikeSlime_S.ID, AcidSlime_L.ID, AcidSlime_M.ID, AcidSlime_S.ID));


        private static class Locator extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
                Matcher finalMatcher = new Matcher.MethodCallMatcher(AbstractMonster.class, "showHealthBar");
                return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
            }
        }
    }

}
