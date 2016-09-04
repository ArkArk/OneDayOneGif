package workset.work014

import processing.core.PApplet
import processing.core.PGraphics
import processing.core.PMatrix3D
import processing.event.MouseEvent
import util.Function
import util.MouseCamera

// 2016/07/29

data class vec3(val x: Float, val y: Float, val z: Float)

class App : PApplet() {
    private lateinit var mouseCamera: MouseCamera

    private lateinit var gra: Array<PGraphics>

    override fun settings() {
        size(800, 800, P3D)
    }

    override fun setup() {
        blendMode(ADD)
        imageMode(CENTER)
        hint(DISABLE_DEPTH_TEST)
        stroke(0f, 100f)
        mouseCamera = MouseCamera(this)
        gra = Array(180) {
            val col = getRGB(360f*it/180f)
            createLight(red(col), green(col), blue(col))
        }
    }

    fun getT(): Float {
        val duration = 80f
        return if (frameCount%duration < duration/2f) {
            if (frameCount%duration < duration/4f) {
                Function.easeInOutQuint(frameCount%duration, 0, 1, duration/2f).toFloat()
            } else {
                Function.easeInOutQuad(frameCount%duration, 0, 1, duration/2f).toFloat()
            }
        } else {
            if (frameCount%duration > duration*3f/4f) {
                Function.easeInOutQuint(duration-frameCount%duration, 0, 1, duration/2f).toFloat()
            } else {
                Function.easeInOutQuad(duration-frameCount%duration, 0, 1, duration/2f).toFloat()
            }
        }
    }

    override fun draw() {
        mouseCamera.update()
        background(0f)

        val scale = 50f
        val numU = 288
        val numV = 288

        val a = 1f - 1f * getT()
        val b = sqrt(1-a*a)
        val ary: Array<Array<vec3>> = Array(numU) {
            i -> Array(numV) {
                j ->
                    val u = Function.linear(i, 0, 6*PI, numU).toFloat()
                    val v = Function.linear(j, 0, 2*PI, numV).toFloat()
                    val x = 2f * scale * a*cos(u)*sin(v)
                    val y = 2f * scale * a*sin(u)*sin(v)
                    val z = scale * (a*(cos(v) + log(tan(v/2f))) + b*u)
                    vec3(x, y, z)
            }
        }
        val m = PMatrix3D(mouseCamera.getRotateMatrix())
        m.invert()
        for(i in 0..numU-1) {
            for(j in 0..numV-1) {
                val v = ary[i][j]
                pushMatrix()
                translate(v.x, v.y, v.z)
                applyMatrix(m)
                image(gra[180*i/numU], 0f, 0f)
                popMatrix()
            }
        }
    }

    fun getRGB(hue: Float): Int {
        val h = hue%360
        return if(h<60) {
            color(255f, (h / 60) * 255, 0f)
        } else if (h<120) {
            color(((120 - h) / 60) * 255, 255f, 0f)
        } else if (h<180) {
            color(0f, 255f, ((h - 120) / 60) * 255)
        } else if (h<240) {
            color(0f, ((240 - h) / 60) * 255, 255f)
        } else if (h<300) {
            color(((h - 240) / 60) * 255, 0f, 255f)
        } else {
            color(255f, 0f, ((360 - h) / 60) * 255)
        }
    }

    fun createLight(rPower: Float, gPower: Float, bPower: Float): PGraphics {
        val side =30 // 画像の一辺の大きさ
        val center = side/2f
        val pg = createGraphics(side, side)
        pg.beginDraw()
        pg.loadPixels()
        for(x in 0..side-1) for(y in 0..side-1) {
            val d = (sq(x-center)+sq(y-center)) / 0.001f
            val r = 255*rPower/d
            val g = 255*gPower/d
            val b = 255*bPower/d
            pg.pixels[x+y*side] = color(r, g, b, 50f)
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