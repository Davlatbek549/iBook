package com.example.dz

import com.example.dz.core.media.CropGeometry
import com.example.dz.core.media.CropPosition
import com.example.dz.core.media.CropRegion
import com.example.dz.core.media.CropShape
import kotlin.math.abs
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class CropGeometryTest {

    private fun assertClose(expected: Float, actual: Float, tolerance: Float = 0.001f) =
        assertTrue(abs(expected - actual) <= tolerance, "expected $expected, was $actual")

    @Test
    fun `at first the frame shows the middle of the picture, as large as fits`() {
        // A portrait photo under a circle: the full width, centred vertically.
        val portrait = CropGeometry(1200, 1600, CropShape.Circle)
        assertEquals(CropRegion(0, 200, 1200, 1200), portrait.region(CropPosition()))

        // A landscape photo under a banner: the full width, a third as tall.
        val landscape = CropGeometry(2048, 1536, CropShape.Banner)
        assertEquals(CropRegion(0, 427, 2048, 683), landscape.region(CropPosition()))
    }

    @Test
    fun `a region always has the frame's shape, within the server's 2 percent`() {
        val geometry = CropGeometry(1537, 2048, CropShape.Banner)
        listOf(1f, 1.3f, 1.9f).forEach { zoom ->
            val region = geometry.region(CropPosition(zoom, 0.3f, 0.8f))
            val aspect = region.width.toFloat() / region.height
            assertTrue(abs(aspect - 3f) / 3f < 0.02f, "aspect $aspect at zoom $zoom")
        }
    }

    @Test
    fun `the picture can never be moved off the frame`() {
        val geometry = CropGeometry(1200, 1600, CropShape.Circle)

        val pushedUp = geometry.clamp(CropPosition(centerX = 0.5f, centerY = -3f))
        assertEquals(CropRegion(0, 0, 1200, 1200), geometry.region(pushedUp))

        // At zoom 1 a portrait photo's full width is in the circle, so it cannot move sideways.
        val pushedLeft = geometry.clamp(CropPosition(centerX = 0f, centerY = 0.5f))
        assertClose(0.5f, pushedLeft.centerX)
    }

    @Test
    fun `zoom stops before the region is narrower than the server accepts`() {
        // 2048 wide under a banner: the server wants at least 750, so 2048 / 750.
        assertClose(2048f / 750f, CropGeometry(2048, 1536, CropShape.Banner).maxZoom)
        // Plenty of pixels: capped at 4x.
        assertClose(4f, CropGeometry(2048, 2048, CropShape.Circle).maxZoom)
        // Already too small for a banner: no zoom at all.
        assertClose(1f, CropGeometry(600, 800, CropShape.Banner).maxZoom)
    }

    @Test
    fun `a pinch keeps the point under the fingers where it is`() {
        val geometry = CropGeometry(2000, 2000, CropShape.Circle)
        val frame = 600f
        val start = CropPosition(zoom = 1f)
        // Fingers 150px right of the frame's centre: that is picture x = 1000 + 150 / 0.3 = 1500.
        val zoomed = geometry.transform(start, frame, panX = 0f, panY = 0f, zoomBy = 2f, centroidX = 150f, centroidY = 0f)

        val scale = geometry.scaleOnScreen(zoomed.zoom, frame)
        val underFingers = zoomed.centerX * 2000 + 150f / scale
        assertClose(1500f, underFingers, tolerance = 0.5f)
        assertClose(2f, zoomed.zoom)
    }

    @Test
    fun `a drag moves the picture with the fingers`() {
        val geometry = CropGeometry(2000, 2000, CropShape.Circle)
        val zoomed = CropPosition(zoom = 2f)
        // At zoom 2 a 600px frame shows 1000 picture pixels: 0.6 screen px each.
        val dragged = geometry.transform(zoomed, 600f, panX = 60f, panY = 0f, zoomBy = 1f, centroidX = 0f, centroidY = 0f)
        // Dragging right by 60px shows what is 100 picture pixels further left.
        assertClose(0.45f, dragged.centerX)
    }
}
