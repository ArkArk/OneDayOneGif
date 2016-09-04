#define PI 3.14159265359
uniform vec2 iResolution;
uniform float iGlobalTime;

vec3 pixelToHex(in vec2 pixel, in float size) {
    float q = (sqrt(3.0)/3.0 * pixel.x - 1.0/3.0 * pixel.y) / size;
    float r = (0.0 * pixel.x + 2.0/3.0 * pixel.y) / size;
    return vec3(q, r, -q-r);
}

float maxHex(in vec3 hex) {
    return max(abs(hex.x), max(abs(hex.y), abs(hex.z)));
}

float distHex(in vec3 hex, in float size) {
    hex /= size;
    vec3 nearest = round(hex);
    vec3 diff = abs(nearest - hex);
    if (diff.x > diff.y && diff.x > diff.z) {
        nearest.x = -nearest.y-nearest.z;
    } else if (diff.y > diff.z) {
        nearest.y = -nearest.x-nearest.z;
    } else {
        nearest.z = -nearest.x-nearest.y;
    }
    return maxHex(abs(nearest-hex));
}

vec2 rotate(in vec2 v, in float angle) {
    float c = cos(radians(angle)); // cos
    float s = sin(radians(angle)); // sin
    float x = c*v.x - s*v.y;
    float y = s*v.x + c*v.y;
    return vec2(x, y);
}

float calc(in vec2 uv, in int flag) {
    float size = 40.0;
    vec3 hex = pixelToHex(uv, size);
    if (flag == 0) {
    } else if (flag == 1) {
        hex.x += size*1.0/3.0;
        hex.y -= size*2.0/3.0;
        hex.z += size*1.0/3.0;
    } else if (flag == 2) {
        hex.x -= size*1.0/3.0;
        hex.y += size*2.0/3.0;
        hex.z -= size*1.0/3.0;
    }
    float leng = distHex(hex, 1.0);
    leng *= 3.0/2.0;
    return smoothstep(0.0, 1.0, leng);
}

vec3 get(float ang) {
    float h = mod(ang, 360.0);
    if(h<120.0) {
        return vec3((120.0-h)/120.0, h/120.0, 0.0);
    } else if (h<240.0) {
        return vec3(0.0, (240.0-h)/120.0, (h-120.0)/120.0);
    } else {
        return vec3((h-240.0)/120, 0.0, (360.0-h)/120);
    }
}


void main() {
    vec2 uv = (gl_FragCoord.xy*2.0-iResolution)/2.0;
    vec3 r = get(iGlobalTime);
    float dest1 = calc(uv, 0)*r.x;
    float dest2 = calc(uv, 1)*r.y;
    float dest3 = calc(uv, 2)*r.z;
    float dest = dest1 + dest2 + dest3;
    vec3 col2 = vec3(0.05, 0.25, 0.2);
    vec3 col1 = vec3(0.9, 1.0, 0.9);
    vec3 col = mix(col1, col2, dest);
    gl_FragColor = vec4(col, 1.0);
}
