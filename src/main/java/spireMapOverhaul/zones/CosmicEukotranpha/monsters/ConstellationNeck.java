package spireMapOverhaul.zones.CosmicEukotranpha.monsters;
import com.evacipated.cardcrawl.mod.stslib.actions.tempHp.AddTemporaryHPAction;
import com.megacrit.cardcrawl.actions.ClearCardQueueAction;
import com.megacrit.cardcrawl.actions.common.EscapeAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.unique.CanLoseAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.DamageInfo.DamageType;
import com.megacrit.cardcrawl.cards.status.Dazed;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.ModHelper;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.powers.*;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import spireMapOverhaul.zones.CosmicEukotranpha.cards.special.PathToThePillar;
import spireMapOverhaul.zones.CosmicEukotranpha.cards.special.StarCookies;
import spireMapOverhaul.zones.CosmicEukotranpha.powers.frameworks.AddCardToHandAtTurnStartPostDrawPower;
import spireMapOverhaul.zones.CosmicEukotranpha.powers.monstersOnly.ConstellationNeckNoNervesPower;
import spireMapOverhaul.zones.CosmicEukotranpha.powers.monstersOnly.ConstellationNeckSeenItAllBeforePower;
import spireMapOverhaul.zones.CosmicEukotranpha.powers.monstersOnly.ConstellationNeckThankYouDearPower;
import spireMapOverhaul.zones.CosmicEukotranpha.powers.playerOnly.ConstellationNeckCeremonyPower;
import spireMapOverhaul.zones.CosmicEukotranpha.powers.playerOnly.ExhaustNextCardsPlayedPower;

import java.util.ArrayList;
import java.util.Iterator;

