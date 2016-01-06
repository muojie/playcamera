#extension GL_OES_EGL_image_external : require

precision mediump float;

uniform vec2 uPixelSize;
uniform samplerExternalOES sTexture;
varying vec2 vTextureCoord;

void main()
{
   vec3 border;
   vec3 color;
   float dx = uPixelSize.x * 1.5;
   float dy = uPixelSize.y * 1.5;
    vec3 sample0 = texture2D(sTexture, vec2(vTextureCoord.x - dx, vTextureCoord.y + dy)).rgb;
    vec3 sample1 = texture2D(sTexture, vec2(vTextureCoord.x - dx, vTextureCoord.y)).rgb;
    vec3 sample2 = texture2D(sTexture, vec2(vTextureCoord.x - dx, vTextureCoord.y - dy)).rgb;
    vec3 sample3 = texture2D(sTexture, vec2(vTextureCoord.x, vTextureCoord.y + dy)).rgb;
    vec3 sample4 = texture2D(sTexture, vec2(vTextureCoord.x, vTextureCoord.y)).rgb;
    vec3 sample5 = texture2D(sTexture, vec2(vTextureCoord.x, vTextureCoord.y - dy)).rgb;
    vec3 sample6 = texture2D(sTexture, vec2(vTextureCoord.x + dx, vTextureCoord.y + dy)).rgb;
    vec3 sample7 = texture2D(sTexture, vec2(vTextureCoord.x + dx, vTextureCoord.y)).rgb;
    vec3 sample8 = texture2D(sTexture, vec2(vTextureCoord.x + dx, vTextureCoord.y - dy)).rgb;

    color = (sample0 + sample1 + sample2 + sample3 + sample4 + sample5 + sample6 + sample7 + sample8) / 9.0;

    vec3 horizEdge = sample2 + sample5 + sample8 - (sample0 + sample3 + sample6);
    vec3 vertEdge = sample0 + sample1 + sample2 - (sample6 + sample7 + sample8);

    border = sqrt((horizEdge * horizEdge) + (vertEdge * vertEdge));

    if (border.r > 0.3 || border.g > 0.3 || border.b > 0.3){
        color *= 1.0 - dot(border, border);
    }

    const vec3 colorRed = vec3(1.0, 0.3, 0.3);
    const vec3 colorGreen = vec3(0.3, 1.0, 0.3);
    const vec3 colorBlue = vec3(0.3, 0.3, 1.0);

    color = floor(color * 8.0) * 0.125;
    color = colorRed * color.r + colorBlue * color.b + colorGreen * color.g;

    gl_FragColor = vec4(color.r,color.b,color.g, 1.0);
}