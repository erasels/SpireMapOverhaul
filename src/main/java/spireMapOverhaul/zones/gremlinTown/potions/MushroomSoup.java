package spireMapOverhaul.zones.gremlinTown.potions;

import basemod.BaseMod;
import basemod.abstracts.CustomPotion;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.localization.PotionStrings;
import spireMapOverhaul.SpireAnniversary6Mod;
import spireMapOverhaul.zones.gremlinTown.powers.MushroomSoupPower;

import static spireMapOverhaul.SpireAnniversary6Mod.makeID;
import static spireMapOverhaul.util.Wiz.adp;
import static spireMapOverhaul.util.Wiz.applyToSelf;

public class MushroomSoup extends CustomPotion {
    public static final String POTION_ID = makeID(MushroomSoup.class.getSimpleName());
    private static final PotionStrings potionStrings = CardCrawlGame.languagePack.getPotionString(POTION_ID);
    private static final String NAME = potionStrings.NAME;
    private static final int DEFAULT_POTENCY = 3;
    private static final PotionRarity RARITY = SpireAnniversary6Mod.Enums.ZONE;
    private static final PotionSize SIZE = PotionSize.H;
    private static final PotionColor COLOR = PotionColor.STRENGTH;
    private static final boolean IS_THROWN = false;
    private static final boolean TARGET_REQUIRED = false;

    public static final Color POTION_COLOR = new Color(152f/255f, 188f/255f, 84f/255f, 1f);

    public MushroomSoup() {
        super(NAME, POTION_ID, RARITY, SIZE, COLOR);
        isThrown = IS_THROWN;
        targetRequired = TARGET_REQUIRED;
    }

    public void initializeData() {
        potency = getPotency();
        description = potionStrings.DESCRIPTIONS[0].replace("{0}", String.valueOf(potency));
        tips.clear();
        tips.add(new PowerTip(name, description));
        tips.add(new PowerTip(BaseMod.getKeywordTitle("temporary_hp"), BaseMod.getKeywordDescription("temporary_hp")));
    }

    public void use(AbstractCreature target) {
        applyToSelf(new MushroomSoupPower(adp(), potency));
    }

    public int getPotency(int asc) {
        return DEFAULT_POTENCY;
    }

    public CustomPotion makeCopy() {
        return new MushroomSoup();
    }
}
