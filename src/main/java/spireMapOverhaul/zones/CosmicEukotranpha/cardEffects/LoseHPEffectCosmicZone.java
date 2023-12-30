package spireMapOverhaul.zones.CosmicEukotranpha.cardEffects;

import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.DamageInfo.DamageType;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect;
public class LoseHPEffectCosmicZone extends CosmicZoneAbstractGameAction{
    public LoseHPEffectCosmicZone(AbstractCreature target,AbstractCreature source,int amount){this(target,source,amount,AttackEffect.NONE);}
    public LoseHPEffectCosmicZone(AbstractCreature target,AbstractCreature source,int amount,AttackEffect effect){this.setValues(target,source,amount);this.actionType=ActionType.DAMAGE;this.attackEffect=effect;}
    @Override public CosmicZoneAbstractGameAction makeCopy(){return new LoseHPEffectCosmicZone(target,source,amount,attackEffect);}
    public void update(){if(attackEffect!=AttackEffect.NONE){AbstractDungeon.effectList.add(new FlashAtkImgEffect(this.target.hb.cX,this.target.hb.cY,this.attackEffect));}
        this.target.damage(new DamageInfo(this.source,this.amount,DamageType.HP_LOSS));
        if(AbstractDungeon.getCurrRoom().monsters.areMonstersBasicallyDead()){AbstractDungeon.actionManager.clearPostCombatActions();}
        this.isDone=true;}}
