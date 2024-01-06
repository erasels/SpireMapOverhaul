package spireMapOverhaul.zones.characterinfluence;

import basemod.BaseMod;
import basemod.ReflectionHacks;
import basemod.abstracts.events.PhasedEvent;
import basemod.abstracts.events.phases.CombatPhase;
import basemod.abstracts.events.phases.TextPhase;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.LoseHPAction;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.GenericEventDialog;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.RelicLibrary;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.rewards.RewardItem;

import java.util.ArrayList;

import static spireMapOverhaul.SpireAnniversary6Mod.makeID;
import static spireMapOverhaul.zones.characterinfluence.CharacterInfluenceZone.*;

@SuppressWarnings("unused")
public class CharacterInfluenceEvent extends PhasedEvent {
    public static final String ID = makeID("CharacterVisit");
    private static final EventStrings eventStrings = CardCrawlGame.languagePack.getEventString(ID);
    private static final String[] DESCRIPTIONS = eventStrings.DESCRIPTIONS;
    private static final String[] OPTIONS = eventStrings.OPTIONS;
    private static final String title = eventStrings.NAME;

    public CharacterInfluenceEvent() {
        super(ID, title, "images/events/ShadowyFigure.png");

        setImage(); // If it can find a mod image, it uses that instead.

        registerPhase("EventStart", new TextPhase(DESCRIPTIONS[0] + DESCRIPTIONS[2] + DESCRIPTIONS[1]) {
            @Override
            public String getBody() {
                if (getCurrentZoneCharacter() != null) {
                    return DESCRIPTIONS[0] + getCurrentZoneCharacter().title + DESCRIPTIONS[1];
                } else {
                    return super.getBody();
                }
            }
        }.addOption(OPTIONS[0], (i)->transitionKey("Combat")).addOption(OPTIONS[1], (i)->this.openMap()));

        registerPhase("Combat", new CombatPhase(AbstractDungeon.eliteMonsterList.remove(0)) {
            boolean didReduceHealth = false;
            @Override
            public void update() {
                super.update();
                if (didReduceHealth) return;
                for (AbstractMonster m : AbstractDungeon.getMonsters().monsters) {
                    AbstractDungeon.actionManager.addToTop(new LoseHPAction(m, AbstractDungeon.player, m.currentHealth/4));
                }
                didReduceHealth = false;
            }
        }.setType(AbstractMonster.EnemyType.ELITE).addRewards(true, (room)->{

            //Adds all starting relics to the reward screen
            ArrayList<String> relicStrings = new ArrayList<>();
            if (getCurrentZoneCharacter() != null) {
                relicStrings = getCurrentZoneCharacter().getStartingRelics();
            }

            for (String relicID : relicStrings) {
                room.rewards.add(new RewardItem(RelicLibrary.getRelic(relicID).makeCopy()));
            }

        }));

        transitionKey("EventStart"); //starting point
    }

    // Sets the Event image to a cropped version of the character portrait.
    public void setImage() {

        Texture portraitTexture = null;

        // Finds the portrait
        if (getCurrentZoneCharacter() != null) {
            if (Gdx.files.internal("images/ui/charSelect/" + getCurrentZoneCharacter().getPortraitImageName()).exists()) {
                portraitTexture = ImageMaster.loadImage("images/ui/charSelect/" + getCurrentZoneCharacter().getPortraitImageName());
            } else if (Gdx.files.internal(BaseMod.playerPortraitMap.get(getCurrentZoneCharacter().chosenClass)).exists()) {
                portraitTexture = ImageMaster.loadImage((BaseMod.playerPortraitMap.get(getCurrentZoneCharacter().chosenClass)));
            }
        }

        if (portraitTexture == null) return;

        if (!portraitTexture.getTextureData().isPrepared())
            portraitTexture.getTextureData().prepare();

        Pixmap pixmap = new Pixmap(
                600,
                600,
                portraitTexture.getTextureData().getFormat()
        );

        pixmap.drawPixmap(
                portraitTexture.getTextureData().consumePixmap(),
                720,
                0,
                1200,
                portraitTexture.getHeight(),
                0,
                0,
                600,
                600
        );

        Texture newTexture = new Texture(pixmap);

        // Repeats what this.imageEventText.loadImage(imgUrl) does, with ReflectionHacks.

        if (ReflectionHacks.getPrivate(this.imageEventText, GenericEventDialog.class, "img") != null) {
            ((Texture)ReflectionHacks.getPrivate(this.imageEventText, GenericEventDialog.class, "img")).dispose();
            ReflectionHacks.setPrivate(this.imageEventText, GenericEventDialog.class, "img", null);
        }

        ReflectionHacks.setPrivate(this.imageEventText, GenericEventDialog.class, "img", newTexture);

        ReflectionHacks.setPrivateStatic(GenericEventDialog.class, "DIALOG_MSG_X", ReflectionHacks.getPrivateStatic(GenericEventDialog.class, "DIALOG_MSG_X_IMAGE"));
        ReflectionHacks.setPrivateStatic(GenericEventDialog.class, "DIALOG_MSG_W", ReflectionHacks.getPrivateStatic(GenericEventDialog.class, "DIALOG_MSG_W_IMAGE"));

        pixmap.dispose();

    }
}
