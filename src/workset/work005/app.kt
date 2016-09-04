package workset.work005

import processing.core.PApplet
import processing.core.PGraphics
import util.Function

// 2016/07/20
class App : PApplet() {
    private val NUM = 9
    private lateinit var gra: Array<PGraphics>

    override fun settings() {
        size(400, 600, P2D)
    }

    override fun setup() {
        background(0)
        imageMode(CENTER)
        blendMode(ADD)
        gra = Array(NUM) {
            val c = getRGB(360f*it/NUM)
            createLight(red(c)/255+0.7f, green(c)/255+0.7f, blue(c)/255+0.7f)
        }
    }

    override fun draw() {
        background(0)

        strokeWeight(5f)
        textSize(36f)
        stroke(255)
        fill(255)
        text(frameCount.toString(), 10f, height.toFloat()-10f)

        translate(width/2f, height/2f)

        rotate(radians(frameCount*360f/780*2f))

        val m = 360f
        val duration = 60f
        val N = if (frameCount%(2*m+duration) < m) {
            Function.easeInQuad(frameCount%(2*m+duration), 1, NUM, m).toFloat()
        } else if (frameCount%(2*m+duration) < m+duration) {
            NUM.toFloat()
        } else {
            Function.easeInQuad(2*m+duration - frameCount%(2*m+duration), 1, NUM, m).toFloat()
        }
        val radius = 150f
        val ang = 360f
        for(i in 0..floor(N)) {
            if (i==NUM) break;
            val r = if (i > N-1) N-floor(N) else 1f
            val c = getRGB(360f*(i*2%NUM)/NUM)
            tint(255f, 255f * r)
            stroke(red(c), green(c), blue(c), 30f * r)
            rotate(radians(360f/NUM))
            image(gra[i*2%NUM], 0f, radius*(cos(radians(Function.linear(i, frameCount*6, ang, NUM).toFloat()))))
            line(0f, -radius*10, 0f, radius*10)
        }
    }

    fun getRGB(hue: Float): Int {
        val h = hue%360
        return if(h<60) {
            color(255f, (h / 60) * 255, 0f)
        } else if (h<120) {
            color(((120 - h) / 60) * 255, 255f, 0f)
        } else if (h<180) {
            color(0f, 255f, ((h - 120) / 60) * 255)
        } else if (h<240) {
            color(0f, ((240 - h) / 60) * 255, 255f)
        } else if (h<300) {
            color(((h - 240) / 60) * 255, 0f, 255f)
        } else {
            color(255f, 0f, ((360 - h) / 60) * 255)
        }
    }

    fun createLight(rPower: Float, gPower: Float, bPower: Float): PGraphics {
        val side = 200 // 画像の一辺の大きさ
        val center = side/2f
        val pg = createGraphics(side, side)
        pg.beginDraw()
        pg.loadPixels()
        for(x in 0..side-1) for(y in 0..side-1) {
            val d = (sq(x-center)+sq(y-center)) / 20f
            val r = 255*rPower/d
            val g = 255*gPower/d
            val b = 255*bPower/d
            pg.pixels[x+y*side] = color(r, g, b, 255f)
        }
        pg.updatePixels()
        pg.endDraw()
        return pg
    }
}