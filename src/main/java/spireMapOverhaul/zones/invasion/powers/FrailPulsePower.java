package spireMapOverhaul.zones.invasion.powers;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.FrailPower;
import spireMapOverhaul.SpireAnniversary6Mod;

public class FrailPulsePower extends AbstractInvasionPower {
    public static final String POWER_ID = SpireAnniversary6Mod.makeID("FrailPulse");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    public FrailPulsePower(AbstractCreature owner) {
        super(POWER_ID, NAME, PowerType.BUFF, false, owner, 0);
    }

    @Override
    public void atEndOfTurn(boolean isPlayer) {
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, this.owner, new FrailPower(AbstractDungeon.player, 1, true), 1));
    }

    @Override
    public void updateDescription() {
        this.description = DESCRIPTIONS[0].replace("{0}", 1 + "");
    }
}
