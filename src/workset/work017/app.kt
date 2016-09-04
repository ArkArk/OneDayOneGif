package workset.work017

import processing.core.PApplet
import util.Function

// 2016/08/01

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

data class Result(val a: Vec2, val b: Vec2, val c: Vec2)

class App : PApplet() {
    val depthMax: Int = 7

    override fun settings() {
        size(600, 600, P2D)
    }

    override fun setup() {
        strokeWeight(0.5f)
        background(0f)
    }

    override fun draw() {
        fill(0f, 50f)
        noStroke()
        rect(0f, 0f, width.toFloat(), height.toFloat())

        noFill()
        stroke(255f, 30f)
        val t = -getT()
        translate(width/2f, height/2f)
        val scale = 200f*exp(t)*exp(t)
        for(i in 0..3-1) {
            val v1 = scale*Vec2(-1f, 1f/ sqrt(3f))
            val v5 = scale*Vec2(+1f, 1f/ sqrt(3f))
            beginShape()
            koch(0, t, v1, v5)
            endShape(LINES)
            rotate(2f/3f* PI)
        }

    }

    fun getT(): Float {
        val duration = 100f
        val po = 1f/2f
        val t = frameCount%duration/duration*2f
        return (if (t < 1.0) t else 2.0f-t).let {
            if (it < 0.5f) {
                Function.easeInOutQuad(it, 0, po, 0.5f).toFloat()
            } else {
                Function.easeInOutQuad(it-0.5f, po, 1f-po, 0.5f).toFloat()
            }
        }
    }

    fun calc(t: Float, v1: Vec2, v5: Vec2): Result {
        val v2 = (t/2f).let {
            v1*(1f-it) + v5*it
        }
        val v3 = ((v5-v1)*(t/2f)).let {
            Vec2(-it.y, it.x)
        }.let {
            (v1+it+v5+it)/2f
        }
        val v4 = (t/2f).let {
            v1*it + v5*(1-it)
        }
        return Result(v2, v3, v4)
    }

    fun koch(depth: Int, t: Float, v1: Vec2, v5: Vec2) {
        vertex(v1.x, v1.y)
        if (depth < depthMax) {
            val (v2, v3, v4) = calc(t, v1, v5)
            koch(depth+1, t, v1, v2)
            koch(depth+1, t, v2, v3)
            koch(depth+1, t, v3, v4)
            koch(depth+1, t, v4, v5)
        }
        vertex(v5.x, v5.y)
    }


}
