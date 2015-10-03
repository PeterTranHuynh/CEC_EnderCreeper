package net.minecraft.src;

import java.util.Random;
import org.lwjgl.opengl.GL11;

public class RenderEnderCreeper extends RenderLiving
{
    /** The model of the EnderCreeper */
    private ModelEnderCreeper EnderCreeperModel;
    private Random rnd = new Random();

    public RenderEnderCreeper()
    {
        super(new ModelEnderCreeper(), 0.5F);
        this.EnderCreeperModel = (ModelEnderCreeper)super.mainModel;
        this.setRenderPassModel(this.EnderCreeperModel);
    }

    /**
     * Renders the EnderCreeper
     */
    public void renderEnderCreeper(EntityEnderCreeper par1EntityEnderCreeper, double par2, double par4, double par6, float par8, float par9)
    {
        if (par1EntityEnderCreeper.func_70823_r())
        {
            double var10 = 0.02D;
            par2 += this.rnd.nextGaussian() * var10;
            par6 += this.rnd.nextGaussian() * var10;
        }

        super.doRenderLiving(par1EntityEnderCreeper, par2, par4, par6, par8, par9);
    }
	
    /**
     * Render the EnderCreeper's eyes
     */
    protected int renderEyes(EntityEnderCreeper par1EntityEnderCreeper, int par2, float par3)
    {
        if (par2 != 0)
        {
            return -1;
        }
        else
        {
            this.loadTexture("/mob/endercreeper_eyes.png");
            float var4 = 1.0F;
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glDisable(GL11.GL_ALPHA_TEST);
            GL11.glBlendFunc(GL11.GL_ONE, GL11.GL_ONE);
            GL11.glDisable(GL11.GL_LIGHTING);

            if (par1EntityEnderCreeper.getHasActivePotion())
            {
                GL11.glDepthMask(false);
            }
            else
            {
                GL11.glDepthMask(true);
            }

            char var5 = 61680;
            int var6 = var5 % 65536;
            int var7 = var5 / 65536;
            OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float)var6 / 1.0F, (float)var7 / 1.0F);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            GL11.glEnable(GL11.GL_LIGHTING);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, var4);
            return 1;
        }
    }
    
    /**
     * Updates EnderCreeper scale in prerender callback
     */
    protected void updateEnderCreeperScale(EntityEnderCreeper par1EntityEnderCreeper, float par2)
    {
        float var4 = par1EntityEnderCreeper.getEnderCreeperFlashIntensity(par2);
        float var5 = 1.0F + MathHelper.sin(var4 * 100.0F) * var4 * 0.01F;

        if (var4 < 0.0F)
        {
            var4 = 0.0F;
        }

        if (var4 > 1.0F)
        {
            var4 = 1.0F;
        }

        var4 *= var4;
        var4 *= var4;
        float var6 = (1.0F + var4 * 0.4F) * var5;
        float var7 = (1.0F + var4 * 0.1F) / var5;
        GL11.glScalef(var6, var7, var6);
    }

    /**
     * Updates color multiplier based on EnderCreeper state called by getColorMultiplier
     */
    protected int updateEnderCreeperColorMultiplier(EntityEnderCreeper par1EntityEnderCreeper, float par2, float par3)
    {
        float var5 = par1EntityEnderCreeper.getEnderCreeperFlashIntensity(par3);

        if ((int)(var5 * 10.0F) % 2 == 0)
        {
            return 0;
        }
        else
        {
            int var6 = (int)(var5 * 0.2F * 255.0F);

            if (var6 < 0)
            {
                var6 = 0;
            }

            if (var6 > 255)
            {
                var6 = 255;
            }

            short var7 = 255;
            short var8 = 255;
            short var9 = 255;
            return var6 << 24 | var7 << 16 | var8 << 8 | var9;
        }
    }

    /**
     * A method used to render a EnderCreeper's powered form as a pass model.
     */
    protected int renderEnderCreeperPassModel(EntityEnderCreeper par1EntityEnderCreeper, int par2, float par3)
    {
        if (par1EntityEnderCreeper.getPowered())
        {
            if (par1EntityEnderCreeper.getHasActivePotion())
            {
                GL11.glDepthMask(false);
            }
            else
            {
                GL11.glDepthMask(true);
            }

            if (par2 == 1)
            {
                float var4 = (float)par1EntityEnderCreeper.ticksExisted + par3;
                this.loadTexture("/armor/power.png");
                GL11.glMatrixMode(GL11.GL_TEXTURE);
                GL11.glLoadIdentity();
                float var5 = var4 * 0.01F;
                float var6 = var4 * 0.01F;
                GL11.glTranslatef(var5, var6, 0.0F);
                this.setRenderPassModel(this.EnderCreeperModel);
                GL11.glMatrixMode(GL11.GL_MODELVIEW);
                GL11.glEnable(GL11.GL_BLEND);
                float var7 = 0.5F;
                GL11.glColor4f(var7, var7, var7, 1.0F);
                GL11.glDisable(GL11.GL_LIGHTING);
                GL11.glBlendFunc(GL11.GL_ONE, GL11.GL_ONE);
                return 1;
            }

            if (par2 == 2)
            {
                GL11.glMatrixMode(GL11.GL_TEXTURE);
                GL11.glLoadIdentity();
                GL11.glMatrixMode(GL11.GL_MODELVIEW);
                GL11.glEnable(GL11.GL_LIGHTING);
                GL11.glDisable(GL11.GL_BLEND);
            }
        }

        return -1;
    }

    /**
     * Queries whether should render the specified pass or not.
     */
    protected int shouldRenderPass(EntityLiving par1EntityLiving, int par2, float par3)
    {
        return this.renderEyes((EntityEnderCreeper)par1EntityLiving, par2, par3);
    }

    public void doRenderLiving(EntityLiving par1EntityLiving, double par2, double par4, double par6, float par8, float par9)
    {
        this.renderEnderCreeper((EntityEnderCreeper)par1EntityLiving, par2, par4, par6, par8, par9);
    }

    /**
     * Actually renders the given argument. This is a synthetic bridge method, always casting down its argument and then
     * handing it off to a worker function which does the actual work. In all probabilty, the class Render is generic
     * (Render<T extends Entity) and this method has signature public void doRender(T entity, double d, double d1,
     * double d2, float f, float f1). But JAD is pre 1.5 so doesn't do that.
     */
    public void doRender(Entity par1Entity, double par2, double par4, double par6, float par8, float par9)
    {
        this.renderEnderCreeper((EntityEnderCreeper)par1Entity, par2, par4, par6, par8, par9);
    }

    protected int func_77061_b(EntityEnderCreeper par1EntityEnderCreeper, int par2, float par3)
    {
        return -1;
    }

    /**
     * Allows the render to do any OpenGL state modifications necessary before the model is rendered. Args:
     * entityLiving, partialTickTime
     */
    protected void preRenderCallback(EntityLiving par1EntityLiving, float par2)
    {
        this.updateEnderCreeperScale((EntityEnderCreeper)par1EntityLiving, par2);
    }

    /**
     * Returns an ARGB int color back. Args: entityLiving, lightBrightness, partialTickTime
     */
    protected int getColorMultiplier(EntityLiving par1EntityLiving, float par2, float par3)
    {
        return this.updateEnderCreeperColorMultiplier((EntityEnderCreeper)par1EntityLiving, par2, par3);
    }

    /**
     * Queries whether should render the specified pass or not.
     */
    protected int shouldRenderPass2(EntityLiving par1EntityLiving, int par2, float par3)
    {
        return this.renderEnderCreeperPassModel((EntityEnderCreeper)par1EntityLiving, par2, par3);
    }

    protected int inheritRenderPass(EntityLiving par1EntityLiving, int par2, float par3)
    {
        return this.func_77061_b((EntityEnderCreeper)par1EntityLiving, par2, par3);
    }
}
