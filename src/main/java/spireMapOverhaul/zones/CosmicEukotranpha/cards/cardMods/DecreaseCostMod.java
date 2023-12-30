package spireMapOverhaul.zones.CosmicEukotranpha.cards.cardMods;
import basemod.abstracts.AbstractCardModifier;
import com.megacrit.cardcrawl.cards.AbstractCard;import spireMapOverhaul.SpireAnniversary6Mod;
public class DecreaseCostMod extends AbstractCardModifier{public static final String ID=SpireAnniversary6Mod.makeID(
				DecreaseCostMod.class.getSimpleName());public AbstractCardModifier makeCopy(){return new
				DecreaseCostMod(am);}public String identifier(AbstractCard card){return ID;}public int key=0;public int am=0;public int change=0;
public DecreaseCostMod(int amount){this.am=amount;}public DecreaseCostMod(int amount,int key){this.am=amount;this.key=key;}//Keys 1=Removed at turn end,2=Removed when played
public void onInitialApplication(AbstractCard card){change=Math.min(am,Math.max(card.costForTurn,card.cost));card.costForTurn=Math.max(card.costForTurn-am,0);card.cost=Math.max(card.cost-am,0);}
public void onRemove(AbstractCard card){card.cost+=change;card.costForTurn+=change;}
public boolean removeAtEndOfTurn(AbstractCard card){return key==1;}
public boolean removeOnCardPlayed(AbstractCard card){return key==2;}
public String modifyDescription(String rawDescription,AbstractCard card){return "Test";}}