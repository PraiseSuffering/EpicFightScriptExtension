package yesman.epicfight.api.animation.types;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import yesman.epicfight.api.animation.*;
import yesman.epicfight.api.animation.property.AnimationProperty;
import yesman.epicfight.api.client.animation.JointMask.BindModifier;
import yesman.epicfight.config.ConfigurationIngame;
import yesman.epicfight.gameasset.Animations;
import yesman.epicfight.world.capabilities.entitypatch.LivingEntityPatch;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public abstract class DynamicAnimation {
	public Map<String, TransformSheet> jointTransforms;
	public final boolean isRepeat;
	public final float convertTime;
	public float totalTime = 0.0F;
	
	public DynamicAnimation() {
		this(ConfigurationIngame.GENERAL_ANIMATION_CONVERT_TIME, false);
	}
	
	public DynamicAnimation(float convertTime, boolean isRepeat) {
		this.jointTransforms = new HashMap<String, TransformSheet>();
		this.isRepeat = isRepeat;
		this.convertTime = convertTime;
	}
	
	public void addSheet(String jointName, TransformSheet sheet) {
		this.jointTransforms.put(jointName, sheet);
	}
	
	public final Pose getPoseByTimeRaw(LivingEntityPatch<?> entitypatch, float time, float partialTicks) {
		Pose pose = new Pose();
		for (String jointName : this.jointTransforms.keySet()) {
			if (!entitypatch.isLogicalClient() || this.isJointEnabled(entitypatch, jointName)) {
				pose.putJointData(jointName, this.jointTransforms.get(jointName).getInterpolatedTransform(time));
			}
		}
		return pose;
	}
	
	public Pose getPoseByTime(LivingEntityPatch<?> entitypatch, float time, float partialTicks) {
		Pose pose = new Pose();
		
		for (String jointName : this.jointTransforms.keySet()) {
			if (!entitypatch.isLogicalClient() || this.isJointEnabled(entitypatch, jointName)) {
				pose.putJointData(jointName, this.jointTransforms.get(jointName).getInterpolatedTransform(time));
			}
		}
		
		this.modifyPose(pose, entitypatch, time);
		
		return pose;
	}
	
	/** Modify the pose which also modified in link animation. **/
	protected void modifyPose(Pose pose, LivingEntityPatch<?> entitypatch, float time) {
		;
	}
	
	public void setLinkAnimation(Pose pose1, float convertTimeModifier, LivingEntityPatch<?> entitypatch, LinkAnimation dest) {
		if (!entitypatch.isLogicalClient()) {
			pose1 = Animations.DUMMY_ANIMATION.getPoseByTime(entitypatch, 0.0F, 1.0F);
		}
		float totalTime = convertTimeModifier >= 0.0F ? convertTimeModifier + this.convertTime : this.convertTime;
		boolean isNeg = convertTimeModifier < 0.0F;
		float nextStart = isNeg ? -convertTimeModifier : 0.0F;
		
		if (isNeg) {
			dest.startsAt = nextStart;
		}
		
		dest.getTransfroms().clear();
		dest.setTotalTime(totalTime);
		dest.setNextAnimation(this);
		
		Map<String, JointTransform> data1 = pose1.getJointTransformData();
		Map<String, JointTransform> data2 = this.getPoseByTime(entitypatch, nextStart, 1.0F).getJointTransformData();
		
		for (String jointName : data1.keySet()) {
			if (data1.containsKey(jointName) && data2.containsKey(jointName)) {
				Keyframe[] keyframes = new Keyframe[2];
				keyframes[0] = new Keyframe(0.0F, data1.get(jointName));
				keyframes[1] = new Keyframe(totalTime, data2.get(jointName));
				TransformSheet sheet = new TransformSheet(keyframes);
				dest.addSheet(jointName, sheet);
			}
		}
	}
	
	public void putOnPlayer(AnimationPlayer player) {
		player.setPlayAnimation(this);
	}
	
	public void begin(LivingEntityPatch<?> entitypatch) {}
	public void tick(LivingEntityPatch<?> entitypatch) {}
	public void end(LivingEntityPatch<?> entitypatch, boolean isEnd) {}
	public void linkTick(LivingEntityPatch<?> entitypatch, LinkAnimation linkAnimation) {};
	@OnlyIn(Dist.CLIENT)
	public boolean isJointEnabled(LivingEntityPatch<?> entitypatch, String joint) {
		return this.jointTransforms.containsKey(joint);
	}
	@OnlyIn(Dist.CLIENT)
	public BindModifier getBindModifier(LivingEntityPatch<?> entitypatch, String joint) {
		return null;
	}
	
	public EntityState getState(float time) {
		return EntityState.DEFAULT;
	}
	
	public Map<String, TransformSheet> getTransfroms() {
		return this.jointTransforms;
	}
	
	public float getPlaySpeed(LivingEntityPatch<?> entitypatch) {
		return 1.0F;
	}
	
	public DynamicAnimation getRealAnimation() {
		return this;
	}
	
	public void setTotalTime(float totalTime) {
		this.totalTime = totalTime;
	}
	
	public float getTotalTime() {
		return this.totalTime - 0.001F;
	}
	
	public float getConvertTime() {
		return this.convertTime;
	}
	
	public boolean isRepeat() {
		return this.isRepeat;
	}
	
	public boolean canBePlayedReverse() {
		return false;
	}
	
	public int getNamespaceId() {
		return -1;
	}
	
	public int getId() {
		return -1;
	}
	
	public <V> Optional<V> getProperty(AnimationProperty<V> propertyType) {
		return Optional.empty();
	}
	
	public boolean isMainFrameAnimation() {
		return false;
	}
	
	public boolean isReboundAnimation() {
		return false;
	}
	
	public boolean isMetaAnimation() {
		return false;
	}
	
	@OnlyIn(Dist.CLIENT)
	public void renderDebugging(MatrixStack poseStack, IRenderTypeBuffer buffer, LivingEntityPatch<?> entitypatch, float playTime, float partialTicks) {
		
	}
	//from118
	public TransformSheet getCoord() {
		return this.jointTransforms.get("Root");
	}
}