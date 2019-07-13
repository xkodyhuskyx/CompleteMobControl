package com.kodyhusky.cmc.objects;

import java.util.List;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;

public class EntityRepeller {

	private Location location;
	private Location toggleLocation;
	private Material type;
	private Byte areaMode;
	private UUID uuid;
	private List<String> repelTypes;
	private List<EntityType> customEntities;
	private List<UUID> owners;
	private List<UUID> members;

	/**
	 * Creates a new EntityRepeller instance.
	 * 
	 * @param location
	 * @param toggleLocation
	 * @param type
	 * @param areaMode
	 * @param repelTypes
	 * @param customEntities
	 * @param owners
	 * @param members
	 */
	public EntityRepeller(Location location, Location toggleLocation, Material type, Byte areaMode,
			List<String> repelTypes, List<EntityType> customEntities, List<UUID> owners, List<UUID> members) {
		uuid = UUID.randomUUID();
		this.location = location;
		this.toggleLocation = toggleLocation;
		this.type = type;
		this.areaMode = areaMode;
		this.repelTypes = repelTypes;
		this.customEntities = customEntities;
		this.owners = owners;
		this.members = members;
	}

	/**
	 * Get the entity repellers UUID.
	 * 
	 * @return UUID
	 */
	public UUID getUUID() {
		return uuid;
	}

	/**
	 * Get the entity repellers exact location.
	 * 
	 * @return Location
	 */
	public Location getLocation() {
		return location;
	}

	/**
	 * Get the entity repellers toggle block location.<br>
	 * (Used for redstone power control)
	 * 
	 * @return Location
	 */
	public Location getToggleLocation() {
		return toggleLocation;
	}

	/**
	 * Set the entity repellers toggle block location.<br>
	 * (Used for redstone power control)
	 * 
	 * @param toggleLocation
	 */
	public void setToggleLocation(Location toggleLocation) {
		this.toggleLocation = toggleLocation;
	}

	/**
	 * Get the entity repellers material type.
	 * 
	 * @return Material
	 */
	public Material getType() {
		return type;
	}

	/**
	 * Get the entity repellers area mode.<br>
	 * <br>
	 * <b>Possible Values:</b><br>
	 * -1 = Repel Below Only<br>
	 * 0 = Normal<br>
	 * 1 = Repel Above Only
	 * 
	 * @return Byte
	 */
	public Byte getAreaMode() {
		return areaMode;
	}

	/**
	 * Set the entity repellers area mode.<br>
	 * <br>
	 * <b>Possible Values:</b><br>
	 * -1 = Repel Below Only<br>
	 * 0 = Normal<br>
	 * 1 = Repel Above Only
	 * 
	 * @param areaMode
	 */
	public void setAreaMode(Byte areaMode) {
		this.areaMode = areaMode;
	}

	/**
	 * Get the mob repellers enabled repel types.<br>
	 * <br>
	 * <b>Possible Values:</b><br>
	 * DEFAULT, NEUTRAL, HOSTILE, PLAYERS, CUSTOM, WHITELIST<br>
	 * <br>
	 * **Note: Other mods may add values to this list.<br>
	 * 
	 * @return List
	 */
	public List<String> getRepelTypes() {
		return repelTypes;
	}

	/**
	 * Set the mob repellers enabled repel types.<br>
	 * <br>
	 * <b>Possible Values:</b><br>
	 * DEFAULT, NEUTRAL, HOSTILE, PLAYERS, CUSTOM, WHITELIST<br>
	 * <br>
	 * **Note: Custom values can be added to this list.<br>
	 * 
	 * @param repelTypes
	 */
	public void setRepelTypes(List<String> repelTypes) {
		this.repelTypes = repelTypes;
	}

	/**
	 * Get the mob repellers list of custom entity types.
	 * 
	 * @return List
	 */
	public List<EntityType> getCustomEntities() {
		return customEntities;
	}

	/**
	 * Set the mob repellers list of custom entity types.
	 * 
	 * @param customEntities
	 */
	public void setCustomEntities(List<EntityType> customEntities) {
		this.customEntities = customEntities;
	}

	/**
	 * Get the entity repellers owner list.
	 * 
	 * @return List
	 */
	public List<UUID> getOwners() {
		return owners;
	}

	/**
	 * Set the entity repellers owner list.
	 * 
	 * @param owners
	 */
	public void setOwners(List<UUID> owners) {
		this.owners = owners;
	}

	/**
	 * Get the entity repellers member list.
	 * 
	 * @return List
	 */
	public List<UUID> getMembers() {
		return members;
	}

	/**
	 * Set the entity repellers member list.
	 * 
	 * @param members
	 */
	public void setMembers(List<UUID> members) {
		this.members = members;
	}
}
