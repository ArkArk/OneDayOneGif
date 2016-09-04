package workset.work008

import processing.core.PApplet
import java.util.LinkedList

// 2016/07/23

data class vec2(val x: Float, val y: Float)

class App : PApplet() {
    private val N: Int = 1000000
    private val scale: Float = 600f
    private val queue: LinkedList<vec2> = LinkedList<vec2>()

    private var n: Int = 0

    override fun settings() {
        size(600, 600, P2D)
    }

    override fun setup() {
        background(255)
        stroke(0f, 128f, 0f)
        queue.offer(vec2(0.0f, 0.0f))
    }

    override fun draw() {
        for(i in 0..pow(n.toFloat(), 0.6f).toInt()) {
            if (!queue.isEmpty()) {
                val v = queue.poll()
                point(width / 2f + v.x * scale, height - v.y * scale)
                f(v)
            }
        }
    }

    fun f1(v: vec2): vec2 = vec2(0.836f * v.x + 0.044f * v.y, -0.044f * v.x + 0.836f * v.y + 0.169f)
    fun f2(v: vec2): vec2 = vec2(-0.141f * v.x + 0.302f * v.y, 0.302f * v.x + 0.141f * v.y + 0.127f)
    fun f3(v: vec2): vec2 = vec2(0.141f * v.x - 0.302f * v.y, 0.302f * v.x + 0.141f * v.y + 0.169f)
    fun f4(v: vec2): vec2 = vec2(0f, 0.175337f * v.y)

    fun f(v: vec2) {
        if (n < N) {
            queue.offer(f1(v))
            n++
            if (random(3f) < 1f) {
                queue.offer(f2(v))
                n++
            }
            if (random(3f) < 1f) {
                queue.offer(f3(v))
                n++
            }
            if (random(3f) < 1f) {
                queue.offer(f4(v))
                n++
            }
        }
    }
}