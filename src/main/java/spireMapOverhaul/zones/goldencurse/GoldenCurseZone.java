package spireMapOverhaul.zones.goldencurse;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rewards.RewardItem;
import spireMapOverhaul.SpireAnniversary6Mod;
import spireMapOverhaul.abstracts.AbstractZone;
import spireMapOverhaul.patches.CustomRewardTypes;
import spireMapOverhaul.rewards.SingleCardReward;
import spireMapOverhaul.util.TexLoader;
import spireMapOverhaul.zoneInterfaces.RenderableZone;
import spireMapOverhaul.zoneInterfaces.RewardModifyingZone;

import java.util.ArrayList;

import static spireMapOverhaul.patches.CustomRewardTypes.SMO_SINGLECARDREWARD;

public class GoldenCurseZone extends AbstractZone implements RewardModifyingZone, RenderableZone {
    public static final String ID = "GoldenCurse";
    private Texture bg = TexLoader.getTexture(SpireAnniversary6Mod.makeBackgroundPath("goldencurse/bg.png"));

    public static final int BASE_CARD_COST = 30;
    public static final int BASE_HEAL_COST = 10;

    private final Color color = Color.GOLD.cpy();

    public GoldenCurseZone() {
        super(ID);
        this.width = 2;
        this.maxWidth = 3;
        this.height = 2;
        this.maxHeight = 3;



    }

    public static int getRewardCost(RewardItem instance) {

        switch (instance.type) {
            case CARD:
                return BASE_CARD_COST;
            case RELIC:
                return instance.relic.getPrice() / 2;
            case POTION:
                return instance.potion.getPrice() / 2;
            case GOLD:
            case SAPPHIRE_KEY:
            case EMERALD_KEY:
                return 0;
            default:
                break;
        }

        if (instance.type == SMO_SINGLECARDREWARD && instance instanceof SingleCardReward) {
            return AbstractCard.getPrice(((SingleCardReward) instance).card.rarity) / 2;
        }

        if (instance.type == CustomRewardTypes.SMO_ANYCOLORCARDREWARD) {
            return BASE_CARD_COST;
        }

        if (instance.type == CustomRewardTypes.HEALREWARD) {
            return BASE_HEAL_COST;
        }

        return 30;
    }

    @Override
    public ArrayList<RewardItem> getAdditionalRewards() {
        ArrayList<RewardItem> rewards = new ArrayList<>();
        rewards.add(new RewardItem());
        rewards.add(new RewardItem(AbstractDungeon.returnRandomPotion()));
        if (AbstractDungeon.getCurrRoom().rewards.stream().anyMatch(r -> r.type == RewardItem.RewardType.RELIC)) {
            rewards.add(new RewardItem(AbstractDungeon.returnRandomRelic(AbstractDungeon.returnRandomRelicTier())));
        }
        return rewards;
    }

    @Override
    public void modifyRewards(ArrayList<RewardItem> rewards) {
        rewards.removeIf(r -> r.type == RewardItem.RewardType.GOLD);
        rewards.forEach(r -> {
            int goldAmt = GoldenCurseZone.getRewardCost(r);
            RewardPatches.RewardItemFields.cost.set(r, goldAmt);
        });
        AbstractDungeon.combatRewardScreen.positionRewards();
    }

    @Override
    public AbstractZone copy() {
        return new GoldenCurseZone();
    }

    @Override
    protected boolean canIncludeEarlyRows() {
        return false;
    }

    @Override
    protected boolean canIncludeFinalCampfireRow() {
        return false;
    }

    @Override
    protected boolean canIncludeTreasureRow() {
        return false;
    }

    @Override
    public boolean canSpawn() {
        return isAct(2) || isAct(3);
    }

    @Override
    public Color getColor() {
        return color;
    }

    @Override
    public void postRenderCombatBackground(SpriteBatch sb) {
        sb.draw(bg, 0, 0, Settings.WIDTH, Settings.HEIGHT);
    }

}