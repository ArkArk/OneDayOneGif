uniform mat4 transform;
uniform mat4 texMatrix;

attribute vec4 vertex;
attribute vec2 texCoord;
attribute vec3 diff;

varying vec4 vertTexCoord;

vec4 rotateY(vec4 p, float angle) {
    float s = sin(angle);
    float c = cos(angle);
    return vec4(c*p.x + s*p.z, p.y, -s*p.x + c*p.z, p.w);
}

void main() {
    vec4 tmp = vertex;
    tmp.xyz += diff;
    gl_Position = transform * tmp;
    vertTexCoord = texMatrix * vec4(texCoord, 1.0, 1.0);
}
