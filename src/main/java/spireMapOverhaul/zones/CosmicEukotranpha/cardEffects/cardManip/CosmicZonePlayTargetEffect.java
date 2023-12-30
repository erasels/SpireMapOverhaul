package spireMapOverhaul.zones.CosmicEukotranpha.cardEffects.cardManip;
import com.megacrit.cardcrawl.actions.utility.NewQueueCardAction;
import com.megacrit.cardcrawl.actions.utility.UnlimboAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import spireMapOverhaul.zones.CosmicEukotranpha.cardEffects.CosmicZoneAbstractGameAction;
import spireMapOverhaul.zones.CosmicEukotranpha.util.CZGetAll;
public class CosmicZonePlayTargetEffect extends CosmicZoneAbstractGameAction{
    public CosmicZonePlayTargetEffect(AbstractCard c,AbstractCreature target){this.card=c;this.target=target;}
    public CosmicZoneAbstractGameAction makeCopy(){
        return new CosmicZonePlayTargetEffect(card,target);}
    public void update(){if(card==null){this.isDone=true;return;}
        if(CZGetAll.get().contains(card)){
            if(p.cardInUse==card){p.cardInUse=null;}
            if(p.drawPile.contains(card)){p.drawPile.removeCard(card);}
            if(p.discardPile.contains(card)){p.discardPile.removeCard(card);}
            if(p.exhaustPile.contains(card)){p.exhaustPile.removeCard(card);}
            if(p.limbo.contains(card)){p.limbo.removeCard(card);}
            if(p.hand.contains(card)){p.hand.removeCard(card);}
            AbstractDungeon.getCurrRoom().souls.remove(card);AbstractDungeon.player.limbo.group.add(card);
            card.current_y=-200.0F*Settings.scale;card.target_x=(float)Settings.WIDTH/2.0F+200.0F*Settings.xScale;card.target_y=(float)Settings.HEIGHT/2.0F;card.targetAngle=0.0F;card.lighten(false);card.drawScale=0.12F;card.targetDrawScale=0.75F;
            card.applyPowers();att(new NewQueueCardAction(card,this.target,false,true));att(new UnlimboAction(card));}
        this.isDone=true;}}
