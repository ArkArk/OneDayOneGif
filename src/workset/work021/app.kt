package workset.work021

import processing.core.PApplet
import processing.opengl.PShader

// 2016/08/05
class App : PApplet() {
    private lateinit var sd: PShader

    override fun settings() {
        size(800, 800, P3D)
    }

    override fun setup() {
        sd= loadShader("/src/workset/work021/shader.frag")
        sd.set("iResolution", width.toFloat(), height.toFloat())
    }

    override fun draw() {
        shader(sd)
        sd.set("iGlobalTime", frameCount.toFloat())
        sd.set("mousePos", mouseX/width.toFloat(), 1-mouseY/height.toFloat())
        beginShape(QUADS)
        vertex(0f, height.toFloat(), 0f)
        vertex(width.toFloat(), height.toFloat(), 0f)
        vertex(width.toFloat(), 0f, 0f)
        vertex(0f, 0f, 0f)
        endShape()
    }
}