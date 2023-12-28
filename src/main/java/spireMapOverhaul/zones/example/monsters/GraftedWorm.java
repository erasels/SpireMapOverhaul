package spireMapOverhaul.zones.example.monsters;

import basemod.abstracts.CustomMonster;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.AnimateSlowAttackAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.RollMoveAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.powers.LoseStrengthPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import spireMapOverhaul.SpireAnniversary6Mod;
import spireMapOverhaul.zones.example.powers.LeafSporesPower;

import static spireMapOverhaul.util.Wiz.*;

public class GraftedWorm extends CustomMonster
{
    public static final String ID = SpireAnniversary6Mod.makeID("GraftedWorm");
    private static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID);
    public static final String NAME = monsterStrings.NAME;
    public static final String[] MOVES = monsterStrings.MOVES;
    private static final String IMG = SpireAnniversary6Mod.makeImagePath("monsters/GraftedWorm/GraftedWorm.png");
    private boolean firstMove = true;
    private static final byte LEAF_BLADE_ATTACK = 1;
    private static final int LEAF_BLADE_DAMAGE = 4;
    private static final int A2_LEAF_BLADE_DAMAGE = 5;
    private static final int LEAF_SPORES_AMOUNT = 1;
    private static final int A17_LEAF_SPORES_AMOUNT = 2;
    private static final int TEMPORARY_STRENGTH = 0;
    private static final int A17_TEMPORARY_STRENGTH = 1;
    private static final int HP_MIN = 11;
    private static final int HP_MAX = 13;
    private static final int A7_HP_MIN = 12;
    private static final int A7_HP_MAX = 14;
    private int leafBladeDamage;
    private int leafSporesAmount;
    private int temporaryStrength;

    public GraftedWorm() {
        this(0.0f, 0.0f);
    }

    public GraftedWorm(final float x, final float y) {
        super(GraftedWorm.NAME, ID, HP_MAX, -5.0F, 0, 155.0f, 150.0f, IMG, x, y);
        type = EnemyType.NORMAL;
        if (AbstractDungeon.ascensionLevel >= 7) {
            setHp(A7_HP_MIN, A7_HP_MAX);
        } else {
            setHp(HP_MIN, HP_MAX);
        }

        if (AbstractDungeon.ascensionLevel >= 2) {
            leafBladeDamage = A2_LEAF_BLADE_DAMAGE;
        } else {
            leafBladeDamage = LEAF_BLADE_DAMAGE;
        }
        damage.add(new DamageInfo(this, leafBladeDamage));

        if (AbstractDungeon.ascensionLevel >= 17) {
            leafSporesAmount = A17_LEAF_SPORES_AMOUNT;
            temporaryStrength = A17_TEMPORARY_STRENGTH;
        } else {
            leafSporesAmount = LEAF_SPORES_AMOUNT;
            temporaryStrength = TEMPORARY_STRENGTH;
        }
    }

    @Override
    public void usePreBattleAction() {
        atb(new ApplyPowerAction(this, this, new LeafSporesPower(this, leafSporesAmount)));
        if (temporaryStrength > 0) {
            atb(new ApplyPowerAction(this, this, new StrengthPower(this, temporaryStrength), temporaryStrength));
            atb(new ApplyPowerAction(this, this, new LoseStrengthPower(this, temporaryStrength), temporaryStrength));
        }
    }

    @Override
    public void takeTurn() {
        if (firstMove) {
            firstMove = false;
        }
        switch (nextMove) {
            case LEAF_BLADE_ATTACK:
                atb(new AnimateSlowAttackAction(this));
                atb(new DamageAction(AbstractDungeon.player, damage.get(0), AbstractGameAction.AttackEffect.SLASH_VERTICAL));
                break;
        }
        AbstractDungeon.actionManager.addToBottom(new RollMoveAction(this));
    }

    @Override
    protected void getMove(final int num) {
        setMove(MOVES[0], LEAF_BLADE_ATTACK, Intent.ATTACK, leafBladeDamage);
    }
}