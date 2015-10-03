package net.minecraft.src;

public class EntityEnderCreeper extends EntityMob
{
    // Counter to delay the teleportation of an EnderCreeper towards the currently attacked target
    private int teleportDelay = 0;
    private int field_70826_g = 0;
    
    // Time when this Ender Creeper was last in an active state (Messed up code here, probably causes Ender Creeper animation to go weird)
    private int lastActiveTime;

    // The amount of time since the Ender Creeper was close enough to the player to ignite
    private int timeSinceIgnited;
    private int fuseTime = 10;
    
    // Explosion radius for this Ender Creeper.
    private int explosionRadius = 4;

    public EntityEnderCreeper(World par1World)
    {
        super(par1World);
        this.texture = "/mob/endercreeper.png";
        this.moveSpeed = 0.2F;
        this.setSize(0.6F, 2.9F);
        this.stepHeight = 1.0F;
        
        this.tasks.addTask(1, new EntityAISwimming(this));
        this.tasks.addTask(2, new EntityAIEnderCreeperSwell(this));
        this.tasks.addTask(3, new EntityAIAvoidEntity(this, EntityOcelot.class, 6.0F, 0.25F, 0.3F));
        this.tasks.addTask(4, new EntityAIAttackOnCollide(this, 0.25F, false));
        this.tasks.addTask(5, new EntityAIWander(this, 0.2F));
        this.tasks.addTask(6, new EntityAIWatchClosest(this, EntityEnderman.class, 1280.0F));
        this.tasks.addTask(7, new EntityAILookIdle(this));
        this.targetTasks.addTask(8, new EntityAINearestAttackableTarget(this, EntityEnderman.class, 2560.0F, 0, true));
        this.targetTasks.addTask(9, new EntityAIHurtByTarget(this, true));
    }

    public int getMaxHealth()
    {
        return 40;
    }

    protected void entityInit()
    {
        super.entityInit();
        this.dataWatcher.addObject(16, new Byte((byte)0));
        this.dataWatcher.addObject(17, new Byte((byte)0));
        this.dataWatcher.addObject(18, new Byte((byte)0));
    }
    
    // (abstract) Protected helper method to write subclass entity data to NBT.
    public void writeEntityToNBT(NBTTagCompound par1NBTTagCompound)
    {	
    	super.writeEntityToNBT(par1NBTTagCompound); 

        if (this.dataWatcher.getWatchableObjectByte(17) == 1)
        {
            par1NBTTagCompound.setBoolean("powered", true);
        }

        par1NBTTagCompound.setShort("Fuse", (short)this.fuseTime);
        par1NBTTagCompound.setByte("ExplosionRadius", (byte)this.explosionRadius);
    }

    // (abstract) Protected helper method to read subclass entity data from NBT.
    public void readEntityFromNBT(NBTTagCompound par1NBTTagCompound)
    {
        super.readEntityFromNBT(par1NBTTagCompound);

        this.dataWatcher.updateObject(17, Byte.valueOf((byte)(par1NBTTagCompound.getBoolean("powered") ? 1 : 0)));

        if (par1NBTTagCompound.hasKey("Fuse"))
        {
            this.fuseTime = par1NBTTagCompound.getShort("Fuse");
        }

        if (par1NBTTagCompound.hasKey("ExplosionRadius"))
        {
            this.explosionRadius = par1NBTTagCompound.getByte("ExplosionRadius");
        }
    }

    // Finds the closest player within 16 blocks to attack, or null if this Entity isn't interested in attacking (Animals, Spiders at day, peaceful PigZombies).
    protected Entity findPlayerToAttack()
    {
        EntityPlayer var1 = this.worldObj.getClosestVulnerablePlayerToEntity(this, 64.0D);

        if (var1 != null)
        {
            if (this.shouldAttackPlayer(var1))
            {
                if (this.field_70826_g == 0)
                {
                    this.worldObj.playSoundAtEntity(var1, "mob.endermen.stare", 1.0F, 1.0F);
                }

                if (this.field_70826_g++ == 5)
                {
                    this.field_70826_g = 0;
                    this.func_70819_e(true);
                    return var1;
                }
            }
            else
            {
                this.field_70826_g = 0;
            }
        }

        return null;
    }

