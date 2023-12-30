package spireMapOverhaul.zones.CosmicEukotranpha.monsters;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.DamageInfo.DamageType;
import com.megacrit.cardcrawl.cards.status.Dazed;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.powers.*;
import spireMapOverhaul.zones.CosmicEukotranpha.cards.special.IBroughtSolarFlares;

import static spireMapOverhaul.zones.CosmicEukotranpha.util.CosmicShortcuts.*;import spireMapOverhaul.SpireAnniversary6Mod;import spireMapOverhaul.zones.CosmicEukotranpha.CosmicZoneMod;
public class SaventStars extends CosmicZoneMonster{
public static final String ID=SpireAnniversary6Mod.makeID(SaventStars.class.getSimpleName());
private static final MonsterStrings monsterStrings=CardCrawlGame.languagePack.getMonsterStrings(ID);public static final String NAME=monsterStrings.NAME;public static final String[]MOVES=monsterStrings.MOVES;public static final String[]DIALOG=monsterStrings.DIALOG;
public int ISawSolarFlares;public int frames=0;
public SaventStars(){this(0.0F,0.0F);}
public SaventStars(float x,float y){super(NAME,ID,80,0.0F,-15.0F,380.0F,290.0F,null,x,y);
 this.type=EnemyType.NORMAL;hpRange(80,80,90,90);ISawSolarFlares=0;frames=0;MOVERS=MOVES;
 this.img=ImageMaster.loadImage(CosmicZoneMod.imagePath("monsters/SaventStars/SaventStarsIdle0.png"));//Texture needed?
        /*
        this.loadAnimation("images/monsters/theEnding/spear/skeleton.atlas", "images/monsters/theEnding/spear/skeleton.json", 1.0F);
        AnimationState.TrackEntry e = this.state.setAnimation(0, "Idle", true);
        e.setTime(e.getEndTime() * MathUtils.random());
        this.stateData.setMix("Hit", "Idle", 0.1F);
        e.setTimeScale(0.7F);
         */
 //Savent Stars (Easy Normal enemy)
 //80 hp. 5 Curiosity. 30 Invincible
 //Not Fascinated:
 //A: 15 D. Shuffle a "I Brought Solar Flares!" to deck
 //B: Draw 10. Apply 1 Equilibrium. 10 B
 //Fascinated:
 //C: 5 DX2. Shuffle 5 Dazed into deck
 //D: 5 DX5. Shuffle 10 random cards in discard pile to deck
 //Once per combat, If you play a "I Brought Solar Flares!":
 //E: Heal 30 hp. Give the enemy 3 Dexterity and heal them by 3. Shuffle 3 random cards into deck and discard pile
 //
 //"I Brought Solar Flares!"
 //1 cost. Gain 3 Strength. Choose 1 of 3 random cards to add to your deck, It costs 0. Savent Stars will love this, Be careful, or not
 genMove("Star Seeking",aM(15,17,17,20));
 genMove("Cold Spot",0);
 genMove("Dazzling Stars",aM(5,6,6,7));
 genMove("Burning Wave",aM(5,6,6,7));
 genMove("Hysteric Surge",0);
}
public void usePreBattleAction(){poS(new CuriosityPower(this,5));poS(new InvinciblePower(this,30));testCosmicZonePassive();}
public void takeTurn(){switch(this.nextMove){
 //0, Star Gazing: 15 D. Shuffle a "I Brought Solar Flares!" to deck
 //1, Cold Spot: Draw 10. Apply 2 Equilibrium. 10 B (3 Equil)
 //2, Dazzling Stars: 5 DX2. Shuffle 5 Dazed into deck
 //3, Burning Wave: 5 DX5. Shuffle 10 random cards in discard pile to deck
 //4, Hysteric Surge: Heal 30 hp. Give the enemy 3 Dexterity and heal them by 3. Shuffle 3 random cards into deck and discard pile
 case 0:monsterDmg(0);makeInDeck(new IBroughtSolarFlares(),1);break;
 case 1:draw(10);pofP(new EquilibriumPower(AbstractDungeon.player,2+rBAI(asc>16)));monsterBlck(a789(10,15,0));break;
 case 2:monsterDmg(2);monsterDmg(2);makeInDeck(new Dazed(),5);break;
 case 3:monsterDmg(3);monsterDmg(3);monsterDmg(3);monsterDmg(3);monsterDmg(3);pivotDpRandom(10);break;
 case 4:heal(30);pofP(new DexterityPower(AbstractDungeon.player,3));atb(new HealAction(AbstractDungeon.player,this,3));
  makeInDeck(AbstractDungeon.returnTrulyRandomCardInCombat(),1);makeInDeck(AbstractDungeon.returnTrulyRandomCardInCombat(),1);makeInDeck(AbstractDungeon.returnTrulyRandomCardInCombat(),1);
  makeInDp(AbstractDungeon.returnTrulyRandomCardInCombat(),1);makeInDp(AbstractDungeon.returnTrulyRandomCardInCombat(),1);makeInDp(AbstractDungeon.returnTrulyRandomCardInCombat(),1);
  if(asc>16){poS(new StrengthPower(this,5));}ISawSolarFlares=2;hysteria=2;break;
 default:loggeer("ERROR, takeTurn outside switch, SaventStars");
}lastMoveUpdt(nextMove);rollerMove();}
protected void getMove(int num){
 if(ISawSolarFlares==1&&hysteria<2){
  loggeer("SFlares==1 Hyst<2");
  //           //Opc, If you play a "I Brought Solar Flares!":
  //E: Heal 30 hp. Give the enemy 3 Dexterity and heal them by 3. Shuffle 3 random cards into deck and discard pile
  decMve("Hysteric Surge",iteFIn(10));
 }else if(!fasc()){
  loggeer("Not Fasc");
  //Not Fascinated: Use A. Then 50/50 A or B. Don't use a move 2 turns in a row
  //A: 15 D. Shuffle a "I Brought Solar Flares!" to deck
  //B: Draw 10. Apply 1 Equilibrium. 10 B
  if(moveCount==0){loggeer("First Move");decAtk("Star Seeking",iteFIn(2),1);
  }else{if(lastMoveInARow>1){if(lastMove==0){loggeer("Last move in a row>1 & is 0");decMve("Cold Spot",iteFIn(6));}else{loggeer("Last move in a row>1 & is 1");decAtk("Star Seeking",iteFIn(2),1);}
  }else{if(num<50){loggeer("num<50");decAtk("Star Seeking",iteFIn(2),1);}else{loggeer("num>=50");decMve("Cold Spot",iteFIn(6));}}}
 }else{
  loggeer("Fasc");
  //Fascinated: 50/50 C or D. Don't use a move (or same type of move) 2 turns in a row
  //C: 5 DX2. Shuffle 5 Dazed into deck
  //D: 5 DX5. Shuffle 10 random cards in discard pile to deck
  if(lastMoveInARow>1){if(lastMove==0||lastMove==2){decAtk("Burning Wave",iteFIn(2),5);}else{decAtk("Dazzling Stars",iteFIn(2),2);}
  }else{if(num<50){decAtk("Dazzling Stars",iteFIn(2),2);}else{decAtk("Burning Wave",iteFIn(2),5);}}
 }}
public void changeState(String key){
        /*
        AnimationState.TrackEntry e = null;
        switch (key) {
            case "SLOW_ATTACK":
                this.state.setAnimation(0, "Attack_1", false);
                e = this.state.addAnimation(0, "Idle", true, 0.0F);
                e.setTimeScale(0.5F);
                break;
            case "ATTACK":
                this.state.setAnimation(0, "Attack_2", false);
                e = this.state.addAnimation(0, "Idle", true, 0.0F);
                e.setTimeScale(0.7F);
        }*/
}
public void damage(DamageInfo info){super.damage(info);
 if(info.owner!=null&&info.type!=DamageType.THORNS&&info.output>0){
            /*
            this.state.setAnimation(0, "Hit", false);
            AnimationState.TrackEntry e = this.state.addAnimation(0, "Idle", true, 0.0F);
            e.setTimeScale(0.7F);*/
 }}
public void die(){super.die();
 //Hysteric: On play "I Brought Solar Flares!"
}

@Override public void update(){super.update();if(!fasc()){frames++;
 if(frames>60){frames=0;if(currImg==0){this.img=ImageMaster.loadImage(CosmicZoneMod.imagePath("monsters/SaventStars/SaventStarsIdle1.png"));currImg=1;
 }else{this.img=ImageMaster.loadImage(CosmicZoneMod.imagePath("monsters/SaventStars/SaventStarsIdle0.png"));currImg=0;}}
}else{
 this.img=ImageMaster.loadImage(CosmicZoneMod.imagePath("monsters/SaventStars/SaventStarsFasc0.png"));
}
}

public void applyPowers(){if(bandaid>0){bandaid--;decAtk("Star Seeking",iteFIn(2),1);}super.applyPowers();}
public void seeSolarFlares(){if(ISawSolarFlares<1&&hysteria<1){ISawSolarFlares=1;decMve("Hysteric Surge",iteFIn(10));this.createIntent();this.applyPowers();}}


//General:

//Combat start:
// "Let's have a Blast!",
//Fascination:
// "Yeah, yeah, YEAH!",
//Hysteric Surge Condition Fulfillment:
// "They look AMAZING!!!",
//Hysteric Surge Used:
// "HAhh HahH! HahaaHAHA!!",
//(Norm) Death:
// "Sheesh, I don't think anyone here will like you",
//Fasc Death:
// "That was fun! Hahaha!",
//Hyst Death:
// "HAHAHAHA!!! That was Fun! SO FUN!",
// "Hahahaha, My friends will LOVE you!",
//Player (Norm) Death:
// "Hm? Why aren't you moving?",
//Player Fasc Death:
// "HAHAHA, Let's have more Fun!",
//Player Hyst Death:
// "Come On, COME ON!!! HAHAHAHA!"


//Hit many times before Fascination:
//"You're no fun...",
//"Not now...",
//"I guess you want to see cool things...",
//Anti Cosmic monster card used:
//"Stop it, that hurts...",




//Dialogue: "Let's have a Blast!"
//Hit her too many times before being Fascinated: "You're no fun..."
//Become Fascinated: "Yeah, yeah, YEAH!"
//Play card Savent Stars created: "Look at them!"
//
//Killed before Fascinated: -Cosmic
//"Sheesh, I don't think anyone here will like you"
//Killed while Fascinated: Neutral
//"That was fun! Hahaha!"
//Killed after "I Brought Solar Flares" was played (Even if not Fascinated): +Cosmic
//"HAHAHAHA!!! That was Fun! SO FUN!"
//"Hahahaha, My friends will LOVE you!" (Fade away)
}

