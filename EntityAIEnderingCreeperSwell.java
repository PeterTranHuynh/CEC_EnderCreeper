package net.minecraft.src;

public class EntityAIEnderingCreeperSwell extends EntityAIBase
{
    // The Endering Creeper that is swelling.
    EntityEnderingCreeper swellingEnderingCreeper;

    // The Endering Creeper's attack target. This is used for the changing of the Endering Creeper's state.
    EntityLiving EnderingCreeperAttackTarget;

    public EntityAIEnderingCreeperSwell(EntityEnderingCreeper par1EntityEnderingCreeper)
    {
        this.swellingEnderingCreeper = par1EntityEnderingCreeper;
        this.setMutexBits(1);
    }

    // Returns whether the EntityAIBase should begin execution.
    public boolean shouldExecute()
    {
        EntityLiving var1 = this.swellingEnderingCreeper.getAttackTarget();
        return this.swellingEnderingCreeper.getEnderingCreeperState() > 0 || var1 != null && this.swellingEnderingCreeper.getDistanceSqToEntity(var1) < 9.0D;
    }

    // Execute a one shot task or start executing a continuous task
    public void startExecuting()
    {
        this.swellingEnderingCreeper.getNavigator().clearPathEntity();
        this.EnderingCreeperAttackTarget = this.swellingEnderingCreeper.getAttackTarget();
    }

    // Resets the task
    public void resetTask()
    {
        this.EnderingCreeperAttackTarget = null;
    }

    // Updates the task
    public void updateTask()
    {
        if (this.EnderingCreeperAttackTarget == null)
        {
            this.swellingEnderingCreeper.setEnderingCreeperState(-1);
        }
        else if (this.swellingEnderingCreeper.getDistanceSqToEntity(this.EnderingCreeperAttackTarget) > 49.0D)
        {
            this.swellingEnderingCreeper.setEnderingCreeperState(-1);
        }
        else if (!this.swellingEnderingCreeper.getEntitySenses().canSee(this.EnderingCreeperAttackTarget))
        {
            this.swellingEnderingCreeper.setEnderingCreeperState(-1);
        }
        else
        {
            this.swellingEnderingCreeper.setEnderingCreeperState(1);
        }
    }
}

