package spireMapOverhaul.zones.thieveshideout;

import basemod.BaseMod;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.map.MapRoomNode;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.MonsterGroup;
import com.megacrit.cardcrawl.monsters.city.Mugger;
import com.megacrit.cardcrawl.monsters.city.Taskmaster;
import com.megacrit.cardcrawl.monsters.exordium.Looter;
import com.megacrit.cardcrawl.monsters.exordium.SlaverRed;
import com.megacrit.cardcrawl.random.Random;
import com.megacrit.cardcrawl.rooms.MonsterRoom;
import com.megacrit.cardcrawl.rooms.MonsterRoomElite;
import spireMapOverhaul.SpireAnniversary6Mod;
import spireMapOverhaul.abstracts.AbstractZone;
import spireMapOverhaul.zoneInterfaces.EncounterModifyingZone;
import spireMapOverhaul.zones.thieveshideout.events.ThiefKingEvent;
import spireMapOverhaul.zones.thieveshideout.monsters.BanditLieutenant;
import spireMapOverhaul.zones.thieveshideout.monsters.ThiefKing;
import spireMapOverhaul.zones.thieveshideout.monsters.WeakLooter;
import spireMapOverhaul.zones.thieveshideout.rooms.ForcedEventRoom;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class ThievesHideoutZone extends AbstractZone implements EncounterModifyingZone {
    public static final String ID = "ThievesHideout";
    private static final String TWO_MUGGERS = SpireAnniversary6Mod.makeID("TWO_MUGGERS");
    private static final String LOOTER_AND_RED_SLAVER = SpireAnniversary6Mod.makeID("LOOTER_AND_RED_SLAVER");
    private static final String MUGGER_AND_BLUE_SLAVER = SpireAnniversary6Mod.makeID("MUGGER_AND_BLUE_SLAVER");
    private static final String THREE_LOOTERS_WEAK = SpireAnniversary6Mod.makeID("THREE_LOOTERS_WEAK");
    public static final String BANDIT_LIEUTENANT_AND_TASKMASTER = SpireAnniversary6Mod.makeID("BANDIT_LIEUTENANT_AND_TASKMASTER");
    public static final String THIEF_KING = SpireAnniversary6Mod.makeID("THIEF_KING");

    public ThievesHideoutZone() {
        super(ID, Icons.MONSTER);
        this.width = 1;
        this.maxWidth = 3;
        this.height = 5;
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
    public void manualRoomPlacement(Random rng) {
        //We put the special node with the Thief King fight in the last row of the zone (that isn't a protected row)
        List<MapRoomNode> possibleNodes = new ArrayList<>();
        for (MapRoomNode node : this.nodes) {
            if (node.getRoom() == null && !this.isProtectedRow(node.y)) {
                possibleNodes.add(node);
            }
        }
        possibleNodes.stream()
                .map(n -> n.y).max(Comparator.comparingInt(y -> y))
                .ifPresent(maxY -> possibleNodes.removeIf(n -> n.y != maxY));

        MapRoomNode node = possibleNodes.get(rng.random(possibleNodes.size() - 1));
        node.setRoom(new ForcedEventRoom(ThiefKingEvent::new));
    }

    @Override
    public void replaceRooms(Random rng) {
        //No elites in the zone, just the Thief King fight (which awards as much as two elites)
        for (MapRoomNode node : this.nodes) {
            if (node.room instanceof MonsterRoomElite) {
                node.room = new MonsterRoom();
            }
        }
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
                    new SlaverRed(130.0F, 0.0F)
                })),
            new ZoneEncounter(MUGGER_AND_BLUE_SLAVER, 2, () -> new MonsterGroup(
                new AbstractMonster[] {
                    new Looter(-270.0F, 15.0F),
                    new SlaverRed(130.0F, 0.0F)
                })),
            new ZoneEncounter(THREE_LOOTERS_WEAK, 2, () -> new MonsterGroup(
                new AbstractMonster[] {
                    new WeakLooter(-465.0F, -20.0F),
                    new WeakLooter(-130.0F, 15.0F),
                    new WeakLooter(200.0F, -5.0F)
                }))
        );
    }

    @Override
    public void registerEncounters() {
        EncounterModifyingZone.super.registerEncounters();
        BaseMod.addMonster(BANDIT_LIEUTENANT_AND_TASKMASTER, () -> new MonsterGroup(
            new AbstractMonster[] {
                new Taskmaster(-270.0f, 15.0f),
                new BanditLieutenant(130.0f, 0.0f)
            }
        ));
        BaseMod.addMonster(THIEF_KING, () -> new MonsterGroup(
            new AbstractMonster[] {
                new Looter(-385.0f, -15.0f),
                new Mugger(-133.0f, 0.0f),
                new ThiefKing(125.0f, -30.0f)
            }
        ));
    }
}
