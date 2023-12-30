package spireMapOverhaul.zones.CosmicEukotranpha.orbs;import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.localization.OrbStrings;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.orbs.Frost;
import com.megacrit.cardcrawl.powers.PlatedArmorPower;
import com.megacrit.cardcrawl.vfx.combat.DarkOrbPassiveEffect;
import com.megacrit.cardcrawl.vfx.combat.OrbFlareEffect;
import spireMapOverhaul.zones.CosmicEukotranpha.CosmicZoneMod;import static spireMapOverhaul.zones.CosmicEukotranpha.util.CosmicShortcuts.*;public class
MercuryOrb extends CosmicZoneOrb{public AbstractOrb makeCopy(){return new
				MercuryOrb();}public static final String ORB_ID=CosmicZoneMod.makeID(
				MercuryOrb.class.getSimpleName());public static OrbStrings orbString=CardCrawlGame.languagePack.getOrbString(ORB_ID);
public MercuryOrb(){this.ID=ORB_ID;this.name=orbString.NAME;
	this.baseEvokeAmount=2;this.evokeAmount=this.baseEvokeAmount;
	this.basePassiveAmount=1;this.passiveAmount=this.basePassiveAmount;this.updateDescription();}
public void updateDescription(){this.applyFocus();this.description=orbString.DESCRIPTION[0]+this.passiveAmount+orbString.DESCRIPTION[1]+this.evokeAmount+orbString.DESCRIPTION[2];}
public void onStartOfTurn(){draw(passiveAmount);}
public void onEvoke(){poT(AbstractDungeon.player,AbstractDungeon.player,new PlatedArmorPower(AbstractDungeon.player,evokeAmount));}


/*
public void updateAnimation(){
	super.updateAnimation();
	this.angle += Gdx.graphics.getDeltaTime() * 120.0F;
	this.vfxTimer -= Gdx.graphics.getDeltaTime();
	if (this.vfxTimer < 0.0F) {
		AbstractDungeon.effectList.add(new DarkOrbPassiveEffect(this.cX, this.cY));
		this.vfxTimer = 0.25F;
	}
	
}

public void render(SpriteBatch sb) {
	sb.setColor(this.c);
	sb.draw(this.img, this.cX - 48.0F, this.cY - 48.0F + this.bobEffect.y, 48.0F, 48.0F, 96.0F, 96.0F, this.scale, this.scale, this.angle, 0, 0, 96, 96, false, false);
	this.shineColor.a = this.c.a / 3.0F;
	sb.setColor(this.shineColor);
	sb.setBlendFunction(770, 1);
	sb.draw(this.img, this.cX - 48.0F, this.cY - 48.0F + this.bobEffect.y, 48.0F, 48.0F, 96.0F, 96.0F, this.scale * 1.2F, this.scale * 1.2F, this.angle / 1.2F, 0, 0, 96, 96, false, false);
	sb.draw(this.img, this.cX - 48.0F, this.cY - 48.0F + this.bobEffect.y, 48.0F, 48.0F, 96.0F, 96.0F, this.scale * 1.5F, this.scale * 1.5F, this.angle / 1.4F, 0, 0, 96, 96, false, false);
	sb.setBlendFunction(770, 771);
	this.renderText(sb);
	this.hb.render(sb);
}

protected void renderText(SpriteBatch sb) {
	FontHelper.renderFontCentered(sb, FontHelper.cardEnergyFont_L, Integer.toString(this.evokeAmount), this.cX + NUM_X_OFFSET, this.cY + this.bobEffect.y / 2.0F + NUM_Y_OFFSET - 4.0F * Settings.scale, new Color(0.2F, 1.0F, 1.0F, this.c.a), this.fontScale);
	FontHelper.renderFontCentered(sb, FontHelper.cardEnergyFont_L, Integer.toString(this.passiveAmount), this.cX + NUM_X_OFFSET, this.cY + this.bobEffect.y / 2.0F + NUM_Y_OFFSET + 20.0F * Settings.scale, this.c, this.fontScale);
}*/

}
