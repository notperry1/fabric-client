package dev.toastmc.client.util

import dev.toastmc.client.util.EntityUtils.isAnimal
import dev.toastmc.client.util.EntityUtils.isHostile
import dev.toastmc.client.util.EntityUtils.isNeutral
import net.minecraft.client.render.Tessellator
import net.minecraft.client.render.VertexFormats
import net.minecraft.client.render.WorldRenderer
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher
import net.minecraft.entity.Entity
import net.minecraft.entity.decoration.EndCrystalEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Box
import net.minecraft.util.math.MathHelper

object RenderUtil {
    fun drawFilledBox(blockPos: BlockPos, r: Float, g: Float, b: Float, a: Float) {
        drawFilledBox(Box(blockPos.x.toDouble(), blockPos.y.toDouble(), blockPos.z.toDouble(), (blockPos.x + 1).toDouble(), (blockPos.y + 1).toDouble(), (blockPos.z + 1).toDouble()), r, g, b, a)
    }

    fun drawFilledBox(box: Box, r: Float, g: Float, b: Float, a: Float) {
        gl11Setup()

        // Fill
        val tessellator = Tessellator.getInstance()
        val buffer = tessellator.buffer
        buffer.begin(5, VertexFormats.POSITION_COLOR)
        WorldRenderer.drawBox(buffer, box.minX, box.minY, box.minZ, box.maxX, box.maxY, box.maxZ, r, g, b, a / 2f)
        tessellator.draw()

        // Outline
        buffer.begin(3, VertexFormats.POSITION_COLOR)
        buffer.vertex(box.maxX, box.minY, box.maxZ).color(r, b, b, a / 2f).next()
        buffer.vertex(box.minX, box.minY, box.maxZ).color(r, b, b, a / 2f).next()
        buffer.vertex(box.maxX, box.maxY, box.maxZ).color(r, b, b, a / 2f).next()
        buffer.vertex(box.minX, box.maxY, box.maxZ).color(r, b, b, a / 2f).next()
        buffer.vertex(box.minX, box.minY, box.maxZ).color(r, b, b, 0f).next()
        buffer.vertex(box.minX, box.maxY, box.maxZ).color(r, b, b, a / 2f).next()
        buffer.vertex(box.maxX, box.minY, box.maxZ).color(r, b, b, 0f).next()
        buffer.vertex(box.maxX, box.maxY, box.maxZ).color(r, b, b, a / 2f).next()
        buffer.vertex(box.maxX, box.minY, box.minZ).color(r, b, b, 0f).next()
        buffer.vertex(box.maxX, box.maxY, box.minZ).color(r, b, b, a / 2f).next()
        tessellator.draw()
        gl11Cleanup()
    }

    fun drawLine(x1: Double, y1: Double, z1: Double, x2: Double, y2: Double, z2: Double, r: Float, g: Float, b: Float, t: Float) {
        gl11Setup()
//        GL11.glLineWidth(t)
        val tessellator = Tessellator.getInstance()
        val buffer = tessellator.buffer
        buffer.begin(3, VertexFormats.POSITION_COLOR)
        buffer.vertex(x1, y1, z1).color(r, g, b, 0.0f).next()
        buffer.vertex(x1, y1, z1).color(r, g, b, 1.0f).next()
        buffer.vertex(x2, y2, z2).color(r, g, b, 1.0f).next()
        tessellator.draw()
        gl11Cleanup()
    }

    fun drawLineFromEntity(
        entity: Entity,
        location: Int,
        x: Double,
        y: Double,
        z: Double,
        t: Float
    ) {
        var r = 0f
        var g = 0f
        var b = 0f
        when {
            entity is PlayerEntity -> {
                r = 1f
                g = 1f
            }
            entity is EndCrystalEntity -> {
                r = 1f
                b = 1f
            }
            isAnimal(entity) -> {
                g = 1f
            }
            isNeutral(entity) -> {
                r = 1f
                g = 1f
                b = 1f
            }
            isHostile(entity) -> {
                r = 1f
            }
            else -> {
                r = 1f
                b = 1f
                g = 1f
            }
        }
        val additionalY: Double = when (location) {
            1 -> {
                entity.height / 2.toDouble()
            }
            2 -> {
                entity.standingEyeHeight.toDouble()
            }
            else -> {
                0.0
            }
        }
        gl11Setup()
//        GL11.glLineWidth(t)
        val tessellator = Tessellator.getInstance()
        val buffer = tessellator.buffer
        buffer.begin(3, VertexFormats.POSITION_COLOR)
        buffer.vertex(entity.x, entity.y + additionalY, entity.z).color(r, g, b, 0.0f).next()
        buffer.vertex(entity.x, entity.y + additionalY, entity.z).color(r, g, b, 1.0f).next()
        buffer.vertex(x, y, z).color(r, g, b, 1.0f).next()
        tessellator.draw()
        gl11Cleanup()
    }

    fun drawQuad(
        x1: Double,
        z1: Double,
        x2: Double,
        z2: Double,
        y: Double,
        r: Float,
        g: Float,
        b: Float,
        a: Float
    ) {
        gl11Setup()
        val tessellator = Tessellator.getInstance()
        val buffer = tessellator.buffer
//        buffer.begin(GL11.GL_QUADS, VertexFormats.POSITION_COLOR)
        buffer.vertex(x1, y, z1).color(r, g, b, a).next()
        buffer.vertex(x2, y, z1).color(r, g, b, a).next()
        buffer.vertex(x2, y, z2).color(r, g, b, a).next()
        buffer.vertex(x1, y, z2).color(r, g, b, a).next()
        buffer.vertex(x1, y, z1).color(r, g, b, a).next()
        tessellator.draw()
        gl11Cleanup()
    }

    private fun offsetRender() {
        val camera = BlockEntityRenderDispatcher.INSTANCE.camera
        val camPos = camera.pos
//        GL11.glRotated(MathHelper.wrapDegrees(camera.pitch).toDouble(), 1.0, 0.0, 0.0)
//        GL11.glRotated(MathHelper.wrapDegrees(camera.yaw + 180.0), 0.0, 1.0, 0.0)
//        GL11.glTranslated(-camPos.x, -camPos.y, -camPos.z)
    }

    private fun gl11Setup() {
//        GL11.glPushMatrix()
//        GL11.glEnable(GL11.GL_BLEND)
//        GL14.glBlendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, GL11.GL_ONE, GL11.GL_ZERO)
//        GL11.glLineWidth(2.5f)
//        GL11.glDisable(GL11.GL_TEXTURE_2D)
//        GL11.glEnable(GL11.GL_LINE_SMOOTH)
        offsetRender()
    }

    private fun gl11Cleanup() {
//        GL11.glDisable(GL11.GL_LINE_SMOOTH)
//        GL11.glPopMatrix()
//        GL11.glMatrixMode(5888)
//        GL11.glEnable(GL11.GL_DEPTH_TEST)
//        GL11.glEnable(GL11.GL_TEXTURE_2D)
//        GL11.glDisable(GL11.GL_BLEND)
    }
}