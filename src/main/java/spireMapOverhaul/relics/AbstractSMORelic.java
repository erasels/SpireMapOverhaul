package spireMapOverhaul.relics;

import basemod.abstracts.CustomRelic;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import spireMapOverhaul.util.TexLoader;

import static spireMapOverhaul.SpireAnniversary6Mod.makeRelicPath;
import static spireMapOverhaul.SpireAnniversary6Mod.modID;

public abstract class AbstractSMORelic extends CustomRelic {
    public AbstractCard.CardColor color;

    public AbstractSMORelic(String setId, AbstractRelic.RelicTier tier, AbstractRelic.LandingSound sfx) {
        super(setId, TexLoader.getTexture(makeRelicPath(setId.replace(modID + ":", "") + ".png")), tier, sfx);
        outlineImg = TexLoader.getTexture(makeRelicPath(setId.replace(modID + ":", "") + "Outline.png"));
    }

    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }
}