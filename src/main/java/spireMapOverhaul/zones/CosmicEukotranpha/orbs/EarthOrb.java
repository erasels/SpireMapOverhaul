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
import com.megacrit.cardcrawl.powers.ThornsPower;
import com.megacrit.cardcrawl.vfx.combat.DarkOrbPassiveEffect;
import com.megacrit.cardcrawl.vfx.combat.OrbFlareEffect;
import spireMapOverhaul.zones.CosmicEukotranpha.CosmicZoneMod;import static spireMapOverhaul.zones.CosmicEukotranpha.util.CosmicShortcuts.*;public class
EarthOrb extends CosmicZoneOrb{public AbstractOrb makeCopy(){return new
				EarthOrb();}public static final String ORB_ID=CosmicZoneMod.makeID(
				EarthOrb.class.getSimpleName());public static OrbStrings orbString=CardCrawlGame.languagePack.getOrbString(ORB_ID);
public EarthOrb(){this.ID=ORB_ID;this.name=orbString.NAME;
	this.baseEvokeAmount=2;this.evokeAmount=this.baseEvokeAmount;
	this.basePassiveAmount=1;this.passiveAmount=this.basePassiveAmount;this.updateDescription();}
public void updateDescription(){this.applyFocus();this.description=orbString.DESCRIPTION[0]+this.passiveAmount+orbString.DESCRIPTION[1]+this.evokeAmount+orbString.DESCRIPTION[2];}
public void onEndOfTurn(){retain(passiveAmount);}
public void onEvoke(){poT(AbstractDungeon.player,AbstractDungeon.player,new ThornsPower(AbstractDungeon.player,evokeAmount));}
}