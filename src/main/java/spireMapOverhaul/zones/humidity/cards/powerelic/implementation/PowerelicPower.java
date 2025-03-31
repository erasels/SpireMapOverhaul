package spireMapOverhaul.zones.humidity.cards.powerelic.implementation;

import basemod.ReflectionHacks;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.evacipated.cardcrawl.modthespire.patcher.PatchingException;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import com.megacrit.cardcrawl.vfx.combat.FlashPowerEffect;
import com.megacrit.cardcrawl.vfx.combat.GainPowerEffect;
import javassist.CannotCompileException;
import javassist.CtBehavior;
import spireMapOverhaul.abstracts.AbstractSMOPower;
import spireMapOverhaul.util.Wiz;

import java.util.ArrayList;
import java.util.Iterator;

import static spireMapOverhaul.SpireAnniversary6Mod.makeID;

public class PowerelicPower extends AbstractSMOPower {

    public static final String POWER_ID = makeID(PowerelicPower.class.getSimpleName());
    public AbstractRelic sourceRelic;
    private static int powerIdOffset;

    public PowerelicPower(String ID, String NAME, PowerType powerType, boolean isTurnBased, AbstractCreature owner, int amount) {
        super(ID, NAME, powerType, isTurnBased, owner, amount);
    }
    public PowerelicPower(AbstractRelic sourceRelic){
        this(PowerelicPower.POWER_ID,sourceRelic.name,PowerType.BUFF,false, Wiz.adp(),sourceRelic.counter);
        this.sourceRelic=sourceRelic;
        this.ID = POWER_ID + powerIdOffset;
        ++powerIdOffset;

        setRelicInfo(sourceRelic);
    }

    public void setRelicInfo(AbstractRelic sourceRelic){
        if(sourceRelic!=null) {
            this.name = sourceRelic.name;
            this.description = sourceRelic.description;
            this.amount = sourceRelic.counter;
            this.img = sourceRelic.img;
            if(this.img!=null)this.region48=null;
        }
    }

    public void update(int slot){
        setRelicInfo(sourceRelic);
        super.update(slot);
    }

    public void renderIcons(SpriteBatch sb, float x, float y, Color c) {
        //relic: sb.draw(this.img, this.currentX - 64.0F, this.currentY - 64.0F, 64.0F, 64.0F, 128.0F, 128.0F, this.scale, this.scale, this.rotation, 0, 0, 128, 128, false, false);
        if (this.img != null) {
            sb.setColor(c);
            sb.draw(this.img, x - 43F*Settings.scale, y - 43F*Settings.scale, 0F, 0F, 128F, 128F, 1.25F*48/128F*Settings.scale * 1.5F, 1.25F*48/128F*Settings.scale * 1.5F, 0.0F, 0, 0, 128, 128, false, false);
        }
//        } else {
//            sb.setColor(c);
//
//            if (Settings.isMobile) {
//                sb.draw(this.region48, x - (float)this.region48.packedWidth / 2.0F, y - (float)this.region48.packedHeight / 2.0F, (float)this.region48.packedWidth / 2.0F, (float)this.region48.packedHeight / 2.0F, (float)this.region48.packedWidth, (float)this.region48.packedHeight, Settings.scale * 1.17F, Settings.scale * 1.17F, 0.0F);
//            } else {
//                sb.draw(this.region48, x - (float)this.region48.packedWidth / 2.0F, y - (float)this.region48.packedHeight / 2.0F, (float)this.region48.packedWidth / 2.0F, (float)this.region48.packedHeight / 2.0F, (float)this.region48.packedWidth, (float)this.region48.packedHeight, Settings.scale, Settings.scale, 0.0F);
//            }
//        }
        ArrayList<AbstractGameEffect> effect = ReflectionHacks.getPrivate(this, AbstractPower.class,"effect");
        Iterator var5 = effect.iterator();
        while(var5.hasNext()) {
            AbstractGameEffect e = (AbstractGameEffect)var5.next();
            e.render(sb, x, y);
        }
    }
    @Override
    public void renderAmount(SpriteBatch sb, float x, float y, Color c) {
        super.renderAmount(sb,x,y,c);
        if (this.amount == 0) {
            if (!this.isTurnBased) {
                Color greenColor=ReflectionHacks.getPrivate(this,AbstractPower.class,"greenColor");
                greenColor.a = c.a;
                c = greenColor;
            }
            FontHelper.renderFontRightTopAligned(sb, FontHelper.powerAmountFont, Integer.toString(this.amount), x, y, this.fontScale, c);
        }
    }


