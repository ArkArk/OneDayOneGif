uniform mat4 transform;
uniform mat4 texMatrix;
uniform float iGlobalTime;

attribute vec4 vertex;
attribute vec2 texCoord;

varying vec4 vertTexCoord;

vec4 rotateY(vec4 p, float angle) {
    float s = sin(angle);
    float c = cos(angle);
    return vec4(c*p.x + s*p.z, p.y, -s*p.x + c*p.z, p.w);
}

void main() {
    float t = pow(iGlobalTime/100.0, 8.0);
    gl_Position = transform * rotateY(vertex, radians(t * texCoord.y));
    vertTexCoord = texMatrix * vec4(texCoord, 1.0, 1.0);
}
