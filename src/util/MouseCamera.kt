package util

import processing.core.PApplet
import processing.core.PMatrix3D
import processing.core.PVector
import processing.event.MouseEvent

class MouseCamera constructor(
    private val app: PApplet,
    private val radius: Float,
    private val eyeX: Float,
    private val eyeY: Float,
    private val eyeZ: Float,
    private val centerX: Float,
    private val centerY: Float,
    private val centerZ: Float,
    private val upX: Float,
    private val upY: Float,
    private val upZ: Float
) {
    constructor(app: PApplet, radius: Float) : this(app, radius, 0f, 0f, (app.height/2f)/PApplet.tan(PApplet.PI*30/180), 0f, 0f, 0f, 0f, 1f, 0f)
    constructor(app: PApplet) : this(app, Math.min(app.width, app.height).toFloat())


    private val matrix: PMatrix3D = PMatrix3D()
    private val rotateMatrix: PMatrix3D = PMatrix3D()

    fun getMatrix(): PMatrix3D = matrix
    fun getRotateMatrix(): PMatrix3D = rotateMatrix
    var reversable: Boolean = false

    private var preVector: PVector = PVector()

    init {
        setIdentityMatrix()
    }

    fun update() {
        app.beginCamera()
        app.camera(eyeX, eyeY, eyeZ, centerX, centerY, centerZ, upX, upY, upZ)
        app.applyMatrix(matrix)
        app.endCamera()
    }

    fun mousePressed() {
        val x = app.mouseX
        val y = if(reversable) app.height-app.mouseY else app.mouseY
        when(app.mouseButton) {
            PApplet.RIGHT  -> setIdentityMatrix()
            PApplet.LEFT   -> preVector = mouseOnSphere(x-app.width/2f, y-app.height/2f)
            PApplet.CENTER -> preVector = PVector(x-app.width/2f, y-app.height/2f)
        }
    }

    fun mouseDragged(t: Float = 1f) {
        val x = app.mouseX
        val y = if(reversable) app.height-app.mouseY else app.mouseY
        when(app.mouseButton) {
            PApplet.LEFT   -> {
                val v = mouseOnSphere(x-app.width/2f, y-app.height/2f)
                rotate(preVector, v)
                preVector = v
            }
            PApplet.CENTER ->{
                val v = PVector(x-app.width/2f, y-app.height/2f)
                translate(preVector, v, t)
                preVector = v
            }
        }
    }

    fun mouseWheel(event: MouseEvent, t: Float = 10f) {
        scale(event.getCount().toFloat(), t)
    }

    private fun setIdentityMatrix() {
        matrix.set(
                1f, 0f, 0f, 0f,
                0f, 1f, 0f, 0f,
                0f, 0f, 1f, 0f,
                0f, 0f, 0f, 1f
        )
        rotateMatrix.set(
                1f, 0f, 0f, 0f,
                0f, 1f, 0f, 0f,
                0f, 0f, 1f, 0f,
                0f, 0f, 0f, 1f
        )
    }

    private fun rotate(v1: PVector, v2: PVector) {
        val v = v1.cross(v2).normalize() // 回転軸
        val c = v1.dot(v2) // cos
        val s = v1.cross(v2).mag() // sin
        val m = PMatrix3D(
                c + v.x*v.x*(1-c), v.x*v.y*(1-c) - v.z*s, v.x*v.z*(1-c) + v.y*s, 0f,
                v.y*v.x*(1-c) + v.z*s, c + v.y*v.y*(1-c), v.y*v.z*(1-c) - v.x*s, 0f,
                v.z*v.x*(1-c) - v.y*s, v.z*v.y*(1-c) + v.x*s, c + v.z*v.z*(1-c), 0f,
                0f, 0f, 0f, 1f
        )
        matrix.preApply(m)
        rotateMatrix.preApply(m)
    }

    private fun translate(v1: PVector, v2: PVector, t: Float) {
        val m = PMatrix3D(
                1f, 0f, 0f, (v2.x-v1.x)/t,
                0f, 1f, 0f, (v2.y-v1.y)/t,
                0f, 0f, 1f, (v2.z-v1.z)/t,
                0f, 0f, 0f, 1f
        )
        matrix.preApply(m)
    }

    private fun scale(wheelCount: Float, t: Float) {
        val m = PMatrix3D(
                PApplet.exp(-wheelCount/t), 0f, 0f, 0f,
                0f, PApplet.exp(-wheelCount/t), 0f, 0f,
                0f, 0f, PApplet.exp(-wheelCount/t), 0f,
                0f, 0f, 0f, 1f
        )
        matrix.preApply(m)
    }

    // マウスの座標から球面上の位置ベクトルを取得する
    private fun mouseOnSphere(x: Float, y: Float): PVector {
        val _x = x/radius
        val _y = y/radius
        val res = PVector(_x, _y, 0f)
        if (_x*_x + _y*_y > 1f) {
            res.normalize()
        } else {
            res.z = PApplet.sqrt(1f - _x*_x - _y*_y)
        }
        return res
    }

}
