package spireMapOverhaul.zones.divinitiesgaze.commands;

import basemod.DevConsole;
import basemod.devcommands.ConsoleCommand;
import spireMapOverhaul.zones.divinitiesgaze.divinities.DivineBeingManager;

import java.util.ArrayList;

public class SetDivinityCommand extends ConsoleCommand {

  public SetDivinityCommand() {
    this.minExtraTokens = 1;
    this.simpleCheck = true;
  }

  @Override
  public void execute(String[] strings, int i) {
    if(strings.length != 2) {
      DevConsole.log("Specify the ID of the divinity to force");
      return;
    }

    if(!DivineBeingManager.getDivinityIds().contains(strings[1])) {
      DevConsole.log("No such divinity");
      return;
    }

    DevConsole.log("Only " + strings[1] + " will spawn");
    DivineBeingManager.setOverrideDivinity(strings[1]);
  }

  @Override
  public ArrayList<String> extraOptions(String[] tokens, int depth) {
    return new ArrayList<>(DivineBeingManager.getDivinityIds());
  }
}
