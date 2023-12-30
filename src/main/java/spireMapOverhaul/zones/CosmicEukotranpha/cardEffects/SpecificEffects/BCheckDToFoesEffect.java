package spireMapOverhaul.zones.CosmicEukotranpha.cardEffects.SpecificEffects;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import spireMapOverhaul.zones.CosmicEukotranpha.cardEffects.CosmicZoneAbstractGameAction;
public class BCheckDToFoesEffect extends CosmicZoneAbstractGameAction{int b=0;
public BCheckDToFoesEffect(int bCheck,int amount){this.amount=amount;this.b=bCheck;}public CosmicZoneAbstractGameAction makeCopy(){
	return new BCheckDToFoesEffect(b,amount);}
public void update(){if(AbstractDungeon.player.currentBlock>=b){dmgAll(amount);}this.isDone=true;}}