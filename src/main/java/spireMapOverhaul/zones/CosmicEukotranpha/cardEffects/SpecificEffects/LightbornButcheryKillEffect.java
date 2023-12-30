package spireMapOverhaul.zones.CosmicEukotranpha.cardEffects.SpecificEffects;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.DexterityPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import spireMapOverhaul.zones.CosmicEukotranpha.cardEffects.CosmicZoneAbstractGameAction;
import spireMapOverhaul.zones.CosmicEukotranpha.cardEffects.LoseHPEffectCosmicZone;
public class LightbornButcheryKillEffect extends CosmicZoneAbstractGameAction{@Override public CosmicZoneAbstractGameAction makeCopy()
{return new LightbornButcheryKillEffect(target,info);}public DamageInfo info;
public LightbornButcheryKillEffect(AbstractCreature target,DamageInfo info){this.info=info;this.setValues(target,info);this.actionType=ActionType.DAMAGE;}
public void update(){if(target!=null){this.target.damage(this.info);
		if(this.target.isDying||this.target.currentHealth<=0){
			if(AbstractDungeon.player.hasPower("CosmicZone:Fascination")){
				poT(AbstractDungeon.player,AbstractDungeon.player,new DexterityPower(AbstractDungeon.player,2));poT(AbstractDungeon.player,AbstractDungeon.player,new StrengthPower(AbstractDungeon.player,2));
				att(new GainEnergyAction(2));att(new LoseHPEffectCosmicZone(AbstractDungeon.player,AbstractDungeon.player,2));
			}att(new DrawCardAction(5));}
		if(AbstractDungeon.getCurrRoom().monsters.areMonstersBasicallyDead()){AbstractDungeon.actionManager.clearPostCombatActions();}
	}this.isDone=true;}}