package spireMapOverhaul.zones.humidity.relics;

import basemod.abstracts.CustomReward;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.helpers.PowerTip;
import spireMapOverhaul.SpireAnniversary6Mod;
import spireMapOverhaul.patches.CustomRewardTypes;
import spireMapOverhaul.util.TexLoader;

public class AvocadoReward extends CustomReward {
    public static final String zoneID = "Humidity";
    private static final Texture ICON = TexLoader.getTexture(SpireAnniversary6Mod.makeRelicPath(zoneID + "/AvocadoRewardIcon.png"));

    public int amount;

    public AvocadoReward(int amount) {
        super(ICON, "Avocado", CustomRewardTypes.SMO_AVOCADOREWARD);
        this.amount = amount;
        this.relic = new Avocado();
    }

    @Override
    public boolean claimReward() {
        Avocado relic = new Avocado();
        relic.hpLost = amount;
        relic.description = relic.getUpdatedDescription();
        relic.tips.clear();
        relic.tips.add(new PowerTip(relic.name, relic.description));
        relic.instantObtain();
        return true;
    }

    @Override
    public void render(SpriteBatch sb) {
        super.render(sb);
        if (this.hb.hovered) {
            ((Avocado) this.relic).hpLost = amount;
            this.relic.description = this.relic.getUpdatedDescription();
            relic.tips.clear();
            relic.tips.add(new PowerTip(relic.name, relic.description));
            this.relic.renderTip(sb);
        }
    }
}
