# MtS Modding Anniversary 6: Spire Map overhaul
## Preamble
A group project for the sixth anniversary of Mod the Spire. Zones will spawn on the map of each act where they will affect the map nodes within. This includes custom events, encouters, campfire actions, nodes, etc.   
For a full write-up, please see https://docs.google.com/document/d/1ecUm3TWngPBoLVC278x7AfpXj_ImJMJkSWnZAyGLpYg/edit?usp=sharing  
  
## Contribution
The scope of a contribution will commonly be a Zone with its various changes. For guidelines regarding what kind of content is allowed/accounted for and about the minimum content required, please see the google doc linked above.  

Either modargo or I (erasels) will be reviewing your pull request and suggest changes to code and/or design and balance if needed to keep the project cohesive. Since the start of this project is during the christmas holidays, we may be slow to respond but don't worry, we will.
  
### Technical Guidelines
Create a package within the zones package; this is where you will put all your code. This allows all zone content to be grouped together, so you're not supposed to put your patches/monsters/etc in the top-level packages anymore (which is a change from previous anniversary projects that we think will help with organization). You are free to define subpackages within your zone package.    

#### Zone

You will need a Zone class which extends AbstractZone and implements the various interfaces for modifying aspects of the game (found in the zoneInterfaces package). Within it you will have most of the code which controls what your zone does, from adding new encouters to deciding what nodes spawn in your zone. It's okay to do things those interfaces don't support yet by writing patches, though if there's something that multiple people want to do, it may be worth adding to one of the interfaces.

AbstractZone's constructor requires an unprefixed ID (so do not call makeID on your ID when creating it) and any of the Icons enums which will used to signify what type of content your zone modifies in its tooltip. Available icons are: MONSTER, CHEST, EVENT, REST and SHOP.  

Strings will be handled in the same way as they were for Packmaster: create a directory with the same ID as your zone under `resources/anniv6Resources/localization/eng` and put all your localization jsons into that.

Images (for cards, powers, monsters, etc.) should go in `resources/anniv6Resources/images`, then the appropriate directory for the type of image (e.g. `cards`), then create a directory with the same ID as your zone. There are constructors for `AbstractSMOCard` and `AbstractSMOPower` that take in a zoneID, which you will need to use to load the correct image. If you're making a number of cards/powers for your zone, you may want to create your own abstract class to make things a bit easier (see `AbstractInvasionCard`/`AbstractInvasionPower` for an example of this).
  
In case your zone replaces or changes nodes, be sure to look at the following methods within AbstractZone:  
- canIncludeEarlyRows()
- canIncludeTreasureRow()
- canIncludeFinalCampfireRow()

If your zone includes manual node replacement logic, you need to either check for the last campfire before the boss node and the mid-act chest as these should not be overridden, or have those methods return `false` so that your zone never includes those rows.  

To test your zone, you can add the console command `addzone ID` which will make ti spawn in the next act. Zones are also toggleable in the mod config menu.

Your zone's icon should be a 512x512 png file located in `resources/anniv6Resources/images/ui/zoneIcons` and fit the template provided in that same folder. It should entirely black, optionally with some white. Ideally it should be pretty simple and easily recognizable.

#### Cards, relics, monsters, powers, etc.
Cards, relics, monsters, powers, patches, and everything else should go in the package you created for your zone.

There are abstract classes that you should extend in the abstracts package: `AbstractSMOCard`, `AbstractSMORelic`, `AbstractSMOMonster`, and `AbstractSMOPower`.

#### Events
Any event classes within the same package as a zone will automatically be registered with the requirement of being within that zone. If there is a `public static boolean bonusCondition()` method within the class, that will also be used as a condition for the event to spawn. If you require something else, or don't want the event to be registered, use `@AutoAdd.Ignore` on the class and register it manually.

#### Bestiary
If you add new monsters, consider adding [Bestiary](https://steamcommunity.com/sharedfiles/filedetails/?id=2285965269) entries for them, so that players have an easy way to understand and analyze what the monsters do. This also helps document the logic for your new monsters. See the existing `bestiary.json` files for examples.

### How to make PRs
  
To make a contribution, you must have a GitHub account. 
For the specifics of how to fork this repo and then make a pull request, please look at this guide:  
https://docs.github.com/en/get-started/quickstart/contributing-to-projects  
   
I recommend using the GitHub desktop client for this if you have no experience with Github  
https://desktop.github.com/
