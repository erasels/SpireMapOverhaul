package spireMapOverhaul.zones.CosmicEukotranpha.cards.cardMods;
import basemod.abstracts.AbstractCardModifier;
import com.evacipated.cardcrawl.mod.stslib.fields.cards.AbstractCard.GraveField;
import com.megacrit.cardcrawl.cards.AbstractCard;import spireMapOverhaul.SpireAnniversary6Mod;
public class GraveMod extends AbstractCardModifier{public static final String ID=SpireAnniversary6Mod.makeID(
				GraveMod.class.getSimpleName());public AbstractCardModifier makeCopy(){return new
				GraveMod(am);}public String identifier(AbstractCard card){return ID;}public int key=0;public int am=0;public int change=0;
public GraveMod(){}public GraveMod(int key){this.key=key;}//Keys 1=Don't remove when removed
public void onInitialApplication(AbstractCard card){if(GraveField.grave.get(card)){key=1;}else{GraveField.grave.set(card,true);}}
public void onRemove(AbstractCard card){if(key!=1){GraveField.grave.set(card,false);}}
public String modifyDescription(String rawDescription,AbstractCard card){return "Test";}}