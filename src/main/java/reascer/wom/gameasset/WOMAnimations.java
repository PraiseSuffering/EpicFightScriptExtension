package reascer.wom.gameasset;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.SoundCategory;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.loading.FMLEnvironment;
import reascer.wom.animation.BasicMultipleAttackAnimation;
import reascer.wom.particle.WOMEpicFightParticle;
import yesman.epicfight.api.animation.property.AnimationProperty;
import yesman.epicfight.api.animation.types.AttackAnimation;
import yesman.epicfight.api.animation.types.BasicAttackAnimation;
import yesman.epicfight.api.animation.types.DashAttackAnimation;
import yesman.epicfight.api.animation.types.StaticAnimation;
import yesman.epicfight.api.client.model.ClientModels;
import yesman.epicfight.api.forgeevent.AnimationRegistryEvent;
import yesman.epicfight.api.model.Model;
import yesman.epicfight.api.utils.ExtendedDamageSource;
import yesman.epicfight.api.utils.math.ValueCorrector;
import yesman.epicfight.gameasset.EpicFightSounds;
import yesman.epicfight.gameasset.Models;
import yesman.epicfight.main.EpicFightMod;
import yesman.epicfight.particle.EpicFightParticles;
import yesman.epicfight.world.capabilities.entitypatch.player.PlayerPatch;

