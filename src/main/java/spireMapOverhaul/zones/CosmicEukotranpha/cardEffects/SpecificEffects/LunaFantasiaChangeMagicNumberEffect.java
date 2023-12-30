package spireMapOverhaul.zones.CosmicEukotranpha.cardEffects.SpecificEffects;
import com.megacrit.cardcrawl.cards.AbstractCard;
import spireMapOverhaul.zones.CosmicEukotranpha.cardEffects.CosmicZoneAbstractGameAction;
import spireMapOverhaul.zones.CosmicEukotranpha.cards.special.LunaFantasia;
public class LunaFantasiaChangeMagicNumberEffect extends CosmicZoneAbstractGameAction{@Override public CosmicZoneAbstractGameAction makeCopy()
{return new LunaFantasiaChangeMagicNumberEffect();}public boolean retrieveCard=false;
    public LunaFantasiaChangeMagicNumberEffect(){}
    public void update(){if(p.cardInUse instanceof LunaFantasia){p.cardInUse.magicNumber=1;p.cardInUse.applyPowers();}
        for(AbstractCard c:p.drawPile.group){if(c instanceof LunaFantasia){c.magicNumber=1;c.applyPowers();}}
        for(AbstractCard c:p.hand.group){if(c instanceof LunaFantasia){c.magicNumber=1;c.applyPowers();}}
        for(AbstractCard c:p.discardPile.group){if(c instanceof LunaFantasia){c.magicNumber=1;c.applyPowers();}}
        for(AbstractCard c:p.exhaustPile.group){if(c instanceof LunaFantasia){c.magicNumber=1;c.applyPowers();}}
        for(AbstractCard c:p.limbo.group){if(c instanceof LunaFantasia){c.magicNumber=1;c.applyPowers();}}
        this.isDone=true;}}