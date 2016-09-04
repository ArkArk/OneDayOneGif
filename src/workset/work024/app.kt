package workset.work024

import processing.core.PApplet
import processing.core.PImage
import processing.core.PShape
import processing.opengl.PShader
import util.Function

// 2016/08/08

//data class Vec3(val x: Float, val y: Float, val z: Float)
//operator fun Vec3.plus(rhs: Vec3)  : Vec3 = Vec3(x+rhs.x, y+rhs.y, z+rhs.z)
//operator fun Vec3.minus(rhs: Vec3) : Vec3 = Vec3(x-rhs.x, y-rhs.y, z-rhs.z)
//operator fun Vec3.times(rhs: Vec3) : Vec3 = Vec3(x*rhs.x, y*rhs.y, z*rhs.z)
//operator fun Vec3.div(rhs: Vec3)   : Vec3 = Vec3(x/rhs.x, y/rhs.y, z/rhs.z)
//operator fun Vec3.mod(rhs: Vec3)   : Vec3 = Vec3(x%rhs.x, y%rhs.y, z%rhs.z)
//operator fun Vec3.plus(rhs: Float) : Vec3 = Vec3(x+rhs, y+rhs, z+rhs)
//operator fun Vec3.minus(rhs: Float): Vec3 = Vec3(x-rhs, y-rhs, z-rhs)
//operator fun Vec3.times(rhs: Float): Vec3 = Vec3(x*rhs, y*rhs, z*rhs)
//operator fun Vec3.div(rhs: Float)  : Vec3 = Vec3(x/rhs, y/rhs, z/rhs)
//operator fun Vec3.mod(rhs: Float)  : Vec3 = Vec3(x%rhs, y%rhs, z%rhs)
//operator fun Float.plus(rhs: Vec3) : Vec3 = Vec3(this+rhs.x, this+rhs.y, this+rhs.z)
//operator fun Float.minus(rhs: Vec3): Vec3 = Vec3(this-rhs.x, this-rhs.y, this-rhs.z)
//operator fun Float.times(rhs: Vec3): Vec3 = Vec3(this*rhs.x, this*rhs.y, this*rhs.z)
//operator fun Float.div(rhs: Vec3)  : Vec3 = Vec3(this/rhs.x, this/rhs.y, this/rhs.z)
//operator fun Float.mod(rhs: Vec3)  : Vec3 = Vec3(this%rhs.x, this%rhs.y, this%rhs.z)

class App : PApplet() {
    private lateinit var sd: PShader
    private lateinit var img: PImage
    private lateinit var sh: PShape

    override fun settings() {
        size(600, 600, P3D)
    }

    override fun setup() {
        textureMode(NORMAL)
        background(255f)
        sd = loadShader("/src/workset/work024/shader.frag", "/src/workset/work024/shader.vert")
        img = loadImage("/src/workset/work024/d3.png")
        sh = custom()
    }

    override fun draw() {

        sd.set("iGlobalTime", frameCount.toFloat())
        shader(sd)
        translate(width/2f, height/2f)
        scale(0.6f)
        rotateY(radians(frameCount*2f))
        shape(custom())
    }

    fun custom(): PShape {
        val x1 = -width/2f
        val x2 = +width/2f
        val y1 = -height/2f
        val y2 = +height/2f

        val sh = createShape()

        sh.beginShape(QUAD_STRIP)

        sh.noStroke()
        sh.texture(img)

        val N = 10000
        for(i in 0..N) {
            val r = Function.linear(i, 0, 1, N).toFloat()
            sh.vertex(x1, y1*(1-r) + y2*r, 0f, r)
            sh.vertex(x2, y1*(1-r) + y2*r, 1f, r)
        }

        sh.endShape()

        return sh
    }

}