//09:44:56.730 INFO CosmicZone> Genned Move
//09:44:56.731 INFO CosmicZone> Genned Move
//09:44:56.732 INFO CosmicZone> Genned Move
//09:44:56.733 INFO CosmicZone> Genned Move
//09:44:56.733 INFO CosmicZone> Genned Move
//09:44:57.154 INFO basemod.BaseMod> publish on post power apply
//09:44:57.196 INFO core.CardCrawlGame> Exception occurred in CardCrawlGame render method!
//09:44:57.198 ERROR core.CardCrawlGame> Exception caught
//java.lang.NullPointerException: null
//	at com.megacrit.cardcrawl.monsters.AbstractMonster.applyPowers(AbstractMonster.java:1357) ~[?:?]
//	at com.megacrit.cardcrawl.dungeons.AbstractDungeon.onModifyPower(AbstractDungeon.java:3239) ~[?:?]
//	at com.megacrit.cardcrawl.actions.common.ApplyPowerAction.update(ApplyPowerAction.java:294) ~[?:?]
//	at com.megacrit.cardcrawl.actions.GameActionManager.update(GameActionManager.java:179) ~[?:?]
//	at com.megacrit.cardcrawl.rooms.AbstractRoom.update(AbstractRoom.java:283) ~[?:?]
//	at com.megacrit.cardcrawl.dungeons.AbstractDungeon.update(AbstractDungeon.java:2532) ~[?:?]
//	at com.megacrit.cardcrawl.core.CardCrawlGame.update(CardCrawlGame.java:876) ~[?:?]
//	at com.megacrit.cardcrawl.core.CardCrawlGame.render(CardCrawlGame.java:423) [?:?]
//	at com.badlogic.gdx.backends.lwjgl.LwjglApplication.mainLoop(LwjglApplication.java:225) [?:?]
//	at com.badlogic.gdx.backends.lwjgl.LwjglApplication$1.run(LwjglApplication.java:126) [?:?]
//Controllers: removed manager for application, 0 managers active