package yesman.epicfight.api.client.animation;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import net.minecraft.resources.IResource;
import net.minecraft.util.JSONUtils;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import yesman.epicfight.api.animation.LivingMotion;
import yesman.epicfight.api.animation.types.StaticAnimation;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.List;

@OnlyIn(Dist.CLIENT)
public class AnimationDataReader {
	static final Gson GSON = (new GsonBuilder()).registerTypeAdapter(AnimationDataReader.class, new Deserializer()).create();
	static final TypeToken<AnimationDataReader> TYPE = new TypeToken<AnimationDataReader>() {
	};
	
	public static void readAndApply(StaticAnimation animation, IResource iresource) {
		InputStream inputstream = iresource.getInputStream();
        Reader reader = new InputStreamReader(inputstream, StandardCharsets.UTF_8);
        AnimationDataReader propertySetter = JSONUtils.fromJson(GSON, reader, TYPE);
        
        if (propertySetter.jointMaskEntry.isValid()) {
        	animation.addProperty(ClientAnimationProperties.JOINT_MASK, propertySetter.jointMaskEntry);
        }
        
        animation.addProperty(ClientAnimationProperties.PRIORITY, propertySetter.priority);
        animation.addProperty(ClientAnimationProperties.LAYER_TYPE, propertySetter.layerType);
	}
	public static void readAndApply(StaticAnimation animation, InputStream inputstream) {
		Reader reader = new InputStreamReader(inputstream, StandardCharsets.UTF_8);
		AnimationDataReader propertySetter = JSONUtils.fromJson(GSON, reader, TYPE);

		if (propertySetter.jointMaskEntry.isValid()) {
			animation.addProperty(ClientAnimationProperties.JOINT_MASK, propertySetter.jointMaskEntry);
		}

		animation.addProperty(ClientAnimationProperties.PRIORITY, propertySetter.priority);
		animation.addProperty(ClientAnimationProperties.LAYER_TYPE, propertySetter.layerType);
	}
	private JointMaskEntry jointMaskEntry;
	private Layer.LayerType layerType;
	private Layer.Priority priority;
	
	private AnimationDataReader(JointMaskEntry jointMaskEntry, Layer.Priority priority, Layer.LayerType layerType) {
		this.jointMaskEntry = jointMaskEntry;
		this.priority = priority;
		this.layerType = layerType;
	}
	
	static class Deserializer implements JsonDeserializer<AnimationDataReader> {
		@Override
		public AnimationDataReader deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
			JsonObject jsonObject = json.getAsJsonObject();
			JointMaskEntry.Builder builder = JointMaskEntry.builder();
			Layer.Priority priority = jsonObject.has("priority") ? Layer.Priority.valueOf(JSONUtils.getAsString(jsonObject, "priority")) : Layer.Priority.LOWEST;
			Layer.LayerType layerType = jsonObject.has("layer") ? Layer.LayerType.valueOf(JSONUtils.getAsString(jsonObject, "layer")) : Layer.LayerType.BASE_LAYER;
			
			if (jsonObject.has("masks")) {
				builder.defaultMask(JointMaskEntry.NONE);
				JsonArray maskArray = jsonObject.get("masks").getAsJsonArray();
				maskArray.forEach((element) -> {
					JsonObject jointMaskEntry = element.getAsJsonObject();
					String livingMotionName = JSONUtils.getAsString(jointMaskEntry, "livingmotion");
					
					if (livingMotionName.equals("ALL")) {
						builder.defaultMask(getJointMaskEntry(JSONUtils.getAsString(jointMaskEntry, "type")));
					} else {
						LivingMotion livingMotion = LivingMotion.ENUM_MANAGER.get(livingMotionName);
						builder.mask(livingMotion, getJointMaskEntry(JSONUtils.getAsString(jointMaskEntry, "type")));
					}
				});
			}
			
			return new AnimationDataReader(builder.create(), priority, layerType);
		}
	}
	
	private static List<JointMask> getJointMaskEntry(String type) {
		switch (type) {
		case "none":
			return JointMaskEntry.NONE;
		case "arms":
			return JointMaskEntry.BIPED_ARMS;
		case "right_arms":
			return JointMaskEntry.BIPED_RIGHT_ARMS;
		case "right_arms_body":
			return JointMaskEntry.BIPED_BODY_AND_RIGHT_ARMS;
		case "left_arms":
			return JointMaskEntry.BIPED_LEFT_ARMS;
		case "left_arms_body":
			return JointMaskEntry.BIPED_BODY_AND_LEFT_ARMS;
		case "upper_joints":
			return JointMaskEntry.BIPED_UPPER_JOINTS;
		case "root_upper_joints":
			return JointMaskEntry.BIPED_UPPER_JOINTS_WITH_ROOT;
		case "wings":
			return JointMaskEntry.WINGS;
		default:
			return JointMaskEntry.NONE;
		}
	}
}