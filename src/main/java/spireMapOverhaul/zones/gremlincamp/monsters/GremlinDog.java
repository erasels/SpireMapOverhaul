package spireMapOverhaul.zones.gremlincamp.monsters;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.esotericsoftware.spine.AnimationState;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.BiteEffect;
import spireMapOverhaul.abstracts.AbstractSMOMonster;
import spireMapOverhaul.util.Wiz;
import spireMapOverhaul.zones.gremlincamp.GremlinCamp;
import spireMapOverhaul.zones.gremlincamp.PlayerPoisonPower;

import static spireMapOverhaul.SpireAnniversary6Mod.makeID;
import static spireMapOverhaul.SpireAnniversary6Mod.makeMonsterPath;

public class GremlinDog extends AbstractSMOMonster {
    public static final String ID = makeID(GremlinDog.class.getSimpleName());
    private static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID);
    public static final String NAME = monsterStrings.NAME;
    public static final String[] MOVES = monsterStrings.MOVES;

    private static final Byte POISON_BITE = 0, BITE = 1;

    private int poisonAmt;

    public GremlinDog() {
        this(0, 0);
    }

    public GremlinDog(float x, float y) {
        super(NAME, ID, 30, 0, -20, 200f, 150f, null, x, y);
        type = EnemyType.NORMAL;
        setHp(calcAscensionTankiness(21), calcAscensionTankiness(27));
        addMove(POISON_BITE, Intent.ATTACK_DEBUFF, calcAscensionDamage(4));
        addMove(BITE, Intent.ATTACK_DEFEND, calcAscensionDamage(8));

        poisonAmt = calcAscensionSpecial(4);

        // Has idle, hit and attack animations
        loadAnimation(makeMonsterPath(GremlinCamp.ID+"/gremgreen/gremgreen.atlas"), makeMonsterPath(GremlinCamp.ID+"/gremgreen/gremgreen.json"), 0.75F);
        AnimationState.TrackEntry e = this.state.setAnimation(0, "idle", true);
        e.setTime(e.getEndTime() * MathUtils.random());
    }

    @Override
    public void takeTurn() {
        addToBot(new ChangeStateAction(this, "ATTACK"));
        addToBot(new WaitAction(0.25F));
        DamageInfo info = new DamageInfo(this, moves.get(nextMove).baseDamage, DamageInfo.DamageType.NORMAL);
        info.applyPowers(this, AbstractDungeon.player);
        switch (nextMove) {
            case 0: //POISON BITE
                Hitbox phb = Wiz.p().hb;
                BiteEffect be = new BiteEffect(phb.cX, phb.cY - 40.0F * Settings.scale, Color.PURPLE.cpy());
                addToBot(new VFXAction(be, 0.3f));
                addToBot(new DamageAction(AbstractDungeon.player, info, AbstractGameAction.AttackEffect.NONE));
                addToBot(new ApplyPowerAction(AbstractDungeon.player, this, new PlayerPoisonPower(AbstractDungeon.player, poisonAmt), poisonAmt, AbstractGameAction.AttackEffect.POISON));
                break;
            case 1: // BITE
                addToBot(new VFXAction(new BiteEffect(Wiz.p().hb.cX, Wiz.p().hb.cY - 40.0F * Settings.scale), 0.3f));
                addToBot(new DamageAction(AbstractDungeon.player, info, AbstractGameAction.AttackEffect.NONE));
                addToBot(new GainBlockAction(this, this, calcAscensionSpecial(6)));
        }

        addToBot(new RollMoveAction(this));
    }

    @Override
    protected void getMove(int i) {
        // First move is random, and it toggles between them each turn after
        if(firstMove) {
            firstMove = false;

            //Set first move to the opposite of other dog in case there's more than one
            for(AbstractMonster m : Wiz.getEnemies()) {
                if(m instanceof GremlinDog) {
                    if(m == this) break;
                    if(m.moveName.equals(MOVES[BITE])) setMoveShortcut(POISON_BITE, MOVES[POISON_BITE]);
                    else setMoveShortcut(BITE, MOVES[BITE]);
                    return;
                }
            }

            if(i < 50) {
                setMoveShortcut(POISON_BITE, MOVES[POISON_BITE]);
            } else {
                setMoveShortcut(BITE, MOVES[BITE]);
            }
            return;
        }

        if(lastMove(BITE)) {
            setMoveShortcut(POISON_BITE, MOVES[POISON_BITE]);
        } else {
            setMoveShortcut(BITE, MOVES[BITE]);
        }
    }

    public void changeState(String stateName) {
        switch (stateName) {
            case "ATTACK":
                this.state.setAnimation(0, "attack", false);
                this.state.addAnimation(0, "idle", true, 0.0F);
                break;
        }
    }

    public void damage(DamageInfo info) {
        super.damage(info);
        if (info.owner != null && info.type != DamageInfo.DamageType.THORNS && info.output > 0) {
            this.state.setAnimation(0, "hit", false);
            this.state.addAnimation(0, "idle", true, 0.0F);
        }
    }
}
