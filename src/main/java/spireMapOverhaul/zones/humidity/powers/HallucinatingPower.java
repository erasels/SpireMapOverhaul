//
//with apologies to Indi
//

package spireMapOverhaul.zones.humidity.powers;

import basemod.ReflectionHacks;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.esotericsoftware.spine.Skeleton;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch2;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import spireMapOverhaul.SpireAnniversary6Mod;
import spireMapOverhaul.abstracts.AbstractSMOPower;
import spireMapOverhaul.util.Wiz;
import spireMapOverhaul.zones.humidity.HumidityZone;

import static spireMapOverhaul.SpireAnniversary6Mod.getShaderConfig;

public class HallucinatingPower extends AbstractSMOPower {
    public static final String POWER_ID = SpireAnniversary6Mod.makeID("HallucinatingPower");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    public static String ZONE_ID = HumidityZone.ID;

    private boolean justApplied = false;

    public static boolean isActive=true;
    public static float fakeIntensity =-0.25f;
    public static float displayedIntensity =0.0f;
    public static float cycleTime=0.0f;

    public HallucinatingPower(AbstractCreature owner, int amount) {
        super(POWER_ID,NAME,ZONE_ID, PowerType.DEBUFF, false, owner, amount);
        boolean isSourceMonster=true;
        if (AbstractDungeon.actionManager.turnHasEnded && isSourceMonster) {
            this.justApplied = true;
        }
        this.isTurnBased = true;
    }
    @Override
    public void updateDescription() {
        if(getShaderConfig())
            this.description = DESCRIPTIONS[0];
        else
            this.description = DESCRIPTIONS[1];
    }

    public void atEndOfRound() {
        if (this.justApplied) {
            this.justApplied = false;
        } else {
            if (this.amount == 0) {
                this.addToBot(new RemoveSpecificPowerAction(this.owner, this.owner, this));
            } else {
                this.addToBot(new ReducePowerAction(this.owner, this.owner, this, 1));
            }
        }
    }




    @SpirePatch2(clz = CardCrawlGame.class, method = "update")
    public static class HallucinationUpdatePatch{
        @SpirePostfixPatch
        public static void Foo(){
            cycleTime+=Gdx.graphics.getDeltaTime();
            boolean shouldBeActive=Wiz.adp()!=null && Wiz.adp().hasPower(HallucinatingPower.POWER_ID);
            if(!getShaderConfig())shouldBeActive=false;
            //if(true)shouldBeActive=true;

            if(shouldBeActive){
                if(fakeIntensity <1f){
                    fakeIntensity +=0.2f*Gdx.graphics.getDeltaTime();
                    if(fakeIntensity >=1f) fakeIntensity =1f;
                }
            }else{
                if(fakeIntensity >-0.25f){
                    fakeIntensity -=0.5f*Gdx.graphics.getDeltaTime();
                    if(fakeIntensity <=-0.25f) fakeIntensity =-0.25f;
                }
            }
            displayedIntensity = fakeIntensity;
            if(displayedIntensity<0f)displayedIntensity=0f;
            //by having intensity go below 0, we ensure that scaling properly returns to normal before the effect shuts off
            isActive=(fakeIntensity>-0.25f);
        }
    }




    public static float getCardMultiplier(AbstractCard __instance){
        if(__instance==null || __instance.uuid==null)return 1;
        float hashCodeRatio = (float) (__instance.uuid.hashCode()-(long)Integer.MIN_VALUE) / ((long)Integer.MAX_VALUE-(long)Integer.MIN_VALUE);
        //hashCodeRatio is now between 0 and 1
        float cycleSpeed = (hashCodeRatio+1)/4f;
        float cosWave = (float) (displayedIntensity /2 * Math.cos(cycleTime*cycleSpeed));
        return 1+cosWave;
    }

