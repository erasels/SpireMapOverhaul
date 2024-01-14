package spireMapOverhaul.zones.thefog.events;

import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.AbstractImageEvent;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import spireMapOverhaul.rewards.AnyColorCardReward;
import spireMapOverhaul.zones.thefog.relics.StoneFragment;

import static spireMapOverhaul.SpireAnniversary6Mod.makeID;
import static spireMapOverhaul.SpireAnniversary6Mod.makeImagePath;
import static spireMapOverhaul.util.Wiz.adp;

@SuppressWarnings("unused")
public class StandingStones extends AbstractImageEvent {
    public static final String ID = makeID("StandingStones");

    private static final EventStrings eventStrings = CardCrawlGame.languagePack.getEventString(ID);

    public static final String NAME = eventStrings.NAME;
    public static final String[] DESCRIPTIONS = eventStrings.DESCRIPTIONS;
    public static final String[] OPTIONS = eventStrings.OPTIONS;

    private int screenNum = 0;

    private static final float HP_HEAL_PERCENT = 0.33F;
    private static final float A15_HP_HEAL_PERCENT = 0.2F;
    private static final float HP_DAMAGE_PERCENT = 0.15F;
    private static final float A15_HP_DAMAGE_PERCENT = 0.2F;

    private final int healAmt;
    private final int damageAmt;

    public StandingStones() {
        super(NAME, DESCRIPTIONS[0], makeImagePath("events/TheFog/StandingStones.png"));
        this.noCardsInRewards = true;

        if (AbstractDungeon.ascensionLevel >= 15) {
            this.healAmt = (int)(adp().maxHealth * A15_HP_HEAL_PERCENT);
            this.damageAmt = (int)(adp().maxHealth * A15_HP_DAMAGE_PERCENT);
        } else {
            this.healAmt = (int)(adp().maxHealth * HP_HEAL_PERCENT);
            this.damageAmt = (int)(adp().maxHealth * HP_DAMAGE_PERCENT);
        }

        this.imageEventText.setDialogOption(OPTIONS[0] + this.healAmt + OPTIONS[1]);
        this.imageEventText.setDialogOption(OPTIONS[2]);
        this.imageEventText.setDialogOption(OPTIONS[3] + this.damageAmt + OPTIONS[4], new StoneFragment());
    }

    protected void buttonEffect(int buttonPressed) {
        if (this.screenNum == 0) {
            switch (buttonPressed) {
                case 0:
                    this.imageEventText.updateBodyText(DESCRIPTIONS[1]);
                    AbstractDungeon.player.heal(healAmt, true);
                    this.imageEventText.updateDialogOption(0, OPTIONS[5]);
                    this.imageEventText.clearRemainingOptions();
                    this.screenNum++;
                    return;
                case 1:
                    AbstractDungeon.getCurrRoom().rewards.clear();
                    AbstractDungeon.getCurrRoom().addCardReward(new AnyColorCardReward());
                    AbstractDungeon.getCurrRoom().phase = AbstractRoom.RoomPhase.COMPLETE;
                    AbstractDungeon.combatRewardScreen.open();
                    this.imageEventText.updateBodyText(DESCRIPTIONS[2]);
                    this.imageEventText.updateDialogOption(0, OPTIONS[5]);
                    this.imageEventText.clearRemainingOptions();
                    this.screenNum++;
                    return;
                case 2:
                    AbstractDungeon.getCurrRoom().rewards.clear();
                    AbstractDungeon.getCurrRoom().addRelicToRewards(new StoneFragment());
                    AbstractDungeon.getCurrRoom().phase = AbstractRoom.RoomPhase.COMPLETE;
                    AbstractDungeon.combatRewardScreen.open();
                    this.imageEventText.updateBodyText(DESCRIPTIONS[3]);
                    adp().damage(new DamageInfo(null, damageAmt, DamageInfo.DamageType.HP_LOSS));
                    this.imageEventText.updateDialogOption(0, OPTIONS[5]);
                    this.imageEventText.clearRemainingOptions();
                    this.screenNum++;
                    return;
            }
        }
        openMap();
    }
}