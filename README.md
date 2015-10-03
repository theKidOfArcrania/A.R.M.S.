#A.R.M.S Basics
A.R.M.S, short for "A Race for Munitions and Survival", is an intense, turn based card game. You face your opponents as unique robots with special traits, using various weapons to destroy the competitors before they can destroy you. But that's not all! In the "Junkyard", you salvage various weapon upgrades and many useful tools, ranging from cards that repair your shields to those that bombard your opponents with a hellstorm of bullets. At times, scavenging the junkyard can trigger an unexpected event, and only luck will tell whether these will help or hurt you. If you efficiently use the resources you recieve, and you are the last bot standing, you gain Scraps, the game's currency to buy robots, permanent skins, or temporary boosts. In addition, your prestige will rise, pulling you higher on the leaderboard. Now is the time: grab your A.R.M.S and pulverize your opponents before they turn their weapons on you! Ready... Aim... **FIRE!**

#A.R.M.S Game Rules
##Game Modes
Currently, there is only one game mode: survival. Further down the road, we might implement other game modes.
###Survival
In this game mode, players play as robots and they must gain as many weapons as possible using them to attack and destroy all the opponents. Last survivor wins, death -> elimination

##Player Specifications
Player Energy: 100 Volts
Player Shield: Starts at 0 Volts, charges by attacking with a weapon that uses energy or drawing shields.
You can gain shield by selling cards (energy worth is shown on the card)

##Gameplay
A.R.M.S. is a turn-based strategic game. One player goes at a time, based on their ordering. In one turn a player gets 3 moves. At the beginning of their turn, players draw two cards, and then can play up to three actions. For each action, a player can  

	A. Play a card  
	B. Discard a card  
	C. End turn

At the end of each turn, players can have up to 7 cards in their hand. Or else, player is forced to discard so that the player is left with exactly 7 cards. Discarding cards at this point does not take a turn.

##Win Conditions

When all other players are dead, winner will gain X amount of points, others will gain points based on performance.  
People in the games are matched based on their ranking.

##Weapons

The weapons are used to attack and damage other players' energies/shields. These may not be used until the player has obtained a respective Ammunition card, allowing one chance to attack single or multiple targets.

###Weapon Upgrade Types

Each weapon upgrade that can be added on to a base weapon fall in the following categories:

######Accuracy
*(Increased by range, sights, etc.)* Weapons start out with a base "accuracy", and the actual damage is a percentage of what the base "accuracy" is. For example, if a weapon has an accuaracy of 67%, 67% of the time the weapon will reach 100% efficency. To determine efficentcy, the player has to stop the a moving tick on a bar that is skewed so to represent xx% efficency.

######Damage
*(Increased by rate of fire, power upgrades, etc.)* Weapons start out with a base damage, and energy/shield this takes away is based on this damage. This is seperated with two categories: group damage (damage done to multiple targets) and single damage (damage done to single target).

######Vampiric
*(Some upgrades make things vampiric, energy crystals always do)* Weapons can be vampiric, which means that they absorb shields or energy to the player who attacks.

#####Excess Parts   
*(When you draw or gain a part of a weapon that is already complete, you can either discard it, or salvage it/break it down for shields)

###Energy Crystal
You can add an energy crystal to a completed weapon, changing the weapon to its crystal form  
You can use the crystal to destroy all upgrades on an opponents weapon, but it will consume the crystal  

###Weapon Sets

Each robot by default is equipped with 8 basic weapons.
Weapons can be upgraded with parts. 

1. Brown - Knife (Especially effective for "Nightv0") 
	- Base Accuracy: 30%  
	- Base Damage: X  
	- Upgrades:
		- Blade Upgrade: Longer length and serrated edges (+Damage)  
		- Handle Upgrade: Increased precision and allows shooting of blade. (+Accuracy)  
		- **Energy Sword: (Crystal Form)** Breaks through shield, dealing direct damage to health.   

2. Dark Blue - Pistol - (Especially effective for "DualDestiny")  
	- Base Accuracy: 30%  
	- Base Damage: X  
	- Upgrades:
		- Barrel Upgrade: Longer barrel allows for increased precision (+Accuracy)  
		- Grip Upgrade: Increased ease of use and reduced recoil (+Accuracy)  
		- Magazine Upgrade: A larger magazine increases the number of bullets fired per load (+Damage)  
		- **Energy Pulser (Crystal Form):**  Threeburst pulser which fires large bursts of splattering plasma with large range.   

3. Purple- Shotgun - (Especially Effective for "Bulk XR")  
	- Base Accuracy: 40%  
	- Base Damage: X  
	- Takes one turns to charge when ammo is put in, but inflicts heavy damage  
	- Upgrades:
		- Cone of Fire: The gun's cone width is reduced, allowing for higher precision.(+Accuracy)  
		- Magazine Upgrade: Increased magazine allows more more shots every load. (+Damage)  
		- **Charging Blaster (Crystal Form):**  Weapon that fires pure energy, inflicting tons of damage at short range. Takes one turn to charge.  
	
