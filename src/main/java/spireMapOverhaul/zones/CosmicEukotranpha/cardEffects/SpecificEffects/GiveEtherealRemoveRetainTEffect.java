package spireMapOverhaul.zones.CosmicEukotranpha.cardEffects.SpecificEffects;
import com.megacrit.cardcrawl.cards.AbstractCard;
import spireMapOverhaul.zones.CosmicEukotranpha.cardEffects.CosmicZoneAbstractGameAction;
public class GiveEtherealRemoveRetainTEffect extends CosmicZoneAbstractGameAction{
    public GiveEtherealRemoveRetainTEffect(AbstractCard c){this.card=c;}public CosmicZoneAbstractGameAction makeCopy(){
        return new GiveEtherealRemoveRetainTEffect(this.card);}
    public void update(){if(card!=null){card.retain=false;card.selfRetain=false;card.isEthereal=true;}this.isDone=true;}}