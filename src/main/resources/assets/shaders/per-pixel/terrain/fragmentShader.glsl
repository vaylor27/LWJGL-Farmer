#version 330

in vec2 pass_textureCoords;
in vec3 surfaceNormal;
in vec3 toLightVector[5];

out vec4 out_Color;

uniform sampler2D textureSampler;
uniform vec3 lightColor[5];
uniform vec3 attenuation[5];
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
	vec3 totalDiffuse = vec3(0.0);

	for (int i = 0; i < 5; i++) {
		float distance = length(toLightVector[i]);
		float attenuationFactor = attenuation[i].x + (attenuation[i].y * distance) + (attenuation[i].z * distance * distance);

		vec3 unitLight = normalize(toLightVector[i]);
		float nDot1 = dot(unitxNormal, unitLight);
		float brightness = max(nDot1, 0.0);

		totalDiffuse = totalDiffuse + (brightness * lightColor[i]) / attenuationFactor;
	}

	totalDiffuse = max(totalDiffuse, ambientLight);

	out_Color = vec4(totalDiffuse, 1.0) * textureColor;
	out_Color = mix(vec4(skyColor, 1.0), out_Color, visibility);
}