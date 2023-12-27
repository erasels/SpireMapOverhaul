package spireMapOverhaul.zones.invasion.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import java.util.ArrayList;

public class CustomTextChooseOneAction extends AbstractGameAction {
    private ArrayList<AbstractCard> choices;
    private String bannerText;

    public CustomTextChooseOneAction(ArrayList<AbstractCard> choices, String bannerText) {
        this.duration = Settings.ACTION_DUR_FAST;
        this.choices = choices;
        this.bannerText = bannerText;
    }

    public void update() {
        if (this.duration == Settings.ACTION_DUR_FAST) {
            AbstractDungeon.cardRewardScreen.chooseOneOpen(this.choices);
            AbstractDungeon.dynamicBanner.appearInstantly(this.bannerText);
        }
        this.tickDuration();
    }
}
