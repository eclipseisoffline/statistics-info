package xyz.eclipseisoffline.statisticsinfo;

import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.ResourceArgument;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.stats.StatType;

public class StatTypeArgument {

    public static RequiredArgumentBuilder<CommandSourceStack, Holder.Reference<StatType<?>>> statType(String name, CommandBuildContext buildContext) {
        return Commands.argument(name, ResourceArgument.resource(buildContext, Registries.STAT_TYPE));
    }

    public static Holder.Reference<StatType<?>> getStatType(CommandContext<CommandSourceStack> context, String name) throws CommandSyntaxException {
        return ResourceArgument.getResource(context, name, Registries.STAT_TYPE);
    }
}
