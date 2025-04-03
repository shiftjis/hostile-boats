package com.github.shiftjis.hostileboats.entity;

import com.github.shiftjis.hostileboats.entity.controls.BoatBodyControl;
import com.github.shiftjis.hostileboats.entity.controls.BoatLookControl;
import com.github.shiftjis.hostileboats.entity.controls.BoatMoveControl;
import com.github.shiftjis.hostileboats.entity.goals.CircleMovementGoal;
import com.github.shiftjis.hostileboats.entity.goals.FindTargetGoal;
import com.github.shiftjis.hostileboats.entity.goals.StartAttackGoal;
import com.github.shiftjis.hostileboats.entity.goals.SwoopMovementGoal;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.control.BodyControl;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.FlyingEntity;
import net.minecraft.entity.mob.Monster;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

// reference: net.minecraft.entity.mob.PhantomEntity
public class HostileBoatEntity extends FlyingEntity implements Monster {
    private static final TrackedData<Boolean> RIGHT_PADDLE_MOVING;
    private static final TrackedData<Boolean> LEFT_PADDLE_MOVING;

    static {
        RIGHT_PADDLE_MOVING = DataTracker.registerData(HostileBoatEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
        LEFT_PADDLE_MOVING = DataTracker.registerData(HostileBoatEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    }

    private final float[] paddlePhases = new float[2];
    public BoatMovementType movementType = BoatMovementType.CIRCLE;
    public BlockPos circlingCenter = BlockPos.ORIGIN;
    public Random boatRandom = Random.create();
    public Vec3d targetPosition = Vec3d.ZERO;

    public HostileBoatEntity(EntityType<? extends HostileBoatEntity> entityType, World world) {
        super(entityType, world);
        this.moveControl = new BoatMoveControl(this);
        this.lookControl = new BoatLookControl(this);
    }

    @Override
    public @Nullable EntityData initialize(ServerWorldAccess world, LocalDifficulty difficulty, SpawnReason spawnReason, @Nullable EntityData entityData, @Nullable NbtCompound entityNbt) {
        circlingCenter = getBlockPos().up(5);
        return super.initialize(world, difficulty, spawnReason, entityData, entityNbt);
    }

    @Override
    protected void initDataTracker() {
        super.initDataTracker();
        dataTracker.startTracking(RIGHT_PADDLE_MOVING, false);
        dataTracker.startTracking(LEFT_PADDLE_MOVING, false);
    }

    @Override
    protected void initGoals() {
        goalSelector.add(1, new StartAttackGoal(this));
        goalSelector.add(2, new SwoopMovementGoal(this));
        goalSelector.add(3, new CircleMovementGoal(this));
        targetSelector.add(1, new FindTargetGoal(this));
    }

    @Override
    public void tick() {
        super.tick();
        for (int index = 0; index <= 1; ++index) {
            if (isPaddleMoving(index)) {
                paddlePhases[index] += (float) Math.PI / 8;
            } else {
                paddlePhases[index] = 0.0F;
            }
        }
    }

    @Override
    protected float getActiveEyeHeight(EntityPose pose, EntityDimensions dimensions) {
        return dimensions.height * 0.35F;
    }

    @Override
    public EntityDimensions getDimensions(EntityPose pose) {
        EntityDimensions dimensions = super.getDimensions(pose);
        return dimensions.scaled((dimensions.width + 0.2F) / dimensions.width);
    }

    @Override
    protected BodyControl createBodyControl() {
        return new BoatBodyControl(this);
    }

    @Override
    public @Nullable LivingEntity getControllingPassenger() {
        return null;
    }

    @Override
    protected boolean isDisallowedInPeaceful() {
        return true;
    }

//    @Override
//    public boolean collidesWith(Entity other) {
//        return (other.isCollidable() || other.isPushable()) && !isConnectedThroughVehicle(other);
//    }

//    @Override
//    public boolean isCollidable() {
//        return true;
//    }

    public float interpolatePaddlePhase(int paddle, float tickDelta) {
        return isPaddleMoving(paddle) ? MathHelper.clampedLerp(paddlePhases[paddle] - ((float) Math.PI / 8F), paddlePhases[paddle], tickDelta) : 0.0F;
    }

    public void setPaddleMovings(boolean leftMoving, boolean rightMoving) {
        dataTracker.set(LEFT_PADDLE_MOVING, leftMoving);
        dataTracker.set(RIGHT_PADDLE_MOVING, rightMoving);
    }

    public boolean isPaddleMoving(int paddle) {
        return dataTracker.get(paddle == 0 ? LEFT_PADDLE_MOVING : RIGHT_PADDLE_MOVING);
    }

    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        if (nbt.contains("AX")) {
            circlingCenter = new BlockPos(nbt.getInt("AX"), nbt.getInt("AY"), nbt.getInt("AZ"));
        }
    }

    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        nbt.putInt("AX", circlingCenter.getX());
        nbt.putInt("AY", circlingCenter.getY());
        nbt.putInt("AZ", circlingCenter.getZ());
    }

    public enum BoatMovementType {
        CIRCLE, SWOOP
    }
}
