package workset.work010

import processing.core.PApplet
import util.Function

// 2016/07/25
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
    private val duration: Int = 60
    private val diff: Array<vec2> = Array(3) {
        when (it) {
            0 -> vec2(sqrt(3f) / 2f, 1f / 2f)
            1 -> vec2(0f, 0f)
            2 -> vec2(0f, 1f)
            else -> vec2(0f, 0f)
        }
    }

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
        if (frameCount%duration == 0) {
            phase = (phase+1)%3
        }
        grid.forEach {
            it.forEach {
                drawHex(it)
            }
        }
    }

    fun drawHex(h: Hex) {
        val (x1, y1) = hexToPixel(h, phase)
        val (x2, y2) = hexToPixel(h, (phase+1)%3)
        val (x3, y3) = hexToPixel(h, (phase+2)%3)
        val ary = Array(6) {
            val p = when (it) {
                0 -> vec2(sqrt(3f) / 2f, 1f / 2f)
                1 -> vec2(0f, 1f)
                2 -> vec2(-sqrt(3f) / 2f, 1f / 2f)
                3 -> vec2(-sqrt(3f) / 2f, -1f / 2f)
                4 -> vec2(0f, -1f)
                5 -> vec2(sqrt(3f) / 2f, -1f / 2f)
                else -> vec2(0f, 0f)
            }
            vec2(p.x * size, p.y * size)
        }

        val t = frameCount%duration
        val r = Function.easeInOutQuint(t, 0, 1, duration).toFloat()
        for (i in 0..5 step 2) {
            line(x1, y1, x1 + ary[i].x, y1 + ary[i].y)
        }
        for (i in 0..5 step 2) {
            val _x = Function.linear(r, x2, ary[i].x, 1).toFloat()
            val _y = Function.linear(r, y2, ary[i].y, 1).toFloat()
            line(x2, y2, _x, _y)
        }
        for (i in 0..5 step 2) {
            val _x = Function.linear(1-r, x3, ary[i].x, 1).toFloat()
            val _y = Function.linear(1-r, y3, ary[i].y, 1).toFloat()
            line(x3, y3, _x, _y)
        }
    }

    fun hexToPixel(hex: Hex, i: Int): vec2 {
        val x = sqrt(3f) *hex.q + sqrt(3f) /2f*hex.r
        val y = 3f/2f*hex.r
        return vec2(size*(x+diff[i].x), size*(y+diff[i].y))
    }
}