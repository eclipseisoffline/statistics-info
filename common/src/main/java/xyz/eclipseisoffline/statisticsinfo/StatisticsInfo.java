package xyz.eclipseisoffline.statisticsinfo;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.IdentifierArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.stats.Stat;
import net.minecraft.stats.StatType;
import net.minecraft.stats.Stats;

import java.util.Map;
import java.util.function.BiConsumer;

public abstract class StatisticsInfo {
    private static final Map<StatType<?>, String> STAT_VERBS = Map.of(
            Stats.BLOCK_MINED, "mined",
            Stats.ITEM_CRAFTED, "crafted",
            Stats.ITEM_USED, "used",
            Stats.ITEM_BROKEN, "broken",
            Stats.ITEM_PICKED_UP, "picked up",
            Stats.ITEM_DROPPED, "dropped",
            Stats.ENTITY_KILLED, "killed",
            Stats.ENTITY_KILLED_BY, "killed by"
    );

    public void initialize() {
        registerCommands((dispatcher, buildContext) -> {
            dispatcher.register(Commands.literal("stats")
                    .then(Commands.literal("get")
                            .then(StatTypeArgument.statType("type", buildContext)
                                    .then(StatKeyArgument.statKey("key", context -> StatTypeArgument.getStatType(context, "type").value())
                                            .executes(context -> {
                                                StatType<?> type = StatTypeArgument.getStatType(context, "type").value();
                                                Identifier statKey = IdentifierArgument.getId(context, "key");
                                                Stat<?> stat = StatKeyArgument.getStatKey(context, "key", type);
                                                int value = context.getSource().getPlayerOrException().getStats().getValue(stat);

                                                if (type == Stats.CUSTOM) {
                                                    context.getSource().sendSuccess(() -> Component.literal("Value for " + statKey + " is " + stat.format(value)), false);
                                                } else {
                                                    String verb = STAT_VERBS.getOrDefault(type, type.getDisplayName().getString());
                                                    context.getSource().sendSuccess(() -> Component.literal("You have " + verb + " " + statKey + " " + stat.format(value) + " times"), false);
                                                }

                                                return value;
                                            })
                                    )
                            )
                    )
                    .then(Commands.literal("top")
                    )
            );
        });
    }

    protected abstract void registerCommands(BiConsumer<CommandDispatcher<CommandSourceStack>, CommandBuildContext> registerer);
}
