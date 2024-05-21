# Do not go to Moriyashiine (creator of Enchancement) for issues with this mod!</br>Please come to me instead!

_**This mod, like Enchancement, requires client configurations to be synced with the server's.**_

### How to install this mod:
Enchancement Unbound's versions will be put into this format:
a.b.c+x.y-z
All the user needs to care about is the x.y-z part. This part is the Enchancement version that it is compatible with.
For example, 1.0.1+1.20-20 will only be compatible with Enchancement 1.20-20.

### About this mod:
Enchancement Unbound was originally made to remove some of the balancing changes that Moriya had implemented.
However, I had too many ideas, so I not only did I pretty much accomplish that goal, I also added some of my own enchantments and items. I also added shield enchantments, but they were moved to another mod that I will be working on.
As of now, all changes that I made are configurable.

## Items
Can be disabled in the config under "Register This Mod's Items (Separate From Enchancement)" [Default = false, My Recommendation = true] WARNING: DISABLING THIS WILL REMOVE ALL ITEMS FROM THIS MOD FROM ANY WORLD YOU ENTER. DATA MAY BE LOST; TREAD WITH CAUTION.
- Rain of Fire (Orbital Strike Brimstone) : Calls down brimstone from the heavens.
- Amethyst Shard Minigun : Fires amethyst shards extremely quickly. Uses Amethyst Shards for ammunition.
- Ice Shard Minigun : Amethyst Shard Minigun, but fires frostbite shards. Uses Packed Ice and Blue Ice for ammunition.
- <obfuscated> : Bow but fires arrows every tick, compatible with enchantments from both vanilla and Enchancement.

## Entities
Do not summon naturally unless a config option is turned on.
- Astral Phantom (in-game, obfuscated) : Spawns randomly when a player has been in the astral dimension (Astral Veil) for too long. Will not render if the player has never entered the astral dimension since their last death. The higher "insanity" a player has, the higher the chance an astral phantom will spawn. Has a very low chance to spawn more of itself when attacking a player.

