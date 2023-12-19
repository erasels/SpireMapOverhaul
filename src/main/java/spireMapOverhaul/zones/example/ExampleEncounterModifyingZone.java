package spireMapOverhaul.zones.example;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.monsters.beyond.Reptomancer;
import com.megacrit.cardcrawl.monsters.beyond.Spiker;
import com.megacrit.cardcrawl.monsters.city.BookOfStabbing;
import com.megacrit.cardcrawl.monsters.city.Mugger;
import spireMapOverhaul.abstracts.AbstractZone;
import spireMapOverhaul.zoneInterfaces.EncounterModifyingZone;

import java.util.Arrays;
import java.util.List;

public class ExampleEncounterModifyingZone extends AbstractZone implements EncounterModifyingZone {
    public static final String ID = "ExampleEncounterModifying";

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
            new ZoneEncounter(Mugger.ID, () -> new Mugger(0.0f, 0.0f)),
            new ZoneEncounter(Spiker.ID, () -> new Spiker(0.0f, 0.0f))
        );
    }

    @Override
    public List<ZoneEncounter> getEliteEncounters() {
        return Arrays.asList(
            new ZoneEncounter(BookOfStabbing.ID, BookOfStabbing::new),
            new ZoneEncounter(Reptomancer.ID, Reptomancer::new)
        );
    }
}
