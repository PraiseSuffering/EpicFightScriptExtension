package yesman.epicfight.world.capabilities.entitypatch;

import com.mojang.datafixers.util.Pair;

import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.brain.Brain;
import net.minecraft.entity.ai.brain.schedule.Activity;
import net.minecraft.entity.ai.brain.task.AttackTargetTask;
import net.minecraft.entity.ai.brain.task.SupplementedTask;
import net.minecraft.entity.ai.brain.task.WalkToTargetTask;
import net.minecraft.item.CrossbowItem;
import net.minecraft.item.UseAction;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import yesman.epicfight.api.animation.LivingMotion;
import yesman.epicfight.api.animation.LivingMotions;
import yesman.epicfight.api.animation.types.StaticAnimation;
import yesman.epicfight.api.client.animation.ClientAnimator;
import yesman.epicfight.api.client.animation.Layer;
import yesman.epicfight.api.data.reloader.MobPatchReloadListener;
import yesman.epicfight.api.model.Model;
import yesman.epicfight.api.utils.ExtendedDamageSource.StunType;
import yesman.epicfight.api.utils.math.OpenMatrix4f;
import yesman.epicfight.gameasset.Models;
import yesman.epicfight.world.capabilities.item.CapabilityItem;
import yesman.epicfight.world.capabilities.item.CapabilityItem.WeaponCategories;
import yesman.epicfight.world.entity.ai.attribute.EpicFightAttributes;
import yesman.epicfight.world.entity.ai.brain.BrainRecomposer;
import yesman.epicfight.world.entity.ai.brain.task.AnimatedCombatBehavior;
import yesman.epicfight.world.entity.ai.brain.task.BackUpIfTooCloseStopInaction;
import yesman.epicfight.world.entity.ai.brain.task.MoveToTargetSinkStopInaction;
import yesman.epicfight.world.entity.ai.goal.AnimatedAttackGoal;
import yesman.epicfight.world.entity.ai.goal.CombatBehaviors;
import yesman.epicfight.world.entity.ai.goal.TargetChasingGoal;

public class CustomHumanoidMobPatch<T extends CreatureEntity> extends HumanoidMobPatch<T> {
	private final MobPatchReloadListener.CustomHumanoidMobPatchProvider provider;

