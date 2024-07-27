package spireMapOverhaul.zones.gremlincamp.monsters;

import basemod.ReflectionHacks;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.AnimateSlowAttackAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.SetMoveAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.exordium.GremlinTsundere;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect;
import spireMapOverhaul.util.Wiz;

import java.util.ArrayList;

import static spireMapOverhaul.SpireAnniversary6Mod.makeID;

public class GremlinBodyguard extends GremlinTsundere {
    public static final String ID = makeID("GremlindBodyGuard");
    private static final byte PROTECT = 1, BASH = 2;

    public GremlinBodyguard(float x, float y) {
        super(x, y);
        id = ID;
        this.maxHealth += 6;
        this.heal(6, false);
        this.healthBarUpdatedEvent();
    }

    @Override
    public void takeTurn() {
        switch (nextMove) {
            case PROTECT:
                // Gives a random monster Block
                Wiz.atb(new AbstractGameAction() {
                    @Override
                    public void update() {
                        ArrayList<AbstractMonster> mons = Wiz.getEnemies();
                        mons.removeIf(m -> GremlinTsundere.ID.equals(m.id) || ID.equals(m.id));
                        if(mons.isEmpty()) {
                            mons = Wiz.getEnemies();
                        }
                        AbstractMonster target = Wiz.getRandomItem(mons, AbstractDungeon.aiRng);
                        AbstractDungeon.effectList.add(new FlashAtkImgEffect(target.hb.cX, target.hb.cY, AttackEffect.SHIELD));
                        target.addBlock(ReflectionHacks.getPrivate(GremlinBodyguard.this, GremlinTsundere.class, "blockAmt"));
                        isDone = true;
                    }
                });

                ArrayList<AbstractMonster> mons = Wiz.getEnemies();
                mons.removeIf(m -> GremlinTsundere.ID.equals(m.id) || ID.equals(m.id));
                if (!mons.isEmpty()) {
                    setMove(MOVES[0], PROTECT, Intent.DEFEND);
                } else {
                    setMove(MOVES[1], BASH, Intent.ATTACK_BUFF, damage.get(0).base);
                }
                break;
            case BASH:
                Wiz.atb(new AnimateSlowAttackAction(this));
                Wiz.atb(new DamageAction(AbstractDungeon.player, damage.get(0), AbstractGameAction.AttackEffect.BLUNT_LIGHT));
                Wiz.atb(new ApplyPowerAction(this, this, new StrengthPower(this, 5)));
                Wiz.atb(new SetMoveAction(this, MOVES[1], BASH, Intent.ATTACK_BUFF, damage.get(0).base));
                break;
            default:
                break;
        }

    }
}
