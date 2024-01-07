package spireMapOverhaul.zones.heavenlyClouds;

import basemod.helpers.CardModifierManager;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import spireMapOverhaul.abstracts.AbstractZone;
import spireMapOverhaul.util.Wiz;
import spireMapOverhaul.zoneInterfaces.CombatModifyingZone;
import spireMapOverhaul.zoneInterfaces.RewardModifyingZone;

import java.util.ArrayList;

import static spireMapOverhaul.util.Wiz.atb;

public class HeavenlyCloudsZone extends AbstractZone implements CombatModifyingZone, RewardModifyingZone {
    public static final String ID = "HeavenlyClouds";
    private static final float HEALTH_MODIFIER = 0.70f;
    public HeavenlyCloudsZone() {
        super(ID, Icons.MONSTER);
        this.width = 3;
        this.maxHeight = 4;
        this.height = 4;
        this.maxHeight = 5;
    }

    @Override
    public AbstractZone copy() {
        return new HeavenlyCloudsZone();
    }

    @Override
    public Color getColor() {
        return Color.SKY.cpy();
    }

    @Override
    public void atBattleStart() {
        int flightAmount = 3;
        if (AbstractDungeon.ascensionLevel >= 19) {
            flightAmount = 4;
        }
        for (AbstractMonster mo : Wiz.getEnemies()) {
            atb(new ApplyPowerAction(mo, mo, new HeavenlyFlightPower(mo, flightAmount), flightAmount));
            mo.currentHealth = (int)((float)mo.maxHealth * HEALTH_MODIFIER);
            mo.maxHealth = (int)((float)mo.maxHealth * HEALTH_MODIFIER);
            mo.healthBarUpdatedEvent();
        }
    }

    @Override
    public void modifyRewardCards(ArrayList<AbstractCard> cards) {
        for (AbstractCard card : cards) {
            CardModifierManager.addModifier(card, new FlightMod());
        }
    }

}
