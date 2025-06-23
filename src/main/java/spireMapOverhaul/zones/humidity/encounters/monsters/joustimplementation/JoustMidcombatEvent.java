package spireMapOverhaul.zones.humidity.encounters.monsters.joustimplementation;

import basemod.AutoAdd;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.EscapeAction;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.AbstractEvent;
import com.megacrit.cardcrawl.events.RoomEventDialog;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.MathHelper;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.SmokeBombEffect;
import spireMapOverhaul.util.Wiz;

import static spireMapOverhaul.SpireAnniversary6Mod.makeID;

@AutoAdd.Ignore
public class JoustMidcombatEvent extends AbstractEvent {
    public static final String ID = makeID("JoustHumidity");
    private static final EventStrings eventStrings = CardCrawlGame.languagePack.getEventString(ID);


    public float panelAlpha=0f;
    public int screen=0;
    private int betChoice=0;

    public JoustMidcombatEvent(){
        this.screen=0;
        roomEventText.clear();
        roomEventText.updateBodyText(eventStrings.DESCRIPTIONS[0]);
        roomEventText.addDialogOption(eventStrings.OPTIONS[0]);
        roomEventText.show();
        //this.color=new Color(1f,1f,1f,1f);
    }

    public void update(){
        super.update();
        //Panel is ordinarily visible only when room phase is EVENT
        if (!this.hideAlpha) {
            this.panelAlpha = MathHelper.fadeLerpSnap(this.panelAlpha, 0.66F);
        } else {
            this.panelAlpha = MathHelper.fadeLerpSnap(this.panelAlpha, 0.0F);
        }
        if(this.screen==3 && !JoustManagerPower.joustMonstersAreValid()){
            buttonEffect(0);
        }
    }

    protected void buttonEffect(int buttonPressed) {
        int goldBet = Math.min(50, Wiz.adp().gold);
        if(this.screen==0) {
            roomEventText.updateBodyText(eventStrings.DESCRIPTIONS[1]);
            roomEventText.updateDialogOption(0, eventStrings.OPTIONS[1] + goldBet + eventStrings.OPTIONS[2]);
            roomEventText.addDialogOption(eventStrings.OPTIONS[3] + goldBet + eventStrings.OPTIONS[4]);
            this.screen = 1;
        }else if(this.screen==1){
            betChoice=buttonPressed;
            roomEventText.updateBodyText(eventStrings.DESCRIPTIONS[betChoice==0?2:3]);
            Wiz.adp().loseGold(goldBet);
            roomEventText.updateDialogOption(0, eventStrings.OPTIONS[5]);
            roomEventText.clearRemainingOptions();
            this.screen = 2;
        }else if(this.screen == 2){
            this.hideAlpha=true;
            roomEventText.clear();
            this.screen = 3;
        }else if(this.screen == 3){
            this.hideAlpha=false;
            String text="";
            boolean unknownResult=false;
            if(Wiz.curRoom().monsters.monsters.get(0).isDeadOrEscaped()){
                text+=eventStrings.DESCRIPTIONS[7];
            }else if(Wiz.curRoom().monsters.monsters.get(1).isDeadOrEscaped()){
                text+=eventStrings.DESCRIPTIONS[6];
            }else{
                text+=eventStrings.DESCRIPTIONS[10];
                unknownResult=true;
            }
            if(!unknownResult) {
                if (Wiz.curRoom().monsters.monsters.get(0).isDeadOrEscaped() == (betChoice == 0)) {
                    text += eventStrings.DESCRIPTIONS[8];
                    //For later: is this affected by money-changing relics? ...and if it is, do we care?
                    Wiz.curRoom().addGoldToRewards(betChoice==0?100:250);
                } else {
                    text += eventStrings.DESCRIPTIONS[9];
                }
            }
            roomEventText.updateBodyText(text);
            roomEventText.addDialogOption(eventStrings.OPTIONS[6]);

            this.screen=4;
        }else if(this.screen==4){
            this.hideAlpha=true;
            roomEventText.clear();
            Wiz.curRoom().event=null;
            Wiz.curRoom().isBattleOver=true;
        }
    }



    @Override
    public void dispose() {

    }
}
