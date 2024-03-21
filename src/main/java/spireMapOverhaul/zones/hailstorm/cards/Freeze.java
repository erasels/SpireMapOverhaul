package spireMapOverhaul.zones.hailstorm.cards;

import com.megacrit.cardcrawl.actions.common.MakeTempCardInDiscardAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import spireMapOverhaul.SpireAnniversary6Mod;
import spireMapOverhaul.abstracts.AbstractSMOCard;
import spireMapOverhaul.zones.hailstorm.HailstormZone;

//Can be renamed Freezing if needed
public class Freeze extends AbstractSMOCard {
    //public static final String ID = SpireAnniversary6Mod.makeID(Freeze.class.getSimpleName());
    public static final String ID = SpireAnniversary6Mod.makeID("Freeze");
    private static final int COST = -2;

    public Freeze() {
        super(ID, HailstormZone.ID, COST, CardType.CURSE, CardRarity.CURSE, CardTarget.NONE, CardColor.CURSE);
        this.selfRetain = true;
        cardsToPreview = new IceBurn();
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
//        if (this.dontTriggerOnUseCard) {
//            this.addToTop((new MakeTempCardInDiscardAction(new IceBurn(upgraded), 1)));
//        }
    }

    public void triggerOnEndOfTurnForPlayingCard() {
//        this.dontTriggerOnUseCard = true;
//        AbstractDungeon.actionManager.cardQueue.add(new CardQueueItem(this, true));
        this.addToTop((new MakeTempCardInDiscardAction(new IceBurn(upgraded), 1)));
    }
    public void upp() {
        cardsToPreview.upgrade();
    }
}
