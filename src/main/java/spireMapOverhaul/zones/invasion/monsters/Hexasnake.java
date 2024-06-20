package spireMapOverhaul.zones.invasion.monsters;

import basemod.abstracts.CustomMonster;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.AnimateFastAttackAction;
import com.megacrit.cardcrawl.actions.animations.FastShakeAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDiscardAction;
import com.megacrit.cardcrawl.actions.common.RollMoveAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.status.Slimed;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.powers.ConstrictedPower;
import spireMapOverhaul.SpireAnniversary6Mod;
import spireMapOverhaul.zones.invasion.powers.SerpentsGazePower;

import static spireMapOverhaul.util.Wiz.atb;

public class Hexasnake extends CustomMonster {
    public static final String ID = SpireAnniversary6Mod.makeID("Hexasnake");
    private static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID);
    public static final String NAME = monsterStrings.NAME;
    public static final String[] MOVES = monsterStrings.MOVES;
    private static final String IMG = SpireAnniversary6Mod.makeMonsterPath("Invasion/Hexasnake/Hexasnake.png");
    private boolean firstMove = true;
    private static final byte SERPENTS_GAZE_DEBUFF = 1;
    private static final byte VENOMOUS_BITE_ATTACK = 2;
    private static final byte CONSTRICT_ATTACK = 3;
    private static final int SERPENTS_GAZE_AMOUNT = 2;
    private static final int A17_SERPENTS_GAZE_AMOUNT = 3;
    private static final int VENOMOUS_BITE_DAMAGE = 2;
    private static final int A2_VENOMOUS_BITE_DAMAGE = 2;
    private static final int VENOMOUS_BITE_HITS = 6;
    private static final int VENOMOUS_BITE_SLIMES = 1;
    private static final int A17_VENOMOUS_BITE_SLIMES = 1;
    private static final int CONSTRICT_DAMAGE = 7;
    private static final int A2_CONSTRICT_DAMAGE = 8;
    private static final int CONSTRICT_AMOUNT = 2;
    private static final int A17_CONSTRICT_AMOUNT = 3;
    private static final int HP_MIN = 60;
    private static final int HP_MAX = 64;
    private static final int A7_HP_MIN = 63;
    private static final int A7_HP_MAX = 67;
    private int serpentsGazeAmount;
    private int venomousBiteDamage;
    private int venomousBiteSlimes;
    private int constrictDamage;
    private int constrictAmount;

    public Hexasnake() {
        this(0.0f, 0.0f);
    }

    public Hexasnake(final float x, final float y) {
        super(Hexasnake.NAME, ID, HP_MAX, -5.0F, 0, 455.0f, 225.0f, IMG, x, y);
        type = EnemyType.NORMAL;
        if (AbstractDungeon.ascensionLevel >= 7) {
            setHp(A7_HP_MIN, A7_HP_MAX);
        } else {
            setHp(HP_MIN, HP_MAX);
        }

        if (AbstractDungeon.ascensionLevel >= 2) {
            venomousBiteDamage = A2_VENOMOUS_BITE_DAMAGE;
            constrictDamage = A2_CONSTRICT_DAMAGE;
        } else {
            venomousBiteDamage = VENOMOUS_BITE_DAMAGE;
            constrictDamage = CONSTRICT_DAMAGE;
        }
        damage.add(new DamageInfo(this, venomousBiteDamage));
        damage.add(new DamageInfo(this, constrictDamage));

        if (AbstractDungeon.ascensionLevel >= 17) {
            serpentsGazeAmount = A17_SERPENTS_GAZE_AMOUNT;
            venomousBiteSlimes = A17_VENOMOUS_BITE_SLIMES;
            constrictAmount = A17_CONSTRICT_AMOUNT;
        } else {
            serpentsGazeAmount = SERPENTS_GAZE_AMOUNT;
            venomousBiteSlimes = VENOMOUS_BITE_SLIMES;
            constrictAmount = CONSTRICT_AMOUNT;
        }
    }

    @Override
    public void takeTurn() {
        if (firstMove) {
            firstMove = false;
        }
        switch (nextMove) {
            case SERPENTS_GAZE_DEBUFF:
                atb(new FastShakeAction(this, 0.5F, 0.2F));
                atb(new ApplyPowerAction(AbstractDungeon.player, this, new SerpentsGazePower(AbstractDungeon.player, serpentsGazeAmount), serpentsGazeAmount));
                break;
            case VENOMOUS_BITE_ATTACK:
                atb(new AnimateFastAttackAction(this));
                for (int i = 0; i < VENOMOUS_BITE_HITS; i++) {
                    atb(new DamageAction(AbstractDungeon.player, damage.get(0), AbstractGameAction.AttackEffect.POISON));
                }
                atb(new MakeTempCardInDiscardAction(new Slimed(), venomousBiteSlimes));
                break;
            case CONSTRICT_ATTACK:
                atb(new FastShakeAction(this, 0.5F, 0.2F));
                atb(new DamageAction(AbstractDungeon.player, damage.get(1), AbstractGameAction.AttackEffect.BLUNT_LIGHT));
                atb(new ApplyPowerAction(AbstractDungeon.player, this, new ConstrictedPower(AbstractDungeon.player, this, constrictAmount), constrictAmount));
                break;
        }
        atb(new RollMoveAction(this));
    }

    @Override
    protected void getMove(final int num) {
        if (firstMove) {
            setMove(MOVES[0], SERPENTS_GAZE_DEBUFF, Intent.STRONG_DEBUFF);
        }
        else if (num < 50) {
            setMove(MOVES[1], VENOMOUS_BITE_ATTACK, Intent.ATTACK_DEBUFF, venomousBiteDamage, VENOMOUS_BITE_HITS, true);
        }
        else {
            setMove(MOVES[2], CONSTRICT_ATTACK, Intent.ATTACK_DEBUFF, constrictDamage);
        }
    }
}