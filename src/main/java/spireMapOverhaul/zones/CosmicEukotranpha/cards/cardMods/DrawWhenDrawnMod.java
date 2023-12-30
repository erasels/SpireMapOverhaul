package spireMapOverhaul.zones.CosmicEukotranpha.cards.cardMods;
import basemod.abstracts.AbstractCardModifier;
import com.megacrit.cardcrawl.cards.AbstractCard;

import static spireMapOverhaul.zones.CosmicEukotranpha.util.CosmicShortcuts.draw;import spireMapOverhaul.SpireAnniversary6Mod;
public class DrawWhenDrawnMod extends AbstractCardModifier{public static final String ID=SpireAnniversary6Mod.makeID(
				DrawWhenDrawnMod.class.getSimpleName());public AbstractCardModifier makeCopy(){return new
				DrawWhenDrawnMod(am,key);}public String identifier(AbstractCard card){return ID;}public int key=0;public int am=0;public int change=0;
public DrawWhenDrawnMod(int am){this.am=am;}public DrawWhenDrawnMod(int am,int key){this.am=am;this.key=key;}//Keys
public void onDrawn(AbstractCard card){draw(1);}
public String modifyDescription(String rawDescription,AbstractCard card){return "Test";}}