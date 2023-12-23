# MtS Modding Anniversary 6: Spire Map overhaul
## Preamble
A group project for the sixth anniversary of Mod the Spire. Zones will spawn on the map of each act where they will affect the map nodes within. This includes custom events, encouters, campfire actions, nodes, etc.   
For a full write-up, please see https://docs.google.com/document/d/1ecUm3TWngPBoLVC278x7AfpXj_ImJMJkSWnZAyGLpYg/edit?usp=sharing  
  
## Contribution
The scope of a contribution will commonly be a Zone with its various changes. For guidelines regarding what kind of content is allowed/accounted for and about the minimum content required, please look at the google doc linked above.  
  
### Technical Guidelines
Create a package within the zones package where you will store all your code. That means you're not supposed to put your patches/monsters/etc in the relevant top-level packages as you are used to, this will allow all zone content to be grouped correctly and might help with organization. You are free to define the sub package order within your zone package.    

You will need a Zone class which extends AbstractZone and implements the various interfaces for modifying aspects of the game (found in the zoneInterfaces package) within it you will have most of the code which controls what you zone does, from adding new encouters to deciding what nodes spawn in your zone.    

AbstractZone's constructor requires an unprefixed ID (so do not call makeID on your ID when creating it) and any of the Icons enums which will used to signify what type of content your zone modifies in its tooltip. Available icons are: MONSTER, ELITE, REWARD, EVENT, REST and SHOP.  

Strings will be handled int he same way as they were in packmaster, create a directory with the same ID as your zone and put all your localization jsons into that.  
  
In case your zone replaces or changes nodes, be sure to look at the following methods within AbstractZone:  
- canIncludeEarlyRows()  
- canIncludeTreasureRow()  
- canIncludeFinalCampfireRow()  
If your zone includes manual node replacement logic be sure to check for the last cmapfire before the boss node and the mid-act chest as these should not be overridden and the above methods don't apply.  
  
To make a contribution, you must have a GitHub account. 
For the specifics of how to fork this repo and then make a pull request, please look at this guide:  
https://docs.github.com/en/get-started/quickstart/contributing-to-projects  
   
I recommend using the GitHub desktop client for this if you have no experience with Github  
https://desktop.github.com/
