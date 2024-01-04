package spireMapOverhaul.zones.manasurge.powers;

import basemod.abstracts.AbstractCardModifier;
import basemod.helpers.CardModifierManager;
import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.NonStackablePower;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import spireMapOverhaul.SpireAnniversary6Mod;
import spireMapOverhaul.abstracts.AbstractSMOPower;
import spireMapOverhaul.zones.manasurge.ManaSurgeZone;
import spireMapOverhaul.zones.manasurge.modifiers.common.negative.FeebleModifier;
import spireMapOverhaul.zones.manasurge.modifiers.common.negative.FlawedModifier;
import spireMapOverhaul.zones.manasurge.modifiers.common.negative.FragileModifier;
import spireMapOverhaul.zones.manasurge.modifiers.common.negative.HarmfulModifier;
import spireMapOverhaul.zones.manasurge.modifiers.common.positive.CripplingModifier;
import spireMapOverhaul.zones.manasurge.modifiers.common.positive.ExposingModifier;
import spireMapOverhaul.zones.manasurge.modifiers.common.positive.SharpModifier;
import spireMapOverhaul.zones.manasurge.modifiers.common.positive.ToughModifier;
import spireMapOverhaul.zones.manasurge.modifiers.uncommon.negative.BrittleModifier;
import spireMapOverhaul.zones.manasurge.modifiers.uncommon.negative.PowerlessModifier;
import spireMapOverhaul.zones.manasurge.modifiers.uncommon.positive.PowerfulModifier;
import spireMapOverhaul.zones.manasurge.modifiers.uncommon.positive.ProtectiveModifier;

public class ManaSurgePower extends AbstractSMOPower implements NonStackablePower {
    public static final String POWER_ID = SpireAnniversary6Mod.makeID("ManaSurgePower");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    private static final float EFFECT_CHANCE = 0.5f;
    private static final float COMMON_CHANCE = 0.8f;

    public ManaSurgePower(AbstractCreature owner, int amount) {
        super(POWER_ID, NAME, PowerType.BUFF, false, owner, amount);
    }

    @Override
    public void onCardDraw(AbstractCard card) {
        super.onCardDraw(card);
        if (!ManaSurgeZone.hasManaSurgeModifier(card) && card.cost != -2 && card.type != AbstractCard.CardType.CURSE && card.type != AbstractCard.CardType.STATUS) {
            if (Math.random() < EFFECT_CHANCE) {
                if (Math.random() < COMMON_CHANCE) {
                    int numberOfCommonModifiers = 8;
                    int selectedModifierIndex = (int) (Math.random() * numberOfCommonModifiers);
                    AbstractCardModifier modifier;
                    switch (selectedModifierIndex) {
                        case 0:
                            modifier = new SharpModifier(false);
                            break;
                        case 1:
                            modifier = new HarmfulModifier(false);
                            break;
                        case 2:
                            modifier = new ToughModifier(false);
                            break;
                        case 3:
                            modifier = new FragileModifier(false);
                            break;
                        case 4:
                            modifier = new ExposingModifier(false);
                            break;
                        case 5:
                            modifier = new FeebleModifier(false);
                            break;
                        case 6:
                            modifier = new CripplingModifier(false);
                            break;
                        case 7:
                            modifier = new FlawedModifier(false);
                            break;
                        default:
                            modifier = null;
                            break;
                    }
                    if (modifier != null) {
                        CardModifierManager.addModifier(card, modifier);
                    }
                } else {
                    int numberOfUncommonModifiers = 4;
                    int selectedModifierIndex = (int) (Math.random() * numberOfUncommonModifiers);
                    AbstractCardModifier modifier;
                    switch (selectedModifierIndex) {
                        case 0:
                            modifier = new PowerfulModifier(false);
                            break;
                        case 1:
                            modifier = new PowerlessModifier(false);
                            break;
                        case 2:
                            modifier = new ProtectiveModifier(false);
                            break;
                        case 3:
                            modifier = new BrittleModifier(false);
                            break;
                        default:
                            modifier = null;
                            break;
                    }
                    if (modifier != null) {
                        CardModifierManager.addModifier(card, modifier);
                    }
                }
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

