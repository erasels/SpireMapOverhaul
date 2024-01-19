package spireMapOverhaul.zones.gremlinTown.potions;

import basemod.abstracts.CustomPotion;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.localization.PotionStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.ending.CorruptHeart;
import com.megacrit.cardcrawl.powers.BeatOfDeathPower;
import com.megacrit.cardcrawl.powers.InvinciblePower;
import spireMapOverhaul.SpireAnniversary6Mod;
import spireMapOverhaul.zones.gremlinTown.powers.HeartImpairmentPower;

import static spireMapOverhaul.SpireAnniversary6Mod.makeID;
import static spireMapOverhaul.util.Wiz.*;

public class RitualBlood extends CustomPotion {
    public static final String POTION_ID = makeID(RitualBlood.class.getSimpleName());
    private static final PotionStrings potionStrings = CardCrawlGame.languagePack.getPotionString(POTION_ID);
    private static final String NAME = potionStrings.NAME;
    private static final int DEFAULT_POTENCY = 1;
    private static final PotionRarity RARITY = SpireAnniversary6Mod.Enums.ZONE;
    private static final PotionSize SIZE = PotionSize.EYE;
    private static final PotionColor COLOR = PotionColor.FEAR;
    private static final boolean IS_THROWN = true;
    private static final boolean TARGET_REQUIRED = true;

    public static final Color POTION_COLOR = new Color(138f/255f, 3f/255f, 3f/255f, 1f);

    public RitualBlood() {
        super(NAME, POTION_ID, RARITY, SIZE, COLOR);
        isThrown = IS_THROWN;
        targetRequired = TARGET_REQUIRED;
    }

    public void initializeData() {
        potency = getPotency();
        description = potionStrings.DESCRIPTIONS[0].replace("{0}", String.valueOf(potency));
        tips.clear();
        tips.add(new PowerTip(name, description));
    }

    public void use(AbstractCreature target) {
        if (target.name.equals(CorruptHeart.NAME)) {
            atb(new RemoveSpecificPowerAction(target, adp(), BeatOfDeathPower.POWER_ID));
            atb(new RemoveSpecificPowerAction(target, adp(), InvinciblePower.POWER_ID));
            applyToEnemy((AbstractMonster) target, new HeartImpairmentPower(target, -1));
        }
    }

    public int getPotency(int asc) {
        return DEFAULT_POTENCY;
    }

    public CustomPotion makeCopy() {
        return new RitualBlood();
    }
}
