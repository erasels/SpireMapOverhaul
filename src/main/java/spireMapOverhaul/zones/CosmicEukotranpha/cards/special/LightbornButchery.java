package spireMapOverhaul.zones.CosmicEukotranpha.cards.special;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import spireMapOverhaul.zones.CosmicEukotranpha.cardEffects.SpecificEffects.LightbornButcheryKillEffect;
import spireMapOverhaul.zones.CosmicEukotranpha.cards.BaseCard;

import static com.megacrit.cardcrawl.cards.AbstractCard.CardColor.COLORLESS;
import static com.megacrit.cardcrawl.cards.AbstractCard.CardRarity.SPECIAL;
import static com.megacrit.cardcrawl.cards.AbstractCard.CardTarget.ENEMY;
import static com.megacrit.cardcrawl.cards.AbstractCard.CardType.ATTACK;

import static spireMapOverhaul.zones.CosmicEukotranpha.CosmicZoneMod.CosmicZoneCosmicCard;
import static spireMapOverhaul.zones.CosmicEukotranpha.util.CosmicShortcuts.atb;
import static spireMapOverhaul.zones.CosmicEukotranpha.util.CosmicShortcuts.typesInGroup;import spireMapOverhaul.SpireAnniversary6Mod;
public class LightbornButchery extends BaseCard{public static String ID=SpireAnniversary6Mod.makeID(
				LightbornButchery.class.getSimpleName());
public LightbornButchery(){super(ID,0,ATTACK,ENEMY,SPECIAL,COLORLESS);this.tags.add(CosmicZoneCosmicCard);
baseDamage=0;}
@Override public void use(AbstractPlayer p,AbstractMonster m){
	//(Cosmic) Deal 6(Types in hand) damage, If it died: Draw 5, Fascinated: Lose 2 hp and Gain 2 energy, Str, and Dex
	atb(new LightbornButcheryKillEffect(m,new DamageInfo(p,this.damage,this.damageTypeForTurn)));
}
public void applyPowers(){damage=6*typesInGroup(AbstractDungeon.player.hand).size();super.applyPowers();}
@Override public void upgrade(){if(!upgraded){upgradeName();initializeDescription();this.initializeTitle();}}}




