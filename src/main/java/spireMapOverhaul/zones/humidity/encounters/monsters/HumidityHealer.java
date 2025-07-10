package spireMapOverhaul.zones.humidity.encounters.monsters;

import basemod.ReflectionHacks;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.AnimateSlowAttackAction;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.city.Healer;
import com.megacrit.cardcrawl.powers.FrailPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import spireMapOverhaul.util.Wiz;
import spireMapOverhaul.zones.humidity.encounters.monsters.joustimplementation.JoustManagerPower;

public class HumidityHealer extends Healer {
    public HumidityHealer(float x, float y) {
        super(x, y);
    }

    public void usePreBattleAction() {
        Wiz.att(new ApplyPowerAction(Wiz.adp(), Wiz.adp(), new JoustManagerPower(Wiz.adp())));
    }


    public void takeTurn() {
        int healAmt = ReflectionHacks.getPrivate(this, Healer.class, "healAmt");
        int strAmt = ReflectionHacks.getPrivate(this, Healer.class, "strAmt");
        AbstractCreature offenseTarget = Wiz.adp();
        if (JoustManagerPower.joustMonstersAreValid()) offenseTarget = HumidityCenturion.findEnemyCenturion(this);
        switch (this.nextMove) {// 77
            case 1:
                ReflectionHacks.privateMethod(Healer.class, "playSfx").invoke(this);
                AbstractDungeon.actionManager.addToBottom(new AnimateSlowAttackAction(this));
                AbstractDungeon.actionManager.addToBottom(new DamageAction(offenseTarget, this.damage.get(0), AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(offenseTarget, this, new FrailPower(offenseTarget, 2, true), 2));
                break;// 89
            case 2:
                ReflectionHacks.privateMethod(Healer.class, "playSfx").invoke(this);
                AbstractDungeon.actionManager.addToBottom(new ChangeStateAction(this, "STAFF_RAISE"));
                AbstractDungeon.actionManager.addToBottom(new WaitAction(0.25F));

                for (AbstractMonster m : AbstractDungeon.getMonsters().monsters) {
                    if (!m.isDying && !m.isEscaping && allyCheck(m)) {
                        AbstractDungeon.actionManager.addToBottom(new HealAction(m, this, healAmt));
                    }
                }
                break;
            case 3:
                ReflectionHacks.privateMethod(Healer.class, "playSfx").invoke(this);
                AbstractDungeon.actionManager.addToBottom(new ChangeStateAction(this, "STAFF_RAISE"));
                AbstractDungeon.actionManager.addToBottom(new WaitAction(0.25F));

                for (AbstractMonster m : AbstractDungeon.getMonsters().monsters) {
                    if (!m.isDying && !m.isEscaping && allyCheck(m)) {
                        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(m, this, new StrengthPower(m, strAmt), strAmt));
                    }
                }
        }

        AbstractDungeon.actionManager.addToBottom(new RollMoveAction(this));
    }

    public static boolean allyCheck(AbstractMonster m) {
        //if a joust is not active, all monsters are valid
        if (!JoustManagerPower.joustMonstersAreValid()) return true;
        //if a joust is active, ignore left centurion
        return Wiz.getEnemies().get(0) != m;
    }
}
