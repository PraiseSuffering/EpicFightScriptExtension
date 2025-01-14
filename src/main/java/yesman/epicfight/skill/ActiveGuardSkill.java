package yesman.epicfight.skill;

import com.google.common.collect.Lists;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;
import tkk.epic.event.GuardIsBlockableEvent;
import yesman.epicfight.api.animation.types.StaticAnimation;
import yesman.epicfight.gameasset.Animations;
import yesman.epicfight.gameasset.EpicFightSounds;
import yesman.epicfight.gameasset.Skills;
import yesman.epicfight.particle.EpicFightParticles;
import yesman.epicfight.particle.HitParticleType;
import yesman.epicfight.skill.SkillDataManager.SkillDataKey;
import yesman.epicfight.world.capabilities.entitypatch.player.PlayerPatch;
import yesman.epicfight.world.capabilities.item.CapabilityItem;
import yesman.epicfight.world.capabilities.item.CapabilityItem.Styles;
import yesman.epicfight.world.capabilities.item.CapabilityItem.WeaponCategories;
import yesman.epicfight.world.entity.eventlistener.HurtEvent;
import yesman.epicfight.world.entity.eventlistener.PlayerEventListener.EventType;

import javax.annotation.Nullable;
import java.util.List;

public class ActiveGuardSkill extends GuardSkill {
	private static final SkillDataKey<Integer> LAST_ACTIVE = SkillDataKey.createDataKey(SkillDataManager.ValueType.INTEGER);
	private static final SkillDataKey<Integer> PARRY_MOTION_COUNTER = SkillDataKey.createDataKey(SkillDataManager.ValueType.INTEGER);
	
	public static GuardSkill.Builder createBuilder(ResourceLocation resourceLocation) {
		return GuardSkill.createBuilder(resourceLocation)
				.addAdvancedGuardMotion(WeaponCategories.SWORD, (itemCap, playerpatch) -> itemCap.getStyle(playerpatch) == Styles.ONE_HAND ?
					new StaticAnimation[] { Animations.SWORD_GUARD_ACTIVE_HIT1, Animations.SWORD_GUARD_ACTIVE_HIT2 } :
					new StaticAnimation[] { Animations.SWORD_GUARD_ACTIVE_HIT2, Animations.SWORD_GUARD_ACTIVE_HIT3 })
				.addAdvancedGuardMotion(WeaponCategories.LONGSWORD, (itemCap, playerpatch) ->
					new StaticAnimation[] { Animations.LONGSWORD_GUARD_ACTIVE_HIT1, Animations.LONGSWORD_GUARD_ACTIVE_HIT2 })
				.addAdvancedGuardMotion(WeaponCategories.TACHI, (itemCap, playerpatch) ->
					new StaticAnimation[] { Animations.LONGSWORD_GUARD_ACTIVE_HIT1, Animations.LONGSWORD_GUARD_ACTIVE_HIT2 })
				.addAdvancedGuardMotion(WeaponCategories.AXE, (itemCap, playerpatch) ->
						new StaticAnimation[] { Animations.SWORD_GUARD_ACTIVE_HIT1, Animations.SWORD_GUARD_ACTIVE_HIT2 })
				.addAdvancedGuardMotion(WeaponCategories.KATANA, (itemCap, playerpatch) ->
						new StaticAnimation[] { Animations.SWORD_GUARD_ACTIVE_HIT1, Animations.SWORD_GUARD_ACTIVE_HIT2 })
				.addAdvancedGuardMotion(WeaponCategories.SPEAR, (itemCap, playerpatch) ->
						new StaticAnimation[] { Animations.LONGSWORD_GUARD_ACTIVE_HIT1, Animations.LONGSWORD_GUARD_ACTIVE_HIT2 })
				.addAdvancedGuardMotion(WeaponCategories.DAGGER, (itemCap, playerpatch) ->
						new StaticAnimation[] { Animations.LONGSWORD_GUARD_ACTIVE_HIT1, Animations.LONGSWORD_GUARD_ACTIVE_HIT2 })
				.addAdvancedGuardMotion(WeaponCategories.GREATSWORD, (itemCap, playerpatch) ->
						new StaticAnimation[] { Animations.LONGSWORD_GUARD_ACTIVE_HIT1, Animations.LONGSWORD_GUARD_ACTIVE_HIT2 });
	}
	
	public ActiveGuardSkill(GuardSkill.Builder builder) {
		super(builder);
	}
	
	@Override
	public void onInitiate(SkillContainer container) {
		super.onInitiate(container);
		
		container.getDataManager().registerData(LAST_ACTIVE);
		container.getDataManager().registerData(PARRY_MOTION_COUNTER);
		
		container.getExecuter().getEventListener().addEventListener(EventType.SERVER_ITEM_USE_EVENT, EVENT_UUID, (event) -> {
			CapabilityItem itemCapability = event.getPlayerPatch().getHoldingItemCapability(Hand.MAIN_HAND);
			
			if (this.isHoldingWeaponAvailable(event.getPlayerPatch(), itemCapability, BlockType.GUARD) && this.isExecutableState(event.getPlayerPatch())) {
				event.getPlayerPatch().getOriginal().startUsingItem(Hand.MAIN_HAND);
			}
			
			container.getDataManager().setData(LAST_ACTIVE, event.getPlayerPatch().getOriginal().tickCount);
		});
	}
	
