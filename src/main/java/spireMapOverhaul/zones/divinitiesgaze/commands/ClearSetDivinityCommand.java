package spireMapOverhaul.zones.divinitiesgaze.commands;

import basemod.DevConsole;
import basemod.devcommands.ConsoleCommand;
import spireMapOverhaul.zones.divinitiesgaze.divinities.DivineBeingManager;

public class ClearSetDivinityCommand extends ConsoleCommand {

  public ClearSetDivinityCommand() {
    this.minExtraTokens = this.maxExtraTokens = 0;
    this.simpleCheck = true;
  }

  @Override
  protected void execute(String[] strings, int i) {
    DevConsole.log("Divinity lock cleared");
    DivineBeingManager.setOverrideDivinity("");
  }
}
