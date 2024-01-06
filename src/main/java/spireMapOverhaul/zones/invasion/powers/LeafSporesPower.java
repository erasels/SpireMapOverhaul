package spireMapOverhaul.zones.invasion.powers;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.FrailPower;
import com.megacrit.cardcrawl.powers.WeakPower;
import spireMapOverhaul.SpireAnniversary6Mod;

public class LeafSporesPower extends AbstractInvasionPower {
    public static final String POWER_ID = SpireAnniversary6Mod.makeID("LeafSpores");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    public LeafSporesPower(AbstractCreature owner, int amount) {
        super(POWER_ID, NAME, PowerType.BUFF, false, owner, amount);
    }

    @Override
    public void updateDescription() {
        this.description = DESCRIPTIONS[0].replace("{0}", this.amount + "");
    }

    @Override
    public void onDeath() {
        if (!AbstractDungeon.getCurrRoom().isBattleEnding()) {
            CardCrawlGame.sound.play("SPORE_CLOUD_RELEASE");
            this.flashWithoutSound();
            this.addToTop(new ApplyPowerAction(AbstractDungeon.player, null, new WeakPower(AbstractDungeon.player, this.amount, true), this.amount));
            this.addToTop(new ApplyPowerAction(AbstractDungeon.player, null, new FrailPower(AbstractDungeon.player, this.amount, true), this.amount));
        }
    }
}

