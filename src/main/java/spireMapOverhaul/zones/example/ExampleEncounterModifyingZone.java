package spireMapOverhaul.zones.example;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.MonsterGroup;
import com.megacrit.cardcrawl.monsters.beyond.Reptomancer;
import com.megacrit.cardcrawl.monsters.city.BookOfStabbing;
import com.megacrit.cardcrawl.monsters.exordium.LouseNormal;
import com.megacrit.cardcrawl.rooms.MonsterRoomElite;
import spireMapOverhaul.SpireAnniversary6Mod;
import spireMapOverhaul.abstracts.AbstractZone;
import spireMapOverhaul.zoneInterfaces.EncounterModifyingZone;
import spireMapOverhaul.zones.example.monsters.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ExampleEncounterModifyingZone extends AbstractZone implements EncounterModifyingZone {
    public static final String ID = "ExampleEncounterModifying";
    public static final String STYGIAN_BOAR_AND_WHISPERING_WRAITH = SpireAnniversary6Mod.makeID("STYGIAN_BOAR_AND_WHISPERING_WRAITH");
    public static final String DREAD_MOTH_AND_GRAFTED_WORMS = SpireAnniversary6Mod.makeID("DREAD_MOTH_AND_GRAFTED_WORMS");

    public ExampleEncounterModifyingZone() {
        super(ID);
        this.width = 3;
        this.height = 3;
    }

    @Override
    public AbstractZone copy() {
        return new ExampleEncounterModifyingZone();
    }

    @Override
    public Color getColor() {
        return Color.BLUE.cpy();
    }

    @Override
    public List<ZoneEncounter> getNormalEncounters() {
        return Arrays.asList(
            new ZoneEncounter(Hexasnake.ID, 1, () -> new Hexasnake(0.0f, 0.0f)),
            new ZoneEncounter(STYGIAN_BOAR_AND_WHISPERING_WRAITH, 1, () -> new MonsterGroup(
                new AbstractMonster[] {
                        new WhisperingWraith(-350.0F, 0.0F),
                        new StygianBoar(0.0F, 0.0F)
                })),
            new ZoneEncounter(DREAD_MOTH_AND_GRAFTED_WORMS, 1, () -> new MonsterGroup(
                new AbstractMonster[] {
                        new GraftedWorm(-550.0F, 0.0F),
                        new GraftedWorm(-300.0F, 0.0F),
                        new GraftedWorm(-50.0F, 0.0F),
                        new DreadMoth(200.0F, 125.0F),
                }))
        );
    }

    @Override
    public List<ZoneEncounter> getEliteEncounters() {
        return Arrays.asList(
            new ZoneEncounter(BookOfStabbing.ID, 1, BookOfStabbing::new),
            new ZoneEncounter(Reptomancer.ID, 1, Reptomancer::new)
        );
    }

    @Override
    public List<AbstractMonster> getAdditionalMonsters() {
        if (AbstractDungeon.getCurrRoom() instanceof MonsterRoomElite) {
            // It's best to stick to adding only one additional enemy like this example, because the positioning
            // logic isn't perfect and there's only so much space on the screen.
            return Collections.singletonList(new LouseNormal(0.0f, 0.0f));
        }
        return null;
    }
}
