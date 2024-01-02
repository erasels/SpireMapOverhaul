package spireMapOverhaul.zones.brokenSpace.relics;

import com.megacrit.cardcrawl.actions.watcher.ChangeStanceAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.relics.Damaru;
import com.megacrit.cardcrawl.stances.DivinityStance;

import static spireMapOverhaul.util.Wiz.adp;

public class BrokenDamaru extends BrokenRelic {
    public static final String ID = "BrokenDamaru";

    public BrokenDamaru() {
        super(ID, RelicTier.SPECIAL, LandingSound.CLINK, Damaru.ID);
    }

    @Override
    public void playLandingSFX() {
        // pick a random sound from the list
        switch ((int) (Math.random() * 5)) {// 470
            case 0:
                CardCrawlGame.sound.play("RELIC_DROP_CLINK");// 471
                break;// 472
            case 1:
                CardCrawlGame.sound.play("RELIC_DROP_FLAT");// 474
                break;// 475
            case 2:
                CardCrawlGame.sound.play("RELIC_DROP_ROCKY");// 477
                break;// 478
            case 3:
                CardCrawlGame.sound.play("RELIC_DROP_HEAVY");// 480
                break;// 481
            case 4:
                CardCrawlGame.sound.play("RELIC_DROP_MAGICAL");// 483
                break;// 484

        }

    }

    @Override
    public boolean canSpawn() {
        return adp().chosenClass == AbstractPlayer.PlayerClass.WATCHER;
    }

    @Override
    public void atBattleStart() {
        addToBot(new ChangeStanceAction(new DivinityStance()));
    }
}