package net.minecraft.src;

import java.util.Random;

import org.lwjgl.opengl.GL11;

public class RenderEnderingCreeper extends RenderLiving
{
    /** The Endering Creeper model. */
    private ModelEnderingCreeper EnderingCreeperModel;
    private Random rnd = new Random();
    
    public RenderEnderingCreeper()
    {
        super(new ModelEnderingCreeper(), 0.5F);
        this.EnderingCreeperModel = (ModelEnderingCreeper)super.mainModel;
        this.setRenderPassModel(this.EnderingCreeperModel);
    }

    /**
     * Updates Endering Creeper scale in prerender callback
     */
    protected void updateEnderingCreeperScale(EntityEnderingCreeper par1EntityEnderingCreeper, float par2)
    {
        float var4 = par1EntityEnderingCreeper.getEnderingCreeperFlashIntensity(par2);
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
     * Updates color multiplier based on Endering Creeper state called by getColorMultiplier
     */
    protected int updateEnderingCreeperColorMultiplier(EntityEnderingCreeper par1EntityEnderingCreeper, float par2, float par3)
    {
        float var5 = par1EntityEnderingCreeper.getEnderingCreeperFlashIntensity(par3);

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
     * A method used to render a Endering Creeper's powered form as a pass model.
     */
    protected int renderEnderingCreeperPassModel(EntityEnderingCreeper par1EntityEnderingCreeper, int par2, float par3)
    {
        if (par1EntityEnderingCreeper.getPowered())
        {
            if (par1EntityEnderingCreeper.getHasActivePotion())
            {
                GL11.glDepthMask(false);
            }
            else
            {
                GL11.glDepthMask(true);
            }

            if (par2 == 1)
            {
                float var4 = (float)par1EntityEnderingCreeper.ticksExisted + par3;
                this.loadTexture("/armor/power.png");
                GL11.glMatrixMode(GL11.GL_TEXTURE);
                GL11.glLoadIdentity();
                float var5 = var4 * 0.01F;
                float var6 = var4 * 0.01F;
                GL11.glTranslatef(var5, var6, 0.0F);
                this.setRenderPassModel(this.EnderingCreeperModel);
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

    protected int func_77061_b(EntityEnderingCreeper par1EntityEnderingCreeper, int par2, float par3)
    {
        return -1;
    }

    /**
     * Allows the render to do any OpenGL state modifications necessary before the model is rendered. Args:
     * entityLiving, partialTickTime
     */
    protected void preRenderCallback(EntityLiving par1EntityLiving, float par2)
    {
        this.updateEnderingCreeperScale((EntityEnderingCreeper)par1EntityLiving, par2);
    }

    /**
     * Returns an ARGB int color back. Args: entityLiving, lightBrightness, partialTickTime
     */
    protected int getColorMultiplier(EntityLiving par1EntityLiving, float par2, float par3)
    {
        return this.updateEnderingCreeperColorMultiplier((EntityEnderingCreeper)par1EntityLiving, par2, par3);
    }

    /**
     * Renders the Endering Creeper
     */
    public void renderEnderingCreeper(EntityEnderingCreeper par1EntityEnderingCreeper, double par2, double par4, double par6, float par8, float par9)
    {

        if (par1EntityEnderingCreeper.func_70823_r())
        {
            double var10 = 0.02D;
            par2 += this.rnd.nextGaussian() * var10;
            par6 += this.rnd.nextGaussian() * var10;
        }

        super.doRenderLiving(par1EntityEnderingCreeper, par2, par4, par6, par8, par9);
    }
    
    /**
     * Render the endering creeper eyes
     */
    protected int renderEyes(EntityEnderingCreeper par1EntityEnderingCreeper, int par2, float par3)
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

            if (par1EntityEnderingCreeper.getHasActivePotion())
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
     * Queries whether should render the specified pass or not.
     */
    protected int shouldRenderPass(EntityLiving par1EntityLiving, int par2, float par3)
    {
        return this.renderEyes((EntityEnderingCreeper)par1EntityLiving, par2, par3);
    }

    protected int shouldRenderPass2(EntityLiving par1EntityLiving, int par2, float par3)
    {
        return this.renderEnderingCreeperPassModel((EntityEnderingCreeper)par1EntityLiving, par2, par3);
    }
    
    protected int inheritRenderPass(EntityLiving par1EntityLiving, int par2, float par3)
    {
        return this.func_77061_b((EntityEnderingCreeper)par1EntityLiving, par2, par3);
    }
}
