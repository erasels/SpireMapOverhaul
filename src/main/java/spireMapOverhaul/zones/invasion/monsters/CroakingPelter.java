package spireMapOverhaul.zones.invasion.monsters;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.AnimateFastAttackAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.AngerPower;
import spireMapOverhaul.SpireAnniversary6Mod;

public class CroakingPelter extends AbstractCroaking {
    public static final String ID = SpireAnniversary6Mod.makeID("CroakingPelter");
    private static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID);
    public static final String NAME = monsterStrings.NAME;
    public static final String[] MOVES = monsterStrings.MOVES;
    public static final String[] DIALOG = monsterStrings.DIALOG;
    private static final String IMG = SpireAnniversary6Mod.makeImagePath("monsters/Invasion/CroakingPelter/CroakingPelter.png");
    private static final byte ROCK_TOSS_ATTACK = 2;
    private static final int ROCK_TOSS_DAMAGE = 3;
    private static final int A2_ROCK_TOSS_DAMAGE = 4;
    private static final int ROCK_TOSS_BLOCK = 5;
    private static final int A7_ROCK_TOSS_BLOCK = 8;
    private int rockTossDamage;
    private int rockTossBlock;

    public CroakingPelter() {
        this(0.0f, 0.0f);
    }

    public CroakingPelter(final float x, final float y) {
        super(CroakingPelter.NAME, ID, -5.0F, 0, 195.0f, 225.0f, IMG, x, y);

        if (AbstractDungeon.ascensionLevel >= 2) {
            this.rockTossDamage = A2_ROCK_TOSS_DAMAGE;
        } else {
            this.rockTossDamage = ROCK_TOSS_DAMAGE;
        }
        if (AbstractDungeon.ascensionLevel >= 7) {
            this.rockTossBlock = A7_ROCK_TOSS_BLOCK;
        } else {
            this.rockTossBlock = ROCK_TOSS_BLOCK;
        }
        this.damage.add(new DamageInfo(this, this.rockTossDamage));
    }

    @Override
    protected AbstractPower getBuffPower(int amount) {
        return new AngerPower(this, amount);
    }

    @Override
    protected String getDialog() {
        return DIALOG[0];
    }

    @Override
    protected String getFirstMoveName() {
        return MOVES[0];
    }

    @Override
    protected AbstractGameAction.AttackEffect getFirstMoveAttackEffect() {
        return AbstractGameAction.AttackEffect.SLASH_DIAGONAL;
    }

    @Override
    protected void executeSecondMove() {
        AbstractDungeon.actionManager.addToBottom(new AnimateFastAttackAction(this));
        AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage.get(1), AbstractGameAction.AttackEffect.BLUNT_LIGHT));
        AbstractDungeon.actionManager.addToBottom(new GainBlockAction(this, this.rockTossBlock));
    }

    @Override
    protected void setSecondMoveIntent() {
        this.setMove(MOVES[1], ROCK_TOSS_ATTACK, AbstractMonster.Intent.ATTACK_DEFEND, this.rockTossDamage);
    }
}