package xyz.eclipseisoffline.statisticsinfo.mixin;

import com.mojang.datafixers.util.Pair;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.players.PlayerList;
import net.minecraft.stats.ServerStatsCounter;
import net.minecraft.stats.Stat;
import net.minecraft.world.level.storage.LevelResource;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import xyz.eclipseisoffline.statisticsinfo.ServerStatsManager;

import java.io.File;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Mixin(PlayerList.class)
public abstract class PlayerListMixin implements ServerStatsManager {

    @Shadow
    @Final
    private MinecraftServer server;

    @Shadow
    @Final
    private Map<UUID, ServerStatsCounter> stats;

    @Override
    public void statistics_info$ensureAllStatisticDataIsLoaded() {
        File statsDir = server.getWorldPath(LevelResource.PLAYER_STATS_DIR).toFile();

        File[] statsFiles = statsDir.listFiles();
        if (statsFiles != null) {
            for (File statsFile : statsFiles) {
                UUID uuid = UUID.fromString(statsFile.getName().replace(".json", ""));
                if (!stats.containsKey(uuid)) {
                    stats.put(uuid, new ServerStatsCounter(server, statsFile.toPath()));
                }
            }
        }
    }

    @Override
    public List<Pair<UUID, Integer>> statistics_info$getLeaderboard(Stat<?> stat) {
        statistics_info$ensureAllStatisticDataIsLoaded();
        return stats.entrySet().stream()
                .map(entry -> Pair.of(entry.getKey(), entry.getValue().getValue(stat)))
                .sorted(Comparator.<Pair<UUID, Integer>>comparingInt(Pair::getSecond).reversed())
                .toList();
    }
}
