package xyz.eclipseisoffline.statisticsinfo.mixin;

import net.minecraft.ChatFormatting;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.protocol.common.ServerCommonPacketListener;
import net.minecraft.network.protocol.common.ServerboundCustomClickActionPacket;
import net.minecraft.resources.Identifier;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerCommonPacketListenerImpl;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.server.permissions.PermissionLevel;
import net.minecraft.stats.Stat;
import net.minecraft.stats.StatType;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.eclipseisoffline.commonpermissionsapi.api.CommonPermissions;
import xyz.eclipseisoffline.statisticsinfo.StatisticsInfo;
import xyz.eclipseisoffline.statisticsinfo.StatisticsInfoPermissions;

import java.util.Optional;

@Mixin(ServerCommonPacketListenerImpl.class)
public abstract class ServerCommonPacketListenerImplMixin implements ServerCommonPacketListener {

    @Shadow
    @Final
    protected MinecraftServer server;

    @Inject(method = "handleCustomClickAction", at = @At("HEAD"), cancellable = true)
    public void handleShareStatisticsAction(ServerboundCustomClickActionPacket packet, CallbackInfo callbackInfo) {
        //noinspection ConstantValue
        if (packet.id().equals(StatisticsInfo.SHARE_STATISTICS_ACTION) && (Object) this instanceof ServerGamePacketListenerImpl gamePacketListener
                && CommonPermissions.check(gamePacketListener.player, StatisticsInfoPermissions.STATISTICS_SHARE, PermissionLevel.GAMEMASTERS)) {
            callbackInfo.cancel();
            packet.payload().ifPresent(tag -> {
                if (tag instanceof CompoundTag compound) {
                    Optional<StatType<?>> optionalType = compound.getString("type")
                            .flatMap(s -> Optional.ofNullable(Identifier.tryParse(s)))
                            .flatMap(BuiltInRegistries.STAT_TYPE::getOptional);
                    //noinspection rawtypes,unchecked
                    statistics_info$broadcastStatistic(gamePacketListener.player, (Optional) optionalType, compound, "key");
                }
            });
        }
    }

    @Unique
    private <T> void statistics_info$broadcastStatistic(ServerPlayer player, Optional<StatType<T>> statType, CompoundTag compound, String key) {
        Optional<Stat<T>> statKey = statType.flatMap(type -> compound.getString(key)
                .flatMap(s -> Optional.ofNullable(Identifier.tryParse(s)))
                .flatMap(identifier -> type.getRegistry().getOptional(identifier))
                .map(type::get));

        statKey.ifPresent(stat -> {
            MutableComponent message = Component.empty().withStyle(ChatFormatting.ITALIC);
            message.append(player.getDisplayName().copy());
            message.append(" has ");
            message.append(StatisticsInfo.formatStatisticInfoForDisplay(player, stat));
            server.getPlayerList().broadcastSystemMessage(message, false);
        });
    }
}
