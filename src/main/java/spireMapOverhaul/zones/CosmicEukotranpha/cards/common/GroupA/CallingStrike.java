package spireMapOverhaul.zones.CosmicEukotranpha.cards.common.GroupA;import basemod.helpers.TooltipInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import spireMapOverhaul.SpireAnniversary6Mod;
import spireMapOverhaul.zones.CosmicEukotranpha.cardEffects.SomewhatGeneric.AddOldestInHoroscopeToDeckEffect;
import spireMapOverhaul.zones.CosmicEukotranpha.cards.BaseCard;
import spireMapOverhaul.zones.CosmicEukotranpha.util.CosmicZoneGameActionHistory;

import java.util.ArrayList;
import java.util.List;

import static com.megacrit.cardcrawl.cards.AbstractCard.CardColor.COLORLESS;
import static com.megacrit.cardcrawl.cards.AbstractCard.CardRarity.SPECIAL;
import static com.megacrit.cardcrawl.cards.AbstractCard.CardTarget.ENEMY;
import static com.megacrit.cardcrawl.cards.AbstractCard.CardTarget.SELF;
import static com.megacrit.cardcrawl.cards.AbstractCard.CardType.ATTACK;
import static com.megacrit.cardcrawl.cards.AbstractCard.CardType.SKILL;
import static spireMapOverhaul.zones.CosmicEukotranpha.CosmicZoneMod.CosmicZoneCosmicCard;
import static spireMapOverhaul.zones.CosmicEukotranpha.util.CosmicShortcuts.*;
public class CallingStrike extends BaseCard{public static String ID=SpireAnniversary6Mod.makeID(
				CallingStrike.class.getSimpleName());public
CallingStrike(){super(ID,1,ATTACK,ENEMY,SPECIAL,COLORLESS);this.tags.add(CosmicZoneCosmicCard);}
public void use(AbstractPlayer p,AbstractMonster m){
	//11 D. Add oldest in Horoscope to deck. Fascinated: Deal +4 D. Lose (Non-Atks on hand) hp
	dmg(m);atb(new AddOldestInHoroscopeToDeckEffect());if(fasc()){loseHP(p,nonAtksInGroup(p.hand));}
}
public List<TooltipInfo>getCustomTooltipsTop(){if(kTips==null){kTips=new ArrayList<>();
	if(!CosmicZoneGameActionHistory.horoscope.isEmpty()&&CosmicZoneGameActionHistory.horoscopeDoThings>0){
		String dimt=getChaS("Horoscope",1,0)+CosmicZoneGameActionHistory.horoscope.group.get(0)+getChaS("Horoscope",1,1)+CosmicZoneGameActionHistory.horoscope.group.get(1)+getChaS("Horoscope",1,2)+CosmicZoneGameActionHistory.horoscope.group.get(2)+getChaS("Horoscope",1,3)+CosmicZoneGameActionHistory.horoscope.group.get(3);
		kTips.add(new TooltipInfo(getChaS("Horoscope",0,0),dimt));}else{
		String dimt=getChaS("Horoscope",1,4);kTips.add(new TooltipInfo(getChaS("Horoscope",0,0),dimt));}}
	List<TooltipInfo>compoundList=new ArrayList<>(kTips);if(super.getCustomTooltipsTop()!=null){compoundList.addAll(super.getCustomTooltipsTop());}
	return compoundList;}
public void applyPowers(){if(fasc()){preCalcDmgInc(4);}super.applyPowers();if(fasc()){postCalcDmgInc();}initializeDescription();}
public void upgrade(){if(!upgraded){upgradeName();initializeDescription();this.initializeTitle();}}}

