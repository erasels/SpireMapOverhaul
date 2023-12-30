package spireMapOverhaul.zones.CosmicEukotranpha;
import com.evacipated.cardcrawl.modthespire.lib.SpireEnum;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.RegenPower;
import spireMapOverhaul.abstracts.AbstractZone;
import spireMapOverhaul.zoneInterfaces.CombatModifyingZone;
import spireMapOverhaul.zoneInterfaces.EncounterModifyingZone;
import spireMapOverhaul.zoneInterfaces.RewardModifyingZone;
import spireMapOverhaul.zones.CosmicEukotranpha.monsters.*;
import spireMapOverhaul.zones.CosmicEukotranpha.powers.FascinationPower;

import java.util.Arrays;
import java.util.List;
import static spireMapOverhaul.zones.CosmicEukotranpha.util.CosmicShortcuts.atb;
import static spireMapOverhaul.zones.CosmicEukotranpha.util.CosmicShortcuts.monsterList;import com.badlogic.gdx.graphics.Color;
public class CosmicEukotranphaZone extends AbstractZone implements CombatModifyingZone,EncounterModifyingZone,RewardModifyingZone{public static final String ID=
				"CosmicEukotranphaZone";public
CosmicEukotranphaZone(){super(ID,Icons.MONSTER,Icons.EVENT,Icons.ELITE);this.width=2;
	this.height=5;}public AbstractZone copy(){return new
				CosmicEukotranphaZone();}public Color getColor(){return Color.NAVY.cpy();}
public boolean canSpawn(){return this.isAct(3);}
public void atPreBattle(){atb(new ApplyPowerAction(AbstractDungeon.player,AbstractDungeon.player,new RegenPower(AbstractDungeon.player,5)));
	for(AbstractMonster mo:monsterList()){atb(new ApplyPowerAction(mo,mo,new RegenPower(mo,5)));}}
public void atTurnStart(){if(GameActionManager.turn==5){atb(new ApplyPowerAction(AbstractDungeon.player,AbstractDungeon.player,new FascinationPower(AbstractDungeon.player,AbstractDungeon.player)));
	for(AbstractMonster mo:monsterList()){atb(new ApplyPowerAction(mo,mo,new FascinationPower(mo,mo)));}}}
public String getCombatText(){return "Combat start: All gain 5 Regen. Non-Cosmic foes gain Cosmic Power!!! (Not Implemented)[] Turn 6 start: All gain Fascination";}
public List<ZoneEncounter>getNormalEncounters(){return Arrays.asList(
				new ZoneEncounter(Claumissier.ID,3,()->new Claumissier()),
				new ZoneEncounter(Man.ID,3,()->new Man()),
				new ZoneEncounter(SaventStars.ID,3,()->new SaventStars()),
				new ZoneEncounter(SevenHandedTwinhead.ID,3,()->new SevenHandedTwinhead()));}//This won't be the final iteration spawning
public List<ZoneEncounter>getEliteEncounters(){return Arrays.asList(
				new ZoneEncounter(ConstellationNeck.ID,3,()->new ConstellationNeck()),
				new ZoneEncounter(HairClipApple.ID,3,()->new HairClipApple()));}


//Spawnings: First 3 monsters are either Savent or SevenHand. First Elite is usually ConstNeck. First monster is always Cosmic but after that is cosmic chances
//Event Spawnings: Cosmic chances (Astra, CColumn, Cube, DDevil, DITLight, FInv, Tightrope). Some are different
// Crystals and Unknown Voice are both Cosmic and not Cosmic. Monochrome always spawns if cosmic chances are 100 but has normal cosmic spawn rate. Pink Rain has higher chances the more Cosmic chances there is
//MForest: Forces MForest Combats and MForest Events no matter what

//Things may be different in the future based on my laziness
}
