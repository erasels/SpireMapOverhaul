package spireMapOverhaul.zones.example;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.map.MapRoomNode;
import com.megacrit.cardcrawl.random.Random;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.rooms.MonsterRoomElite;
import spireMapOverhaul.BetterMapGenerator;
import spireMapOverhaul.abstracts.AbstractZone;

import java.util.ArrayList;

public class PlaceholderZone extends AbstractZone {
    private final int width, height;
    private final Color color;

    private static String randomName() {
        StringBuilder randString = new StringBuilder();
        while (randString.length() < 4 || MathUtils.randomBoolean(0.6f)) {
            randString.append((char)MathUtils.random('a', 'z'));
        }
        return randString.toString();
    }

    public PlaceholderZone() {
        this(randomName(), MathUtils.random(1, 4), MathUtils.random(2, 6));
        System.out.println("Placeholder Zone " + name + " " + width + "x" + height);
    }

    @Override
    public AbstractZone copy() {
        return new PlaceholderZone(name, width, height);
    }

    private PlaceholderZone(String name, int width, int height) {
        super("Placeholder");

        this.width = width;
        this.height = height;

        color = new Color(MathUtils.random(1f), MathUtils.random(1f), MathUtils.random(1f), 1);
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
    public void manualRoomPlacement(Random rng, ArrayList<AbstractRoom> roomList) {
        for (MapRoomNode node : nodes) {
            //set all nodes to elite rooms, taken from the generated room list first.
            //For future reference: Might need to change the method a bit.
            //Remove a normal combat room from room list if no elite rooms remain in list? To avoid messing with distribution of other types of nodes
            node.setRoom(roomOrDefault(roomList, (room)->room instanceof MonsterRoomElite, MonsterRoomElite::new));
        }
    }
}
