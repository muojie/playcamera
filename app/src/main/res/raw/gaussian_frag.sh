//#extension GL_OES_EGL_image_external : require\n
precision mediump float
uniform vec2 uTcOffset[25];\n
varying vec2 vTextureCoord;\n
uniform sampler2D sTexture;\n
void main() {\n
  vec4 sample[25];\n" +

                        "  vec2 vCoord = vec2(1.0 - vTextureCoord.x, vTextureCoord.y);\n" +

                        "  sample[12] = texture2D(sTexture, vCoord + uTcOffset[12]); \n" +

                        "  sample[2] = texture2D(sTexture, vCoord + uTcOffset[2]); \n" +
                        "  sample[6] = texture2D(sTexture, vCoord + uTcOffset[6]); \n" +
                        "  sample[7] = texture2D(sTexture, vCoord + uTcOffset[7]); \n" +
                        "  sample[8] = texture2D(sTexture, vCoord + uTcOffset[8]); \n" +
                        "  sample[10] = texture2D(sTexture, vCoord + uTcOffset[10]); \n" +
                        "  sample[11] = texture2D(sTexture, vCoord + uTcOffset[11]); \n" +

                        "  sample[13] = texture2D(sTexture, vCoord + uTcOffset[13]); \n" +
                        "  sample[14] = texture2D(sTexture, vCoord + uTcOffset[14]); \n" +
                        "  sample[16] = texture2D(sTexture, vCoord + uTcOffset[16]); \n" +
                        "  sample[17] = texture2D(sTexture, vCoord + uTcOffset[17]); \n" +
                        "  sample[18] = texture2D(sTexture, vCoord + uTcOffset[18]); \n" +
                        "  sample[22] = texture2D(sTexture, vCoord + uTcOffset[22]); \n" +

                        "  sample[1] = texture2D(sTexture, vCoord + uTcOffset[1]); \n" +
                        "  sample[5] = texture2D(sTexture, vCoord + uTcOffset[5]); \n" +
                        "  sample[3] = texture2D(sTexture, vCoord + uTcOffset[3]); \n" +
                        "  sample[9] = texture2D(sTexture, vCoord + uTcOffset[9]); \n" +
                        "  sample[15] = texture2D(sTexture, vCoord + uTcOffset[15]); \n" +
                        "  sample[19] = texture2D(sTexture, vCoord + uTcOffset[19]); \n" +
                        "  sample[21] = texture2D(sTexture, vCoord + uTcOffset[21]); \n" +
                        "  sample[23] = texture2D(sTexture, vCoord + uTcOffset[23]); \n" +

                        "  sample[0] = texture2D(sTexture, vCoord + uTcOffset[0]); \n" +
                        "  sample[4] = texture2D(sTexture, vCoord + uTcOffset[4]); \n" +
                        "  sample[20] = texture2D(sTexture, vCoord + uTcOffset[20]); \n" +
                        "  sample[24] = texture2D(sTexture, vCoord + uTcOffset[24]); \n" +

                        "// Gaussian weighting:\n" +
                        "// 1  4  7  4 1\n" +
                        "// 4 16 26 16 4\n" +
                        "// 7 26 41 26 7 / 273 (i.e. divide by total of weightings)\n" +
                        "// 4 16 26 16 4\n" +
                        "// 1  4  7  4 1\n" +

                        "//  gl_FragColor = ( \n" +
                        "//       (1.0  * (sample[0] + sample[4]  + sample[20] + sample[24])) + \n" +
                        "//       (4.0  * (sample[1] + sample[3]  + sample[5]  + sample[9] + sample[15] + sample[19] + sample[21] + sample[23])) + \n" +
                        "//       (7.0  * (sample[2] + sample[10] + sample[14] + sample[22])) + \n" +
                        "//       (16.0 * (sample[6] + sample[8]  + sample[16] + sample[18])) + \n" +
                        "//       (26.0 * (sample[7] + sample[11] + sample[13] + sample[17])) + \n" +
                        "//       (41.0 * sample[12]) \n" +
                        "//       ) / 273.0; \n" +

                        "  vec4 color = ( \n" +
                        "       (sample[0] + sample[4]  + sample[20] + sample[24]) + \n" +
                        "       (sample[1] + sample[3]  + sample[5]  + sample[9] + sample[15] + sample[19] + sample[21] + sample[23]) + \n" +
                        "       (sample[2] + sample[10] + sample[14] + sample[22]) + \n" +
                        "       (sample[6] + sample[8]  + sample[16] + sample[18]) + \n" +
                        "       (sample[7] + sample[11] + sample[13] + sample[17]) + \n" +
                        "       sample[12] \n" +
                        "       ) / 58.0; \n" +
                        "  gl_FragColor = vec4(color.rgb, 1);\n" +
                        "}\n";
