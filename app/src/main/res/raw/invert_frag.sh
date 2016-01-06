#extension GL_OES_EGL_image_external : require
precision mediump float;
varying vec2 vTextureCoord;
uniform samplerExternalOES sTexture;
void main() {
  vec4 rgba = texture2D( sTexture, vTextureCoord );
  rgba.r = 1.0 - rgba.r;
  rgba.g = 1.0 - rgba.g;
  rgba.b = 1.0 - rgba.b;
  gl_FragColor = vec4(rgba);
}
