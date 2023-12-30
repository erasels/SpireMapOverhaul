package spireMapOverhaul.zones.CosmicEukotranpha.cards.special;
import com.megacrit.cardcrawl.actions.utility.LoseBlockAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.DexterityPower;
import spireMapOverhaul.zones.CosmicEukotranpha.cardEffects.cardManip.CZExhaustTEffect;
import spireMapOverhaul.zones.CosmicEukotranpha.cards.BaseCard;

import static com.megacrit.cardcrawl.cards.AbstractCard.CardColor.COLORLESS;
import static com.megacrit.cardcrawl.cards.AbstractCard.CardRarity.SPECIAL;
import static com.megacrit.cardcrawl.cards.AbstractCard.CardTarget.SELF;
import static com.megacrit.cardcrawl.cards.AbstractCard.CardType.SKILL;

import static spireMapOverhaul.zones.CosmicEukotranpha.CosmicZoneMod.CosmicZoneCosmicCard;
import static spireMapOverhaul.zones.CosmicEukotranpha.util.CosmicShortcuts.atb;import spireMapOverhaul.SpireAnniversary6Mod;
public class PathToThePillar extends BaseCard{public static String ID=SpireAnniversary6Mod.makeID(PathToThePillar.class.getSimpleName());
    public PathToThePillar(){super(ID,0,SKILL,SELF,SPECIAL,COLORLESS);this.tags.add(CosmicZoneCosmicCard);}
    @Override public void use(AbstractPlayer p,AbstractMonster m){
        //0 cost. Lose 6 B. Exhaust. Turn end in hand: Lose 1 Dex. Exhaust
        atb(new LoseBlockAction(AbstractDungeon.player,AbstractDungeon.player,6));}
    @Override public void triggerOnEndOfPlayerTurn(){if(isInHand()){poS(new DexterityPower(AbstractDungeon.player,-1));atb(new CZExhaustTEffect(this));}}
    @Override public void upgrade(){if(!upgraded){upgradeName();initializeDescription();this.initializeTitle();}}}




