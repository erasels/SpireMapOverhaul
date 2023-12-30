package spireMapOverhaul.zones.CosmicEukotranpha.cardEffects.cardManip;
import basemod.abstracts.AbstractCardModifier;
import basemod.helpers.CardModifierManager;
import com.megacrit.cardcrawl.cards.AbstractCard;
import spireMapOverhaul.zones.CosmicEukotranpha.cardEffects.CosmicZoneAbstractGameAction;
public class CZAddModTEffect extends CosmicZoneAbstractGameAction{public AbstractCardModifier mod;
public CZAddModTEffect(AbstractCard c,AbstractCardModifier mod){this.card=c;this.mod=mod;}public CosmicZoneAbstractGameAction makeCopy(){return new CZAddModTEffect(this.card,mod);}
public void update(){if(card==null||mod==null){this.isDone=true;return;}CardModifierManager.addModifier(card,mod);this.isDone=true;}}