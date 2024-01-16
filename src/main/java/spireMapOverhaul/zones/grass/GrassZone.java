package spireMapOverhaul.zones.grass;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import spireMapOverhaul.SpireAnniversary6Mod;
import spireMapOverhaul.abstracts.AbstractZone;
import spireMapOverhaul.util.Wiz;
import spireMapOverhaul.zoneInterfaces.CombatModifyingZone;
import spireMapOverhaul.zoneInterfaces.EncounterModifyingZone;
import spireMapOverhaul.zoneInterfaces.RenderableZone;
import spireMapOverhaul.zones.grass.vegetables.AbstractVegetable;
import spireMapOverhaul.zones.grass.vegetables.AbstractVegetableData;
import spireMapOverhaul.zones.grass.vegetables.Radish;
import spireMapOverhaul.zones.mirror.powers.MirrorZonePower;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentLinkedQueue;

public class GrassZone extends AbstractZone implements CombatModifyingZone, RenderableZone {
    public static final int SPAWN_VEGS = 3;
    public static final String ID = "Grass";
    private final ArrayList<AbstractVegetableData> ALL = new ArrayList<>();
    private final ArrayList<AbstractVegetable> vegetables = new ArrayList<>();

    public GrassZone() {
        super(ID, Icons.MONSTER);
        this.width = 2;
        this.maxWidth = 4;
        this.height = 3;
        this.maxHeight = 4;
    }

    @Override
    public AbstractZone copy() {
        return new GrassZone();
    }

    @Override
    public Color getColor() {
        return Color.FOREST.cpy();
    }

    @Override
    public void atBattleStart() {
        initializeVegetables();
        for (int i = 0; i < AbstractDungeon.cardRandomRng.random(1, 3); i++) {
            spawn(random().create());
        }
    }

    public void atTurnEnd() {
        if (EnergyPanel.totalCount > 0 && !vegetables.isEmpty()) {
            for (int i = 0; i < EnergyPanel.totalCount; i++) {
                AbstractVegetable veg = getRandom();
                if (veg != null) {
                    veg.upgrade(1);
                }
            }
            AbstractDungeon.player.energy.use(EnergyPanel.totalCount);
        }
    }

    @Override
    public String getCombatText() {
        return TEXT[2];
    }

    public AbstractVegetable getRandom() {
        return Wiz.getRandomItem(vegetables);
    }

    protected void initializeVegetables() {
        ALL.clear();
        ALL.add(Radish.DATA);
    }

    public void postRenderBackground(SpriteBatch sb) {
        for (AbstractVegetable veg : vegetables) {
            veg.render(sb);
        }
    }

    public AbstractVegetableData random() {
        return Wiz.getRandomItem(ALL);
    }

    public void spawn(AbstractVegetable vegetable) {
        vegetables.add(vegetable);
        vegetable.onSpawn();
    }

    public void update() {
        for (AbstractVegetable veg : vegetables) {
            veg.update();
        }
        vegetables.removeIf(AbstractVegetable::isPulled);
    }
}
