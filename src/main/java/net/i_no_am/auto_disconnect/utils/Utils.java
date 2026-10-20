package net.i_no_am.auto_disconnect.utils;

import net.i_no_am.auto_disconnect.Global;
import net.i_no_am.auto_disconnect.config.Config;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.protocol.game.ServerboundSetCarriedItemPacket;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RespawnAnchorBlock;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.List;


public class Utils implements Global {
    /**
     * Checks if the current game state is invalid for execution.
     */
    public static boolean isInvalid() {
        return mc.player == null || mc.player.connection == null || mc.level == null;
    }

    /**
     * Disconnects the player with a given reason.
     */
    public static void disconnect(String reason) {
        if (!Config.enable && Config.autoDisable || Utils.isInvalid()) return;
        MutableComponent disconnectText = Component.literal("§3Auto-Disconnect was triggered§r").append(Component.literal("\n\n" + reason).withStyle(ChatFormatting.RED));
        // Send fake slot update before disconnecting (used as a workaround so we will disconnect immediately)
        mc.player.connection.getConnection().send(new ServerboundSetCarriedItemPacket(228));
        mc.player.connection.getConnection().disconnect(disconnectText);
        if (Config.autoDisable) Config.enable = false;
        else if (isDev) System.out.println(PREFIX + ": disconnected because " + reason);
    }

    /**
     * Checks if a respawn anchor at the given block position has the specified number of charges.
     */
    public static boolean isAnchorLoaded(int charges, BlockPos pos) {
        var state = mc.player.level().getBlockState(pos);
        if (state.getBlock().equals(Blocks.RESPAWN_ANCHOR)) return state.getValue(RespawnAnchorBlock.CHARGE) == charges;
        return isAnchorLoaded(charges, pos);
    }

    /**
     * find a specific entity in a certain range from the player.
     */
    public static <T extends Entity> T findEntity(EntityType<T> entityType, int range) {
        for (Entity e : getEntitiesInRange(range)) {
            if (e.getType() == entityType) {
                return (T) e;
            }
        }
        return null;
    }

    /**
     * Get entity instances in a certain range from the player.
     */
    private static <T extends Entity> List<T> getEntitiesInRange(int range) {
        AABB box = mc.player.getBoundingBox().inflate(range);
        return mc.level.getEntitiesOfClass((Class<T>) Entity.class, box, entity -> true).stream().toList();
    }

    /**
     * get blocks in a certain range from the player.
     */
    private static BlockPos[] getBlocksInRange(int range) {
        if (range < 0) throw new IllegalArgumentException("Range cannot be negative");
        BlockPos playerPos = mc.player.blockPosition();
        BlockPos[] blocks = new BlockPos[(range * 2 + 1) * (range * 2 + 1) * (range * 2 + 1)];
        int index = 0;
        for (int x = -range; x <= range; x++) {
            for (int y = -range; y <= range; y++) {
                for (int z = -range; z <= range; z++) {
                    blocks[index++] = playerPos.offset(x, y, z);
                }
            }
        }
        return blocks;
    }

    /**
     * Finds a block of the specified type within a certain range from the player.
     */
    public static BlockPos findBlockPos(Block block, int range) {
        for (BlockPos pos : getBlocksInRange(range)) {
            if (mc.level.getBlockState(pos).getBlock() == block) return pos;
        }
        return new BlockPos(0, 0, 0);
    }

    /**
     * Checks if a playerUserName is the same as in a list.
     */
    public static boolean isPlayerInList(String playerUserName) {
        for (String name : Config.playersName)
            return (name.equalsIgnoreCase(playerUserName.toLowerCase()) && Config.checkPlayersInList);
        return false;
    }
}
