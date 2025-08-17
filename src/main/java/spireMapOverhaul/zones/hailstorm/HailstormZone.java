package spireMapOverhaul.zones.hailstorm;

import basemod.BaseMod;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.MonsterGroup;
import com.megacrit.cardcrawl.monsters.exordium.Looter;
import com.megacrit.cardcrawl.random.Random;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.ui.campfire.AbstractCampfireOption;
import com.megacrit.cardcrawl.ui.campfire.RestOption;
import spireMapOverhaul.BetterMapGenerator;
import spireMapOverhaul.SpireAnniversary6Mod;
import spireMapOverhaul.abstracts.AbstractZone;
import spireMapOverhaul.zoneInterfaces.*;
import spireMapOverhaul.zones.hailstorm.campfire.FlintOption;
import spireMapOverhaul.zones.hailstorm.events.AbandonedCamp;
import spireMapOverhaul.zones.hailstorm.monsters.FrostSlimeL;
import spireMapOverhaul.zones.hailstorm.monsters.FrostSlimeM;
import spireMapOverhaul.zones.hailstorm.vfx.HailEffect;
import spireMapOverhaul.zones.hailstorm.vfx.HailstormEffect;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static spireMapOverhaul.SpireAnniversary6Mod.makeID;
import static spireMapOverhaul.util.Wiz.adp;

public class HailstormZone extends AbstractZone implements CombatModifyingZone, ModifiedEventRateZone, RenderableZone, EncounterModifyingZone, CampfireModifyingZone {
    public static final String ID = "Hailstorm";
    public static final String Frost_Slime_L = SpireAnniversary6Mod.makeID("Frost_Slime_L");
    public static final String Frost_Slime_M = SpireAnniversary6Mod.makeID("Frost_Slime_M");
    public static final String Exordium_Thugs_FrostSlime = SpireAnniversary6Mod.makeID("Exordium_Thugs_FrostSlime");

    public static final int blockFromSnow = 4;
    public static final int turnSwitchBetweenSnowBlockAndHailDamage = 4;

    public void atTurnStart() {
        if (GameActionManager.turn < turnSwitchBetweenSnowBlockAndHailDamage) {
            AbstractDungeon.actionManager.addToBottom(new GainBlockAction(adp(), blockFromSnow));
            for (AbstractMonster q : AbstractDungeon.getCurrRoom().monsters.monsters) {
                AbstractDungeon.actionManager.addToBottom(new GainBlockAction(q, blockFromSnow));
            }
        }
    }
    public void atTurnEnd() {
        if (GameActionManager.turn > turnSwitchBetweenSnowBlockAndHailDamage) {
            AbstractDungeon.actionManager.addToBottom(new DamageAction(adp(), new DamageInfo((AbstractCreature) null, GameActionManager.turn, DamageInfo.DamageType.THORNS), AbstractGameAction.AttackEffect.BLUNT_LIGHT));
            AbstractDungeon.actionManager.addToBottom(new DamageAllEnemiesAction((AbstractCreature) null, DamageInfo.createDamageMatrix(GameActionManager.turn, true), DamageInfo.DamageType.THORNS, AbstractGameAction.AttackEffect.BLUNT_LIGHT));
        }
    }

    public String getCombatText() {
        return CardCrawlGame.languagePack.getUIString(makeID("HailEffect")).TEXT[0];
        //I'd like to update text during combat so the player doesn't have to remind himself every single turn
    }

    @Override
    public String forceEvent() {
        return ModifiedEventRateZone.returnIfUnseen(AbandonedCamp.ID);
    }

    public HailstormZone() {
        super(ID, Icons.MONSTER, Icons.EVENT, Icons.REST);
        this.width = 2;
        this.maxWidth = 4;
        this.height = 3;
        this.maxHeight = 4;
    }


    @Override
    public void update() {
        if (GameActionManager.turn <= turnSwitchBetweenSnowBlockAndHailDamage)
            for (int i = 0; i < 7; i++) {
                AbstractDungeon.effectsQueue.add(new HailEffect());
            }
        else
            for (int i = 0; i < 20; i++) {
                AbstractDungeon.effectsQueue.add(new HailstormEffect());
            }
    }

    @Override
    public AbstractZone copy() {
        return new HailstormZone();
    }

    @Override
    public Color getColor() {
        return Color.CYAN.cpy();
    }

    @Override
    public boolean canSpawn() {
        return this.isAct(1);
    }

    @Override
    protected boolean canIncludeEarlyRows() {
        return false;
    }

    @Override
    public List<ZoneEncounter> getNormalEncounters() {
        return Arrays.asList(
                new ZoneEncounter(Frost_Slime_L, 1, () -> new MonsterGroup(
                        new AbstractMonster[]{
                                new FrostSlimeL(0.0f, 0.0f),
                        })),
                new ZoneEncounter(Exordium_Thugs_FrostSlime, 1, () -> new MonsterGroup(
                        new AbstractMonster[]{
                                new Looter(200.0f, 0.0f),
                                new FrostSlimeM(-100.0f, 0.0f),
                        }))

        );
    }

    public void postAddButtons(ArrayList<AbstractCampfireOption> buttons) {
        for (AbstractCampfireOption c : buttons) {
            if (c instanceof RestOption && c.usable) {
                c.usable = false;
                ((RestOption) c).updateUsability(false);
                break;
            }
        }
    }
}
