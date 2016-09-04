package workset.work002

import processing.core.PApplet
import util.Function

// 2016/07/17
class App : PApplet() {
    val N: Int = 36*8
    val size: Float = 300f

    var time: Int = 0

    override fun settings() {
        size(600, 600, P2D)
    }

    override fun setup() {
        background(255)
        rectMode(CENTER)
    }

    override fun draw() {
        background(255)
        fill(0)
        textSize(36f);
        text(time.toString(), 10f, height.toFloat()-10f)
        translate(width.toFloat()/2, height.toFloat()/2)
        strokeWeight(2f)

        stroke(0)
        fill(255)
        for(i in 0..N) {
            val n = 2000
            val r = if (time%n<n/2) {
                Function.easeInOutQuad(time%n, 0, 15000, n/2)
            } else {
                Function.easeInOutQuad(n-time%n, 0, 15000, n/2)
            }
            val angle = Function.easeOutCubic(i, 90 + r/10, 360*4 + 90 + r, N).toFloat()
            pushMatrix()
            rotate(radians(angle))
            val s = (size*pow(Math.E.toFloat(), -i.toFloat()/10)).toFloat()
            rect(0f, 0f, s, s)
            popMatrix()
        }

        time++
    }
}