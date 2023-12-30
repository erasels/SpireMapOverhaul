package spireMapOverhaul.zones.CosmicEukotranpha.cards.special;
import com.evacipated.cardcrawl.mod.stslib.cards.targeting.SelfOrEnemyTargeting;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.RegenPower;
import spireMapOverhaul.zones.CosmicEukotranpha.cardEffects.SpecificEffects.GiveEtherealRemoveRetainTEffect;
import spireMapOverhaul.zones.CosmicEukotranpha.cards.BaseCard;
import spireMapOverhaul.zones.CosmicEukotranpha.powers.MusclePower;

import static com.evacipated.cardcrawl.mod.stslib.cards.targeting.SelfOrEnemyTargeting.SELF_OR_ENEMY;
import static com.megacrit.cardcrawl.cards.AbstractCard.CardColor.COLORLESS;
import static com.megacrit.cardcrawl.cards.AbstractCard.CardRarity.SPECIAL;
import static com.megacrit.cardcrawl.cards.AbstractCard.CardType.SKILL;

import static spireMapOverhaul.zones.CosmicEukotranpha.CosmicZoneMod.CosmicZoneCosmicCard;
import static spireMapOverhaul.zones.CosmicEukotranpha.util.CosmicShortcuts.atb;
import static spireMapOverhaul.zones.CosmicEukotranpha.util.CosmicShortcuts.poT;import spireMapOverhaul.SpireAnniversary6Mod;
public class StarCookies extends BaseCard{public static String ID=SpireAnniversary6Mod.makeID(StarCookies.class.getSimpleName());
    public StarCookies(){super(ID,1,SKILL,SELF_OR_ENEMY,SPECIAL,COLORLESS);this.tags.add(CosmicZoneCosmicCard);
        exhaust=true;}
    @Override public void use(AbstractPlayer p,AbstractMonster m){AbstractCreature target=SelfOrEnemyTargeting.getTarget(this);if(target==null){target=AbstractDungeon.player;}
        //Target anyone. Target gains 3 Regen and loses 3 Muscle. Give adjacent cards Ethereal and Remove Retain from them. Exhaust
        poT(target,p,new RegenPower(target,3));poT(target,p,new MusclePower(target,p,-3));
        atb(new GiveEtherealRemoveRetainTEffect(adjLeft()));atb(new GiveEtherealRemoveRetainTEffect(adjRight()));
    }
    @Override public void upgrade(){if(!upgraded){upgradeName();initializeDescription();this.initializeTitle();}}}




