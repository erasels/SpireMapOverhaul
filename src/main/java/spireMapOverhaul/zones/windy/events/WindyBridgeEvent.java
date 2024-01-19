/*
    event: player may grab a hidden card, or see its cost/rarity/type, or leave
    all options other than leave is associated with a chance to lose a card/potion/relic/gold
    actions and items lost are mixed and matched, and card/potion/relic loss options are replaced with gold loss when needed
 */
package spireMapOverhaul.zones.windy.events;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.AbstractImageEvent;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.vfx.cardManip.PurgeCardEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndObtainEffect;
import spireMapOverhaul.SpireAnniversary6Mod;

import java.util.*;

import static spireMapOverhaul.SpireAnniversary6Mod.makeID;
import static spireMapOverhaul.SpireAnniversary6Mod.makeImagePath;

public class WindyBridgeEvent extends AbstractImageEvent {
    public static final String ID = makeID("WindyBridge");
    private static final EventStrings eventStrings = CardCrawlGame.languagePack.getEventString(ID);
    private static final String NAME = eventStrings.NAME;
    public Dictionary<String, String> desc;
    public Dictionary<String, String> opt;

    private CurScreen screen;

    public static final int GOLD_LOSS = 40;
    public AbstractCard couldLoseCard;
    public AbstractRelic couldLoseRelic;
    public AbstractPotion couldLosePotion;
    public boolean lostItem = false;
    public ArrayList<OptionCon> optionCons = new ArrayList<>(Collections.nCopies(4, OptionCon.GOLD));
    public ArrayList<OptionPro> optionPros = new ArrayList<>(Arrays.asList(OptionPro.RARITY, OptionPro.COST, OptionPro.TYPE));

    public AbstractCard couldGainCard;

    public WindyBridgeEvent() {
        super(NAME, "", makeImagePath("events/Windy/WindyBridge.png"));
        desc = mapStrings(eventStrings.DESCRIPTIONS);
        opt = mapStrings(eventStrings.OPTIONS);
        body = desc.get("intro1");
        screen = CurScreen.INTRO;

        //generate potential reward
        if(AbstractDungeon.miscRng.randomBoolean(0.70f)){
            couldGainCard = AbstractDungeon.getCard(AbstractCard.CardRarity.RARE, AbstractDungeon.miscRng).makeCopy();
        }else{
            couldGainCard = AbstractDungeon.getCard(AbstractCard.CardRarity.UNCOMMON, AbstractDungeon.miscRng).makeCopy();
        }

        //identify things that could be lost by the player
        couldLoseCard = getLosableCard();
        couldLoseRelic = getLosableRelic();
        couldLosePotion = AbstractDungeon.player.getRandomPotion();

        //replace default gold loss options with things that could be lost
        if(couldLoseCard != null){
            optionCons.set(0, OptionCon.CARD);
        }
        if(couldLoseRelic != null){
            optionCons.set(1, OptionCon.RELIC);
        }
        if(couldLosePotion != null){
            optionCons.set(2, OptionCon.POTION);
        }

        //shuffle order of pros and cons
        Collections.shuffle(optionCons, new Random(AbstractDungeon.miscRng.randomLong()));
        Collections.shuffle(optionPros, new Random(AbstractDungeon.miscRng.randomLong()));
        imageEventText.setDialogOption(opt.get("hold"));
    }

    protected void buttonEffect(int buttonPressed) {
        int betterButtonNum = imageEventText.optionList.size() - buttonPressed - 1; //counting bottom up makes cases more consistent... i think
        switch (screen) {
            case INTRO: //plain first screen
                imageEventText.updateBodyText(desc.get("intro2"));
                screen = CurScreen.OPTIONS;
                updateOptions();
                break;
            case OPTIONS:
                switch (betterButtonNum) {
                    case 3: //information options
                    case 2:
                        triggerComplexOption(optionPros.get(betterButtonNum-2), optionCons.get(betterButtonNum-2), betterButtonNum-2);
                        updateOptions();
                        break;
                    case 1: //grab option
                        triggerComplexOption(OptionPro.GRAB, optionCons.get(optionCons.size()-1), -1);
                        imageEventText.clearAllDialogs();
                        imageEventText.setDialogOption(opt.get("leave"));
                        screen = CurScreen.RESULT;
                        break;
                    case 0: //leave option
                    default:
                        if(lostItem){
                            imageEventText.updateBodyText(desc.get("leaveB"));
                        }else{
                            imageEventText.updateBodyText(desc.get("leaveG"));
                        }
                        imageEventText.clearAllDialogs();
                        imageEventText.setDialogOption(opt.get("leave"));
                        screen = CurScreen.RESULT;
                }
                break;
            case RESULT:
            default:
                openMap();
        }
    }

    // update dialogue options: up to 2 information options + 1 grab option + 1 leave option
    public void updateOptions(){
        imageEventText.clearAllDialogs();
        for (int i = Math.min(1, optionPros.size()-1); i >= 0; i--) {
            setComplexOption(optionPros.get(i), optionCons.get(i));
        }
        setComplexOption(OptionPro.GRAB, optionCons.get(optionCons.size()-1));
        imageEventText.setDialogOption(opt.get("run"));
    }