    @SpirePatch(clz = GainPowerEffect.class, method = SpirePatch.CLASS)
    public static class GainPowerEffectFields
    {
        public static SpireField<Texture> relicImg = new SpireField<>(() -> null);
    }
    @SpirePatch(clz= GainPowerEffect.class,method=SpirePatch.CONSTRUCTOR)
    public static class GainPowerEffectPatch{
        @SpirePostfixPatch
        public static void patch(GainPowerEffect __instance, AbstractPower power) {
            if(power instanceof PowerelicPower){
                if (power.img != null) {
                    GainPowerEffectFields.relicImg.set(__instance,power.img);
                    ReflectionHacks.setPrivate(__instance,GainPowerEffect.class,"region48",null);
                }
            }
        }
    }
    @SpirePatch(clz=GainPowerEffect.class,method="render",paramtypez = {SpriteBatch.class,float.class,float.class})
    public static class GainPowerRenderPatch{
        @SpireInsertPatch(locator = Locator.class )
        public static void Patch(GainPowerEffect __instance,SpriteBatch sb,float x,float y) {
            Texture img=GainPowerEffectFields.relicImg.get(__instance);
            float scale=ReflectionHacks.getPrivate(__instance,GainPowerEffect.class,"scale");
            scale*=2/3F;
            if(img!=null){
                sb.draw(img,
                    x - (float)img.getWidth() / 2.0F, y - (float)img.getHeight() / 2.0F,
                    (float)img.getWidth() / 2.0F, (float)img.getHeight() / 2.0F,
                    (float)img.getWidth(), (float)img.getHeight(), scale, scale, 0.0F,
                    0,0,img.getWidth(),img.getHeight(),false,false);
            }
        }
        private static class Locator extends SpireInsertLocator {
            public int[] Locate(CtBehavior ctMethodToPatch) throws CannotCompileException, PatchingException {
                Matcher finalMatcher = new Matcher.FieldAccessMatcher(GainPowerEffect.class, "region48");
                return LineFinder.findInOrder(ctMethodToPatch, new ArrayList<Matcher>(), finalMatcher);
            }
        }
    }

    @SpirePatch(clz = FlashPowerEffect.class, method = SpirePatch.CLASS)
    public static class FlashPowerEffectFields
    {
        public static SpireField<Boolean> isPowerelic = new SpireField<>(() -> false);
    }
    @SpirePatch2(clz = FlashPowerEffect.class, method = SpirePatch.CONSTRUCTOR)
    public static class FlashPowerConstructorPatch
    {
        @SpirePostfixPatch
        public static void Patch(FlashPowerEffect __instance, AbstractPower power){
            if(power instanceof PowerelicPower){
                FlashPowerEffectFields.isPowerelic.set(__instance,true);
            }
        }
    }
    @SpirePatch(clz=FlashPowerEffect.class,method="render",paramtypez = {SpriteBatch.class})
    public static class FlashPowerRenderPatch{
        @SpireInsertPatch(locator = Locator.class )
        public static void Patch(FlashPowerEffect __instance,SpriteBatch sb) {
            if(FlashPowerEffectFields.isPowerelic.get(__instance)){
                Texture img=ReflectionHacks.getPrivate(__instance,FlashPowerEffect.class,"img");
                if(img!=null) {
                    float x = ReflectionHacks.getPrivate(__instance, FlashPowerEffect.class, "x");
                    float y = ReflectionHacks.getPrivate(__instance, FlashPowerEffect.class, "y");
                    float scale = ReflectionHacks.getPrivate(__instance, FlashPowerEffect.class, "scale");
                    scale*=1.5F;
                    sb.draw(img, x-img.getWidth()/2.0F, y-img.getHeight()/2.0F, img.getWidth() / 2.0F, img.getHeight() / 2.0F,
                            img.getWidth(), img.getHeight(), scale * 3.0F, scale * 3.0F, 0.0F, 0, 0, img.getWidth(), img.getHeight(), false, false);
                }
            }
        }
        private static class Locator extends SpireInsertLocator {
            public int[] Locate(CtBehavior ctMethodToPatch) throws CannotCompileException, PatchingException {
                Matcher finalMatcher = new Matcher.FieldAccessMatcher(FlashPowerEffect.class, "img");
                return LineFinder.findInOrder(ctMethodToPatch, new ArrayList<Matcher>(), finalMatcher);
            }
        }
    }


}
