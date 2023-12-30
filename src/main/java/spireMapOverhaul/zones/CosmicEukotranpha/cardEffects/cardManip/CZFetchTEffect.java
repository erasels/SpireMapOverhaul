package spireMapOverhaul.zones.CosmicEukotranpha.cardEffects.cardManip;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import spireMapOverhaul.zones.CosmicEukotranpha.cardEffects.CosmicZoneAbstractGameAction;
public class CZFetchTEffect extends CosmicZoneAbstractGameAction{
    public CZFetchTEffect(AbstractCard c){this.card=c;}public CosmicZoneAbstractGameAction makeCopy(){
        return new CZFetchTEffect(this.card);}
    public void update(){if(card!=null){AbstractDungeon.player.discardPile.moveToHand(card);}this.isDone=true;}}