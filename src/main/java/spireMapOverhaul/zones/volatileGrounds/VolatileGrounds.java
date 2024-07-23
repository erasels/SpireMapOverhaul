package spireMapOverhaul.zones.volatileGrounds;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.MonsterGroup;
import com.megacrit.cardcrawl.monsters.beyond.Exploder;
import spireMapOverhaul.SpireAnniversary6Mod;
import spireMapOverhaul.abstracts.AbstractZone;
import spireMapOverhaul.util.TexLoader;
import spireMapOverhaul.zoneInterfaces.EncounterModifyingZone;
import spireMapOverhaul.zoneInterfaces.RenderableZone;
import spireMapOverhaul.zones.volatileGrounds.monsters.*;

import java.util.Arrays;
import java.util.List;

public class VolatileGrounds extends AbstractZone implements EncounterModifyingZone, RenderableZone {
    private static final String SUNSTONE_ELITE = SpireAnniversary6Mod.makeID("SUNSTONE_ELITE");
    private static final String EXPLOSIVE_SHAPES = SpireAnniversary6Mod.makeID("EXPLOSIVE_SHAPES");
    private static final String HEAT_BLISTER = SpireAnniversary6Mod.makeID("HEAT_BLISTER");
    private static final String GREMLIN_ARCHMAGE = SpireAnniversary6Mod.makeID("GREMLIN_ARCHMAGE");
    public static final String ID = "VolatileGrounds";
    private Texture bg = TexLoader.getTexture(SpireAnniversary6Mod.makeBackgroundPath("volatileGrounds/bg.png"));
    
    public VolatileGrounds() {
        super(ID, Icons.MONSTER);
        this.width = 1;
        this.height = 4;
    }
    
    @Override
    public AbstractZone copy() {
        return new VolatileGrounds();
    }
    
    @Override
    public Color getColor() {
        return Color.ORANGE.cpy();
    }
    
    @Override
    public boolean canSpawn() {
        return isAct(3);
    }
    
    @Override
    public List<ZoneEncounter> getNormalEncounters() {
        return Arrays.asList(
                new ZoneEncounter(EXPLOSIVE_SHAPES, 3, () -> new MonsterGroup(
                        new AbstractMonster[]{
                                new Eruptor(-400.0F, 0.0F),
                                new Exploder(-200.0F, 0.0F),
                                new Eruptor(0.0F, 0.0F),
                                new Exploder(200.0F, 0.0F)
                        })),
                new ZoneEncounter(HEAT_BLISTER, 3, () -> new MonsterGroup(
                        new AbstractMonster[]{
                                new Eruptor(-320.0F, 0.0F),
                                new HeatBlister(0.0F, 0.0F)
                        })),
                new ZoneEncounter(GREMLIN_ARCHMAGE, 3, () -> new MonsterGroup(
                        new AbstractMonster[]{
                                new Exploder(-430.0F, 0.0F),
                                new Exploder(-200.0F, 0.0F),
                                new GremlinArchmage(0.0F, 0.0F)
                        }))
        );
    }
    
    @Override
    public List<ZoneEncounter> getEliteEncounters() {
        return Arrays.asList(
                new ZoneEncounter(SUNSTONE_ELITE, 3, () -> new MonsterGroup(
                        new AbstractMonster[]{
                                new SunStoneShard(-400.0F, 0.0f),
                                new UnstableSunstone(-100.0F, 0.0F),
                                new SunStoneShard(200.0F, 0.0f),
                        }))
        );
    }
    
    protected boolean canIncludeEarlyRows() {
        return false;
    }

    private static final Color saturationCol = new Color(1f, 0.64f, 0f, 0.1f);
    @Override
    public void postRenderCombatBackground(SpriteBatch sb) {
        sb.setColor(saturationCol);
        sb.draw(ImageMaster.WHITE_SQUARE_IMG, 0, 0, Settings.WIDTH, Settings.HEIGHT);
        sb.setColor(Color.WHITE);
        sb.draw(bg, 0, 0, Settings.WIDTH, Settings.HEIGHT);
    }
}
