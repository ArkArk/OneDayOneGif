package test

import processing.core.PApplet

data class vec2(val x: Float, val y: Float)

class Test : PApplet() {
    private val N = 22
    private val scale = 1000f

    override fun settings() {
        size(1000, 1000, P2D)
    }

    override fun setup() {
        background(255)
        stroke(0f, 128f, 0f)
        rec(N, vec2(0f, 0f))
    }

    fun f1(v: vec2): vec2 = vec2(0.836f * v.x + 0.044f * v.y, -0.044f * v.x + 0.836f * v.y + 0.169f)
    fun f2(v: vec2): vec2 = vec2(-0.141f * v.x + 0.302f * v.y, 0.302f * v.x + 0.141f * v.y + 0.127f)
    fun f3(v: vec2): vec2 = vec2(0.141f * v.x - 0.302f * v.y, 0.302f * v.x + 0.141f * v.y + 0.169f)
    fun f4(v: vec2): vec2 = vec2(0f, 0.175337f * v.y)

    fun rec(i: Int, v: vec2) {
        if (i > 0) {
            rec(i - 1, f1(v))
            if (random(3f) < 1f) rec(i - 1, f2(v))
            if (random(3f) < 1f) rec(i - 1, f3(v))
            if (random(3f) < 1f) rec(i - 1, f4(v))
        } else {
            point(width / 2f + v.x * scale, height - v.y * scale)
        }
    }
}