package spireMapOverhaul.zones.hailstorm.cards;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.CardQueueItem;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.FrailPower;
import spireMapOverhaul.SpireAnniversary6Mod;
import spireMapOverhaul.abstracts.AbstractSMOCard;

public class IceBurn extends AbstractSMOCard {
    public static final String ID = SpireAnniversary6Mod.makeID(IceBurn.class.getSimpleName());
    private static final int COST = -2;

    public IceBurn() {
        super(ID, COST, CardType.STATUS, CardRarity.COMMON, CardTarget.NONE, CardColor.COLORLESS);
        this.selfRetain = false;
        this.magicNumber = 2;
        this.baseMagicNumber = 2;
    }

    public IceBurn(boolean upgraded) {
        this();
        if (upgraded)
            this.upgrade();
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        if (this.dontTriggerOnUseCard) {
            this.addToBot(new DamageAction(AbstractDungeon.player, new DamageInfo(AbstractDungeon.player, this.magicNumber, DamageInfo.DamageType.THORNS), AbstractGameAction.AttackEffect.FIRE));
        }

    }

    public void triggerOnEndOfTurnForPlayingCard() {
        this.dontTriggerOnUseCard = true;
        AbstractDungeon.actionManager.cardQueue.add(new CardQueueItem(this, true));
    }

//    public void upgrade() {
//        if (!this.upgraded) {
//            this.upgradeName();
//            this.upgradeMagicNumber(2);
//            this.rawDescription = cardStrings.UPGRADE_DESCRIPTION;
//            this.initializeDescription();
//        }
//    }
    public void upp() {
        upgradeMagicNumber(2);
    }
}
