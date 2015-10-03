package net.minecraft.src;

import java.awt.Color;
import java.util.Map;

public class mod_EnderingCreeper extends BaseMod
{
	public String getVersion()
	{
	return "1.4.5";
	}

	public void load()
	{
		// Registers the mobs name and ID.
		ModLoader.registerEntityID(EntityEnderingCreeper.class, "Semi Endercreeper", 30);
		// Makes the mob spawn in game.
		ModLoader.addSpawn("Semi Endercreeper", 15, -5, 10, EnumCreatureType.monster);
		// Adds Mob name on the spawn egg.
		ModLoader.addLocalization("entity.Semi Endercreeper.name", "Semi Endercreeper");
		// Creates the spawn egg, and changes the color of egg.
		EntityList.entityEggs.put(Integer.valueOf(30), new EntityEggInfo(30, 894731, (new Color(100, 106, 127)).getRGB()));
	}	// 21 15 6

	public void addRenderer(Map var1)
	{
		var1.put(EntityEnderingCreeper.class, new RenderLiving(new ModelEnderingCreeper(),.5f));
	}
}
