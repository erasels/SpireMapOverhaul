package spireMapOverhaul.zones.CosmicEukotranpha.relics.Special;
import spireMapOverhaul.SpireAnniversary6Mod;
import spireMapOverhaul.zones.CosmicEukotranpha.relics.BaseRelic;

import static com.megacrit.cardcrawl.relics.AbstractRelic.LandingSound.MAGICAL;
import static com.megacrit.cardcrawl.relics.AbstractRelic.RelicTier.SPECIAL;
public class PinkRainRelic extends BaseRelic{public static String ID=SpireAnniversary6Mod.makeID(
				PinkRainRelic.class.getSimpleName());public
PinkRainRelic(){super(ID,"",SPECIAL,MAGICAL);}
//At the start of each even number turn, Choose up to 3 from 8 cosmic cards to add to hand, Lose 1 energy for each added. Fascinated: Everyone gains 2 Curiosity. Lose 2 hp

}
