package spireMapOverhaul.zones.mirror.powers;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import spireMapOverhaul.SpireAnniversary6Mod;
import spireMapOverhaul.abstracts.AbstractSMOPower;
import spireMapOverhaul.zones.mirror.MirrorZone;
import spireMapOverhaul.zones.mirror.cards.MirrorMove;

public class ReflectivePower extends AbstractSMOPower {
    public static final String POWER_ID = SpireAnniversary6Mod.makeID("Reflective");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    public ReflectivePower(AbstractCreature owner, int amount) {
        super(POWER_ID, NAME, MirrorZone.ID, PowerType.BUFF, false, owner, amount);
    }

    @Override
    public void updateDescription() {
        description = DESCRIPTIONS[0].replace("{0}", Integer.toString(amount));
    }

    @Override
    public float atDamageGive(float damage, DamageInfo.DamageType type, AbstractCard card) {
        if (card instanceof MirrorMove) {
            return damage + amount;
        } else {
            return damage;
        }
    }

    @Override
    public float modifyBlock(float blockAmount, AbstractCard card) {
        if (card instanceof MirrorMove) {
            return blockAmount + amount;
        } else {
            return blockAmount;
        }
    }
}
