package spireMapOverhaul.zones.CosmicEukotranpha.cards.cardMods;
import basemod.abstracts.AbstractCardModifier;
import basemod.interfaces.AlternateCardCostModifier;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import spireMapOverhaul.SpireAnniversary6Mod;

import static spireMapOverhaul.zones.CosmicEukotranpha.util.CosmicShortcuts.loseHP;
public class AltCostHPMod extends AbstractCardModifier implements AlternateCardCostModifier{public static final String ID=SpireAnniversary6Mod.makeID(
				AltCostHPMod.class.getSimpleName());public AbstractCardModifier makeCopy(){return new
				AltCostHPMod();}public String identifier(AbstractCard card){return ID;}public int key=0;
public AltCostHPMod(){}public AltCostHPMod(int key){this.key=key;}//Keys 1=Removed at turn end
public boolean removeAtEndOfTurn(AbstractCard card){return super.removeAtEndOfTurn(card)||key==1;}
public String modifyDescription(String rawDescription,AbstractCard card){return "Test";}
public int getAlternateResource(AbstractCard card){return AbstractDungeon.player.currentHealth;}public boolean canSplitCost(AbstractCard card){return true;}
public int spendAlternateCost(AbstractCard card,int costToSpend){
	if(AbstractDungeon.player.currentHealth>costToSpend){loseHP(AbstractDungeon.player,costToSpend);costToSpend=0;
	}else if(AbstractDungeon.player.currentHealth>0){loseHP(AbstractDungeon.player,AbstractDungeon.player.currentHealth);costToSpend-=AbstractDungeon.player.currentHealth;
	}return costToSpend;}
}