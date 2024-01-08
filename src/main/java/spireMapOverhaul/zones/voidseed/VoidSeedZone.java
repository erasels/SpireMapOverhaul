package spireMapOverhaul.zones.voidseed;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import spireMapOverhaul.BetterMapGenerator;
import spireMapOverhaul.abstracts.AbstractZone;
import spireMapOverhaul.zoneInterfaces.CombatModifyingZone;
import spireMapOverhaul.zoneInterfaces.ModifiedEventRateZone;
import spireMapOverhaul.zoneInterfaces.RenderableZone;
import spireMapOverhaul.zones.voidseed.powers.VoidFogPower;
import spireMapOverhaul.zones.voidseed.powers.VoidtouchedPower;

import static spireMapOverhaul.util.Wiz.*;

public class VoidSeedZone extends AbstractZone implements ModifiedEventRateZone, RenderableZone, CombatModifyingZone {
    public static final String ID = "VoidSeed";
    private final int width, height;
    private final Color color;


    public VoidSeedZone() {
        this("Placeholder 0", 2, 4);
        System.out.println("Placeholder Zone " + name + " " + width + "x" + height);
    }

    @Override
    public AbstractZone copy() {
        return new VoidSeedZone(name, width, height);
    }

    private VoidSeedZone(String name, int width, int height) {
        super(ID);

        this.width = width;
        this.height = height;

        color = new Color(Color.PURPLE.cpy());
        this.name = name;
    }

    @Override
    protected boolean allowAdditionalPaths() {
        return false;
    }

    @Override
    public boolean generateMapArea(BetterMapGenerator.MapPlanner planner) {
        return generateNormalArea(planner, width, height);
    }

    @Override
    public Color getColor() { //I considered changing this to a variable, but a method lets you do funky stuff like a rainbow zone that changes colors or something.
        return color;
    }

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

    @Override
    public void renderBackground(SpriteBatch sb) {
        VoidSeedShaderManager.StartFbo(sb);

    }

    @Override
    public void postRenderBackground(SpriteBatch sb) {
        VoidSeedShaderManager.StopFbo(sb, 0.0f, 1f);
    }

    @Override
    public void update() {
        VoidSeedShaderManager.shaderTimer += Gdx.graphics.getDeltaTime();
    }
}
