package yesman.epicfight.api.animation.types;

import com.google.common.collect.Maps;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.entity.PartEntity;
import net.minecraftforge.fml.RegistryObject;
import tkk.epic.event.AttackAnimationEndEvent;
import yesman.epicfight.api.animation.*;
import yesman.epicfight.api.animation.property.AnimationProperty.*;
import yesman.epicfight.api.collider.Collider;
import yesman.epicfight.api.model.Model;
import yesman.epicfight.api.utils.AttackResult;
import yesman.epicfight.api.utils.ExtendedDamageSource;
import yesman.epicfight.api.utils.ExtendedDamageSource.StunType;
import yesman.epicfight.api.utils.HitEntityList;
import yesman.epicfight.api.utils.math.ExtraDamageType;
import yesman.epicfight.api.utils.math.MathUtils;
import yesman.epicfight.api.utils.math.OpenMatrix4f;
import yesman.epicfight.api.utils.math.Vec3f;
import yesman.epicfight.gameasset.Models;
import yesman.epicfight.particle.HitParticleType;
import yesman.epicfight.world.capabilities.entitypatch.HumanoidMobPatch;
import yesman.epicfight.world.capabilities.entitypatch.LivingEntityPatch;
import yesman.epicfight.world.capabilities.entitypatch.MobPatch;
import yesman.epicfight.world.capabilities.entitypatch.player.PlayerPatch;
import yesman.epicfight.world.capabilities.entitypatch.player.ServerPlayerPatch;
import yesman.epicfight.world.entity.eventlistener.AttackEndEvent;
import yesman.epicfight.world.entity.eventlistener.DealtDamageEvent;
import yesman.epicfight.world.entity.eventlistener.PlayerEventListener.EventType;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public class AttackAnimation extends ActionAnimation {
	public AttackAnimation(){
		super();
		phases=new Phase[0];
	}
	public AttackAnimation(int i){
		super();
		phases=new Phase[i];
	}
	protected static final ActionAnimationCoordSetter COMMON_COORD_SETTER = (self, entitypatch, transformSheet) -> {
		LivingEntity attackTarget = entitypatch.getTarget();
		
		if (!self.getRealAnimation().getProperty(AttackAnimationProperty.FIXED_MOVE_DISTANCE).orElse(false) && attackTarget != null) {
			TransformSheet transform = self.getTransfroms().get("Root").copyAll();
			Keyframe[] keyframes = transform.getKeyframes();
			int startFrame = 0;
			int endFrame = transform.getKeyframes().length - 1;
			Vec3f keyLast = keyframes[endFrame].transform().translation();
			Vector3d pos = entitypatch.getOriginal().getEyePosition(1.0F);
			Vector3d targetpos = attackTarget.position();
			float horizontalDistance = Math.max((float)MathUtils.horizontalDistance(targetpos.subtract(pos)) - (attackTarget.getBbWidth() + entitypatch.getOriginal().getBbWidth()) * 0.75F, 0.0F);
			Vec3f worldPosition = new Vec3f(keyLast.x, 0.0F, -horizontalDistance);
			float scale = Math.min(worldPosition.length() / keyLast.length(), 2.0F);
			
			for (int i = startFrame; i <= endFrame; i++) {
				Vec3f translation = keyframes[i].transform().translation();
				translation.z *= scale;
			}
			
			transformSheet.readFrom(transform);
		} else {
			transformSheet.readFrom(self.getTransfroms().get("Root"));
		}
	};
	
	public final Phase[] phases;
	
	public AttackAnimation(float convertTime, float antic, float preDelay, float contact, float recovery, @Nullable Collider collider, String index, String path, Model model) {
		this(convertTime, path, model, new Phase(0.0F, antic, preDelay, contact, recovery, Float.MAX_VALUE, index, collider));
	}
	
	public AttackAnimation(float convertTime, float antic, float preDelay, float contact, float recovery, Hand hand, @Nullable Collider collider, String index, String path, Model model) {
		this(convertTime, path, model, new Phase(0.0F, antic, preDelay, contact, recovery, Float.MAX_VALUE, hand, index, collider));
	}
	
	public AttackAnimation(float convertTime, String path, Model model, Phase... phases) {
		super(convertTime, path, model);
		
		this.addProperty(ActionAnimationProperty.COORD_SET_BEGIN, COMMON_COORD_SETTER);
		this.addProperty(ActionAnimationProperty.COORD_SET_TICK, COMMON_COORD_SETTER);
		this.addProperty(ActionAnimationProperty.STOP_MOVEMENT, true);
		this.phases = phases;
		
		this.stateSpectrumBlueprint.clear();
		
		for (Phase phase : phases) {
			
			float preDelay = phase.preDelay;
			
			if (preDelay == 0.0F) {
				preDelay += 0.01F;
			}
			
			this.stateSpectrumBlueprint
				.newTimePair(phase.start,preDelay)
				.addState(EntityState.PHASE_LEVEL, 1)
				.newTimePair(phase.start, phase.recovery)
				.addState(EntityState.MOVEMENT_LOCKED, true)
				.addState(EntityState.CAN_BASIC_ATTACK, false)
				.newTimePair(phase.start, phase.end)
				.addState(EntityState.INACTION, true)
				.newTimePair(phase.antic, phase.recovery)
				.addState(EntityState.TURNING_LOCKED, true)
				.newTimePair(preDelay, phase.contact + 0.01F)
				.addState(EntityState.ATTACKING, true)
				.addState(EntityState.PHASE_LEVEL, 2)
				.newTimePair(phase.contact + 0.01F, phase.end)
				.addState(EntityState.PHASE_LEVEL, 3);
		}
	}

	@Override
	public void begin(LivingEntityPatch<?> entitypatch) {
		super.begin(entitypatch);
		entitypatch.getAnimator().tkkCustomCollider.updateCollider();
	}
	@Override
	public void tick(LivingEntityPatch<?> entitypatch) {
		super.tick(entitypatch);
		
		if (!entitypatch.isLogicalClient()) {
			AnimationPlayer player = entitypatch.getAnimator().getPlayerFor(this);
			float elapsedTime = player.getElapsedTime();
			float prevElapsedTime = player.getPrevElapsedTime();
			EntityState state = this.getState(elapsedTime);
			EntityState prevState = this.getState(prevElapsedTime);
			Phase phase = this.getPhaseByTime(elapsedTime);
			
			if (state.getLevel() == 1 && !state.turningLocked()) {
				if (entitypatch instanceof MobPatch) {
					((MobEntity)entitypatch.getOriginal()).getNavigation().stop();
					entitypatch.getOriginal().attackAnim = 2;
					LivingEntity target = entitypatch.getTarget();
					
					if (target != null) {
						entitypatch.rotateTo(target, entitypatch.getYRotLimit(), false);
					}
				}
			} else if (prevState.attacking() || state.attacking() || (prevState.getLevel() < 2 && state.getLevel() > 2)) {
				if (!prevState.attacking()) {
					entitypatch.playSound(this.getSwingSound(entitypatch, phase), 0.0F, 0.0F);
					entitypatch.currentlyAttackedEntity.clear();
				}
				
				this.hurtCollidingEntities(entitypatch, prevElapsedTime, elapsedTime, prevState, state, phase);
			}
		}
	}
	
	@Override
	public void end(LivingEntityPatch<?> entitypatch, boolean isEnd) {
		super.end(entitypatch, isEnd);
		
		if (entitypatch instanceof ServerPlayerPatch && isEnd) {
			ServerPlayerPatch playerpatch = ((ServerPlayerPatch)entitypatch);
			playerpatch.getEventListener().triggerEvents(EventType.ATTACK_ANIMATION_END_EVENT, new AttackEndEvent(playerpatch, entitypatch.currentlyAttackedEntity, this.getId()));
		}
		MinecraftForge.EVENT_BUS.post(new AttackAnimationEndEvent(entitypatch,entitypatch.currentlyAttackedEntity, this.getId()));
		
		entitypatch.currentlyAttackedEntity.clear();
		//tkk
		entitypatch.getAnimator().updateAttackSpeed();

		if (entitypatch instanceof HumanoidMobPatch && entitypatch.isLogicalClient()) {
			MobEntity entity = (MobEntity)entitypatch.getOriginal();
			
			if (entity.getTarget() != null && !entity.getTarget().isAlive()) {
				entity.setTarget((LivingEntity) null);
			}
		}
	}
	
	public void hurtCollidingEntities(LivingEntityPatch<?> entitypatch, float prevElapsedTime, float elapsedTime, EntityState prevState, EntityState state, Phase phase) {
		Collider collider = this.getCollider(entitypatch, elapsedTime);
		LivingEntity entity = entitypatch.getOriginal();
		entitypatch.getEntityModel(Models.LOGICAL_SERVER).getArmature().initializeTransform();
		float prevPoseTime = prevState.attacking() ? prevElapsedTime : phase.preDelay;
		float poseTime = state.attacking() ? elapsedTime : phase.contact;
		List<Entity> list = collider.updateAndSelectCollideEntity(entitypatch, this, prevPoseTime, poseTime, phase.getColliderJointName(), this.getPlaySpeed(entitypatch));
		
		if (list.size() > 0) {
			HitEntityList hitEntities = new HitEntityList(entitypatch, list, phase.getProperty(AttackPhaseProperty.HIT_PRIORITY).orElse(HitEntityList.Priority.DISTANCE));
			boolean flag1 = true;
			int maxStrikes = this.getMaxStrikes(entitypatch, phase);
			
			while (entitypatch.currentlyAttackedEntity.size() < maxStrikes && hitEntities.next()) {
				Entity e = hitEntities.getEntity();
				LivingEntity trueEntity = this.getTrueEntity(e);
				
				if (!entitypatch.currentlyAttackedEntity.contains(trueEntity) && !entitypatch.isTeammate(e)) {
					if (e instanceof LivingEntity || e instanceof PartEntity) {
						if (entity.canSee(e)) {
							ExtendedDamageSource source = this.getExtendedDamageSource(entitypatch, e, phase);
							AttackResult attackResult = entitypatch.tryHarm(e, source, this.getDamageTo(entitypatch, trueEntity, phase, source));
							boolean count = attackResult.resultType.count();
							
							if (attackResult.resultType.dealtDamage()) {
								int temp = e.invulnerableTime;
								trueEntity.invulnerableTime = 0;
								boolean attackSuccess = e.hurt((DamageSource)source, attackResult.damage);
								trueEntity.invulnerableTime = temp;
								count = attackSuccess || trueEntity.isDamageSourceBlocked((DamageSource)source);
								entitypatch.onHurtSomeone(e, phase.hand, source, attackResult.damage, attackSuccess);
								
								if (attackSuccess) {
									if (entitypatch instanceof ServerPlayerPatch) {
										ServerPlayerPatch playerpatch = ((ServerPlayerPatch)entitypatch);
										playerpatch.getEventListener().triggerEvents(EventType.DEALT_DAMAGE_EVENT_POST, new DealtDamageEvent<>(playerpatch, trueEntity, source, attackResult.damage));
									}
									
									if (flag1 && entitypatch instanceof PlayerPatch) {
										entity.getItemInHand(phase.hand).hurtEnemy(trueEntity, (PlayerEntity)entity);
										flag1 = false;
									}
									if(this.getHitSound(entitypatch, phase)!=null){
										e.level.playSound(null, e.getX(), e.getY(), e.getZ(), this.getHitSound(entitypatch, phase), e.getSoundSource(), 1.0F, 1.0F);
									}
									this.spawnHitParticle(((ServerWorld)e.level), entitypatch, e, phase);
								}
							}
							
							if (count) {
								entitypatch.currentlyAttackedEntity.add(trueEntity);
							}
						}
					}
				}
			}
		}
	}
	
	@Override
	public void onLoaded() {
		if (!this.getProperty(AttackAnimationProperty.LOCK_ROTATION).orElse(false)) {
			this.stateSpectrumBlueprint.newTimePair(0.0F, Float.MAX_VALUE).addStateRemoveOld(EntityState.TURNING_LOCKED, false);
		}
		
		super.onLoaded();
	}
	
	public Collider getCollider(LivingEntityPatch<?> entitypatch, float elapsedTime) {
		Phase phase = this.getPhaseByTime(elapsedTime);
		//tkk
		Collider temp = phase.collider != null ? phase.collider : entitypatch.getColliderMatching(phase.hand);
		return entitypatch.getAnimator().tkkCustomCollider.getCollider(temp);
	}
	
	public LivingEntity getTrueEntity(Entity entity) {
		if (entity instanceof LivingEntity) {
			return (LivingEntity)entity;
		} else if (entity instanceof PartEntity) {
			Entity parentEntity = ((PartEntity<?>)entity).getParent();
			
			if (parentEntity instanceof LivingEntity) {
				return (LivingEntity)parentEntity;
			}
		}
		
		return null;
	}
	
	protected int getMaxStrikes(LivingEntityPatch<?> entitypatch, Phase phase) {
		return phase.getProperty(AttackPhaseProperty.MAX_STRIKES).map((valueCorrector) -> valueCorrector.getTotalValue(entitypatch.getMaxStrikes(phase.hand))).orElse(Float.valueOf(entitypatch.getMaxStrikes(phase.hand))).intValue();
	}
	
	protected float getDamageTo(LivingEntityPatch<?> entitypatch, LivingEntity target, Phase phase, ExtendedDamageSource source) {
		float totalDamage = phase.getProperty(AttackPhaseProperty.DAMAGE).map((valueCorrector) -> valueCorrector.getTotalValue(entitypatch.getDamageTo(target, source, phase.hand))).orElse(entitypatch.getDamageTo(target, source, phase.hand));
		ExtraDamageType extraCalculator = phase.getProperty(AttackPhaseProperty.EXTRA_DAMAGE).orElse(null);
		
		if (extraCalculator != null) {
			totalDamage += extraCalculator.get(entitypatch.getOriginal(), target);
		}
		
		return entitypatch.tkkCustomADS.getDamage(totalDamage);
	}
	
	protected SoundEvent getSwingSound(LivingEntityPatch<?> entitypatch, Phase phase) {
		return phase.getProperty(AttackPhaseProperty.SWING_SOUND).orElse(entitypatch.getSwingSound(phase.hand));
	}
	
	protected SoundEvent getHitSound(LivingEntityPatch<?> entitypatch, Phase phase) {
		return entitypatch.tkkCustomADS.getHitSound(phase.getProperty(AttackPhaseProperty.HIT_SOUND).orElse(entitypatch.getWeaponHitSound(phase.hand)));
	}
	
	protected ExtendedDamageSource getExtendedDamageSource(LivingEntityPatch<?> entitypatch, Entity target, Phase phase) {
		StunType stunType = phase.getProperty(AttackPhaseProperty.STUN_TYPE).orElse(StunType.SHORT);
		ExtendedDamageSource extendedSource = entitypatch.getDamageSource(stunType, this, phase.hand);
		
		phase.getProperty(AttackPhaseProperty.ARMOR_NEGATION).ifPresent((opt) -> {
			extendedSource.setArmorNegation(opt.getTotalValue(extendedSource.getArmorNegation()));
		});
		phase.getProperty(AttackPhaseProperty.IMPACT).ifPresent((opt) -> {
			extendedSource.setImpact(opt.getTotalValue(extendedSource.getImpact()));
		});
		
		phase.getProperty(AttackPhaseProperty.FINISHER).ifPresent((opt) -> {
			extendedSource.setFinisher(opt);
		});
		
		phase.getProperty(AttackPhaseProperty.SOURCE_LOCATION_PROVIDER).ifPresent((opt) -> {
			extendedSource.setInitialPosition(opt.apply(entitypatch));
		});
		//tkk
		return entitypatch.tkkCustomADS.getDamageSource(extendedSource);
	}
	
	protected void spawnHitParticle(ServerWorld world, LivingEntityPatch<?> attacker, Entity hit, Phase phase) {
		Optional<RegistryObject<HitParticleType>> particleOptional = phase.getProperty(AttackPhaseProperty.PARTICLE);
		HitParticleType particle = particleOptional.isPresent() ? particleOptional.get().get() : attacker.getWeaponHitParticle(phase.hand);
		particle=attacker.tkkCustomADS.getHitParticleType(particle);
		if(particle==null){return;}
		particle.spawnParticleWithArgument(world, null, null, hit, attacker.getOriginal());
	}
	
	@Override
	public Pose getPoseByTime(LivingEntityPatch<?> entitypatch, float time, float partialTicks) {
		Pose pose = super.getPoseByTime(entitypatch, time, partialTicks);
		
		this.getProperty(AttackAnimationProperty.ROTATE_X).ifPresent((flag) -> {
			if (flag) {
				float pitch = entitypatch.getAttackDirectionPitch();
				JointTransform chest = pose.getOrDefaultTransform("Chest");
				chest.frontResult(JointTransform.getRotation(MathUtils.quaternionFromDegree(Vector3f.XP, -pitch)), OpenMatrix4f::mulAsOriginFront);
				
				if (entitypatch instanceof PlayerPatch) {
					JointTransform head = pose.getOrDefaultTransform("Head");
					MathUtils.mulQuaternion(MathUtils.quaternionFromDegree(Vector3f.XP, pitch), head.rotation(), head.rotation());
				}
			}
		});
		
		return pose;
	}
	
	@Override
	public float getPlaySpeed(LivingEntityPatch<?> entitypatch) {
		float speed=1.0f;
		if (this.getProperty(StaticAnimationProperty.PLAY_SPEED).isPresent()) {
			speed = super.getPlaySpeed(entitypatch);
		}else if(entitypatch.getTkkEnableCustom()){
			float speedFactor = this.getProperty(AttackAnimationProperty.ATTACK_SPEED_FACTOR).orElse(1.0F);
			Optional<Float> property = this.getProperty(AttackAnimationProperty.BASIS_ATTACK_SPEED);
			float correctedSpeed = property.map((value) -> entitypatch.getTkkModifiersAttackSpeed(entitypatch.getTkkCustomSpeed()) / value)
					.orElse(this.totalTime * entitypatch.getTkkModifiersAttackSpeed(entitypatch.getTkkCustomSpeed()));

			correctedSpeed = Math.round(correctedSpeed * 1000.0F) / 1000.0F;
			speed = 1.0F + (correctedSpeed - 1.0F) * speedFactor;
			//return entitypatch.getTkkCustomSpeed();
		}else if (entitypatch instanceof PlayerPatch<?>) {
			Phase phase = this.getPhaseByTime(entitypatch.getAnimator().getPlayerFor(this).getElapsedTime());
			float speedFactor = this.getProperty(AttackAnimationProperty.ATTACK_SPEED_FACTOR).orElse(1.0F);
			Optional<Float> property = this.getProperty(AttackAnimationProperty.BASIS_ATTACK_SPEED);
			float correctedSpeed = property.map((value) -> entitypatch.getTkkModifiersAttackSpeed(((PlayerPatch<?>)entitypatch).getAttackSpeed(phase.hand)) / value)
					.orElse(this.totalTime * entitypatch.getTkkModifiersAttackSpeed(((PlayerPatch<?>)entitypatch).getAttackSpeed(phase.hand)));
			
			correctedSpeed = Math.round(correctedSpeed * 1000.0F) / 1000.0F;
			speed = 1.0F + (correctedSpeed - 1.0F) * speedFactor;
		}
		return speed;
	}
	
	public <V> AttackAnimation addProperty(AttackAnimationProperty<V> propertyType, V value) {
		this.properties.put(propertyType, value);
		return this;
	}
	
	public <V> AttackAnimation addProperty(AttackPhaseProperty<V> propertyType, V value) {
		return this.addProperty(propertyType, value, 0);
	}
	
	public <V> AttackAnimation addProperty(AttackPhaseProperty<V> propertyType, V value, int index) {
		this.phases[index].addProperty(propertyType, value);
		return this;
	}
	
	public String getPathIndexByTime(float elapsedTime) {
		return this.getPhaseByTime(elapsedTime).jointName;
	}
	
	public Phase getPhaseByTime(float elapsedTime) {
		Phase currentPhase = null;
		
		for (Phase phase : this.phases) {
			currentPhase = phase;
			
			if (phase.end > elapsedTime) {
				break;
			}
		}
		
		return currentPhase;
	}
	
	@Deprecated
	public void changeCollider(Collider newCollider, int index) {
		this.phases[index].collider = newCollider;
	}
	
	@Override
	@OnlyIn(Dist.CLIENT)
	public void renderDebugging(MatrixStack poseStack, IRenderTypeBuffer buffer, LivingEntityPatch<?> entitypatch, float playTime, float partialTicks) {
		AnimationPlayer animPlayer = entitypatch.getAnimator().getPlayerFor(this);
		float prevElapsedTime = animPlayer.getPrevElapsedTime();
		float elapsedTime = animPlayer.getElapsedTime();
		this.getCollider(entitypatch, elapsedTime).draw(poseStack, buffer, entitypatch, this, prevElapsedTime, elapsedTime, partialTicks, this.getPlaySpeed(entitypatch));
	}
	
	public static class Phase {
		public final Map<AttackPhaseProperty<?>, Object> properties = Maps.newHashMap();
		public final float start;
		public final float antic;
		public final float preDelay;
		public final float contact;
		public final float recovery;
		public final float end;
		public final String jointName;
		public final Hand hand;
		public Collider collider;
		
		public Phase(float start, float antic, float contact, float recovery, float end, String jointName, Collider collider) {
			this(start, antic, contact, recovery, end, Hand.MAIN_HAND, jointName, collider);
		}
		
		public Phase(float start, float antic, float contact, float recovery, float end, Hand hand, String jointName, Collider collider) {
			this(start, antic, antic, contact, recovery, end, hand, jointName, collider);
		}
		
		public Phase(float start, float antic, float preDelay, float contact, float recovery, float end, String jointName, Collider collider) {
			this(start, antic, preDelay, contact, recovery, end, Hand.MAIN_HAND, jointName, collider);
		}
		
		public Phase(float start, float antic, float preDelay, float contact, float recovery, float end, Hand hand, String jointName, Collider collider) {
			this.start = start;
			this.antic = antic;
			this.preDelay = preDelay;
			this.contact = contact;
			this.recovery = recovery;
			this.end = end;
			this.collider = collider;
			this.jointName = jointName;
			this.hand = hand;
		}
		
		public <V> Phase addProperty(AttackPhaseProperty<V> propertyType, V value) {
			this.properties.put(propertyType, value);
			return this;
		}
		
		public void addProperties(Set<Map.Entry<AttackPhaseProperty<?>, Object>> set) {
			for(Map.Entry<AttackPhaseProperty<?>, Object> entry : set) {
				this.properties.put(entry.getKey(), entry.getValue());
			}
		}
		
		@SuppressWarnings("unchecked")
		public <V> Optional<V> getProperty(AttackPhaseProperty<V> propertyType) {
			return (Optional<V>) Optional.ofNullable(this.properties.get(propertyType));
		}
		
		public String getColliderJointName() {
			return this.jointName;
		}
		
		public Hand getHand() {
			return this.hand;
		}

		/*
		//from118
		public List<Entity> getCollidingEntities(LivingEntityPatch<?> entitypatch, AttackAnimation animation, float prevElapsedTime, float elapsedTime, float attackSpeed) {
			List<Entity> entities = Lists.newArrayList();
			Collider collider = this.collider;
			if (collider == null)
				collider = entitypatch.getColliderMatching(this.hand);
			entities.addAll(collider.updateAndSelectCollideEntity(entitypatch, animation, prevElapsedTime, elapsedTime, this.jointName, attackSpeed));

			return new ArrayList<>(new HashSet<>(entities));
		}

		 */
	}
}