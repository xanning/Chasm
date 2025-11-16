
package com.xraymod.util;

import com.xraymod.mixin.IAccessorEntityRenderer;
import com.xraymod.mixin.IAccessorMinecraft;
import com.xraymod.mixin.IAccessorRenderManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.Vec3;
import org.lwjgl.opengl.GL11;

public class RenderUtil {
    private static final Minecraft mc = Minecraft.getMinecraft();

    public static void drawBlockBoundingBox(BlockPos blockPos, double height, int red, int green, int blue, int alpha, float lineWidth) {
        drawBoundingBox(
                new AxisAlignedBB(
                        blockPos.getX(),
                        blockPos.getY(),
                        blockPos.getZ(),
                        (double) blockPos.getX() + 1.0,
                        (double) blockPos.getY() + height,
                        (double) blockPos.getZ() + 1.0
                ).offset(
                        -((IAccessorRenderManager) mc.getRenderManager()).getRenderPosX(),
                        -((IAccessorRenderManager) mc.getRenderManager()).getRenderPosY(),
                        -((IAccessorRenderManager) mc.getRenderManager()).getRenderPosZ()
                ),
                red, green, blue, alpha, lineWidth
        );
    }

    public static void drawBoundingBox(AxisAlignedBB axisAlignedBB, int red, int green, int blue, int alpha, float lineWidth) {
        GL11.glLineWidth(lineWidth);
        GL11.glEnable(GL11.GL_LINE_SMOOTH);
        GL11.glHint(GL11.GL_LINE_SMOOTH_HINT, GL11.GL_NICEST);
        RenderGlobal.drawOutlinedBoundingBox(axisAlignedBB, red, green, blue, alpha);
        GL11.glDisable(GL11.GL_LINE_SMOOTH);
        GL11.glLineWidth(2.0f);
    }

    public static void drawLine3D(Vec3 start, double endX, double endY, double endZ, float red, float green, float blue, float alpha, float lineWidth) {
        GlStateManager.pushMatrix();
        GlStateManager.color(red, green, blue, alpha);
        boolean bl = mc.gameSettings.viewBobbing;
        mc.gameSettings.viewBobbing = false;
        ((IAccessorEntityRenderer) mc.entityRenderer).callSetupCameraTransform(
                ((IAccessorMinecraft) mc).getTimer().renderPartialTicks, 2
        );
        mc.gameSettings.viewBobbing = bl;
        GL11.glLineWidth(lineWidth);
        GL11.glEnable(GL11.GL_LINE_SMOOTH);
        GL11.glHint(GL11.GL_LINE_SMOOTH_HINT, GL11.GL_NICEST);
        GL11.glBegin(GL11.GL_LINES);
        GL11.glVertex3d(start.xCoord, start.yCoord, start.zCoord);
        GL11.glVertex3d(
                endX - ((IAccessorRenderManager) mc.getRenderManager()).getRenderPosX(),
                endY - ((IAccessorRenderManager) mc.getRenderManager()).getRenderPosY(),
                endZ - ((IAccessorRenderManager) mc.getRenderManager()).getRenderPosZ()
        );
        GL11.glEnd();
        GL11.glDisable(GL11.GL_LINE_SMOOTH);
        GL11.glLineWidth(2.0f);
        GlStateManager.resetColor();
        GlStateManager.popMatrix();
    }

    public static void enableRenderState() {
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GlStateManager.disableTexture2D();
        GlStateManager.disableCull();
        GlStateManager.disableAlpha();
        GlStateManager.disableDepth();
    }

    public static void disableRenderState() {
        GlStateManager.enableDepth();
        GlStateManager.enableAlpha();
        GlStateManager.enableCull();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }

    public static float lerpFloat(float current, float previous, float t) {
        return previous + (current - previous) * t;
    }

    public static double lerpDouble(double current, double previous, double t) {
        return previous + (current - previous) * t;
    }
}