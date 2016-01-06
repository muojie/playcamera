#extension GL_OES_EGL_image_external : require
precision mediump float;
varying vec2 vTextureCoord;
uniform samplerExternalOES sTexture;
void main() {
  vec4 rgba = texture2D( sTexture, vTextureCoord );
  float pixel = 1.0 - ((1.0 - rgba.r) * (1.0 - rgba.r));
  if (pixel < 0.0)
    pixel = -pixel;
  pixel = pixel * rgba.r;
  if (pixel > 1.0)
    pixel = 1.0;
  rgba.r = pixel;
  pixel = 1.0 - ((1.0 - rgba.g) * (1.0 - rgba.g));
  if (pixel < 0.0)
    pixel = -pixel;
  pixel = pixel * rgba.r;
  if (pixel > 1.0)
    pixel = 1.0;
  rgba.g = pixel;
  pixel = 1.0 - ((1.0 - rgba.b) * (1.0 - rgba.b));
  if (pixel < 0.0)
    pixel = -pixel;
  pixel = pixel * rgba.g;
  if (pixel > 1.0)
    pixel = 1.0;
  rgba.b = pixel;
gl_FragColor = vec4(rgba);
}
