package spireMapOverhaul.zones.voidseed;

import basemod.helpers.CardModifierManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.screens.DungeonMapScreen;
import com.megacrit.cardcrawl.ui.campfire.AbstractCampfireOption;
import com.megacrit.cardcrawl.ui.campfire.RestOption;
import spireMapOverhaul.BetterMapGenerator;
import spireMapOverhaul.abstracts.AbstractZone;
import spireMapOverhaul.zoneInterfaces.*;
import spireMapOverhaul.zones.voidseed.campfire.CorruptOption;
import spireMapOverhaul.zones.voidseed.campfire.CorruptScreenPatch;
import spireMapOverhaul.zones.voidseed.cardmods.CorruptedModifier;
import spireMapOverhaul.zones.voidseed.powers.VoidFogPower;
import spireMapOverhaul.zones.voidseed.powers.VoidtouchedPower;

import java.util.ArrayList;

import static spireMapOverhaul.util.Wiz.*;

public class VoidSeedZone extends AbstractZone implements OnTravelZone, ModifiedEventRateZone, RenderableZone, CombatModifyingZone, RewardModifyingZone, CampfireModifyingZone {
    public static final String ID = "VoidSeed";
    private static final float OFFSET_X = Settings.isMobile ? 496.0F * Settings.xScale : 560.0F * Settings.xScale;
    private static final float OFFSET_Y = 180.0F * Settings.scale;
    private static final float SPACING_X = Settings.isMobile ? (int) (Settings.xScale * 64.0F) * 2.2F : (int) (Settings.xScale * 64.0F) * 2.0F;

    private final int width, height;
    private final Color color;


    public VoidSeedZone() {
        this("Placeholder 0");
        System.out.println("Placeholder Zone " + name + " " + width + "x" + height);
    }

    @Override
    public AbstractZone copy() {
        return new VoidSeedZone(name);
    }

    private VoidSeedZone(String name) {
        super(ID, Icons.MONSTER, Icons.REST);

        this.width = 3;
        this.maxWidth = 3;
        this.height = 3;
        this.maxHeight = 3;

        color = new Color(Color.WHITE.cpy());
        this.name = name;
    }



    @Override
    public boolean generateMapArea(BetterMapGenerator.MapPlanner planner) {
        return generateNormalArea(planner, width, height);
    }

    @Override
    public Color getColor() { //I considered changing this to a variable, but a method lets you do funky stuff like a rainbow zone that changes colors or something.
        return color;
    }


    //Combat stuff
    @Override
    public void atBattleStart() {
        forAllMonstersLiving(m -> {
            applyToEnemy(m, new VoidtouchedPower(m, 1));
        });
        addToBot(new ApplyPowerAction(adp(), adp(), new VoidFogPower(adp(), 2), 2));
    }

    protected void addToBot(AbstractGameAction action) {
        AbstractDungeon.actionManager.addToBottom(action);// 119
    }// 120

    //Render stuff
    @Override
    public void renderBackground(SpriteBatch sb) {
        VoidSeedShaderManager.StartFbo(sb);
    }

    @Override
    public void postRenderBackground(SpriteBatch sb) {
        VoidSeedShaderManager.StopFbo(sb, 0.0f, 1f, true);
    }

    @Override
    public void renderOnMap(SpriteBatch sb, float alpha) {
        VoidSeedShaderManager.StartFbo(sb);
        super.renderOnMap(sb, alpha);
        VoidSeedShaderManager.StopFbo(sb, 1.0f, 1.0f, false, new Color(0.2f, 0.1f, 0.2f, 1f));
        if (alpha > 0) {
            FontHelper.renderFontCentered(sb, FontHelper.menuBannerFont, name,
                    labelX * SPACING_X + OFFSET_X, labelY * Settings.MAP_DST_Y + OFFSET_Y + DungeonMapScreen.offsetY,
                    Color.WHITE.cpy(), 0.8f
            );
        }
    }


    //Reward stuff
    @Override
    public void modifyRewardCards(ArrayList<AbstractCard> cards) {
        cards.forEach(c -> {
            if (!CardModifierManager.hasModifier(c, CorruptedModifier.ID) && AbstractDungeon.cardRng.randomBoolean(0.75f) && CorruptedModifier.valid(c)) {
                CardModifierManager.addModifier(c, new CorruptedModifier());
            }
        });

    }

    // travel stuff

    @Override
    public void onEnterRoom() {
        CorruptScreenPatch.active = false;
    }

    @Override
    public void onExit() {
        CorruptScreenPatch.active = false;
    }

    //Campfire stuff

    @Override
    public void postAddButtons(ArrayList<AbstractCampfireOption> buttons) {

        buttons.replaceAll(o -> {
            if (o instanceof RestOption) {
                return new CorruptOption(true);
            }
            return o;
        });

    }

    @Override
    public void postAddCampfireMessages(ArrayList<String> messages) {
        ArrayList<String> newMessages = new ArrayList<>();
        for (String s : messages) {
            //replace 3 random characters with a ?
            for (int i = 0; i < 3; i++) {
                int j = MathUtils.random(s.length() - 1);
                s = s.substring(0, j) + "?" + s.substring(j + 1);
            }
            newMessages.add(s);

        }
        messages.clear();
        messages.addAll(newMessages);
    }
}
