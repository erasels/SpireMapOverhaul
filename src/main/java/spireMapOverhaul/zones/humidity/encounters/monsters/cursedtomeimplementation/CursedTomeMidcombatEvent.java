package spireMapOverhaul.zones.humidity.encounters.monsters.cursedtomeimplementation;

import basemod.AutoAdd;
import com.badlogic.gdx.Gdx;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.AbstractEvent;
import com.megacrit.cardcrawl.events.city.CursedTome;
import com.megacrit.cardcrawl.helpers.MathHelper;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.SpeechBubble;
import spireMapOverhaul.util.Wiz;
import spireMapOverhaul.zones.humidity.encounters.BookOfStabbingCursedTome;

import static spireMapOverhaul.SpireAnniversary6Mod.makeID;

@AutoAdd.Ignore
public class CursedTomeMidcombatEvent extends AbstractEvent {
    public static final String MONSTER_ID = makeID("BookOfStabbingHumidity");
    private static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(MONSTER_ID);
    public static final String EVENT_ID = CursedTome.ID;
    private static final EventStrings eventStrings = CardCrawlGame.languagePack.getEventString(EVENT_ID);



    public float panelAlpha=0f;
    public int screen=0;
    public float duration=0.0f;

    public CursedTomeMidcombatEvent(){
        this.screen=0;
        roomEventText.clear();
        roomEventText.updateBodyText(eventStrings.DESCRIPTIONS[0]);
        roomEventText.addDialogOption(monsterStrings.DIALOG[0]);
        roomEventText.addDialogOption(monsterStrings.DIALOG[1]);
        this.hideAlpha=false;
        roomEventText.show();
        //this.color=new Color(1f,1f,1f,1f);
    }

    public void setScreen(int screen){
        if(screen>6)screen=6;
        this.screen=screen;
        roomEventText.clear();
        if(screen!=6) {
            roomEventText.updateBodyText(eventStrings.DESCRIPTIONS[screen]);
            this.hideAlpha=false;
        }else{
            this.hideAlpha=true;
        }
        if(screen==5){
            roomEventText.addDialogOption(monsterStrings.DIALOG[3]);
        }
        roomEventText.show();
    }

    public void update(){
        super.update();
        if(duration>0){
            duration -= Gdx.graphics.getDeltaTime();
            if(duration<0){
                roomEventText.clear();
                this.hideAlpha=true;
            }
        }
        //Panel is ordinarily visible only when room phase is EVENT
        if (!this.hideAlpha) {
            this.panelAlpha = MathHelper.fadeLerpSnap(this.panelAlpha, 0.66F);
        } else {
            this.panelAlpha = MathHelper.fadeLerpSnap(this.panelAlpha, 0.0F);
        }
    }

    protected void buttonEffect(int buttonPressed) {
        int goldBet = Math.min(50, Wiz.adp().gold);
        if(buttonPressed==1){
            roomEventText.clear();
            roomEventText.updateBodyText(monsterStrings.DIALOG[4]);
            AbstractDungeon.getCurrRoom().smoked = true;
            AbstractDungeon.player.hideHealthBar();
            AbstractDungeon.player.isEscaping = true;
            AbstractDungeon.player.flipHorizontal = !AbstractDungeon.player.flipHorizontal;
            AbstractDungeon.overlayMenu.endTurnButton.disable();
            AbstractDungeon.player.escapeTimer = 2.5F;
            duration=2.5f;
        }else{
            if(this.screen==0) {
                setScreen(1);
                //SpireAnniversary6Mod.logger.info("Current screen: " + this.screen);
            }else{
                setScreen(6);
                AbstractDungeon.player.hideHealthBar();
                //AbstractDungeon.player.isEscaping = true;
                //AbstractDungeon.overlayMenu.endTurnButton.disable();
                //AbstractDungeon.player.escapeTimer = 4.0F;

                //Normal escape timers won't work here --
                // the player can smuggle the timer into the next room
                // and eventually crash via BlockModifierPatches$ClearMonsterContainersOnVictory.
                // Use a custom flee sequence instead.
                duration=4.0f;
                BookOfStabbingCursedTome.BookOfStabbingCursedTomeFields.hideMonsterAndShowOnlyBook.set(Wiz.curRoom(),true);
                BookOfStabbingCursedTome.BookOfStabbingCursedTomeFields.escapeRight.set(Wiz.curRoom(),true);
                if(!Wiz.getEnemies().isEmpty()) {
                    AbstractMonster book = Wiz.getEnemies().get(0);
                    //can't use TalkAction here; combat is ending
                    AbstractDungeon.effectList.add(new SpeechBubble(book.hb.cX + book.dialogX, book.hb.cY + book.dialogY, 2.0F, monsterStrings.DIALOG[5], false));
                }
            }
        }
    }

    @Override
    public void dispose() {

    }
}
