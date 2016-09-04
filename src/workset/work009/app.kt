package workset.work009

import processing.core.PApplet
import util.Function

// 2016/07/24
data class Hex(val q: Int, val r: Int)
data class vec2(val x: Float, val y: Float)

class App : PApplet() {
    private val size: Float = 50f

    private val numQ: Int = 15
    private val numR: Int = 10
    private val grid: Array<Array<Hex>> = Array(numQ) {
        i -> Array(numR) {
            j -> Hex(i-numQ/2, j-numR/2)
        }
    }

    private var phase: Int = 0
    private val duration1: Int = 60
    private val duration2: Int = 60
    private var diff: vec2 = vec2(0f, 0f)

    override fun settings() {
        size(600, 600, P2D)
    }

    override fun setup() {
        stroke(255f)
        strokeWeight(3f)
    }

    override fun draw() {
        background(0f)
        translate(width/2f, height/2f)
        if (frameCount%(duration1+duration2) == 0) {
            when(phase) {
                0 -> diff = vec2(sqrt(3f)/2f, 1f/2f)
                1 -> diff = vec2(0f, 0f)
            }
            phase = (phase+1)%2
        }
        grid.forEach {
            it.forEach {
                drawHex(it)
            }
        }
    }

    fun drawHex(h: Hex) {
        val (x, y) = hexToPixel(h)
        val ary = Array(7) {
            val p = when (it) {
                0, 6 -> vec2(sqrt(3f) / 2f, 1f / 2f)
                1 -> vec2(0f, 1f)
                2 -> vec2(-sqrt(3f) / 2f, 1f / 2f)
                3 -> vec2(-sqrt(3f) / 2f, -1f / 2f)
                4 -> vec2(0f, -1f)
                5 -> vec2(sqrt(3f) / 2f, -1f / 2f)
                else -> vec2(0f, 0f)
            }
            vec2(p.x * size, p.y * size)
        }
        val t = frameCount%(duration1 + duration2)
        if (t < duration1) {
            val r = Function.easeInOutQuad(t, 0.01, 1, duration1).toFloat()
            for(i in phase .. phase+4 step 2) {
                val x0 = x+ary[i].x
                val y0 = y+ary[i].y
                val x1 = Function.linear(r, x0, x+ary[(i+5)%6].x-x0, 1).toFloat()
                val y1 = Function.linear(r, y0, y+ary[(i+5)%6].y-y0, 1).toFloat()
                val x2 = Function.linear(r, x0, x+ary[(i+1)%6].x-x0, 1).toFloat()
                val y2 = Function.linear(r, y0, y+ary[(i+1)%6].y-y0, 1).toFloat()
                line(x0, y0, x1, y1)
                line(x0, y0, x2, y2)
            }
        } else {
            val r = 1f - Function.easeInOutQuad(t-duration1, 0, 0.99, duration2).toFloat()
            ary.forEachIndexed {
                i, v ->  ary[i] = vec2(v.x*r, v.y*r)
            }
            for (i in 0..5) {
                line(x + ary[i].x, y + ary[i].y, x + ary[i + 1].x, y + ary[i + 1].y)
            }
        }
    }

    fun hexToPixel(hex: Hex): vec2 {
        val x = sqrt(3f)*hex.q + sqrt(3f)/2f*hex.r
        val y = 3f/2f*hex.r
        return vec2(size*(x+diff.x), size*(y+diff.y))
    }
}