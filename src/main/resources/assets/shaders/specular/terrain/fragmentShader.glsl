#version 330

in vec2 pass_textureCoords;
in vec3 surfaceNormal;
in vec3 toLightVector[5];
in vec3 toCameraVector;
in float visibility;

out vec4 out_Color;

uniform sampler2D textureSampler;
uniform vec3 lightColor[5];
uniform vec3 attenuation[5];
uniform float shineDamper;
uniform float reflectivity;
uniform float ambientLight;
uniform vec3 skyColor;

void main(void) {

	if (visibility <= 0) {
		discard;
	}

	vec4 textureColor = texture(textureSampler, pass_textureCoords);
	if (textureColor.a < 0.5) {
		discard;
	}

	vec3 unitNormal = normalize(surfaceNormal);
	vec3 unitVectorToCamera = normalize(toCameraVector);

	vec3 totalDiffuse = vec3(0.0);
	vec3 totalSpecular = vec3(0.0);

	for (int i = 0; i < 4; i++) {
		float distance = length(toLightVector[i]);
		float attenuationFactor = attenuation[i].x + (attenuation[i].y * distance) + (attenuation[i].z * distance * distance);

		vec3 unitLight = normalize(toLightVector[i]);
		float nDot1 = dot(unitNormal, unitLight);
		float brightness = max(nDot1, 0.0);
		vec3 lightDirection = -unitLight;
		vec3 reflectedLightDirection = reflect(lightDirection, unitNormal);
		float specularFactor = dot(reflectedLightDirection, unitVectorToCamera);
		specularFactor = max(specularFactor, 0.0);
		float dampedFactor = pow(specularFactor, shineDamper);
		totalDiffuse = totalDiffuse + (brightness * lightColor[i]) / attenuationFactor;
		totalSpecular = max(totalSpecular + (dampedFactor * reflectivity * lightColor[i]) / attenuationFactor, 0.0);
	}

	totalDiffuse = max(totalDiffuse, ambientLight);

	out_Color = vec4(totalDiffuse, 1.0) * textureColor + vec4(totalSpecular, 1.0);
	out_Color = mix(vec4(skyColor, 1.0), out_Color, visibility);
}