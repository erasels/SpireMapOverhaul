package spireMapOverhaul.zones.CosmicEukotranpha.cardEffects.cardManip.Frames;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import spireMapOverhaul.zones.CosmicEukotranpha.cardEffects.CosmicZoneAbstractGameAction;
public class TargetLeftMostInHandEffect extends AbstractGameAction{private int cost=99;private AbstractCard.CardType type=null;private AbstractCard.CardRarity rarity=null;CosmicZoneAbstractGameAction action;private int am;private boolean lessThan=false;
    public TargetLeftMostInHandEffect(CosmicZoneAbstractGameAction action, int am){this.action=action;this.am=am;}public TargetLeftMostInHandEffect(int cost, AbstractCard.CardType type, AbstractCard.CardRarity rarity, CosmicZoneAbstractGameAction action, int am){this.cost=cost;this.type=type;this.rarity=rarity;this.action=action;this.am=am;this.lessThan=false;}
    public TargetLeftMostInHandEffect(int cost,boolean lessThan,AbstractCard.CardType type,AbstractCard.CardRarity rarity,CosmicZoneAbstractGameAction action,int am){this.cost=cost;this.type=type;this.rarity=rarity;this.action=action;this.am=am;this.lessThan=lessThan;}
    public void update(){if(!AbstractDungeon.player.hand.isEmpty()){for(int i=0;i<AbstractDungeon.player.hand.size();i++){if(AbstractDungeon.player.hand.group.get(i)!=null){int vvvvvv=0;
                if(type!=null){if(AbstractDungeon.player.hand.group.get(i).type==type){vvvvvv++;}}else{vvvvvv++;}if(cost!=99){if(AbstractDungeon.player.hand.group.get(i).costForTurn==cost){vvvvvv++;}else if(lessThan && AbstractDungeon.player.hand.group.get(i).costForTurn<=cost){vvvvvv++;}}else{vvvvvv++;}if(rarity!=null){if(AbstractDungeon.player.hand.group.get(i).rarity==rarity){vvvvvv++;}}else{vvvvvv++;}if(vvvvvv==3){
                    action.card=AbstractDungeon.player.hand.group.get(i);addToTop(action);am--;if(am==0){break;}}}}}this.isDone=true;}}
