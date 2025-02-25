package yesman.epicfight.api.forgeevent;

import java.util.Map;

import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.fml.event.lifecycle.IModBusEvent;

public class AnimationRegistryEvent extends Event implements IModBusEvent {
	private Map<String, Runnable> registryMap;
	
	public AnimationRegistryEvent(Map<String, Runnable> registryMap) {
		this.registryMap = registryMap;
	}
	
	public Map<String, Runnable> getRegistryMap() {
		return this.registryMap;
	}
}