#define INF 1e10
#define EPS 1e-2
#define PI 3.141592653589793

uniform sampler2D tex;
uniform vec2 iResolution;
uniform float iGlobalTime;

float getT(float t, float duration) {
    t = mod(t/duration*2, 2.0);
    return (t < 1.0) ? t : 2.0-t;
}

float easeInOutQuad(float t) {
    t *= 2;
    if (t < 1.0) {
        return 0.5 * t*t;
    } else {
        return -0.5 * ((t-1)*(t-3) - 1);
    }
}

vec2 rotate(vec2 v, float angle) {
    mat2 m = mat2(cos(angle), -sin(angle), sin(angle), cos(angle));
    return m * v;
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
    p -= 0.5;

    const int N = 8;
    vec2 c = vec2(0.4, 0.0);
    for(int j=0; j<4; j++) {
        for(int i=0; i<N; i++) {
            float po = sin(2.0*PI*i/(2*N) + radians(iGlobalTime * 2.0));
            p = convert(p, c*po, 0.01+0.15*pow(abs(po), 0.5));
            c = rotate(c, 2.0*PI/(2*N));
        }
    }

    p += 0.5;

    p.y = 1.0 - p.y;
    return texture2D(tex, p);
}

void main() {
	vec2 uv = gl_FragCoord.xy / iResolution.xy;
	gl_FragColor = calc(uv);
}