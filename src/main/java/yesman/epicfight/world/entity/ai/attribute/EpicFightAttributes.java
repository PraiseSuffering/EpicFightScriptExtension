package yesman.epicfight.world.entity.ai.attribute;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.attributes.RangedAttribute;
import net.minecraft.entity.boss.dragon.EnderDragonEntity;
import net.minecraft.entity.monster.AbstractSkeletonEntity;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.event.entity.EntityAttributeModificationEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import noppes.npcs.CustomEntities;
import yesman.epicfight.main.EpicFightMod;
import yesman.epicfight.world.entity.EpicFightEntities;
import yesman.epicfight.world.entity.WitherGhostClone;

import java.util.UUID;

public class EpicFightAttributes {
	public static final DeferredRegister<Attribute> ATTRIBUTES = DeferredRegister.create(ForgeRegistries.ATTRIBUTES, EpicFightMod.MODID);
    public static final RegistryObject<Attribute> MAX_STAMINA = ATTRIBUTES.register("staminar", () -> new RangedAttribute("attribute.name." + EpicFightMod.MODID + ".staminar", 0.0D, 0.0D, 1024.0D).setSyncable(true));
    public static final RegistryObject<Attribute> STUN_ARMOR = ATTRIBUTES.register("stun_armor", () -> new RangedAttribute("attribute.name." + EpicFightMod.MODID + ".stun_armor", 0.0D, 0.0D, 1024.0D).setSyncable(true));
    public static final RegistryObject<Attribute> WEIGHT = ATTRIBUTES.register("weight", () -> new RangedAttribute("attribute.name." + EpicFightMod.MODID + ".weight", 0.0D, 0.0D, 1024.0).setSyncable(true));
    public static final RegistryObject<Attribute> MAX_STRIKES = ATTRIBUTES.register("max_strikes", () -> new RangedAttribute("attribute.name." + EpicFightMod.MODID + ".max_strikes", 1.0D, 1.0D, 1024.0).setSyncable(true));
	public static final RegistryObject<Attribute> ARMOR_NEGATION = ATTRIBUTES.register("armor_negation", () -> new RangedAttribute("attribute.name." + EpicFightMod.MODID + ".armor_negation", 0.0D, 0.0D, 100.0D).setSyncable(true));
	public static final RegistryObject<Attribute> IMPACT = ATTRIBUTES.register("impact", () -> new RangedAttribute("attribute.name." + EpicFightMod.MODID + ".impact", 0.0D, 0.0D, 1024.0).setSyncable(true));
	public static final RegistryObject<Attribute> OFFHAND_ATTACK_DAMAGE = ATTRIBUTES.register("offhand_attack_damage", () -> new RangedAttribute("offhand attack damage", 1.0D, 0.0D, 2048.0D));
	public static final RegistryObject<Attribute> OFFHAND_ATTACK_SPEED = ATTRIBUTES.register("offhand_attack_speed", () -> new RangedAttribute("offhand attack speed", 4.0D, 0.0D, 1024.0D).setSyncable(true));
	public static final RegistryObject<Attribute> OFFHAND_MAX_STRIKES = ATTRIBUTES.register("offhand_max_strikes", () -> new RangedAttribute("attribute.name." + EpicFightMod.MODID + ".offhand_max_strikes", 1.0D, 1.0D, 1024.0).setSyncable(true));
	public static final RegistryObject<Attribute> OFFHAND_ARMOR_NEGATION = ATTRIBUTES.register("offhand_armor_negation", () -> new RangedAttribute("attribute.name." + EpicFightMod.MODID + ".offhand_armor_negation", 0.0D, 0.0D, 100.0D).setSyncable(true));
	public static final RegistryObject<Attribute> OFFHAND_IMPACT = ATTRIBUTES.register("offhand_impact", () -> new RangedAttribute("attribute.name." + EpicFightMod.MODID + ".offhand_impact", 0.0D, 0.0D, 1024.0).setSyncable(true));
	public static final UUID ARMOR_NEGATION_MODIFIER = UUID.fromString("b0a7436e-5734-11eb-ae93-0242ac130002");
	public static final UUID MAX_STRIKE_MODIFIER = UUID.fromString("b0a745b2-5734-11eb-ae93-0242ac130002");
	public static final UUID IMPACT_MODIFIER = UUID.fromString("b0a746ac-5734-11eb-ae93-0242ac130002");
	public static final UUID ATTACK_DAMAGE_MODIFIER = UUID.fromString("1c224694-19f3-11ec-9621-0242ac130002");
	public static final UUID ATTACK_SPEED_MODIFIER = UUID.fromString("1c2249f0-19f3-11ec-9621-0242ac130002");
	//tkk
	public static final UUID MOVE_SPEED_MODIFIER = UUID.fromString("3C8F01BF-20F2-63A0-D65B-639C307A4326");
	public static final UUID MAX_STAMINA_MODIFIER = UUID.fromString("36BDF2BD-1756-D2A3-97D6-F166A2F5BE79");
	public static final UUID ENTITY_GRAVITY_MODIFIER = UUID.fromString("1E22B41D-DD9E-6351-8B20-BC1B487F8BDC");
    
