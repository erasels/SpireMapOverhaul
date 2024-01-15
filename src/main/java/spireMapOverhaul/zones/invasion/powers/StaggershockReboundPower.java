package spireMapOverhaul.zones.invasion.powers;

import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.CardQueueItem;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import spireMapOverhaul.SpireAnniversary6Mod;
import spireMapOverhaul.zones.invasion.cards.Staggershock;

import java.util.concurrent.atomic.AtomicInteger;

public class StaggershockReboundPower extends AbstractInvasionPower {
    public static final String POWER_ID = SpireAnniversary6Mod.makeID("StaggershockRebound");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    private static final AtomicInteger counter = new AtomicInteger();
    private Staggershock staggershock;

    public StaggershockReboundPower(AbstractCreature owner, Staggershock staggershock) {
        super(POWER_ID + counter.getAndAdd(1), NAME, PowerType.BUFF, true, owner, 0);
        this.staggershock = (Staggershock)staggershock.makeSameInstanceOf();
    }

    @Override
    public void updateDescription() {
        this.description = DESCRIPTIONS[0];
    }

    @Override
    public void atStartOfTurn() {
        this.flash();
        this.staggershock.purgeOnUse = true;
        this.staggershock.rebound = true;
        AbstractDungeon.actionManager.addCardQueueItem(new CardQueueItem(this.staggershock, null, this.staggershock.energyOnUse, true, true), true);
        this.addToBot(new RemoveSpecificPowerAction(this.owner, this.owner, this));
    }
}