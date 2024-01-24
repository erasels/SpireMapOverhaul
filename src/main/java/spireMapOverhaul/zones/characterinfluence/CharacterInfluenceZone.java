package spireMapOverhaul.zones.characterinfluence;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.map.MapRoomNode;
import spireMapOverhaul.abstracts.AbstractZone;
import spireMapOverhaul.patches.ZonePatches;
import spireMapOverhaul.zoneInterfaces.ModifiedEventRateZone;
import spireMapOverhaul.zoneInterfaces.OnTravelZone;
import spireMapOverhaul.zoneInterfaces.RewardModifyingZone;
import spireMapOverhaul.zoneInterfaces.ShopModifyingZone;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

@SuppressWarnings("unused")
public class CharacterInfluenceZone extends AbstractZone implements RewardModifyingZone, ShopModifyingZone, OnTravelZone, ModifiedEventRateZone {

    public static final String ID = "CharacterInfluence";

    public CardGroup commonPool;
    public CardGroup uncommonPool;
    public CardGroup rarePool;

    public AbstractPlayer classInfluence;
    public Color c;

    public CharacterInfluenceZone() {
        super(ID, Icons.EVENT, Icons.SHOP);
        this.width = 2;
        this.maxWidth = 3;
        this.height = 3;
        this.c = Color.GRAY;
    }

    public CharacterInfluenceZone(AbstractPlayer p) {
        this();
        this.classInfluence = p;
        if (classInfluence != null) {
            this.c = classInfluence.getCardRenderColor();
            this.c.a = 1;
        }
    }

    public static AbstractPlayer getCurrentZoneCharacter() {
        AbstractZone currentZone = ZonePatches.currentZone();

        if (!(currentZone instanceof CharacterInfluenceZone))
            return null;

        return ((CharacterInfluenceZone) currentZone).classInfluence;
    }

    @Override
    public void mapGenDone(ArrayList<ArrayList<MapRoomNode>> map) {
        super.mapGenDone(map);
        ArrayList<AbstractPlayer> options = CardCrawlGame.characterManager.getAllCharacters().stream()
                .filter(p -> p.chosenClass != AbstractDungeon.player.chosenClass
                        && !p.chosenClass.name().equals("THE_PACKMASTER")
                        && !p.chosenClass.name().equals("THE_SISTERS")
                        && !p.chosenClass.name().equals("Librarian")
                        && !p.chosenClass.name().equals("THE_RAINBOW "))
                .collect(Collectors.toCollection(ArrayList::new));
        this.classInfluence = !options.isEmpty() ? options.get(AbstractDungeon.mapRng.random(options.size() - 1)) : AbstractDungeon.player;
        if (classInfluence != null) {
            this.c = classInfluence.getCardRenderColor();
            this.c.a = 1;
        }
        this.name = TEXT[3] + this.classInfluence.title;
        updateDescription();
    }

    @Override
    public AbstractZone copy() {
        return new CharacterInfluenceZone(this.classInfluence);
    }

    @Override
    public Color getColor() {
        return c;
    }

    public void initPools() {
        commonPool = new CardGroup(CardGroup.CardGroupType.CARD_POOL);
        uncommonPool = new CardGroup(CardGroup.CardGroupType.CARD_POOL);
        rarePool = new CardGroup(CardGroup.CardGroupType.CARD_POOL);

        ArrayList<AbstractCard> tmpCardList = new ArrayList<>();
        this.classInfluence.getCardPool(tmpCardList);

        for (AbstractCard c : tmpCardList) {
            switch (c.rarity) {
                case COMMON:
                    commonPool.addToTop(c);
                    continue;
                case UNCOMMON:
                    uncommonPool.addToTop(c);
                    continue;
                case RARE:
                    rarePool.addToTop(c);
                    continue;
                case CURSE:
            }
        }

        //Now, we should have gotten our pools.

    }

    public AbstractCard getCard(AbstractCard.CardRarity rarity, AbstractCard.CardType type) {
        if (commonPool == null || uncommonPool == null || rarePool == null) initPools();
        //Extra saftey checks to prevent crashes when characters have weird cardpool mechanics
        if(commonPool.isEmpty()) commonPool = AbstractDungeon.commonCardPool;
        if(uncommonPool.isEmpty()) uncommonPool = AbstractDungeon.uncommonCardPool;
        if(rarePool.isEmpty()) rarePool = AbstractDungeon.rareCardPool;

        AbstractCard cardToReturn;
        switch (rarity) {
            case UNCOMMON:
                cardToReturn = type == null ? uncommonPool.getRandomCard(true) : uncommonPool.getRandomCard(type, true);
                break;
            case RARE:
                cardToReturn = type == null ? rarePool.getRandomCard(true) : rarePool.getRandomCard(type, true);
                break;
            case COMMON:
            default:
                cardToReturn = type == null ? commonPool.getRandomCard(true) : commonPool.getRandomCard(type, true);
                break;
        }
        return cardToReturn.makeCopy();
    }

    public void replaceCards(ArrayList<AbstractCard> cardList, boolean keepType) {
        ArrayList<AbstractCard> newCards = new ArrayList<>();
        for (AbstractCard card : cardList) {
            boolean dupe;
            do {
                dupe = false;
                AbstractCard cardToAdd = getCard(card.rarity, keepType ? card.type : null);
                for (AbstractCard newCard : newCards) {
                    if (cardToAdd.cardID.equals(newCard.cardID)) {
                        dupe = true;
                        break;
                    }
                }
                if (!dupe) {
                    newCards.add(cardToAdd);
                }
            } while (dupe);
        }
        cardList.clear();
        applyStandardUpgradeLogic(newCards);
        cardList.addAll(newCards);
    }

    @Override
    public AbstractCard getReplacementShopCardForCourier(AbstractCard purchasedCard) {
        AbstractCard.CardRarity rar = purchasedCard.rarity;
        if(!Arrays.asList(AbstractCard.CardRarity.COMMON, AbstractCard.CardRarity.UNCOMMON, AbstractCard.CardRarity.RARE).contains(rar))
            rar = AbstractCard.CardRarity.UNCOMMON;
        return getCard(rar, purchasedCard.type);
    }

    @Override
    public void modifyRewardCards(ArrayList<AbstractCard> rewardCards) {
        replaceCards(rewardCards, false);
    }

    @Override
    public void postCreateShopCards(ArrayList<AbstractCard> coloredCards, ArrayList<AbstractCard> colorlessCards) {
        replaceCards(coloredCards, true);
    }

    @Override
    public float zoneSpecificEventRate() {
        return 1;
    }

}
