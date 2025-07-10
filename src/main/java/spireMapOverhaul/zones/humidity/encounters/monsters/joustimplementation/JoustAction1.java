package spireMapOverhaul.zones.humidity.encounters.monsters.joustimplementation;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.actions.common.SpawnMonsterAction;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.AbstractEvent;
import com.megacrit.cardcrawl.events.RoomEventDialog;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.city.Centurion;
import com.megacrit.cardcrawl.monsters.city.Healer;
import com.megacrit.cardcrawl.powers.MinionPower;
import spireMapOverhaul.util.Wiz;

import static spireMapOverhaul.SpireAnniversary6Mod.makeID;

public class JoustAction1 extends AbstractGameAction {
    public static final String ID = makeID("JoustHumidity");
    private static final EventStrings eventStrings = CardCrawlGame.languagePack.getEventString(ID);

    RoomEventDialog roomEventText;

    @Override
    public void update() {
        if (AbstractDungeon.getMonsters().monsters.size() >= 2) {
            AbstractMonster rightCenturion = AbstractDungeon.getMonsters().monsters.get(0);
            if (rightCenturion.isDeadOrEscaped()) {
                Wiz.att(new RemoveSpecificPowerAction(Wiz.adp(), Wiz.adp(), JoustManagerPower.POWER_ID));
                isDone = true;
            } else {
                if (this.roomEventText == null) {
                    AbstractMonster healer = AbstractDungeon.getMonsters().monsters.get(1);
                    if (healer instanceof Healer) {
                        Wiz.att(new ApplyPowerAction(healer, healer, new MinionPower(healer)));
                    }
                    AbstractEvent.type = AbstractEvent.EventType.ROOM;
                    rightCenturion.name = eventStrings.DESCRIPTIONS[4];
                    Centurion leftCenturion = new Centurion(-Settings.WIDTH * 0.75F / Settings.xScale - 200f, -15.0F);
                    leftCenturion.flipHorizontal = true;
                    leftCenturion.name = eventStrings.DESCRIPTIONS[5];
                    CenturionAttacksOppositeCenturionPatch.Fields.isSolo.set(leftCenturion, true);
                    AbstractEvent jme = new JoustMidcombatEvent();
                    Wiz.curRoom().event = jme;
                    EventTextRenderEffect etre = new EventTextRenderEffect(jme);
                    AbstractDungeon.topLevelEffectsQueue.add(etre);
                    Wiz.att(new JoustAction2(roomEventText));
                    Wiz.att(new UpdateIntentAction(leftCenturion));
                    Wiz.att(new SpawnMonsterAction(leftCenturion, false));

                    isDone = true;
                }
            }
        } else {
            Wiz.att(new RemoveSpecificPowerAction(Wiz.adp(), Wiz.adp(), JoustManagerPower.POWER_ID));
            isDone = true;
        }
    }
}
