#define PI 3.141592653589793
uniform vec2 iResolution;
uniform float iGlobalTime;

float getT() {
    float t = mod(iGlobalTime/120.0, 2.0);
    return (t < 1.0) ? t : 2.0-t;
}

float easeInOutQuart(float t) {
    t *= 2.0;
    if (t < 1.0) {
        return 0.5 * t*t*t*t;
    } else {
        t -= 2.0;
        return -0.5 * (t*t*t*t - 2.0);
    }
}

vec2 convert(vec2 uv, vec2 origin) {
    float s = 2.0/PI;
    float r = length(uv-origin);
    float theta = mod(atan(uv.y - origin.y, uv.x - origin.x) + PI/2.0 + PI, 2.0*PI) - PI;
    return vec2(s * theta, -r*2.0) + origin;
}

float calc(vec2 p, vec2 origin, float t) {
    vec2 q = convert(p, origin);
    vec2 uv = mix(p, q, t);

    float diff = 0.1;
    float a = 0.2;
    uv += vec2(diff);
    float d_x = min(mod(uv.x, a), mod(-uv.x, a));
    float d_y = min(mod(uv.y, a), mod(-uv.y, a));
    float d = min(d_x, d_y);
    return 0.02/d;
}

void main() {
	vec2 uv = (2.0*gl_FragCoord.xy-iResolution.xy) / min(iResolution.x, iResolution.y);
    float t = easeInOutQuart(getT());
    float col = calc(uv, vec2(0, 0), t);
    gl_FragColor = vec4(col*vec3(0.2, 0.45, 0.8), 1.0);
}