package net.minecraft.src;

public class EntityAIEnderCreeperSwell extends EntityAIBase
{
	// The Ender Creeper that is swelling.
    EntityEnderCreeper swellingEnderCreeper;

    // The Ender Creeper's attack target. This is used for the changing of the Ender Creeper's state.
    EntityLiving EnderCreeperAttackTarget;

    public EntityAIEnderCreeperSwell(EntityEnderCreeper par1EntityEnderCreeper)
    {
        this.swellingEnderCreeper = par1EntityEnderCreeper;
        this.setMutexBits(1);
    }

    // Returns whether the EntityAIBase should begin execution.
    public boolean shouldExecute()
    {
        EntityLiving var1 = this.swellingEnderCreeper.getAttackTarget();
        return this.swellingEnderCreeper.getEnderCreeperState() > 0 || var1 != null && this.swellingEnderCreeper.getDistanceSqToEntity(var1) < 9.0D;
    }

    // Execute a one shot task or start executing a continuous task
    public void startExecuting()
    {
        this.swellingEnderCreeper.getNavigator().clearPathEntity();
        this.EnderCreeperAttackTarget = this.swellingEnderCreeper.getAttackTarget();
    }

    // Resets the task
    public void resetTask()
    {
        this.EnderCreeperAttackTarget = null;
    }

    // Updates the task
    public void updateTask()
    {
        if (this.EnderCreeperAttackTarget == null)
        {
            this.swellingEnderCreeper.setEnderCreeperState(-1);
        }
        else if (this.swellingEnderCreeper.getDistanceSqToEntity(this.EnderCreeperAttackTarget) > 49.0D)
        {
            this.swellingEnderCreeper.setEnderCreeperState(-1);
        }
        else if (!this.swellingEnderCreeper.getEntitySenses().canSee(this.EnderCreeperAttackTarget))
        {
            this.swellingEnderCreeper.setEnderCreeperState(-1);
        }
        else
        {
            this.swellingEnderCreeper.setEnderCreeperState(1);
        }
    }
}
