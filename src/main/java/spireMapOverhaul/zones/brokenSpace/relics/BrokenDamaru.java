package spireMapOverhaul.zones.brokenSpace.relics;

import com.evacipated.cardcrawl.mod.stslib.actions.common.SelectCardsInHandAction;
import com.megacrit.cardcrawl.actions.common.*;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.watcher.MantraPower;
import com.megacrit.cardcrawl.relics.Damaru;


import java.util.ArrayList;

import static spireMapOverhaul.util.Wiz.*;

public class BrokenDamaru extends BrokenRelic {
    public static final String ID = "BrokenDamaru";

    public static final int MANTRA_AMOUNT = 2;


    public BrokenDamaru() {
        super(ID, RelicTier.SPECIAL, LandingSound.CLINK, Damaru.ID);
    }



    @Override
    public boolean canSpawn() {
        return adp().chosenClass == AbstractPlayer.PlayerClass.WATCHER;
    }

    @Override
    public void onPlayerEndTurn() {
        addToBot(new SelectCardsInHandAction(1, DESCRIPTIONS[1], false, true, (c) -> {// 76
            return true;
        }, (abstractCards -> {
            AbstractCard c = abstractCards.get(0);
            applyToSelf(new MantraPower(adp(), c.costForTurn));
            addToBot(new ExhaustSpecificCardAction(c, adp().hand));
            flash();
        })));
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }
}