package spireMapOverhaul.zones.storm;

import basemod.helpers.CardModifierManager;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.blue.Storm;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.rooms.MonsterRoomElite;
import spireMapOverhaul.SpireAnniversary6Mod;
import spireMapOverhaul.abstracts.AbstractZone;
import spireMapOverhaul.zoneInterfaces.CombatModifyingZone;
import spireMapOverhaul.zoneInterfaces.OnTravelZone;
import spireMapOverhaul.zoneInterfaces.RewardModifyingZone;
import spireMapOverhaul.zones.storm.cardmods.DampModifier;
import spireMapOverhaul.zones.storm.cardmods.ElectricModifier;
import spireMapOverhaul.zones.storm.patches.AddLightningPatch;
import spireMapOverhaul.zones.storm.powers.ConduitPower;

import java.util.ArrayList;

import static spireMapOverhaul.SpireAnniversary6Mod.*;
import static spireMapOverhaul.util.Wiz.*;
import static spireMapOverhaul.zones.storm.StormUtil.*;

public class StormZone extends AbstractZone implements CombatModifyingZone, RewardModifyingZone, OnTravelZone {
    public static final String ID = "Storm";

    public static final String THUNDER_KEY = makeID("Storm_Thunder");
    public static final String THUNDER_MP3 = makePath("audio/storm/thunder.mp3");
    public static final String RAIN_KEY = makeID("Storm_Rain");
    public static final String RAIN_MP3 = makePath("audio/storm/rain.mp3");

    public StormZone() {
        super(ID, Icons.MONSTER);
        this.width = 3;
        this.height = 4;
    }

    @Override
    public AbstractZone copy() {
        return new StormZone();
    }

    @Override
    public Color getColor() {
        return Color.DARK_GRAY.cpy();
    }

    public void onEnterRoom() {
        if(StormUtil.rainSoundId == 0L) {
            StormUtil.rainSoundId = CardCrawlGame.sound.playAndLoop(RAIN_KEY);
        }
    }
    public void onExit() {
        CardCrawlGame.sound.stop(RAIN_KEY, StormUtil.rainSoundId);
        StormUtil.rainSoundId = 0L;
    }

    @Override
    public void modifyRewardCards(ArrayList<AbstractCard> cards) {
        if(AbstractDungeon.getCurrRoom() instanceof  MonsterRoomElite || AbstractDungeon.cardRng.randomBoolean()) {
            AbstractCard card = cards.get(AbstractDungeon.cardRng.random(cards.size() - 1));
            CardModifierManager.addModifier(card, new ElectricModifier());
        }
    }

    @Override
    public void atTurnStartPostDraw() {
        if (!AbstractDungeon.getMonsters().areMonstersBasicallyDead()) {
            atb(new AbstractGameAction() {
                @Override
                public void update() {
                    int validCards = countValidCardsInHandToMakeDamp();
                    if(validCards > 0) {
                        AbstractCard card = AbstractDungeon.player.hand.getRandomCard(AbstractDungeon.cardRandomRng);
                        while (!cardValidToMakeDamp(card)) { //Get random cards until you get one you can make damp
                            card = AbstractDungeon.player.hand.getRandomCard(AbstractDungeon.cardRandomRng);
                        }
                        CardModifierManager.addModifier(card, new DampModifier());
                        card.superFlash();
                    }
                    isDone = true;
                }
            });
        }

        ArrayList<AbstractMonster> mons = getEnemies();
        int totalActors = mons.size() + 1;

        if (AbstractDungeon.cardRandomRng.random(1, totalActors) == 1) {
            conduitTarget = AbstractDungeon.player;
            applyToSelf(new ConduitPower(AbstractDungeon.player));
        } else {
            AbstractMonster m = AbstractDungeon.getMonsters().getRandomMonster(null, true, AbstractDungeon.cardRandomRng);
            conduitTarget = m;
            applyToEnemy(m, new ConduitPower(m));
        }

    }
}
