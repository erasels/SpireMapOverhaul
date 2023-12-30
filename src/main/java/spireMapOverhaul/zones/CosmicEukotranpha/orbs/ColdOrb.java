package spireMapOverhaul.zones.CosmicEukotranpha.orbs;import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.localization.OrbStrings;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.orbs.Frost;
import com.megacrit.cardcrawl.powers.*;
import com.megacrit.cardcrawl.vfx.combat.DarkOrbPassiveEffect;
import com.megacrit.cardcrawl.vfx.combat.OrbFlareEffect;
import spireMapOverhaul.zones.CosmicEukotranpha.CosmicZoneMod;
import spireMapOverhaul.zones.CosmicEukotranpha.powers.CZGainDexterityPower;

import static spireMapOverhaul.zones.CosmicEukotranpha.util.CosmicShortcuts.*;public class
ColdOrb extends CosmicZoneOrb{public AbstractOrb makeCopy(){return new
				ColdOrb();}public static final String ORB_ID=CosmicZoneMod.makeID(
				ColdOrb.class.getSimpleName());public static OrbStrings orbString=CardCrawlGame.languagePack.getOrbString(ORB_ID);
public ColdOrb(){this.ID=ORB_ID;this.name=orbString.NAME;
	this.baseEvokeAmount=2;this.evokeAmount=this.baseEvokeAmount;
	this.basePassiveAmount=2;this.passiveAmount=this.basePassiveAmount;this.updateDescription();}
public void updateDescription(){this.applyFocus();this.description=orbString.DESCRIPTION[0]+this.passiveAmount+orbString.DESCRIPTION[1]+this.evokeAmount+orbString.DESCRIPTION[2];}
public void onEndOfTurn(){poT(AbstractDungeon.player,AbstractDungeon.player,new StrengthPower(AbstractDungeon.player,-passiveAmount));if(!AbstractDungeon.player.hasPower("Artifact")){poT(AbstractDungeon.player,AbstractDungeon.player,new GainStrengthPower(AbstractDungeon.player,passiveAmount));}
	poT(AbstractDungeon.player,AbstractDungeon.player,new DexterityPower(AbstractDungeon.player,passiveAmount));poT(AbstractDungeon.player,AbstractDungeon.player,new LoseDexterityPower(AbstractDungeon.player,passiveAmount));
}
public void onEvoke(){poT(AbstractDungeon.player,AbstractDungeon.player,new DexterityPower(AbstractDungeon.player,-evokeAmount));if(!AbstractDungeon.player.hasPower("Artifact")){poT(AbstractDungeon.player,AbstractDungeon.player,new CZGainDexterityPower(AbstractDungeon.player,AbstractDungeon.player,evokeAmount));}
	poT(AbstractDungeon.player,AbstractDungeon.player,new StrengthPower(AbstractDungeon.player,evokeAmount));poT(AbstractDungeon.player,AbstractDungeon.player,new LoseStrengthPower(AbstractDungeon.player,evokeAmount));}
}