## Enchantments
Can be disabled in the config under "Register This Mod's Enchantments (Separate From Enchancement)" [Default = false, My Recommendation = true] WARNING: DISABLING THIS WILL REMOVE ALL ENCHANTMENTS FROM THIS MOD FROM ANY WORLD YOU ENTER. DATA MAY BE LOST; TREAD WITH CAUTION.
- Ascension (idea by @duriangape on Discord) : Shovel enchantment. Inspired by the Ascend ability from Zelda: Tears of the Kingdom. Allows the user to phase through blocks upward when clicking on the bottom face of a block.
- Executioner : Axe enchantment. About 2.5% chance to instantly execute the entity. Entities that cannot be executed, such as bosses and players, will take increased damage instead. Only activates when the target has 40% health or lower, but chance of activating increases with how wounded the target is, to a maximum of 5% at around 0.2% health remaining. The damage formula is clamp((-(log10(healthPercentage) / 5) - 0.0546), 0, 0.5) in percentage
- Midas Touch (suggested by @opixeel on Discord) : Sword/Axe/Pickaxe/Shovel/Hoe curse. Inspired by the Greek myth of King Midas in which he gains the ability to turn anything to gold. This curse turns entities into gold upon hit. Fails in the nether. Deactivates if the entity is touching rain, water, or a bubble column, if the entity has extended water (the amphibious effect that allows you to riptide out of water), is frozen (from frostbite), or has been golden for 10 minutes. Stronger Entities will break free much faster. Only works on gold or netherite tools.
- Apple Sauce : Hoe enchantment. Breaking leaves has a chance of dropping extra apples. Has a smaller chance to drop golden apples. A gold apple drop has a small chance to be an enchanted golden apple instead. Greatly increases the hoe's mining speed when breaking leaves.
- Soul Reaper : Hoe Curse (but renders in aqua if Enchancement's Colored Enchantment Names is on). Steals experience from the target entity and gives it to the attacker.

## Other Config Options
### MAIN:
- Remove Crossbow Cooldown : Prevents crossbow enchantments such as scatter and brimstone from putting the crossbow on cooldown after firing. [Default = false, My Recommendation = false]
- Brimstone Self-Damage : Whether brimstone should deal life drain damage after firing. [Default = true, My Recommendation = true]
- Maximum Brimstone Damage : The maximum damage a single brimstone projectile can do. [Default = 50, My Recommendation = 75]
- All Crossbows Have Multishot : Puts multishot on all crossbows (similar to Enchancement's All Tridents Have Loyalty). [Default = false, My Recommendation = true]
- Super Quick Charge (Crossbow) : Makes crossbows have a really short projectile loading time. [Default = false, My Recommendation = true]
- Take Extra Damage While Sliding : Whether using slide makes the user take extra damage. [Default = true, My Recommendation = true]
- Deal Damage On (Slide) Slam : Deals damage on slam impact. [Default = false, My Recommendation = true]
- Slam Impact Does Self Damage : Deals self-damage on slam impact (from hitting the ground too hard). Self-damage is 0.6 * damage value. Requires Deal Damage On (Slide) Slam to be on also. [Default = false, My Recommendation = true]
- (Slide) Slam Impact Damage : Numerical value of impact damage. [Default = 4, My Recommendation = 4]
- Veil Gets Permanent Invisibility : Self-explanatory. Wearing a veil helmet makes you invisible to people who don't have perception. [Default = false, My Recommendation = true]
- Astral Veil : WIP feature. Inspired by Talon's end dust mod. When wearing a veil helmet, press the Veil Syzygy keybind (default bound to Enter) to enter the astral plane. While in the astral plane, you cannot take damage, but you cannot use items or interact with blocks or entities. [Default = false, My Recommendation = false]
- Perception Makes Entities Glow : Makes perception show entity outlines even if the entity is not behind a wall [Default = false, My Recommendation = true]
- Amphibious Allows Swimming In Air : Another WIP feature. Allows you to swim in air with an amphibious chestplate. [Default = false, My Recommendation = false]
- Amphibious Always Applies Extended Water : Self-explanatory. [Default = false, My Recommendation = true]
- Infinite Gale : Self-explanatory. [Default = false, My Recommendation = false]
- Infinite Dash : Self-explanatory. [Default = false, My Recommendation = false]
- Infinite Strafe : Self-explanatory. [Default = false, My Recommendation = false]
- Instant Eating with Assimilation : Assimilation only takes one tick to eat a piece of food. [Default = false, My Recommendation = true]
- No Berserk Damage Cap : Removes the berserk damage cap (I can't really tell if this considerably boosts damage output or not). [Default = false, My Recommendation = true]
- Scatter Projectile Multiplier : The number of shards that scatter fires will be multiplied by this amount. Also affects Amethyst Shard Minigun and Ice Shard Minigun. [Default = 1, My Recommendation = 1]
- Wardenspine Yeets : Wardenspine will throw the attacker back upon activation. [Default = false, My Recommendation = false]
- No Bury Cooldown : Shovels will no longer go on cooldown after using bury. [Default = false, My Recommendation = false]
- Bury Everything : ignores the cannot_bury tag from Enchancement [Default = false, My Recommendation = false]
- Frostbite Tridents Freeze Water : When a trident enchanted with frostbite hits water, it will apply a frost-walker-like effect. [Default = false, My Recommendation = true]
- Bouncy Charge Speed : How fast bouncy should charge up fully. [Default = DEFAULT, My Recommendation = INSTANT]
- Bouncy Jump Multiplier : The upwards velocity gained from bouncy will be multiplied by this amount. [Default = 1, My Recommendation = 1]
- Disarm Steals Player Items : Disarm steals items from players instead of just putting them on cooldown. [Default = false, My Recommendation = false]
- Leech Leeches Forever : Self-explanatory. [Default = false, My Recommendation = false]
- Interval Between Leeches (In Ticks) : How long (in ticks) it takes for leech to leech (attack target and heal owner) [Default = 20, My Recommendation = 5]
- Chaos Applies Longer And Stronger Effects : Effects applied to chaos arrows last 3x longer and have an amplifier equal to the enchantment level + 1. Instant effects will instead have an amplifier of the enchantment level + 3 (for reference, if chaos applies instant damage and this option is true, it is enough to one-shot a player). [Default = false, My Recommendation = false]
- Instant Charge Delay : Self-explanatory. [Default = false, My Recommendation = false]
- Maximum Delay Floating Time (In Ticks) : How long a delay arrow can float before shooting on its own. Set to the value of Integer.MAX_VALUE (2147483647) for delay to float forever. [Default = 200, My Recommendation = 1200]
- Phasing Homing Radius (In Blocks) : How far away (in block length) phasing can activate homing from. [Default = 0.5, My Recommendation = 1]
- Grapple Multiplier When Pulling Others : The velocity gained from grapple put on the pulled entity will be multiplied by this amount. [Default = 1, My Recommendation = 2]
- Grapple Multiplier When Pulling Self : The velocity gained from grapple put on the user will be multiplied by this amount. [Default = 1, My Recommendation = 1.5]
- Impact Falls Slightly Faster : Impact falls slightly faster depending on external factors. However, it will not go below the regular speed. [Default = false, My Recommendation = true]
- Warp Tridents With Loyalty Return Faster : Makes tridents with warp and (innate) loyalty return slightly faster than normal. [Default = false, My Recommendation = true]
- Frostbite Lasts Forever : Self-explanatory. [Default = false, My Recommendation = false]
- Fire Aspect (Basically) Lasts Forever : Self-explanatory. The word "basically" is in parentheses because the fire time is actually set to Integer.MAX_VALUE, so it is close to almost forever. [Default = false, My Recommendation = false]
- All Tools Have Efficiency Automatically : Puts efficiency on all tools (similar to Enchancement's All Tridents Have Loyalty). [Default = false, My Recommendation = false]
- Remove Movement Boost Limit : Prevents Enchancement from putting a cap on movement buffs gained from adrenaline. [Default = false, My Recommendation = true]
- Adrenaline Speed Multiplier : The adrenaline speed boost will be multiplied by this amount. Doesn't do a lot without Remove Adrenaline Movement Boost Limit. [Default = 0.05, My Recommendation = 0.15]
- Return Curse Patch : Curse Patch is applied to the game. All enchantable items can have a regular enchantment, plus unlimited curses. Curses do not count towards the enchantment limit. [Default = false, My Recommendation = TRUE, I TRIED SO HARD TO MAKE THIS ONE, YOU SHOULD DEFINITELY REAP THE BENEFITS]
- Horseshoes: Allows all horse armor to be enchanted with vanilla and enchancement boot enchantments. Provides support for all vanilla and enchancement enchantments except for Gale. Does not support other modded enchantments. Also adds horse armor glint. [Default = false, My Recommendation = true]

### CLIENT:
- Brimstone Size Limit (purely visual) : Brimstone size scales with damage, so this puts a cap on how big brimstone can render (does not actually change the hitbox or raycast size of the brimstone). [Default = 50, My Recommendation = 50]
- Lines Of Brimstone (purely visual) : How many lines of brimstone should render. High values add lots of lag, low values put less strain on your computer but makes brimstone look a bit strange. [Default = DEFAULT, My Recommendation = DEFAULT]
- Berserk Color Tint (purely visual) : Whether berserk applies a red tint to the weapon. [Default = true, My Recommendation = true]
- (Astral Veil) Insanity Bar Color (Purely Visual) : The color of the insanity bar. [Default = PURPLE, My Recommendation = WHITE]
- (Astral Veil) Insanity Bar Style (Purely Visual) : The style (how many notches) of the insanity bar. [Default = PROGRESS, My Recommendation = NOTCHED_6]

## Tags
- enchancment_unbound:cannot_execute : Entities in this tag cannot be executed by the Executioner enchantment. By default, this includes bosses and wardens.
- enchancment_unbound:midas_touch_immune : Entities in this tag cannot be turned into golden statues by Midas Touch. By default, this only includes astral phantoms.
- enchancment_unbound:should_hit_in_veil : Entities in this tag can be hit be players in Astral Veil and can also hit and target players in Astral Veil. By default, this includes bosses, wardens, and astral phantoms.
- enchancment_unbound:bypasses_midas_link : Damage Types in this tag will not activate Midas Link. By default, this only includes thorns to prevent an infinite damage cycle.