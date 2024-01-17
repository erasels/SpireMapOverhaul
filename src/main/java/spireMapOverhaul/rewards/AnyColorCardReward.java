package spireMapOverhaul.rewards;

import basemod.abstracts.CustomReward;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.colorless.Madness;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import spireMapOverhaul.SpireAnniversary6Mod;
import spireMapOverhaul.util.TexLoader;

import java.util.ArrayList;
import java.util.Arrays;

import static spireMapOverhaul.SpireAnniversary6Mod.makeID;
import static spireMapOverhaul.SpireAnniversary6Mod.makeImagePath;
import static spireMapOverhaul.patches.AnyColorCardRewardPatch.rewardsShouldBeAnyColor;
import static spireMapOverhaul.patches.CustomRewardTypes.SMO_ANYCOLORCARDREWARD;

public class AnyColorCardReward extends CustomReward {
    private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString(makeID("AnyColorCardReward"));

    public AnyColorCardReward() {
        super(TexLoader.getTexture(makeImagePath("ui/AnyColorCardReward.png")), uiStrings.TEXT[0], SMO_ANYCOLORCARDREWARD);

        // Gets three valid cards of any color using the same logic as Prismatic Shard
        rewardsShouldBeAnyColor = true;
        this.cards = AbstractDungeon.getRewardCards();
        rewardsShouldBeAnyColor = false;

        init();
    }

    public AnyColorCardReward(String cards) {
        super(TexLoader.getTexture(makeImagePath("ui/AnyColorCardReward.png")), uiStrings.TEXT[0], SMO_ANYCOLORCARDREWARD);

        this.cards = new ArrayList<>();

        //Loading save
        String[] cardsList = cards.split("#");
        for (String s : cardsList) {
            String[] params = s.split("\\|");
            if (params.length == 3) {
                this.cards.add(CardLibrary.getCopy(params[0], Integer.parseInt(params[1]), Integer.parseInt(params[2])));
            } else {
                SpireAnniversary6Mod.logger.warn(String.format("Tried to generate an AnyColorCardReward with input: %s", Arrays.toString(params)));
                this.cards.add(CardLibrary.getCard(Madness.ID));
            }
        }

        init();
    }

    protected void init() {
        for (AbstractCard c : this.cards) {
            for (AbstractRelic r : AbstractDungeon.player.relics)
                r.onPreviewObtainCard(c);
        }
    }

    public boolean claimReward() {
        if (AbstractDungeon.screen == AbstractDungeon.CurrentScreen.COMBAT_REWARD) {
            AbstractDungeon.cardRewardScreen.open(this.cards, this, uiStrings.TEXT[1]);
            AbstractDungeon.previousScreen = AbstractDungeon.CurrentScreen.COMBAT_REWARD;
        }
        return false;
    }
}