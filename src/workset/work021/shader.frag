#define INF 1e10
#define EPS 1e-2
#define PI 3.141592653589793

uniform vec2 iResolution;
uniform float iGlobalTime;
uniform vec2 mousePos;

vec2 rotate(vec2 v, float angle) {
    mat2 m = mat2(cos(angle), -sin(angle), sin(angle), cos(angle));
    return m * v;
}

float distSquare(vec2 p, vec2 c, float size, float angle) {
    p = rotate(p-c, angle)+c;

    vec2 ary[4] = {
        vec2(-size/2, -size/2),
        vec2(-size/2, +size/2),
        vec2(+size/2, -size/2),
        vec2(+size/2, +size/2)
    };
    for(int i=0; i<4; i++) {
        ary[i] = ary[i] + c;
    }

    float res = INF;
    for(int i=0; i<4; i++) {
        res = min(res, length(p - ary[i]));
    }
    if (-size/2<(p.y-c.y) && (p.y-c.y)<size/2) {
        res = min(res, abs(p.x-(c.x-size/2)));
        res = min(res, abs(p.x-(c.x+size/2)));
    }
    if (-size/2<(p.x-c.x) && (p.x-c.x)<size/2) {
        res = min(res, abs(p.y-(c.y-size/2)));
        res = min(res, abs(p.y-(c.y+size/2)));
    }
    return res;
}

float dist(vec2 p) {
    float res = INF;
    const int numX = 40;
    const int numY = 40;
    float size = 1.0/numX;
    ivec2 po = ivec2((p-size/2)/size);
    float aryX[2] = {po.x, po.x+1};
    float aryY[2] = {po.y, po.y+1};
    for(int i=0; i<2; i++) for(int j=0; j<2; j++) {
        res = min(res, distSquare(p, vec2(size*aryX[i], size*aryY[j]) + size/2, size, PI/4.0));
    }

//    for(int i=0; i<numX; i++) for(int j=0; j<numY; j++) {
//        res = min(res, distSquare(p, vec2(size*i, size*j) + size/2, size, 0.0));
//    }
    return res;
}

vec2 convert(vec2 p, vec2 c, float radius) {
    return (p-c)*min(1.0, length((p-c)/radius)) + c;
}

vec2 convert(vec2 p, vec2 c, float radius1, float radius2) {
    p -= c;
    float center = (radius1+radius2)/2.0;
    float width = abs(radius1-radius2)/2.0;
    vec2 q = normalize(p)*center;
    float leng = length(p-q);
    float l = min(1.0, leng/width);
    return (p-q)*l + c + q;
}

vec4 calc(vec2 p) {
    float width = 0.2;
    float t = mod(iGlobalTime, 160.0)/100.0;
    p = convert(p, mousePos, -width+t, t);
    float d = dist(p);
    return vec4(vec3(0.001/d), 1);
}

void main() {
	vec2 uv = gl_FragCoord.xy / min(iResolution.x, iResolution.y);
	gl_FragColor = calc(uv);
}