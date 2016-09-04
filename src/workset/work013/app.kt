package workset.work013

import processing.core.PApplet
import processing.opengl.PShader

// 2016/07/28
class App : PApplet() {
    private lateinit var sd: PShader

    override fun settings() {
        size(600, 600, P2D)
    }

    override fun setup() {
        sd= loadShader("/src/workset/work013/shader.frag")
        sd.set("iResolution", width.toFloat(), height.toFloat())
    }

    override fun draw() {
        shader(sd)
        sd.set("iGlobalTime", frameCount.toFloat())
        rect(0f, 0f, width.toFloat(), height.toFloat())

    }
}
