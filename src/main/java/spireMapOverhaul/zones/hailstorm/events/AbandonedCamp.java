package spireMapOverhaul.zones.hailstorm.events;

import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.city.Colosseum;
import com.megacrit.cardcrawl.helpers.MonsterHelper;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.relics.AbstractRelic.RelicTier;
import com.megacrit.cardcrawl.vfx.RainingGoldEffect;
import spireMapOverhaul.SpireAnniversary6Mod;
import spireMapOverhaul.zones.thieveshideout.ThievesHideoutZone;

// We extend the Colosseum event because ProceedButton.java specifically checks if an event is an instance of this type
// (or a few other types) in the logic for what happens when you click proceed. This is easier than a patch.
public class AbandonedCamp extends Colosseum {
    public static final String ID = SpireAnniversary6Mod.makeID(AbandonedCamp.class.getSimpleName());
    private static final EventStrings eventStrings = CardCrawlGame.languagePack.getEventString(ID);
    private static final String NAME = eventStrings.NAME;
    private static final String[] DESCRIPTIONS = eventStrings.DESCRIPTIONS;
    private static final String[] OPTIONS = eventStrings.OPTIONS;
    public static final String IMG = SpireAnniversary6Mod.makeImagePath("events/Invasion/ThiefKing.png");

    private static final int FLINT_HP_LOSS = 1/24;
    private static final int A_15_FLINT_HP_LOSS = 1/16;
    private static final int GOLD = 90;
    private static final int A_15_GOLD = 60;
    private static final int HEALING = 1/4;
    private static final int A_15_HEALING = 1/6;

    private CurScreen screen;

    public AbandonedCamp() {
        super();
        this.imageEventText.clear();
        this.roomEventText.clear();
        this.title = NAME;
        this.body = DESCRIPTIONS[0];
        this.imageEventText.loadImage(IMG);
        type = EventType.IMAGE;
        this.noCardsInRewards = false;

        this.screen = CurScreen.INTRO;

        if (AbstractDungeon.ascensionLevel >= 15) {//Asc 15

            //First impactful choice
            if (AbstractDungeon.miscRng.randomBoolean()) {
                this.imageEventText.setDialogOption(OPTIONS[0] + 20 + OPTIONS[1]);
            } else {
                this.imageEventText.setDialogOption(OPTIONS[2] + FLINT_HP_LOSS + OPTIONS[3]);
            }

            //Second light choice
            if (AbstractDungeon.miscRng.randomBoolean()) {
                this.imageEventText.setDialogOption(OPTIONS[4] + GOLD + OPTIONS[5]);
            } else {
                this.imageEventText.setDialogOption(OPTIONS[6] + HEALING + OPTIONS[7]);
            }

        } else {//Non-Asc 15

            //First impactful choice
            if (AbstractDungeon.miscRng.randomBoolean()) {
                this.imageEventText.setDialogOption(OPTIONS[0] + 20 + OPTIONS[1]);
            } else {
                this.imageEventText.setDialogOption(OPTIONS[2] + A_15_FLINT_HP_LOSS + OPTIONS[3]);
            }

            //Second light choice
            if (AbstractDungeon.miscRng.randomBoolean()) {
                this.imageEventText.setDialogOption(OPTIONS[4] + A_15_GOLD + OPTIONS[5]);
            } else {
                this.imageEventText.setDialogOption(OPTIONS[6] + A_15_HEALING + OPTIONS[7]);
            }

        }
    }

    @Override
    protected void buttonEffect(int buttonPressed) {
        switch(this.screen) {
            case INTRO:
                this.imageEventText.updateBodyText(DESCRIPTIONS[1]);
                this.imageEventText.updateDialogOption(0, OPTIONS[1]);
                this.screen = CurScreen.FIGHT;
                break;
            case FIGHT:
                this.screen = CurScreen.POST_COMBAT;
                logMetric(ID,"Fight");
                AbstractDungeon.getCurrRoom().monsters = MonsterHelper.getEncounter(ThievesHideoutZone.BANDIT_LIEUTENANT_AND_TASKMASTER);
                AbstractDungeon.getCurrRoom().rewards.clear();
                AbstractDungeon.getCurrRoom().rewardAllowed = false;
                this.enterCombatFromImage();
                AbstractDungeon.lastCombatMetricKey = ThievesHideoutZone.BANDIT_LIEUTENANT_AND_TASKMASTER;
                break;
            case POST_COMBAT:
                AbstractDungeon.getCurrRoom().rewardAllowed = true;
                switch(buttonPressed) {
                    case 1:
                        logMetric(ID,"Greed");
                        AbstractDungeon.getCurrRoom().monsters = MonsterHelper.getEncounter(ThievesHideoutZone.THIEF_KING);
                        AbstractDungeon.getCurrRoom().rewards.clear();
                        AbstractDungeon.getCurrRoom().addRelicToRewards(RelicTier.RARE);
                        AbstractDungeon.getCurrRoom().addRelicToRewards(RelicTier.UNCOMMON);
                        AbstractDungeon.getCurrRoom().addGoldToRewards(GOLD);
                        AbstractDungeon.getCurrRoom().eliteTrigger = true;
                        this.enterCombatFromImage();
                        AbstractDungeon.lastCombatMetricKey = ThievesHideoutZone.THIEF_KING;
                        break;
                    default:
                        this.screen = CurScreen.LEAVE;
                        AbstractDungeon.effectList.add(new RainingGoldEffect(GOLD));
                        AbstractDungeon.player.gainGold(GOLD);
                        logMetricGainGold(ID, "Prudence", GOLD);
                        this.imageEventText.updateBodyText(DESCRIPTIONS[3]);
                        this.imageEventText.clearAllDialogs();
                        this.imageEventText.setDialogOption(OPTIONS[4]);
                        break;
                }
                break;
            default:
                this.openMap();
                break;
        }
    }

    @Override
    public void reopen() {
        if (this.screen != CurScreen.LEAVE) {
            AbstractDungeon.resetPlayer();
            AbstractDungeon.player.drawX = (float) Settings.WIDTH * 0.25F;
            AbstractDungeon.player.preBattlePrep();
            this.enterImageFromCombat();
            this.imageEventText.updateBodyText(DESCRIPTIONS[2]);
            this.imageEventText.updateDialogOption(0, OPTIONS[2].replace("{0}", GOLD + ""));
            this.imageEventText.setDialogOption(OPTIONS[3]);
        }
    }

    private enum CurScreen {
        INTRO,
        FIGHT,
        LEAVE,
        POST_COMBAT
    }
}
