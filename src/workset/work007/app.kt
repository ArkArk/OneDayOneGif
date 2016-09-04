package workset.work007

import processing.core.PApplet
import processing.opengl.PShader
import util.Function

// 2016/07/22
class App : PApplet() {
    private lateinit var sd: PShader

    override fun settings() {
        size(600, 600, P2D)
    }

    override fun setup() {
        sd= loadShader("/src/workset/work007/hex.frag")
        sd.set("iResolution", width.toFloat(), height.toFloat())
        background(0)
    }

    override fun draw() {
        background(0)

        shader(sd)
        sd.set("iGlobalTime", frameCount.toFloat())
        rect(0f, 0f, width.toFloat(), height.toFloat())
    }
}