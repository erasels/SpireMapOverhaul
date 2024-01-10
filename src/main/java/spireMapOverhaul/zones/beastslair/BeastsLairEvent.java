package spireMapOverhaul.zones.beastslair;

import basemod.AutoAdd;
import basemod.abstracts.events.PhasedEvent;
import basemod.abstracts.events.phases.CombatPhase;
import basemod.abstracts.events.phases.TextPhase;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.powers.AngerPower;
import com.megacrit.cardcrawl.powers.ArtifactPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
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
                    .addRewards(true, (room)->room.addRelicToRewards(AbstractDungeon.returnRandomRelicTier()))
                    .setNextKey("Phase 2"));

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
}
