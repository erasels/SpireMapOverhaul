package spireMapOverhaul.zones.gremlinTown.potions;

import basemod.abstracts.CustomPotion;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.helpers.GameDictionary;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.helpers.TipHelper;
import com.megacrit.cardcrawl.localization.PotionStrings;
import com.megacrit.cardcrawl.powers.watcher.VigorPower;
import spireMapOverhaul.SpireAnniversary6Mod;

import static spireMapOverhaul.SpireAnniversary6Mod.makeID;
import static spireMapOverhaul.util.Wiz.adp;
import static spireMapOverhaul.util.Wiz.applyToSelf;

public class PreerelxsBlueRibbon extends CustomPotion {
    public static final String POTION_ID = makeID(PreerelxsBlueRibbon.class.getSimpleName());
    private static final PotionStrings potionStrings = CardCrawlGame.languagePack.getPotionString(POTION_ID);
    private static final String NAME = potionStrings.NAME;
    private static final int DEFAULT_POTENCY = 9;
    private static final PotionRarity RARITY = SpireAnniversary6Mod.Enums.ZONE;
    private static final PotionSize SIZE = PotionSize.BOTTLE;
    private static final PotionColor COLOR = PotionColor.ENERGY;
    private static final boolean IS_THROWN = false;
    private static final boolean TARGET_REQUIRED = false;

    public PreerelxsBlueRibbon() {
        super(NAME, POTION_ID, RARITY, SIZE, COLOR);
        isThrown = IS_THROWN;
        targetRequired = TARGET_REQUIRED;
    }

    public void initializeData() {
        potency = getPotency();
        description = potionStrings.DESCRIPTIONS[0].replace("{0}", String.valueOf(potency));
        tips.clear();
        tips.add(new PowerTip(name, description));
        tips.add(new PowerTip(TipHelper.capitalize(GameDictionary.VIGOR.NAMES[0]),
                GameDictionary.keywords.get(GameDictionary.VIGOR.NAMES[0])));
    }

    public void use(AbstractCreature target) {
        applyToSelf(new VigorPower(adp(), potency));
    }

    public int getPotency(int asc) {
        return DEFAULT_POTENCY;
    }

    public CustomPotion makeCopy() {
        return new PreerelxsBlueRibbon();
    }
}