import static spireMapOverhaul.zones.CosmicEukotranpha.util.CosmicShortcuts.*;import spireMapOverhaul.SpireAnniversary6Mod;import spireMapOverhaul.zones.CosmicEukotranpha.CosmicZoneMod;
public class ConstellationNeck extends CosmicZoneMonster{public ConstellationNeck(){this(0.0F,0.0F);}public static final String ID=
        SpireAnniversary6Mod.makeID(ConstellationNeck.class.getSimpleName());private static final MonsterStrings monsterStrings=CardCrawlGame.languagePack.getMonsterStrings(ID);public static final String NAME=monsterStrings.NAME;public static final String[]MOVES=monsterStrings.MOVES;public static final String[]DIALOG=monsterStrings.DIALOG;
    public int stellarEyes=0;public int currentConstell=0;public int familySecrets=0;
    public int intentA=2;public int intentB=2;public int intentC=2;public int intentD=0;public int rageCounter=0;
    public ConstellationNeck(float x,float y){super(NAME,ID,80,0.0F,-15.0F,800.0F,990.0F,null,x,y);MOVERS=MOVES;
        this.type=EnemyType.ELITE;hpRange(300,300,330,330);currentHealth=100;stellarEyes=0;currentConstell=0;familySecrets=0;
        this.img=ImageMaster.loadImage(CosmicZoneMod.imagePath("monsters/ConstellationNeck/ConstellationNeckIdle0.png"));
        intentA=2;intentB=2;intentC=2;intentD=0;rageCounter=0;
        //Normal: 0,2,7,17. Elite: 0,3,8,18. Boss: 0,4,9,19

        //Constellation Neck
        //100/300 hp. Unawakened (330)
        //4 No Nerves-{4 B on Exhaust card. Gain 4 Regen when 1 cost or higher atk is played}
        //Thank You Dear-{Can't lose hp while hp=max hp}
        //Seen it all Before-{After a Power is played, The player gains 1 Frail} (A18 only)

        //Not Fascinated: A then B/C
        //A: Apply 1 Ceremony-{Gain (Skls+Stas in hand) Muscle-{Increase attack D by 1 & Increase B from cards by 1 if player} at turn end} (A18: Shuffle 2 Path to the Pillars to deck)
        //B: 12 D. Add 1 Star Cookie to hand now and at next turn start after drawing (12,14,14,16 and 1 to deck)
        //C: Gain 1 Buffer. 24 D (24,27,27,31)
        //Fascinated:
        //D: Spend 8 Regen. 24+2(Size of Exhaust pile) D (24,26,26,32+3())
        //E: Apply -2 Dex. 18 D. Add 1 Star Cookie to hand now and at next turn start after drawing (18,20,20,22 and 1 to deck)
        //F: Gain 2 Buffer. 40 D (40,44,44,49)
        //
        //Heal to full hp: Hysteric Surge. Gives you a reward based on sequence of Atks and non-Atks in hand next turn then leaves
        genMovePlus("Ceremony: Asteroid Pathway",0,Intent.MAGIC);
        genMovePlus("Want Some Cookies?",aM(12,14,14,16),Intent.ATTACK_DEBUFF);
        genMovePlus("Different Perspective",aM(24,27,27,31),Intent.ATTACK_DEFEND);
        genMovePlus("You Forgot This",aM(24,26,26,32),Intent.ATTACK);
        genMovePlus("Want More?",aM(18,20,20,22),Intent.ATTACK_DEBUFF);
        genMovePlus("Youth and Elders",aM(40,44,44,49),Intent.ATTACK_DEFEND);
        genMovePlus("Hysteric Surge",0,Intent.MAGIC);
        //Killed: Awaken into Stellar Eyes
        //100/100 hp. Awakened (Powers that persist: Buffs except No Nerves and Regen)
        //Renewed Sight-{While Fascinated: Player has 5 or more Muscle: Halve hp loss above 20 this receives. On lose hp: Player loses 5 Muscle}
        //Above 50 hp: 50%/25%/25%/0% (8/4/4/0, 4th move increased by 4 every turn after 3 turn above 50 hp but chances are transferred to B if 50<hp<60)
        //A: Power based on number of times used: 5 Curiosity, 12 Artifact, 3 Malleable, 2 Enrage, 2 Angry, 12 Time Warp, 50 Invincible, 2 non-Self D Intangible (2 non-Self D Intangible every time after that) (Increased at A18)
        //B: Lose 10 hp. Apply 10 Constricted. Add 10 Dazeds to discard pile (A18 Dazeds are also added to deck)
        //C: Heal 5 hp. Gain 6 Plated Armor. Next 2 cards played are Exhausted (8 9 3 at A18)
        //D: Lose 30 hp. 30 DX2 (30,33,33,39)
        //
        //Else: 40%/40%/20%, 3rd move is much more likely if below 30 hp
        //E: Heal 30 hp. 30 D (30,33,33,39)
        //F: Heal 12 hp. Gain 12 Temp hp. 24 B (A18: Gain 12 B on Intent selection)
        //G: Heal 100 hp. Lose 5 Str. Apply -1 Momentary Str. Use only 2 times a combat (A18: Apply -6 Momentary Str)
        genMovePlus("Rebirth",0,Intent.UNKNOWN);
        genMovePlus("Revolving World",0,Intent.BUFF);
        genMovePlus("Ritual of the Ten Thousand Arms",0,Intent.STRONG_DEBUFF);
        genMovePlus("Into Storage",0,Intent.BUFF);
        genMovePlus("Rip and Tear+11.5",aM(30,33,33,39),Intent.ATTACK,2);
        genMovePlus("Shining Pulsation",aM(30,33,33,39),Intent.ATTACK);
        genMovePlus("Depth Perception",0,Intent.DEFEND_BUFF);
        genMovePlus("Family Secret",0,Intent.MAGIC);

        //Star Cookies
        //Target anyone. Target gains 3 Regen and loses 3 Muscle. Give adjacent cards Ethereal and Remove Retain from them. Exhaust

        //Path to the Pillar
        //0 cost. Lose 6 B. Exhaust. Turn end in hand: Lose 1 Dex. Exhaust
    }
    public void usePreBattleAction(){testCosmicZonePassive();AbstractDungeon.getCurrRoom().cannotLose=true;poS(new UnawakenedPower(this));poS(new ConstellationNeckNoNervesPower(this,this,4));poS(new ConstellationNeckThankYouDearPower(this,this));if(asc>17){poS(new ConstellationNeckSeenItAllBeforePower(this,this,1));}}
    public void takeTurn(){switch(this.nextMove){
        //A: Apply 1 Ceremony-{Gain (Skls+Stas in hand) Muscle-{Increase attack D by 1 & Increase B from cards by 1 if player} at turn end} (A18: Shuffle 2 Path to the Pillars to deck)
        case 0:pofP(new ConstellationNeckCeremonyPower(AbstractDungeon.player,this,1));if(asc>17){makeInDeck(new PathToThePillar(),2);}break;
        //B: 12 D. Add 1 Star Cookie to hand now and at turn start after drawing (12,14,14,16 and 1 to deck)
        case 1:monsterDmg();makeInHand(new StarCookies());pofP(new AddCardToHandAtTurnStartPostDrawPower(AbstractDungeon.player,this,new StarCookies(),"","",1,1));if(asc>17){makeInDeck(new StarCookies());}intentA--;intentB=2;break;
        //C: Gain 1 Buffer. 24 D (24,27,27,31)
        case 2:poS(new BufferPower(this,1));monsterDmg();intentA=2;intentB--;break;
        //D: Spend 8 Regen. 24+2(Size of Exhaust pile) D (24,26,26,32+3())
        case 3:atb(new ReducePowerAction(this,this,"Regeneration",8));monsterDmg();intentA=2;intentB=2;intentC--;break;
        //E: Apply -2 Dex. 18 D. Add 1 Star Cookie to hand now and at turn start after drawing (18,20,20,22 and 1 to deck)
        case 4:pofP(new DexterityPower(AbstractDungeon.player,-2));monsterDmg();makeInHand(new StarCookies());pofP(new AddCardToHandAtTurnStartPostDrawPower(AbstractDungeon.player,this,new StarCookies(),"","",1,1));if(asc>17){makeInDeck(new StarCookies());}intentA--;intentB=2;intentC=2;break;
        //F: Gain 2 Buffer. 40 D (40,44,44,49)
        case 5:poS(new BufferPower(this,2));monsterDmg();intentA=2;intentB--;intentC=2;break;
        //Heal to full hp: Hysteric Surge. Gives you a reward based on sequence of Atks and non-Atks in hand next turn then leaves
        case 6:
            if(atksInGroup(AbstractDungeon.player.hand.group)>5){
                if(AbstractDungeon.player.hand.size()/2>atksInGroup(AbstractDungeon.player.hand.group)){
                    //Card Atks>5 & Atks>nonAtks
                }else{
                    //Relic Atks>5 & Atks<=nonAtks
                }
            }else{
                if(AbstractDungeon.player.hand.size()/2>atksInGroup(AbstractDungeon.player.hand.group)){
                    //Potion Atks<=5 & Atks>nonAtks
                }else{
                    //Card Atks<=5 & Atks<=nonAtks
                }
            }
            atb(new EscapeAction(this));
            break;
        //Killed: Awaken into Stellar Eyes
        case 7:maxHealth=100;if(Settings.isEndless&&AbstractDungeon.player.hasBlight("ToughEnemies")){
                float mod=AbstractDungeon.player.getBlight("ToughEnemies").effectFloat();this.maxHealth=(int)((float)this.maxHealth*mod);}
            if(ModHelper.isModEnabled("MonsterHunter")){this.currentHealth=(int)((float)this.currentHealth*1.5F);}
            this.halfDead=false;heal(maxHealth);AbstractDungeon.actionManager.addToBottom(new CanLoseAction());stellarEyes=2;intentD=0;rageCounter=0;break;
        //A: Power based on number of times used: 5 Curiosity, 12 Artifact, 3 Malleable, 2 Anger, 2 Angry, 12 Time Warp, 50 Invincible, 1 Intangible (Increased at A18)
        case 8:switch(currentConstell){
                case 0:poS(new CuriosityPower(this,5+rBAI(asc>17,2)));break;
                case 1:poS(new ArtifactPower(this,12+rBAI(asc>17,6)));break;
                case 2:poS(new MalleablePower(this,3+rBAI(asc>17)));break;
                case 3:poS(new AngerPower(this,2+rBAI(asc>17)));break;
                case 4:poS(new AngryPower(this,2+rBAI(asc>17)));break;
                case 5:poS(new TimeWarpPower(this));break;
                case 6:poS(new InvinciblePower(this,50-rBAI(asc>17,10)));break;
                default:poS(new IntangiblePlayerPower(this,1+rBAI(asc>17,1)));}currentConstell++;rageCounter++;break;
        //B: Lose 10 hp. Apply 10 Constricted. Add 10 Dazeds to discard pile (A18 Dazeds are also added to deck)
        case 9:loseHP(this,10);pofP(new ConstrictedPower(AbstractDungeon.player,this,10));makeInDp(new Dazed(),10);if(asc>17){makeInDeck(new Dazed(),10);}rageCounter++;break;
        //C: Heal 5 hp. Gain 6 Plated Armor. Next 2 cards played are Exhausted (8 9 3 at A18)
        case 10:heal(5+rBAI(asc>17,3));poS(new PlatedArmorPower(AbstractDungeon.player,6+rBAI(asc>17,3)));pofP(new ExhaustNextCardsPlayedPower(AbstractDungeon.player,AbstractDungeon.player,2+rBAI(asc>17)));rageCounter++;break;
        //D: Lose 30 hp. 30 DX2 (30,33,33,39)
        case 11:loseHP(this,30);monsterDmg();monsterDmg();rageCounter++;break;
        //E: Heal 30 hp. 30 D (30,33,33,39)
        case 12:heal(30);monsterDmg();rageCounter=0;break;
        //F: Heal 12 hp. Gain 12 Temp hp. 24 B (A18: Gain 12 B on Intent selection)
        case 13:heal(12);atb(new AddTemporaryHPAction(this,this,12));monsterBlck(24);rageCounter=0;break;
        //G: Heal 100 hp. Lose 5 Str. Apply -1 Momentary Str. Use only 2 times a combat (A18: Apply -6 Momentary Str)
        case 14:heal(100);poS(new StrengthPower(AbstractDungeon.player,-5));pofP(new StrengthPower(AbstractDungeon.player,-1-rBAI(asc>17,5)));
            if(!AbstractDungeon.player.hasPower("Artifact")){pofP(new GainStrengthPower(AbstractDungeon.player,1+rBAI(asc>17,5)));}familySecrets++;rageCounter=0;break;
        default:loggeer("ERROR, takeTurn outside switch, Constellation Neck");
    }lastMoveUpdt(nextMove);rollerMove();}
    public void startOfTurnIntentCheck(){if(nextMove==13&&asc>17){monsterBlck(12);}}
    protected void getMove(int num){dontRedecide=false;
        if(this.currentHealth<=0&&stellarEyes<2){//I mean if somehow it gets here
            decMvePlus("Rebirth");
        }else if(stellarEyes<1){//Not Stellar Eyes:
            if(currentHealth==maxHealth){//Full hp: Hysteric Surge
                decMvePlus("Hysteric Surge");
            }else if(!fasc()){//Not Fascinated: A then B/C - 2/2 reducing by 1 when used resetting when not used
                if(moveCount==0){decMvePlus("Ceremony: Asteroid Pathway");
                }else{applicableMoveList=new ArrayList<>();appMoveWeights=new ArrayList<>();
                    applicableMoveList.add("Want Some Cookies?");appMoveWeights.add(intentA);
                    applicableMoveList.add("Different Perspective");appMoveWeights.add(intentB);
                    decMvePlus(getAppMoveUseWeights());
                }
            }else{//Fascinated: D/E/F where E=B and F=C, Same as previous
                applicableMoveList=new ArrayList<>();appMoveWeights=new ArrayList<>();
                applicableMoveList.add("You Forgot This");appMoveWeights.add(intentC);
                applicableMoveList.add("Want More?");appMoveWeights.add(intentA);
                applicableMoveList.add("Youth and Elders");appMoveWeights.add(intentB);
                decMvePlus(getAppMoveUseWeights());
            }
        }else{//Stellar Eyes:
            if(currentHealth>50){//Above 50 hp: 50%/25%/25%/0% (8/4/4/0, 4th move is 4(Turns above 50 hp-2) but chances are transferred to B if 50<hp<60)
                applicableMoveList=new ArrayList<>();appMoveWeights=new ArrayList<>();
                applicableMoveList.add("Revolving World");appMoveWeights.add(8);
                applicableMoveList.add("Ritual of the Ten Thousand Arms");appMoveWeights.add(4);
                applicableMoveList.add("Into Storage");appMoveWeights.add(4);
                if(rageCounter>2){if(currentHealth<60){applicableMoveList.add("Ritual of the Ten Thousand Arms");appMoveWeights.add(4*(rageCounter-2));
                    }else{applicableMoveList.add("Rip and Tear+11.5");appMoveWeights.add(4*(rageCounter-2));}}
                decMvePlus(getAppMoveUseWeights());
            }else{//Else: 40%/40%/20%, 3rd move is more likely if below 30 hp, much more if below 10 hp
                applicableMoveList=new ArrayList<>();appMoveWeights=new ArrayList<>();
                applicableMoveList.add("Shining Pulsation");appMoveWeights.add(4);
                applicableMoveList.add("Depth Perception");appMoveWeights.add(4);
                if(familySecrets>0){if(currentHealth<10){applicableMoveList.add("Family Secret");appMoveWeights.add(22);
                }else if(currentHealth<30){applicableMoveList.add("Family Secret");appMoveWeights.add(12);
                }else{applicableMoveList.add("Family Secret");appMoveWeights.add(2);}}
                decMvePlus(getAppMoveUseWeights());
            }
        }
        moveChangeCheck();}
    @Override public void update(){super.update();updateMveDmg("You Forgot This",aM(24,26,26,32)+(2+rBAI(asc>17))*(AbstractDungeon.player.exhaustPile.size()));
					upTicks++;if(stellarEyes<2){if(upTicks>60){upTicks=0;if(currImg==0){this.img=ImageMaster.loadImage(CosmicZoneMod.imagePath("monsters/ConstellationNeck/ConstellationNeckIdle1.png"));currImg=1;
					}else{this.img=ImageMaster.loadImage(CosmicZoneMod.imagePath("monsters/ConstellationNeck/ConstellationNeckIdle0.png"));currImg=0;}}
					}else{if(upTicks>60){switch(currImg){
						case 0:this.img=ImageMaster.loadImage(CosmicZoneMod.imagePath("monsters/StellarEyes/StellarEyesIdle1.png"));upTicks=50;currImg++;break;
						case 1:this.img=ImageMaster.loadImage(CosmicZoneMod.imagePath("monsters/StellarEyes/StellarEyesIdle2.png"));upTicks=50;currImg++;break;
						case 2:this.img=ImageMaster.loadImage(CosmicZoneMod.imagePath("monsters/StellarEyes/StellarEyesIdle3.png"));upTicks=0;currImg++;break;
						case 3:this.img=ImageMaster.loadImage(CosmicZoneMod.imagePath("monsters/StellarEyes/StellarEyesIdle2.png"));upTicks=50;currImg++;break;
						case 4:this.img=ImageMaster.loadImage(CosmicZoneMod.imagePath("monsters/StellarEyes/StellarEyesIdle1.png"));upTicks=50;currImg++;break;
						case 5:this.img=ImageMaster.loadImage(CosmicZoneMod.imagePath("monsters/StellarEyes/StellarEyesIdle0.png"));upTicks=0;currImg=0;break;
					}}}
				}
    public void changeState(String key){}
    public void damage(DamageInfo info){super.damage(info);if(info.owner!=null&&info.type!=DamageType.THORNS&&info.output>0){}


        if(this.currentHealth<=0&&!this.halfDead){
            if(AbstractDungeon.getCurrRoom().cannotLose){this.halfDead=true;}
            Iterator s=this.powers.iterator();
            AbstractPower p;while(s.hasNext()){p=(AbstractPower)s.next();p.onDeath();}
            s=AbstractDungeon.player.relics.iterator();while(s.hasNext()){AbstractRelic r=(AbstractRelic)s.next();r.onMonsterDeath(this);}
            this.addToTop(new ClearCardQueueAction());
            s=this.powers.iterator();
            while(true){
                do{
                    if(!s.hasNext()){dontRedecide=false;decMvePlus("Rebirth");this.createIntent();this.applyPowers();this.stellarEyes=1;return;}
                    p=(AbstractPower)s.next();
                }while(p.type!=AbstractPower.PowerType.DEBUFF&&!p.ID.equals("CosmicZone:ConstellationNeckNoNervesPower")&&!p.ID.equals("Regeneration")&&!p.ID.equals("Unawakened")&&!p.ID.equals("Shackled"));

                s.remove();
            }
        }
    }

    public void die(){if(!AbstractDungeon.getCurrRoom().cannotLose){super.die();}}
    //Dialogue:

    //Combat start:
    //""
    //Fascination:
    //""
    //Use Hysteric Surge:
    //""
    //Normal Kill:
    //""
    //Fascination Kill:
    //""
    //Hysteric Kill:
    //""
    //Anti Cosmic monster card used:
    //""
    //Player death:
    //""
    //Player death while Fascinated or Hysteric
    //""
}
