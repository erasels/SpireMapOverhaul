package spireMapOverhaul.rewards;

import basemod.abstracts.CustomReward;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import spireMapOverhaul.SpireAnniversary6Mod;
import spireMapOverhaul.patches.CustomRewardTypes;
import spireMapOverhaul.util.TexLoader;

import static spireMapOverhaul.SpireAnniversary6Mod.modID;

public class HealReward extends CustomReward {
    private static final String[] uiStrings = CardCrawlGame.languagePack.getUIString(SpireAnniversary6Mod.makeID("HealReward")).TEXT;
    public int amount;
    public String iconPath;

    public HealReward(int amount) {
        this("HealReward.png", amount);
    }

    public HealReward(String iconPath, int amount) {
        super(TexLoader.getTexture(modID + "Resources/images/ui/" + iconPath), uiStrings[0] + amount, CustomRewardTypes.HEALREWARD);
        this.amount = amount;
        this.iconPath = iconPath;
    }

    @Override
    public boolean claimReward() {
        AbstractDungeon.player.heal(amount);
        return true;
    }
}
