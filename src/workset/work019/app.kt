package workset.work019

import processing.core.PApplet
import processing.event.MouseEvent
import processing.opengl.PShader
import util.MouseCamera

// 2016/08/03
class App : PApplet() {
    private lateinit var sd: PShader
    private lateinit var mouseCamera: MouseCamera

    override fun settings() {
        size(600, 600, P3D)
    }

    override fun setup() {
        sd= loadShader("/src/workset/work019/shader.frag")
        sd.set("iResolution", width.toFloat(), height.toFloat())
        mouseCamera = MouseCamera(this, 400f)
        mouseCamera.reversable = true
    }

    override fun draw() {
        shader(sd)
        sd.set("iGlobalTime", frameCount.toFloat())
        sd.set("cameraMatrix", mouseCamera.getMatrix(), false)
        sd.set("cameraRotateMatrix", mouseCamera.getRotateMatrix(), false)
        beginShape(QUADS)
        vertex(0f, height.toFloat(), 0f)
        vertex(width.toFloat(), height.toFloat(), 0f)
        vertex(width.toFloat(), 0f, 0f)
        vertex(0f, 0f, 0f)
        endShape()
    }

    override fun mousePressed() {
        mouseCamera.mousePressed()
    }

    override fun mouseDragged() {
        mouseCamera.mouseDragged(20f)
    }

    override fun mouseWheel(event: MouseEvent) {
        mouseCamera.mouseWheel(event)
    }
}
