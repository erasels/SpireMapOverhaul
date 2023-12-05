package spireMapOverhaul.zoneInterfaces;

import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;

public interface CombatModifyingZone {
    /*
     * These methods mirror their relic counterparts and generally happen after them.
     * Unless specified, they refer to the player.
     */

    default void atPreBattle() {}

    default void atBattleStartPreDraw() {}

    default void atBattleStart() {}

    default void atTurnStart() {}

    default void atTurnStartPostDraw() {}

    default void atTurnEnd() {}

    default void atRoundEnd() {}

    default void onVictory() {}

    /***
     * Method for any creature being attacked in combat.
     * @param info DamageInfo which contains the damage source which can be null
     * @param damageAmount The amount of damage dealt post relic modifications
     * @param target The creature that is getting attacked
     * @return The damage modified damage, if you don't want to change the damage dealt, just return damageAmount.
     */
    default int onAttacked(DamageInfo info, int damageAmount, AbstractCreature target) {
        return damageAmount;
    }
}
