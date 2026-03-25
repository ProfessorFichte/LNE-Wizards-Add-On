# 1.1.0 - 1.21.1
**Update to use Spell Engine 1.9.0**
- DISCLAIMER: All spell books and spell scrolls will be reset, due to major API changes.
- Ray of Frost, Flamerush & Falling Star are now Tier 5 Spells, they got slightly buffed
- These Spells can now also be learned in the Spell Binding Table
- Moved the Spells Aeroblast & Explosive Bubbles from Elemental Wizards to this Mod
- Reworked Explosive Bubbles, it now spawns Entities that Explode on Impact
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