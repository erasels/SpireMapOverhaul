package spireMapOverhaul.zones.mirror.cards;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.evacipated.cardcrawl.mod.stslib.actions.tempHp.AddTemporaryHPAction;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.actions.defect.ChannelAction;
import com.megacrit.cardcrawl.actions.unique.RemoveDebuffsAction;
import com.megacrit.cardcrawl.actions.unique.VampireDamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.GameDictionary;
import com.megacrit.cardcrawl.helpers.TipHelper;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.localization.LocalizedStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.powers.*;
import com.megacrit.cardcrawl.vfx.combat.BiteEffect;
import com.megacrit.cardcrawl.vfx.combat.WeightyImpactEffect;
import spireMapOverhaul.SpireAnniversary6Mod;
import spireMapOverhaul.abstracts.AbstractSMOCard;
import spireMapOverhaul.zones.mirror.MirrorMoveData;
import spireMapOverhaul.zones.mirror.MirrorZone;
import spireMapOverhaul.zones.mirror.powers.ReflectivePower;
import spireMapOverhaul.zones.mirror.powers.ShatteredPower;

import java.util.ArrayList;
import java.util.Map;

public class MirrorMove extends AbstractSMOCard {
    private static final String RAW_ID = "MirrorMove";
    public static final String ID = SpireAnniversary6Mod.makeID(RAW_ID);
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String[] EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;
    private static final int COST = -2;
    private static final AbstractCard.CardType TYPE = AbstractCard.CardType.SKILL;
    private static final AbstractCard.CardRarity RARITY = AbstractCard.CardRarity.SPECIAL;
    private static final AbstractCard.CardTarget TARGET = AbstractCard.CardTarget.NONE;


    // card backs
    public static String BG_SMALL_ATTACK = SpireAnniversary6Mod.makeImagePath("cards/" + MirrorZone.ID + "/MirrorMoveCardBack512Attack.png");
    public static String BG_SMALL_SKILL = SpireAnniversary6Mod.makeImagePath("cards/" + MirrorZone.ID + "/MirrorMoveCardBack512Skill.png");
    public static String BG_SMALL_POWER = SpireAnniversary6Mod.makeImagePath("cards/" + MirrorZone.ID + "/MirrorMoveCardBack512Power.png");
    public static String BG_LARGE_ATTACK = SpireAnniversary6Mod.makeImagePath("cards/" + MirrorZone.ID + "/MirrorMoveCardBack1024Attack.png");
    public static String BG_LARGE_SKILL = SpireAnniversary6Mod.makeImagePath("cards/" + MirrorZone.ID + "/MirrorMoveCardBack1024Skill.png");
    public static String BG_LARGE_POWER = SpireAnniversary6Mod.makeImagePath("cards/" + MirrorZone.ID + "/MirrorMoveCardBack1024Power.png");

    // description strings
    public static String
            blockDesc, damageDesc, multiDesc, drawDesc, chaosDesc, chaosDescPlus,
            buffDesc, debuffDesc,
            exhaustOtherDesc, gainEDesc, vampireDesc, strengthDownDesc, exhaustDesc,
            tempHpDesc, removeDebuffDesc;

    public static boolean descriptionInitialized = false;

    public MirrorMoveData moveData = null;
    int baseCost;
    public boolean gainEnergy = false;

    public MirrorMove() {
        super(ID, MirrorZone.ID, COST, TYPE, RARITY, TARGET, CardColor.COLORLESS);
        isEthereal = true;
        exhaust = true;

        setBackgroundTexture(BG_SMALL_SKILL, BG_LARGE_SKILL);
    }

