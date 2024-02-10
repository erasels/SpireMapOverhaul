package spireMapOverhaul.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.core.Settings;

import java.util.function.Consumer;

public class CallbackAction<T extends AbstractGameAction> extends AbstractGameAction {
    private final T action;
    private final Consumer<T> onComplete;

    public CallbackAction(T action, Consumer<T> onCompletion) {
        this.action = action;
        this.onComplete = onCompletion;
    }

    public static CallbackAction<WaitAction> wait(Consumer<WaitAction> onComplete) {
        return wait(onComplete, Settings.FAST_MODE ? Settings.ACTION_DUR_XFAST : Settings.ACTION_DUR_FASTER);
    }

    public static CallbackAction<WaitAction> wait(Consumer<WaitAction> onComplete, float dur) {
        return new CallbackAction<>(new WaitAction(dur), onComplete);
    }

    @Override
    public void update() {
        if (!this.action.isDone) {
            this.action.update();
        }
        else {
            isDone = true;
            onComplete.accept(this.action);
        }
    }
}
