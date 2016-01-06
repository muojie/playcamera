#extension GL_OES_EGL_image_external : require
precision mediump float;
varying vec2 vTextureCoord;
uniform samplerExternalOES sTexture;
void main() {
  vec4 rgba = texture2D( sTexture, vTextureCoord );
  float cx = 0.5;
  float cy = 0.381;
  float max = cx * cx + cy * cy;
  float diff = max / 2.0;
  float dx = cx - vTextureCoord.x;
  float dy = cy - vTextureCoord.y;
  float distSq = dx * dx + dy * dy;
  float v = (distSq / diff) * 0.5;
  float r = rgba.r + v;
  float g = rgba.g + v;
  float b = rgba.b + v;
  r = r > 1.0 ? 1.0 : (r < 0.0 ? 0.0 : r);
  g = g > 1.0 ? 1.0 : (g < 0.0 ? 0.0 : g);
  b = b > 1.0 ? 1.0 : (b < 0.0 ? 0.0 : b);
  gl_FragColor = vec4(r,g,b, 1.0);
}
