package xyz.eclipseisoffline.statisticsinfo;

import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.Dynamic2CommandExceptionType;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.commands.arguments.IdentifierArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.stats.Stat;
import net.minecraft.stats.StatType;

public class StatKeyArgument {
    private static final Dynamic2CommandExceptionType ERROR_UNKNOWN_STAT_KEY = new Dynamic2CommandExceptionType(
            (statType, statKey) -> Component.literal("Unknown stat key " + statKey + " for type ").append(((StatType<?>) statType).getDisplayName()));

    public static RequiredArgumentBuilder<CommandSourceStack, ?> statKey(String name, StatTypeGetter typeGetter) {
        return Commands.argument(name, IdentifierArgument.id())
                .suggests((context, builder) -> {
                    StatType<?> statType = typeGetter.getStatType(context);
                    return SharedSuggestionProvider.listSuggestions(context, builder, statType.getRegistry().key(), SharedSuggestionProvider.ElementSuggestionType.ELEMENTS);
                });
    }

    public static <T> Stat<T> getStatKey(CommandContext<CommandSourceStack> context, String name, StatType<T> type) throws CommandSyntaxException {
        Identifier identifier = IdentifierArgument.getId(context, name);
        return type.get(type.getRegistry().getOptional(identifier).orElseThrow(() -> ERROR_UNKNOWN_STAT_KEY.create(type, identifier)));
    }

    @FunctionalInterface
    public interface StatTypeGetter {

        StatType<?> getStatType(CommandContext<CommandSourceStack> context) throws CommandSyntaxException;
    }
}