    // determines the text for buttons with varying/mixed-and-matched upsides and downsides
    public void setComplexOption(OptionPro p, OptionCon c){
        String option;
        switch (p) {
            case GRAB:
                option = opt.get("grab");
                break;
            case RARITY:
                option = opt.get("rarity");
                break;
            case COST:
                option = opt.get("cost");
                break;
            case TYPE:
            default:
                option = opt.get("type");
        }
        option += getLossProbability() + opt.get("lose");
        switch (c){
            case CARD:
                imageEventText.setDialogOption(option + FontHelper.colorString(couldLoseCard.name, "r"));
                break;
            case RELIC:
                imageEventText.setDialogOption(option + FontHelper.colorString(couldLoseRelic.name, "r"));
                break;
            case POTION:
                imageEventText.setDialogOption(option + FontHelper.colorString(couldLosePotion.name, "r"));
                break;
            case GOLD:
            default:
                imageEventText.setDialogOption(option + FontHelper.colorString(""+GOLD_LOSS, "r") + opt.get("gold"));
        }
    }

    //trigger the upside of grabbing/seeing information and the downside of losing items + update event text accordingly
    public void triggerComplexOption(OptionPro p, OptionCon c, int optionNum){
        String newBodyText = "";
        switch (p){
            case GRAB:
                AbstractDungeon.effectList.add(new ShowCardAndObtainEffect(couldGainCard, Settings.WIDTH / 4f*3, Settings.HEIGHT / 2f));
                newBodyText += desc.get("grab");
                break;
            case RARITY:
                newBodyText += desc.get("rarity");
                newBodyText += couldGainCard.rarity == AbstractCard.CardRarity.RARE ? desc.get("rarityR") : desc.get("rarityU");
                break;
            case COST:
                newBodyText += desc.get("cost");
                switch (couldGainCard.cost){
                    case -2:
                        newBodyText += desc.get("costZ");
                        break;
                    case -1:
                        newBodyText += desc.get("costX");
                        break;
                    default:
                        newBodyText += desc.get("costN") + couldGainCard.cost + desc.get("costN2");
                }
                break;
            case TYPE:
                newBodyText += desc.get("type");
                switch (couldGainCard.type){
                    case ATTACK:
                        newBodyText += desc.get("typeA");
                        break;
                    case SKILL:
                        newBodyText += desc.get("typeS");
                        break;
                    case POWER:
                        newBodyText += desc.get("typeP");
                        break;
                    default:
                        newBodyText += desc.get("typeU");
                }
                break;
        }

        if(AbstractDungeon.miscRng.randomBoolean(getLossProbability() * 0.01f)){
            CardCrawlGame.sound.play("CARD_EXHAUST");
            lostItem = true;
            newBodyText += desc.get("loseItem");
            switch (c){
                case CARD:
                    AbstractDungeon.effectList.add(new PurgeCardEffect(couldLoseCard));
                    AbstractDungeon.player.masterDeck.removeCard(couldLoseCard);
                    newBodyText += FontHelper.colorString(couldLoseCard.name, "r");
                    break;
                case GOLD:
                    newBodyText += FontHelper.colorString("" + Math.min(GOLD_LOSS, AbstractDungeon.player.gold), "r") + desc.get("gold");
                    AbstractDungeon.player.loseGold(GOLD_LOSS);
                    break;
                case POTION:
                    AbstractDungeon.player.removePotion(couldLosePotion);
                    newBodyText += FontHelper.colorString(couldLosePotion.name, "r");
                    break;
                case RELIC:
                    AbstractDungeon.player.loseRelic(couldLoseRelic.relicId);
                    newBodyText += FontHelper.colorString(couldLoseRelic.name, "r");
                    break;
                default:
                    SpireAnniversary6Mod.logger.info("Windy zone event broken!??!?! Leave a detailed steam comment about this bug and call the man who fixes the mistakes of other modders, GK.");
            }
            newBodyText += desc.get("loseItem2");
        }

        if(optionNum >= 0){
            optionCons.remove(optionNum);
            optionPros.remove(optionNum);
        }
        imageEventText.updateBodyText(newBodyText);
    }

    public static int getLossProbability(){
        if(AbstractDungeon.ascensionLevel >= 15){
            return 40;
        }
        return 25;
    }

    // method sampled from Nloth event
    public AbstractRelic getLosableRelic(){
        ArrayList<AbstractRelic> relics = new ArrayList<>(AbstractDungeon.player.relics);
        Collections.shuffle(relics, new Random(AbstractDungeon.miscRng.randomLong()));
        return relics.size() > 0 ? relics.get(0) : null;
    }

    //identify a random highest tier card that can be lost
    public AbstractCard getLosableCard(){
        CardGroup group = CardGroup.getGroupWithoutBottledCards(AbstractDungeon.player.masterDeck);
        couldLoseCard = group.getRandomCard(AbstractDungeon.miscRng, AbstractCard.CardRarity.RARE);
        if(couldLoseCard == null){
            couldLoseCard = group.getRandomCard(AbstractDungeon.miscRng, AbstractCard.CardRarity.UNCOMMON);
            if(couldLoseCard == null){
                couldLoseCard = group.getRandomCard(AbstractDungeon.miscRng, AbstractCard.CardRarity.COMMON);
                if(couldLoseCard == null){
                    couldLoseCard = group.getRandomCard(AbstractDungeon.miscRng, AbstractCard.CardRarity.BASIC);
                }
            }
        }
        return couldLoseCard;
    }

    //map ID'd strings to dictionary so i dont have to deal with 100 shifting string indices
    public static Dictionary<String, String> mapStrings(String[] strings){
        Dictionary<String, String> dict = new Hashtable<>();
        for(String s : strings){
            String[] splits = s.split(":", 2);
            if(splits.length == 2){
                dict.put(splits[0], splits[1]);
            }
        }
        return dict;
    }

    private enum OptionCon {
        CARD, GOLD, RELIC, POTION
    }
    private enum OptionPro {
        GRAB, RARITY, COST, TYPE
    }
    private enum CurScreen {
        INTRO, OPTIONS, RESULT
    }
}
