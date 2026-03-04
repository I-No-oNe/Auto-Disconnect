package net.i_no_am.auto_disconnect.mixin;

import com.mojang.authlib.GameProfile;
import net.i_no_am.auto_disconnect.Global;
import net.i_no_am.auto_disconnect.config.Config;
import net.i_no_am.auto_disconnect.utils.Utils;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.entity.EntityType;
import net.minecraft.network.packet.s2c.play.GameJoinS2CPacket;
import net.minecraft.network.packet.s2c.play.PlayerListS2CPacket;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Mixin(ClientPlayNetworkHandler.class)
public abstract class MixinClientPlayNetworkHandler implements Global {

    @Unique private final Set<UUID> knownPlayerIds = new HashSet<>();

    @Inject(method = "onPlayerList", at = @At("TAIL"))
    private void onListUpdate(PlayerListS2CPacket packet, CallbackInfo ci) {
        if (!Config.enable) return;

        for (PlayerListS2CPacket.Entry entry : packet.getEntries()) {
            GameProfile profile = entry.profile();
            if (profile == null) continue;
            UUID uuid = profile.id();

            // Skip self
            if (mc.player != null && uuid.equals(mc.player.getUuid())) continue;

            String playerName = profile.name();

            if (Config.checkNewPlayers && !knownPlayerIds.contains(uuid)) {
                knownPlayerIds.add(uuid);
                Utils.disconnect("New player joined: " + playerName);
                return;
            }

            if (Utils.isPlayerInList(playerName)) {
                Utils.disconnect("Blocked player joined: " + playerName);
                return;
            }

            knownPlayerIds.add(uuid);
        }
    }

    @Inject(method = "onGameJoin", at = @At("TAIL"))
    private void onGameJoin(GameJoinS2CPacket packet, CallbackInfo ci) {
        knownPlayerIds.clear();
    }

    @Inject(method = "tick", at = @At("RETURN"))
    private void onTick(CallbackInfo ci) {

        if (Utils.isInvalid() || !Config.enable || !Config.checkPlayersInRenderDistance) return;

        Vec3d selfPos = mc.player.getEntityPos();
        double rangeSquared = Config.range * Config.range;

        var player = Utils.findEntity(EntityType.PLAYER, Config.range);
        double distSq = player != mc.player ? player.getEntityPos().squaredDistanceTo(selfPos) : Double.MAX_VALUE;

        if (distSq <= rangeSquared) {
            Utils.disconnect("Player in render range");
        }
    }
}
