package spireMapOverhaul.zones.CosmicEukotranpha.cards.cardMods;
import basemod.abstracts.AbstractCardModifier;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import static spireMapOverhaul.zones.CosmicEukotranpha.util.CosmicShortcuts.pivotT;import spireMapOverhaul.SpireAnniversary6Mod;
public class ShuffleFirstTurnEndsInDPMod extends AbstractCardModifier{public static final String ID=SpireAnniversary6Mod.makeID(
				ShuffleFirstTurnEndsInDPMod.class.getSimpleName());public AbstractCardModifier makeCopy(){return new
				ShuffleFirstTurnEndsInDPMod(am);}public String identifier(AbstractCard card){return ID;}public int key=0;public int am=0;public int change=0;
public ShuffleFirstTurnEndsInDPMod(int amount){this.am=amount;}public ShuffleFirstTurnEndsInDPMod(int amount,int key){this.am=amount;this.key=key;}//Keys
public void atEndOfTurn(AbstractCard card,CardGroup group){if(AbstractDungeon.player.discardPile.contains(card)){pivotT(card);am--;}}
public boolean removeAtEndOfTurn(AbstractCard card){return am==0&&key!=-1;}
public String modifyDescription(String rawDescription,AbstractCard card){return "Test";}}