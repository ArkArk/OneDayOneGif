package test

import processing.core.PApplet
import processing.event.MouseEvent
import util.MouseCamera

class MouseCameraTest : PApplet() {
    private lateinit var mouseCamera: MouseCamera

    override fun settings() {
        size(600, 600, P3D)
    }

    override fun setup() {
        mouseCamera = MouseCamera(this)
        background(255)
    }

    override fun draw() {
        mouseCamera.update()

        background(255)
        sphere(100f)
        torus(250f, 50f, 60, 30)
    }

    fun torus(R: Float, r: Float, countS: Int, countT: Int) {
        for(s in 0..countS-1) {
            val theta1 = PApplet.map(s.toFloat(), 0f, countS.toFloat(), 0f, 2* PI)
            val theta2 = PApplet.map(s.toFloat()+1, 0f, countS.toFloat(), 0f, 2* PI)
            beginShape(TRIANGLE_STRIP);
            // beginShape(QUAD_STRIP);
            for(t in 0..countT) {
                val phi = PApplet.map(t.toFloat(), 0f, countT.toFloat(), 0f, 2* PI)
                vertex((R+r* cos(phi))* cos(theta1), (R+r* cos(phi))* sin(theta1), r* sin(phi))
                vertex((R+r* cos(phi))* cos(theta2), (R+r* cos(phi))* sin(theta2), r* sin(phi))
            }
            endShape();
        }
    }

    override fun mousePressed() {
        mouseCamera.mousePressed()
    }

    override fun mouseDragged() {
        mouseCamera.mouseDragged()
    }
    override fun mouseWheel(event: MouseEvent) {
        mouseCamera.mouseWheel(event)
    }
}