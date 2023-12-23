package spireMapOverhaul.util;

import basemod.DevConsole;
import basemod.devcommands.ConsoleCommand;
import spireMapOverhaul.BetterMapGenerator;
import spireMapOverhaul.SpireAnniversary6Mod;
import spireMapOverhaul.abstracts.AbstractZone;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class QueueZoneCommand extends ConsoleCommand {

    public QueueZoneCommand() {
        minExtraTokens = 1;
        simpleCheck = true;
    }

    @Override
    protected void execute(String[] tokens, int depth) {
        if (tokens.length != 2) {
            DevConsole.log("Specify the id of the zone to place on the next generated map");
            return;
        }
        AbstractZone zone = SpireAnniversary6Mod.unfilteredAllZones.stream()
                .filter(z-> z.id.equals(tokens[1])).findFirst().orElse(null);
        if (zone != null) {
            BetterMapGenerator.queueCommandZones.add(zone.copy());
            DevConsole.log(zone.id + " will appear on the next generated map");
        } else {
            DevConsole.log("No matching zone id found");
        }
    }

    @Override
    protected ArrayList<String> extraOptions(String[] tokens, int depth) {
        return SpireAnniversary6Mod.unfilteredAllZones.stream().map(z->z.id).collect(Collectors.toCollection(ArrayList::new));
    }
}
