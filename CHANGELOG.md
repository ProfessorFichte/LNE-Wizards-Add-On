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

- Thanks to Daedelus for the PR!
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
