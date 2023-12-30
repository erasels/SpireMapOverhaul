package spireMapOverhaul.zones.CosmicEukotranpha.cards.special;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import spireMapOverhaul.zones.CosmicEukotranpha.cardEffects.SpecificEffects.LunaFantasiaChannelOrbEffect;
import spireMapOverhaul.zones.CosmicEukotranpha.cards.BaseCard;

import static com.megacrit.cardcrawl.cards.AbstractCard.CardColor.COLORLESS;
import static com.megacrit.cardcrawl.cards.AbstractCard.CardRarity.SPECIAL;
import static com.megacrit.cardcrawl.cards.AbstractCard.CardTarget.ENEMY;
import static com.megacrit.cardcrawl.cards.AbstractCard.CardType.STATUS;

import static spireMapOverhaul.zones.CosmicEukotranpha.CosmicZoneMod.CosmicZoneCosmicCard;
import static spireMapOverhaul.zones.CosmicEukotranpha.util.CosmicShortcuts.atb;
import static spireMapOverhaul.zones.CosmicEukotranpha.util.CosmicShortcuts.loseHP;import spireMapOverhaul.SpireAnniversary6Mod;
public class LunaFantasia extends BaseCard{public static String ID=SpireAnniversary6Mod.makeID(LunaFantasia.class.getSimpleName());
    public LunaFantasia(){super(ID,1,STATUS,ENEMY,SPECIAL,COLORLESS);this.tags.add(CosmicZoneCosmicCard);baseMagicNumber=magicNumber=0;this.exhaust=true;}
    @Override public void use(AbstractPlayer p,AbstractMonster m){
        //Luna Fantasia
        //1 cost. Status. Heal 10. Exhaust. Turn end in hand: Lose 4 hp
        //Transformed:
        //10 D, Channel orb based on enemy (Each Insect, Queen, Other) targeted. Exhaust. Turn end in hand: Lose 2 hp
        if(magicNumber==0){atb(new HealAction(m,AbstractDungeon.player,10));
        }else{dmg(m);atb(new LunaFantasiaChannelOrbEffect(m));}
    }
    @Override public void triggerOnEndOfPlayerTurn(){if(isInHand()){loseHP(AbstractDungeon.player,4-2*magicNumber);}}
    @Override public void upgrade(){if(!upgraded){upgradeName();initializeDescription();this.initializeTitle();}}}