    // Checks to see if this EnderCreeper should be attacking this player
    private boolean shouldAttackPlayer(EntityPlayer par1EntityPlayer)
    {
        ItemStack var2 = par1EntityPlayer.inventory.armorInventory[3];

        if (var2 != null && var2.itemID == Block.pumpkin.blockID)
        {
            return false;
        }
        else
        {
            Vec3 var3 = par1EntityPlayer.getLook(1.0F).normalize();
            Vec3 var4 = this.worldObj.getWorldVec3Pool().getVecFromPool(this.posX - par1EntityPlayer.posX, this.boundingBox.minY + (double)(this.height / 2.0F) - (par1EntityPlayer.posY + (double)par1EntityPlayer.getEyeHeight()), this.posZ - par1EntityPlayer.posZ);
            double var5 = var4.lengthVector();
            var4 = var4.normalize();
            double var7 = var3.dotProduct(var4);
            return var7 > 1.0D - 0.025D / var5 ? par1EntityPlayer.canEntityBeSeen(this) : false;
        }
    }

    // Called frequently so the entity can update its state every tick as required. For example, zombies and skeletons use this to react to sunlight and start to burn.
    public void onLivingUpdate()
    {
        if (this.isWet())
        {
            this.attackEntityFrom(DamageSource.drown, 1);
        }

        this.moveSpeed = this.entityToAttack != null ? 6.5F : 0.3F;
        int var1;

        for (var1 = 0; var1 < 2; ++var1)
        {
            this.worldObj.spawnParticle("portal", this.posX + (this.rand.nextDouble() - 0.5D) * (double)this.width, this.posY + this.rand.nextDouble() * (double)this.height - 0.25D, this.posZ + (this.rand.nextDouble() - 0.5D) * (double)this.width, (this.rand.nextDouble() - 0.5D) * 2.0D, -this.rand.nextDouble(), (this.rand.nextDouble() - 0.5D) * 2.0D);
        }

        if (this.worldObj.isDaytime() && !this.worldObj.isRemote)
        {
            float var6 = this.getBrightness(1.0F);

            if (var6 > 0.5F && this.worldObj.canBlockSeeTheSky(MathHelper.floor_double(this.posX), MathHelper.floor_double(this.posY), MathHelper.floor_double(this.posZ)) && this.rand.nextFloat() * 30.0F < (var6 - 0.4F) * 2.0F)
            {
                this.entityToAttack = null;
                this.func_70819_e(false);
                this.teleportRandomly();
            }
        }

        if (this.isWet())
        {
            this.entityToAttack = null;
            this.func_70819_e(false);
            this.teleportRandomly();
        }

        this.isJumping = false;

        if (this.entityToAttack != null)
        {
            this.faceEntity(this.entityToAttack, 100.0F, 100.0F);
        }

        if (!this.worldObj.isRemote && this.isEntityAlive())
        {
            if (this.entityToAttack != null)
            {
                if (this.entityToAttack instanceof EntityPlayer && this.shouldAttackPlayer((EntityPlayer)this.entityToAttack))
                {
                    this.moveStrafing = this.moveForward = 0.0F;
                    this.moveSpeed = 0.0F;

                    if (this.entityToAttack.getDistanceSqToEntity(this) < 16.0D)
                    {
                        this.teleportRandomly();
                    }

                    this.teleportDelay = 0;
                }
                else if (this.entityToAttack.getDistanceSqToEntity(this) > 256.0D && this.teleportDelay++ >= 30 && this.teleportToEntity(this.entityToAttack))
                {
                    this.teleportDelay = 0;
                }
            }
            else
            {
                this.func_70819_e(false);
                this.teleportDelay = 0;
            }
        }

        super.onLivingUpdate();
    }

    // Teleport the EnderCreeper to a random nearby position
    protected boolean teleportRandomly()
    {
        double var1 = this.posX + (this.rand.nextDouble() - 0.5D) * 64.0D;
        double var3 = this.posY + (double)(this.rand.nextInt(64) - 32);
        double var5 = this.posZ + (this.rand.nextDouble() - 0.5D) * 64.0D;
        return this.teleportTo(var1, var3, var5);
    }

    // Teleport the EnderCreeper to another entity
    protected boolean teleportToEntity(Entity par1Entity)
    {
        Vec3 var2 = this.worldObj.getWorldVec3Pool().getVecFromPool(this.posX - par1Entity.posX, this.boundingBox.minY + (double)(this.height / 2.0F) - par1Entity.posY + (double)par1Entity.getEyeHeight(), this.posZ - par1Entity.posZ);
        var2 = var2.normalize();
        double var3 = 16.0D;
        double var5 = this.posX + (this.rand.nextDouble() - 0.5D) * 8.0D - var2.xCoord * var3;
        double var7 = this.posY + (double)(this.rand.nextInt(16) - 8) - var2.yCoord * var3;
        double var9 = this.posZ + (this.rand.nextDouble() - 0.5D) * 8.0D - var2.zCoord * var3;
        return this.teleportTo(var5, var7, var9);
    }

