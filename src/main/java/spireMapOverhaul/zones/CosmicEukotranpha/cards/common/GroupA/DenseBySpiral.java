package spireMapOverhaul.zones.CosmicEukotranpha.cards.common.GroupA;import com.megacrit.cardcrawl.actions.defect.ChannelAction;
import com.megacrit.cardcrawl.cards.curses.Decay;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.LoseStrengthPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import spireMapOverhaul.SpireAnniversary6Mod;
import spireMapOverhaul.zones.CosmicEukotranpha.cards.BaseCard;
import spireMapOverhaul.zones.CosmicEukotranpha.orbs.ColdOrb;

import static com.megacrit.cardcrawl.cards.AbstractCard.CardColor.COLORLESS;
import static com.megacrit.cardcrawl.cards.AbstractCard.CardRarity.SPECIAL;
import static com.megacrit.cardcrawl.cards.AbstractCard.CardTarget.ALL_ENEMY;
import static com.megacrit.cardcrawl.cards.AbstractCard.CardTarget.ENEMY;
import static com.megacrit.cardcrawl.cards.AbstractCard.CardType.ATTACK;
import static com.megacrit.cardcrawl.cards.AbstractCard.CardType.SKILL;
import static spireMapOverhaul.zones.CosmicEukotranpha.CosmicZoneMod.CosmicZoneCosmicCard;
import static spireMapOverhaul.zones.CosmicEukotranpha.util.CosmicShortcuts.*;
import static spireMapOverhaul.zones.CosmicEukotranpha.util.CosmicShortcuts.makeInDeck;
public class DenseBySpiral extends BaseCard{public static String ID=SpireAnniversary6Mod.makeID(
				DenseBySpiral.class.getSimpleName());public
DenseBySpiral(){super(ID,1,ATTACK,ALL_ENEMY,SPECIAL,COLORLESS);this.tags.add(CosmicZoneCosmicCard);
				baseDamage=7;}
public void use(AbstractPlayer p,AbstractMonster m){
	//7 D to attacking foes. Channel Cold. Fascinated: Gain 2 Momentary Str
	//Cold: Turn start: Lose 2 Momentary Str. Gain 2 Momentary Dex. Evoke: Lose 3 Momentary Dex. Gain 3 Momentary Str
	for(AbstractMonster mo:monsterList()){if(mo.getIntentBaseDmg()>0){dmg(mo);}}atb(new ChannelAction(new ColdOrb()));if(fasc()){poT(AbstractDungeon.player,AbstractDungeon.player,new StrengthPower(AbstractDungeon.player,2));poT(AbstractDungeon.player,AbstractDungeon.player,new LoseStrengthPower(AbstractDungeon.player,2));}
}
public void upgrade(){if(!upgraded){upgradeName();initializeDescription();this.initializeTitle();}}}
