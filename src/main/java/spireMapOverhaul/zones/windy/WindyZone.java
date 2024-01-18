package spireMapOverhaul.zones.windy;

import com.badlogic.gdx.graphics.Color;
import com.evacipated.cardcrawl.modthespire.lib.ByRef;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertLocator;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch2;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import spireMapOverhaul.SpireAnniversary6Mod;
import spireMapOverhaul.abstracts.AbstractZone;
import spireMapOverhaul.util.Wiz;
import spireMapOverhaul.zoneInterfaces.CombatModifyingZone;
import spireMapOverhaul.zoneInterfaces.ModifiedEventRateZone;
import spireMapOverhaul.zoneInterfaces.OnTravelZone;
import spireMapOverhaul.zoneInterfaces.RenderableZone;
import spireMapOverhaul.zones.storm.StormUtil;
import spireMapOverhaul.zones.windy.actions.FlyAwayAction;
import spireMapOverhaul.zones.windy.effects.WindyDustEffect;

import static spireMapOverhaul.SpireAnniversary6Mod.*;
import static spireMapOverhaul.zones.storm.StormUtil.conduitTarget;

public class WindyZone extends AbstractZone implements CombatModifyingZone, ModifiedEventRateZone, OnTravelZone, RenderableZone {
    public static final String ID = "Windy";

    public static final String WINDY_KEY = makeID("Windy_Amb");
    public static final String WINDY_MP3 = makePath("audio/Windy/windyAmb.mp3");

    public WindyZone() {
        super(ID, Icons.MONSTER, Icons.EVENT);
        this.width = 2;
        this.maxWidth = 4;
        this.height = 3;
        this.maxHeight = 4;
    }

    @Override
    public AbstractZone copy() {
        return new WindyZone();
    }

    @Override
    public Color getColor() {
        return Color.WHITE.cpy();
    }

    public String getCombatText() {
        return CardCrawlGame.languagePack.getUIString(SpireAnniversary6Mod.makeID(ID)).TEXT[1];
    }

    public static long windID = 0L;
    public void onEnterRoom() {
        if(windID == 0L) {
            windID = CardCrawlGame.sound.playAndLoop(WINDY_KEY, 0.2f);
        }
    }
    public void onExit() {
        CardCrawlGame.sound.stop(WINDY_KEY, windID);
        StormUtil.rainSoundId = 0L;
    }

    public void postDamageCheck(AbstractMonster m){
        if(m.currentHealth <= m.maxHealth/10 && !m.isDeadOrEscaped()){
            Wiz.atb(new FlyAwayAction(m));
        }
    }

    @Override
    public float zoneSpecificEventRate() {
        return 0.70f;
    }

    @Override
    public void update() {
//        AbstractDungeon.effectsQueue.add(new WindyDustEffect());
    }
}