	public CustomHumanoidMobPatch(Faction faction, MobPatchReloadListener.CustomHumanoidMobPatchProvider provider) {
		super(faction);
		this.provider = provider;
		this.weaponLivingMotions = this.provider.getHumanoidWeaponMotions();
		this.weaponAttackMotions = this.provider.getHumanoidCombatBehaviors();
		//tkk
		this.setTkkCustomSpeed(this.provider.tkkCustomSpeed);
		this.setTkkEnableCustom(this.provider.tkkEnableCustom);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void setAIAsInfantry(boolean holdingRanedWeapon) {
		boolean isUsingBrain = this.getOriginal().getBrain().availableBehaviorsByPriority.size() > 0;
		
		if (isUsingBrain) {
			if (!holdingRanedWeapon) {
				CombatBehaviors.Builder<HumanoidMobPatch<?>> builder = this.getHoldingItemWeaponMotionBuilder();
				
				if (builder != null) {
					BrainRecomposer.replaceBehaviors((Brain<T>)this.original.getBrain(), Activity.FIGHT, AttackTargetTask.class, new AnimatedCombatBehavior<>(this, builder.build(this)));
				}
				
				BrainRecomposer.replaceBehaviors((Brain<T>)this.original.getBrain(), Activity.FIGHT, SupplementedTask.class, new SupplementedTask<>((entity) -> entity.isHolding(is -> is.getItem() instanceof CrossbowItem), new BackUpIfTooCloseStopInaction<>(5, 0.75F)));
				BrainRecomposer.replaceBehaviors((Brain<T>)this.original.getBrain(), Activity.CORE, WalkToTargetTask.class, new MoveToTargetSinkStopInaction());
			}
		} else {
			if (!holdingRanedWeapon) {
				CombatBehaviors.Builder<HumanoidMobPatch<?>> builder = this.getHoldingItemWeaponMotionBuilder();
				
				if (builder != null) {
					this.original.goalSelector.addGoal(0, new AnimatedAttackGoal<>(this, builder.build(this)));
					this.original.goalSelector.addGoal(1, new TargetChasingGoal(this, this.getOriginal(), this.provider.getChasingSpeed(), true));
				}
			}
		}
	}
	
	@Override
	protected void setWeaponMotions() {
		if (this.weaponAttackMotions == null) {
			super.setWeaponMotions();
		}
	}
	
	@Override
	protected void initAttributes() {
		this.original.getAttribute(EpicFightAttributes.WEIGHT.get()).setBaseValue(this.original.getAttribute(Attributes.MAX_HEALTH).getBaseValue() * 2.0D);
		this.original.getAttribute(EpicFightAttributes.MAX_STRIKES.get()).setBaseValue(this.provider.getAttributeValues().get(EpicFightAttributes.MAX_STRIKES.get()));
		this.original.getAttribute(EpicFightAttributes.ARMOR_NEGATION.get()).setBaseValue(this.provider.getAttributeValues().get(EpicFightAttributes.ARMOR_NEGATION.get()));
		this.original.getAttribute(EpicFightAttributes.IMPACT.get()).setBaseValue(this.provider.getAttributeValues().get(EpicFightAttributes.IMPACT.get()));
		
		if (this.provider.getAttributeValues().containsKey(Attributes.ATTACK_DAMAGE)) {
			this.original.getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(this.provider.getAttributeValues().get(Attributes.ATTACK_DAMAGE));
		}

		//tkk
		this.original.getAttribute(EpicFightAttributes.OFFHAND_MAX_STRIKES.get()).setBaseValue(this.provider.getAttributeValues().get(EpicFightAttributes.OFFHAND_MAX_STRIKES.get()));
		this.original.getAttribute(EpicFightAttributes.OFFHAND_ARMOR_NEGATION.get()).setBaseValue(this.provider.getAttributeValues().get(EpicFightAttributes.OFFHAND_ARMOR_NEGATION.get()));
		this.original.getAttribute(EpicFightAttributes.OFFHAND_IMPACT.get()).setBaseValue(this.provider.getAttributeValues().get(EpicFightAttributes.OFFHAND_IMPACT.get()));

		if (this.provider.getAttributeValues().containsKey(EpicFightAttributes.OFFHAND_ATTACK_DAMAGE.get())) {
			this.original.getAttribute(EpicFightAttributes.OFFHAND_ATTACK_DAMAGE.get()).setBaseValue(this.provider.getAttributeValues().get(EpicFightAttributes.OFFHAND_ATTACK_DAMAGE.get()));
		}else {
			this.original.getAttribute(EpicFightAttributes.OFFHAND_ATTACK_DAMAGE.get()).setBaseValue(1);
		}
	}
	@OnlyIn(Dist.CLIENT)
	@Override
	public void initAnimator(ClientAnimator clientAnimator) {
		for (Pair<LivingMotion, StaticAnimation> pair : this.provider.getDefaultAnimations()) {
			clientAnimator.addLivingAnimation(pair.getFirst(), pair.getSecond());
		}
		
		clientAnimator.setCurrentMotionsAsDefault();
	}
	
	@Override
	public void updateMotion(boolean considerInaction) {
		super.commonAggressiveMobUpdateMotion(considerInaction);
		
		if (this.original.isUsingItem()) {
			CapabilityItem activeItem = this.getHoldingItemCapability(this.original.getUsedItemHand());
			UseAction useAnim = this.original.getItemInHand(this.original.getUsedItemHand()).getUseAnimation();
			UseAction secondUseAnim = activeItem.getUseAnimation(this);
			
			if (useAnim == UseAction.BLOCK || secondUseAnim == UseAction.BLOCK)
				if (activeItem.getWeaponCategory() == WeaponCategories.SHIELD)
					currentCompositeMotion = LivingMotions.BLOCK_SHIELD;
				else
					currentCompositeMotion = LivingMotions.BLOCK;
			else if (useAnim == UseAction.BOW || useAnim == UseAction.SPEAR)
				currentCompositeMotion = LivingMotions.AIM;
			else if (useAnim == UseAction.CROSSBOW)
				currentCompositeMotion = LivingMotions.RELOAD;
			else
				currentCompositeMotion = currentLivingMotion;
		} else {
			if (CrossbowItem.isCharged(this.original.getMainHandItem()))
				currentCompositeMotion = LivingMotions.AIM;
			else if (this.getClientAnimator().getCompositeLayer(Layer.Priority.MIDDLE).animationPlayer.getAnimation().isReboundAnimation())
				currentCompositeMotion = LivingMotions.NONE;
			else if (this.original.swinging && !this.original.getSleepingPos().isPresent())
				currentCompositeMotion = LivingMotions.DIGGING;
			else
				currentCompositeMotion = currentLivingMotion;
			
			if (this.getClientAnimator().isAiming() && currentCompositeMotion != LivingMotions.AIM) {
				this.playReboundAnimation();
			}
		}
	}
	
	@Override
	public <M extends Model> M getEntityModel(Models<M> modelDB) {
		return modelDB.get(this.provider.getModelLocation());
	}
	
	@Override
	public StaticAnimation getHitAnimation(StunType stunType) {
		return this.provider.getStunAnimations().get(stunType);
	}
	
	@Override
	public OpenMatrix4f getModelMatrix(float partialTicks) {
		float scale = this.provider.getScale();
		return super.getModelMatrix(partialTicks).scale(scale, scale, scale);
	}
	public float getAttackSpeed(){
		float attackspeed = this.provider.getAttackspeed();
		return attackspeed;
	}
}