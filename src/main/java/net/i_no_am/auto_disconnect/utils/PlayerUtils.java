package net.i_no_am.auto_disconnect.utils;

import net.i_no_am.auto_disconnect.client.Global;
import net.minecraft.block.BlockState;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.BiConsumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class PlayerUtils implements Global {

/*Ported from https://github.com/clickcrystals-development/ClickCrystals/blob/main/src/main/java/io/github/itzispyder/clickcrystals/util/minecraft/PlayerUtils.java */

    public static boolean invalid() {
        return mc.player == null;
    }

    public static boolean valid() {
        return !invalid();
    }

    public static ClientPlayerEntity player() {
        return mc.player;
    }

    public static void sendText(Text text) {
        if (text == null) return;
        player().sendMessage(Text.of(text), false);
    }

    public static World getWorld() {
        return Objects.requireNonNull(player()).getWorld();
    }

    public static void boxIterator(World world, Box box, BiConsumer<BlockPos, BlockState> function) {
        for (double x = box.minX; x <= box.maxX; x++) {
            for (double y = box.minY; y <= box.maxY; y++) {
                for (double z = box.minZ; z <= box.maxZ; z++) {
                    BlockPos pos = new BlockPos((int) Math.floor(x), (int) Math.floor(y), (int) Math.floor(z));
                    BlockState state = world.getBlockState(pos);

                    if (state == null || state.isAir()) {
                        continue;
                    }
                    function.accept(pos, state);
                }
            }
        }
    }

    public static Entity getNearestEntity(World world, Entity exclude, Vec3d at, double range, Predicate<Entity> filter) {
        List<Entity> candidates = world.getOtherEntities(exclude, Box.from(at).expand(range), filter).stream()
                .sorted(Comparator.comparing(entity -> entity.getPos().distanceTo(at)))
                .toList();

        if (candidates.isEmpty()) {
            return null;
        }
        return candidates.getFirst();
    }

    public static Entity getNearestEntity(double range, Predicate<Entity> filter) {
        if (invalid()) return null;
        return getNearestEntity(getWorld(), player(), player().getPos(), range, filter);
    }

    public static BlockPos getNearestBlock(double range, Predicate<BlockState> filter) {
        if (invalid()) {
            return null;
        }

        AtomicReference<Double> nearestDist = new AtomicReference<>(range);
        AtomicReference<BlockPos> nearestPos = new AtomicReference<>();
        Box box = player().getBoundingBox().expand(range);
        Vec3d playerPos = player().getPos();
        World world = getWorld();

        boxIterator(world, box, (pos, state) -> {
            if (filter.test(state) && pos.isWithinDistance(playerPos, nearestDist.get())) {
                double distance = Math.sqrt(pos.getSquaredDistance(playerPos));
                if (distance < nearestDist.get()) {
                    nearestDist.set(distance);
                    nearestPos.set(pos);
                }
            }
        });
        return nearestPos.get();
    }

    public static List<Integer> getRangeList(int maxRange) {
        return IntStream.rangeClosed(1,maxRange)
                .boxed()
                .collect(Collectors.toList());
    }
}
