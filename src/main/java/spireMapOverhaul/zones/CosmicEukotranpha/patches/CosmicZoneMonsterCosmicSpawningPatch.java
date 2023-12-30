/*package spireMapOverhaul.zones.CosmicEukotranpha.patches;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rooms.MonsterRoom;
import spireMapOverhaul.zones.CosmicEukotranpha.util.CosmicZoneGameActionHistory;import spireMapOverhaul.SpireAnniversary6Mod;import spireMapOverhaul.zones.CosmicEukotranpha.CosmicZoneMod;
@SpirePatch(clz=MonsterRoom.class,method="onPlayerEntry")public class CosmicZoneMonsterCosmicSpawningPatch{
    @SpirePrefixPatch public static void Prefix(MonsterRoom __this){CosmicZoneMod.logger.info("Patch: CosmicZoneMonsterCosmicSpawningPatch triggered");
        if(AbstractDungeon.actNum==3){
            CosmicZoneGameActionHistory.monstersGenned++;
            if(__this.monsters!=null){
                CosmicZoneMod.logger.info("Patch: CosmicZoneMonsterCosmicSpawningPatch ERROR: Monsters aren't null");
            }else{
                if(CosmicZoneGameActionHistory.cosmicMonstersMet==0){CosmicZoneGameActionHistory.cosmicMonstersMet++;CosmicZoneMod.logger.info("Patch: CosmicZoneMonsterCosmicSpawningPatch first cosmic monster");
                    if(CosmicZoneGameActionHistory.monstersGenned<4){
                        AbstractDungeon.monsterList.set(0,"CosmicZone:SaventStars");
                        //__this.monsters=new MonsterGroup(new SaventStars());
                    }else{AbstractDungeon.monsterList.set(0,"CosmicZone:Claumissier");
                        //__this.monsters=new MonsterGroup(new Claumissier());
                    }
                }else{int i=AbstractDungeon.aiRng.random(99);
                    if(i>CosmicZoneGameActionHistory.cosmicPercentage){CosmicZoneGameActionHistory.cosmicMonstersMet++;CosmicZoneMod.logger.info("Patch: CosmicZoneMonsterCosmicSpawningPatch random roll success Roll:"+i+", CChances:"+CosmicZoneGameActionHistory.cosmicPercentage);
                        if(CosmicZoneGameActionHistory.monstersGenned<4){
                            AbstractDungeon.monsterList.set(0,"CosmicZone:SevenHandedTwinhead");
                            //__this.monsters=new MonsterGroup(new SevenHandedTwinhead());
                        }else{
                            AbstractDungeon.monsterList.set(0,"CosmicZone:Man");
                           // __this.monsters=new MonsterGroup(new Man());
                        }//TODO: Change spawning monsters, A: Once all are met it becomes less likely to find Cosmic Monsters but Monochrome Rift is more likely and stuff
                    }else{CosmicZoneMod.logger.info("Patch: CosmicZoneMonsterCosmicSpawningPatch random roll failure Roll:"+i+", CChances:"+CosmicZoneGameActionHistory.cosmicPercentage);}
                }
            }
        }
    }
}
*/