    public static void initializeDescriptionParts() {
        if (descriptionInitialized) return;
        descriptionInitialized = true;
        blockDesc = CardCrawlGame.languagePack.getCardStrings("Defend_B").DESCRIPTION;
        damageDesc = CardCrawlGame.languagePack.getCardStrings("Strike_B").DESCRIPTION;
        multiDesc = CardCrawlGame.languagePack.getCardStrings("Expunger").EXTENDED_DESCRIPTION[0];
        drawDesc = CardCrawlGame.languagePack.getCardStrings("Skim").DESCRIPTION;
        strengthDownDesc = CardCrawlGame.languagePack.getCardStrings("Disarm").DESCRIPTION;
        exhaustDesc = CardCrawlGame.languagePack.getCardStrings("Slimed").DESCRIPTION;
        chaosDesc = CardCrawlGame.languagePack.getCardStrings("Chaos").DESCRIPTION;
        chaosDescPlus = CardCrawlGame.languagePack.getCardStrings("Chaos").UPGRADE_DESCRIPTION;
        buffDesc = EXTENDED_DESCRIPTION[0];
        debuffDesc = EXTENDED_DESCRIPTION[1];
        exhaustOtherDesc = EXTENDED_DESCRIPTION[2];
        gainEDesc = EXTENDED_DESCRIPTION[3];
        vampireDesc = EXTENDED_DESCRIPTION[4];
        tempHpDesc = EXTENDED_DESCRIPTION[5];
        removeDebuffDesc = EXTENDED_DESCRIPTION[6];
    }

    // Used when calculating the cost of Mirror Move.
    // Cost is set to X if the card's "power"(equivalent to damage) is less than
    // costBaseThresholdMatrix[act][X] + costThresholdRangeMatrix[act][X] * rng(0~1).
    public static float[][] costBaseThresholdMatrix = new float[][]{
            null,
            new float[]{6, 12, 24, 40},
            new float[]{7, 13.5f, 26, 43},
            new float[]{8, 15, 28, 46}
    };
    public static float[][] costThresholdRangeMatrix = new float[][]{
            null,
            new float[]{2, 4, 6, 10},
            new float[]{2, 4.5f, 7, 12},
            new float[]{2, 5, 8, 14}
    };

    @Override
    public void applyPowers() {
        if (moveData == null && MirrorZone.chosenMoveData != null) {
            setMoveData(MirrorZone.chosenMoveData, true);
        }
        super.applyPowers();
    }

    public static AbstractPower getBuffPower(AbstractCreature owner, String id, int amount) {
        switch (id) {
            case StrengthPower.POWER_ID:
                return new StrengthPower(owner, amount);
            case ArtifactPower.POWER_ID:
                return new ArtifactPower(owner, amount);
            case PlatedArmorPower.POWER_ID:
                return new PlatedArmorPower(owner, amount);
            case MetallicizePower.POWER_ID:
                return new MetallicizePower(owner, amount);
            case ThornsPower.POWER_ID:
                return new ThornsPower(owner, amount);
            case RitualPower.POWER_ID:
                return new RitualPower(owner, amount, true);
            case AngerPower.POWER_ID:
                return new AngerPower(owner, amount);
            case IntangiblePower.POWER_ID:
                return new IntangiblePlayerPower(owner, amount);
            case BufferPower.POWER_ID:
                return new BufferPower(owner, amount);
            default:
                return new ReflectivePower(owner, amount);
        }
    }

    public static AbstractPower getDebuffPower(AbstractCreature target, AbstractCreature source, String id, int amount) {
        switch (id) {
            case StrengthPower.POWER_ID:
                return new StrengthPower(target, -amount);
            case VulnerablePower.POWER_ID:
                return new VulnerablePower(target, amount, false);
            case WeakPower.POWER_ID:
                return new WeakPower(target, amount, false);
            case PoisonPower.POWER_ID:
                return new PoisonPower(target, source, amount);
            case ConstrictedPower.POWER_ID:
                return new ConstrictedPower(target, source, amount);
            default:
                return new ShatteredPower(target, amount);
        }
    }

    public void setMoveData(MirrorMoveData data, boolean initialize) {
        moveData = data;
        if (initialize && data != null) {
            baseDamage = data.damage;
            baseBlock = data.block;
            calculateCost();
        }
        setType();
        setTexture();
        updateDescription();
    }

    private void setType() {
        if (moveData == null || moveData.isEmpty()) {
            this.type = AbstractCard.CardType.SKILL;
            this.baseCost = this.cost = this.costForTurn = -2;
            this.target = CardTarget.NONE;
        } else if (baseDamage > 0) {
            this.type = AbstractCard.CardType.ATTACK;
            this.target = AbstractCard.CardTarget.ENEMY;
        } else if (!moveData.debuffs.isEmpty()) {
            this.type = AbstractCard.CardType.SKILL;
            this.target = AbstractCard.CardTarget.ENEMY;
        } else if (!moveData.buffs.isEmpty()) {
            this.type = AbstractCard.CardType.POWER;
            this.target = AbstractCard.CardTarget.SELF;
        } else {
            this.type = AbstractCard.CardType.SKILL;
            this.target = CardTarget.SELF;
        }

        switch (type) {
            case ATTACK:
                setBackgroundTexture(BG_SMALL_ATTACK, BG_LARGE_ATTACK);
                break;
            case SKILL:
                setBackgroundTexture(BG_SMALL_SKILL, BG_LARGE_SKILL);
                break;
            case POWER:
                setBackgroundTexture(BG_SMALL_POWER, BG_LARGE_POWER);
                break;
        }

        exhaust = (type != AbstractCard.CardType.POWER);
    }

