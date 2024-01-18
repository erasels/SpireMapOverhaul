package spireMapOverhaul.zones.humility.patches.utils

import com.megacrit.cardcrawl.monsters.AbstractMonster

fun AbstractMonster.isFirstMove(): Boolean {
    return moveHistory.isEmpty()
}

fun AbstractMonster.lastMove(move: Byte): Boolean {
    return moveHistory.isNotEmpty() && moveHistory.last() == move
}

fun AbstractMonster.lastMoveBefore(move: Byte): Boolean {
    return moveHistory.size >= 2 && moveHistory[moveHistory.size - 2] == move
}

fun AbstractMonster.lastTwoMoves(move: Byte): Boolean {
    return moveHistory.size >= 2 && moveHistory.last() == move && moveHistory[moveHistory.size - 2] == move
}
