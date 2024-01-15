package spireMapOverhaul.zones.mirror.events;

import basemod.abstracts.events.PhasedEvent;
import basemod.abstracts.events.phases.TextPhase;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.blue.EchoForm;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.relics.DollysMirror;
import com.megacrit.cardcrawl.relics.PrismaticShard;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndObtainEffect;
import spireMapOverhaul.SpireAnniversary6Mod;

public class WeirdMirrorsEvent extends PhasedEvent {
    public static final String ID = SpireAnniversary6Mod.makeID("WeirdMirrors");
    private static final EventStrings eventStrings = CardCrawlGame.languagePack.getEventString(ID);
    private static final String[] DESCRIPTIONS = eventStrings.DESCRIPTIONS;
    private static final String[] OPTIONS = eventStrings.OPTIONS;
    private static final String title = eventStrings.NAME;

    public WeirdMirrorsEvent() {
        super(ID, title, SpireAnniversary6Mod.makeImagePath("events/Mirror/WeirdMirrors.png"));

        TextPhase firstPhase = new TextPhase(DESCRIPTIONS[0]).addOption(new TextPhase.OptionInfo(OPTIONS[0]).cardSelectOption(
                "duplicate",
                () -> AbstractDungeon.player.masterDeck,
                OPTIONS[5],
                1,
                false,
                false,
                false,
                false,
                (cards) -> {
                    if (!cards.isEmpty()) {
                        AbstractCard card = cards.get(0).makeStatEquivalentCopy();
                        logMetricObtainCard(ID, "Copied", card);
                        AbstractDungeon.effectList.add(new ShowCardAndObtainEffect(card, (float) Settings.WIDTH / 2.0F, (float) Settings.HEIGHT / 2.0F));
                    }
                }
        ));
        boolean noRelic = true;
        EchoForm echoForm = new EchoForm();
        AbstractRelic prism = AbstractDungeon.player.getRelic(PrismaticShard.ID);
        if (prism != null) {
            firstPhase.addOption(new TextPhase.OptionInfo(OPTIONS[1], echoForm), (i)->{
                logMetricObtainCard(ID, "PutPrism", echoForm);
                prism.flash();
                AbstractDungeon.effectList.add(new ShowCardAndObtainEffect(echoForm, (float)Settings.WIDTH / 2.0F, (float)Settings.HEIGHT / 2.0F));
                transitionKey("prism");
            });
            noRelic = false;
        }
        AbstractRelic mirror = AbstractDungeon.player.getRelic(DollysMirror.ID);
        if (mirror != null) {
            firstPhase.addOption(new TextPhase.OptionInfo(OPTIONS[2], echoForm), (i)-> {
                logMetricObtainCard(ID, "PutMirror", echoForm);
                mirror.flash();
                AbstractDungeon.effectList.add(new ShowCardAndObtainEffect(echoForm, (float) Settings.WIDTH / 2.0F, (float) Settings.HEIGHT / 2.0F));
                transitionKey("mirror");
            });
            noRelic = false;
        }
        if (noRelic) {
            firstPhase.addOption(new TextPhase.OptionInfo(OPTIONS[3]).enabledCondition(()->false));
        }
        firstPhase.addOption(OPTIONS[4], (i)->{
            logMetricIgnored(ID);
            transitionKey("leave");
        });

        registerPhase("first", firstPhase);
        registerPhase("duplicate", new TextPhase(DESCRIPTIONS[1]).addOption(OPTIONS[4], (i)->openMap()));
        registerPhase("prism", new TextPhase(DESCRIPTIONS[2]).addOption(OPTIONS[4], (i)->openMap()));
        registerPhase("mirror", new TextPhase(DESCRIPTIONS[3]).addOption(OPTIONS[4], (i)->openMap()));
        registerPhase("leave", new TextPhase(DESCRIPTIONS[4]).addOption(OPTIONS[4], (i)->openMap()));

        transitionKey("first");
    }
}