    private void setTexture() {
        if (moveData == null) {
            loadCardImage(getPortraitId(null));
        } else {
            loadCardImage(getPortraitId(moveData.monster.id));
        }
    }

    private void calculateCost() {
        if (moveData == null) {
            return;
        }

        // 1 cardPower is equivalent to 1 damage.
        float cardPower = Math.max(baseDamage, 0);
        if (moveData.hits > 1) {
            cardPower = cardPower * moveData.hits + moveData.hits - 1;
        }
        if (moveData.vampire) {
            cardPower *= 2;
        }

        cardPower += baseBlock * 1.18f;

        for (Map.Entry<String, Integer> entry : moveData.buffs.entrySet()) {
            String id = entry.getKey();
            int magic = entry.getValue();
            switch (id) {
                case StrengthPower.POWER_ID:
                    cardPower += magic * 5.1f;
                    break;
                case ArtifactPower.POWER_ID:
                case PlatedArmorPower.POWER_ID:
                case ThornsPower.POWER_ID:
                    cardPower += magic * 3.2f;
                    break;
                case MetallicizePower.POWER_ID:
                    cardPower += magic * 3.5f;
                    break;
                case RitualPower.POWER_ID:
                    // In act 1, Ritual 3 always costs 3, Ritual 4+ always costs 4
                    cardPower += magic * 12.5f;
                    break;
                case AngerPower.POWER_ID:
                    cardPower += magic * 8.0f;
                    break;
                case IntangiblePower.POWER_ID:
                    cardPower += magic * 18.0f;
                    break;
                case BufferPower.POWER_ID:
                    cardPower += magic * 10.0f;
                    break;
                default:
                    // ReflectivePower
                    cardPower += magic * 1.5f;
                    break;
            }
        }
        for (Map.Entry<String, Integer> entry : moveData.debuffs.entrySet()) {
            String id = entry.getKey();
            int magic = entry.getValue();
            switch (id) {
                case StrengthPower.POWER_ID:
                    cardPower += -magic * 4.0f;
                    break;
                case VulnerablePower.POWER_ID:
                case WeakPower.POWER_ID:
                    cardPower += magic * 3.0f;
                    break;
                case PoisonPower.POWER_ID:
                case ConstrictedPower.POWER_ID:
                    cardPower += magic * 1.5f;
                    break;
                default:
                    // ShatteredPower
                    cardPower += magic * 5.0f;
                    break;
            }
        }

        cardPower += moveData.draw * 3.0f;
        cardPower += moveData.randomOrbs * 7.5f;
        cardPower += moveData.tempHp * 1.3f;

        if (moveData.removeDebuff) cardPower += 8.0f;
        if (moveData.exhaustOther) cardPower += 2.0f;

        // Calculate Cost
        float costRngValue = AbstractDungeon.cardRandomRng.random();

        int actNum = Math.max(Math.min(AbstractDungeon.actNum, 3), 1);
        for (baseCost = 0; baseCost < 4; baseCost++) {
            if (cardPower <= costBaseThresholdMatrix[actNum][baseCost] + costThresholdRangeMatrix[actNum][baseCost] * costRngValue) {
                break;
            }
        }
        cost = costForTurn = baseCost;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (moveData == null || moveData.isEmpty()) {
            return;
        }
        if (gainEnergy) {
            addToBot(new GainEnergyAction(1));
        }
        if (baseBlock > 0) {
            addToBot(new GainBlockAction(p, p, block));
        }
        if (baseDamage > 0) {
            int hits = Math.max(moveData.hits, 1);
            AbstractGameAction.AttackEffect effect;
            switch (baseCost) {
                case 0:
                case 1:
                    effect = AbstractGameAction.AttackEffect.SLASH_DIAGONAL;
                    break;
                case 2:
                    effect = AbstractGameAction.AttackEffect.BLUNT_LIGHT;
                    break;
                default:
                    effect = AbstractGameAction.AttackEffect.BLUNT_HEAVY;
                    break;
            }
            if (moveData.vampire) {
                effect = AbstractGameAction.AttackEffect.NONE;
                addToBot(new VFXAction(new BiteEffect(m.hb.cX, m.hb.cY, Color.GOLD.cpy()), 0.0F));
            } else if (baseCost >= 3 && hits <= 1) {
                addToBot(new VFXAction(new WeightyImpactEffect(m.hb.cX, m.hb.cY)));
            }
            for (int i = 0; i < hits; i++) {
                if (moveData.vampire) {
                    addToBot(new VampireDamageAction(m, new DamageInfo(p, damage, damageTypeForTurn), effect));
                } else {
                    addToBot(new DamageAction(m, new DamageInfo(p, damage, damageTypeForTurn), effect));
                }
            }
        }
        if (moveData.draw > 0) {
            addToBot(new DrawCardAction(p, moveData.draw));
        }
        if (moveData.removeDebuff) {
            addToBot(new RemoveDebuffsAction(p));
        }
        for (String id : MirrorMoveData.acceptableBuffIds) {
            // iterating over acceptableBuffIds to make the buff order consistent
            if (moveData.buffs.containsKey(id)) {
                AbstractPower pow = getBuffPower(p, id, moveData.buffs.get(id));
                addToBot(new ApplyPowerAction(p, p, pow));
            }
        }
        for (String id : MirrorMoveData.acceptableDebuffIds) {
            // iterating over acceptableDebuffIds to make the debuff order consistent
            if (moveData.debuffs.containsKey(id)) {
                AbstractPower pow = getDebuffPower(m, p, id, moveData.debuffs.get(id));
                addToBot(new ApplyPowerAction(m, p, pow));
            }
        }

        for (int i = 0; i < moveData.randomOrbs; i++) {
            addToBot(new ChannelAction(AbstractOrb.getRandomOrb(true)));
        }

        if (moveData.tempHp > 0) {
            addToBot(new AddTemporaryHPAction(p, p, moveData.tempHp));
        }
        if (moveData.exhaustOther) {
            addToBot(new ExhaustAction(1, false));
        }
    }

