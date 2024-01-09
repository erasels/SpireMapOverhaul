package spireMapOverhaul.zones.thefog.events;

import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.AbstractImageEvent;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndObtainEffect;

import static spireMapOverhaul.SpireAnniversary6Mod.makeID;
import static spireMapOverhaul.SpireAnniversary6Mod.makeImagePath;
import static spireMapOverhaul.util.Wiz.adp;

public class StandingStones extends AbstractImageEvent {
    public static final String ID = makeID("StandingStones");

    private static final EventStrings eventStrings = CardCrawlGame.languagePack.getEventString(ID);

    public static final String NAME = eventStrings.NAME;
    public static final String[] DESCRIPTIONS = eventStrings.DESCRIPTIONS;
    public static final String[] OPTIONS = eventStrings.OPTIONS;

    private int screenNum = 0;

    private boolean pickCard = false;

    private static final float HP_HEAL_PERCENT = 0.33F;
    private static final float A15_HP_HEAL_PERCENT = 0.2F;

    private final int healAmt;
    private final int damageAmt;

    public StandingStones() {
        // TODO: add this art
        super(NAME, DESCRIPTIONS[0], makeImagePath("events/TheFog/StandingStones.png"));
        if (AbstractDungeon.ascensionLevel >= 15) {
            this.healAmt = MathUtils.round(adp().maxHealth * A15_HP_HEAL_PERCENT);
            this.damageAmt = 10;
        } else {
            this.healAmt = MathUtils.round(adp().maxHealth * HP_HEAL_PERCENT);
            this.damageAmt = 7;
        }
        this.imageEventText.setDialogOption(OPTIONS[0] + this.healAmt + OPTIONS[1]);
        this.imageEventText.setDialogOption(OPTIONS[2]);
        this.imageEventText.setDialogOption(OPTIONS[3] + this.damageAmt + OPTIONS[4]);
    }

    public void update() {
        super.update();
        if (this.pickCard &&
                !AbstractDungeon.isScreenUp && !AbstractDungeon.gridSelectScreen.selectedCards.isEmpty()) {
            // TODO: change/delete this code cus the reward should do everything
            AbstractCard c = (AbstractDungeon.gridSelectScreen.selectedCards.get(0)).makeCopy();
            logMetricObtainCard("Standing Stones", "Stargaze", c);
            AbstractDungeon.effectList.add(new ShowCardAndObtainEffect(c, Settings.WIDTH / 2.0F, Settings.HEIGHT / 2.0F));
            AbstractDungeon.gridSelectScreen.selectedCards.clear();
        }
    }

    protected void buttonEffect(int buttonPressed) {
        if (this.screenNum == 0) {
            this.screenNum++;
            switch (buttonPressed) {
                case 0:
                    this.imageEventText.updateBodyText(DESCRIPTIONS[1]);
                    AbstractDungeon.player.heal(healAmt, true);
                    logMetricHeal("Standing Stones", "Sleep", healAmt);
                    this.imageEventText.updateDialogOption(0, OPTIONS[3]);
                    this.imageEventText.clearRemainingOptions();
                    return;
                case 1:
                    this.imageEventText.updateBodyText(DESCRIPTIONS[2]);
                    this.imageEventText.updateDialogOption(0, OPTIONS[3]);
                    this.imageEventText.clearRemainingOptions();
                    this.pickCard = true;
                    // TODO: 1 of 5 cards of any color, as a reward
                    return;
                case 2:
                    // TODO: make the relic
                    adp().damage(new DamageInfo(null, damageAmt, DamageInfo.DamageType.HP_LOSS));
                    logMetricTakeDamage("Standing Stones", "Smashed a Stone", damageAmt);
                    return;
            }
        }
        openMap();
    }
}