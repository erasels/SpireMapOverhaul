package spireMapOverhaul.zones.invasion.cards;

import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import spireMapOverhaul.SpireAnniversary6Mod;
import spireMapOverhaul.zones.invasion.actions.ReduceAllEnemyRegenAction;

public class BrokenCrystal extends AbstractInvasionZoneCard {
    public static final String ID = SpireAnniversary6Mod.makeID("BrokenCrystal");
    private static final int COST = 1;
    private static final int MAX_COST = 2;
    private static final int DRAW = 1;
    private static final int REGEN_REDUCTION = 5;
    private static final int UPGRADE_REGEN_REDUCTION = 8;

    public BrokenCrystal() {
        super(ID, COST, CardType.STATUS, CardRarity.SPECIAL, CardTarget.SELF, CardColor.COLORLESS);
        this.baseMagicNumber = REGEN_REDUCTION;
        this.magicNumber = this.baseMagicNumber;
        this.exhaust = true;
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new ReduceAllEnemyRegenAction(this.magicNumber));
    }

    @Override
    public void triggerWhenDrawn() {
        if (this.cost >= 0) {
            int newCost = AbstractDungeon.cardRandomRng.random(MAX_COST);
            if (this.cost != newCost) {
                this.cost = newCost;
                this.costForTurn = this.cost;
                this.isCostModified = true;
            }

            this.freeToPlayOnce = false;
        }
        this.addToBot(new DrawCardAction(DRAW));
    }

    public void upp() {
        this.upgradeMagicNumber(UPGRADE_REGEN_REDUCTION);
    }
}
