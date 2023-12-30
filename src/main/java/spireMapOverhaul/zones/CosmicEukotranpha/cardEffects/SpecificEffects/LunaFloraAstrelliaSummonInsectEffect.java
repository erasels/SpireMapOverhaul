package spireMapOverhaul.zones.CosmicEukotranpha.cardEffects.SpecificEffects;


import com.badlogic.gdx.math.Interpolation;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ModHelper;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.MinionPower;
import com.megacrit.cardcrawl.powers.SlowPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import spireMapOverhaul.zones.CosmicEukotranpha.CosmicZoneMod;
import spireMapOverhaul.zones.CosmicEukotranpha.cardEffects.CosmicZoneAbstractGameAction;
import spireMapOverhaul.zones.CosmicEukotranpha.monsters.*;

import java.util.Iterator;

import static spireMapOverhaul.zones.CosmicEukotranpha.util.CosmicZoneGameActionHistory.lunaFloraAstrelliaInsectsSummoned;

public class LunaFloraAstrelliaSummonInsectEffect extends CosmicZoneAbstractGameAction{@Override public CosmicZoneAbstractGameAction makeCopy()
{return new LunaFloraAstrelliaSummonInsectEffect(insects);}private AbstractMonster m;AbstractMonster[]insects;
    public LunaFloraAstrelliaSummonInsectEffect(AbstractMonster[]gremlins){this.actionType=ActionType.SPECIAL;int slot=this.identifySlot(gremlins);insects=gremlins;
        if(slot>5){CosmicZoneMod.logger.info("LunaFloraAstrelliaSummonInsectEffect 6 insects");this.isDone=true;return;}//todo: Will this work?
        if(slot==-1){CosmicZoneMod.logger.info("LunaFloraAstrelliaSummonInsectEffect ERROR");
        }else{CosmicZoneMod.logger.info("LunaFloraAstrelliaSummonInsectEffect lunaFloraAstrelliaInsectsSummoned%8="+(lunaFloraAstrelliaInsectsSummoned%8));switch(lunaFloraAstrelliaInsectsSummoned%8){
                case 0:m=new InsectHydoail(LunaFloraAstrellia.xPos[slot],LunaFloraAstrellia.yPos[slot]);break;
                case 1:m=new InsectGoldire(LunaFloraAstrellia.xPos[slot],LunaFloraAstrellia.yPos[slot]);break;
                case 2:m=new InsectTerniff(LunaFloraAstrellia.xPos[slot],LunaFloraAstrellia.yPos[slot]);break;
                case 3:m=new InsectPyergy(LunaFloraAstrellia.xPos[slot],LunaFloraAstrellia.yPos[slot]);break;
                case 4:m=new InsectBarceq(LunaFloraAstrellia.xPos[slot],LunaFloraAstrellia.yPos[slot]);break;
                case 5:m=new InsectDrittu(LunaFloraAstrellia.xPos[slot],LunaFloraAstrellia.yPos[slot]);break;
                case 6:m=new InsectHeazath(LunaFloraAstrellia.xPos[slot],LunaFloraAstrellia.yPos[slot]);break;
                case 7:m=new InsectOcianyvy(LunaFloraAstrellia.xPos[slot],LunaFloraAstrellia.yPos[slot]);break;}
            lunaFloraAstrelliaInsectsSummoned++;gremlins[slot]=this.m;
            for(AbstractRelic r:p.relics){r.onSpawnMonster(this.m);}
        }}
    private int identifySlot(AbstractMonster[]gremlins){for(int i=0;i<gremlins.length;++i){if(gremlins[i]==null||gremlins[i].isDying){return i;}}return-1;}
    public void update(){if(sc){sc=false;if(m==null){CosmicZoneMod.logger.info("LunaFloraAstrelliaSummonInsectEffect m is null");this.isDone=true;return;}
     this.m.animX=1200.0F*Settings.xScale;this.m.init();this.m.applyPowers();
            AbstractDungeon.getCurrRoom().monsters.addMonster(this.getSmartPosition(),this.m);
            if(ModHelper.isModEnabled("Lethality")){this.addToBot(new ApplyPowerAction(this.m,this.m,new StrengthPower(this.m,3),3));}
            if(ModHelper.isModEnabled("Time Dilation")){this.addToBot(new ApplyPowerAction(this.m,this.m,new SlowPower(this.m,0)));}
            this.addToBot(new ApplyPowerAction(this.m,this.m,new MinionPower(this.m)));
        }else{this.m.animX=Interpolation.fade.apply(0.0F,1200.0F*Settings.xScale,this.duration);
            this.tickDuration();if(this.isDone){this.m.animX=0.0F;this.m.showHealthBar();this.m.usePreBattleAction();}
        }}
    private int getSmartPosition(){int position=0;
        for(Iterator var2=AbstractDungeon.getCurrRoom().monsters.monsters.iterator();var2.hasNext();++position){
            AbstractMonster mo=(AbstractMonster)var2.next();if(!(this.m.drawX>mo.drawX)){break;}
        }return position;}
}

/*
private AbstractMonster getRandomGremlin(int slot){
        ArrayList<String>pool=new ArrayList();
        pool.add("CosmicZone:Insect");
        pool.add("GremlinWarrior");
        pool.add("GremlinThief");
        pool.add("GremlinThief");
        pool.add("GremlinFat");
        pool.add("GremlinFat");
        pool.add("GremlinTsundere");
        pool.add("GremlinWizard");
        float x;
        float y;
        switch (slot) {
            case 0:
                x = GremlinLeader.POSX[0];
                y = GremlinLeader.POSY[0];
                break;
            case 1:
                x = GremlinLeader.POSX[1];
                y = GremlinLeader.POSY[1];
                break;
            case 2:
                x = GremlinLeader.POSX[2];
                y = GremlinLeader.POSY[2];
                break;
            default:
                x = GremlinLeader.POSX[0];
                y = GremlinLeader.POSY[0];
        }

        return MonsterHelper.getGremlin((String)pool.get(AbstractDungeon.aiRng.random(0, pool.size() - 1)), x, y);
    }
 */