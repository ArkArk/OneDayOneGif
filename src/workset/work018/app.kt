package workset.work018

import processing.core.PApplet
import util.Function

// 2016/08/02

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
    private val maxDepth: Int = 6
    private val duration = 240f
    private lateinit var node: Node
    private val globalScale = 200f
    private val col1 = color(255f, 20f)
    private val col2 = color(0f, 60f)

    private val po: Float = 6f


    override fun settings() {
        size(600, 600, P2D)
    }

    override fun setup() {
        strokeWeight(2f)
        background(0f)
        node = Node(0, Vec2(0f, 0f), globalScale)
    }

    override fun draw() {
        //background(0f)
        fill(0f, 30f*(1.5f-pow(getT(), 5f)))
        noStroke()
        rect(0f, 0f, width.toFloat(), height.toFloat())

        translate(width/2f, height/2f)
        rotate(radians(frameCount.toFloat()/duration*360f*1f))
        stroke(255f, 50f)
        strokeWeight(1f)
        node.draw(getT()*po-po)
    }

    fun getT(): Float {
        val t = frameCount%duration/duration*2f
        return (if (t < 1.0) t else 2.0f-t).let {
            Function.easeInOutQuad(it).toFloat()
        }
    }

    private val vertices: Array<Vec2> = arrayOf(
            Vec2(-1/2f, 1/2f/sqrt(3f)),
            Vec2(+1/2f, 1/2f/sqrt(3f)),
            Vec2(0f, -1/sqrt(3f))
    )

    inner class Node(val depth: Int, val pos: Vec2, val scale: Float, val parent: Node? = null) {
        private val children: Array<Node?> = Array(3) {null}
        init {
            if (depth < maxDepth) {
                for(i in 0..3-1) {
                    children[i] = Node(depth+1, pos + vertices[i]*scale/2f, scale/2f, this)
                }
            }
        }

        fun draw(t: Float) {
            if (parent is Node) {
                pushMatrix()
                (pos * (1 - t) + parent.pos * t).let {
                    it * exp(-abs(t)/8f)
                }.let {
                    translate(it.x, it.y)
                    rotate((t+po)*PI/po)
                    translate(-it.x, -it.y)
                }

                fill(col1)
                beginShape()
                for (i in 0..3) {
                    val v1 = pos + vertices[i % 3] * scale
                    val v2 = parent.pos + vertices[i % 3] * parent.scale
                    (v1 * (1 - t) + v2 * t).let {
                        it * exp(-abs(t)/15f)
                    }.let {
                        vertex(it.x, it.y)
                    }
                }
                endShape(CLOSE)
                fill(col2)
                beginShape()
                for (i in 0..3) {
                    val v1 = pos - vertices[i % 3] * scale / 2f
                    val v2 = parent.pos - vertices[i % 3] * parent.scale / 2f
                    (v1 * (1 - t) + v2 * t).let {
                        it * exp(-abs(t)*13f)
                    }.let {
                        vertex(it.x, it.y)
                    }
                }
                endShape(CLOSE)

                popMatrix()
            }

            children.forEach { it?.draw(t) }
        }
    }
}
