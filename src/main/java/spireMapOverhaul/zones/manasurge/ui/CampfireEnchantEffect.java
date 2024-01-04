package spireMapOverhaul.zones.manasurge.ui;

import basemod.abstracts.AbstractCardModifier;
import basemod.helpers.CardModifierManager;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Interpolation;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.rooms.CampfireUI;
import com.megacrit.cardcrawl.rooms.RestRoom;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import com.megacrit.cardcrawl.vfx.UpgradeShineEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardBrieflyEffect;
import spireMapOverhaul.zones.manasurge.ManaSurgeZone;
import spireMapOverhaul.zones.manasurge.modifiers.common.positive.CripplingModifier;
import spireMapOverhaul.zones.manasurge.modifiers.common.positive.ExposingModifier;
import spireMapOverhaul.zones.manasurge.modifiers.common.positive.SharpModifier;
import spireMapOverhaul.zones.manasurge.modifiers.common.positive.ToughModifier;
import spireMapOverhaul.zones.manasurge.modifiers.uncommon.positive.PowerfulModifier;
import spireMapOverhaul.zones.manasurge.modifiers.uncommon.positive.ProtectiveModifier;
import spireMapOverhaul.zones.manasurge.utils.ManaSurgeTags;

import java.util.Iterator;

public class CampfireEnchantEffect extends AbstractGameEffect {
    private static final float DUR = 1.5F;
    private boolean openedScreen = false;
    private Color screenColor;

    public CampfireEnchantEffect() {
        this.screenColor = AbstractDungeon.fadeColor.cpy();
        this.duration = DUR;
        this.screenColor.a = 0.0F;
        AbstractDungeon.overlayMenu.proceedButton.hide();
    }

    public void update() {
        if (!AbstractDungeon.isScreenUp) {
            this.duration -= Gdx.graphics.getDeltaTime();
            this.updateBlackScreenColor();
        }

        Iterator var1;
        if (!AbstractDungeon.isScreenUp && !AbstractDungeon.gridSelectScreen.selectedCards.isEmpty()) {
            var1 = AbstractDungeon.gridSelectScreen.selectedCards.iterator();

            while (var1.hasNext()) {
                AbstractCard c = (AbstractCard) var1.next();
                AbstractDungeon.effectsQueue.add(new UpgradeShineEffect((float) Settings.WIDTH / 2.0F, (float) Settings.HEIGHT / 2.0F));
                c.tags.add(ManaSurgeTags.PERMANENT_MODIFIER);
                if (Math.random() < ManaSurgeZone.COMMON_CHANCE) {
                    int numberOfCommonModifiers = 4;
                    int selectedModifierIndex = (int) (Math.random() * numberOfCommonModifiers);
                    AbstractCardModifier modifier;
                    switch (selectedModifierIndex) {
                        case 0:
                            modifier = new SharpModifier();
                            break;
                        case 1:
                            modifier = new ToughModifier();
                            break;
                        case 2:
                            modifier = new ExposingModifier();
                            break;
                        case 3:
                            modifier = new CripplingModifier();
                            break;
                        default:
                            modifier = null;
                            break;
                    }
                    if (modifier != null) {
                        CardModifierManager.addModifier(c, modifier);
                    }
                } else {
                    int numberOfUncommonModifiers = 2;
                    int selectedModifierIndex = (int) (Math.random() * numberOfUncommonModifiers);
                    AbstractCardModifier modifier;
                    switch (selectedModifierIndex) {
                        case 0:
                            modifier = new PowerfulModifier();
                            break;
                        case 1:
                            modifier = new ProtectiveModifier();
                            break;
                        default:
                            modifier = null;
                            break;
                    }
                    if (modifier != null) {
                        CardModifierManager.addModifier(c, modifier);
                    }
                }
                AbstractDungeon.player.bottledCardUpgradeCheck(c);
                AbstractDungeon.effectsQueue.add(new ShowCardBrieflyEffect(c.makeStatEquivalentCopy()));
            }

            AbstractDungeon.gridSelectScreen.selectedCards.clear();
            ((RestRoom) AbstractDungeon.getCurrRoom()).fadeIn();
        }

        if (this.duration < 1.0F && !this.openedScreen) {
            this.openedScreen = true;
            CardGroup selectedCards = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
            AbstractDungeon.player.masterDeck.group.stream()
                    .filter(card -> !card.hasTag(ManaSurgeTags.PERMANENT_MODIFIER) &&
                            card.cost != -2 &&
                            card.type != AbstractCard.CardType.CURSE &&
                            card.type != AbstractCard.CardType.STATUS)
                    .forEach(selectedCards::addToTop);

            AbstractDungeon.gridSelectScreen.open(
                    selectedCards,
                    1,
                    "Select a card to enchant.",
                    false,
                    false,
                    true,
                    true
            );
        }

        if (this.duration < 0.0F) {
            this.isDone = true;
            if (CampfireUI.hidden) {
                AbstractRoom.waitTimer = 0.0F;
                AbstractDungeon.getCurrRoom().phase = AbstractRoom.RoomPhase.COMPLETE;
                ((RestRoom) AbstractDungeon.getCurrRoom()).cutFireSound();
            }
        }

    }

    private void updateBlackScreenColor() {
        if (this.duration > 1.0F) {
            this.screenColor.a = Interpolation.fade.apply(1.0F, 0.0F, (this.duration - 1.0F) * 2.0F);
        } else {
            this.screenColor.a = Interpolation.fade.apply(0.0F, 1.0F, this.duration / 1.5F);
        }

    }

    public void render(SpriteBatch sb) {
        sb.setColor(this.screenColor);
        sb.draw(ImageMaster.WHITE_SQUARE_IMG, 0.0F, 0.0F, (float)Settings.WIDTH, (float)Settings.HEIGHT);
        if (AbstractDungeon.screen == AbstractDungeon.CurrentScreen.GRID) {
            AbstractDungeon.gridSelectScreen.render(sb);
        }

    }

    public void dispose() {
    }
}