
package workset.work022

import processing.core.PApplet
import processing.core.PImage
import processing.opengl.PShader

// 2016/08/06
class App : PApplet() {
    private lateinit var sd: PShader
    private lateinit var img: PImage

    override fun settings() {
        size(600, 800, P3D)
    }

    override fun setup() {
        sd= loadShader("/src/workset/work022/shader.frag")
        sd.set("iResolution", width.toFloat(), height.toFloat())
        img = loadImage("/src/workset/work022/d3.gif")
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