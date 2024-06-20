package spireMapOverhaul.zones.gravewoodGrove;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.ui.campfire.AbstractCampfireOption;
import spireMapOverhaul.SpireAnniversary6Mod;
import spireMapOverhaul.abstracts.AbstractZone;
import spireMapOverhaul.util.TexLoader;
import spireMapOverhaul.zoneInterfaces.CampfireModifyingZone;
import spireMapOverhaul.zoneInterfaces.CombatModifyingZone;
import spireMapOverhaul.zoneInterfaces.RenderableZone;
import spireMapOverhaul.zones.gravewoodGrove.powers.DeadBranchPower;
import spireMapOverhaul.zones.invasion.powers.FixedTextDrawPower;

import java.util.ArrayList;

import static spireMapOverhaul.util.Wiz.adp;
import static spireMapOverhaul.util.Wiz.atb;

public class GravewoodGroveZone extends AbstractZone implements CombatModifyingZone, CampfireModifyingZone, RenderableZone {
    public static final String ID = "GravewoodGrove";
    private Texture bg = TexLoader.getTexture(SpireAnniversary6Mod.makeBackgroundPath("gravewoodGrove/bg.png"));


    public GravewoodGroveZone() {
        this(2, 4);
    }

    private GravewoodGroveZone(int width, int height) {
        super(ID, Icons.MONSTER, Icons.REST);

        this.width = width;
        this.height = height;
    }

    @Override
    public void atPreBattle() {
        atb(new ApplyPowerAction(adp(), null, new FixedTextDrawPower(adp(), -1)));
        atb(new ApplyPowerAction(adp(), null, new DeadBranchPower(adp())));
    }

    @Override
    public void postAddButtons(ArrayList<AbstractCampfireOption> buttons) {
        buttons.add(new TransformOption());
    }

    @Override
    public AbstractZone copy() {
        return new GravewoodGroveZone();
    }

    @Override
    public Color getColor() {
        return new Color(0.25f, 0.3f, 0.2f, 1f);
    }

    @Override
    public void postRenderCombatBackground(SpriteBatch sb) {
        sb.draw(bg, 0, 0, Settings.WIDTH, Settings.HEIGHT);
    }
}