4. Orange - Machine Gun - (Especially effective for "R24 Lockload")  
	- Base Accuracy: 40%  
	- Base Damage: X  
	- Upgrades:
		- Holographic Sight: Sight added, increasing accuracy greatly. (+Accuracy)  
		- Heat Dissipation Upgrade: Diamond heatsink located in the barrel, allows for much longer periods of fire (+Damage)  
		- Fire Rate Upgrade: Six Barrels  greatly increases the amount of bullets fired (+Damage)  
		- Kickstand Upgrade: A folding kickstand reduces kick of the bullets fired and spread of bullets fired, increasing accuracy (+Accuracy)  
		- **Hexid Barrel Energy Pulser (Crystal Form):** Accurate weapon which fires many pulses of energy per second, burning through any defenses.  

5. LightBlue - Sniper - (Especially effective for "PP3 Skyjack")  
	- Base Accuracy: 50%  
	- Base Damage: X  
	- High single target damage, but low multitarget damage  
	- Upgrades: 
		- Bullet Upgrade - Penetrating bullets rip through most materials, inflicting heavy damage (+Damage)  
		- Scope Upgrade - Scope added with enhanced zoom capabilities, and night vision, for increased ease of use and accuracy(+Accuracy)  
		- Clip Upgrade - Increases size of the magazine (+Damage)  
		- **Beam Raygun (Crystal Form):** Gun with insane range, fires an lightquick beam of energy that can pierce any material. Any. This gives it high single target damage, but its low rate of fire gives it low multitarget damage.  
	
6. Green - Grenade - (Especially effective for "Scrap rm7")  
	- Base Accuracy: 50%  
	- Base Damage: X  
	- Each upgrade adds a new grenade to the package, High multitarget damage, low single target damage  
	- Upgrades:	
		- Fragmentation Upgrade: Shell of grenade is made of a different material that splits into fragments upon explosion, inflicts some multitarget damage if single target attack (+Damage)  
		- Filler Upgrade: Heavier filler creates a more powerful explosion. (+Damage)  
		- Aerodynamic Upgrade: Streamlined shape makes the grenades fly further (+Accuracy)  
		- **Plasma Grenade (Crystal Form):** Grenades stick onto opponents, then explodes at extreme temperatures. Leaves behind a burning field of plasma.  
	
7. Red - Rocket Launcher- (Especially effective for "Tech-o-maniac Tr1")
	- Base Accuracy: 60%  
	- Base Damage: X  
	- Upgrades:
		- Warhead Upgrade: Shaped charge warhead is highly explosive and breaks through most armor (+Damage) 
		- Launcher Upgrade: Turbocharged Launcher fires the rocket at huge ranges. (+Accuracy)  
		- Energy Tracker Upgrade: New technology allows rockets to lock onto heat sources or energy sources. (+Accuracy)  
		- **Splashing Plasma Rockets (Crystal Form):** On explosion, quick lockon rockets scatter high pressure plasma in all directions.  
	
8. Yellow- Nuclear Bomb (Maybe take this out)  
	- Nuclear explosion occurs when two sources of plutonium are rammed together by an Explosive  
	- Base Accuracy: 80%  
	- Base Damage: X (LOTS)  
	- Upgrades:
		- Uranium-235 Supply- Must be played and enriched, takes X amount of turns to reach 100%  
		- Nuclear Shell - Shell to hold Material  
		- Explosives: Trinitrotoluene (TNT) is used to compress stores of plutonium  
		- Remote Detonator: Uses radio technology to ignite the TNT  
		- No Crystal Form  

##Robots

There are 7 robots, each specializing in one weapon (shown in paraenthesis). Each robot also has unique traits, passives, and event cards.  
###Nightv0 (Dagger)

- Unique passive: *Unseen:* Takes reduced single target damage.
- If dagger is complete, becomes *Untouchable:* cannot be attacked by single target damage  
- Event Cards
	1. **Backstab:** Instantly deal X damage to the player of choice  
	2. **Invisibility Cloak:** Become unattackable for the next turn  

###DualDestiny (Pistol)

- Unique Passive: *Agility:* Takes reduced multitarget damage
- If pistol is complete, becomes *Lightspeed:* Cannot be attacked by multitarget damage   
- Event Cards
	1. **Prepare:** Next action does not require a turn  
	2. **Dance of Death:** Deal X damage to everyone  

###Bulk XR (Shotgun)

- Unique Passive: *Tanky:* Takes reduced damage from all attacks
- If shotgun is complete, becomes *Thorny:* Attackers damage themselves a little if they damage Bulk.  
- Event Cards
	1. **Riot Shield:** When played, blocks the next source of damage from anything  
	2. **Armor Suit:** Grants X Shield for the next X turns that recharges.  