@Mod.EventBusSubscriber(modid = EpicFightMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class WOMAnimations {
    public static StaticAnimation AGONY_DASH;
    
    public static StaticAnimation LONGSWORD_TWOHAND_AUTO_1;

    public static StaticAnimation LONGSWORD_TWOHAND_AUTO_2;

    public static StaticAnimation LONGSWORD_TWOHAND_AUTO_3;

    public static StaticAnimation LONGSWORD_TWOHAND_AUTO_4;

    public static StaticAnimation GREATSWORD_TWOHAND_AUTO_1;

    public static StaticAnimation GREATSWORD_TWOHAND_AUTO_2;

    public static StaticAnimation GREATSWORD_TWOHAND_AUTO_3;

    public static StaticAnimation STAFF_AUTO_1;

    public static StaticAnimation STAFF_AUTO_2;

    public static StaticAnimation STAFF_AUTO_3;

    public static StaticAnimation STAFF_DASH;

    public static StaticAnimation AGONY_AIR_SLASH;

    public static StaticAnimation RUINE_AUTO_1;

    public static StaticAnimation RUINE_AUTO_2;

    public static StaticAnimation RUINE_AUTO_3;

    public static StaticAnimation RUINE_COMET;

    public static StaticAnimation RUINE_DASH;

    public static StaticAnimation TORMENT_AUTO_1;

    public static StaticAnimation TORMENT_AUTO_2;

    public static StaticAnimation TORMENT_AUTO_3;

    public static StaticAnimation TORMENT_DASH;

    public static StaticAnimation TORMENT_AIRSLAM;

    public static StaticAnimation TORMENT_BERSERK_AUTO_1;

    public static StaticAnimation TORMENT_BERSERK_AUTO_2;

    public static StaticAnimation TORMENT_BERSERK_DASH;

    public static StaticAnimation TORMENT_BERSERK_AIRSLAM;

    public static StaticAnimation KATANA_AUTO_1;

    public static StaticAnimation KATANA_AUTO_2;

    public static StaticAnimation KATANA_AUTO_3;

    public static StaticAnimation ENDERBLASTER_ONEHAND_AUTO_1;

    public static StaticAnimation ENDERBLASTER_ONEHAND_AUTO_2;

    public static StaticAnimation ENDERBLASTER_ONEHAND_AUTO_3;

    public static StaticAnimation ENDERBLASTER_ONEHAND_AUTO_4;

    public static StaticAnimation ENDERBLASTER_ONEHAND_DASH;

    public static StaticAnimation ENDERBLASTER_ONEHAND_JUMPKICK;

    public static StaticAnimation ANTITHEUS_AGRESSION;

    public static StaticAnimation ANTITHEUS_GUILLOTINE;

    public static StaticAnimation ANTITHEUS_AUTO_1;

    public static StaticAnimation ANTITHEUS_AUTO_2;

    public static StaticAnimation ANTITHEUS_AUTO_3;

    public static StaticAnimation ANTITHEUS_AUTO_4;


    //@SubscribeEvent
    public static void registerAnimations(AnimationRegistryEvent event) {
        //event.getRegistryMap().put("wom", WOMAnimations::build);
    }
    public static void build() {
        Models<?> models = FMLEnvironment.dist == Dist.CLIENT ? ClientModels.LOGICAL_CLIENT : Models.LOGICAL_SERVER;
        Model biped = models.biped;

        AGONY_AIR_SLASH = (new BasicAttackAnimation(0.0F, 0.45F, 0.6F, 0.75F, WOMColliders.AGONY_AIRSLASH, "Tool_R", "biped/wom/combat/agony_airslash", biped)).addProperty(AnimationProperty.AttackPhaseProperty.DAMAGE, ValueCorrector.multiplier(1.4F)).addProperty(AnimationProperty.AttackPhaseProperty.IMPACT, ValueCorrector.multiplier(1.1F)).addProperty(AnimationProperty.AttackPhaseProperty.STUN_TYPE, ExtendedDamageSource.StunType.LONG).addProperty(AnimationProperty.AttackAnimationProperty.BASIS_ATTACK_SPEED, Float.valueOf(1.3F)).addProperty(AnimationProperty.AttackAnimationProperty.LOCK_ROTATION, Boolean.valueOf(false)).addProperty(AnimationProperty.AttackAnimationProperty.ROTATE_X, Boolean.valueOf(false)).addProperty(AnimationProperty.ActionAnimationProperty.MOVE_VERTICAL, Boolean.valueOf(true)).addProperty(AnimationProperty.ActionAnimationProperty.STOP_MOVEMENT, Boolean.valueOf(false));
        AGONY_DASH = (StaticAnimation)(new DashAttackAnimation(0.15F, 0.05F, 0.2F, 0.458F, 0.625F, WOMColliders.AGONY_AIRSLASH, "Tool_R", "biped/wom/combat/agony_dash", biped)).addProperty(AnimationProperty.AttackAnimationProperty.ROTATE_X, Boolean.valueOf(false)).addProperty(AnimationProperty.AttackAnimationProperty.BASIS_ATTACK_SPEED, Float.valueOf(1.1F)).addProperty(AnimationProperty.AttackPhaseProperty.IMPACT, ValueCorrector.multiplier(1.4F));
        ANTITHEUS_AGRESSION = (new BasicMultipleAttackAnimation(0.05F, "biped/wom/combat/antitheus_agression", biped, new AttackAnimation.Phase[] { new AttackAnimation.Phase(0.0F, 0.25F, 0.4F, 0.6F, 0.6F, "Tool_R", null), new AttackAnimation.Phase(0.6F, 0.6F, 0.8F, 0.85F, Float.MAX_VALUE, "Tool_R", null) })).addProperty(AnimationProperty.AttackPhaseProperty.DAMAGE, ValueCorrector.multiplier(0.35F)).addProperty(AnimationProperty.AttackPhaseProperty.STUN_TYPE, ExtendedDamageSource.StunType.HOLD).addProperty(AnimationProperty.AttackPhaseProperty.DAMAGE, ValueCorrector.multiplier(0.55F), 1).addProperty(AnimationProperty.AttackPhaseProperty.STUN_TYPE, ExtendedDamageSource.StunType.LONG, 1).addProperty(AnimationProperty.AttackPhaseProperty.IMPACT, ValueCorrector.adder(-1.0F), 1).addProperty(AnimationProperty.AttackPhaseProperty.PARTICLE, WOMEpicFightParticle.ANTITHEUS_HIT).addProperty(AnimationProperty.AttackPhaseProperty.HIT_SOUND, EpicFightSounds.EVISCERATE, 1).addProperty(AnimationProperty.AttackPhaseProperty.PARTICLE, WOMEpicFightParticle.ANTITHEUS_HIT, 1).addProperty(AnimationProperty.AttackAnimationProperty.COLLIDER_ADDER, Integer.valueOf(1)).addProperty(AnimationProperty.AttackAnimationProperty.ROTATE_X, Boolean.valueOf(false)).addProperty(AnimationProperty.AttackAnimationProperty.BASIS_ATTACK_SPEED, Float.valueOf(1.9F)).addProperty(AnimationProperty.ActionAnimationProperty.CANCELABLE_MOVE, Boolean.valueOf(false));

        ANTITHEUS_AUTO_1 = (new BasicMultipleAttackAnimation(0.05F, "biped/wom/combat/antitheus_auto_1", biped, new AttackAnimation.Phase[] { new AttackAnimation.Phase(0.0F, 0.35F, 0.6F, 0.6F, 0.6F, "Tool_R", null), new AttackAnimation.Phase(0.6F, 0.7F, 0.9F, 0.9F, Float.MAX_VALUE, "Tool_R", null) })).addProperty(AnimationProperty.AttackPhaseProperty.DAMAGE, ValueCorrector.multiplier(0.55F)).addProperty(AnimationProperty.AttackPhaseProperty.DAMAGE, ValueCorrector.multiplier(0.75F), 1).addProperty(AnimationProperty.AttackPhaseProperty.PARTICLE, WOMEpicFightParticle.ANTITHEUS_HIT).addProperty(AnimationProperty.AttackPhaseProperty.PARTICLE, WOMEpicFightParticle.ANTITHEUS_HIT, 1).addProperty(AnimationProperty.AttackAnimationProperty.COLLIDER_ADDER, Integer.valueOf(1)).addProperty(AnimationProperty.AttackAnimationProperty.BASIS_ATTACK_SPEED, Float.valueOf(1.9F)).addProperty(AnimationProperty.AttackAnimationProperty.ROTATE_X, Boolean.valueOf(false)).addProperty(AnimationProperty.AttackAnimationProperty.FIXED_MOVE_DISTANCE, Boolean.valueOf(true)).addProperty(AnimationProperty.StaticAnimationProperty.EVENTS, new StaticAnimation.Event[] { StaticAnimation.Event.create(0.05F, entitypatch -> ((LivingEntity)entitypatch.getOriginal()).level.playSound((PlayerEntity) entitypatch.getOriginal(), entitypatch.getOriginal(), EpicFightSounds.WHOOSH_BIG, SoundCategory.PLAYERS, 1.0F, 1.0F), StaticAnimation.Event.Side.CLIENT)});


        ANTITHEUS_AUTO_2 = (new BasicAttackAnimation(0.05F, 0.2F, 0.4F, 0.4F, null, "Tool_R", "biped/wom/combat/antitheus_auto_2", biped)).addProperty(AnimationProperty.AttackPhaseProperty.DAMAGE, ValueCorrector.multiplier(1.2F)).addProperty(AnimationProperty.AttackPhaseProperty.PARTICLE, WOMEpicFightParticle.ANTITHEUS_HIT).addProperty(AnimationProperty.AttackAnimationProperty.BASIS_ATTACK_SPEED, Float.valueOf(1.9F)).addProperty(AnimationProperty.AttackAnimationProperty.ROTATE_X, Boolean.valueOf(false));
        ANTITHEUS_AUTO_3 = (new BasicMultipleAttackAnimation(0.05F, "biped/wom/combat/antitheus_auto_3", biped, new AttackAnimation.Phase[] { new AttackAnimation.Phase(0.0F, 0.3F, 0.55F, 0.55F, 0.55F, "Tool_R", null), new AttackAnimation.Phase(0.55F, 0.65F, 0.75F, 0.8F, Float.MAX_VALUE, "Tool_R", null) })).addProperty(AnimationProperty.AttackPhaseProperty.DAMAGE, ValueCorrector.multiplier(0.6F)).addProperty(AnimationProperty.AttackPhaseProperty.DAMAGE, ValueCorrector.multiplier(0.8F), 1).addProperty(AnimationProperty.AttackPhaseProperty.PARTICLE, WOMEpicFightParticle.ANTITHEUS_HIT).addProperty(AnimationProperty.AttackPhaseProperty.PARTICLE, WOMEpicFightParticle.ANTITHEUS_HIT, 1).addProperty(AnimationProperty.AttackPhaseProperty.IMPACT, ValueCorrector.adder(-1.0F), 1).addProperty(AnimationProperty.AttackPhaseProperty.STUN_TYPE, ExtendedDamageSource.StunType.LONG, 1).addProperty(AnimationProperty.AttackAnimationProperty.ROTATE_X, Boolean.valueOf(false)).addProperty(AnimationProperty.AttackAnimationProperty.BASIS_ATTACK_SPEED, Float.valueOf(1.9F));
        ANTITHEUS_AUTO_4 = (new BasicAttackAnimation(0.05F, 0.55F, 0.75F, 0.9F, null, "Tool_R", "biped/wom/combat/antitheus_auto_4", biped)).addProperty(AnimationProperty.AttackPhaseProperty.DAMAGE, ValueCorrector.multiplier(1.6F)).addProperty(AnimationProperty.AttackPhaseProperty.STUN_TYPE, ExtendedDamageSource.StunType.LONG).addProperty(AnimationProperty.AttackPhaseProperty.PARTICLE, WOMEpicFightParticle.ANTITHEUS_HIT).addProperty(AnimationProperty.AttackAnimationProperty.BASIS_ATTACK_SPEED, Float.valueOf(1.8F)).addProperty(AnimationProperty.AttackAnimationProperty.COLLIDER_ADDER, Integer.valueOf(2)).addProperty(AnimationProperty.AttackAnimationProperty.ROTATE_X, Boolean.valueOf(false));

        ANTITHEUS_GUILLOTINE = (new BasicMultipleAttackAnimation(0.0F, "biped/wom/combat/antitheus_guillotine", biped, new AttackAnimation.Phase[] { new AttackAnimation.Phase(0.0F, 0.6F, 0.8F, 0.8F, 0.8F, "Root", WOMColliders.ANTITHEUS_GUILLOTINE), new AttackAnimation.Phase(0.8F, 0.9F, 1.0F, 1.1F, Float.MAX_VALUE, "Root", WOMColliders.ANTITHEUS_GUILLOTINE) })).addProperty(AnimationProperty.AttackPhaseProperty.DAMAGE, ValueCorrector.multiplier(0.75F)).addProperty(AnimationProperty.AttackPhaseProperty.MAX_STRIKES, ValueCorrector.setter(10.0F)).addProperty(AnimationProperty.AttackPhaseProperty.DAMAGE, ValueCorrector.multiplier(0.95F), 1).addProperty(AnimationProperty.AttackPhaseProperty.MAX_STRIKES, ValueCorrector.setter(10.0F), 1).addProperty(AnimationProperty.AttackPhaseProperty.IMPACT, ValueCorrector.multiplier(1.1F), 1).addProperty(AnimationProperty.AttackPhaseProperty.STUN_TYPE, ExtendedDamageSource.StunType.LONG, 1).addProperty(AnimationProperty.AttackPhaseProperty.PARTICLE, WOMEpicFightParticle.ANTITHEUS_HIT).addProperty(AnimationProperty.AttackPhaseProperty.PARTICLE, WOMEpicFightParticle.ANTITHEUS_HIT, 1).addProperty(AnimationProperty.AttackAnimationProperty.COLLIDER_ADDER, Integer.valueOf(2)).addProperty(AnimationProperty.AttackAnimationProperty.BASIS_ATTACK_SPEED, Float.valueOf(1.9F)).addProperty(AnimationProperty.AttackAnimationProperty.FIXED_MOVE_DISTANCE, Boolean.valueOf(true)).addProperty(AnimationProperty.AttackAnimationProperty.LOCK_ROTATION, Boolean.valueOf(false)).addProperty(AnimationProperty.AttackAnimationProperty.ROTATE_X, Boolean.valueOf(false)).addProperty(AnimationProperty.ActionAnimationProperty.MOVE_VERTICAL, Boolean.valueOf(true)).addProperty(AnimationProperty.ActionAnimationProperty.STOP_MOVEMENT, Boolean.valueOf(false)).addProperty(AnimationProperty.ActionAnimationProperty.CANCELABLE_MOVE, Boolean.valueOf(false)).addProperty(AnimationProperty.StaticAnimationProperty.EVENTS, new StaticAnimation.Event[] { StaticAnimation.Event.create(0.05F, entitypatch -> ((LivingEntity)entitypatch.getOriginal()).level.playSound((PlayerEntity) entitypatch.getOriginal(), entitypatch.getOriginal(), EpicFightSounds.WHOOSH_BIG, SoundCategory.PLAYERS, 1.0F, 1.0F), StaticAnimation.Event.Side.CLIENT)});
        ENDERBLASTER_ONEHAND_AUTO_1 = (StaticAnimation)(new BasicMultipleAttackAnimation(0.05F, "biped/wom/combat/enderblaster_onehand_auto_1", biped, new AttackAnimation.Phase[] { new AttackAnimation.Phase(0.0F, 0.15F, 0.24F, 0.25F, 0.25F, "Leg_L", WOMColliders.KICK), new AttackAnimation.Phase(0.25F, 0.3F, 0.34F, 0.35F, Float.MAX_VALUE, "Leg_L", WOMColliders.KICK) })).addProperty(AnimationProperty.AttackPhaseProperty.DAMAGE, ValueCorrector.multiplier(0.35F)).addProperty(AnimationProperty.AttackPhaseProperty.PARTICLE, EpicFightParticles.HIT_BLUNT).addProperty(AnimationProperty.AttackPhaseProperty.HIT_SOUND, EpicFightSounds.BLUNT_HIT).addProperty(AnimationProperty.AttackPhaseProperty.STUN_TYPE, ExtendedDamageSource.StunType.HOLD).addProperty(AnimationProperty.AttackPhaseProperty.DAMAGE, ValueCorrector.multiplier(0.45F), 1).addProperty(AnimationProperty.AttackPhaseProperty.IMPACT, ValueCorrector.multiplier(2.0F), 1).addProperty(AnimationProperty.AttackPhaseProperty.STUN_TYPE, ExtendedDamageSource.StunType.HOLD, 1).addProperty(AnimationProperty.AttackPhaseProperty.PARTICLE, EpicFightParticles.HIT_BLUNT, 1).addProperty(AnimationProperty.AttackPhaseProperty.HIT_SOUND, EpicFightSounds.BLUNT_HIT, 1).addProperty(AnimationProperty.AttackAnimationProperty.ATTACK_SPEED_FACTOR, Float.valueOf(0.0F));
        ENDERBLASTER_ONEHAND_AUTO_2 = (StaticAnimation)(new BasicMultipleAttackAnimation(0.05F, "biped/wom/combat/enderblaster_onehand_auto_2", biped, new AttackAnimation.Phase[] { new AttackAnimation.Phase(0.0F, 0.15F, 0.24F, 0.25F, 0.25F, "Leg_R", WOMColliders.KICK), new AttackAnimation.Phase(0.25F, 0.3F, 0.44F, 0.45F, 0.45F, "Tool_R", null), new AttackAnimation.Phase(0.45F, 0.5F, 0.54F, 0.55F, Float.MAX_VALUE, "Elbow_L", WOMColliders.KNEE) })).addProperty(AnimationProperty.AttackPhaseProperty.DAMAGE, ValueCorrector.multiplier(0.45F)).addProperty(AnimationProperty.AttackPhaseProperty.PARTICLE, EpicFightParticles.HIT_BLUNT).addProperty(AnimationProperty.AttackPhaseProperty.HIT_SOUND, EpicFightSounds.BLUNT_HIT).addProperty(AnimationProperty.AttackPhaseProperty.DAMAGE, ValueCorrector.multiplier(0.55F), 1).addProperty(AnimationProperty.AttackPhaseProperty.PARTICLE, EpicFightParticles.HIT_BLUNT, 2).addProperty(AnimationProperty.AttackPhaseProperty.HIT_SOUND, EpicFightSounds.BLUNT_HIT_HARD, 2).addProperty(AnimationProperty.AttackPhaseProperty.STUN_TYPE, ExtendedDamageSource.StunType.HOLD, 2).addProperty(AnimationProperty.AttackPhaseProperty.DAMAGE, ValueCorrector.multiplier(0.35F), 2).addProperty(AnimationProperty.AttackPhaseProperty.IMPACT, ValueCorrector.multiplier(2.0F), 2).addProperty(AnimationProperty.AttackAnimationProperty.ATTACK_SPEED_FACTOR, Float.valueOf(0.0F));
        ENDERBLASTER_ONEHAND_AUTO_3 = (StaticAnimation)(new BasicMultipleAttackAnimation(0.1F, "biped/wom/combat/enderblaster_onehand_auto_3", biped, new AttackAnimation.Phase[] { new AttackAnimation.Phase(0.0F, 0.05F, 0.19F, 0.2F, 0.2F, "Tool_R", null), new AttackAnimation.Phase(0.2F, 0.25F, 0.35F, 0.35F, Float.MAX_VALUE, "Tool_L", null) })).addProperty(AnimationProperty.AttackPhaseProperty.DAMAGE, ValueCorrector.multiplier(0.55F)).addProperty(AnimationProperty.AttackPhaseProperty.DAMAGE, ValueCorrector.multiplier(0.55F), 1).addProperty(AnimationProperty.AttackPhaseProperty.IMPACT, ValueCorrector.multiplier(2.0F), 1).addProperty(AnimationProperty.AttackPhaseProperty.STUN_TYPE, ExtendedDamageSource.StunType.HOLD, 1).addProperty(AnimationProperty.AttackAnimationProperty.ATTACK_SPEED_FACTOR, Float.valueOf(0.0F));
        ENDERBLASTER_ONEHAND_AUTO_4 = (StaticAnimation)(new BasicMultipleAttackAnimation(0.05F, "biped/wom/combat/enderblaster_onehand_auto_4", biped, new AttackAnimation.Phase[] { new AttackAnimation.Phase(0.0F, 0.3F, 0.4F, 0.55F, Float.MAX_VALUE, "Leg_L", WOMColliders.KICK_HUGE) })).addProperty(AnimationProperty.AttackPhaseProperty.DAMAGE, ValueCorrector.multiplier(0.75F)).addProperty(AnimationProperty.AttackPhaseProperty.MAX_STRIKES, ValueCorrector.multiplier(2.0F)).addProperty(AnimationProperty.AttackPhaseProperty.STUN_TYPE, ExtendedDamageSource.StunType.LONG).addProperty(AnimationProperty.AttackPhaseProperty.PARTICLE, EpicFightParticles.HIT_BLUNT).addProperty(AnimationProperty.AttackPhaseProperty.HIT_SOUND, EpicFightSounds.BLUNT_HIT_HARD).addProperty(AnimationProperty.AttackAnimationProperty.ATTACK_SPEED_FACTOR, Float.valueOf(0.0F));
        ENDERBLASTER_ONEHAND_DASH = (StaticAnimation)(new BasicMultipleAttackAnimation(0.05F, "biped/wom/combat/enderblaster_onehand_dash", biped, new AttackAnimation.Phase[] { new AttackAnimation.Phase(0.0F, 0.15F, 0.3F, 0.4F, 0.4F, "Leg_L", WOMColliders.KICK_HUGE), new AttackAnimation.Phase(0.4F, 0.4F, 0.75F, 0.85F, Float.MAX_VALUE, "Leg_L", WOMColliders.KICK_HUGE) })).addProperty(AnimationProperty.AttackPhaseProperty.DAMAGE, ValueCorrector.multiplier(0.45F)).addProperty(AnimationProperty.AttackPhaseProperty.PARTICLE, EpicFightParticles.HIT_BLUNT).addProperty(AnimationProperty.AttackPhaseProperty.HIT_SOUND, EpicFightSounds.BLUNT_HIT).addProperty(AnimationProperty.AttackPhaseProperty.DAMAGE, ValueCorrector.multiplier(0.45F), 1).addProperty(AnimationProperty.AttackPhaseProperty.IMPACT, ValueCorrector.multiplier(4.0F), 1).addProperty(AnimationProperty.AttackPhaseProperty.STUN_TYPE, ExtendedDamageSource.StunType.HOLD, 1).addProperty(AnimationProperty.AttackPhaseProperty.PARTICLE, EpicFightParticles.HIT_BLUNT, 1).addProperty(AnimationProperty.AttackPhaseProperty.HIT_SOUND, EpicFightSounds.BLUNT_HIT_HARD, 1).addProperty(AnimationProperty.AttackAnimationProperty.ATTACK_SPEED_FACTOR, Float.valueOf(0.0F)).addProperty(AnimationProperty.ActionAnimationProperty.CANCELABLE_MOVE, Boolean.valueOf(false));
        ENDERBLASTER_ONEHAND_JUMPKICK = (StaticAnimation)(new BasicMultipleAttackAnimation(0.05F, "biped/wom/combat/enderblaster_onehand_jumpkick", biped, new AttackAnimation.Phase[] { new AttackAnimation.Phase(0.0F, 0.05F, 0.24F, 0.25F, 0.25F, "Leg_L", WOMColliders.KICK_HUGE), new AttackAnimation.Phase(0.25F, 0.3F, 0.4F, 0.5F, 0.5F, "Leg_R", WOMColliders.KICK_HUGE), new AttackAnimation.Phase(0.5F, 0.55F, 0.7F, 0.8F, Float.MAX_VALUE, "Leg_R", WOMColliders.KICK_HUGE) })).addProperty(AnimationProperty.AttackPhaseProperty.DAMAGE, ValueCorrector.multiplier(0.35F)).addProperty(AnimationProperty.AttackPhaseProperty.PARTICLE, EpicFightParticles.HIT_BLUNT).addProperty(AnimationProperty.AttackPhaseProperty.HIT_SOUND, EpicFightSounds.BLUNT_HIT).addProperty(AnimationProperty.AttackPhaseProperty.DAMAGE, ValueCorrector.multiplier(0.35F), 1).addProperty(AnimationProperty.AttackPhaseProperty.PARTICLE, EpicFightParticles.HIT_BLUNT, 1).addProperty(AnimationProperty.AttackPhaseProperty.HIT_SOUND, EpicFightSounds.BLUNT_HIT, 1).addProperty(AnimationProperty.AttackPhaseProperty.DAMAGE, ValueCorrector.multiplier(0.5F), 2).addProperty(AnimationProperty.AttackPhaseProperty.STUN_TYPE, ExtendedDamageSource.StunType.LONG, 2).addProperty(AnimationProperty.AttackPhaseProperty.PARTICLE, EpicFightParticles.HIT_BLUNT, 2).addProperty(AnimationProperty.AttackPhaseProperty.HIT_SOUND, EpicFightSounds.BLUNT_HIT, 2).addProperty(AnimationProperty.AttackAnimationProperty.ATTACK_SPEED_FACTOR, Float.valueOf(0.0F)).addProperty(AnimationProperty.ActionAnimationProperty.MOVE_VERTICAL, Boolean.valueOf(true)).addProperty(AnimationProperty.ActionAnimationProperty.CANCELABLE_MOVE, Boolean.valueOf(false)).addProperty(AnimationProperty.ActionAnimationProperty.STOP_MOVEMENT, Boolean.valueOf(false));
        KATANA_AUTO_1 = (StaticAnimation)(new BasicAttackAnimation(0.1F, 0.05F, 0.2F, 0.25F, null, "Tool_R", "biped/wom/combat/katana_auto_1", biped)).addProperty(AnimationProperty.AttackAnimationProperty.BASIS_ATTACK_SPEED, Float.valueOf(1.5F));
        KATANA_AUTO_2 = (StaticAnimation)(new BasicMultipleAttackAnimation(0.05F, "biped/wom/combat/katana_auto_2", biped, new AttackAnimation.Phase[] { new AttackAnimation.Phase(0.0F, 0.15F, 0.3F, 0.4F, 0.4F, "Tool_R", null), new AttackAnimation.Phase(0.4F, 0.4F, 0.65F, 0.65F, Float.MAX_VALUE, "Tool_R", null) })).addProperty(AnimationProperty.AttackPhaseProperty.DAMAGE, ValueCorrector.multiplier(0.5F), 0).addProperty(AnimationProperty.AttackPhaseProperty.DAMAGE, ValueCorrector.multiplier(0.6F), 1).addProperty(AnimationProperty.AttackPhaseProperty.IMPACT, ValueCorrector.multiplier(2.0F), 1).addProperty(AnimationProperty.AttackAnimationProperty.BASIS_ATTACK_SPEED, Float.valueOf(1.5F));
        KATANA_AUTO_3 = (StaticAnimation)(new BasicAttackAnimation(0.05F, 0.25F, 0.4F, 0.75F, null, "Tool_R", "biped/wom/combat/katana_auto_3", biped)).addProperty(AnimationProperty.AttackPhaseProperty.DAMAGE, ValueCorrector.multiplier(1.2F)).addProperty(AnimationProperty.AttackPhaseProperty.STUN_TYPE, ExtendedDamageSource.StunType.LONG).addProperty(AnimationProperty.AttackAnimationProperty.BASIS_ATTACK_SPEED, Float.valueOf(1.5F));
        RUINE_AUTO_1 = (StaticAnimation)(new BasicAttackAnimation(0.25F, 0.25F, 0.45F, 0.5F, null, "Tool_R", "biped/wom/combat/ruine_auto_1", biped)).addProperty(AnimationProperty.AttackPhaseProperty.DAMAGE, ValueCorrector.multiplier(1.0F)).addProperty(AnimationProperty.AttackAnimationProperty.BASIS_ATTACK_SPEED, Float.valueOf(1.0F));
        RUINE_AUTO_2 = (StaticAnimation)(new BasicMultipleAttackAnimation(0.15F, "biped/wom/combat/ruine_auto_2", biped, new AttackAnimation.Phase[] { new AttackAnimation.Phase(0.0F, 0.05F, 0.3F, 0.65F, 0.65F, "Tool_R", null), new AttackAnimation.Phase(0.65F, 0.65F, 0.8F, 0.8F, Float.MAX_VALUE, "Tool_R", null) })).addProperty(AnimationProperty.AttackPhaseProperty.DAMAGE, ValueCorrector.multiplier(0.55F)).addProperty(AnimationProperty.AttackPhaseProperty.DAMAGE, ValueCorrector.multiplier(0.65F), 1).addProperty(AnimationProperty.AttackAnimationProperty.BASIS_ATTACK_SPEED, Float.valueOf(1.25F));
        RUINE_AUTO_3 = (StaticAnimation)(new BasicAttackAnimation(0.15F, 0.15F, 0.35F, 0.7F, null, "Tool_R", "biped/wom/combat/ruine_auto_3", biped)).addProperty(AnimationProperty.AttackPhaseProperty.DAMAGE, ValueCorrector.multiplier(1.2F)).addProperty(AnimationProperty.AttackPhaseProperty.IMPACT, ValueCorrector.setter(1.5F)).addProperty(AnimationProperty.AttackPhaseProperty.STUN_TYPE, ExtendedDamageSource.StunType.LONG).addProperty(AnimationProperty.AttackAnimationProperty.BASIS_ATTACK_SPEED, Float.valueOf(1.25F)).addProperty(AnimationProperty.ActionAnimationProperty.CANCELABLE_MOVE, Boolean.valueOf(false));
        RUINE_DASH = (StaticAnimation)(new BasicAttackAnimation(0.05F, 0.35F, 0.35F, 0.55F, 0.55F, null, "Tool_R", "biped/wom/combat/ruine_dash", biped)).addProperty(AnimationProperty.AttackAnimationProperty.ROTATE_X, Boolean.valueOf(false)).addProperty(AnimationProperty.AttackPhaseProperty.DAMAGE, ValueCorrector.multiplier(1.2F)).addProperty(AnimationProperty.AttackPhaseProperty.IMPACT, ValueCorrector.multiplier(1.2F)).addProperty(AnimationProperty.AttackAnimationProperty.BASIS_ATTACK_SPEED, Float.valueOf(1.0F)).addProperty(AnimationProperty.ActionAnimationProperty.CANCELABLE_MOVE, Boolean.valueOf(false));
        RUINE_COMET = (new BasicAttackAnimation(0.15F, 0.25F, 0.55F, 0.75F, WOMColliders.RUINE_COMET, "Tool_R", "biped/wom/combat/ruine_comet", biped)).addProperty(AnimationProperty.AttackPhaseProperty.DAMAGE, ValueCorrector.multiplier(1.5F)).addProperty(AnimationProperty.AttackPhaseProperty.IMPACT, ValueCorrector.multiplier(1.2F)).addProperty(AnimationProperty.AttackAnimationProperty.BASIS_ATTACK_SPEED, Float.valueOf(1.25F)).addProperty(AnimationProperty.AttackAnimationProperty.ROTATE_X, Boolean.valueOf(false)).addProperty(AnimationProperty.ActionAnimationProperty.MOVE_VERTICAL, Boolean.valueOf(true)).addProperty(AnimationProperty.ActionAnimationProperty.STOP_MOVEMENT, Boolean.valueOf(false)).addProperty(AnimationProperty.ActionAnimationProperty.CANCELABLE_MOVE, Boolean.valueOf(false));
        STAFF_AUTO_1 = (StaticAnimation)(new BasicMultipleAttackAnimation(0.05F, "biped/wom/combat/staff_auto_1", biped, new AttackAnimation.Phase[] { new AttackAnimation.Phase(0.0F, 0.1F, 0.19F, 0.2F, 0.2F, "Tool_R", null), new AttackAnimation.Phase(0.2F, 0.25F, 0.34F, 0.35F, 0.35F, "Tool_R", null), new AttackAnimation.Phase(0.35F, 0.4F, 0.49F, 0.5F, 0.5F, "Tool_R", null), new AttackAnimation.Phase(0.5F, 0.55F, 0.7F, 0.7F, Float.MAX_VALUE, "Tool_R", null) })).addProperty(AnimationProperty.AttackPhaseProperty.DAMAGE, ValueCorrector.multiplier(0.4F)).addProperty(AnimationProperty.AttackPhaseProperty.PARTICLE, EpicFightParticles.HIT_BLUNT).addProperty(AnimationProperty.AttackPhaseProperty.DAMAGE, ValueCorrector.multiplier(0.4F), 1).addProperty(AnimationProperty.AttackPhaseProperty.PARTICLE, EpicFightParticles.HIT_BLUNT, 1).addProperty(AnimationProperty.AttackPhaseProperty.DAMAGE, ValueCorrector.multiplier(0.4F), 2).addProperty(AnimationProperty.AttackPhaseProperty.PARTICLE, EpicFightParticles.HIT_BLUNT, 2).addProperty(AnimationProperty.AttackPhaseProperty.DAMAGE, ValueCorrector.multiplier(0.4F), 3).addProperty(AnimationProperty.AttackPhaseProperty.PARTICLE, EpicFightParticles.HIT_BLUNT, 3).addProperty(AnimationProperty.AttackAnimationProperty.BASIS_ATTACK_SPEED, Float.valueOf(2.3F));
        STAFF_AUTO_2 = (StaticAnimation)(new BasicMultipleAttackAnimation(0.05F, "biped/wom/combat/staff_auto_2", biped, new AttackAnimation.Phase[] { new AttackAnimation.Phase(0.0F, 0.15F, 0.25F, 0.35F, 0.35F, "Tool_R", null), new AttackAnimation.Phase(0.35F, 0.35F, 0.45F, 0.55F, 0.55F, "Tool_R", null), new AttackAnimation.Phase(0.55F, 0.55F, 0.65F, 0.65F, Float.MAX_VALUE, "Tool_R", null) })).addProperty(AnimationProperty.AttackPhaseProperty.DAMAGE, ValueCorrector.multiplier(0.6F)).addProperty(AnimationProperty.AttackPhaseProperty.PARTICLE, EpicFightParticles.HIT_BLUNT).addProperty(AnimationProperty.AttackPhaseProperty.DAMAGE, ValueCorrector.multiplier(0.6F), 1).addProperty(AnimationProperty.AttackPhaseProperty.PARTICLE, EpicFightParticles.HIT_BLUNT, 1).addProperty(AnimationProperty.AttackPhaseProperty.DAMAGE, ValueCorrector.multiplier(0.6F), 2).addProperty(AnimationProperty.AttackPhaseProperty.PARTICLE, EpicFightParticles.HIT_BLUNT, 2).addProperty(AnimationProperty.AttackAnimationProperty.FIXED_MOVE_DISTANCE, Boolean.valueOf(true)).addProperty(AnimationProperty.AttackAnimationProperty.BASIS_ATTACK_SPEED, Float.valueOf(2.3F));
        STAFF_AUTO_3 = (StaticAnimation)(new BasicMultipleAttackAnimation(0.05F, "biped/wom/combat/staff_auto_3", biped, new AttackAnimation.Phase[] { new AttackAnimation.Phase(0.0F, 0.15F, 0.45F, 0.65F, 0.65F, "Tool_R", null), new AttackAnimation.Phase(0.65F, 0.65F, 0.85F, 0.85F, Float.MAX_VALUE, "Tool_R", null) })).addProperty(AnimationProperty.AttackPhaseProperty.DAMAGE, ValueCorrector.multiplier(0.95F)).addProperty(AnimationProperty.AttackPhaseProperty.IMPACT, ValueCorrector.multiplier(1.2F)).addProperty(AnimationProperty.AttackPhaseProperty.PARTICLE, EpicFightParticles.HIT_BLUNT).addProperty(AnimationProperty.AttackPhaseProperty.DAMAGE, ValueCorrector.multiplier(0.95F), 1).addProperty(AnimationProperty.AttackPhaseProperty.IMPACT, ValueCorrector.multiplier(1.2F), 1).addProperty(AnimationProperty.AttackPhaseProperty.PARTICLE, EpicFightParticles.HIT_BLUNT, 1).addProperty(AnimationProperty.AttackAnimationProperty.BASIS_ATTACK_SPEED, Float.valueOf(2.3F));
        STAFF_DASH = (StaticAnimation)(new BasicMultipleAttackAnimation(0.05F, "biped/wom/combat/staff_dash", biped, new AttackAnimation.Phase[] { new AttackAnimation.Phase(0.0F, 0.15F, 0.25F, 0.3F, 0.3F, "Tool_R", null), new AttackAnimation.Phase(0.3F, 0.3F, 0.4F, 0.5F, Float.MAX_VALUE, "Tool_R", null) })).addProperty(AnimationProperty.AttackPhaseProperty.DAMAGE, ValueCorrector.multiplier(0.85F)).addProperty(AnimationProperty.AttackPhaseProperty.IMPACT, ValueCorrector.multiplier(1.4F)).addProperty(AnimationProperty.AttackPhaseProperty.PARTICLE, EpicFightParticles.HIT_BLUNT).addProperty(AnimationProperty.AttackPhaseProperty.DAMAGE, ValueCorrector.multiplier(0.85F), 1).addProperty(AnimationProperty.AttackPhaseProperty.STUN_TYPE, ExtendedDamageSource.StunType.LONG, 1).addProperty(AnimationProperty.AttackPhaseProperty.PARTICLE, EpicFightParticles.HIT_BLUNT, 1).addProperty(AnimationProperty.AttackAnimationProperty.COLLIDER_ADDER, Integer.valueOf(1)).addProperty(AnimationProperty.AttackAnimationProperty.BASIS_ATTACK_SPEED, Float.valueOf(2.3F));
        TORMENT_AUTO_1 = (StaticAnimation)(new BasicAttackAnimation(0.2F, 0.2F, 0.5F, 0.6F, null, "Tool_R", "biped/wom/combat/torment_auto_1", biped)).addProperty(AnimationProperty.AttackAnimationProperty.ROTATE_X, Boolean.valueOf(false)).addProperty(AnimationProperty.AttackAnimationProperty.BASIS_ATTACK_SPEED, Float.valueOf(1.1F));
        TORMENT_AUTO_2 = (StaticAnimation)(new BasicAttackAnimation(0.25F, 0.05F, 0.3F, 0.55F, null, "Tool_R", "biped/wom/combat/torment_auto_2", biped)).addProperty(AnimationProperty.AttackAnimationProperty.ROTATE_X, Boolean.valueOf(false)).addProperty(AnimationProperty.AttackPhaseProperty.STUN_TYPE, ExtendedDamageSource.StunType.HOLD).addProperty(AnimationProperty.AttackPhaseProperty.DAMAGE, ValueCorrector.multiplier(0.2F)).addProperty(AnimationProperty.AttackPhaseProperty.IMPACT, ValueCorrector.multiplier(1.8F)).addProperty(AnimationProperty.AttackPhaseProperty.HIT_SOUND, EpicFightSounds.BLUNT_HIT_HARD).addProperty(AnimationProperty.AttackPhaseProperty.PARTICLE, EpicFightParticles.HIT_BLUNT).addProperty(AnimationProperty.AttackAnimationProperty.BASIS_ATTACK_SPEED, Float.valueOf(1.2F));
        TORMENT_AUTO_3 = (new BasicMultipleAttackAnimation(0.25F, "biped/wom/combat/torment_auto_3", biped, new AttackAnimation.Phase[] { new AttackAnimation.Phase(0.0F, 0.15F, 0.3F, 0.4F, 0.4F, "Tool_R", null), new AttackAnimation.Phase(0.4F, 0.4F, 0.55F, 0.9F, Float.MAX_VALUE, "Tool_R", null) })).addProperty(AnimationProperty.AttackPhaseProperty.DAMAGE, ValueCorrector.multiplier(0.75F)).addProperty(AnimationProperty.AttackPhaseProperty.DAMAGE, ValueCorrector.multiplier(1.2F), 1).addProperty(AnimationProperty.AttackPhaseProperty.STUN_TYPE, ExtendedDamageSource.StunType.LONG, 1).addProperty(AnimationProperty.AttackAnimationProperty.ROTATE_X, Boolean.valueOf(false)).addProperty(AnimationProperty.AttackAnimationProperty.BASIS_ATTACK_SPEED, Float.valueOf(1.2F));
        TORMENT_DASH = (new DashAttackAnimation(0.05F, 0.25F, 0.25F, 0.5F, 0.75F, null, "Tool_R", "biped/wom/combat/torment_dash", biped)).addProperty(AnimationProperty.AttackPhaseProperty.DAMAGE, ValueCorrector.multiplier(1.2F)).addProperty(AnimationProperty.AttackPhaseProperty.IMPACT, ValueCorrector.multiplier(1.2F)).addProperty(AnimationProperty.AttackAnimationProperty.ROTATE_X, Boolean.valueOf(false)).addProperty(AnimationProperty.AttackAnimationProperty.BASIS_ATTACK_SPEED, Float.valueOf(1.1F)).addProperty(AnimationProperty.ActionAnimationProperty.CANCELABLE_MOVE, Boolean.valueOf(false));
        TORMENT_AIRSLAM = (new BasicMultipleAttackAnimation(0.15F, "biped/wom/combat/torment_airslam", biped, new AttackAnimation.Phase[] { new AttackAnimation.Phase(0.0F, 0.45F, 0.55F, 0.6F, 0.6F, "Tool_R", null), new AttackAnimation.Phase(0.6F, 0.6F, 0.7F, 0.8F, Float.MAX_VALUE, "Root", WOMColliders.TORMENT_AIRSLAM) })).addProperty(AnimationProperty.AttackPhaseProperty.DAMAGE, ValueCorrector.multiplier(0.75F)).addProperty(AnimationProperty.AttackPhaseProperty.DAMAGE, ValueCorrector.multiplier(0.75F), 1).addProperty(AnimationProperty.AttackPhaseProperty.IMPACT, ValueCorrector.multiplier(1.5F), 1).addProperty(AnimationProperty.AttackAnimationProperty.BASIS_ATTACK_SPEED, Float.valueOf(1.0F)).addProperty(AnimationProperty.ActionAnimationProperty.CANCELABLE_MOVE, Boolean.valueOf(false));
        TORMENT_BERSERK_AUTO_1 = (StaticAnimation)(new BasicAttackAnimation(0.2F, 0.1F, 0.1F, 0.35F, 0.65F, null, "Tool_R", "biped/wom/skill/torment_berserk_auto_1", biped)).addProperty(AnimationProperty.AttackPhaseProperty.DAMAGE, ValueCorrector.multiplier(1.2F)).addProperty(AnimationProperty.AttackPhaseProperty.MAX_STRIKES, ValueCorrector.setter(8.0F)).addProperty(AnimationProperty.AttackAnimationProperty.BASIS_ATTACK_SPEED, Float.valueOf(0.75F)).addProperty(AnimationProperty.AttackAnimationProperty.ATTACK_SPEED_FACTOR, Float.valueOf(1.5F));
        TORMENT_BERSERK_AUTO_2 = (StaticAnimation)(new BasicAttackAnimation(0.2F, 0.1F, 0.1F, 0.55F, 0.7F, null, "Tool_R", "biped/wom/skill/torment_berserk_auto_2", biped)).addProperty(AnimationProperty.AttackPhaseProperty.DAMAGE, ValueCorrector.multiplier(1.2F)).addProperty(AnimationProperty.AttackPhaseProperty.MAX_STRIKES, ValueCorrector.setter(8.0F)).addProperty(AnimationProperty.AttackAnimationProperty.BASIS_ATTACK_SPEED, Float.valueOf(0.75F)).addProperty(AnimationProperty.AttackAnimationProperty.ATTACK_SPEED_FACTOR, Float.valueOf(1.5F));
        TORMENT_BERSERK_DASH = (StaticAnimation)(new BasicAttackAnimation(0.1F, 0.5F, 0.85F, 0.95F, null, "Tool_R", "biped/wom/skill/torment_berserk_dash", biped)).addProperty(AnimationProperty.AttackPhaseProperty.DAMAGE, ValueCorrector.multiplier(1.4F)).addProperty(AnimationProperty.AttackPhaseProperty.IMPACT, ValueCorrector.multiplier(1.2F)).addProperty(AnimationProperty.AttackPhaseProperty.MAX_STRIKES, ValueCorrector.setter(10.0F)).addProperty(AnimationProperty.AttackAnimationProperty.BASIS_ATTACK_SPEED, Float.valueOf(1.3F)).addProperty(AnimationProperty.AttackAnimationProperty.ATTACK_SPEED_FACTOR, Float.valueOf(1.5F)).addProperty(AnimationProperty.ActionAnimationProperty.CANCELABLE_MOVE, Boolean.valueOf(false));
        TORMENT_BERSERK_AIRSLAM = (new BasicMultipleAttackAnimation(0.15F, 0.4F, 0.7F, 1.2F, WOMColliders.TORMENT_BERSERK_AIRSLAM, "Root", "biped/wom/skill/torment_berserk_airslam", biped)).addProperty(AnimationProperty.AttackPhaseProperty.SWING_SOUND, EpicFightSounds.GROUND_SLAM).addProperty(AnimationProperty.AttackPhaseProperty.DAMAGE, ValueCorrector.multiplier(1.8F)).addProperty(AnimationProperty.AttackPhaseProperty.IMPACT, ValueCorrector.multiplier(1.2F)).addProperty(AnimationProperty.AttackPhaseProperty.MAX_STRIKES, ValueCorrector.setter(10.0F)).addProperty(AnimationProperty.AttackAnimationProperty.BASIS_ATTACK_SPEED, Float.valueOf(1.0F)).addProperty(AnimationProperty.AttackAnimationProperty.ATTACK_SPEED_FACTOR, Float.valueOf(1.5F)).addProperty(AnimationProperty.AttackAnimationProperty.ROTATE_X, Boolean.valueOf(false)).addProperty(AnimationProperty.ActionAnimationProperty.CANCELABLE_MOVE, Boolean.valueOf(false)).addProperty(AnimationProperty.StaticAnimationProperty.EVENTS, new StaticAnimation.Event[] { StaticAnimation.Event.create(0.0F, entitypatch -> {
            if (entitypatch instanceof PlayerPatch)
                ((PlayerPatch)entitypatch).setStamina(((PlayerPatch)entitypatch).getStamina() - 4.0F);
        },StaticAnimation.Event.Side.CLIENT)});
        LONGSWORD_TWOHAND_AUTO_1 = (StaticAnimation)(new BasicAttackAnimation(0.1F, 0.2F, 0.3F, 0.4F, null, "Tool_R", "biped/wom/combat/longsword_twohand_auto_1", biped)).addProperty(AnimationProperty.AttackAnimationProperty.BASIS_ATTACK_SPEED, Float.valueOf(1.35F));
        LONGSWORD_TWOHAND_AUTO_2 = (StaticAnimation)(new BasicAttackAnimation(0.1F, 0.2F, 0.3F, 0.4F, null, "Tool_R", "biped/wom/combat/longsword_twohand_auto_2", biped)).addProperty(AnimationProperty.AttackAnimationProperty.BASIS_ATTACK_SPEED, Float.valueOf(1.35F));
        LONGSWORD_TWOHAND_AUTO_3 = (StaticAnimation)(new BasicAttackAnimation(0.1F, 0.2F, 0.3F, 0.4F, null, "Tool_R", "biped/wom/combat/longsword_twohand_auto_3", biped)).addProperty(AnimationProperty.AttackAnimationProperty.BASIS_ATTACK_SPEED, Float.valueOf(1.35F));
        LONGSWORD_TWOHAND_AUTO_4 = (StaticAnimation)(new BasicAttackAnimation(0.1F, 0.3F, 0.4F, 0.5F, null, "Tool_R", "biped/wom/combat/longsword_twohand_auto_4", biped)).addProperty(AnimationProperty.AttackAnimationProperty.BASIS_ATTACK_SPEED, Float.valueOf(1.35F));
        GREATSWORD_TWOHAND_AUTO_1 = (StaticAnimation)(new BasicAttackAnimation(0.1F, 0.3F, 0.4F, 0.5F, null, "Tool_R", "biped/wom/combat/greatsword_twohand_auto_1", biped)).addProperty(AnimationProperty.AttackPhaseProperty.DAMAGE, ValueCorrector.multiplier(0.6F)).addProperty(AnimationProperty.AttackAnimationProperty.BASIS_ATTACK_SPEED, Float.valueOf(1.05F));
        GREATSWORD_TWOHAND_AUTO_2 = (StaticAnimation)(new BasicAttackAnimation(0.1F, 0.3F, 0.4F, 0.5F, null, "Tool_R", "biped/wom/combat/greatsword_twohand_auto_2", biped)).addProperty(AnimationProperty.AttackPhaseProperty.DAMAGE, ValueCorrector.multiplier(0.6F)).addProperty(AnimationProperty.AttackAnimationProperty.BASIS_ATTACK_SPEED, Float.valueOf(1.05F));
        GREATSWORD_TWOHAND_AUTO_3 = (StaticAnimation)(new BasicAttackAnimation(0.1F, 0.4F, 0.5F, 0.6F, null, "Tool_R", "biped/wom/combat/greatsword_twohand_auto_3", biped)).addProperty(AnimationProperty.AttackPhaseProperty.DAMAGE, ValueCorrector.multiplier(0.6F)).addProperty(AnimationProperty.AttackAnimationProperty.BASIS_ATTACK_SPEED, Float.valueOf(1.05F));












    }

}