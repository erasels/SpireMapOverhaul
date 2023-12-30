package spireMapOverhaul.zones.CosmicEukotranpha.cardEffects.SpecificEffects;
import com.badlogic.gdx.math.Interpolation;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ModHelper;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.SlowPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import spireMapOverhaul.zones.CosmicEukotranpha.CosmicZoneMod;
import spireMapOverhaul.zones.CosmicEukotranpha.cardEffects.CosmicZoneAbstractGameAction;
import spireMapOverhaul.zones.CosmicEukotranpha.monsters.LunaFloraAstrellia;
import spireMapOverhaul.zones.CosmicEukotranpha.monsters.Queen;

import java.util.Iterator;
public class LunaFloraAstrelliaSummonQueenEffect extends CosmicZoneAbstractGameAction{@Override public CosmicZoneAbstractGameAction makeCopy()
{return new LunaFloraAstrelliaSummonQueenEffect(insects);}private AbstractMonster m;AbstractMonster[]insects;
    public LunaFloraAstrelliaSummonQueenEffect(AbstractMonster[]gremlins){this.actionType=ActionType.SPECIAL;int slot=6;insects=gremlins;
        if(slot==-1){CosmicZoneMod.logger.info("LunaFloraAstrelliaSummonInsectEffect ERROR");
        }else{m=new Queen(LunaFloraAstrellia.xPos[6],LunaFloraAstrellia.yPos[6]);
            gremlins[6]=this.m;for(AbstractRelic r:p.relics){r.onSpawnMonster(this.m);}
        }}
    public void update(){if(sc){sc=false;this.m.animX=1200.0F*Settings.xScale;this.m.init();this.m.applyPowers();
        AbstractDungeon.getCurrRoom().monsters.addMonster(this.getSmartPosition(),this.m);
        if(ModHelper.isModEnabled("Lethality")){this.addToBot(new ApplyPowerAction(this.m,this.m,new StrengthPower(this.m,3),3));}
        if(ModHelper.isModEnabled("Time Dilation")){this.addToBot(new ApplyPowerAction(this.m,this.m,new SlowPower(this.m,0)));}
    }else{this.m.animX=Interpolation.fade.apply(0.0F,1200.0F*Settings.xScale,this.duration);
        this.tickDuration();if(this.isDone){this.m.animX=0.0F;this.m.showHealthBar();this.m.usePreBattleAction();}
    }}
    private int getSmartPosition(){int position=0;
        for(Iterator var2=AbstractDungeon.getCurrRoom().monsters.monsters.iterator();var2.hasNext();++position){
            AbstractMonster mo=(AbstractMonster)var2.next();if(!(this.m.drawX>mo.drawX)){break;}
        }return position;}
}