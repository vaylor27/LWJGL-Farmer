#version 330

in vec2 pass_textureCoords;
in vec3 surfaceNormal;
in vec3 toLightVector;

out vec4 out_Color;

uniform sampler2D textureSampler;
uniform vec3 lightColor;
uniform float ambientLight;

void main(void){

	vec3 unitNormal = normalize(surfaceNormal);
	vec3 unitLight = normalize(toLightVector);

	float nDot1 = dot(unitNormal, unitLight);
	float brightness = max(nDot1, ambientLight);
	vec3 diffuse = brightness * lightColor;

	vec4 textureColor = texture(textureSampler, pass_textureCoords);
	if (textureColor.a < 0.5) {
		discard;
	}

	out_Color = vec4(diffuse, 1.0) * textureColor;
}