uniform vec2 iResolution;
uniform vec3 v1, v2;

vec2 mult(vec2 z1, vec2 z2) {
    return vec2(z1.x*z2.x - z1.y*z2.y, z1.x*z2.y + z1.y*z2.x);
}

void main() {
    float zoom = v1.x;
    vec2 uv = gl_FragCoord.xy*2.0-iResolution;
    uv = (vec3(uv, 1)*inverse(mat3(v1, v2, vec3(0, 0, 1)))).xy;
    uv = 1.5*uv/min(iResolution.x, iResolution.y);

    const int MAX = 256;
    vec2 z = vec2(0.0);
    vec2 dz = vec2(0.0);
    int i;
    for(i=0; i<MAX; i++) {
        dz = 2.0*mult(z, dz) + vec2(1.0, 0.0);
        z = mult(z, z) + uv;
        if (length(z) > 512.0) break;
    }
    float dist = length(z)/length(dz) * log(length(z));
    dist = clamp(dist, 0.0, 1.0);
    dist = pow(dist*4.0*zoom, 0.25);

    gl_FragColor = vec4(vec3(dist), 1.0);
}