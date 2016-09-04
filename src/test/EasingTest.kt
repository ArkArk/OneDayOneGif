package test

import processing.core.PApplet
import util.Function

class EasingTest : PApplet() {

    override fun settings() {
        size(800, 800, P3D)
    }

    override fun setup() {
        background(0)
    }

    override fun draw() {
        background(0)
        stroke(255f, 150f)
        fill(255f, 100f)

        val l = 200
        val b = 200
        val w = width - l*2
        val h = height - b*2

        val N = 100
        for(i in 0..N) {
            val x = Function.linear(i, l, w, N)
            val y = height - Function.easeInOutCirc(i, b, h, N)
            ellipse(x.toFloat(), y.toFloat(), 10f, 10f)
        }
    }
}
