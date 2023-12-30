package spireMapOverhaul.zones.gremlinTown.potions;

import basemod.abstracts.CustomPotion;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.helpers.GameDictionary;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.helpers.TipHelper;
import com.megacrit.cardcrawl.localization.PotionStrings;
import com.megacrit.cardcrawl.powers.DexterityPower;
import com.megacrit.cardcrawl.powers.FocusPower;
import com.megacrit.cardcrawl.powers.StrengthPower;

import static spireMapOverhaul.SpireAnniversary6Mod.makeID;
import static spireMapOverhaul.util.Wiz.*;

public class LouseMilk extends CustomPotion {
    public static final String POTION_ID = makeID(LouseMilk.class.getSimpleName());
    private static final PotionStrings potionStrings = CardCrawlGame.languagePack.getPotionString(POTION_ID);
    private static final String NAME = potionStrings.NAME;
    private static final int DEFAULT_POTENCY = 1;
    private static final PotionRarity RARITY = PotionRarity.COMMON;
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
        tips.clear();
        tips.add(new PowerTip(name, description));
        tips.add(new PowerTip(TipHelper.capitalize(GameDictionary.STRENGTH.NAMES[0]),
                GameDictionary.keywords.get(GameDictionary.STRENGTH.NAMES[0])));
        tips.add(new PowerTip(TipHelper.capitalize(GameDictionary.DEXTERITY.NAMES[0]),
                GameDictionary.keywords.get(GameDictionary.DEXTERITY.NAMES[0])));
        tips.add(new PowerTip(TipHelper.capitalize(GameDictionary.FOCUS.NAMES[0]),
                GameDictionary.keywords.get(GameDictionary.FOCUS.NAMES[0])));
    }

    public void use(AbstractCreature target) {
        applyToSelf(new StrengthPower(adp(), potency));
        applyToSelf(new DexterityPower(adp(), potency));
        applyToSelf(new FocusPower(adp(), potency));
    }

    public int getPotency(int asc) {
        return DEFAULT_POTENCY;
    }

    public CustomPotion makeCopy() {
        try {
            return getClass().newInstance();
        } catch (IllegalAccessException | InstantiationException var2) {
            RuntimeException e = new RuntimeException("BaseMod failed to auto-generate makeCopy for potion: " + ID);
            e.printStackTrace();
            throw e;
        }
    }
}
