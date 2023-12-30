package spireMapOverhaul.zones.CosmicEukotranpha.cardEffects.cardManip;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import spireMapOverhaul.zones.CosmicEukotranpha.cardEffects.CosmicZoneAbstractGameAction;
public class CZSeekTEffect extends CosmicZoneAbstractGameAction{
    public CZSeekTEffect(AbstractCard c){this.card=c;}public CosmicZoneAbstractGameAction makeCopy(){
        return new CZSeekTEffect(this.card);}
    public void update(){if(card!=null){AbstractDungeon.player.drawPile.moveToHand(card);}this.isDone=true;}}