package spireMapOverhaul.zones.humidity.powers;

import basemod.ReflectionHacks;
import com.badlogic.gdx.graphics.Color;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.InstantKillAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.AbstractEvent;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.city.SphericGuardian;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.vfx.combat.GiantCustomTextEffect;
import com.megacrit.cardcrawl.vfx.combat.GiantTextEffect;
import com.megacrit.cardcrawl.vfx.combat.PowerBuffEffect;
import com.megacrit.cardcrawl.vfx.combat.WeightyImpactEffect;
import spireMapOverhaul.SpireAnniversary6Mod;
import spireMapOverhaul.abstracts.AbstractSMOPower;
import spireMapOverhaul.util.Wiz;
import spireMapOverhaul.zones.humidity.HumidityZone;

public class BaseballPower extends AbstractSMOPower {
    public static final String POWER_ID = SpireAnniversary6Mod.makeID("BaseballPower");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    public static String ZONE_ID = HumidityZone.ID;

    public BaseballPower(AbstractCreature owner, int amount) {
        super(POWER_ID,NAME,ZONE_ID, AbstractPower.PowerType.BUFF, false, owner, amount);
    }
    @Override
    public void updateDescription() {
        int remaining=3-this.amount;
        remaining=Math.min(remaining,3);
        if(remaining>0)
            this.description = DESCRIPTIONS[0] + remaining + DESCRIPTIONS[remaining] + DESCRIPTIONS[4];
        else
            this.description=DESCRIPTIONS[5];
    }

    public void onUseCard(AbstractCard card, UseCardAction action) {
        if (card.tags.contains(AbstractCard.CardTags.STRIKE)) {
            //if this is about to be the third strike, it's a knockout
            if(this.amount>=2){
                this.addToTop(new VFXAction(new GiantCustomTextEffect(this.owner.hb.cX, this.owner.hb.cY,DESCRIPTIONS[6])));
                this.addToTop(new WaitAction(0.8F));
                this.addToTop(new VFXAction(new WeightyImpactEffect(this.owner.hb.cX, this.owner.hb.cY)));
                this.addToTop(new InstantKillAction(this.owner));
            }
            this.addToTop(new UpdatePowerDescriptionAction(this));
            this.addToTop(new ApplyPowerAction(this.owner, this.owner, new BaseballPower(this.owner, 1), 1));
            this.flash();
        }
    }

    @SpirePatch2(clz= PowerBuffEffect.class,method=SpirePatch.CONSTRUCTOR)
    public static class ReplaceOverheadTextPatch {
        @SpirePrefixPatch
        public static void Foo(PowerBuffEffect __instance, @ByRef String[] msg){
            //Don't say "+1 Bottom of the Ninth" when the player plays a Strike.

            if(msg[0].charAt(0) == '+' && msg[0].contains(powerStrings.NAME)) {
                //unfortunately we don't have a reference to the original power to get the stack count.
                //attempt to it manually.
                int baseballCount=0;
                AbstractMonster baseball=null;
                for(AbstractMonster m : Wiz.getEnemies()){
                    if(m.hasPower(BaseballPower.POWER_ID)){
                        baseball=m;
                        baseballCount+=1;
                    }
                }
                if(baseballCount==1){
                    int strikeCount=baseball.getPower(BaseballPower.POWER_ID).amount;
                    if(strikeCount==1)
                        msg[0]=DESCRIPTIONS[7];
                    else if(strikeCount==2)
                        msg[0]=DESCRIPTIONS[8];
                    else
                        msg[0]=DESCRIPTIONS[9];
                }else{
                    //if we're in a non-vanilla encounter with two baseballs, we can't determine stack count
                    //so just say "+1 Strike"
                    msg[0]=DESCRIPTIONS[10];
                }
            }

        }
    }
}

