package xyz.eclipseisoffline.statisticsinfo;

import com.mojang.datafixers.util.Pair;
import net.minecraft.stats.Stat;

import java.util.List;
import java.util.UUID;

public interface ServerStatsManager {

    void statistics_info$ensureAllStatisticDataIsLoaded();

    List<Pair<UUID, Integer>> statistics_info$getLeaderboard(Stat<?> stat);
}
