package spireMapOverhaul.zones.characterinfluence;

import basemod.BaseMod;
import basemod.ReflectionHacks;
import basemod.abstracts.events.PhasedEvent;
import basemod.abstracts.events.phases.CombatPhase;
import basemod.abstracts.events.phases.TextPhase;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.LoseHPAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.GenericEventDialog;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.MonsterHelper;
import com.megacrit.cardcrawl.helpers.RelicLibrary;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.rewards.RewardItem;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.vfx.UpgradeShineEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardBrieflyEffect;
import spireMapOverhaul.BetterMapGenerator;
import spireMapOverhaul.SpireAnniversary6Mod;
import spireMapOverhaul.abstracts.AbstractZone;
import spireMapOverhaul.patches.ZonePatches;

import java.util.ArrayList;

import static spireMapOverhaul.SpireAnniversary6Mod.makeID;

public class CharacterInfluenceEvent extends PhasedEvent {
    public static final String ID = makeID("CharacterVisit");
    private static final EventStrings eventStrings = CardCrawlGame.languagePack.getEventString(ID);
    private static final String[] DESCRIPTIONS = eventStrings.DESCRIPTIONS;
    private static final String[] OPTIONS = eventStrings.OPTIONS;
    private static final String title = eventStrings.NAME;

    //TODO: Check if the event spawns forever

    public CharacterInfluenceEvent() {
        super(ID, title, "images/events/ShadowyFigure.png"); //TODO: Set a default image.

        setImage(); //If it can find a mod image, it uses that instead.

        /*
        if (ZonePatches.currentZone() instanceof CharacterInfluenceZone && ZonePatches.currentZone() != null && ((CharacterInfluenceZone) ZonePatches.currentZone()).classInfluence != null) {
            this.imageEventText.loadImage("images/ui/charSelect/" + ((CharacterInfluenceZone) ZonePatches.currentZone()).classInfluence.getPortraitImageName());
        }

         */

        registerPhase("EventStart", new TextPhase(DESCRIPTIONS[0] + "a Stranger" + DESCRIPTIONS[1]) {
            @Override
            public String getBody() {
                if (ZonePatches.currentZone() instanceof CharacterInfluenceZone && ZonePatches.currentZone() != null && ((CharacterInfluenceZone) ZonePatches.currentZone()).classInfluence != null) {
                    return DESCRIPTIONS[0] + ((CharacterInfluenceZone) ZonePatches.currentZone()).classInfluence.title + DESCRIPTIONS[1];
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
            if (ZonePatches.currentZone() instanceof CharacterInfluenceZone && ZonePatches.currentZone() != null && ((CharacterInfluenceZone) ZonePatches.currentZone()).classInfluence != null) {
                relicStrings = ((CharacterInfluenceZone) ZonePatches.currentZone()).classInfluence.getStartingRelics();
            }

            for (String relicID : relicStrings) {
                room.rewards.add(new RewardItem(RelicLibrary.getRelic(relicID).makeCopy()));
            }

        }));

        transitionKey("EventStart"); //starting point
    }

    public void setImage() {

        Texture portraitTexture = null;

        if (ZonePatches.currentZone() instanceof CharacterInfluenceZone && ZonePatches.currentZone() != null && ((CharacterInfluenceZone) ZonePatches.currentZone()).classInfluence != null) {
            if (Gdx.files.internal("images/ui/charSelect/" + ((CharacterInfluenceZone) ZonePatches.currentZone()).classInfluence.getPortraitImageName()).exists()) {
                portraitTexture = ImageMaster.loadImage("images/ui/charSelect/" + ((CharacterInfluenceZone) ZonePatches.currentZone()).classInfluence.getPortraitImageName());
            } else if (Gdx.files.internal(BaseMod.playerPortraitMap.get(((CharacterInfluenceZone) ZonePatches.currentZone()).classInfluence.chosenClass)).exists()) {
                portraitTexture = ImageMaster.loadImage((BaseMod.playerPortraitMap.get(((CharacterInfluenceZone) ZonePatches.currentZone()).classInfluence.chosenClass)));
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
