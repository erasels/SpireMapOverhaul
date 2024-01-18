package spireMapOverhaul.actions;

import com.evacipated.cardcrawl.mod.stslib.actions.common.DamageCallbackAction;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rooms.AbstractRoom;

import java.util.function.Consumer;
import java.util.function.Function;

public class CallbackAction<T, U> extends AbstractGameAction {
    private final Function<T, U> onAction;
    private T param;
    private Consumer<U> onComplete;

    public CallbackAction(Function<T, U> onAction, T param) {
        this.onAction = onAction;
        this.param = param;
    }

    public static <V> CallbackAction<Void, V> voidAction(Function<Void, V> onAction) {
        return new CallbackAction<>(onAction, null);
    }

    public CallbackAction<T, U> setOnComplete(Consumer<U> onComplete) {
        this.onComplete = onComplete;
        return this;
    }

    public CallbackAction<T, U> setParam(T param) {
        this.param = param;
        return this;
    }

    @Override
    public void update() {
        isDone = true;
        U res = onAction.apply(param);
        if (onComplete != null) {
            onComplete.accept(res);
        }
    }
}
