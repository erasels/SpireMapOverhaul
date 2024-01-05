package spireMapOverhaul.zones.invasion.monsters;

import basemod.abstracts.CustomMonster;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.AnimateSlowAttackAction;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.StrengthPower;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import spireMapOverhaul.SpireAnniversary6Mod;
import spireMapOverhaul.util.MonsterUtil;
import spireMapOverhaul.zones.invasion.actions.SummonRoyalProtectorAction;
import spireMapOverhaul.zones.invasion.powers.PrimevalCallPower;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

public class PrimevalQueen extends CustomMonster {
    public static final Logger logger = LogManager.getLogger(PrimevalQueen.class.getName());
    public static final String ID = SpireAnniversary6Mod.makeID("PrimevalQueen");
    private static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID);
    public static final String NAME = monsterStrings.NAME;
    public static final String[] MOVES = monsterStrings.MOVES;
    private static final String IMG = SpireAnniversary6Mod.makeImagePath("monsters/Invasion/PrimevalQueen/PrimevalQueen.png");
    private boolean firstMove = true;
    private static final byte SCYTHE_ATTACK = 1;
    private static final byte STRENGTHEN_CARAPACE_BUFF = 2;
    private static final byte CALL_PROTECTOR_MOVE = 3;
    private static final int SCYTHE_DAMAGE = 22;
    private static final int A3_SCYTHE_DAMAGE = 24;
    private static final int STRENGTHEN_CARAPACE_BLOCK = 20;
    private static final int A8_STRENGTHEN_CARAPACE_BLOCK = 25;
    private static final int STRENGTHEN_CARAPACE_STRENGTH = 3;
    private static final int A18_STRENGTHEN_CARAPACE_STRENGTH = 6;
    private static final int STARTING_SUMMONS = 1;
    private static final int A18_STARTING_SUMMONS = 2;
    private static final int HP_MIN = 210;
    private static final int HP_MAX = 210;
    private static final int A8_HP_MIN = 230;
    private static final int A8_HP_MAX = 230;
    private int scytheDamage;
    private int strengthenCarapaceBlock;
    private int strengthenCarapaceStrength;
    private int startingSummons;
    private AbstractMonster[] activeMinions = new AbstractMonster[5];

    public PrimevalQueen() {
        this(0.0f, 0.0f);
    }

    public PrimevalQueen(final float x, final float y) {
        super(PrimevalQueen.NAME, ID, HP_MAX, -5.0F, 0, 305.0f, 305.0f, IMG, x, y);
        this.type = EnemyType.ELITE;
        if (AbstractDungeon.ascensionLevel >= 8) {
            this.setHp(A8_HP_MIN, A8_HP_MAX);
            this.strengthenCarapaceBlock = A8_STRENGTHEN_CARAPACE_BLOCK;
        } else {
            this.setHp(HP_MIN, HP_MAX);
            this.strengthenCarapaceBlock = STRENGTHEN_CARAPACE_BLOCK;
        }

        if (AbstractDungeon.ascensionLevel >= 3) {
            this.scytheDamage = A3_SCYTHE_DAMAGE;
        } else {
            this.scytheDamage = SCYTHE_DAMAGE;
        }
        this.damage.add(new DamageInfo(this, this.scytheDamage));

        if (AbstractDungeon.ascensionLevel >= 18) {
            this.strengthenCarapaceStrength = A18_STRENGTHEN_CARAPACE_STRENGTH;
            this.startingSummons = A18_STARTING_SUMMONS;
        } else {
            this.strengthenCarapaceStrength = STRENGTHEN_CARAPACE_STRENGTH;
            this.startingSummons = STARTING_SUMMONS;
        }
    }

    @Override
    public void usePreBattleAction() {
        this.addToBot(new ApplyPowerAction(this, this, new PrimevalCallPower(this)));
        this.summonRoyalProtectors(startingSummons, true, false);
    }

    @Override
    public void takeTurn() {
        if (this.firstMove) {
            this.firstMove = false;
        }
        switch (this.nextMove) {
            case SCYTHE_ATTACK:
                AbstractDungeon.actionManager.addToBottom(new AnimateSlowAttackAction(this));
                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage.get(0), AbstractGameAction.AttackEffect.SLASH_HEAVY));
                break;
            case STRENGTHEN_CARAPACE_BUFF:
                AbstractDungeon.actionManager.addToBottom(new GainBlockAction(this, this.strengthenCarapaceBlock));
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new StrengthPower(this, this.strengthenCarapaceStrength), this.strengthenCarapaceStrength));
                break;
            case CALL_PROTECTOR_MOVE:
                this.summonRoyalProtectors(1, false, false);
                break;
        }
        AbstractDungeon.actionManager.addToBottom(new RollMoveAction(this));
    }

    @Override
    protected void getMove(final int num) {
        int minionCount = 0;
        for (AbstractMonster m : AbstractDungeon.getCurrRoom().monsters.monsters) {
            if (m != this && !m.isDying && m.id.equals(RoyalProtector.ID)) {
                minionCount++;
            }
        }

        List<Byte> moveOptions = new ArrayList<>();
        moveOptions.add(SCYTHE_ATTACK);
        moveOptions.add(STRENGTHEN_CARAPACE_BUFF);
        moveOptions.add(CALL_PROTECTOR_MOVE);

        Consumer<Byte> remove = m -> moveOptions.remove((Object)m);
        if (this.firstMove || (!this.lastMove(SCYTHE_ATTACK) && !this.lastMoveBefore(SCYTHE_ATTACK) && !MonsterUtil.lastMoveX(this, SCYTHE_ATTACK, 3))) {
            remove.accept(STRENGTHEN_CARAPACE_BUFF);
            remove.accept(CALL_PROTECTOR_MOVE);
        }
        if (minionCount > 2) {
            remove.accept(CALL_PROTECTOR_MOVE);
        }
        if (this.lastMove(SCYTHE_ATTACK) && this.lastMoveBefore(SCYTHE_ATTACK)) {
            remove.accept(SCYTHE_ATTACK);
        }
        if (this.lastMove(STRENGTHEN_CARAPACE_BUFF)){
            remove.accept(STRENGTHEN_CARAPACE_BUFF);
        }
        if (this.lastMove(CALL_PROTECTOR_MOVE) && this.lastMoveBefore(CALL_PROTECTOR_MOVE)) {
            remove.accept(CALL_PROTECTOR_MOVE);
        }

        byte move;
        if (moveOptions.size() > 1) {
            Collections.shuffle(moveOptions, new java.util.Random(AbstractDungeon.aiRng.randomLong()));
            move = moveOptions.get(0);
        }
        else if (moveOptions.size() == 1) {
            move = moveOptions.get(0);
        }
        else {
            logger.warn("No move options left, defaulting to attack");
            move = SCYTHE_ATTACK;
        }

        if (move == SCYTHE_ATTACK) {
            this.setMove(MOVES[0], SCYTHE_ATTACK, Intent.ATTACK, this.scytheDamage);
        }
        else if (move == STRENGTHEN_CARAPACE_BUFF) {
            this.setMove(MOVES[1], STRENGTHEN_CARAPACE_BUFF, Intent.DEFEND_BUFF);
        }
        else {
            this.setMove(MOVES[2], CALL_PROTECTOR_MOVE, Intent.UNKNOWN);
        }
    }

    @Override
    public void die() {
        super.die();
        for (AbstractMonster m : AbstractDungeon.getCurrRoom().monsters.monsters) {
            if (m.id.equals(RoyalProtector.ID) && !m.isDying) {
                AbstractDungeon.actionManager.addToBottom(new EscapeAction(m));
            }
        }
    }

    public void summonRoyalProtectors(int numberToSummon, boolean firstTurn, boolean endOfTurn) {
        for (int i = 0; i < numberToSummon; i++) {
            int slot = this.getFirstFreeMinionSlot();
            if (slot != -1) {
                float xPosition = this.slotToXPosition(slot);
                AbstractDungeon.actionManager.addToBottom(new SummonRoyalProtectorAction(xPosition, 0.0F, firstTurn, endOfTurn, this.activeMinions, slot));
            }
        }
    }

    private int getFirstFreeMinionSlot() {
        for(int i = 0; i < this.activeMinions.length; ++i) {
            if (this.activeMinions[i] == null || this.activeMinions[i].isDying) {
                return i;
            }
        }

        return -1;
    }

    private float slotToXPosition(int slot) {
        switch (slot) {
            case 0: return -20.0F;
            case 1: return -180.0F;
            case 2: return -340.0F;
            case 3: return -500.0F;
            default: return -660.0F;
        }
    }
}