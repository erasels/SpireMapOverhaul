package spireMapOverhaul.zones.CosmicEukotranpha.monsters;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.TalkAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.RollMoveAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.RegenPower;
import spireMapOverhaul.zones.CosmicEukotranpha.cardEffects.CosmicZoneDamageAction;
import spireMapOverhaul.zones.CosmicEukotranpha.powers.WillGainFascinationPower;

import java.util.ArrayList;
import java.util.Random;

import static spireMapOverhaul.zones.CosmicEukotranpha.util.CosmicShortcuts.*;import spireMapOverhaul.SpireAnniversary6Mod;import spireMapOverhaul.zones.CosmicEukotranpha.CosmicZoneMod;
public abstract class CosmicZoneMonster extends AbstractMonster{
    public static AbstractPlayer p=AbstractDungeon.player;
    public int hysteria=0;public int moveCount=0;public int lastMove=-1;public int lastMoveInARow=0;public boolean dontRedecide=false;

    public int bandaid=1;
    public String[]MOVERS;
    public int upTicks=0;public int currImg=0;

    public CosmicZoneMonster(String name,String id,int maxHealth,float hb_x,float hb_y,float hb_w,float hb_h,String imgUrl,float offsetX,float offsetY){super(name,id,maxHealth,hb_x,hb_y,hb_w,hb_h,imgUrl,offsetX,offsetY);}
    public CosmicZoneMonster(String name,String id,int maxHealth,float hb_x,float hb_y,float hb_w,float hb_h,String imgUrl){this(name,id,maxHealth,hb_x,hb_y,hb_w,hb_h,imgUrl,0.0F,0.0F);}
    public void startOfTurnIntentCheck(){}
    public void onSeePlayCard(AbstractCard card){}
    public void testCosmicZonePassive(){/*poS(new WillGainFascinationPower(this,this,6));poS(new RegenPower(this,5));pofP(new RegenPower(AbstractDungeon.player,5));*/}
    public boolean fasc(){return this.hasPower("CosmicZone:FascinationPower");}
    protected void poS(AbstractPower po){if(po.amount!=0){atb(new ApplyPowerAction(this,this,po,po.amount,true));}}protected void pofP(AbstractPower po){loggeer("pofP");if(po.amount!=0){atb(new ApplyPowerAction(AbstractDungeon.player,this,po,po.amount,true));}else{loggeer("po.amount==0");}}
    protected void poSMomentaryDebuff(AbstractPower po,AbstractPower po0){if(po.amount!=0){atb(new ApplyPowerAction(this,this,po,po.amount,true));}
        if(!this.hasPower("Artifact")&&po0.amount!=0){atb(new ApplyPowerAction(this,this,po0,po0.amount,true));}}
    public void monsterDmg(){monsterDmg(nextMove,AbstractGameAction.AttackEffect.NONE,false);}public void monsterDmg(int atkNumber){monsterDmg(atkNumber,AbstractGameAction.AttackEffect.NONE,false);}public void monsterDmg(int atkNumber,AbstractGameAction.AttackEffect attackEffect){monsterDmg(atkNumber,attackEffect,false);}public void monsterDmg(int atkNumber,AbstractGameAction.AttackEffect attackEffect,boolean mute){loggeer("Cosmic Zone Mod Damage start");atb(new CosmicZoneDamageAction(AbstractDungeon.player,damage.get(atkNumber),attackEffect,true));}
    public void monsterStraightDmg(int dNum){monsterStraightDmg(dNum,AbstractGameAction.AttackEffect.NONE,false);}public void monsterStraightDmg(int dNum, AbstractGameAction.AttackEffect attackEffect, boolean mute){atb(new DamageAction(AbstractDungeon.player,new DamageInfo(this,dNum),attackEffect,true,mute));}
    public void monsterBlck(int blck){AbstractDungeon.actionManager.addToBottom(new GainBlockAction(this,this,blck));}public void monsterBlck(int blck,AbstractCreature target){AbstractDungeon.actionManager.addToBottom(new GainBlockAction(target,this,blck));}
    public int aM(int z,int l,int m,int h){if(this.type==EnemyType.NORMAL){return aNM(z,l,m,h);}else if(this.type==EnemyType.ELITE){return aEM(z,l,m,h);}else if(this.type==EnemyType.BOSS){return aBM(z,l,m,h);}loggeer("Cosmic Zone Mod ERROR ERROR ERROR, Unknown enemy type ERROR");return z;}
    public int aNM(int a0,int a2,int a7,int a17){if(asc>16){return a17;}else if(asc>6){return a7;}else if(asc>1){return a2;}return a0;};public int aEM(int a0,int a3,int a8,int a18){if(asc>17){return a18;}else if(asc>7){return a8;}else if(asc>2){return a3;}return a0;};public int aBM(int a0,int a4,int a9,int a19){if(asc>18){return a19;}else if(asc>8){return a9;}else if(asc>3){return a4;}return a0;};//Ascension normal/elite/boss monster
    public int a789(int low,int high,int key){if(key==0){if(asc>6){return high;}else{return low;}}else if(key==1){if(asc>7){return high;}else{return low;}}else if(key==2){if(asc>8){return high;}else{return low;}}return low;}//keys 0=N,1=E,2=B,3=?
    public void hpRange(int low,int high,int low0,int high0,int key){if(key==0){if(asc>5){this.setHp(low0,high0);}else{this.setHp(low,high);}}else if(key==1){if(asc>6){this.setHp(low0,high0);}else{this.setHp(low,high);}}else if(key==2){if(asc>7){this.setHp(low0,high0);}else{this.setHp(low,high);}}else{this.setHp(low,high);}}
    public void hpRange(int low,int high,int low0,int high0){if(type==EnemyType.NORMAL){if(asc>5){this.setHp(low0,high0);}else{this.setHp(low,high);}}else if(type==EnemyType.ELITE){if(asc>6){this.setHp(low0,high0);}else{this.setHp(low,high);}}else if(type==EnemyType.BOSS){if(asc>7){this.setHp(low0,high0);}else{this.setHp(low,high);}}else{this.setHp(low,high);}}
    public Intent iteFIn(int tgraph){switch(tgraph){
        case 0:return Intent.ATTACK;case 1:return Intent.ATTACK_BUFF;case 2:return Intent.ATTACK_DEBUFF;case 3:return Intent.ATTACK_DEFEND;
        case 4:return Intent.DEFEND;case 5:return Intent.DEFEND_BUFF;case 6:return Intent.DEFEND_DEBUFF;
        case 7:return Intent.BUFF;case 8:return Intent.DEBUFF;case 9:return Intent.STRONG_DEBUFF;case 10:return Intent.MAGIC;
        case 11:return Intent.ESCAPE;case 12:return Intent.SLEEP;case 13:return Intent.STUN;case 14:return Intent.UNKNOWN;
        case 15:return Intent.NONE;default:return Intent.DEBUG;}}
    public ArrayList<Intent>intentList=new ArrayList<>();public ArrayList<Integer>hitList=new ArrayList<>();
    public ArrayList<String>moveList=new ArrayList<>();public int getFMoveList(String move){if(moveList.contains(move)){return moveList.indexOf(move);}loggeer("Cosmic Zone Mod ERROR ERROR ERROR, Move not in movelist ERROR");return -1;}
    public ArrayList<String>dialogueList=new ArrayList<>();public int getFDialogueList(String dialogue){if(dialogueList.contains(dialogue)){return dialogueList.indexOf(dialogue);}loggeer("Cosmic Zone Mod ERROR ERROR ERROR, Dialogue not in dialoguelist ERROR");return -1;}
    public ArrayList<String>applicableMoveList=new ArrayList<>();public Random ranGen=new Random();public ArrayList<Integer>appMoveWeights=new ArrayList<>();

