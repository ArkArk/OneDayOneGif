package workset.work004

import processing.core.PApplet
import processing.core.PGraphics
import processing.core.PMatrix3D
import processing.event.MouseEvent
import util.Function
import util.MouseCamera

// 2016/07/19
class App : PApplet() {
    private lateinit var mouseCamera: MouseCamera

    private lateinit var gra: PGraphics

    override fun settings() {
        size(600, 600, P3D)
    }

    override fun setup() {
        mouseCamera = MouseCamera(this)
        background(0)
        imageMode(CENTER)
        hint(DISABLE_DEPTH_TEST)
        gra = createLight(1f, 1f, 1f)
        colorMode(HSB, 360f, 100f, 100f, 255f)
    }

    override fun draw() {
        mouseCamera.update()
        background(0)

        noStroke()
        fill(200f, 100f)

        val N = 360
        for(i in 0..N) {
            val s = Function.easeOutQuint(i, frameCount, 360 * 16, N).toFloat()
            val t = if (i<N/2) {
                Function.easeOutQuint(i, frameCount*4, 360, N/2).toFloat()
            } else {
                Function.easeInQuint(i-N/2, frameCount*4+360, -360, N/2).toFloat()
            }
            val radius = Function.easeOutQuint(i, 20, 80, N).toFloat()

            val radS = radians(s)
            val radT = radians(t)

            val z = radius*cos(radT)
            val r = pow(sin(radians(z+frameCount*2))*0.5f+0.5f, 0.5f)
            val x = radius*cos(radS)*sin(radT)// * r
            val y = radius*sin(radS)*sin(radT)// * r

            pushMatrix()
            translate(x, y, z)
            //
            blendMode(ADD)
            val m = PMatrix3D(mouseCamera.getRotateMatrix())
            m.invert()
            applyMatrix(m)
            image(gra, 0f, 0f)
            blendMode(BLEND)
            //
            popMatrix()
        }


        fill(255)
        textSize(36f);
        text(frameCount.toString(), 100f, height.toFloat()/3)
        strokeWeight(2f)
    }

    fun createLight(rPower: Float, gPower: Float, bPower: Float): PGraphics {
        val side = 200 // 画像の一辺の大きさ
        val center = side/2f
        val pg = createGraphics(side, side)
        pg.beginDraw()
        pg.loadPixels()
        for(x in 0..side-1) for(y in 0..side-1) {
            val d = (sq(x-center)+sq(y-center)) / 5f
            val r = 255*rPower/d
            val g = 255*gPower/d
            val b = 255*bPower/d
            pg.pixels[x+y*side] = color(r, g, b, 255f)
        }
        pg.updatePixels()
        pg.endDraw()
        return pg
    }

    override fun mousePressed() {
        mouseCamera.mousePressed()
    }

    override fun mouseDragged() {
        mouseCamera.mouseDragged()
    }

    override fun mouseWheel(event: MouseEvent) {
        mouseCamera.mouseWheel(event)
    }
}