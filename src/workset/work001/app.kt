package workset.work001

import processing.core.PApplet
import processing.core.PVector
import util.Function

// 2016/07/16
class App : PApplet() {
    private val N: Int = 5
    private val radius: Float = 70.0f
    private val pos: Array<PVector> = Array(N) {
        val theta = radians((360 * it / N - 90).toFloat())
        PVector(radius * cos(theta), radius * sin(theta))
    }

    private val NUM: Int = 40
    private val duration: Int = 200

    private var nowEdges: Array<Array<Int>> = Array(N) {
        arrayOf(it, (it+1)%N)
    }

    private var preEdges: Array<Array<Int>> = Array(N) {
        arrayOf(it, (it+1)%N)
    }

    private var time: Int = 0
    private var index: Int = 0

    override fun settings() {
        size(150, 150, P2D)
    }

    override fun setup() {
        background(0)
    }

    override fun draw() {
        translate(width/2f, height/2f)
        for(i in 0..NUM-1) {
            _draw()
        }
    }

    fun _draw() {
        if (time%duration == 0) {
            preEdges[index][0] = nowEdges[index][0]
            preEdges[index][1] = nowEdges[index][1]
//            index = nowEdges[index][1]
            index = (index+2)%N
            nowEdges[index][0] = (nowEdges[index][0] + 1 + N) % N
            nowEdges[index][1] = (nowEdges[index][1] + 2) % N
        }

        stroke(255f)
        strokeWeight(1f)
        for(i in 0..N-1) {
            line(getEasing(pos[preEdges[i][0]].x, pos[nowEdges[i][0]].x), getEasing(pos[preEdges[i][0]].y, pos[nowEdges[i][0]].y), getEasing(pos[preEdges[i][1]].x, pos[nowEdges[i][1]].x), getEasing(pos[preEdges[i][1]].y, pos[nowEdges[i][1]].y))
        }

        loadPixels()
        val x = if (time%3==0) 1 else 0
        for (i in 0..pixels.size-1) {
            pixels[i] = color(red(pixels[i])-x, green(pixels[i])-x, blue(pixels[i])-x)
        }
        updatePixels()

        time++
    }

    fun getEasing(x: Float, y: Float): Float {
        val r = time%duration/duration.toFloat()
        return Function.easeOutCubic(r, x, y-x, 1).toFloat()
    }

//    fun getColor(t: Float): Int {
//        return color((255*(sin(-2 * PI / 3 + t) *0.5+0.5)).toInt(), (255*(sin(0 + t) *0.5+0.5)).toInt(), (255*(sin(2 * PI / 3 + t) *0.5+0.5)).toInt(), 100)
//    }
}
