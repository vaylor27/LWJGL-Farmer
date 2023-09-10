#version 330

in vec2 pass_textureCoords;
in vec3 surfaceNormal;
in vec3 toLightVector;

out vec4 out_Color;

uniform sampler2D textureSampler;
uniform vec3 lightColor;
uniform float ambientLight;
uniform vec3 skyColor;

void main(void){
	if (visibility <= 0) {
		discard;
	}
	vec4 textureColor = texture(textureSampler, pass_textureCoords);
	if (textureColor.a < 0.5) {
		discard;
	}

	vec3 unitNormal = normalize(surfaceNormal);
	vec3 unitLight = normalize(toLightVector);

	float nDot1 = dot(unitNormal, unitLight);
	float brightness = max(nDot1, ambientLight);
	vec3 diffuse = brightness * lightColor;

	out_Color = vec4(diffuse, 1.0) * textureColor;
	out_Color = mix(vec4(skyColor, 1.0), out_Color, visibility);
}