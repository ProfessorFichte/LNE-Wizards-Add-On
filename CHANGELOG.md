# 1.2.1+1.20.1

> ### ⚠️ Read this before updating
>
> This release is a **major technical overhaul and is not backwards compatible.**
>
> - **Requires the matching Spell Engine and More RPG Library releases.** This version will not run on
>   Spell Engine **0.9.x**, and mods built against 0.9.x will not work alongside it.
> - **Update the whole set together.** Spell Engine, More RPG Library and every RPG Series mod must be on
>   matching versions. Mixing in an older add-on will break at startup or misbehave in play.
>
> **Back up your world before updating.**

- Ported to Minecraft 1.20.1 (Fabric + Forge 47). NeoForge is replaced by Forge on this line; the same
  Forge jar also loads on NeoForge 1.20.1.
- Requires the matching 1.20.1 releases of Spell Engine (1.10.5), Spell Power (1.6.0), More RPG Library
  (2.7.2) and Wizards (3.1.2). Loot & Explore and Elemental Wizards stay optional, as before.
- Every registry write goes through Forge's `RegisterEvent` window, so the mod also boots on Forge 47.0-47.3
  and on NeoForge 1.20.1, which never unlock the vanilla registries.

### Accepted 1.20.1 limitations

- Loot & Explore has no Forge build on this version line, so the six elemental staves and their smithing
  recipes only exist on Fabric. Everything else - the six elemental evokers, the magic orb blocks, the
  spells and all 24 structures - works on both loaders.
- Wind charges are 1.20.5 content, so the four wind loot-chest tiers and the Air Evoker's drops give
  feathers instead. A balance change, not just a substitution.
- Rune pouches never drop on this line: Runes gates them on Bundle API, which has no 1.20.1 build. Those
  loot entries simply expand to nothing.
- One of the two earth tower variants (`wizards_earth_tower_1`) is built from the tuff block family, which
  does not exist on 1.20.1 and generates as air. Roughly half of all earth towers therefore have holes in
  them until the palette is reworked.
- The evokers placed by a structure template no longer carry the saved attribute *modifiers* from the
  template file. They still spawn with their configured stats, which the mod applies at spawn time.

# 1.2.1 - 1.21.1
- Drop Forgified Fabric API (FFAPI) as a required dependency

# 1.2.0 - 1.21.1
- Adopt Spell Engine 1.10 - Thanks Daedelus for the PR!

# 1.1.4 - 1.21.1
- fixed the issue, where Elemental Wizards is not installed but the spell books get created with only the additional spells
- Converted Explosive Bubbles to a Spell Cloud instead of a custom Entity
- Flamerush now uses the Velocity Impact of Spell Engine for the Dash

# 1.1.3 - 1.21.1
- Adapt to Spell Engine 1.9.10+ API Changes
**Balancing & Internal Changes:**
- Elemental Evokers can now also use the Tier 2 Spell Expansion Wizard Spells
- nerfed default spell power from elemental evokers from 6.0 -> 2.5
- changed casting spell pool-spell tag files & structure from the elemental evokers
- Removed Updraft and added Aeroblast to the AirEvoker's secondary spell pool
- Added a new magical orb blocks that can be found in LNE Wizard's Structures!
- They provide you a long-lasting buff for the according spell power
- Add the Elemental Evokers to the "illager" Entity Type Tag

# 1.1.2 - 1.21.1
- Elemental Evokers stopped casting spells and ran away when they got hit, this was fixed
- Overall improved the fleeing goals of the Elemental Evokers, to stay in safe distance from the attacker

# 1.1.1 - 1.21.1
- fixed crash due to elemental evokers ticking
- added the damage number tooltip to the Aeroburst Spell

# 1.1.0 - 1.21.1
**Update to use Spell Engine 1.9.0**
- DISCLAIMER: All spell books and spell scrolls will be reset, due to major API changes.
- Ray of Frost, Flamerush & Falling Star are now Tier 5 Spells, they got slightly buffed
- These Spells can now also be learned in the Spell Binding Table
- Moved the Spells Aeroblast & Explosive Bubbles from Elemental Wizards to this Mod
- Reworked Explosive Bubbles, its now a channeled spell, that spawns Bubble Entities that Explode on Impact
- Moved all the structures from loot_n_explore to lne_wizards, so it's clearer that these structures come from this add-on
**New Content**
- Added a new T5 Extra Spell for the Earth Wizard: Rock Crash (A Giant Meteor Projectile that crashes in the ground)
- Added 6x Elemental Evoker MobEntities
- They are able to cast two Spells from the Wizards and Elemental Wizards Weapons & Spell Books according to their element
- You now have to fight them, in the LNE-Wizard themed structures!
- The primary spell that these evokers can cast are hard-coded weapon skills from the elemental wands & staffs
- What other secondary spells these evokers can cast is fully configurable with spell tags: "resources\data\lne_wizards\tags\spell\mob\evoker\"
- These entities also drop materials for their element's magic runes, they also have a chance to drop spell scrolls from their element
- For technical info how these spell casting mobs work, take a look at the More RPG Library Readme!
- The Evokers can also spawn in Raid's, this can be turned off in the tweaks config

# 1.0.8 - 1.21.1
- Update Staff Spell Power

# 1.0.7 - 1.21.1
- Move to Architectury Enviroment for Multiloader
- NeoForge Beta!
- Add 1 new Earth Wizard Tower Variant (Builder: Maxi - Thanks!)
- Air Wizard Tower is no longer a structure that spawns in the air
- moved the air wizard tower to the Jungle Biome and let it spawn on the ground

# 1.0.6 - 1.21.1
- Fix Seismic Staff Wizard Item Staff tag
- Add LNE Staves to LNE Weapon Theme Item Tag

# 1.0.5 - 1.21.1
- Add Spell Datagen
- Change Flame Rush Spell
- it's now a small dash that leaves a damaging flame cloud trail, also you still receive the buff
- improved Flame Rush's Spell Texture
- use new loot function for loot chests: "more_rpg_classes:specific_spell_scroll_pool"
- Ray of Frost now can stack the Frosted until the target is Frozen Solid 
- Increase Arcane Starfall Area Impact Range & slightly increase damage

# 1.0.4 - 1.21.1
- Flame Rush, Ray of Frost & Starfall are now a T4 spell
- they can now also be looted outside the classes structure

# 1.0.3 - 1.21.1
- Spell Engine 1.7
- Flame Rush Effect now does not burn nearby enemies (was too unbalanced)

# 1.0.2 - 1.21.1
- Update License
- Update Mod Icon

# 1.0.1 - 1.21.1
- fix some recipes
- pyromancers training Passive was renamed to Pyromaniac
- The Passive was changed to attacking burning targets, the chance was reduced, cooldown reduction nerfed, but additional damage was added
- Flamerush was changed, it now also damages and burns targets on release, cast time was nerfed

# 1.0.0 - 1.21.1
# Official 1.21.1 Release!
# CHANGES
- Passive Spells For the Weapons are now handled with the new Spell Engine Passive API
- The Class related structures will now contain spell scrolls in their loot chests
- Made some small loot table tweaks
- changed all item staff ids
- Some Passive got completely new functions than their 1.20.1 versions