    // Teleport the EnderCreeper
    protected boolean teleportTo(double par1, double par3, double par5)
    {
        double var7 = this.posX;
        double var9 = this.posY;
        double var11 = this.posZ;
        this.posX = par1;
        this.posY = par3;
        this.posZ = par5;
        boolean var13 = false;
        int var14 = MathHelper.floor_double(this.posX);
        int var15 = MathHelper.floor_double(this.posY);
        int var16 = MathHelper.floor_double(this.posZ);
        int var18;

        if (this.worldObj.blockExists(var14, var15, var16))
        {
            boolean var17 = false;

            while (!var17 && var15 > 0)
            {
                var18 = this.worldObj.getBlockId(var14, var15 - 1, var16);

                if (var18 != 0 && Block.blocksList[var18].blockMaterial.blocksMovement())
                {
                    var17 = true;
                }
                else
                {
                    --this.posY;
                    --var15;
                }
            }

            if (var17)
            {
                this.setPosition(this.posX, this.posY, this.posZ);

                if (this.worldObj.getCollidingBoundingBoxes(this, this.boundingBox).isEmpty() && !this.worldObj.isAnyLiquid(this.boundingBox))
                {
                    var13 = true;
                }
            }
        }

        if (!var13)
        {
            this.setPosition(var7, var9, var11);
            return false;
        }
        else
        {
            short var30 = 128;

            for (var18 = 0; var18 < var30; ++var18)
            {
                double var19 = (double)var18 / ((double)var30 - 1.0D);
                float var21 = (this.rand.nextFloat() - 0.5F) * 0.2F;
                float var22 = (this.rand.nextFloat() - 0.5F) * 0.2F;
                float var23 = (this.rand.nextFloat() - 0.5F) * 0.2F;
                double var24 = var7 + (this.posX - var7) * var19 + (this.rand.nextDouble() - 0.5D) * (double)this.width * 2.0D;
                double var26 = var9 + (this.posY - var9) * var19 + this.rand.nextDouble() * (double)this.height;
                double var28 = var11 + (this.posZ - var11) * var19 + (this.rand.nextDouble() - 0.5D) * (double)this.width * 2.0D;
                this.worldObj.spawnParticle("portal", var24, var26, var28, (double)var21, (double)var22, (double)var23);
            }

            this.worldObj.playSoundEffect(var7, var9, var11, "mob.endermen.portal", 1.0F, 1.0F);
            this.func_85030_a("mob.endermen.portal", 1.0F, 1.0F);
            return true;
        }
    }

    // Returns the sound this mob makes while it's alive.
    protected String getLivingSound()
    {
        return this.func_70823_r() ? "mob.endermen.scream" : "mob.endermen.idle";
    }

    // Returns the sound this mob makes when it is hurt.
    protected String getHurtSound()
    {
        return "mob.endermen.hit";
    }

    // Returns the sound this mob makes on death.
    protected String getDeathSound()
    {
        return "mob.creeper.death";
    }

    // Returns the item ID for the item the mob drops on death.
    protected int getDropItemId()
    {
        return Item.enderPearl.shiftedIndex;
    }

    // Drop 0-2 items of this living's type
    protected void dropFewItems(boolean par1, int par2)
    {
        int var3 = this.getDropItemId();

        if (var3 > 0)
        {
            int var4 = this.rand.nextInt(2 + par2);

            for (int var5 = 0; var5 < var4; ++var5)
            {
                this.dropItem(var3, 1);
            }
        }
    }


    // Called when the entity is attacked.
    public boolean attackEntityFrom(DamageSource par1DamageSource, int par2)
    {
        if (this.func_85032_ar())
        {
            return false;
        }
        else if (par1DamageSource instanceof EntityDamageSourceIndirect)
        {
            for (int var3 = 0; var3 < 64; ++var3)
            {
                if (this.teleportRandomly())
                {
                    return true;
                }
            }

            return false;
        }
        else
        {
            if (par1DamageSource.getEntity() instanceof EntityPlayer)
            {
                this.func_70819_e(true);
            }

            return super.attackEntityFrom(par1DamageSource, par2);
        }
    }

