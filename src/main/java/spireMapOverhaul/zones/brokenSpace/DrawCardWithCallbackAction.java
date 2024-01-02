//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package spireMapOverhaul.zones.brokenSpace;

import com.badlogic.gdx.Gdx;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.AbstractGameAction.ActionType;
import com.megacrit.cardcrawl.actions.common.EmptyDeckShuffleAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.SoulGroup;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.PlayerTurnEffect;

import java.util.ArrayList;
import java.util.function.Consumer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class DrawCardWithCallbackAction extends AbstractGameAction {
    private boolean shuffleCheck;
    private static final Logger logger = LogManager.getLogger(DrawCardWithCallbackAction.class.getName());
    public static ArrayList<AbstractCard> drawnCards = new ArrayList();
    private boolean clearDrawHistory;
    private AbstractGameAction followUpAction;
    private Consumer<ArrayList<AbstractCard>> callback;

    public DrawCardWithCallbackAction(AbstractCreature source, int amount, boolean endTurnDraw, Consumer<ArrayList<AbstractCard>> callback) {
        this.shuffleCheck = false;// 19
        this.clearDrawHistory = true;// 23
        this.followUpAction = null;// 24
        this.callback = callback;
        if (endTurnDraw) {// 27
            AbstractDungeon.topLevelEffects.add(new PlayerTurnEffect());// 28
        }

        this.setValues(AbstractDungeon.player, source, amount);// 31
        this.actionType = ActionType.DRAW;// 32
        if (Settings.FAST_MODE) {// 33
            this.duration = Settings.ACTION_DUR_XFAST;// 34
        } else {
            this.duration = Settings.ACTION_DUR_FASTER;// 36
        }

    }// 38

    public DrawCardWithCallbackAction(AbstractCreature source, int amount, Consumer<ArrayList<AbstractCard>> callback) {
        this(source, amount, false, callback);// 41
    }// 42

    public DrawCardWithCallbackAction(int amount, boolean clearDrawHistory, Consumer<ArrayList<AbstractCard>> callback) {
        this(amount, callback);// 45
        this.clearDrawHistory = clearDrawHistory;// 46
    }// 47

    public DrawCardWithCallbackAction(int amount, Consumer<ArrayList<AbstractCard>> callback) {
        this((AbstractCreature) null, amount, false, callback);// 55
    }// 56

    public DrawCardWithCallbackAction(int amount, AbstractGameAction action, Consumer<ArrayList<AbstractCard>> callback) {
        this(amount, action, true, callback);// 64
    }// 65

    public DrawCardWithCallbackAction(int amount, AbstractGameAction action, boolean clearDrawHistory, Consumer<ArrayList<AbstractCard>> callback) {
        this(amount, clearDrawHistory, callback);// 68
        this.followUpAction = action;// 69
    }// 70

    public void update() {
        if (this.clearDrawHistory) {// 74
            this.clearDrawHistory = false;// 75
            drawnCards.clear();// 76
        }

        if (AbstractDungeon.player.hasPower("No Draw")) {// 79
            AbstractDungeon.player.getPower("No Draw").flash();// 80
            this.endActionWithFollowUp();// 81
        } else if (this.amount <= 0) {// 85
            this.endActionWithFollowUp();// 86
        } else {
            int deckSize = AbstractDungeon.player.drawPile.size();// 90
            int discardSize = AbstractDungeon.player.discardPile.size();// 91
            if (!SoulGroup.isActive()) {// 94
                if (deckSize + discardSize == 0) {// 99
                    this.endActionWithFollowUp();// 100
                } else if (AbstractDungeon.player.hand.size() == 10) {// 104
                    AbstractDungeon.player.createHandIsFullDialog();// 105
                    this.endActionWithFollowUp();// 106
                } else {
                    if (!this.shuffleCheck) {// 113
                        int tmp;
                        if (this.amount + AbstractDungeon.player.hand.size() > 10) {// 114
                            tmp = 10 - (this.amount + AbstractDungeon.player.hand.size());// 115
                            this.amount += tmp;// 116
                            AbstractDungeon.player.createHandIsFullDialog();// 117
                        }

                        if (this.amount > deckSize) {// 119
                            tmp = this.amount - deckSize;// 120
                            this.addToTop(new DrawCardWithCallbackAction(tmp, this.followUpAction, false, callback));// 121
                            this.addToTop(new EmptyDeckShuffleAction());// 122
                            if (deckSize != 0) {// 123
                                this.addToTop(new DrawCardWithCallbackAction(deckSize, false, callback));// 124
                            }

                            this.amount = 0;// 126
                            this.isDone = true;// 127
                            return;// 128
                        }

                        this.shuffleCheck = true;// 130
                    }

                    this.duration -= Gdx.graphics.getDeltaTime();// 133
                    if (this.amount != 0 && this.duration < 0.0F) {// 136
                        if (Settings.FAST_MODE) {// 137
                            this.duration = Settings.ACTION_DUR_XFAST;// 138
                        } else {
                            this.duration = Settings.ACTION_DUR_FASTER;// 140
                        }

                        --this.amount;// 142
                        if (!AbstractDungeon.player.drawPile.isEmpty()) {// 144
                            drawnCards.add(AbstractDungeon.player.drawPile.getTopCard());// 145
                            AbstractDungeon.player.draw();// 146
                            AbstractDungeon.player.hand.refreshHandLayout();// 147
                        } else {
                            logger.warn("Player attempted to draw from an empty drawpile mid-DrawAction?MASTER DECK: " + AbstractDungeon.player.masterDeck.getCardNames());// 149 151
                            this.endActionWithFollowUp();// 152
                        }

                        if (this.amount == 0) {// 155
                            this.endActionWithFollowUp();// 156
                        }
                    }

                }
            }
        }
    }// 82 87 95 101 107 159

    private void endActionWithFollowUp() {
        callback.accept(drawnCards);
        this.isDone = true;// 162
        if (this.followUpAction != null) {// 163
            this.addToTop(this.followUpAction);// 164
        }

    }// 166
}
