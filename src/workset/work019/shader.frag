#define MAX 1e10
#define EPS 1e-2
#define PI 3.141592653589793

uniform vec2 iResolution;
uniform float iGlobalTime;
uniform mat4 cameraMatrix;
uniform mat4 cameraRotateMatrix;

struct {
    vec3 pos;
    vec3 dir;
    vec3 up;
    vec3 side;
    float focus;
} camera;

struct {
    vec3 pos;
} light;

struct {
    vec3 dir;
} ray;

struct {
    int id;
    vec3 pos;
    vec3 diffuseColor;
    vec3 specularColor;
    vec3 ambientColor;
    float specularPower;
} intersect;

float getT() {
    float t = mod(iGlobalTime/240.0, 2.0);
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

float distTorus(vec3 p, vec2 t) {
    vec2 q = vec2(length(p.xz)-t.x,p.y);
    return length(q)-t.y;
}

float distTwist(vec3 p) {
    float r = easeInOutQuad(getT())*0.8;
    float c = cos(PI*p.y*r);
    float s = sin(PI*p.y*r);
    mat2  m = mat2(c,-s,s,c);
    vec3  q = vec3(m*p.xz, p.y);
    return distTorus(q, vec2(5.0, 1.0));
}

float getMinDist(vec3 pos) {
    float res = MAX;
    res = min(res, distTwist(pos));
    return res;
}

vec3 getNormal(vec3 pos) {
    return normalize(vec3(
        getMinDist(pos+vec3(EPS, 0.0, 0.0)) - getMinDist(pos),
        getMinDist(pos+vec3(0.0, EPS, 0.0)) - getMinDist(pos),
        getMinDist(pos+vec3(0.0, 0.0, EPS)) - getMinDist(pos)
    ));
}

void setPhong() {
    intersect.diffuseColor = vec3(0.2, 0.4, 0.7);
    intersect.specularColor = vec3(1.0, 1.0, 1.0) * 0.9;
    intersect.ambientColor = vec3(0.0, 0.02, 0.10);
    intersect.specularPower = 20;
}

vec4 calc(vec2 uv) {
    float d;
    intersect.pos = camera.pos;
    for(int i=0; i<160; i++) {
        d = getMinDist(intersect.pos);
        intersect.pos += (d/10)*ray.dir;
        if (abs(d) < EPS) break;
    }

    setPhong();
    vec3 n = getNormal(intersect.pos);
    vec3 lightDir = - normalize(intersect.pos - light.pos);
    vec3 eyeDir = - normalize(intersect.pos - camera.pos);
    float diffuse = clamp(dot(n, lightDir), 0.0, 1.0);
    float specular = pow(clamp(dot(n, normalize(lightDir+eyeDir)), 0.0, 1.0), intersect.specularPower);
    float ambient = 1.0;

    if (abs(d) < EPS) {
        return vec4(diffuse * intersect.diffuseColor + specular * intersect.specularColor + ambient * intersect.ambientColor, 1.0);
    } else {
        return vec4(vec3(0.0), 1.0);
    }
}

vec3 fun(vec3 v, mat4 m) {
    return (vec4(v, 1.0) * inverse(m)).xyz;
}

void init(vec2 pos) {
    camera.pos = vec3(0.0, 0.0, 20.0);
    camera.pos = fun(camera.pos, cameraMatrix);
    camera.dir = vec3(0.0, 0.0, -1.0);
    camera.dir = normalize(fun(camera.dir, cameraRotateMatrix));
    camera.up = vec3(0.0, 1.0, 0.0);
    camera.up = normalize(fun(camera.up, cameraRotateMatrix));
    camera.side = cross(camera.dir, camera.up);
    camera.focus = 1.8;

    light.pos = vec3(15.0, 20.0, 5.0);
    //light.pos = (cameraMatrix * vec4(light.pos, 1.0)).xyz;

    ray.dir = normalize(camera.side*pos.x + camera.up*pos.y + camera.dir*camera.focus);
}

void main() {
	vec2 uv = (2.0*gl_FragCoord.xy - iResolution.xy) / min(iResolution.x, iResolution.y);
    init(uv);
	gl_FragColor = calc(uv);
}