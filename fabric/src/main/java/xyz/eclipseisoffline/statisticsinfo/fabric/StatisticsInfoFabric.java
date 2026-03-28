package xyz.eclipseisoffline.statisticsinfo.fabric;

import com.mojang.brigadier.CommandDispatcher;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import xyz.eclipseisoffline.statisticsinfo.StatisticsInfo;

import java.util.function.BiConsumer;

public class StatisticsInfoFabric extends StatisticsInfo implements ModInitializer {

    @Override
    public void onInitialize() {
        initialize();
    }

    @Override
    protected void registerCommands(BiConsumer<CommandDispatcher<CommandSourceStack>, CommandBuildContext> registerer) {
        CommandRegistrationCallback.EVENT.register((dispatcher, buildContext, _) -> registerer.accept(dispatcher, buildContext));
    }
}
