/*package spireMapOverhaul.zones.CosmicEukotranpha.patches;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rooms.MonsterRoom;
import com.megacrit.cardcrawl.rooms.MonsterRoomElite;
import spireMapOverhaul.zones.CosmicEukotranpha.util.CosmicZoneGameActionHistory;
@SpirePatch(clz=MonsterRoomElite.class,method="onPlayerEntry")public class CosmicZoneMonsterEliteCosmicSpawningPatch{
    @SpirePrefixPatch public static void Prefix(MonsterRoom __this){CosmicZoneMod.logger.info("Patch: CosmicZoneMonsterEliteCosmicSpawningPatch triggered");
        if(AbstractDungeon.actNum==3){
            CosmicZoneGameActionHistory.monstersGenned++;
            if(__this.monsters!=null){
                CosmicZoneMod.logger.info("Patch: CosmicZoneMonsterEliteCosmicSpawningPatch ERROR: Monsters aren't null");
            }else{
                if(CosmicZoneGameActionHistory.cosmicMonstersMet==0){CosmicZoneGameActionHistory.cosmicMonstersMet++;CosmicZoneMod.logger.info("Patch: CosmicZoneMonsterEliteCosmicSpawningPatch first cosmic monster");
                    AbstractDungeon.monsterList.set(0,"CosmicZone:ConstellationNeck");//__this.monsters=new MonsterGroup(new ConstellationNeck());
                }else{int i=AbstractDungeon.aiRng.random(99);
                    if(i>CosmicZoneGameActionHistory.cosmicPercentage){CosmicZoneGameActionHistory.cosmicMonstersMet++;CosmicZoneMod.logger.info("Patch: CosmicZoneMonsterEliteCosmicSpawningPatch random roll success Roll:"+i+", CChances:"+CosmicZoneGameActionHistory.cosmicPercentage);
                        AbstractDungeon.monsterList.set(0,"CosmicZone:HairClipApple");//__this.monsters=new MonsterGroup(new HairClipApple());
                    }else{CosmicZoneMod.logger.info("Patch: CosmicZoneMonsterEliteCosmicSpawningPatch random roll failure Roll:"+i+", CChances:"+CosmicZoneGameActionHistory.cosmicPercentage);}
                }
            }
        }
    }
}
*/