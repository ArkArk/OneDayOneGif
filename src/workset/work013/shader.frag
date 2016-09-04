#define PI 3.141592653589793
uniform vec2 iResolution;
uniform float iGlobalTime;

float getT() {
    float t = mod(iGlobalTime/30.0, 2.0);
    return (t < 1.0) ? t : 2.0-t;
}

float easeInOutQuad(float t) {
    t *= 2.0;
    if (t < 1.0) {
        return 0.5 * t*t;
    } else {
        return -0.5 * ((t-1.0)*(t-3.0)-1.0);
    }
}

vec2 convert(vec2 uv, vec2 origin) {
    float s = 0.6/PI;
    float r = length(uv-origin);
    float theta = mod(atan(uv.y - origin.y, uv.x - origin.x) + PI/2.0 + PI, 2.0*PI) - PI;
    return vec2(s * theta, -r*1.0) + origin;
}

vec4 calc(vec2 p, vec2 origin, float t) {
    vec2 q = convert(p, origin);
    vec2 uv = mix(p, q, t);

    uv *= 3.2;

    const int maxIterations = 10000;
    uv.y += 1.6;
    vec2 z = vec2(0.0);
    int n = 0;
    for(int i=0; i<maxIterations; i++) {
        z = vec2(z.x*z.x-z.y*z.y, 2.0*z.x*z.y) + uv;
        if (length(z) > 4.0) break;
        n++;
    }
    if (n == maxIterations) {
        return vec4(vec3(0.0), 1.0);
    } else {
        return vec4(vec3(mod(float(n), 16.0) /16.0), 1.0);
    }
}

void main() {
	vec2 uv = (2.0*gl_FragCoord.xy-iResolution.xy) / min(iResolution.x, iResolution.y);
    float t = easeInOutQuad(getT());
    gl_FragColor = calc(uv, vec2(0, 0), t);
}