    public static float getCreatureMultiplier(AbstractCreature __instance){
        float hashCodeRatio = (float) (__instance.hashCode()-(long)Integer.MIN_VALUE) / ((long)Integer.MAX_VALUE-(long)Integer.MIN_VALUE);
        //hashCodeRatio is now between 0 and 1
        float cycleSpeed = (hashCodeRatio+1)/4f;
        float cosWave = (float) (displayedIntensity /2 * Math.cos(cycleTime*cycleSpeed));
        return 1+cosWave;
    }

    @SpirePatch2(clz = AbstractCard.class, paramtypez = { SpriteBatch.class }, method = "render")
    public static class CardSizeModifierPatch {
        @SpirePrefixPatch
        public static void doubleDrawScale(AbstractCard __instance) {
            if(!isActive)return;
            float multiplier = getCardMultiplier(__instance);
            __instance.drawScale *= multiplier;
        }
        @SpirePostfixPatch
        public static void setDrawScaleBack(AbstractCard __instance) {
            if(!isActive)return;
            float multiplier = getCardMultiplier(__instance);
            __instance.drawScale /= multiplier;
        }
    }

    @SpirePatch2(clz = AbstractMonster.class, paramtypez = { SpriteBatch.class }, method = "render")
    public static class MonsterSizeModifierPatch {
        @SpirePrefixPatch
        public static void doubleDrawScale(AbstractMonster __instance) {
            if(!isActive)return;
            scaleCreature(__instance);
        }
        @SpirePostfixPatch
        public static void setDrawScaleBack(AbstractMonster __instance) {
            if(!isActive)return;
            unscaleCreature(__instance);
        }
    }

    @SpirePatch2(clz = AbstractPlayer.class, paramtypez = { SpriteBatch.class }, method = "render")
    public static class PlayerSizeModifierPatch {
        @SpirePrefixPatch
        public static void doubleDrawScale(AbstractPlayer __instance) {
            if(!isActive)return;
            scaleCreature(__instance);
        }
        @SpirePostfixPatch
        public static void setDrawScaleBack(AbstractPlayer __instance) {
            if(!isActive)return;
            unscaleCreature(__instance);
        }
    }

    public static void scaleCreature(AbstractCreature __instance){
        float multiplier = getCreatureMultiplier(__instance);
        Skeleton skeleton = ReflectionHacks.getPrivate(__instance,AbstractCreature.class,"skeleton");

        //TODO: seek expert advice regarding why there are apparently two sets of bones data
        // and different creatures use only one or the other
        {
            float scaleX = skeleton.getData().getBones().get(0).getScaleX();
            float scaleY = skeleton.getData().getBones().get(0).getScaleY();
            skeleton.getData().getBones().get(0).setScaleX(scaleX * multiplier);
            skeleton.getData().getBones().get(0).setScaleY(scaleY * multiplier);
        }
        {
            float scaleX = skeleton.getBones().get(0).getScaleX();
            float scaleY = skeleton.getBones().get(0).getScaleY();
            skeleton.getBones().get(0).setScaleX(scaleX * multiplier);
            skeleton.getBones().get(0).setScaleY(scaleY * multiplier);
        }

    }
    public static void unscaleCreature(AbstractCreature __instance){
        float multiplier = getCreatureMultiplier(__instance);
        Skeleton skeleton = ReflectionHacks.getPrivate(__instance,AbstractCreature.class,"skeleton");
        {
            float scaleX = skeleton.getData().getBones().get(0).getScaleX();
            float scaleY = skeleton.getData().getBones().get(0).getScaleY();
            skeleton.getData().getBones().get(0).setScaleX(scaleX / multiplier);
            skeleton.getData().getBones().get(0).setScaleY(scaleY / multiplier);
        }
        {
            float scaleX = skeleton.getBones().get(0).getScaleX();
            float scaleY = skeleton.getBones().get(0).getScaleY();
            skeleton.getBones().get(0).setScaleX(scaleX / multiplier);
            skeleton.getBones().get(0).setScaleY(scaleY / multiplier);
        }
    }

}
