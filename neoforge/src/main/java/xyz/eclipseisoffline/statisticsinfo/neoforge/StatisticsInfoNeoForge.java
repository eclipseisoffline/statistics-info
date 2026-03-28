package xyz.eclipseisoffline.statisticsinfo.neoforge;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import xyz.eclipseisoffline.statisticsinfo.StatisticsInfo;

import java.util.function.BiConsumer;

@Mod(StatisticsInfo.MOD_ID)
public class StatisticsInfoNeoForge extends StatisticsInfo {

    public StatisticsInfoNeoForge() {
        initialize();
    }

    @Override
    protected void registerCommands(BiConsumer<CommandDispatcher<CommandSourceStack>, CommandBuildContext> registerer) {
        NeoForge.EVENT_BUS.addListener(RegisterCommandsEvent.class, event -> registerer.accept(event.getDispatcher(), event.getBuildContext()));
    }
}
