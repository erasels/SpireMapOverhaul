package spireMapOverhaul.zones.manasurge.ui.campfire;

import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.ui.campfire.AbstractCampfireOption;
import spireMapOverhaul.util.TexLoader;

import static spireMapOverhaul.SpireAnniversary6Mod.makeImagePath;

public class EnchantOption extends AbstractCampfireOption {
    private static final Texture IMG = TexLoader.getTexture(makeUIPath("ManaSurge/enchant.png"));

    public EnchantOption(boolean active) {
        this.label = "Enchant";
        this.usable = active;
        this.description = "Give a card a permanent Enchantment that lasts outside of this zone.";
        this.img = IMG;
    }

    @Override
    public void useOption() {
        if (this.usable) {
            AbstractDungeon.effectList.add(new CampfireEnchantEffect());
        }
    }
}