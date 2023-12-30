package spireMapOverhaul.zones.CosmicEukotranpha.cards.special;
import basemod.helpers.TooltipInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import spireMapOverhaul.zones.CosmicEukotranpha.cards.BaseCard;

import static com.megacrit.cardcrawl.cards.AbstractCard.CardColor.COLORLESS;
import static com.megacrit.cardcrawl.cards.AbstractCard.CardRarity.SPECIAL;
import static com.megacrit.cardcrawl.cards.AbstractCard.CardTarget.SELF;
import static com.megacrit.cardcrawl.cards.AbstractCard.CardType.SKILL;
import static spireMapOverhaul.zones.CosmicEukotranpha.CosmicZoneMod.CosmicZoneCosmicCard;

import spireMapOverhaul.SpireAnniversary6Mod;
import spireMapOverhaul.zones.CosmicEukotranpha.util.CosmicZoneGameActionHistory;

import java.util.ArrayList;
import java.util.List;
public class AngelicBrace extends BaseCard{public static String ID=SpireAnniversary6Mod.makeID(AngelicBrace.class.getSimpleName());
public AngelicBrace(){super(ID,1,SKILL,SELF,SPECIAL,COLORLESS);this.tags.add(CosmicZoneCosmicCard);}
public void use(AbstractPlayer p,AbstractMonster m){
    //Retain. Send this to deck. Choose, [Heal 4. Upgrade hand and this] [Exhume 3] [Add 2 copies of next card played to deck]
    //Upgrade: [Activate this effect of "Angelic Brace" once per combat: Add non-Common/Uncommon/Rare card to master deck]
}
public List<TooltipInfo>getCustomTooltipsTop(){if(kTips==null){kTips=new ArrayList<>();
    if(!CosmicZoneGameActionHistory.horoscope.isEmpty()&&CosmicZoneGameActionHistory.horoscopeDoThings>0){
        String dimt="Horoscope: "+CosmicZoneGameActionHistory.horoscope.group.get(0)+", "+CosmicZoneGameActionHistory.horoscope.group.get(1)+", "+CosmicZoneGameActionHistory.horoscope.group.get(2)+", "+CosmicZoneGameActionHistory.horoscope.group.get(3);
        kTips.add(new TooltipInfo(getChaS("AngelicBrace",0,0),dimt));}}
    List<TooltipInfo>compoundList=new ArrayList<>(kTips);if(super.getCustomTooltipsTop()!=null){compoundList.addAll(super.getCustomTooltipsTop());}
    return compoundList;}
public void applyPowers(){super.applyPowers();initializeDescription();}
public void upgrade(){if(!upgraded){upgradeName();initializeDescription();this.initializeTitle();}}}

