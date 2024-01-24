package spireMapOverhaul.zones.heavenlyClouds;

import basemod.helpers.CardModifierManager;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.animations.TalkAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.city.Byrd;
import com.megacrit.cardcrawl.powers.MinionPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import spireMapOverhaul.SpireAnniversary6Mod;
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
        this.width = 2;
        this.maxWidth = 3;
        this.height = 3;
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
    public boolean canSpawn() {
        // Don't spawn in Act 3
        return this.isAct(1) || this.isAct(2);
    }

    @Override
    public void atBattleStart() {
        UIStrings uistrings = CardCrawlGame.languagePack.getUIString(SpireAnniversary6Mod.makeID("ByrdTalk"));
        String[] TEXT = uistrings.TEXT;
        int flightAmount = 3;
        int numNonMinionEnemies = 0;
        for (AbstractMonster mo : Wiz.getEnemies()) {
            if (!mo.hasPower(MinionPower.POWER_ID)) {
                numNonMinionEnemies++;
            }
        }
        if (numNonMinionEnemies == 1) {
            flightAmount++;
        }
        for (AbstractMonster mo : Wiz.getEnemies()) {
            if (mo.id.equals(Byrd.ID)) {
                atb(new ApplyPowerAction(mo, mo, new StrengthPower(mo, 1), 1));
                atb(new TalkAction(mo, TEXT[AbstractDungeon.monsterRng.random(TEXT.length - 1)]));
            } else if (!mo.hasPower(MinionPower.POWER_ID)) {
                atb(new ApplyPowerAction(mo, mo, new HeavenlyFlightPower(mo, flightAmount), flightAmount));
                mo.currentHealth = Math.max((int)((float)mo.currentHealth * HEALTH_MODIFIER), 1);
                mo.maxHealth = Math.max((int)((float)mo.maxHealth * HEALTH_MODIFIER), 1);
                mo.healthBarUpdatedEvent();
                mo.currentBlock = (int)((float)mo.currentBlock * HEALTH_MODIFIER); // wreck spheric guardian
            }
        }
    }

    @Override
    public void modifyRewardCards(ArrayList<AbstractCard> cards) {
        for (AbstractCard card : cards) {
            CardModifierManager.addModifier(card, new FlightMod());
        }
    }

}
