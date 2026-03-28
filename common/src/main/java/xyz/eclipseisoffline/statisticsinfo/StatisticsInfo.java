package xyz.eclipseisoffline.statisticsinfo;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.datafixers.util.Pair;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.NameAndId;
import net.minecraft.stats.Stat;
import net.minecraft.stats.StatType;
import net.minecraft.stats.Stats;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.function.BiConsumer;

public abstract class StatisticsInfo {
    public static final String MOD_ID = "statistics_info";
    public static final Identifier SHARE_STATISTICS_ACTION = getModdedIdentifier("share_statistics");
    private static final Map<StatType<?>, String> STAT_VERBS = Map.of(
            Stats.BLOCK_MINED, "mined",
            Stats.ITEM_CRAFTED, "crafted",
            Stats.ITEM_USED, "used",
            Stats.ITEM_BROKEN, "broken",
            Stats.ITEM_PICKED_UP, "picked up",
            Stats.ITEM_DROPPED, "dropped",
            Stats.ENTITY_KILLED, "killed",
            Stats.ENTITY_KILLED_BY, "been killed by"
    );

    public void initialize() {
        registerCommands((dispatcher, buildContext) -> {
            dispatcher.register(Commands.literal("stats")
                    .then(Commands.literal("get")
                            .then(StatTypeArgument.statType("type", buildContext)
                                    .then(StatKeyArgument.statKey("key", context -> StatTypeArgument.getStatType(context, "type").value())
                                            .executes(context -> {
                                                ServerPlayer player = context.getSource().getPlayerOrException();
                                                StatType<?> type = StatTypeArgument.getStatType(context, "type").value();
                                                Stat<?> stat = StatKeyArgument.getStatKey(context, "key", type);
                                                int value = player.getStats().getValue(stat);

                                                CompoundTag shareInfo = new CompoundTag();
                                                shareInfo.putString("type", Objects.requireNonNull(BuiltInRegistries.STAT_TYPE.getKey(type)).toString());
                                                shareInfo.putString("key", getStatIdentifier(stat).toString());

                                                MutableComponent feedback = Component.literal("You have ").append(formatStatisticInfoForDisplay(player, stat)).append(" ");
                                                feedback.append(Component.literal("[share]")
                                                        .withStyle(style -> style.withClickEvent(new ClickEvent.Custom(SHARE_STATISTICS_ACTION, Optional.of(shareInfo)))));

                                                context.getSource().sendSuccess(() -> feedback, false);
                                                return value;
                                            })
                                    )
                            )
                    )
                    .then(Commands.literal("top")
                            .then(StatTypeArgument.statType("type", buildContext)
                                    .then(StatKeyArgument.statKey("key", context -> StatTypeArgument.getStatType(context, "type").value())
                                            .executes(context -> {
                                                StatType<?> type = StatTypeArgument.getStatType(context, "type").value();
                                                Stat<?> stat = StatKeyArgument.getStatKey(context, "key", type);
                                                List<Pair<UUID, Integer>> leaderboard = ((ServerStatsManager) context.getSource().getServer().getPlayerList()).statistics_info$getLeaderboard(stat);

                                                context.getSource().sendSuccess(() -> Component.literal("Leaderboard for ").append(formatStatisticNameForDisplay(stat)).append(":"), false);

                                                for (int i = 0; i < Math.min(5, leaderboard.size()); i++) {
                                                    Pair<UUID, Integer> entry = leaderboard.get(i);
                                                    String playerName = context.getSource().getServer().services().nameToIdCache().get(entry.getFirst())
                                                            .map(NameAndId::name)
                                                            .orElseGet(() -> entry.getFirst().toString());
                                                    Component leaderboardLine = Component.literal(i + 1 + ". " + playerName + " (" + stat.format(entry.getSecond()) + ")");
                                                    context.getSource().sendSuccess(() -> leaderboardLine, false);
                                                }

                                                return 0;
                                            })
                                    )
                            )
                    )
            );
        });
    }

    protected abstract void registerCommands(BiConsumer<CommandDispatcher<CommandSourceStack>, CommandBuildContext> registerer);

    public static Component formatStatisticNameForDisplay(Stat<?> stat) {
        if (stat.getType() == Stats.CUSTOM) {
            return getStatKeyDisplay(stat);
        } else {
            String verb = STAT_VERBS.getOrDefault(stat.getType(), stat.getType().getDisplayName().getString());
            return Component.literal(verb + " ").append(getStatKeyDisplay(stat));
        }
    }

    public static Component formatStatisticInfoForDisplay(ServerPlayer player, Stat<?> stat) {
        int value = player.getStats().getValue(stat);

        if (stat.getType() == Stats.CUSTOM) {
            return Component.literal(stat.format(value) + " of ").append(getStatKeyDisplay(stat));
        } else {
            String verb = STAT_VERBS.getOrDefault(stat.getType(), stat.getType().getDisplayName().getString());
            return Component.literal(verb + " ").append(getStatKeyDisplay(stat)).append(" " + stat.format(value) + " times");
        }
    }

    public static Component getStatKeyDisplay(Stat<?> stat) {
        if (stat.getType() == Stats.CUSTOM) {
            return Component.translatable("stat." + stat.getValue().toString().replace(':', '.'));
        } else if (stat.getType() == Stats.BLOCK_MINED) {
            return ((Block) stat.getValue()).getName();
        } else if (stat.getType() == Stats.ENTITY_KILLED || stat.getType() == Stats.ENTITY_KILLED_BY) {
            return ((EntityType<?>) stat.getValue()).getDescription();
        }

        Identifier statKey = getStatIdentifier(stat);
        if (stat.getType() == Stats.ITEM_CRAFTED || stat.getType() == Stats.ITEM_USED || stat.getType() == Stats.ITEM_BROKEN
                || stat.getType() == Stats.ITEM_PICKED_UP || stat.getType() == Stats.ITEM_DROPPED) {
            return ((Item) stat.getValue()).components().getOrDefault(DataComponents.ITEM_NAME, Component.literal(statKey.toString()));
        }
        return Component.literal(statKey.toString());
    }

    public static <T> Identifier getStatIdentifier(Stat<T> stat) {
        return Objects.requireNonNull(stat.getType().getRegistry().getKey(stat.getValue()));
    }

    public static Identifier getModdedIdentifier(String path) {
        return Identifier.fromNamespaceAndPath(MOD_ID, path);
    }
}
