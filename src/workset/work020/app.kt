package workset.work020

import processing.core.PApplet
import util.Function

// 2016/08/04

data class Hex(val q: Int, val r: Int)

data class Vec2(val x: Float, val y: Float)
operator fun Vec2.plus(rhs: Vec2)  : Vec2 = Vec2(x+rhs.x, y+rhs.y)
operator fun Vec2.minus(rhs: Vec2) : Vec2 = Vec2(x-rhs.x, y-rhs.y)
operator fun Vec2.times(rhs: Vec2) : Vec2 = Vec2(x*rhs.x, y*rhs.y)
operator fun Vec2.div(rhs: Vec2)   : Vec2 = Vec2(x/rhs.x, y/rhs.y)
operator fun Vec2.mod(rhs: Vec2)   : Vec2 = Vec2(x%rhs.x, y%rhs.y)
operator fun Vec2.plus(rhs: Float) : Vec2 = Vec2(x+rhs, y+rhs)
operator fun Vec2.minus(rhs: Float): Vec2 = Vec2(x-rhs, y-rhs)
operator fun Vec2.times(rhs: Float): Vec2 = Vec2(x*rhs, y*rhs)
operator fun Vec2.div(rhs: Float)  : Vec2 = Vec2(x/rhs, y/rhs)
operator fun Vec2.mod(rhs: Float)  : Vec2 = Vec2(x%rhs, y%rhs)
operator fun Float.plus(rhs: Vec2) : Vec2 = Vec2(this+rhs.x, this+rhs.y)
operator fun Float.minus(rhs: Vec2): Vec2 = Vec2(this-rhs.x, this-rhs.y)
operator fun Float.times(rhs: Vec2): Vec2 = Vec2(this*rhs.x, this*rhs.y)
operator fun Float.div(rhs: Vec2)  : Vec2 = Vec2(this/rhs.x, this/rhs.y)
operator fun Float.mod(rhs: Vec2)  : Vec2 = Vec2(this%rhs.x, this%rhs.y)

class App : PApplet() {
    private val maxDepth: Int = 4
    private val duration: Int = 60

    private val scale: Float = 150f

    private val numQ: Int = 3
    private val numR: Int = 3
    private val grid: Array<Array<Hex>> = Array(numQ) {
        i -> Array(numR) {
            j -> Hex(i-numQ/2, j-numR/2)
        }
    }

    override fun settings() {
        size(500, 500, P2D)
    }

    override fun setup() {
        background(0f)
        strokeWeight(2f)
    }

    override fun draw() {
        background(0f)
        translate(width/2f, height/2f)

        grid.forEach {
            it.forEach {
                drawHex(it)
            }
        }
    }

    fun drawHex(h: Hex) {
        val v = hexToPixel(h)
        val ary = Array(6) {
            when (it) {
                0 -> Vec2(sqrt(3f) / 2f, 1f / 2f)
                1 -> Vec2(0f, 1f)
                2 -> Vec2(-sqrt(3f) / 2f, 1f / 2f)
                3 -> Vec2(-sqrt(3f) / 2f, -1f / 2f)
                4 -> Vec2(0f, -1f)
                5 -> Vec2(sqrt(3f) / 2f, -1f / 2f)
                else -> Vec2(0f, 0f)
            } * scale
        }

        val n = getN()
        val t = Function.easeInOutQuad(getT()).toFloat()
        for(i in 0..6-1) {
            rec(0, n, t, v+ary[i], v+ary[(i+1)%6], v)
        }
    }

    fun hexToPixel(hex: Hex): Vec2 {
        val x = sqrt(3f) *hex.q + sqrt(3f) /2f*hex.r
        val y = 3f/2f*hex.r
        return Vec2(scale*x, scale*y)
    }

    fun getN(): Int {
        return (frameCount/duration%(maxDepth*2)).let {
            if (it < maxDepth) {
                it
            } else {
                2 * maxDepth - it - 1
            }
        }
    }

    fun getT(): Float {
        return (frameCount/duration%(maxDepth*2)).let { i ->
            (frameCount%duration/duration.toFloat()).let {
                if (i < maxDepth) {
                    it
                } else {
                    1f - it
                }
            }

        }
    }

    fun rec(depth: Int, N: Int, t: Float, v1: Vec2, v2: Vec2, v3: Vec2) {

        // 画面外を描画しない
        if (isOutWindow(v1) && isOutWindow(v2) && isOutWindow(v3)) return

        if (depth < N) {
            noFill()
            stroke(255f)
            beginShape()
            vertex(v1.x, v1.y)
            vertex(v2.x, v2.y)
            vertex(v3.x, v3.y)
            endShape(CLOSE)
            val c = (v1+v2+v3)/3f
            rec(depth+1, N, t, v1, v2, c)
            rec(depth+1, N, t, v2, v3, c)
            rec(depth+1, N, t, v3, v1, c)
        } else if (depth == N) {
            noFill()
            stroke(255f)
            val tmp = (v1+v2)/2f * (1f-t) + v3*t
            beginShape()
            vertex(v1.x, v1.y)
            vertex(v2.x, v2.y)
            vertex(tmp.x, tmp.y)
            endShape(CLOSE)
        }
    }

    fun isOutWindow(v: Vec2): Boolean {
        return v.x < -width/2f || v.x > width/2f || v.y < -height/2f || v.y > height/2f
    }

}
