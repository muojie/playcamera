#extension GL_OES_EGL_image_external : require
precision mediump float;
varying vec2 vTextureCoord;
uniform samplerExternalOES sTexture;
void main() {
  vec4 rgba = texture2D( sTexture, vTextureCoord );
  float pixR = rgba.r;
  float pixG = rgba.g;
  float pixB = rgba.b;
  float r = 0.393 * pixR + 0.769 * pixG + 0.189 * pixB;
  float g = 0.349 * pixR + 0.686 * pixG + 0.168 * pixB;
  float b = 0.272 * pixR + 0.534 * pixG + 0.131 * pixB;
  r = r > 1.0 ? 1.0 : (r < 0.0 ? 0.0 : r);
  g = g > 1.0 ? 1.0 : (g < 0.0 ? 0.0 : g);
  b = b > 1.0 ? 1.0 : (b < 0.0 ? 0.0 : b);
  gl_FragColor = vec4(r,g,b, 1.0);
}
