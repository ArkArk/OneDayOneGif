#define INF 1e10
#define EPS 1e-2
#define PI 3.141592653589793

uniform sampler2D texture;

varying vec4 vertTexCoord;

void main() {
    gl_FragColor = texture2D(texture, vertTexCoord.st);
}