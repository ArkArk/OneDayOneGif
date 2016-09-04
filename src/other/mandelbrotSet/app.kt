package other.mandelbrotSet

import processing.core.PApplet
import processing.opengl.PShader
import processing.event.MouseEvent

class App : PApplet() {
    private lateinit var sd: PShader
    private lateinit var mat: Array<Array<Float>>
    private var prePressed = false

    override fun settings() {
        size(1000, 600, P3D)
    }

    override fun setup() {
        sd= loadShader("other/mandelbrotSet/mandelbrot.frag")
        sd.set("iResolution", width.toFloat(), height.toFloat())
        mat = getIdentityMatrix()
    }

    override fun draw() {
        background(0)
        shader(sd)

        if (mousePressed && prePressed) {
            val dx = 2*(mouseX-pmouseX).toFloat()
            val dy = 2*(mouseY-pmouseY).toFloat()
            mat = mult(getTranslateMatrix(dx, -dy), mat)
        }

        shader(sd)
        sd.set("v1", mat.get(0).get(0), mat.get(0).get(1), mat.get(0).get(2))
        sd.set("v2", mat.get(1).get(0), mat.get(1).get(1), mat.get(1).get(2))
        //sd.set("matrix", mat)
        rect(0f, 0f, width.toFloat(), height.toFloat())

        prePressed = mousePressed
    }

    override fun mouseWheel(e: MouseEvent) {
        mat = mult(getScaleMatrix(e.count.toFloat()), mat)
    }

    fun getIdentityMatrix(): Array<Array<Float>> {
        return Array(3) {
            i -> Array(3) {
                j -> if (i==j) 1f else 0f
            }
        }
    }

    fun getTranslateMatrix(dx: Float, dy: Float): Array<Array<Float>> {
        return arrayOf(
                arrayOf(1f, 0f, dx),
                arrayOf(0f, 1f, dy),
                arrayOf(0f, 0f, 1f)
        )
    }

    fun getScaleMatrix(dz: Float): Array<Array<Float>> {
        val zoom = exp(-dz/5f)
        return arrayOf(
                arrayOf(zoom, 0f, 0f),
                arrayOf(0f, zoom, 0f),
                arrayOf(0f, 0f, 1f)
        )
    }

    fun mult(m1: Array<Array<Float>>, m2: Array<Array<Float>>): Array<Array<Float>> {
        assert(m1.first().size == m2.size)
        return Array(m1.size) {
            i -> Array(m2.first().size) {
                j -> Array(m1.first().size) {
                    k -> m1.get(i).get(k) * m2.get(k).get(j)
                }.reduce{ a, b -> a+b }
            }
        }
    }
}