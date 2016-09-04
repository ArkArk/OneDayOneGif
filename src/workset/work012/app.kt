package workset.work012

import processing.core.PApplet
import processing.opengl.PShader
import util.Function

// 2016/07/27
class App : PApplet() {
    private lateinit var sd: PShader

    override fun settings() {
        size(600, 600, P2D)
    }

    override fun setup() {
        sd= loadShader("/src/workset/work012/polar.frag")
        sd.set("iResolution", width.toFloat(), height.toFloat())
    }

    override fun draw() {
        shader(sd)
        sd.set("iGlobalTime", frameCount.toFloat())
        rect(0f, 0f, width.toFloat(), height.toFloat())
    }
}
