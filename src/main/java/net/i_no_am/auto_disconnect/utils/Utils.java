package net.i_no_am.auto_disconnect.utils;

import net.i_no_am.auto_disconnect.Global;
import net.i_no_am.auto_disconnect.config.Config;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.RespawnAnchorBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.network.packet.c2s.play.UpdateSelectedSlotC2SPacket;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Colors;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3i;

import java.util.List;


public class Utils implements Global {
    /**
     * Checks if the current game state is invalid for execution.
     */
    public static boolean isInvalid() {
        return mc.player == null || mc.player.networkHandler == null || mc.world == null;
    }

    /**
     * Disconnects the player with a given reason.
     */
    public static void disconnect(String reason) {
        if (!Config.enable && Config.autoDisable || Utils.isInvalid()) return;
        MutableText disconnectText = Text.literal("§3Auto-Disconnect was triggered§r").append(Text.literal("\n\n" + reason).styled(style -> style.withColor(Colors.RED)));
        // Send fake slot update before disconnecting (used as a workaround so we will disconnect immediately)
        mc.getNetworkHandler().sendPacket(new UpdateSelectedSlotC2SPacket(228));
        mc.player.networkHandler.getConnection().disconnect(disconnectText);
        if (Config.autoDisable) Config.enable = false;
        else if (isDev) System.out.println(PREFIX + ": disconnected because " + reason);
    }

    /**
     * Checks if a respawn anchor at the given block position has the specified number of charges.
     */
    public static boolean isAnchorLoaded(int charges, BlockPos pos) {
        var state = mc.player.getEntityWorld().getBlockState(pos);
        if (state.getBlock().equals(Blocks.RESPAWN_ANCHOR)) return state.get(RespawnAnchorBlock.CHARGES) == charges;
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
        Box box = mc.player.getBoundingBox().expand(range);
        return mc.world.getEntitiesByClass((Class<T>) Entity.class, box, entity -> true).stream().toList();
    }

    /**
     * get blocks in a certain range from the player.
     */
    private static BlockPos[] getBlocksInRange(int range) {
        if (range < 0) throw new IllegalArgumentException("Range cannot be negative");
        BlockPos playerPos = mc.player.getBlockPos();
        BlockPos[] blocks = new BlockPos[(range * 2 + 1) * (range * 2 + 1) * (range * 2 + 1)];
        int index = 0;
        for (int x = -range; x <= range; x++) {
            for (int y = -range; y <= range; y++) {
                for (int z = -range; z <= range; z++) {
                    blocks[index++] = playerPos.add(x, y, z);
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
            if (mc.world.getBlockState(pos).getBlock() == block) return pos;
        }
        return new BlockPos(Vec3i.ZERO);
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