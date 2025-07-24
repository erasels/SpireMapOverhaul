package spireMapOverhaul.zones.humidity.cards.powerelic.implementation;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import spireMapOverhaul.util.Wiz;

public class RemoveRelicGameEffect extends AbstractGameEffect {

    //Used to remove relics safely (i.e. dodge ConcurrentModificationException) when a PowerelicCard is removed from the master deck.

    AbstractRelic capturedRelic;

    public RemoveRelicGameEffect(AbstractRelic capturedRelic) {
        super();
        this.capturedRelic=capturedRelic;
    }

    @Override
    public void update() {
        boolean success = Wiz.adp().loseRelic(capturedRelic.relicId);
        if (!success) {
            //if we're here, then loseRelic failed because the relic wasn't in the player relic list
            // (inactive) and we need to onUnequip it manually.
            capturedRelic.onUnequip();
        }
        this.isDone = true;
    }

    @Override
    public void render(SpriteBatch spriteBatch) {
    }

    @Override
    public void dispose() {
    }
}
