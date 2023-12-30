package spireMapOverhaul.zones.CosmicEukotranpha.relics.Special;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import spireMapOverhaul.SpireAnniversary6Mod;
import spireMapOverhaul.zones.CosmicEukotranpha.relics.BaseRelic;

import static com.megacrit.cardcrawl.relics.AbstractRelic.LandingSound.MAGICAL;
import static com.megacrit.cardcrawl.relics.AbstractRelic.RelicTier.SPECIAL;
public class MarkOfTheDormant extends BaseRelic{public static String ID=SpireAnniversary6Mod.makeID(
				MarkOfTheDormant.class.getSimpleName());public
MarkOfTheDormant(){super(ID,"",SPECIAL,MAGICAL);}
//On obtain: Half max hp. [] Combat end: Heal 50. [] On Rest: Gain 5 max hp
public void onEquip(){loseMaxHp(AbstractDungeon.player.maxHealth/2);}
public void onVictory(){heal(50);}
public void onRest(){incMaxHp(5);}
}
