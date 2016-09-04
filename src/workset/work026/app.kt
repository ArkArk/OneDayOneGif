package workset.work026

import processing.core.PApplet
import util.Function

class App : PApplet() {
    val N = 8
    val M = 5
    val radius = 120f

    val ary = Array(N) {
        Function.linear(it, 0, 2*PI, N).toFloat()
    }

    override fun settings() {
        size(600, 600, P2D)
    }

    override fun setup() {
        frameRate(40f)
        background(0f)
        colorMode(HSB, 360f, 100f, 100f, 255f)
        blendMode(ADD)
    }

    override fun draw() {
        background(0f)

        translate(width/2f, height/2f)

        for(j in 0..M-1) {
            pushMatrix()
            rotate(radians(frameCount*2f) * if(j%2==0) 1 else -1)
            for(i in 0..N-1) {
                val t = ary[i]
                val _r = Function.linear(j, 0, radius, M).toFloat()
                val r = Function.linear(j+1, 0, radius, M).toFloat()
                if (_r is Float) {
                    val a = Function.easeInQuart(M-j+1, 0, 1, M).toFloat()
                    drawLine(_r*cos(t), _r*sin(t), r*cos(t), r*sin(t), color((frameCount*1f)%360f, 50f, a*255f*0.4f, (1-a)*255f), 700f, 4)
                }
            }
            popMatrix()
        }
         if (frameCount%40 == 0) println(frameCount)
    }

    fun drawLine(x1: Float, y1: Float, x2: Float, y2: Float, col: Int = color(0f), w: Float = 2f, detail: Int = 10) {
        colorMode(RGB, 255f)
        for(i in 1..detail) {
            val r = Function.easeOutCirc(i, 0, 1, detail).toFloat()
            strokeWeight((1-r) * w)
            stroke(red(col)*r, green(col)*r, blue(col)*r, alpha(col)/detail)
            line(x1, y1, x2, y2)
        }
        colorMode(HSB, 360f, 100f, 100f, 255f)
    }

}