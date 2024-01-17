package spireMapOverhaul.zones.gremlinTown.potions;

import basemod.abstracts.CustomPotion;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.localization.PotionStrings;
import spireMapOverhaul.SpireAnniversary6Mod;

import static spireMapOverhaul.SpireAnniversary6Mod.makeID;
import static spireMapOverhaul.util.Wiz.atb;

public class LouseMilk extends CustomPotion {
    public static final String POTION_ID = makeID(LouseMilk.class.getSimpleName());
    private static final PotionStrings potionStrings = CardCrawlGame.languagePack.getPotionString(POTION_ID);
    private static final String NAME = potionStrings.NAME;
    private static final int DEFAULT_POTENCY = 2;
    private static final PotionRarity RARITY = SpireAnniversary6Mod.Enums.ZONE;
    private static final PotionSize SIZE = PotionSize.BOTTLE;
    private static final PotionColor COLOR = PotionColor.WHITE;
    private static final boolean IS_THROWN = false;
    private static final boolean TARGET_REQUIRED = false;

    public LouseMilk() {
        super(NAME, POTION_ID, RARITY, SIZE, COLOR);
        isThrown = IS_THROWN;
        targetRequired = TARGET_REQUIRED;
    }

    public void initializeData() {
        potency = getPotency();
        description = potionStrings.DESCRIPTIONS[0].replace("{0}", String.valueOf(potency));
        description = description.replace("{1}", String.valueOf(potency/2));
        tips.clear();
        tips.add(new PowerTip(name, description));
    }

    public void use(AbstractCreature target) {
        atb(new DrawCardAction(potency));
        atb(new GainEnergyAction(potency/2));
    }

    public int getPotency(int asc) {
        return DEFAULT_POTENCY;
    }

    public CustomPotion makeCopy() {
        return new LouseMilk();
    }
}
