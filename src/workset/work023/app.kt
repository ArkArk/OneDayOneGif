package workset.work023

import processing.core.PApplet
import processing.core.PImage
import processing.opengl.PShader

// 2016/08/07
class App : PApplet() {
    private lateinit var sd: PShader
    private lateinit var img: PImage

    override fun settings() {
        size(400, 400, P3D)
    }

    override fun setup() {
        sd= loadShader("/src/workset/work023/shader.frag")
        sd.set("iResolution", width.toFloat(), height.toFloat())
        img = loadImage("/src/workset/work023/d3.png")
        sd.set("tex", img)
    }

    override fun draw() {
        shader(sd)
        sd.set("iGlobalTime", frameCount.toFloat())
        beginShape(QUADS)
        vertex(0f, height.toFloat(), 0f)
        vertex(width.toFloat(), height.toFloat(), 0f)
        vertex(width.toFloat(), 0f, 0f)
        vertex(0f, 0f, 0f)
        endShape()
    }
}