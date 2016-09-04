package workset.work025

import processing.core.PApplet
import processing.core.PImage
import processing.core.PShape
import processing.opengl.PShader
import util.Function

// 2016/08/09

class App : PApplet() {
    private lateinit var sd: PShader
    private lateinit var img: PImage

    override fun settings() {
        size(600, 600, P3D)
    }

    override fun setup() {
        textureMode(NORMAL)
        background(255f)
        sd = loadShader("/src/workset/work025/shader.frag", "/src/workset/work025/shader.vert")
        img = loadImage("/src/workset/work025/d3.png")
    }

    override fun draw() {
        shader(sd)
        translate(width/2f, height/2f)
        scale(0.8f)
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

        val NX = 100
        val NY = 100
        val noiseScale = 2f
        for(i in 0..NX-1) {
            val rx1 = Function.linear(i, 0, 1, NX).toFloat()
            val rx2 = Function.linear(i+1, 0, 1, NX).toFloat()
            for (j in 0..NY) {
                val ry = Function.linear(j, 0, 1, NY).toFloat()
                val r = pow(frameCount/20.0f * noise((rx1+rx2)/2f/noiseScale, ry/noiseScale, frameCount*130f/noiseScale) * 0.5f, 3f) + 3.0f
                val t1 = noise(rx1/noiseScale+frameCount*1f/noiseScale, ry/noiseScale+frameCount*1f/noiseScale) * 2f * PI + PI/2
                val t2 = noise(rx2/noiseScale+frameCount*1f/noiseScale, ry/noiseScale+frameCount*1f/noiseScale) * 2f * PI + PI/2
                sh.vertex(x1 * (1 - rx1) + x2 * rx1, y1 * (1 - ry) + y2 * ry, rx1, ry)
                sh.attrib("diff", r*cos(t1), r*sin(t1), 0f)
                sh.vertex(x1 * (1 - rx2) + x2 * rx2, y1 * (1 - ry) + y2 * ry, rx2, ry)
                sh.attrib("diff", r*cos(t2), r*sin(t2), 0f)
            }
        }

        sh.endShape()

        return sh
    }

}