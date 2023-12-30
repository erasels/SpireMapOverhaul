package spireMapOverhaul.zones.CosmicEukotranpha.cardEffects.cardManip;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.vfx.cardManip.ExhaustCardEffect;
import spireMapOverhaul.zones.CosmicEukotranpha.cardEffects.CosmicZoneAbstractGameAction;
import spireMapOverhaul.zones.CosmicEukotranpha.util.CZGetAll;
public class CZExhaustTEffect extends CosmicZoneAbstractGameAction{
    public CZExhaustTEffect(AbstractCard c){this.card=c;}public CosmicZoneAbstractGameAction makeCopy(){return new CZExhaustTEffect(this.card);}
    public void update(){if(card==null){this.isDone=true;return;}
        if(CZGetAll.get().contains(card)){
            //if(p.exhaustPile.contains(card)){p.exhaustPile.moveToExhaustPile(card);}
            if(p.cardInUse==card){for(AbstractRelic r:p.relics){r.onExhaust(card);}for(AbstractPower po:p.powers){po.onExhaust(card);}
                card.triggerOnExhaust();if(AbstractDungeon.player.hoveredCard==card){AbstractDungeon.player.releaseCard();}
                AbstractDungeon.actionManager.removeFromQueue(card);card.unhover();card.untip();card.stopGlowing();p.cardInUse=null;
                AbstractDungeon.effectList.add(new ExhaustCardEffect(card));AbstractDungeon.player.exhaustPile.addToTop(card);AbstractDungeon.player.onCardDrawOrDiscard();}
            if(p.drawPile.contains(card)){p.drawPile.moveToExhaustPile(card);for(AbstractRelic r:p.relics){r.onExhaust(card);}for(AbstractPower po:p.powers){po.onExhaust(card);}}
            if(p.discardPile.contains(card)){p.discardPile.moveToExhaustPile(card);for(AbstractRelic r:p.relics){r.onExhaust(card);}for(AbstractPower po:p.powers){po.onExhaust(card);}}
            if(p.limbo.contains(card)){p.limbo.moveToExhaustPile(card);for(AbstractRelic r:p.relics){r.onExhaust(card);}for(AbstractPower po:p.powers){po.onExhaust(card);}}
            if(p.hand.contains(card)){p.hand.moveToExhaustPile(card);for(AbstractRelic r:p.relics){r.onExhaust(card);}for(AbstractPower po:p.powers){po.onExhaust(card);}}this.isDone=true;}}}