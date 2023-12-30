package spireMapOverhaul.zones.gremlinTown.powers;

import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.BetterOnApplyPowerPower;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.ending.CorruptHeart;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.BeatOfDeathPower;
import com.megacrit.cardcrawl.powers.WeakPower;
import spireMapOverhaul.SpireAnniversary6Mod;
import spireMapOverhaul.abstracts.AbstractSMOPower;

import static spireMapOverhaul.util.Wiz.applyToEnemyTop;
import static spireMapOverhaul.util.Wiz.atb;

public class HeartImpairmentPower extends AbstractSMOPower implements BetterOnApplyPowerPower {
    public static final String POWER_ID = SpireAnniversary6Mod.makeID("HeartImpairment");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    public HeartImpairmentPower(AbstractCreature owner, int amount) {
        super(POWER_ID, NAME, PowerType.BUFF,false, owner, -1);
        canGoNegative = false;
    }

    @Override
    public void updateDescription() {
        description = DESCRIPTIONS[0];
    }

    public void onAttack(DamageInfo info, int damageAmount, AbstractCreature target) {
        if (damageAmount > 0 && target != owner && info.type == DamageInfo.DamageType.NORMAL) {
            flash();
            applyToEnemyTop((AbstractMonster) target, new WeakPower(target, amount, false));
        }
    }

    @Override
    public boolean betterOnApplyPower(AbstractPower pow, AbstractCreature target, AbstractCreature source) {
        return !pow.ID.equals(BeatOfDeathPower.POWER_ID) || !target.name.equals(CorruptHeart.NAME);
    }
}

