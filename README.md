# Statistics Info

[![Modrinth Version](https://img.shields.io/modrinth/v/udeToK1q?logo=modrinth&color=008800)](https://modrinth.com/mod/udeToK1q)
[![Modrinth Game Versions](https://img.shields.io/modrinth/game-versions/udeToK1q?logo=modrinth&color=008800)](https://modrinth.com/mod/udeToK1q)
[![Modrinth Downloads](https://img.shields.io/modrinth/dt/udeToK1q?logo=modrinth&color=008800)](https://modrinth.com/mod/udeToK1q)
[![Discord Badge](https://img.shields.io/badge/chat-discord-%235865f2)](https://discord.gg/CNNkyWRkqm)
[![Github Badge](https://img.shields.io/badge/github-statistics--info-white?logo=github)](https://github.com/eclipseisoffline/statistics-info)
![GitHub License](https://img.shields.io/github/license/eclipseisoffline/statistics-info)
![Available for Fabric](https://img.shields.io/badge/available_for-fabric-_?color=%23dbd0b4)
![Available for NeoForge](https://img.shields.io/badge/available_for-NeoForge-_?color=%23e58c53)

This mod adds a simple `/stats` command, which players can use to see their statistics, share them with other players,
and see a leaderboard of a statistic.

## License

This mod is licensed under the MIT license.

## Donating

If you like this mod, consider [donating](https://buymeacoffee.com/eclipseisoffline).

## Discord

For support and/or any questions you may have, feel free to join [my discord](https://discord.gg/CNNkyWRkqm).

## Version support

| Minecraft Version | Status       |
|-------------------|--------------|
| 26.1              | ✅ Current    |

I try to keep support up for the latest drop of Minecraft. Updates to newer Minecraft
versions may be delayed from time to time, as I do not always have the time to immediately update my mods.

Unsupported versions are still available to download, but they won't receive new features or bugfixes.

## Usage

The mod is available for both Fabric and NeoForge, the Fabric API is required on Fabric.
This mod is not required on clients when playing on multiplayer.

The mod adds a single new command, `/stats`, which can be used as follows:

- `/stats get <type> <key>`
  - Requires the `statistics_info.get` permission or operator level 2.
  - Displays a statistic for you. If you have the `statistics_info.share` permission (or operator level 2), a `[share]` button is displayed as well.
  - Pressing the `[share]` button shares your statistic with everyone online.
  - Examples:
    - `/stats get minecraft:used minecraft:torch`
      > `You have used Torch 5043 times [share]`
    - `/stats get mined stone`
      > `You have mined Stone 30213 times [share]`
- `/stats get <type> <key> <target`
  - Requires the `statistics_info.get` and `statistics_info.get.other` permissions, or operator level 2.
  - Displays a statistic of `<target>`.
  - Examples:
    - `/stats get minecraft:killed minecraft:villager Player123`
      > `Player123 has killed Villager 67 times`
    - `/stats get crafted beacon Player987`
      > `Player987 has crafted Beacon 42 times`
- `/stats top <type> <key>`
  - Requires the `statistics_info.leaderboard` permission or operator level 2.
  - Displays a leaderboard for the given statistic.
  - Examples:
    - `/stats top minecraft:picked_up minecraft:elytra`
      > `Leaderboard for picked up Elytra:`
      > `1. Player271 (198)`
      > `2. Player709 (79)`
      > `3. Player818 (36)`
      > `4. Player734 (34)`
      > `5. Player753 (23)`
    - `/stats top custom play_time`
      > `Leaderboard for Time Played:`
      > `1. Player271 (11.01 h)`
      > `2. Player709 (5.05 h)`
      > `3. Player818 (2.1 h)`
      > `4. Player734 (11.05 min)`
      > `5. Player753 (5.49 min)`
