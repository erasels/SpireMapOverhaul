package spireMapOverhaul.zones.CosmicEukotranpha.cardEffects;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect;

import static spireMapOverhaul.zones.CosmicEukotranpha.util.CosmicShortcuts.loggeer;
public class CosmicZoneDamageAction extends CosmicZoneAbstractGameAction{@Override public CosmicZoneAbstractGameAction makeCopy(){return
        new CosmicZoneDamageAction(target,info,attackEffect,muteSfx,dur);}public DamageInfo info;public float dur=0.01F;public float recDur=0.01F;public boolean muteSfx;public int w=0;
    public CosmicZoneDamageAction(AbstractCreature target,DamageInfo info){this(target,info,AttackEffect.NONE,true,0.01F);}public CosmicZoneDamageAction(AbstractCreature target,DamageInfo info,AbstractGameAction.AttackEffect effect){this(target,info,effect,true,0.01F);}
    public CosmicZoneDamageAction(AbstractCreature target,DamageInfo info,AbstractGameAction.AttackEffect effect,boolean muteSfx){this(target,info,effect,muteSfx,0.01F);}
    public CosmicZoneDamageAction(AbstractCreature target,DamageInfo info,AbstractGameAction.AttackEffect effect,boolean muteSfx,float dur){this.actionType=ActionType.DAMAGE;this.info=info;this.target=target;this.source=info.owner;this.amount=info.output;this.attackEffect=effect;this.muteSfx=muteSfx;this.duration=this.dur=dur;}
    public void update(){loggeer("Cosmic Zone Mod update");if(this.shouldCancelAction()&&this.info.type!=DamageInfo.DamageType.THORNS){loggeer("Cosmic Zone Mod cancel");
        loggeer("Cosmic Zone Mod target "+target.name);
        loggeer("Cosmic Zone Mod source "+source.name);
        loggeer("Cosmic Zone Mod source is dying "+source.isDying);
        loggeer("Cosmic Zone Mod target is dead or escaped "+target.isDeadOrEscaped());
        this.isDone=true;return;}

        //Cause:
        //java.lang.NullPointerException
        //	at basicmod.cardEffects.CosmicZoneDamageAction.update(CosmicZoneDamageAction.java:21)

        //return this.target == null || this.source != null && this.source.isDying || this.target.isDeadOrEscaped();
        if(sc){loggeer("sc");sc=false;if(this.info.type!=DamageInfo.DamageType.THORNS&&(this.info.owner.isDying||this.info.owner.halfDead)){this.isDone=true;return;}
            AbstractDungeon.effectList.add(new FlashAtkImgEffect(this.target.hb.cX, this.target.hb.cY, this.attackEffect, this.muteSfx));
            /*if(this.goldAmount!=0){this.stealGold();}*/
        }else{loggeer("else");if(this.attackEffect==AttackEffect.POISON){this.target.tint.color.set(Color.CHARTREUSE.cpy());this.target.tint.changeColor(Color.WHITE.cpy());
            }else if(this.attackEffect==AttackEffect.FIRE){this.target.tint.color.set(Color.RED);this.target.tint.changeColor(Color.WHITE.cpy());}
            this.target.damage(this.info);if(AbstractDungeon.getCurrRoom().monsters.areMonstersBasicallyDead()){AbstractDungeon.actionManager.clearPostCombatActions();}
            this.isDone=true;
        }
    }
}