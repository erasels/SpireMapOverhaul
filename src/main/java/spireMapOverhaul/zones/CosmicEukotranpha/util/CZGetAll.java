package spireMapOverhaul.zones.CosmicEukotranpha.util;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import spireMapOverhaul.zones.CosmicEukotranpha.CosmicZoneMod;

import java.util.HashSet;public class CZGetAll{public CZGetAll(){}
    public static HashSet<AbstractCard> get(){CosmicZoneMod.logger.info("CZGetAll() starting");HashSet<AbstractCard>cards=new HashSet();if(AbstractDungeon.player.cardInUse!=null){cards.add(AbstractDungeon.player.cardInUse);}
        cards.addAll(AbstractDungeon.player.drawPile.group);cards.addAll(AbstractDungeon.player.discardPile.group);cards.addAll(AbstractDungeon.player.exhaustPile.group);
        cards.addAll(AbstractDungeon.player.limbo.group);cards.addAll(AbstractDungeon.player.hand.group);CosmicZoneMod.logger.info("CZGetAll() returning cards");return cards;}
    public static CardGroup getCG(){CosmicZoneMod.logger.info("CZGetAll() starting");CardGroup cards=new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);if(AbstractDungeon.player.cardInUse!=null){cards.group.add(AbstractDungeon.player.cardInUse);}
        cards.group.addAll(AbstractDungeon.player.drawPile.group);cards.group.addAll(AbstractDungeon.player.discardPile.group);cards.group.addAll(AbstractDungeon.player.exhaustPile.group);
        cards.group.addAll(AbstractDungeon.player.limbo.group);cards.group.addAll(AbstractDungeon.player.hand.group);CosmicZoneMod.logger.info("CZGetAll() returning cards");return cards;}}