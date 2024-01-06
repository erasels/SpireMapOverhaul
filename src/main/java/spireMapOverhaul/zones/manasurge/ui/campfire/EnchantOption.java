package spireMapOverhaul.zones.manasurge.ui.campfire;

import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.ui.campfire.AbstractCampfireOption;
import spireMapOverhaul.SpireAnniversary6Mod;
import spireMapOverhaul.util.TexLoader;

import static spireMapOverhaul.SpireAnniversary6Mod.makeUIPath;

public class EnchantOption extends AbstractCampfireOption {
    public static final String ID = SpireAnniversary6Mod.makeID("EnchantOption");
    public static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString(ID);
    public static final String[] TEXT = uiStrings.TEXT;
    private static final Texture IMG = TexLoader.getTexture(makeUIPath("ManaSurge/enchant.png"));

    public EnchantOption(boolean active) {
        this.label = TEXT[0];
        this.usable = active;
        this.description = TEXT[1];
        this.img = IMG;
    }

    @Override
    public void useOption() {
        if (this.usable) {
            AbstractDungeon.effectList.add(new CampfireEnchantEffect());
        }
    }
}