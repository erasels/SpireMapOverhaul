package spireMapOverhaul.zones.invasion.monsters;

import basemod.abstracts.CustomMonster;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.FastShakeAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.DexterityPower;
import com.megacrit.cardcrawl.powers.FocusPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.vfx.combat.SmallLaserEffect;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import spireMapOverhaul.SpireAnniversary6Mod;
import spireMapOverhaul.zones.invasion.actions.SummonElementalAction;
import spireMapOverhaul.zones.invasion.powers.SummoningPortalPower;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class ElementalPortal extends CustomMonster {
    public static final Logger logger = LogManager.getLogger(ElementalPortal.class.getName());
    public static final String ID = SpireAnniversary6Mod.makeID("ElementalPortal");
    private static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID);
    public static final String NAME = monsterStrings.NAME;
    public static final String[] MOVES = monsterStrings.MOVES;
    private static final String IMG = SpireAnniversary6Mod.makeImagePath("monsters/Invasion/ElementalPortal/ElementalPortal.png");
    public static final int MAX_ELEMENTALS_ON_FIELD = 3;
    private static final byte PORTAL_MOVE = 1;
    private static final byte STRENGTHENING_RESTORATION_BUFF = 2;
    private static final byte CHAOTIC_ENERGIES_DEBUFF = 3;
    private static final byte ELEMENTAL_BLAST_ATTACK = 4;
    private static final int ELEMENTAL_BLAST_DAMAGE = 16;
    private static final int A3_ELEMENTAL_BLAST_DAMAGE = 18;
    private static final int STRENGTHENING_RESTORATION_STRENGTH = 1;
    private static final int STRENGTHENING_RESTORATION_HEALING = 2;
    private static final int CHAOTIC_ENERGIES_STAT_AMOUNT = 1;
    private static final int ELEMENTAL_COUNT = 5;
    private static final int A18_ELEMENTAL_COUNT = 6;
    private static final int HP_MIN = 48;
    private static final int HP_MAX = 49;
    private static final int A8_HP_MIN = 51;
    private static final int A8_HP_MAX = 52;
    private int elementalBlastDamage;
    private int elementalCount;
    private ArrayList<String> elementalIDs;
    private final AbstractMonster[] activeElementals = new AbstractMonster[3];

    public ElementalPortal() {
        this(0.0f, 0.0f);
    }

    public ElementalPortal(final float x, final float y) {
        super(ElementalPortal.NAME, ID, HP_MAX, -5.0F, 0, 300, 300, IMG, x, y);
        this.type = EnemyType.ELITE;
        this.dialogX = (this.hb_x - 70.0F) * Settings.scale;
        this.dialogY -= (this.hb_y - 55.0F) * Settings.scale;

        if (AbstractDungeon.ascensionLevel >= 8) {
            this.setHp(A8_HP_MIN, A8_HP_MAX);
        } else {
            this.setHp(HP_MIN, HP_MAX);
        }

        if (AbstractDungeon.ascensionLevel >= 18) {
            this.elementalCount = A18_ELEMENTAL_COUNT;
        }
        else {
            this.elementalCount = ELEMENTAL_COUNT;
        }

        if (AbstractDungeon.ascensionLevel >= 3) {
            this.elementalBlastDamage = A3_ELEMENTAL_BLAST_DAMAGE;
        }
        else {
            this.elementalBlastDamage = ELEMENTAL_BLAST_DAMAGE;
        }

        this.damage.add(new DamageInfo(this, this.elementalBlastDamage));
    }

    @Override
    public void usePreBattleAction() {
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new SummoningPortalPower(this, this.elementalCount), this.elementalCount));
        this.summonElementalsUntilFull(true);
    }

    @Override
    public void takeTurn() {
        switch (this.nextMove) {
            case PORTAL_MOVE:
                this.summonElementalsUntilFull(false);
                break;
            case STRENGTHENING_RESTORATION_BUFF:
                for (AbstractMonster m : AbstractDungeon.getMonsters().monsters) {
                    if (m == this || !m.isDying) {
                        AbstractDungeon.actionManager.addToBottom(new HealAction(m, this, STRENGTHENING_RESTORATION_HEALING));
                        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(m, this, new StrengthPower(m, STRENGTHENING_RESTORATION_STRENGTH), STRENGTHENING_RESTORATION_STRENGTH));
                    }
                }
                break;
            case CHAOTIC_ENERGIES_DEBUFF:
                AbstractDungeon.actionManager.addToBottom(new FastShakeAction(this, 0.5F, 0.2F));
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, this, new StrengthPower(AbstractDungeon.player, -CHAOTIC_ENERGIES_STAT_AMOUNT), -CHAOTIC_ENERGIES_STAT_AMOUNT));
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, this, new DexterityPower(AbstractDungeon.player, -CHAOTIC_ENERGIES_STAT_AMOUNT), -CHAOTIC_ENERGIES_STAT_AMOUNT));
                if (!AbstractDungeon.player.orbs.isEmpty()) {
                    AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, this, new FocusPower(AbstractDungeon.player, -CHAOTIC_ENERGIES_STAT_AMOUNT), -CHAOTIC_ENERGIES_STAT_AMOUNT));
                }
                break;
            case ELEMENTAL_BLAST_ATTACK:
                AbstractDungeon.actionManager.addToBottom(new SFXAction("ATTACK_MAGIC_BEAM_SHORT", 0.5F));
                if (Settings.FAST_MODE) {
                    AbstractDungeon.actionManager.addToBottom(new VFXAction(new SmallLaserEffect(AbstractDungeon.player.hb.cX, AbstractDungeon.player.hb.cY, this.hb.cX, this.hb.cY), 0.1F));
                } else {
                    AbstractDungeon.actionManager.addToBottom(new VFXAction(new SmallLaserEffect(AbstractDungeon.player.hb.cX, AbstractDungeon.player.hb.cY, this.hb.cX, this.hb.cY), 0.3F));
                }
                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage.get(0), AbstractGameAction.AttackEffect.NONE));
                break;
        }
        AbstractDungeon.actionManager.addToBottom(new RollMoveAction(this));
    }

    @Override
    protected void getMove(final int num) {
        if (!this.getElementalIDs().isEmpty()) {
            this.setMove(MOVES[0], PORTAL_MOVE, Intent.MAGIC);
        }
        else if (this.lastMove(PORTAL_MOVE)) {
            this.setMove(MOVES[1], STRENGTHENING_RESTORATION_BUFF, Intent.BUFF);
        }
        else if (this.lastMove(STRENGTHENING_RESTORATION_BUFF)) {
            this.setMove(MOVES[2], CHAOTIC_ENERGIES_DEBUFF, Intent.STRONG_DEBUFF);
        }
        else {
            this.setMove(MOVES[3], ELEMENTAL_BLAST_ATTACK, Intent.ATTACK, this.elementalBlastDamage);
        }
    }

    private ArrayList<String> getElementalIDs() {
        if (this.elementalIDs == null) {
            this.elementalIDs = this.getElementalsToSummon();
        }
        return this.elementalIDs;
    }

    private ArrayList<String> getElementalsToSummon() {
        Random rng = new Random(Settings.seed + AbstractDungeon.floorNum);

        ArrayList<String> elementals = new ArrayList<>();
        elementals.add(OrbOfFire.ID);
        elementals.add(LivingStormcloud.ID);
        elementals.add(OpulentOffering.ID);
        elementals.add(ShimmeringMirage.ID);

        ArrayList<String> potentialElementals = new ArrayList<>();
        potentialElementals.add(OrbOfFire.ID);
        potentialElementals.add(LivingStormcloud.ID);
        potentialElementals.add(OpulentOffering.ID);
        potentialElementals.add(ShimmeringMirage.ID);
        Collections.shuffle(potentialElementals, rng);

        int elementalsNeeded = this.elementalCount - elementals.size();
        for (int i = 0; i < elementalsNeeded; i++) {
            elementals.add(potentialElementals.get(i));
        }

        Collections.shuffle(elementals, rng);
        logger.info("ElementalPortal will summon: " + String.join(", ", elementals));
        return elementals;
    }

    private int getFirstFreeElementalSlot(){
        for(int i = 0; i < this.activeElementals.length; ++i) {
            if (this.activeElementals[i] == null || this.activeElementals[i].isDying) {
                return i;
            }
        }

        return -1;
    }

    private void summonNextElemental(float xPosition, int slot, boolean firstTurn) {
        logger.info("Summoning to x position: " + xPosition);
        String elementalID = this.getElementalIDs().get(0);
        this.getElementalIDs().remove(0);
        AbstractDungeon.actionManager.addToBottom(new SummonElementalAction(elementalID, xPosition, 125.0F, this.activeElementals, slot, 0, 0, firstTurn));
        AbstractDungeon.actionManager.addToBottom(new ReducePowerAction(this, this, SummoningPortalPower.POWER_ID, 1));
    }

    private float slotToXPosition(int slot) {
        switch (slot) {
            case 1: return -340.0F;
            case 2: return -560.0F;
            default: return -120.0F;
        }
    }

    private void summonElementalsUntilFull(boolean firstTurn) {
        logger.info("Summoning elementals until the field is full");
        int otherLivingMonsters = (int) AbstractDungeon.getMonsters().monsters.stream().filter(m -> m != null && m != this && !m.isDying).count();
        int spotsToFill = MAX_ELEMENTALS_ON_FIELD - otherLivingMonsters;
        int elementalsLeft = this.getElementalIDs().size();
        int numberToSummon = Math.min(spotsToFill, elementalsLeft);
        logger.info("Number to summon: " + numberToSummon);

        for (int i = 0; i < numberToSummon; i++) {
            int slot = this.getFirstFreeElementalSlot();
            float xPosition = this.slotToXPosition(slot);
            this.summonNextElemental(xPosition, slot, firstTurn);
        }
    }
}
