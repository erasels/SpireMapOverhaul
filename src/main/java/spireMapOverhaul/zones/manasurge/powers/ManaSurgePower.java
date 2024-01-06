package spireMapOverhaul.zones.manasurge.powers;

import com.evacipated.cardcrawl.mod.stslib.patches.NeutralPowertypePatch;
import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.NonStackablePower;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import spireMapOverhaul.SpireAnniversary6Mod;
import spireMapOverhaul.abstracts.AbstractSMOPower;
import spireMapOverhaul.zones.manasurge.ManaSurgeZone;

public class ManaSurgePower extends AbstractSMOPower implements NonStackablePower {
    public static final String POWER_ID = SpireAnniversary6Mod.makeID("ManaSurgePower");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    public static String ZONE_ID = ManaSurgeZone.ID;

    private static final float EFFECT_CHANCE = 0.5f;

    public ManaSurgePower(AbstractCreature owner, int amount) {
        super(POWER_ID,NAME,ZONE_ID, NeutralPowertypePatch.NEUTRAL, false, owner, amount);
    }

    @Override
    public void onCardDraw(AbstractCard card) {
        super.onCardDraw(card);
        if (!ManaSurgeZone.hasManaSurgeModifier(card) && card.cost != -2 && card.type != AbstractCard.CardType.CURSE && card.type != AbstractCard.CardType.STATUS) {
            if (AbstractDungeon.cardRandomRng.randomBoolean(EFFECT_CHANCE)) {
                ManaSurgeZone.applyRandomTemporaryModifier(card);
            }
        }
    }

    @Override
    public boolean isStackable(AbstractPower power) {
        return false;
    }


    @Override
    public void updateDescription() {
        this.description = DESCRIPTIONS[0];
    }
}