    String buffDesc(String name, int amount, boolean capitalize) {
        if (capitalize) {
            return buffDesc.replace("{0}", TipHelper.capitalize(name)).replace("!M!", Integer.toString(amount));
        } else {
            return buffDesc.replace("{0}", name).replace("!M!", Integer.toString(amount));
        }
    }

    String debuffDesc(String name, int amount, boolean capitalize) {
        if (capitalize) {
            return debuffDesc.replace("{0}", TipHelper.capitalize(name)).replace("!M!", Integer.toString(amount));
        } else {
            return debuffDesc.replace("{0}", name).replace("!M!", Integer.toString(amount));
        }
    }

    public void updateDescription() {
        if (moveData == null) {
            rawDescription = DESCRIPTION;
            initializeDescription();
            return;
        }
        ArrayList<String> desc = new ArrayList<>();
        desc.add(TipHelper.capitalize(GameDictionary.ETHEREAL.NAMES[0]) + LocalizedStrings.PERIOD);
        if (gainEnergy) {
            desc.add(gainEDesc);
        }
        if (baseBlock > 0) {
            desc.add(blockDesc);
        }
        if (baseDamage > 0) {
            String result = moveData.hits > 1 ? multiDesc.replace("!M!", String.valueOf(moveData.hits)) : damageDesc;
            if (moveData.vampire) {
                result += " " + vampireDesc;
            }
            desc.add(result);
        }
        if (moveData.draw > 0) {
            desc.add(drawDesc.replace("!M!", Integer.toString(moveData.draw)));
        }

        if (moveData.removeDebuff) {
            desc.add(removeDebuffDesc);
        }
        for (String id : MirrorMoveData.acceptableBuffIds) {
            // iterating over acceptableBuffIds to make the buff order consistent
            if (moveData.buffs.containsKey(id)) {
                int amount = moveData.buffs.get(id);
                switch (id) {
                    case StrengthPower.POWER_ID:
                        desc.add(buffDesc(GameDictionary.STRENGTH.NAMES[0], amount, true));
                        break;
                    case ArtifactPower.POWER_ID:
                        desc.add(buffDesc(GameDictionary.ARTIFACT.NAMES[0], amount, true));
                        break;
                    case PlatedArmorPower.POWER_ID:
                        desc.add(buffDesc(PlatedArmorPower.NAME, amount, false));
                        break;
                    case ThornsPower.POWER_ID:
                        desc.add(buffDesc(GameDictionary.THORNS.NAMES[0], amount, true));
                        break;
                    case MetallicizePower.POWER_ID:
                        desc.add(buffDesc(MetallicizePower.NAME, amount, false));
                        break;
                    case RitualPower.POWER_ID:
                        desc.add(buffDesc(GameDictionary.RITUAL.NAMES[0], amount, true));
                        break;
                    case AngerPower.POWER_ID:
                        desc.add(buffDesc(AngerPower.NAME, amount, false));
                        break;
                    case IntangiblePower.POWER_ID:
                        desc.add(buffDesc(GameDictionary.INTANGIBLE.NAMES[0], amount, false));
                        break;
                    case BufferPower.POWER_ID:
                        desc.add(buffDesc(BufferPower.NAME, amount, false));
                        break;
                    default:
                        // ReflectivePower
                        desc.add(buffDesc(SpireAnniversary6Mod.makeID(ReflectivePower.NAME), amount, false));
                        break;
                }
            }
        }

        int disarm = 0;
        for (String id : MirrorMoveData.acceptableDebuffIds) {
            if (moveData.debuffs.containsKey(id)) {
                int amount = moveData.debuffs.get(id);
                switch (id) {
                    case StrengthPower.POWER_ID:
                        disarm = amount;
                        break;
                    case VulnerablePower.POWER_ID:
                        desc.add(debuffDesc(GameDictionary.VULNERABLE.NAMES[0], amount, true));
                        break;
                    case WeakPower.POWER_ID:
                        desc.add(debuffDesc(GameDictionary.WEAK.NAMES[0], amount, true));
                        break;
                    case PoisonPower.POWER_ID:
                        desc.add(debuffDesc(GameDictionary.POISON.NAMES[0], amount, true));
                        break;
                    case ConstrictedPower.POWER_ID:
                        desc.add(debuffDesc(ConstrictedPower.NAME, amount, false));
                        break;
                    default:
                        // ShatteredPower
                        desc.add(debuffDesc(SpireAnniversary6Mod.makeID(ShatteredPower.NAME), amount, false));
                        break;
                }
            }
        }

        if (moveData.randomOrbs == 1) {
            desc.add(chaosDesc.replace("!M!", "1"));
        } else if (moveData.randomOrbs >= 2) {
            desc.add(chaosDescPlus.replace("!M!", Integer.toString(moveData.randomOrbs)));
        }

        if (moveData.tempHp > 0) {
            desc.add(tempHpDesc.replace("!M!", Integer.toString(moveData.tempHp)));
        }
        if (moveData.exhaustOther) {
            desc.add(exhaustOtherDesc);
        }
        if (disarm > 0) {
            desc.add(strengthDownDesc.replace("!M!", Integer.toString(disarm)));
        } else if (exhaust) {
            desc.add(exhaustDesc);
        }
        rawDescription = String.join(" NL ", desc);
        initializeDescription();
    }

    @Override
    public void upp() {
        if (moveData == null) return;
        if (baseCost > 0) {
            upgradeBaseCost(baseCost - 1);
        } else {
            gainEnergy = true;
            updateDescription();
        }
    }

    @Override
    public AbstractCard makeStatEquivalentCopy() {
        MirrorMove card = (MirrorMove) super.makeStatEquivalentCopy();
        card.baseCost = baseCost;
        card.gainEnergy = gainEnergy;
        card.exhaust = exhaust;
        card.setMoveData(moveData, false);

        return card;
    }

    public static String getPortraitId(String monsterId) {
        String prefix = getCardTextureString(MirrorZone.ID + "/" + RAW_ID, CardType.SKILL);
        if (monsterId == null) {
            return prefix;
        } else {
            return prefix + "*_*" + monsterId;
        }
    }

    @Override
    protected Texture getPortraitImage() {
        if (moveData == null || MirrorZone.textureMissing(moveData.monster.id)) {
            return super.getPortraitImage();
        } else {
            return MirrorZone.largeTextures.get(moveData.monster.id);
        }
    }
}
