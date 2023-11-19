#version 400

in vec3 textureCoords;
out vec4 out_Color;

const float lowerLimit = 0.0;
const float upperLimit = 30.0;

uniform samplerCube cubeMap;

void main(void) {

    out_Color = texture(cubeMap, textureCoords);
}