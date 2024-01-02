package spireMapOverhaul.zones.brokenSpace.relics;

import com.megacrit.cardcrawl.actions.common.EmptyDeckShuffleAction;
import com.megacrit.cardcrawl.actions.common.ShuffleAction;
import com.megacrit.cardcrawl.actions.defect.ShuffleAllAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.relics.Matryoshka;
import com.megacrit.cardcrawl.relics.Sundial;

public class BrokenSundial extends BrokenRelic {
    public static final String ID = "BrokenSundial";
    public static final int AMOUNT = 5;

    public BrokenSundial() {
        super(ID, RelicTier.SPECIAL, LandingSound.CLINK, Sundial.ID);
        counter = 0;
    }

    @Override
    public void onPlayCard(AbstractCard c, AbstractMonster m) {
        ++this.counter;
        if (this.counter == AMOUNT) {
            this.counter = 0;
            this.flash();
            addToBot(new EmptyDeckShuffleAction());
        }
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0] + AMOUNT + DESCRIPTIONS[1];
    }
}
