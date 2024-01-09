package spireMapOverhaul.zones.voidseed.campfire;

import basemod.helpers.CardModifierManager;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Interpolation;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.rooms.CampfireUI;
import com.megacrit.cardcrawl.rooms.RestRoom;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardBrieflyEffect;
import spireMapOverhaul.zones.voidseed.cardmods.CorruptedModifier;

import java.util.Iterator;

import static spireMapOverhaul.SpireAnniversary6Mod.makeID;

public class CampfireCorruptEffect extends AbstractGameEffect {
    private static final UIStrings uiStrings;
    public static final String[] TEXT;
    private static final float DUR = 1.5F;
    private boolean openedScreen = false;
    private Color screenColor;

    public CampfireCorruptEffect() {
        this.screenColor = AbstractDungeon.fadeColor.cpy();// 32
        this.duration = 1.5F;// 38
        this.screenColor.a = 0.0F;// 39
        AbstractDungeon.overlayMenu.proceedButton.hide();// 40
    }// 41

    public void update() {
        if (!AbstractDungeon.isScreenUp) {// 45
            this.duration -= Gdx.graphics.getDeltaTime();// 46
            this.updateBlackScreenColor();// 47
        }

        Iterator var1;
        if (!AbstractDungeon.isScreenUp && !AbstractDungeon.gridSelectScreen.selectedCards.isEmpty() && AbstractDungeon.gridSelectScreen.forUpgrade) {// 51
            var1 = AbstractDungeon.gridSelectScreen.selectedCards.iterator();// 53

            while (var1.hasNext()) {
                AbstractCard c = (AbstractCard) var1.next();
                CardModifierManager.addModifier(c, new CorruptedModifier());// 55
                AbstractDungeon.effectsQueue.add(new ShowCardBrieflyEffect(c.makeStatEquivalentCopy()));// 59
            }

            AbstractDungeon.gridSelectScreen.selectedCards.clear();// 61
            ((RestRoom) AbstractDungeon.getCurrRoom()).fadeIn();// 62
        }

        if (this.duration < 1.0F && !this.openedScreen) {// 66
            this.openedScreen = true;// 67
            CardGroup group = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);// 68
            group.group.addAll(AbstractDungeon.player.masterDeck.group);// 69
            group.group.removeIf((c) -> !CorruptedModifier.valid(c) || CardModifierManager.hasModifier(c, CorruptedModifier.ID));
            AbstractDungeon.gridSelectScreen.open(group, 1, TEXT[0], true, false, true, false);// 69 70
            CorruptScreenPatch.active = true;// 71
        }

        if (this.duration < 0.0F) {// 84
            this.isDone = true;// 85
            if (CampfireUI.hidden) {// 86
                AbstractRoom.waitTimer = 0.0F;// 87
                AbstractDungeon.getCurrRoom().phase = AbstractRoom.RoomPhase.COMPLETE;// 88
                ((RestRoom) AbstractDungeon.getCurrRoom()).cutFireSound();// 89
            }
        }


    }// 92

    private void updateBlackScreenColor() {
        if (this.duration > 1.0F) {// 98
            this.screenColor.a = Interpolation.fade.apply(1.0F, 0.0F, (this.duration - 1.0F) * 2.0F);// 99
        } else {
            this.screenColor.a = Interpolation.fade.apply(0.0F, 1.0F, this.duration / 1.5F);// 101
        }

    }// 103

    public void render(SpriteBatch sb) {
        sb.setColor(this.screenColor);// 107
        sb.draw(ImageMaster.WHITE_SQUARE_IMG, 0.0F, 0.0F, (float) Settings.WIDTH, (float) Settings.HEIGHT);// 108
        if (AbstractDungeon.screen == AbstractDungeon.CurrentScreen.GRID) {// 110
            AbstractDungeon.gridSelectScreen.render(sb);// 111
        }

    }// 113

    public void dispose() {
    }// 118

    static {
        uiStrings = CardCrawlGame.languagePack.getUIString(makeID("CampfireCorruptEffect"));// 27
        TEXT = uiStrings.TEXT;// 28
    }
}