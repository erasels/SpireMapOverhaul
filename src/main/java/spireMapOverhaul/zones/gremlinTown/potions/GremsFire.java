package spireMapOverhaul.zones.gremlinTown.potions;

import basemod.abstracts.CustomPotion;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.localization.PotionStrings;
import spireMapOverhaul.SpireAnniversary6Mod;
import spireMapOverhaul.zones.gremlinTown.powers.GremsFirePower;

import static spireMapOverhaul.SpireAnniversary6Mod.makeID;
import static spireMapOverhaul.util.Wiz.adp;
import static spireMapOverhaul.util.Wiz.applyToSelf;

public class GremsFire extends CustomPotion {
    public static final String POTION_ID = makeID(GremsFire.class.getSimpleName());
    private static final PotionStrings potionStrings = CardCrawlGame.languagePack.getPotionString(POTION_ID);
    private static final String NAME = potionStrings.NAME;
    private static final int DEFAULT_POTENCY = 5;
    private static final PotionRarity RARITY = SpireAnniversary6Mod.Enums.ZONE;
    private static final PotionSize SIZE = PotionSize.HEART;
    private static final PotionColor COLOR = PotionColor.FIRE;
    private static final boolean IS_THROWN = false;
    private static final boolean TARGET_REQUIRED = false;

    public GremsFire() {
        super(NAME, POTION_ID, RARITY, SIZE, COLOR);
        isThrown = IS_THROWN;
        targetRequired = TARGET_REQUIRED;
    }

    @Override
    public void initializeData() {
        potency = getPotency();
        description = potionStrings.DESCRIPTIONS[0].replace("{0}", String.valueOf(potency));
        tips.clear();
        tips.add(new PowerTip(name, description));
    }

    @Override
    public void use(AbstractCreature target) {
        applyToSelf(new GremsFirePower(adp(), potency));
    }

    @Override
    public int getPotency(int asc) {
        return DEFAULT_POTENCY;
    }

    @Override
    public CustomPotion makeCopy() {
        return new GremsFire();
    }
}
