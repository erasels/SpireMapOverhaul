package spireMapOverhaul.zones.hailstorm.events;

import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.AbstractEvent;
import com.megacrit.cardcrawl.events.AbstractImageEvent;
import com.megacrit.cardcrawl.helpers.*;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.relics.DeadBranch;
import com.megacrit.cardcrawl.rewards.RewardItem;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndObtainEffect;
import spireMapOverhaul.SpireAnniversary6Mod;
import spireMapOverhaul.zones.hailstorm.cards.Freeze;
import spireMapOverhaul.zones.hailstorm.relics.Flint;

import static spireMapOverhaul.util.Wiz.adp;

public class AbandonedCamp extends AbstractImageEvent {
    public static final String ID = SpireAnniversary6Mod.makeID(AbandonedCamp.class.getSimpleName());
    private static final EventStrings eventStrings = CardCrawlGame.languagePack.getEventString(ID);
    private static final String NAME = eventStrings.NAME;
    private static final String[] DESCRIPTIONS = eventStrings.DESCRIPTIONS;
    private static final String[] OPTIONS = eventStrings.OPTIONS;

    public static final String IMG = SpireAnniversary6Mod.makeImagePath("events/Hailstorm/AbandonedCamp.png");

    private static final float FLINT_HP_LOSS = (float) 1 /24;
    private static final float A_15_FLINT_HP_LOSS = (float) 1 /16;
    private static final int GOLD = 90;
    private static final int A_15_GOLD = 60;
    private static final float HEALING = (float) 1/6;
    private static final float A_15_HEALING = (float) 1/9;

    private final boolean T_DEADBRANCH_F_FLINT;
    private final boolean T_GOLD_F_HEALING;

    private final AbstractCard curse = new Freeze();
    private int hpLoss;
    private int goldGain;
    private int healing;

    private int screenNum = 0;


    public AbandonedCamp() {
        super(ID, NAME ,IMG );
        this.noCardsInRewards = true;
        this.title = NAME;
        this.body = DESCRIPTIONS[0] + " NL " + DESCRIPTIONS[1] + " NL " + DESCRIPTIONS[2] + " NL " + DESCRIPTIONS[3];

        //Event randomly chooses one big reward between two, and one light reward between two
        T_DEADBRANCH_F_FLINT = AbstractDungeon.miscRng.randomBoolean() && !adp().hasRelic(DeadBranch.ID);
        T_GOLD_F_HEALING = AbstractDungeon.miscRng.randomBoolean();

        //Rewards
        if (AbstractDungeon.ascensionLevel >= 15) {//Asc 15

            //First impactful reward
            if (T_DEADBRANCH_F_FLINT) {
                curse.upgrade();
            } else {
                hpLoss = (int)(AbstractDungeon.player.maxHealth * A_15_FLINT_HP_LOSS);
            }

            //Second light reward
            if (T_GOLD_F_HEALING) {
                goldGain = A_15_GOLD;
            } else {
                healing = (int)(A_15_HEALING * AbstractDungeon.player.maxHealth);
            }

        } else {//Non-Asc 15

            //First impactful reward
            if (!T_DEADBRANCH_F_FLINT) {
                hpLoss = (int)(AbstractDungeon.player.maxHealth * FLINT_HP_LOSS);
            }

            //Second light reward
            if (T_GOLD_F_HEALING) {
                goldGain = GOLD;
            } else {
                healing = (int)(HEALING * AbstractDungeon.player.maxHealth);
            }

        }

        //Text
        //First impactful choice
        if (T_DEADBRANCH_F_FLINT) {
            this.imageEventText.setDialogOption(OPTIONS[0], curse);
        } else {
            this.imageEventText.setDialogOption(OPTIONS[1] + hpLoss + OPTIONS[2], new Flint());
        }

        //Second light choice
        if (T_GOLD_F_HEALING) {
            this.imageEventText.setDialogOption(OPTIONS[3] + goldGain + OPTIONS[4]);
        } else {
            this.imageEventText.setDialogOption(OPTIONS[5] + healing + OPTIONS[6]);
        }

        this.imageEventText.setDialogOption(OPTIONS[7]);
    }

    protected void buttonEffect(int buttonPressed) {
        switch (screenNum) {
            case 0:
                switch (buttonPressed) {
                    case 0:
                        if (T_DEADBRANCH_F_FLINT)
                            AbstractDungeon.effectList.add(new ShowCardAndObtainEffect(curse, (float) Settings.WIDTH / 2.0F, (float) Settings.HEIGHT / 2.0F));
                        else {
                            AbstractDungeon.player.damage(new DamageInfo((AbstractCreature) null, hpLoss));
                        }

                        AbstractDungeon.getCurrRoom().rewards.clear();

                        String targetRelicId = T_DEADBRANCH_F_FLINT ? DeadBranch.ID : Flint.ID;
                        AbstractRelic relic = RelicLibrary.getRelic(targetRelicId).makeCopy();
                        AbstractDungeon.getCurrRoom().spawnRelicAndObtain((float) (Settings.WIDTH / 2), (float) (Settings.HEIGHT / 2), relic);

                        this.screenNum = 1;
                        this.imageEventText.updateDialogOption(0, OPTIONS[8]);
                        this.imageEventText.clearRemainingOptions();
                        this.imageEventText.updateBodyText(DESCRIPTIONS[4]);

                        return;
                    case 1:
                        if (T_GOLD_F_HEALING)
                            AbstractDungeon.player.gainGold(goldGain);
                        else {
                            AbstractDungeon.player.heal(healing, true);
                        }

                        this.screenNum = 1;
                        this.imageEventText.updateDialogOption(0, OPTIONS[8]);
                        this.imageEventText.clearRemainingOptions();
                        this.imageEventText.updateBodyText(DESCRIPTIONS[4]);

                        return;
                    case 2:
                        AbstractDungeon.getCurrRoom().phase = AbstractRoom.RoomPhase.COMPLETE;

                        this.screenNum = 1;
                        this.imageEventText.updateDialogOption(0, OPTIONS[8]);
                        this.imageEventText.clearRemainingOptions();
                        this.imageEventText.updateBodyText(DESCRIPTIONS[4]);

                        return;
                    default:
                        return;
                }
            case 1:
                if (buttonPressed == 0) {
                    this.openMap();
                }
                break;

            default:
                this.openMap();
        }
    }


}
