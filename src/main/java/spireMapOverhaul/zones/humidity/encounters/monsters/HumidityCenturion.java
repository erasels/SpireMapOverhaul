package spireMapOverhaul.zones.humidity.encounters.monsters;

import basemod.ReflectionHacks;
import com.evacipated.cardcrawl.modthespire.lib.SpireField;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ChangeStateAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.RollMoveAction;
import com.megacrit.cardcrawl.actions.unique.GainBlockRandomMonsterAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.city.Centurion;
import spireMapOverhaul.util.Wiz;
import spireMapOverhaul.zones.humidity.encounters.monsters.joustimplementation.JoustManagerPower;

public class HumidityCenturion extends Centurion {
    @SpirePatch(clz = Centurion.class, method = SpirePatch.CLASS)
    public static class Fields {
        public static SpireField<Boolean> isSolo = new SpireField<>(() -> false);
    }

    public HumidityCenturion(float x, float y) {
        super(x, y);
    }

    public static AbstractMonster findEnemyCenturion(AbstractMonster __instance) {
        if (__instance == Wiz.getEnemies().get(0))
            return Wiz.getEnemies().get(1);
        else
            return Wiz.getEnemies().get(0);
    }

    public void takeTurn() {
        int furyHits = ReflectionHacks.getPrivate(this, Centurion.class, "furyHits");
        int blockAmount = ReflectionHacks.getPrivate(this, Centurion.class, "blockAmount");
        AbstractCreature offenseTarget = Wiz.adp();
        if (JoustManagerPower.joustMonstersAreValid()) offenseTarget = findEnemyCenturion(this);
        switch (this.nextMove) {
            case 1:
                ReflectionHacks.privateMethod(Centurion.class, "playSfx").invoke(this);
                AbstractDungeon.actionManager.addToBottom(new ChangeStateAction(this, "MACE_HIT"));
                AbstractDungeon.actionManager.addToBottom(new WaitAction(0.3F));

                AbstractDungeon.actionManager.addToBottom(new DamageAction(offenseTarget, this.damage.get(0), AbstractGameAction.AttackEffect.BLUNT_LIGHT));
                break;
            case 2:
                AbstractDungeon.actionManager.addToBottom(new WaitAction(0.25F));// 81
                AbstractDungeon.actionManager.addToBottom(new GainBlockRandomMonsterAction(this, blockAmount));
                break;
            case 3:
                for (int i = 0; i < furyHits; ++i) {
                    ReflectionHacks.privateMethod(Centurion.class, "playSfx").invoke(this);
                    AbstractDungeon.actionManager.addToBottom(new ChangeStateAction(this, "MACE_HIT"));
                    AbstractDungeon.actionManager.addToBottom(new WaitAction(0.3F));
                    AbstractDungeon.actionManager.addToBottom(new DamageAction(offenseTarget, this.damage.get(1), AbstractGameAction.AttackEffect.BLUNT_HEAVY));
                }
        }

        AbstractDungeon.actionManager.addToBottom(new RollMoveAction(this));
    }

    protected void getMove(int num) {
        int furyHits = ReflectionHacks.getPrivate(this, Centurion.class, "furyHits");
        if (num >= 65 && !this.lastTwoMoves((byte) 2) && !this.lastTwoMoves((byte) 3)) {
            int aliveCount = 0;

            for (AbstractMonster m : AbstractDungeon.getMonsters().monsters) {
                if (!m.isDying && !m.isEscaping) {
                    ++aliveCount;
                }
            }

            if (Fields.isSolo.get(this))
                aliveCount = 1;

            if (aliveCount > 1) {
                this.setMove((byte) 2, Intent.DEFEND);
            } else {
                this.setMove((byte) 3, Intent.ATTACK, this.damage.get(1).base, furyHits, true);
            }
        } else if (!this.lastTwoMoves((byte) 1)) {
            this.setMove((byte) 1, Intent.ATTACK, this.damage.get(0).base);
        } else {
            int aliveCount = 0;

            for (AbstractMonster m : AbstractDungeon.getMonsters().monsters) {
                if (!m.isDying && !m.isEscaping) {
                    ++aliveCount;
                }
            }

            if (Fields.isSolo.get(this))
                aliveCount = 1;

            if (aliveCount > 1) {
                this.setMove((byte) 2, Intent.DEFEND);
            } else {
                this.setMove((byte) 3, Intent.ATTACK, this.damage.get(1).base, furyHits, true);
            }
        }
    }
}