    public void speak(String dialogue,Float duration){AbstractDungeon.actionManager.addToTop(new TalkAction(this,DIALOG[getFDialogueList(dialogue)],duration,duration));}
    public void rollerMove(){loggeer("rollerMove");AbstractDungeon.actionManager.addToBottom(new RollMoveAction(this));}
    public void updateMveDmg(String move,int newBase){this.damage.set(getFMoveList(move),new DamageInfo(this.damage.get(getFMoveList(move)).owner,newBase));}
    public void lastMoveUpdt(int moveNumber){loggeer("lastMoveUpdt");moveCount++;if(lastMove==moveNumber){lastMoveInARow++;}else{lastMove=moveNumber;lastMoveInARow=1;}}
    public void moveChangeCheck(){if(!dontRedecide){loggeer("WARNING Did not redecide move");}}

    public void genMove(String movenName,int baseDamage){loggeer("Genned Move");moveList.add(movenName);this.damage.add(new DamageInfo(this,baseDamage));}
    public void genAtkAuto(String movenName,int a0Base,int aLowBase,int aHighBase){moveList.add(movenName);
        if(this.type==EnemyType.NORMAL){if(asc>16){this.damage.add(new DamageInfo(this,aHighBase));}else if(asc>1){this.damage.add(new DamageInfo(this,aLowBase));}else{this.damage.add(new DamageInfo(this,a0Base));}
        }else if(this.type==EnemyType.ELITE){if(asc>17){this.damage.add(new DamageInfo(this,aHighBase));}else if(asc>2){this.damage.add(new DamageInfo(this,aLowBase));}else{this.damage.add(new DamageInfo(this,a0Base));}
        }else if(this.type==EnemyType.BOSS){if(asc>18){this.damage.add(new DamageInfo(this,aHighBase));}else if(asc>3){this.damage.add(new DamageInfo(this,aLowBase));}else{this.damage.add(new DamageInfo(this,a0Base));}}}
    public void genMovePlus(String movenName,int baseDamage,Intent intent){genMovePlus(movenName,baseDamage,intent,1);}public void genMovePlus(String movenName,int baseDamage,Intent intent,int hits){genMove(movenName,baseDamage);intentList.add(intent);hitList.add(hits);}

