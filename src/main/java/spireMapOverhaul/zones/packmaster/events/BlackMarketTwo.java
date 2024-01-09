package spireMapOverhaul.zones.packmaster.events;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.AbstractImageEvent;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.RelicLibrary;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndObtainEffect;
import spireMapOverhaul.SpireAnniversary6Mod;
import spireMapOverhaul.util.Wiz;
import spireMapOverhaul.zones.packmaster.PackmasterZone;

import java.util.ArrayList;
import java.util.Collections;
import java.util.stream.Collectors;

public class BlackMarketTwo extends AbstractImageEvent {
    public static final String ID = SpireAnniversary6Mod.makeID("BlackMarketTwo");
    private static final EventStrings eventStrings = CardCrawlGame.languagePack.getEventString(ID);
    private static final String NAME = eventStrings.NAME;
    private static final String[] DESCRIPTIONS = eventStrings.DESCRIPTIONS;
    private static final String[] OPTIONS = eventStrings.OPTIONS;
    public static final String IMG = SpireAnniversary6Mod.makeImagePath("events/Packmaster/BlackMarketTwo.png");
    private static final int CARDS = 3;
    private static final int HP_LOSS = 10;
    private static final int A15_HP_LOSS = 12;
    private static final int GOLD = 20;
    private static final int A15_GOLD = 25;

    private int hpLoss;
    private int gold;
    private AbstractRelic boosterBox;
    private AbstractCard vexed;
    private AbstractCard cardistry;

    private int screenNum = 0;

    public BlackMarketTwo() {
        super(NAME, DESCRIPTIONS[0], IMG);

        this.gold = AbstractDungeon.ascensionLevel >= 15 ? A15_GOLD : GOLD;
        this.hpLoss = AbstractDungeon.ascensionLevel >= 15 ? A15_HP_LOSS : HP_LOSS;
        this.boosterBox = RelicLibrary.getRelic("anniv5:PMBoosterBox");
        this.vexed = CardLibrary.getCard("anniv5:Vexed").makeCopy();
        this.cardistry = CardLibrary.getCard("anniv5:Cardistry").makeCopy();

        if (!AbstractDungeon.player.hasRelic(this.boosterBox.relicId)) {
            this.imageEventText.setDialogOption(OPTIONS[0].replace("{0}", FontHelper.colorString(this.boosterBox.name, "g")).replace("{1}", FontHelper.colorString(this.vexed.name, "r")), this.vexed, this.boosterBox);
        } else {
            this.imageEventText.setDialogOption(OPTIONS[1], true);

        }
        this.imageEventText.setDialogOption(OPTIONS[2].replace("{0}", CARDS + "").replace("{1}", this.hpLoss + ""));
        if (AbstractDungeon.player.gold >= this.gold) {
            this.imageEventText.setDialogOption(OPTIONS[3].replace("{0}", FontHelper.colorString(this.cardistry.name, "g")).replace("{1}", this.gold + ""), this.cardistry);
        } else {
            this.imageEventText.setDialogOption(OPTIONS[4].replace("{0}", this.gold + ""), true);
        }
        this.imageEventText.setDialogOption(OPTIONS[5]);
    }

    @Override
    protected void buttonEffect(int buttonPressed) {
        switch (screenNum) {
            case 0:
                switch (buttonPressed) {
                    case 0: // Box
                        this.imageEventText.updateBodyText(DESCRIPTIONS[1]);
                        this.screenNum = 1;
                        this.imageEventText.updateDialogOption(0, OPTIONS[5]);
                        this.imageEventText.clearRemainingOptions();
                        AbstractDungeon.getCurrRoom().spawnRelicAndObtain((float) (Settings.WIDTH / 2), (float) (Settings.HEIGHT / 2), this.boosterBox);
                        AbstractDungeon.rareRelicPool.remove(this.boosterBox.relicId);
                        AbstractDungeon.effectList.add(new ShowCardAndObtainEffect(this.vexed, (float)Settings.WIDTH / 2.0F, (float)Settings.HEIGHT / 2.0F));
                        logMetricObtainCardAndRelic(ID, "Box", this.vexed, this.boosterBox);
                        break;
                    case 1: // Bag
                        this.imageEventText.updateBodyText(DESCRIPTIONS[2]);
                        this.screenNum = 1;
                        this.imageEventText.updateDialogOption(0, OPTIONS[5]);
                        this.imageEventText.clearRemainingOptions();
                        ArrayList<AbstractCard> cards = new ArrayList<>();
                        PackmasterZone zone = (PackmasterZone)Wiz.getCurZone();
                        ArrayList<AbstractCard> packCards = new ArrayList<>(zone.getCards());
                        Collections.shuffle(packCards, new java.util.Random(AbstractDungeon.miscRng.randomLong()));
                        for (int i = 0; i < CARDS; i++) {
                            AbstractCard c = packCards.get(i).makeCopy();
                            cards.add(c);
                            int offset = i - CARDS/2;
                            AbstractDungeon.effectList.add(new ShowCardAndObtainEffect(c, (float)Settings.WIDTH / 2.0F - offset * 300.0F * Settings.scale, (float)Settings.HEIGHT / 2.0F));
                        }
                        AbstractDungeon.player.damage(new DamageInfo(null, this.hpLoss));
                        logMetric(ID, "Bag", cards.stream().map(c -> c.cardID).collect(Collectors.toList()), null, null, null, null, null, null, 0, 0, this.hpLoss, 0, 0, 0);
                        break;
                    case 2: // Bin
                        this.imageEventText.updateBodyText(DESCRIPTIONS[3]);
                        this.screenNum = 1;
                        this.imageEventText.updateDialogOption(0, OPTIONS[5]);
                        this.imageEventText.clearRemainingOptions();
                        AbstractDungeon.effectList.add(new ShowCardAndObtainEffect(this.cardistry, (float)Settings.WIDTH / 2.0F, (float)Settings.HEIGHT / 2.0F));
                        AbstractDungeon.player.loseGold(this.gold);
                        logMetric(ID, "Bin", Collections.singletonList(this.cardistry.cardID), null, null, null, null, null, null, 0, 0, this.hpLoss, 0, 0, this.gold);
                        break;
                    default: // Leave
                        this.imageEventText.updateBodyText(DESCRIPTIONS[4]);
                        this.screenNum = 1;
                        this.imageEventText.updateDialogOption(0, OPTIONS[5]);
                        this.imageEventText.clearRemainingOptions();
                        logMetricIgnored(ID);
                        break;
                }
                break;
            default:
                this.openMap();
                break;
        }
    }
}
