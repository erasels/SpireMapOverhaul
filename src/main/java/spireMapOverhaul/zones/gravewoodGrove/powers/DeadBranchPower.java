package spireMapOverhaul.zones.gravewoodGrove.powers;

import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.RelicLibrary;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.relics.DeadBranch;
import spireMapOverhaul.SpireAnniversary6Mod;

public class DeadBranchPower extends AbstractGravewoodGrovePower {

    public static final String POWER_ID = SpireAnniversary6Mod.makeID("DeadBranch");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    private int cardsExhaustedThisTurn = 0;


    public DeadBranchPower(AbstractCreature owner) {
        super(POWER_ID, NAME, AbstractPower.PowerType.DEBUFF, false, owner, 0);
    }

    public void onExhaust(AbstractCard card) {
        if (!AbstractDungeon.getMonsters().areMonstersBasicallyDead()) {
            flashWithoutSound();
            addToBot(new RelicAboveCreatureAction(AbstractDungeon.player, RelicLibrary.getRelic(DeadBranch.ID)));
            addToBot(new MakeTempCardInHandAction(AbstractDungeon.returnTrulyRandomCardInCombat().makeCopy(), false));
        }
    }

    public void atStartOfTurn() {
        this.cardsExhaustedThisTurn = 0;
    }

    public void onUseCard(AbstractCard card, UseCardAction action) {
        if (this.cardsExhaustedThisTurn < 1) {
            this.cardsExhaustedThisTurn++;
            flashWithoutSound();
            action.exhaustCard = true;
        }
    }

    @Override
    public void updateDescription() {
        this.description = DESCRIPTIONS[0];
    }
}