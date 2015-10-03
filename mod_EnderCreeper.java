package net.minecraft.src;

import java.awt.Color;
import java.util.Map;

public class mod_EnderCreeper extends BaseMod
{
	public String getVersion()
	{
	return "1.4.5";
	}

	public void load()
	{
		// Registers the mobs name and ID.
		ModLoader.registerEntityID(EntityEnderCreeper.class, "Endercreeper", 31);
		// Makes the mob spawn in game.
		ModLoader.addSpawn("Endercreeper", 10, -5, 1, EnumCreatureType.monster);
		// Adds Mob name on the spawn egg.
		ModLoader.addLocalization("entity.Endercreeper.name", "Endercreeper");
		// Creates the spawn egg, and changes the color of egg.
		EntityList.entityEggs.put(Integer.valueOf(31), new EntityEggInfo(31, 894731, (new Color(221, 115, 206)).getRGB()));
	}

	public void addRenderer(Map var1)
	{
		var1.put(EntityEnderCreeper.class, new RenderLiving(new ModelEnderCreeper(),.5f));
	}
}
