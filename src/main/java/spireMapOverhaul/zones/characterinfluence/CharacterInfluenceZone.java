package spireMapOverhaul.zones.characterinfluence;

import basemod.AutoAdd;
import basemod.BaseMod;
import basemod.cardmods.EtherealMod;
import basemod.helpers.CardModifierManager;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.cards.colorless.MindBlast;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.characters.Ironclad;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.AbstractEvent;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.helpers.ModHelper;
import com.megacrit.cardcrawl.map.MapRoomNode;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.relics.Circlet;
import com.megacrit.cardcrawl.rewards.RewardItem;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import spireMapOverhaul.SpireAnniversary6Mod;
import spireMapOverhaul.abstracts.AbstractZone;
import spireMapOverhaul.zoneInterfaces.ModifiedEventRateZone;
import spireMapOverhaul.zoneInterfaces.OnTravelZone;
import spireMapOverhaul.zoneInterfaces.RewardModifyingZone;
import spireMapOverhaul.zoneInterfaces.ShopModifyingZone;
import spireMapOverhaul.zones.example.CoolExampleEvent;

import java.util.ArrayList;
import java.util.Arrays;

import static spireMapOverhaul.SpireAnniversary6Mod.makeID;

public class CharacterInfluenceZone extends AbstractZone implements RewardModifyingZone, ShopModifyingZone, OnTravelZone, ModifiedEventRateZone {

    public static final String ID = "CharacterInfluence";

    public CardGroup commonPool;
    public CardGroup uncommonPool;
    public CardGroup rarePool;

    public AbstractPlayer classInfluence;

    public CharacterInfluenceZone() {
        super(ID, Icons.MONSTER, Icons.EVENT, Icons.SHOP);
        this.width = 2;
        this.height = 3;
    }

    public CharacterInfluenceZone(AbstractPlayer p) {
        this();
        this.classInfluence = p;
    }

    @Override
    public void mapGenDone(ArrayList<ArrayList<MapRoomNode>> map) {
        super.mapGenDone(map);
        do {
            this.classInfluence = CardCrawlGame.characterManager.getRandomCharacter(AbstractDungeon.mapRng);
        } while (this.classInfluence.chosenClass == AbstractDungeon.player.chosenClass && CardCrawlGame.characterManager.getAllCharacters().size() != 1);
        this.name = TEXT[2] + this.classInfluence.title;
        updateDescription();
    }

    @Override
    public AbstractZone copy() {
        return new CharacterInfluenceZone(this.classInfluence);
    }

    @Override
    public Color getColor() {
        if (this.classInfluence == null) return Color.GRAY;
        return this.classInfluence.getCardRenderColor();
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
        switch (rarity) {
            case UNCOMMON:
                return type != null ? uncommonPool.getRandomCard(type, false) : uncommonPool.getRandomCard(false);
            case RARE:
                return type != null ? rarePool.getRandomCard(type, false) : rarePool.getRandomCard(false);
            case COMMON:
            default:
                return type != null ? commonPool.getRandomCard(type, false) : commonPool.getRandomCard(false);
        }
    }

    @Override
    public void modifyRewardCards(ArrayList<AbstractCard> rewardCards) {
        ArrayList<AbstractCard> newCards = new ArrayList<>();
        for (AbstractCard card : rewardCards) {
            boolean dupe;
            do {
                dupe = false;
                AbstractCard cardToAdd = getCard(card.rarity, null);
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
        rewardCards.clear();
        rewardCards.addAll(newCards);
    }

    @Override
    public void postCreateShopCards(ArrayList<AbstractCard> coloredCards, ArrayList<AbstractCard> colorlessCards) {
        ArrayList<AbstractCard> newCards = new ArrayList<>();
        for (AbstractCard card : coloredCards) {
            boolean dupe;
            do {
                dupe = false;
                AbstractCard cardToAdd = getCard(card.rarity, card.type);
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
        coloredCards.clear();
        coloredCards.addAll(newCards);
    }

    @Override
    public float zoneSpecificEventRate() {
        return 1;
    }

}
