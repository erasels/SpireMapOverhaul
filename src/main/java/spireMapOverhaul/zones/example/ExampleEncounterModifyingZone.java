package spireMapOverhaul.zones.example;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.MonsterGroup;
import com.megacrit.cardcrawl.monsters.beyond.GiantHead;
import com.megacrit.cardcrawl.monsters.beyond.Nemesis;
import com.megacrit.cardcrawl.monsters.beyond.Reptomancer;
import com.megacrit.cardcrawl.monsters.city.BookOfStabbing;
import com.megacrit.cardcrawl.monsters.city.Mugger;
import com.megacrit.cardcrawl.monsters.exordium.Looter;
import com.megacrit.cardcrawl.monsters.exordium.LouseNormal;
import com.megacrit.cardcrawl.monsters.exordium.SlaverRed;
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
    public static final String THREE_LOOTERS = SpireAnniversary6Mod.makeID("THREE_LOOTERS");
    public static final String TWO_MUGGERS = SpireAnniversary6Mod.makeID("TWO_MUGGERS");
    public static final String LOOTER_AND_SLAVER = SpireAnniversary6Mod.makeID("LOOTER_AND_SLAVER");

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
    public boolean canSpawn() {
        return this.isAct(1) || this.isAct(2);
    }

    @Override
    public List<ZoneEncounter> getNormalEncounters() {
        // This example has new custom enemies in act 1, remixes of base game enemies in act 2, and isn't valid in act 3
        // (Ignore the lack of any thematic connection, it's just an example)
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
                })),

            new ZoneEncounter(THREE_LOOTERS, 2, () -> new MonsterGroup(
                new AbstractMonster[] {
                        new Looter(-400.0F, 0.0F),
                        new Looter(-150.0F, 0.0F),
                        new Looter(100.0F, 0.0F)
                })),
            new ZoneEncounter(TWO_MUGGERS, 2, () -> new MonsterGroup(
                new AbstractMonster[] {
                        new Mugger(-200.0F, 0.0F),
                        new Mugger(100.0F, 0.0F),
                })),
            new ZoneEncounter(LOOTER_AND_SLAVER, 2, () -> new MonsterGroup(
                new AbstractMonster[] {
                        new Looter(-200.0F, 0.0F),
                        new SlaverRed(100.0F, 0.0F),
                }))
        );
    }

    @Override
    public List<ZoneEncounter> getEliteEncounters() {
        // These are completely unreasonable for their acts, but this is just an example
        return Arrays.asList(
            new ZoneEncounter(BookOfStabbing.ID, 1, BookOfStabbing::new),
            new ZoneEncounter(Reptomancer.ID, 1, Reptomancer::new),
            new ZoneEncounter(GiantHead.ID, 2, GiantHead::new),
            new ZoneEncounter(Nemesis.ID, 2, Nemesis::new)
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
