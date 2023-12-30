package spireMapOverhaul.zones.CosmicEukotranpha.cardEffects.cardManip;
import com.megacrit.cardcrawl.cards.AbstractCard;
import spireMapOverhaul.zones.CosmicEukotranpha.cardEffects.CosmicZoneAbstractGameAction;
public class CZMaintainTEffect extends CosmicZoneAbstractGameAction{
    public CZMaintainTEffect(AbstractCard c){this.card=c;}public CosmicZoneAbstractGameAction makeCopy(){
        return new CZMaintainTEffect(this.card);}
    public void update(){if(card!=null){card.retain=true;}this.isDone=true;}}