#extension GL_OES_EGL_image_external : require
precision mediump float;
varying vec2 vTextureCoord;
uniform samplerExternalOES sTexture;
void main() {
  vec4 rgba = texture2D( sTexture, vTextureCoord );
  float value = rgba.r < 0.5 ? rgba.r : 1.0 - rgba.r;
  float newR = (value * value * value)/0.25;
  newR = (rgba.r < 0.5 ? newR : 1.0 - newR);
  value = rgba.g < 0.5 ? rgba.g : 1.0 - rgba.g;
  float newG = (value * value) /0.5;
  newG = (rgba.g < 0.5 ? newG : 1.0 - newG);
  float newB = (rgba.b /2.0) + 0.144;
  float dis = sqrt(pow(abs(0.5-vTextureCoord.x), 2.0) + pow(abs(0.5-vTextureCoord.y), 2.0));
  float radius = 0.4;
  float pa = pow((0.707-dis)/0.307,2.0);
  if (dis > radius) {
    newR = newR*pa;
    newG = newG*pa;
    newB = newB*pa;
  }
  gl_FragColor = vec4(newR,newG,newB, 1.0);
}
