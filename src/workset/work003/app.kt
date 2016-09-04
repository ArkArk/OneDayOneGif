package workset.work003

import processing.core.PApplet
import util.Function

// 2016/07/18
class App : PApplet() {
    private var time:Float = 0f

    override fun settings() {
        size(500, 500, P2D)
    }

    override fun setup() {
        background(255)
        colorMode(HSB, 360f, 100f, 100f, 255f)
    }

    override fun draw() {
        background(0)
//        stroke(255f, 50f)
        fill(255f, 50f)
        strokeWeight(1f)


        textSize(24f);
        text(time.toString(), 10f, height.toFloat()-10f)

        val l = 60
        val b = 50
        val w = width - l*2

        val N = 16
        for(i in 1..N-1) {
            val x = Function.linear(i, l, w, N)
            val r = i/N.toFloat()
            for(s in 9 downTo 0) for(t in 9 downTo 0) {
                val p = 2f
                val u = p*(sin(radians((time*2+x/1.5).toFloat()))*0.5+0.5)
                val _h = 250* Function.powerCurve(r, u, p-u)
                val v = Function.powerCurve(r, s, t)
                val y = height - (v*_h + b)
                fill(((Function.easeInCubic(v, 0, 80, 1)+time)%360).toFloat(), 60f, 100f, Function.easeInOutQuad(v, 10, 30, 1).toFloat())
                stroke(((Function.easeInCubic(v, 0, 80, 1)+time)%360).toFloat(), 20f, 100f, Function.easeInOutQuad(v, 10, 80, 1).toFloat())
                ellipse(x.toFloat(), y.toFloat(), 20f, 20f)
            }
        }

        time += 1f
    }
}