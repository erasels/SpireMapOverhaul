package spireMapOverhaul.zones.CosmicEukotranpha.cards.cardMods;
import basemod.abstracts.AbstractCardModifier;
import com.megacrit.cardcrawl.cards.AbstractCard;import spireMapOverhaul.SpireAnniversary6Mod;
public class IncreaseCostMod extends AbstractCardModifier{public static final String ID=SpireAnniversary6Mod.makeID(
				IncreaseCostMod.class.getSimpleName());public AbstractCardModifier makeCopy(){return new
				IncreaseCostMod(am);}public String identifier(AbstractCard card){return ID;}public int key=0;public int am=0;public int change=0;
public IncreaseCostMod(int amount){this.am=amount;}public IncreaseCostMod(int amount,int key){this.am=amount;this.key=key;}//Keys 1=Removed at turn end,2=Removed when played
public void onInitialApplication(AbstractCard card){card.cost-=am;card.costForTurn-=am;}
public void onRemove(AbstractCard card){card.cost+=am;card.costForTurn+=am;}
public boolean removeAtEndOfTurn(AbstractCard card){return key==1;}
public boolean removeOnCardPlayed(AbstractCard card){return key==2;}
public String modifyDescription(String rawDescription,AbstractCard card){return "Test";}}