package spireMapOverhaul.zones.gremlinTown.events;

import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.AbstractImageEvent;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.vfx.UpgradeShineEffect;
import com.megacrit.cardcrawl.vfx.cardManip.PurgeCardEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardBrieflyEffect;

import static spireMapOverhaul.SpireAnniversary6Mod.makeID;
import static spireMapOverhaul.SpireAnniversary6Mod.makeImagePath;
import static spireMapOverhaul.util.Wiz.adp;
import static spireMapOverhaul.util.Wiz.asc;

public class GremlinPerformance extends AbstractImageEvent {
    public static final String ID = makeID(GremlinPerformance.class.getSimpleName());
    private static final EventStrings eventStrings;
    private static final String[] DESCRIPTIONS;
    private static final String NAME;
    private static final String IMAGE_PATH;
    private static final String[] OPTIONS;

    private static final int REMOVAL_PRICE = 50;
    private static final int REMOVAL_PRICE_A15 = 75;
    private static final int MAX_HP_PRICE = 125;
    private static final int MAX_HP_PRICE_A15 =150;
    private static final int UPGRADE_PRICE = 250;
    private static final int UPGRADE_PRICE_A15 = 300;

    private static final int MAX_HP_GAIN = 10;

    private CUR_SCREEN screen;

    static {
        eventStrings = CardCrawlGame.languagePack.getEventString(ID);
        NAME = eventStrings.NAME;
        DESCRIPTIONS = eventStrings.DESCRIPTIONS;
        OPTIONS = eventStrings.OPTIONS;
        IMAGE_PATH = makeImagePath("events/GremlinTown/" + GremlinPerformance.class.getSimpleName() + ".jpg");
    }

    public static boolean bonusCondition() {
        if (adp() == null)
            return false;
        return adp().gold >= getMaxHpPrice();
    }

    public GremlinPerformance() {
        super(NAME, DESCRIPTIONS[0], IMAGE_PATH);
        screen = CUR_SCREEN.INTRO1;
        imageEventText.updateBodyText(DESCRIPTIONS[0]);
        imageEventText.setDialogOption(OPTIONS[0]);
    }

    @Override
    public void onEnterRoom() {
        CardCrawlGame.music.precacheTempBgm("BOSS_CITY");
    }

    public void update() {
        super.update();
        if (!AbstractDungeon.gridSelectScreen.selectedCards.isEmpty()) {
            AbstractCard c = AbstractDungeon.gridSelectScreen.selectedCards.get(0);
            AbstractDungeon.topLevelEffects.add(new PurgeCardEffect(c, (float)(Settings.WIDTH / 2), (float)(Settings.HEIGHT / 2)));
            adp().masterDeck.removeCard(c);
            AbstractDungeon.gridSelectScreen.selectedCards.remove(c);
        }
    }

    protected void buttonEffect(int buttonPressed) {
        if (screen == CUR_SCREEN.INTRO1) {
            screen = CUR_SCREEN.INTRO2;
            imageEventText.clearAllDialogs();
            imageEventText.setDialogOption(OPTIONS[0]);
            imageEventText.updateBodyText(DESCRIPTIONS[1]);
            CardCrawlGame.music.unsilenceBGM();
            AbstractDungeon.scene.fadeOutAmbiance();
            CardCrawlGame.music.playPrecachedTempBgm();
        } else if (screen == CUR_SCREEN.INTRO2) {
            screen = CUR_SCREEN.DECISION;
            imageEventText.clearAllDialogs();
            imageEventText.setDialogOption(OPTIONS[1].replace("{0}", String.valueOf(getRemovalPrice())));
            imageEventText.setDialogOption(OPTIONS[2].replace("{0}", String.valueOf(getMaxHpPrice())).replace("{1}", String.valueOf(MAX_HP_GAIN)));
            if (adp().gold > getUpgradePrice())
                imageEventText.setDialogOption(OPTIONS[3].replace("{0}", String.valueOf(getUpgradePrice())));
            else
                imageEventText.setDialogOption(OPTIONS[4].replace("{0}", String.valueOf(getUpgradePrice())), true);
            imageEventText.setDialogOption(OPTIONS[5]);
            imageEventText.updateBodyText(DESCRIPTIONS[2]);
        } else if (screen == CUR_SCREEN.DECISION) {
            screen = CUR_SCREEN.COMPLETE;
            switch (buttonPressed) {
                case 0:
                    if (CardGroup.getGroupWithoutBottledCards(adp().masterDeck.getPurgeableCards()).size() > 0) {
                        adp().loseGold(getRemovalPrice());
                        AbstractDungeon.gridSelectScreen.open(CardGroup.getGroupWithoutBottledCards(
                                adp().masterDeck.getPurgeableCards()), 1, OPTIONS[7], false,
                                false, false, true);
                    }
                    imageEventText.updateBodyText(DESCRIPTIONS[3]);
                    break;
                case 1:
                    adp().loseGold(getMaxHpPrice());
                    adp().increaseMaxHp(MAX_HP_GAIN, true);
                    imageEventText.updateBodyText(DESCRIPTIONS[4]);
                    break;
                case 2:
                    adp().loseGold(getUpgradePrice());

                    int effectCount = 0;
                    for (AbstractCard c : adp().masterDeck.group) {
                        if (c.canUpgrade() && c.type == AbstractCard.CardType.ATTACK) {
                            c.upgrade();
                            ++effectCount;
                            if (effectCount <= 20) {
                                float x = MathUtils.random(0.1F, 0.9F) * (float)Settings.WIDTH;
                                float y = MathUtils.random(0.2F, 0.8F) * (float)Settings.HEIGHT;
                                AbstractDungeon.effectList.add(new ShowCardBrieflyEffect(c.makeStatEquivalentCopy(), x, y));
                                AbstractDungeon.topLevelEffects.add(new UpgradeShineEffect(x, y));
                            }
                            AbstractDungeon.player.bottledCardUpgradeCheck(c);
                        }
                    }
                    imageEventText.updateBodyText(DESCRIPTIONS[5]);
                    break;
                case 3:
                    imageEventText.updateBodyText(DESCRIPTIONS[6]);
                    break;
            }
            imageEventText.clearAllDialogs();
            imageEventText.setDialogOption(OPTIONS[6]);
        } else {
            AbstractDungeon.scene.fadeInAmbiance();
            CardCrawlGame.music.fadeOutTempBGM();
            openMap();
        }
    }

    private enum CUR_SCREEN {
        INTRO1,
        INTRO2,
        DECISION,
        COMPLETE;
    }

    private static int getRemovalPrice() {
        if (asc() < 15)
            return REMOVAL_PRICE;
        else
            return REMOVAL_PRICE_A15;
    }

    private static int getMaxHpPrice() {
        if (asc() < 15)
            return MAX_HP_PRICE;
        else
            return MAX_HP_PRICE_A15;
    }

    private static int getUpgradePrice() {
        if (asc() < 15)
            return UPGRADE_PRICE;
        else
            return UPGRADE_PRICE_A15;
    }
}