    public void decMve(String move,Intent intent){loggeer("Dec Mve: "+move);if(getFMoveList(move)!=-1){loggeer("Isn't -1");this.setMove(MOVERS[getFMoveList(move)],(byte)getFMoveList(move),intent);}else{loggeer("ERROR: -1");this.setMove("ERROR ERROR Cosmic Zone Mod",(byte)1,intent);}}
    public void decAtk(String move,Intent intent){decAtk(move,intent,1);}public void decAtk(String move,Intent intent,int hits){loggeer("Dec Atk: "+move);if(getFMoveList(move)!=-1){loggeer("Isn't -1");if(hits==1){loggeer("Hits==1");this.setMove(MOVERS[getFMoveList(move)],(byte)getFMoveList(move),intent,((DamageInfo)this.damage.get(getFMoveList(move))).base);}else{this.setMove(MOVERS[getFMoveList(move)],(byte)getFMoveList(move),intent,((DamageInfo)this.damage.get(getFMoveList(move))).base,hits,true);}}else{loggeer("ERROR: -1");this.setMove("ERROR Cosmic Zone Mod ERROR",(byte)1,intent);}}//TODO: note to self, add damage info for non-attacks
    public void decMvePlus(String move){loggeer("Dec Mve: "+move);if(dontRedecide){loggeer("Don't Redecide");return;}if(intentList.get(getFMoveList(move))==Intent.ATTACK||intentList.get(getFMoveList(move))==Intent.ATTACK_BUFF||intentList.get(getFMoveList(move))==Intent.ATTACK_DEBUFF||intentList.get(getFMoveList(move))==Intent.ATTACK_DEFEND){loggeer("Redirect to decide Atk");decAtkPlus(move);
        }else{if(getFMoveList(move)!=-1){this.setMove(MOVERS[getFMoveList(move)],(byte)getFMoveList(move),intentList.get(getFMoveList(move)));}else{this.setMove("ERROR ERROR Cosmic Zone Mod",(byte)1,intentList.get(getFMoveList(move)));}}dontRedecide=true;}
    public void decAtkMveNonAtkPlus(String move){if(dontRedecide){return;}if(getFMoveList(move)!=-1){this.setMove(MOVERS[getFMoveList(move)],(byte)getFMoveList(move),intentList.get(getFMoveList(move)));}else{this.setMove("ERROR ERROR Cosmic Zone Mod",(byte)1,intentList.get(getFMoveList(move)));}dontRedecide=true;}
    public void decAtkPlus(String move){if(getFMoveList(move)!=-1){loggeer("Dec Mve: "+move);if(dontRedecide){loggeer("Don't Redecide");return;}if(hitList.get(getFMoveList(move))==1){loggeer("From hit list=1");this.setMove(MOVERS[getFMoveList(move)],(byte)getFMoveList(move),intentList.get(getFMoveList(move)),((DamageInfo)this.damage.get(getFMoveList(move))).base);}else{this.setMove(MOVERS[getFMoveList(move)],(byte)getFMoveList(move),intentList.get(getFMoveList(move)),((DamageInfo)this.damage.get(getFMoveList(move))).base,hitList.get(getFMoveList(move)),true);}}else{this.setMove("ERROR Cosmic Zone Mod ERROR",(byte)1,intent);}dontRedecide=true;}

