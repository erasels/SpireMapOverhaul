package spireMapOverhaul.zones.CosmicEukotranpha.cards.special;
import basemod.patches.com.megacrit.cardcrawl.cards.AbstractCard.MultiCardPreview;
import com.evacipated.cardcrawl.modthespire.lib.SpireField;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.blue.Strike_Blue;
import com.megacrit.cardcrawl.cards.green.Strike_Green;
import com.megacrit.cardcrawl.cards.purple.Strike_Purple;
import com.megacrit.cardcrawl.cards.red.Strike_Red;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import spireMapOverhaul.zones.CosmicEukotranpha.cardEffects.SpecificEffects.BCheckDToFoesEffect;
import spireMapOverhaul.zones.CosmicEukotranpha.cards.BaseCard;

import static com.megacrit.cardcrawl.cards.AbstractCard.CardColor.COLORLESS;
import static com.megacrit.cardcrawl.cards.AbstractCard.CardRarity.SPECIAL;
import static com.megacrit.cardcrawl.cards.AbstractCard.CardTarget.ENEMY;
import static com.megacrit.cardcrawl.cards.AbstractCard.CardType.ATTACK;
import static spireMapOverhaul.zones.CosmicEukotranpha.CosmicZoneMod.CosmicZoneCosmicCard;

import static spireMapOverhaul.zones.CosmicEukotranpha.util.CosmicShortcuts.atb;import spireMapOverhaul.SpireAnniversary6Mod;

import java.util.ArrayList;
public class CrystalsShotgun extends BaseCard{public static String ID=SpireAnniversary6Mod.makeID(
				CrystalsShotgun.class.getSimpleName());
	public CrystalsShotgun(){super(ID,1,ATTACK,ENEMY,SPECIAL,COLORLESS);this.tags.add(CosmicZoneCosmicCard);
	setCostUpgrade(0);baseDamage=16;MultiCardPreview.add(this,true,new Strike_Red(),new Strike_Green(),new Strike_Blue(),new Strike_Purple());}
@Override public void use(AbstractPlayer p,AbstractMonster m){
	//Cosmic. Deal 16 damage, Fascinated: Gain that much Block instead. If you have 60+ B: Deal 32 Thorns damage to foes
	if(!fasc()){blck();atb(new BCheckDToFoesEffect(60,32));
	}else{dmg(m);}
}
@Override public void upgrade(){if(!upgraded){upgradeName();initializeDescription();this.initializeTitle();}}}