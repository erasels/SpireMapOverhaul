package spireMapOverhaul.zones.Junkyard.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import spireMapOverhaul.util.TexLoader;
import spireMapOverhaul.zones.Junkyard.monsters.Junkbot;

import static spireMapOverhaul.zones.Junkyard.monsters.Junkbot.IMG_INACTIVE;

public class DeactivateAction extends AbstractGameAction {
    private AbstractCreature owner;
    private int chanceToDeactivate;
    private float startingDuration;
    public Junkbot junkOwner;

    public DeactivateAction(AbstractCreature owner, int chance) {
        this.owner = owner;
        this.duration = Settings.ACTION_DUR_FAST;
        this.startingDuration = Settings.ACTION_DUR_FAST;
        this.actionType = ActionType.WAIT;
        chanceToDeactivate = chance;
        junkOwner = (Junkbot)owner;
    }

    public void update() {
        float rand = AbstractDungeon.cardRandomRng.random(0, 100);
        junkOwner.isActivated = (rand >= chanceToDeactivate);
        if (!junkOwner.isActivated) {
            CardCrawlGame.sound.playA("ORB_LIGHTNING_EVOKE", -0.5f);
            junkOwner.setImage(TexLoader.getTexture(IMG_INACTIVE));
        }
        else {
            junkOwner.setDeactivateChance(chanceToDeactivate + 20);
        }

        this.isDone = true;
        }
    }