	public static void registerNewMobs(EntityAttributeCreationEvent event) {
		event.put(EpicFightEntities.WITHER_SKELETON_MINION.get(), AbstractSkeletonEntity.createAttributes().build());
		event.put(EpicFightEntities.WITHER_GHOST_CLONE.get(), WitherGhostClone.createAttributes().build());
	}
	
	public static void modifyExistingMobs(EntityAttributeModificationEvent event) {
		commonCreature(EntityType.CAVE_SPIDER, event);
		commonCreature(EntityType.EVOKER, event);
		commonCreature(EntityType.IRON_GOLEM, event);
		humanoid(EntityType.PILLAGER, event);
		commonCreature(EntityType.RAVAGER, event);
		commonCreature(EntityType.SPIDER, event);
		commonCreature(EntityType.VEX, event);
		humanoid(EntityType.VINDICATOR, event);
		humanoid(EntityType.WITCH, event);
		commonCreature(EntityType.HOGLIN, event);
		commonCreature(EntityType.ZOGLIN, event);
		commonCreature(EntityType.ENDER_DRAGON, event);
		commonCreature(EntityType.CREEPER, event);
		humanoid(EntityType.DROWNED, event);
		commonCreature(EntityType.ENDERMAN, event);
		humanoid(EntityType.HUSK, event);
		humanoid(EntityType.PIGLIN, event);
		humanoid(EntityType.PIGLIN_BRUTE, event);
		humanoid(EntityType.SKELETON, event);
		humanoid(EntityType.STRAY, event);
		humanoid(EntityType.WITHER_SKELETON, event);
		humanoid(EntityType.ZOMBIE, event);
		humanoid(EntityType.ZOMBIE_VILLAGER, event);
		humanoid(EntityType.ZOMBIFIED_PIGLIN, event);
		commonCreature(EpicFightEntities.WITHER_SKELETON_MINION.get(), event);
		player(EntityType.PLAYER, event);
		dragon(EntityType.ENDER_DRAGON, event);
		commonCreature(EntityType.WITHER, event);
		//tkk
		humanoid(CustomEntities.entityCustomNpc, event);
	}
    
    private static void commonCreature(EntityType<? extends LivingEntity> entityType, EntityAttributeModificationEvent event) {
		event.add(entityType, EpicFightAttributes.WEIGHT.get());
		event.add(entityType, EpicFightAttributes.ARMOR_NEGATION.get());
		event.add(entityType, EpicFightAttributes.IMPACT.get());
		event.add(entityType, EpicFightAttributes.MAX_STRIKES.get());
		event.add(entityType, EpicFightAttributes.STUN_ARMOR.get());
		//tkk
		event.add(entityType, EpicFightAttributes.OFFHAND_ATTACK_DAMAGE.get());
		event.add(entityType, EpicFightAttributes.OFFHAND_ATTACK_SPEED.get());
		event.add(entityType, EpicFightAttributes.OFFHAND_MAX_STRIKES.get());
		event.add(entityType, EpicFightAttributes.OFFHAND_ARMOR_NEGATION.get());
		event.add(entityType, EpicFightAttributes.OFFHAND_IMPACT.get());
	}
    
