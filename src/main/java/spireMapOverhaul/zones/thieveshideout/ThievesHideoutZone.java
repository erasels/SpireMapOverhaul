package spireMapOverhaul.zones.thieveshideout;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.MonsterGroup;
import com.megacrit.cardcrawl.monsters.city.Mugger;
import com.megacrit.cardcrawl.monsters.exordium.Looter;
import com.megacrit.cardcrawl.monsters.exordium.SlaverRed;
import spireMapOverhaul.SpireAnniversary6Mod;
import spireMapOverhaul.abstracts.AbstractZone;
import spireMapOverhaul.zoneInterfaces.EncounterModifyingZone;
import spireMapOverhaul.zones.thieveshideout.monsters.WeakLooter;

import java.util.Arrays;
import java.util.List;

public class ThievesHideoutZone extends AbstractZone implements EncounterModifyingZone {
    public static final String ID = "ThievesHideout";
    private static final String TWO_MUGGERS = SpireAnniversary6Mod.makeID("TWO_MUGGERS");
    private static final String LOOTER_AND_RED_SLAVER = SpireAnniversary6Mod.makeID("LOOTER_AND_RED_SLAVER");
    private static final String MUGGER_AND_BLUE_SLAVER = SpireAnniversary6Mod.makeID("MUGGER_AND_BLUE_SLAVER");
    private static final String THREE_LOOTERS_WEAK = SpireAnniversary6Mod.makeID("THREE_LOOTERS_WEAK");

    public ThievesHideoutZone() {
        super(ID, Icons.MONSTER);
        this.width = 4;
        this.height = 3;
    }

    @Override
    public AbstractZone copy() {
        return new ThievesHideoutZone();
    }

    @Override
    public Color getColor() {
        return new Color(0.2f,0.6f, 0.7f, 1f);
    }

    @Override
    public boolean canSpawn() {
        return this.isAct(2);
    }

    @Override
    protected boolean canIncludeEarlyRows() {
        //Since this zone always has a dangerous event node (around super-elite difficulty), we don't want it to show up in the rows of the act that never have elites
        //It also has normal fights that are tuned as hard pool fights, so that's another reason to prevent early spawns
        return false;
    }

    @Override
    public List<ZoneEncounter> getNormalEncounters() {
        return Arrays.asList(
            new ZoneEncounter(TWO_MUGGERS, 2, () -> new MonsterGroup(
                new AbstractMonster[] {
                    new Mugger(-200.0F, 15.0F),
                    new Mugger(80.0F, 0.0F)
                })),
                new ZoneEncounter(LOOTER_AND_RED_SLAVER, 2, () -> new MonsterGroup(
                    new AbstractMonster[] {
                        new Looter(-270.0F, 15.0F),
                        new SlaverRed(-130.0F, 0.0F)
                    })),
                new ZoneEncounter(MUGGER_AND_BLUE_SLAVER, 2, () -> new MonsterGroup(
                    new AbstractMonster[] {
                        new Looter(-270.0F, 15.0F),
                        new SlaverRed(-130.0F, 0.0F)
                    })),
                new ZoneEncounter(THREE_LOOTERS_WEAK, 2, () -> new MonsterGroup(
                    new AbstractMonster[] {
                        new WeakLooter(-465.0F, -20.0F),
                        new WeakLooter(-130.0F, 15.0F),
                        new WeakLooter(200.0F, -5.0F)
                    }))
        );
    }
}
