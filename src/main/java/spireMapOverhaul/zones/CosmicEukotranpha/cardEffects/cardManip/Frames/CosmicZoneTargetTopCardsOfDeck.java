package spireMapOverhaul.zones.CosmicEukotranpha.cardEffects.cardManip.Frames;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import spireMapOverhaul.zones.CosmicEukotranpha.cardEffects.CosmicZoneAbstractGameAction;
public class CosmicZoneTargetTopCardsOfDeck extends CosmicZoneAbstractGameAction{private CosmicZoneAbstractGameAction action;
public CosmicZoneTargetTopCardsOfDeck(int amount,CosmicZoneAbstractGameAction action){this.action=action;this.amount=amount;}
    @Override public CosmicZoneAbstractGameAction makeCopy(){return new CosmicZoneTargetTopCardsOfDeck(amount,action);}
    public void update(){int q=AbstractDungeon.player.drawPile.size();for(int i=0;i<Math.min(amount,q);i++){
            action.card=AbstractDungeon.player.drawPile.getNCardFromTop(AbstractDungeon.player.drawPile.size()-i-1);addToBot(action);}isDone=true;}}
