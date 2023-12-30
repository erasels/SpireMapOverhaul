package spireMapOverhaul.zones.CosmicEukotranpha.cards.special;
import spireMapOverhaul.SpireAnniversary6Mod;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.DexterityPower;
import spireMapOverhaul.zones.CosmicEukotranpha.cards.BaseCard;
import spireMapOverhaul.zones.CosmicEukotranpha.cards.cardMods.AltCostHPMod;
import spireMapOverhaul.zones.CosmicEukotranpha.cards.cardMods.IncreaseCostMod;

import static com.megacrit.cardcrawl.cards.AbstractCard.CardColor.COLORLESS;
import static com.megacrit.cardcrawl.cards.AbstractCard.CardRarity.SPECIAL;
import static com.megacrit.cardcrawl.cards.AbstractCard.CardTarget.SELF;
import static com.megacrit.cardcrawl.cards.AbstractCard.CardType.SKILL;
import static spireMapOverhaul.zones.CosmicEukotranpha.util.CosmicShortcuts.*;
public class EscapeEarth extends BaseCard{public static String ID=SpireAnniversary6Mod.makeID(
				EscapeEarth.class.getSimpleName());
public EscapeEarth(){super(ID,1,SKILL,SELF,SPECIAL,COLORLESS);
	baseMagicNumber=magicNumber=0;}
@Override public void use(AbstractPlayer p,AbstractMonster m){
	//Gain Dexterity to match 3. Triple cost and give "Pay hp instead for any energy you lack to play this" to cards in hand this turn only
	poP(new DexterityPower(AbstractDungeon.player,3-getPowAmount(p,"Dexterity")));
	for(AbstractCard c:AbstractDungeon.player.hand.group){addModT(c,new AltCostHPMod(1));addModT(c,new IncreaseCostMod(c.costForTurn*2,1));}
}
@Override public void upgrade(){if(!upgraded){upgradeName();initializeDescription();this.initializeTitle();}}}






