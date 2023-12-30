package spireMapOverhaul.zones.CosmicEukotranpha.cards.common.GroupA;import basemod.helpers.TooltipInfo;
import com.megacrit.cardcrawl.cards.curses.Decay;
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
public class Malicious extends BaseCard{public static String ID=SpireAnniversary6Mod.makeID(
				Malicious.class.getSimpleName());public
Malicious(){super(ID,1,SKILL,ENEMY,SPECIAL,COLORLESS);this.tags.add(CosmicZoneCosmicCard);}
public void use(AbstractPlayer p,AbstractMonster m){
	//Kick the Bucket
	//Foe loses the lower of (Debuffs they have, Absolute value or 20) hp. Fascinated: Foe loses (Debuffs they have, Absolute value)/4 hp. Add Decay to hand and deck
	loseHP(m,Math.min(getAbsDebuffAmount(m),20));if(fasc()){loseHP(m,getAbsDebuffAmount(m)/4);makeInHand(new Decay());makeInDeck(new Decay());}
}
public void upgrade(){if(!upgraded){upgradeName();initializeDescription();this.initializeTitle();}}}

//Too lazy for this
//0 cost. 2 D. Add 2 “Cruel Jinx”s to hand. Fascinated: Cycle up to 2
//Curse. Before your next turn start: The first time foe loses Debuff: They lose 7 hp. [] [] Exhaust. Turn end in hand: Lose 2 hp
