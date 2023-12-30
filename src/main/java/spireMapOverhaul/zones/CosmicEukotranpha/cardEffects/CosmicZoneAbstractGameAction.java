package spireMapOverhaul.zones.CosmicEukotranpha.cardEffects;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;

import java.util.ArrayList;
public abstract class CosmicZoneAbstractGameAction extends AbstractGameAction{
    public static AbstractPlayer p=AbstractDungeon.player;
    public boolean sc=true;public AbstractCard card=null;public ArrayList<AbstractCard>cards=null;public CardGroup cardGroup=null;
    public CosmicZoneAbstractGameAction(){this(null);}public CosmicZoneAbstractGameAction(AbstractCard card){this.card=card;cards=null;cardGroup=null;}
    public abstract CosmicZoneAbstractGameAction makeCopy();public void setCard(AbstractCard card){this.card=card;}
    public void att(AbstractGameAction action){addToTop(action);}public void atbDontUseThisOften(AbstractGameAction action){addToBot(action);}

    public void poT(AbstractCreature t,AbstractCreature s,AbstractPower po){if(po.amount!=0){att(new ApplyPowerAction(t,s,po,po.amount,true));}}
    public void poTCanApplyZero(AbstractCreature t,AbstractCreature s,AbstractPower po){att(new ApplyPowerAction(t,s,po,po.amount,true));}
    public void dmg(AbstractCreature t,AbstractCreature s,int d){att(new DamageAction(t,new DamageInfo(s,d),AbstractGameAction.AttackEffect.NONE,true));}
    public void dmgAll(int d){att(new DamageAllEnemiesAction(null,DamageInfo.createDamageMatrix(d,true),DamageInfo.DamageType.THORNS,AbstractGameAction.AttackEffect.NONE,true));}
    public void blck(int b,AbstractCreature target){att(new GainBlockAction(target,target,b));}public void blck(int b,AbstractCreature target,AbstractCreature source){att(new GainBlockAction(target,source,b));}

}
