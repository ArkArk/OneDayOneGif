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
    p -= c;
    float r = (radius - min(radius, length(p))) / radius;
    float t = easeInOutQuad(getT(iGlobalTime, 480.0));
    //p *= exp(r*t*2.0);
    p = rotate(p, r*PI*t*24.0);
    return p + c;
}

vec4 calc(vec2 p) {
    p = convert(p, vec2(0.5, 0.5), 0.52);

    p.y = 1.0 - p.y;
    vec4 res = texture2D(tex, p);
    if (res.w < 0.5) res = vec4(1.0);
    return res;
}

void main() {
	vec2 uv = gl_FragCoord.xy / iResolution.xy;
	gl_FragColor = calc(uv);
}