###R24 Lock n'load (Machinegun)

- Unique Passive: *Spray and Pray:* attacks that damage everyone do extra damage
- If machine gun is complete, becomes *Just Spray:* multitarget attacks do extra damage, single target attacks do collateral damage. 
- Event Cards
	1. **It Was an Accident!:** Fires a random weapon at a target  
	2. **Happy Feet:** Robot buzzes around and steals X shields from each player  

###PP3 Skyjack (Sniper)

- Unique Passive: *Camper:* If not attacked for X turns, heals X hp (no shield gain)
- If sniper is complete, becomes *Survivor:* heals X hp per turn  
- Event Cards
	1. **Care Package:** Grants X amount of shield and health  
	2. **Headshot!:** X Increased damage on next single target attack  

###Scrap rm7 (Grenade)

- Unique Passive: *Large Inventory:* Can hold 8 cards
- If grenades is complete, becomes *Draw:* Draw 3 cards at the start of turn 
- Event Cards
	1. **Harvest:** Adds a random action card to your hand  
	2. **Collection:** Adds a random upgrade card to your hand  

###Tech-o-maniac T1 (Rocket Launcher)
- Unique Passive: *Reuser Bot:* Event cards have a small chance to be bots (LOL you can't get these cool event cards...)
- If rocket launcher is complete, gains *Construction Bot:* Event cards are guaranteed to be bots  
- Event Cards
	1. **Time Bot:** Reset the players turn. Allows player to play 3 more moves.  
	2. **Hijack Bot:** Disable one weapon for one opponents for their next turn.  
	3. **Heal Bot:** Restores Energy by X amount.  
	4. **Shield Bot:** Grants X Shield.  
	5. **Ammo Bot:** Gain ammo for a chosen weapon.  
	6. **Hack Bot:** Look at 1 card from each persons hand  

##Action Card Types

- **BOOST!**: One use ammunition upgrade
- **PART PICKER**: Stealing one weapon part
- **ARMS ABDUCTION**: Stealing a whole weapon
- **SWAP!**: Swapping a weapon part with another person 
- **FOCUSED SHOT**: Single target barrage
- **RAIN OF FIRE**: Multi target small fire
- **BARRIER**: Prevent any attack or swap
- **SINGLE TARGET AMMUNITION**: Single Ammunition for fire on one person, can be used on any weapon class
- **MULTI TARGET AMMUNITION**: Ammunition specific to one three weapons (Dagger/sword/club, BowArrow/Spear, Bayonett/MachineGun, Flamethrower/GrenadeLauncher, Nuke). This can be used to attack everyone. 
- **ENERGY CRYSTAL** (explained above)
- **SHIELD CHARGE**: This recharges the shield by a fixed amount. 
- **ENERGY PACK**: This recharges the energy by a fixed amount, overflowing into the shield if energy is full.
- **SCAVENGE THE JUNKYARD**: Draw two cards from the scavenge pile.
- **TIME WARP**: Another turn
- **[EVENT CARD]**: Special type of action card that remains a mystery until it is played.

##Event Cards
This category of cards is also known as the mystery card: it remains a mystery until it is played. This may be a good card or bad, depending on your luck. There are four default event cards, and some robots have additional event cards that may be purchased.
	
1. **Lucky Battery**: Heals Energy or Shield by 10
2. **Weapon Malfunction**: If ___ is owned, the weapon upgrade is destroyed and put into the discard pile, unless it is part of a full set
3. **Robot Malfunction**: Glitches and turn ends
4. **Weapon of Choice**: Can become any weapon upgrade.

##Response Cards
This category of cards is used in response to a card that another player has played against you. In order to activate it, you must play it in the field. Other players will not know what your response card is, but will know that you played a response card. There are three types of response cards. Depending on the card, it might be effective for one or more of these subcategories. In all, the cards will always nullify the move or the countermove and replace it with something else.

- Attack Response Card: this subcategory counteracts any attacks made against the player.
- Transfer Response Card: this subcategory counteracts any weapon transfers made against the player.
- Response Response Card: this subcategory counteracts any responses directed against the player. (This is the only one that can be played during a player's turn).

##Currency

Based on the results of the game (win/lose), each player will recieve some "Scraps". These scraps can be spent on things in the "Shop"

###Shop Inventory
#####Permanent Upgrades
- Unlocking characters(robots) costs scraps, (X, 2X, 3X, etc for more and more robots)
- Unlocking more action cards and also some more robot-specific event cards.
- Special Skins for each robot character (X, 2X, 3X for increasingly better ones)
- Special Skins for player's information panel 
- New Game Skins and music

#####Temporary Boosts
- Boost to damage for one game
- Boost to starting shield for one game
- Boost to max shield and max energy for one game
- These can be purchased in bulk (five Boosts bundled) or just single purchases.