    public String getAppMove(ArrayList<String>appMoveList){return appMoveList.get(AbstractDungeon.cardRandomRng.random(appMoveList.size()-1));}
    public String getAppMoveUseWeights(){return getAppMoveUseWeights(applicableMoveList);}
    public String getAppMoveUseWeights(ArrayList<String>appMoveList){ArrayList<String>tempAppMoveList=new ArrayList<>();
        if(appMoveWeights.size()!=appMoveList.size()){loggeer("WARNING CZ Monster appMoveWeights.size()!=appMoveList.size()");}
        for(int i=0;i<appMoveList.size();i++){loggeer("i="+i);for(int o=0;o<appMoveWeights.get(i);o++){loggeer("o="+o);tempAppMoveList.add(appMoveList.get(i));}}
        if(tempAppMoveList.size()!=3&&tempAppMoveList.size()!=4&&tempAppMoveList.size()!=5&&tempAppMoveList.size()!=6&&tempAppMoveList.size()!=8&&tempAppMoveList.size()%10!=0){loggeer("Caution: CZ Monster tempAppMoveList.size() seems to be a weird size");}
        return tempAppMoveList.get(AbstractDungeon.cardRandomRng.random(tempAppMoveList.size()-1));}//return tempAppMoveList.get(ranGen.nextInt(tempAppMoveList.size()));
    public boolean limitUseInARow(int moveNumber,int useLimit){return lastMove==moveNumber&&lastMoveInARow>=useLimit;}
    public boolean limitUseInARow(int moveNumber,int useLimit,int moveNumber0){return(lastMove==moveNumber||lastMove==moveNumber0)&&lastMoveInARow>=useLimit;}
    public boolean limitUseInARow(int[] moveNumber,int useLimit){boolean k=false;for(int i:moveNumber){if(i==lastMove){k=true;}}return k&&lastMoveInARow>=useLimit;}





    //Ascension 1 - There will be approximately 60% more Elites spawned in a map. (Not my job)
    //~~~ ~~ ~ Ascension 2 - Normal enemies deal more damage with their attacks. (Done)
    //~~~ ~ ~~ Ascension 3 - Elites deal more damage with their attacks. (Done)
    //~~~  ~~~ Ascension 4 - Bosses deal more damage with their attacks. (Done)
    //Ascension 5 - Heal for 75% of missing health instead of 100% on new act. (Not my job)
    //Ascension 6 - Lose 10% health at the start of each run. (Not my job)
    //~~~ ~~ ~ Ascension 7 - Normal enemies have more HP. Some gain higher Block. They are overall harder to take down. (Done)
    //~~~ ~ ~~Ascension 8 - Elites have more HP. (Done)
    //~~~  ~~~Ascension 9 - Bosses have more HP. Some gain higher Block. They are overall harder to take down. (Done)
    //Ascension 10 - Obtain 1 Ascender's Bane at the start of each run. (Not my job)
    //Ascension 11 - Start each run with 1 less potion slot. (Not my job)
    //Toodo: Ascension 12 - Upgraded cards are 50% less likely to appear in Act 2 (12.5%, down from 25%) and Act 3 (25%, down from 50%).
    //Toodo maybe: Ascension 13 - Bosses drop 25% less gold
    //Ascension 14 - -5 for Ironclad, -4 for Silent, Defect, and Watcher (Not my job)
    //Toodo: Ascension 15 - Many events have less positive outcomes and more severe consequences. For example: More HP loss. Less gold or card as rewards. Guaranteed Curse.
    //Toodo: Ascension 16 - Everything costs 10% more.
    //~~~ ~~ ~ Ascension 17 - Normal enemies have more challenging movesets and abilities. (Done)
    //~~~ ~ ~~Ascension Icon Ascension 18 - Elites have more challenging movesets and abilities. (Done)
    //~~~  ~~~Ascension Icon Ascension 19 - Bosses have more challenging movesets and abilities. (Done)
    //Ascension Icon Ascension 20 - Fight 2 bosses at the end of Act 3 (Not my job)
}


/*public void decAtkPlus(String move,int increase){if(getFMoveList(move)!=-1){if(dontRedecide){return;}if(hitList.get(getFMoveList(move))==1){int t=((DamageInfo)this.damage.get(getFMoveList(move))).base;((DamageInfo)this.damage.get(getFMoveList(move))).base+=increase;
            this.setMove(MOVES[getFMoveList(move)],(byte)getFMoveList(move),intentList.get(getFMoveList(move)),((DamageInfo)this.damage.get(getFMoveList(move))).base);((DamageInfo)this.damage.get(getFMoveList(move))).base=t;
        }else{int t=((DamageInfo)this.damage.get(getFMoveList(move))).base;((DamageInfo)this.damage.get(getFMoveList(move))).base+=increase;
            this.setMove(MOVES[getFMoveList(move)],(byte)getFMoveList(move),intentList.get(getFMoveList(move)),((DamageInfo)this.damage.get(getFMoveList(move))).base,hitList.get(getFMoveList(move)),true);((DamageInfo)this.damage.get(getFMoveList(move))).base=t;}
    }else{this.setMove("ERROR Cosmic Zone Mod ERROR",(byte)1,intent);}dontRedecide=true;}*/