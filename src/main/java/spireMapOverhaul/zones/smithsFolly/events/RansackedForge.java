package spireMapOverhaul.zones.smithsFolly.events;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.red.SearingBlow;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.AbstractImageEvent;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.potions.BlessingOfTheForge;
import com.megacrit.cardcrawl.potions.PotionSlot;
import com.megacrit.cardcrawl.rewards.RewardItem;
import com.megacrit.cardcrawl.vfx.UpgradeShineEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndObtainEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardBrieflyEffect;

import static spireMapOverhaul.SpireAnniversary6Mod.makeID;
import static spireMapOverhaul.SpireAnniversary6Mod.makeImagePath;

public class RansackedForge extends AbstractImageEvent {
    public static final String ID = makeID("RansackedForge");

    private static final EventStrings eventStrings = CardCrawlGame.languagePack.getEventString(ID);

    public static final String NAME = eventStrings.NAME;
    public static final String[] DESCRIPTIONS = eventStrings.DESCRIPTIONS;
    public static final String[] OPTIONS = eventStrings.OPTIONS;

    private CurScreen screen = CurScreen.INTRO;
    boolean hasStrike = AbstractDungeon.player.masterDeck.group.stream().anyMatch(card -> card.tags.contains(AbstractCard.CardTags.STARTER_STRIKE));

    public RansackedForge() {
        super(NAME, DESCRIPTIONS[0], makeImagePath("events/SmithsFolly/RansackedForge.png"));
        this.screen = CurScreen.INTRO;

        if (AbstractDungeon.player.masterDeck.findCardById(SearingBlow.ID) != null) {
            this.imageEventText.setDialogOption(OPTIONS[0]);
        } else if (AbstractDungeon.actNum == 3 && hasStrike) {
            SearingBlow upgradedSearingBlow = new SearingBlow();
            for (int i = 0; i < 4; i++) {
                upgradedSearingBlow.upgrade();
            }
            this.imageEventText.setDialogOption(OPTIONS[1], upgradedSearingBlow);
        } else {
            SearingBlow upgradedSearingBlow = new SearingBlow();
            for (int i = 0; i < 4; i++) {
                upgradedSearingBlow.upgrade();
            }
            this.imageEventText.setDialogOption(OPTIONS[2], upgradedSearingBlow);
        }

        this.imageEventText.setDialogOption(OPTIONS[3]);
        this.imageEventText.setDialogOption(OPTIONS[4]);
    }


    protected void buttonEffect(int buttonPressed) {
        switch (screen) {
            case INTRO:
                switch (buttonPressed) {
                    case 0:
                        handleSearingBlowOption();
                        break;
                    case 1: // Gain potion slot and one Blessing of the Forge potion
                        // Increase potion slots
                        AbstractDungeon.player.potionSlots += 1;
                        AbstractDungeon.player.potions.add(new PotionSlot(AbstractDungeon.player.potionSlots - 1));

                        // Clear existing rewards and add one Blessing of the Forge potion
                        AbstractDungeon.getCurrRoom().rewards.clear();
                        AbstractDungeon.getCurrRoom().rewards.add(new RewardItem(new BlessingOfTheForge()));

                        // Open the combat reward screen
                        AbstractDungeon.combatRewardScreen.open();
                        AbstractDungeon.getCurrRoom().rewardPopOutTimer = 0.0F;

                        AbstractDungeon.combatRewardScreen.rewards.removeIf(reward -> reward.type == RewardItem.RewardType.CARD);

                        // Update event text and options
                        this.imageEventText.updateBodyText(DESCRIPTIONS[2]);
                        this.imageEventText.updateDialogOption(0, OPTIONS[3]); // Text for closing the event
                        this.imageEventText.clearRemainingOptions();
                        this.screen = CurScreen.COMPLETE;
                        break;

                    case 2:
                        // Leave
                        this.imageEventText.updateBodyText(DESCRIPTIONS[3]);
                        break;
                }
                this.imageEventText.updateDialogOption(0, OPTIONS[5]);
                this.imageEventText.clearRemainingOptions();
                this.screen = CurScreen.COMPLETE;
                break;
            case COMPLETE:
                openMap();
                break;
        }
    }

    private enum CurScreen {
        INTRO,
        COMPLETE
    }
    private void handleSearingBlowOption() {
        if (AbstractDungeon.player.masterDeck.findCardById(SearingBlow.ID) != null) {
            // Upgrade Searing Blow four times
            upgradeSearingBlow(2);
            this.imageEventText.updateBodyText(DESCRIPTIONS[1]);
        } else if (AbstractDungeon.actNum == 3 && hasStrike) {
            // Replace all Strikes with Searing Blow+2
            replaceStrikesWithSearingBlow();
            this.imageEventText.updateBodyText(DESCRIPTIONS[4]);
        } else {
            // Obtain Searing Blow+2
            AbstractCard searingBlow = CardLibrary.getCard(SearingBlow.ID).makeCopy();
            searingBlow.upgrade();
            searingBlow.upgrade();
            AbstractDungeon.effectList.add(new ShowCardAndObtainEffect(searingBlow, (float)Settings.WIDTH / 2.0F, (float)Settings.HEIGHT / 2.0F));
            this.imageEventText.updateBodyText(DESCRIPTIONS[5]);
        }
    }

    private void upgradeSearingBlow(int times) {
        for (AbstractCard card : AbstractDungeon.player.masterDeck.group) {
            if (card.cardID.equals(SearingBlow.ID)) {
                for (int i = 0; i < times; i++) {
                    card.upgrade();
                }
                // Show visual effects for each upgrade
                AbstractDungeon.topLevelEffects.add(new ShowCardBrieflyEffect(card.makeStatEquivalentCopy()));
                AbstractDungeon.topLevelEffects.add(new UpgradeShineEffect((float)Settings.WIDTH / 2.0F, (float)Settings.HEIGHT / 2.0F));
            }
        }
    }


    private void replaceStrikesWithSearingBlow() {
        // Count the number of Strike cards
        long strikeCount = AbstractDungeon.player.masterDeck.group.stream()
                .filter(card -> card.tags.contains(AbstractCard.CardTags.STARTER_STRIKE))
                .count();

        // Remove all Strike cards
        AbstractDungeon.player.masterDeck.group.removeIf(card -> card.tags.contains(AbstractCard.CardTags.STARTER_STRIKE));

        // Add the same number of Searing Blow+2 cards
        for (int i = 0; i < strikeCount; i++) {
            AbstractCard searingBlow = CardLibrary.getCard(SearingBlow.ID).makeCopy();
            searingBlow.upgrade();
            searingBlow.upgrade();
            AbstractDungeon.effectList.add(new ShowCardAndObtainEffect(searingBlow, (float)Settings.WIDTH / 2.0F, (float)Settings.HEIGHT / 2.0F));
        }
    }


}