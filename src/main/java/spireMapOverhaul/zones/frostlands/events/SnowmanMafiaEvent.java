package spireMapOverhaul.zones.frostlands.events;

import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.AbstractImageEvent;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.MonsterGroup;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import spireMapOverhaul.SpireAnniversary6Mod;
import spireMapOverhaul.zones.frostlands.monsters.Cole;
import spireMapOverhaul.zones.frostlands.monsters.Hypothema;
import spireMapOverhaul.zones.frostlands.monsters.Spruce;
import spireMapOverhaul.zones.frostlands.relics.Contraption;
import spireMapOverhaul.zones.frostlands.relics.Frostcoal;
import spireMapOverhaul.zones.frostlands.relics.OldHat;
import spireMapOverhaul.zones.frostlands.relics.SpruceCharm;

public class SnowmanMafiaEvent extends AbstractImageEvent {
    public static final String ID = SpireAnniversary6Mod.makeID("SnowmanMafiaEvent");
    private static final EventStrings eventStrings = CardCrawlGame.languagePack.getEventString(ID);
    public static final String NAME = eventStrings.NAME;
    public static final String[] DESCRIPTIONS = eventStrings.DESCRIPTIONS;
    public static final String[] OPTIONS = eventStrings.OPTIONS;
    private CurScreen screen = CurScreen.INTRO;
    public static final String IMG = SpireAnniversary6Mod.makeImagePath("events/Frostlands/SnowmanMafia.png");
    public static boolean usedContraption;
    public AbstractRelic oldHat, frostcoal, spruceCharm;


    private enum CurScreen {
        INTRO, ACCEPT, DENY, POST_COMBAT, RECOVER, LEAVE;
    }

    public SnowmanMafiaEvent() {
        super(NAME, DESCRIPTIONS[0], IMG);
        imageEventText.setDialogOption(OPTIONS[0]);
        imageEventText.updateDialogOption(1, OPTIONS[1]);
        usedContraption = false;
        oldHat = new OldHat();
        frostcoal = new Frostcoal();
        spruceCharm = new SpruceCharm();
    }

    protected void buttonEffect(int buttonPressed) {
        switch (screen) {
            case INTRO:
                switch (buttonPressed) {
                    case 0:
                        imageEventText.clearRemainingOptions();
                        imageEventText.updateBodyText(DESCRIPTIONS[1]);
                        imageEventText.updateDialogOption(0, OPTIONS[2]);
                        AbstractRelic abstractRelic = new Contraption();
                        AbstractDungeon.getCurrRoom().spawnRelicAndObtain(drawX, drawY, abstractRelic);
                        screen = CurScreen.ACCEPT;
                        break;
                    case 1:
                        imageEventText.clearRemainingOptions();
                        imageEventText.updateBodyText(DESCRIPTIONS[2]);
                        imageEventText.updateDialogOption(0, OPTIONS[3]);
                        screen = CurScreen.DENY;
                        break;
                }
                return;
            case ACCEPT:
                switch (buttonPressed) {
                    case 0:
                        screen = CurScreen.POST_COMBAT;
                        imageEventText.clearRemainingOptions();
                        imageEventText.updateBodyText(DESCRIPTIONS[3]);
                        (AbstractDungeon.getCurrRoom()).monsters = new MonsterGroup(
                                new AbstractMonster[] {
                                        new Cole(-450.0F, 0.0F),
                                        new Spruce(-150.0F, 0.0F),
                                        new Hypothema(150.0F, 0.0F)
                                });
                        (AbstractDungeon.getCurrRoom()).rewards.clear();
                        (AbstractDungeon.getCurrRoom()).rewardAllowed = false;
                        enterCombatFromImage();
                        AbstractDungeon.lastCombatMetricKey = "Snowman Mafia";
                        break;
                }
                imageEventText.clearRemainingOptions();
                return;
            case POST_COMBAT:
                switch (buttonPressed) {
                    case 0:
                        AbstractDungeon.getCurrRoom().spawnRelicAndObtain(drawX, drawY, oldHat.makeCopy());
                        break;
                    case 1:
                        AbstractDungeon.getCurrRoom().spawnRelicAndObtain(drawX, drawY, frostcoal.makeCopy());
                        break;
                    case 2:
                        AbstractDungeon.getCurrRoom().spawnRelicAndObtain(drawX, drawY, spruceCharm.makeCopy());
                        break;
                }
                imageEventText.clearRemainingOptions();
                imageEventText.updateBodyText(DESCRIPTIONS[5]);
                imageEventText.updateDialogOption(0, OPTIONS[4]);
                screen = CurScreen.RECOVER;
                return;
            case DENY:
                switch (buttonPressed) {
                    case 0:
                        openMap();
                        break;
                }
                return;
            case RECOVER:
                switch (buttonPressed) {
                    case 0:
                        AbstractDungeon.player.heal(10);
                        imageEventText.clearRemainingOptions();
                        imageEventText.updateDialogOption(0, OPTIONS[8]);
                        break;
                }
                screen = CurScreen.LEAVE;
                return;
            case LEAVE:
                openMap();
                return;
        }
        openMap();
    }

    public void reopen() {
        AbstractDungeon.resetPlayer();
        AbstractDungeon.player.drawX = Settings.WIDTH * 0.25F;
        AbstractDungeon.player.preBattlePrep();
        enterImageFromCombat();
        imageEventText.clearRemainingOptions();
        if (this.screen != CurScreen.RECOVER) {
            screen = CurScreen.POST_COMBAT;
            imageEventText.optionList.clear();
            imageEventText.updateBodyText(DESCRIPTIONS[3]);
            imageEventText.setDialogOption(OPTIONS[5], oldHat.makeCopy());
            imageEventText.setDialogOption(OPTIONS[6], frostcoal.makeCopy());
            imageEventText.setDialogOption(OPTIONS[7], spruceCharm.makeCopy());
        }
        else
        {
            imageEventText.clearRemainingOptions();
            imageEventText.updateBodyText(DESCRIPTIONS[4]);
            imageEventText.updateDialogOption(0, OPTIONS[4]);
        }
    }

    public void usedContraption()
    {
        screen = CurScreen.RECOVER;
        imageEventText.clearRemainingOptions();
        imageEventText.updateBodyText(DESCRIPTIONS[4]);
        imageEventText.updateDialogOption(0, OPTIONS[4]);
    }
}
