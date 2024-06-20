package spireMapOverhaul.zones.frostlands.relics;

import com.evacipated.cardcrawl.mod.stslib.relics.OnPlayerDeathRelic;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.vfx.combat.SmokeBombEffect;
import spireMapOverhaul.abstracts.AbstractSMORelic;
import spireMapOverhaul.util.Wiz;
import spireMapOverhaul.zones.frostlands.FrostlandsZone;
import spireMapOverhaul.zones.frostlands.events.SnowmanMafiaEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static spireMapOverhaul.SpireAnniversary6Mod.makeID;

public class Contraption extends AbstractSMORelic implements OnPlayerDeathRelic {
    public static final String ID = makeID(Contraption.class.getSimpleName());
    public int escaping;
    public Contraption() {
        super(ID, FrostlandsZone.ID, RelicTier.SPECIAL, AbstractRelic.LandingSound.CLINK);
        escaping = 0;
    }

    @Override
    public void setCounter(int setCounter) {
        if (setCounter == -2) {
            usedUp();
            counter = -2;
        }

    }

    @Override
    public void atBattleStart() {
        if(escaping == 1)
            escaping = 2;
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

    @Override
    public AbstractRelic makeCopy() {
        return new Contraption();
    }

    @Override
    public boolean onPlayerDeath(AbstractPlayer abstractPlayer, DamageInfo damageInfo) {
        if (AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT) {
            if(usedUp && escaping != 1)
                return true;
            abstractPlayer.currentHealth = 1;
            abstractPlayer.healthBarUpdatedEvent();
            AbstractDungeon.player.hbAlpha = 1f;
            if(canFlee() && escaping != 1)
                flee();
            usedUp();
            return false;
        }
        return true;
    }

    public void flee() {
        AbstractCreature target = AbstractDungeon.player;
        if (AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT) {
            if(escaping == 0)
                escaping = 1;

            AbstractDungeon.actionManager.currentAction.isDone = true;
            AbstractDungeon.getCurrRoom().smoked = true;
            AbstractDungeon.actionManager.actions.clear();
            AbstractDungeon.actionManager.monsterQueue.clear();
            float escapeDuration = 2.5f;
            Wiz.atb(new WaitAction(.1f));
            Wiz.atb(new VFXAction(new SmokeBombEffect(target.hb.cX, target.hb.cY)));
            Wiz.atb(new AbstractGameAction() {
                {
                    this.duration = escapeDuration;
                }
                @Override
                public void update() {
                    this.tickDuration();
                }
            });
            AbstractDungeon.player.hideHealthBar();
            AbstractDungeon.player.isEscaping = true;
            AbstractDungeon.player.flipHorizontal = !AbstractDungeon.player.flipHorizontal;
            AbstractDungeon.overlayMenu.endTurnButton.disable();
            AbstractDungeon.player.escapeTimer = escapeDuration;
            incrementFledStat(1);
        }
        if(AbstractDungeon.getCurrRoom().event instanceof SnowmanMafiaEvent)
            ((SnowmanMafiaEvent) AbstractDungeon.getCurrRoom().event).usedContraption();
        counter = -2;
    }

    public boolean canFlee() {
        boolean pass = false;
        if (AbstractDungeon.getCurrRoom().phase != AbstractRoom.RoomPhase.COMBAT)
            return false;
        for (AbstractMonster m: AbstractDungeon.getCurrRoom().monsters.monsters) {
            if(m.type == AbstractMonster.EnemyType.BOSS)
                return false;
            if(!m.isDeadOrEscaped() && !m.isDying)
                pass = true;
        }
        return pass;
    }

    private static final Map<String, Integer> stats = new HashMap<>();
    private static final String FLED_STAT = "fled";

    public String getStatsDescription() {
        return DESCRIPTIONS[1].replace("{0}", stats.get(FLED_STAT) + "");
    }

    public String getExtendedStatsDescription(int totalCombats, int totalTurns) {
        return getStatsDescription();
    }

    public void resetStats() {
        stats.put(FLED_STAT, 0);
    }

    public JsonElement onSaveStats() {
        Gson gson = new Gson();
        List<Integer> statsToSave = new ArrayList<>();
        statsToSave.add(stats.get(FLED_STAT));
        return gson.toJsonTree(statsToSave);
    }

    public void onLoadStats(JsonElement jsonElement) {
        if (jsonElement != null) {
            JsonArray jsonArray = jsonElement.getAsJsonArray();
            stats.put(FLED_STAT, jsonArray.get(0).getAsInt());
        } else {
            resetStats();
        }
    }

    public static void incrementFledStat(int amount) {
        stats.put(FLED_STAT, stats.getOrDefault(FLED_STAT, 0) + amount);
    }
}
