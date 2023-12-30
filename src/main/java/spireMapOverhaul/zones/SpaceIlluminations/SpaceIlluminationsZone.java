package spireMapOverhaul.zones.SpaceIlluminations;import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.RegenPower;
import spireMapOverhaul.abstracts.AbstractZone;

import spireMapOverhaul.zoneInterfaces.CombatModifyingZone;

import static spireMapOverhaul.zones.CosmicEukotranpha.util.CosmicShortcuts.atb;
import static spireMapOverhaul.zones.CosmicEukotranpha.util.CosmicShortcuts.monsterList;import spireMapOverhaul.zones.CosmicEukotranpha.powers.FascinationPower;
public class SpaceIlluminationsZone extends AbstractZone implements CombatModifyingZone{public static final String ID=
				"SpaceIlluminationsZone";public
SpaceIlluminationsZone(){super(ID);}public AbstractZone copy(){return new
				SpaceIlluminationsZone();}public Color getColor(){return Color.NAVY.cpy();}
public void atPreBattle(){atb(new ApplyPowerAction(AbstractDungeon.player,AbstractDungeon.player,new RegenPower(AbstractDungeon.player,5)));
	for(AbstractMonster mo:monsterList()){atb(new ApplyPowerAction(mo,mo,new RegenPower(mo,5)));}}
public void atTurnStart(){if(GameActionManager.turn==5){atb(new ApplyPowerAction(AbstractDungeon.player,AbstractDungeon.player,new FascinationPower(AbstractDungeon.player,AbstractDungeon.player)));
	for(AbstractMonster mo:monsterList()){atb(new ApplyPowerAction(mo,mo,new FascinationPower(mo,mo)));}}}
public String getCombatText(){return "Combat start: All gain 5 Regen. Non-Cosmic foes gain Cosmic Power!!! (Not Implemented) [] Turn 6 start: All gain Fascination";}

//Spawnings: Events are Crystals, Cube of Reflections, Tightrope of Deserts, Unknown Voice
//Most wacky cards are banned
}
