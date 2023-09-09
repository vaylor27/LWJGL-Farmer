#version 330

in vec2 pass_textureCoords;
in vec3 surfaceNormal;
in vec3 toLightVector;
in vec3 toCameraVector;

out vec4 out_Color;

uniform sampler2D textureSampler;
uniform vec3 lightColor;
uniform float shineDamper;
uniform float reflectivity;
uniform float ambientLight;

void main(void){

	vec3 unitNormal = normalize(surfaceNormal);
	vec3 unitLight = normalize(toLightVector);

	float nDot1 = dot(unitNormal, unitLight);
	float brightness = max(nDot1, ambientLight);
	vec3 diffuse = brightness * lightColor;

	vec3 unitVectorToCamera = normalize(toCameraVector);
	vec3 lightDirection = -unitLight;
	vec3 reflectedLightDirection = reflect(lightDirection, unitNormal);

	float specularFactor = dot(reflectedLightDirection, unitVectorToCamera);
	specularFactor = max(specularFactor, 0.0);
	float dampedFactor = pow(specularFactor, shineDamper);
	vec3 finalSpecular = max(dampedFactor * reflectivity * lightColor, 0.0);

	vec4 textureColor = texture(textureSampler, pass_textureCoords);
	if (textureColor.a < 0.5) {
		discard;
	}
	out_Color = vec4(diffuse, 1.0) * textureColor + vec4(finalSpecular, 1.0);
}