    private static void humanoid(EntityType<? extends LivingEntity> entityType, EntityAttributeModificationEvent event) {
    	commonCreature(entityType, event);
		event.add(entityType, EpicFightAttributes.OFFHAND_ATTACK_DAMAGE.get());
		event.add(entityType, EpicFightAttributes.OFFHAND_ATTACK_SPEED.get());
		event.add(entityType, EpicFightAttributes.OFFHAND_MAX_STRIKES.get());
		event.add(entityType, EpicFightAttributes.OFFHAND_ARMOR_NEGATION.get());
		event.add(entityType, EpicFightAttributes.OFFHAND_IMPACT.get());
		//tkk
		event.add(entityType, EpicFightAttributes.OFFHAND_ATTACK_DAMAGE.get());
		event.add(entityType, EpicFightAttributes.OFFHAND_ATTACK_SPEED.get());
		event.add(entityType, EpicFightAttributes.OFFHAND_MAX_STRIKES.get());
		event.add(entityType, EpicFightAttributes.OFFHAND_ARMOR_NEGATION.get());
		event.add(entityType, EpicFightAttributes.OFFHAND_IMPACT.get());
	}
    
    private static void player(EntityType<? extends LivingEntity> entityType, EntityAttributeModificationEvent event) {
    	humanoid(entityType, event);
		event.add(entityType, EpicFightAttributes.MAX_STAMINA.get());
	}
    
    private static void dragon(EntityType<? extends EnderDragonEntity> entityType, EntityAttributeModificationEvent event) {
    	commonCreature(entityType, event);
		event.add(entityType, Attributes.ATTACK_DAMAGE);
	}
    
	public static AttributeModifier getArmorNegationModifier(double value) {
		return new AttributeModifier(EpicFightAttributes.ARMOR_NEGATION_MODIFIER, EpicFightMod.MODID + ":weapon_modifier", value, AttributeModifier.Operation.ADDITION);
	}

	public static AttributeModifier getMaxStrikesModifier(int value) {
		return new AttributeModifier(EpicFightAttributes.MAX_STRIKE_MODIFIER, EpicFightMod.MODID + ":weapon_modifier", (double) value, AttributeModifier.Operation.ADDITION);
	}

	public static AttributeModifier getImpactModifier(double value) {
		return new AttributeModifier(EpicFightAttributes.IMPACT_MODIFIER, EpicFightMod.MODID + ":weapon_modifier", value, AttributeModifier.Operation.ADDITION);
	}

	public static AttributeModifier getDamageBonusModifier(double value) {
		return new AttributeModifier(ATTACK_DAMAGE_MODIFIER, EpicFightMod.MODID + ":weapon_modifier", value, AttributeModifier.Operation.ADDITION);
	}

	public static AttributeModifier getSpeedBonusModifier(double value) {
		return new AttributeModifier(ATTACK_SPEED_MODIFIER, EpicFightMod.MODID + ":weapon_modifier", value, AttributeModifier.Operation.ADDITION);
	}
	//tkk
	public static AttributeModifier getMoveSpeedModifier(double value) {
		return new AttributeModifier(MOVE_SPEED_MODIFIER, EpicFightMod.MODID + ":weapon_modifier", value, AttributeModifier.Operation.MULTIPLY_BASE);
	}
	public static AttributeModifier getMaxStaminaModifier(double value) {
		return new AttributeModifier(MAX_STAMINA_MODIFIER, EpicFightMod.MODID + ":weapon_modifier", value, AttributeModifier.Operation.ADDITION);
	}
	public static AttributeModifier getEntityGravityModifier(double value) {
		return new AttributeModifier(ENTITY_GRAVITY_MODIFIER, EpicFightMod.MODID + ":weapon_modifier", value, AttributeModifier.Operation.MULTIPLY_BASE);
	}
}