	@Override
	public void onRemoved(SkillContainer container) {
		super.onRemoved(container);
	}
	
	@Override
	public void guard(SkillContainer container, CapabilityItem itemCapability, HurtEvent.Pre event, float knockback, float impact, boolean advanced) {
		if (this.isHoldingWeaponAvailable(event.getPlayerPatch(), itemCapability, BlockType.ADVANCED_GUARD)) {
			DamageSource damageSource = event.getDamageSource();
			GuardIsBlockableEvent event1=new GuardIsBlockableEvent(container,itemCapability,event,knockback,impact,advanced,this.isBlockableSource(damageSource, true),true);
			MinecraftForge.EVENT_BUS.post(event1);
			if (event1.canBlock) {
				knockback=event1.knockback;
				impact=event1.impact;
				ServerPlayerEntity playerentity = event.getPlayerPatch().getOriginal();
				boolean successParrying = playerentity.tickCount - container.getDataManager().getDataValue(LAST_ACTIVE) < 8;
				float penalty = container.getDataManager().getDataValue(PENALTY);
				event.getPlayerPatch().playSound(EpicFightSounds.CLASH, -0.05F, 0.1F);
				EpicFightParticles.HIT_BLUNT.get().spawnParticleWithArgument(((ServerWorld)playerentity.level), HitParticleType.FRONT_OF_EYES, HitParticleType.ZERO, playerentity, damageSource.getDirectEntity());
				
				if (successParrying) {
					penalty = 0.1F;
					knockback *= 0.4F;
				} else {
					penalty += this.getPenaltyMultiplier(itemCapability);
					container.getDataManager().setDataSync(PENALTY, penalty, playerentity);
				}
				
				if (damageSource.getDirectEntity() instanceof LivingEntity) {
					knockback += EnchantmentHelper.getKnockbackBonus((LivingEntity)damageSource.getDirectEntity()) * 0.1F;
				}
				
				event.getPlayerPatch().knockBackEntity(damageSource.getDirectEntity().position(), knockback);
				
				float stamina = event.getPlayerPatch().getStamina() - event1.getConsume(penalty * impact);
				event.getPlayerPatch().setStamina(stamina);
				
				BlockType blockType = successParrying ? BlockType.ADVANCED_GUARD : stamina >= 0.0F ? BlockType.GUARD : BlockType.GUARD_BREAK;
				StaticAnimation animation = this.getGuardMotion(event.getPlayerPatch(), itemCapability, blockType);
				
				if (animation != null) {
					event.getPlayerPatch().playAnimationSynchronized(animation, 0);
				}
				
				if (blockType == BlockType.GUARD_BREAK) {
					event.getPlayerPatch().playSound(EpicFightSounds.NEUTRALIZE_MOBS, 3.0F, 0.0F, 0.1F);
				}
				
				this.dealEvent(event.getPlayerPatch(), event);
				
				return;
			}
		}
		
		super.guard(container, itemCapability, event, knockback, impact, false);
	}
	
	@Override
	public boolean isBlockableSource(DamageSource damageSource, boolean advanced) {
		return (damageSource.isProjectile() && advanced) || super.isBlockableSource(damageSource, false);
	}
	
	@Nullable
	public StaticAnimation getGuardMotion(PlayerPatch<?> playerpatch, CapabilityItem itemCapability, BlockType blockType) {
		if (blockType == BlockType.ADVANCED_GUARD) {
			StaticAnimation[] motions = (StaticAnimation[])this.getGuradMotionMap(blockType).getOrDefault(itemCapability.getWeaponCategory(), (a, b) -> null).apply(itemCapability, playerpatch);
			
			if (motions != null) {
				SkillDataManager dataManager = playerpatch.getSkill(this.getCategory()).getDataManager();
				int motionCounter = dataManager.getDataValue(PARRY_MOTION_COUNTER);
				dataManager.setDataF(PARRY_MOTION_COUNTER, (v) -> v + 1);
				motionCounter %= motions.length;
				
				return motions[motionCounter];
			}
		}
		
		return super.getGuardMotion(playerpatch, itemCapability, blockType);
	}
	
	@Override
	public Skill getPriorSkill() {
		return Skills.GUARD;
	}
	
	@Override
	public boolean isAdvancedGuard() {
		return true;
	}
	
	@OnlyIn(Dist.CLIENT)
	@Override
	public List<Object> getTooltipArgs() {
		List<Object> list = Lists.<Object>newArrayList();
		list.add(String.format("%s, %s, %s, %s", WeaponCategories.KATANA, WeaponCategories.LONGSWORD, WeaponCategories.SWORD, WeaponCategories.TACHI).toLowerCase());
		return list;
	}
}