    public boolean func_70823_r()
    {
        return this.dataWatcher.getWatchableObjectByte(18) > 0;
    }

    public void func_70819_e(boolean par1)
    {
        this.dataWatcher.updateObject(18, Byte.valueOf((byte)(par1 ? 1 : 0)));
    }

    // Returns the amount of damage a mob should deal.
    public int getAttackStrength(Entity par1Entity)
    {
        return 7;
    }
    
    // Returns true if the newer Entity AI code should be run
    public boolean isAIEnabled()
    {
        return true;
    }

    public int func_82143_as()
    {
        return this.getAttackTarget() == null ? 3 : 3 + (this.health - 1);
    }

    // Called when the mob is falling. Calculates and applies fall damage.
    protected void fall(float par1)
    {
        super.fall(par1);
        this.timeSinceIgnited = (int)((float)this.timeSinceIgnited + par1 * 1.5F);

        if (this.timeSinceIgnited > this.fuseTime - 5)
        {
            this.timeSinceIgnited = this.fuseTime - 5;
        }
    }

    // Called to update the entity's position/logic.
    public void onUpdate()
    {
        if (this.isEntityAlive())
        {
            this.lastActiveTime = this.timeSinceIgnited;
            int var1 = this.getEnderCreeperState();

            if (var1 > 0 && this.timeSinceIgnited == 0)
            {
                this.func_85030_a("random.fuse", 1.0F, 0.5F);
            }

            this.timeSinceIgnited += var1;

            if (this.timeSinceIgnited < 0)
            {
                this.timeSinceIgnited = 0;
            }

            if (this.timeSinceIgnited >= this.fuseTime)
            {
                this.timeSinceIgnited = this.fuseTime;

                if (!this.worldObj.isRemote)
                {
                    boolean var2 = this.worldObj.getGameRules().getGameRuleBooleanValue("mobGriefing");

                    if (this.getPowered())
                    {
                        this.worldObj.createExplosion(this, this.posX, this.posY, this.posZ, (float)(this.explosionRadius * 2), var2);
                    }
                    else
                    {
                        this.worldObj.createExplosion(this, this.posX, this.posY, this.posZ, (float)this.explosionRadius, var2);
                    }

                    this.setDead();
                }
            }
        }

        super.onUpdate();
    }
    
    // Called when the mob's health reaches 0.
    public void onDeath(DamageSource par1DamageSource)
    {
        super.onDeath(par1DamageSource);

        if (par1DamageSource.getEntity() instanceof EntitySkeleton)
        {
            int var2 = Item.record13.shiftedIndex + this.rand.nextInt(Item.recordWait.shiftedIndex - Item.record13.shiftedIndex + 1);
            this.dropItem(var2, 1);
        }
    }

    public boolean attackEntityAsMob(Entity par1Entity)
    {
        return true;
    }

    // Returns true if the Ender Creeper is powered by a lightning bolt.
    public boolean getPowered()
    {
        return this.dataWatcher.getWatchableObjectByte(17) == 1;
    }

    // Params: (Float)Render tick. Returns the intensity of the Ender Creeper's flash when it is ignited.
    public float getEnderCreeperFlashIntensity(float par1)
    {
        return ((float)this.lastActiveTime + (float)(this.timeSinceIgnited - this.lastActiveTime) * par1) / (float)(this.fuseTime - 2);
    }

    // Returns the item ID for the item the mob drops on death.
    protected void dropRareDrop(int par1)
    {
        this.dropItem(Block.tnt.blockID, 2);
        this.dropItem(Item.eyeOfEnder.shiftedIndex, 1);
    }

    // Returns the current state of Ender Creeper, -1 is idle, 1 is 'in fuse'
    public int getEnderCreeperState()
    {
        return this.dataWatcher.getWatchableObjectByte(16);
    }

    // Sets the state of Ender Creeper, -1 to idle and 1 to be 'in fuse'
    public void setEnderCreeperState(int par1)
    {
        this.dataWatcher.updateObject(16, Byte.valueOf((byte)par1));
    }

    // Called when a lightning bolt hits the entity.
    public void onStruckByLightning(EntityLightningBolt par1EntityLightningBolt)
    {
        super.onStruckByLightning(par1EntityLightningBolt);
        this.dataWatcher.updateObject(17, Byte.valueOf((byte)1));
    }
}
