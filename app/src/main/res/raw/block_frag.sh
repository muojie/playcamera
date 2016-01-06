#extension GL_OES_EGL_image_external : require

precision mediump float;

varying vec2 vTextureCoord;
uniform samplerExternalOES sTexture;
void main() {
  vec4 rgba = texture2D(sTexture, vTextureCoord );
  float avg = rgba.r + rgba.g + rgba.b;
  float pixel = avg*0.33;
  if(pixel > 0.5)
     pixel = 1.0;
  else
     pixel = 0.0;
  gl_FragColor = vec4(pixel,pixel,pixel, 1.0);
