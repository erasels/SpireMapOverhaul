package spireMapOverhaul.util;

import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class MonsterUtil {
    public static boolean lastMoveX(AbstractMonster m, byte move, int movesAgo) {
        return m.moveHistory.size() >= movesAgo && m.moveHistory.get(m.moveHistory.size() - movesAgo) == move;
    }
}
