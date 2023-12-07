package spireMapOverhaul.zoneInterfaces;

public interface CombatModifyingZone {
    /*
     * These methods mirror their relic counterparts and generally happen after them.
     * Unless specified, they refer to the player.
     * For more complex modifications, apply a custom power that explains itself to the player.
     */

    default void atPreBattle() {}

    default void atBattleStartPreDraw() {}

    default void atBattleStart() {}

    default void atTurnStart() {}

    default void atTurnStartPostDraw() {}

    default void atTurnEnd() {}

    default void atRoundEnd() {}

    default void onVictory() {}
}
