package spireMapOverhaul.zones.Junkyard;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.events.exordium.ScrapOoze;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.MonsterGroup;
import com.megacrit.cardcrawl.monsters.exordium.AcidSlime_S;
import com.megacrit.cardcrawl.monsters.exordium.FungiBeast;
import com.megacrit.cardcrawl.monsters.exordium.SpikeSlime_S;
import com.megacrit.cardcrawl.rewards.RewardItem;
import spireMapOverhaul.SpireAnniversary6Mod;
import spireMapOverhaul.abstracts.AbstractZone;
import spireMapOverhaul.util.TexLoader;
import spireMapOverhaul.zoneInterfaces.EncounterModifyingZone;
import spireMapOverhaul.zoneInterfaces.ModifiedEventRateZone;
import spireMapOverhaul.zoneInterfaces.RenderableZone;
import spireMapOverhaul.zoneInterfaces.RewardModifyingZone;
import spireMapOverhaul.zones.Junkyard.monsters.Junkbot;
import spireMapOverhaul.zones.Junkyard.monsters.Peddler;

import java.util.*;

public class Junkyard extends AbstractZone implements RewardModifyingZone, EncounterModifyingZone, ModifiedEventRateZone, RenderableZone {
    public static final String ID = "Junkyard";
    private Texture bg = TexLoader.getTexture(SpireAnniversary6Mod.makeBackgroundPath("Junkyard/bg.png"));
    private static final String JUNKBOTS = SpireAnniversary6Mod.makeID("Junkbots");
    private static final String CULTIST_JUNKBOT = SpireAnniversary6Mod.makeID("Cultist_Junkbot");
    private static final String MUGGER_JUNKBOT = SpireAnniversary6Mod.makeID("Mugger_Junkbot");
    private static final String PEDDLER = SpireAnniversary6Mod.makeID("Peddler");

    public Junkyard() {
        super(ID, Icons.MONSTER, Icons.EVENT);
        this.width = 3;
        this.height = 3;
    }

    @Override
    public AbstractZone copy() {
        return new Junkyard();
    }

    @Override
    public Color getColor() {
        return Color.LIGHT_GRAY.cpy();
    }

    @Override
    public boolean cannotSkipCardRewards() {
        return true;
    }

    @Override
    public ArrayList<RewardItem> getAdditionalRewards() {
        ArrayList<RewardItem> newRewards = new ArrayList<>();
        newRewards.add(new RewardItem());
        return newRewards;
    }

    @Override
    public List<EncounterModifyingZone.ZoneEncounter> getNormalEncounters() {
        return Arrays.asList(
                new EncounterModifyingZone.ZoneEncounter(JUNKBOTS, 1, () -> new MonsterGroup(
                        new AbstractMonster[] {
                                new Junkbot(-200.0F, 0.0F),
                                new Junkbot(100.0F, 0.0F),
                        })),
                new EncounterModifyingZone.ZoneEncounter(CULTIST_JUNKBOT, 1, () -> new MonsterGroup(
                        new AbstractMonster[] {
                                new FungiBeast(-300.0F, 0.0F),
                                new Junkbot(0.0F, 0.0F),
                        })),
                new EncounterModifyingZone.ZoneEncounter(MUGGER_JUNKBOT, 1, () -> new MonsterGroup(
                        new AbstractMonster[] {
                                new AcidSlime_S(-300.0F, 0.0F, 0),
                                new SpikeSlime_S(-100.0F, 0.0F, 0),
                                new Junkbot(130.0F, 0.0F),
                        })),
                new EncounterModifyingZone.ZoneEncounter(PEDDLER, 1, () -> new MonsterGroup(
                new AbstractMonster[] {
                        new Peddler(0.0f, 0.0F)
                }))
        );
    }

    @Override
    public boolean canSpawn() {
        return isAct(1);
    }

    @Override
    public Set<String> addSpecificEvents() {
        Set<String> baseGameEvents = new HashSet<>();
        baseGameEvents.add(ScrapOoze.ID);
        return baseGameEvents;
    }

    @Override
    public float zoneSpecificEventRate() {
        return 0.4f;
    }

    @Override
    public void postRenderCombatBackground(SpriteBatch sb) {
        sb.draw(bg, 0, 0, Settings.WIDTH, Settings.HEIGHT);
    }
}
