//shamelessly stolen from #modding-technical AND from "user1118321"'s comment on stackoverflow
    //https://stackoverflow.com/questions/9234724/how-to-change-hue-of-a-texture-with-glsl

//SpriteBatch will use texture unit 0
uniform sampler2D u_texture;
uniform sampler2D u_screenTexture;
uniform vec2 u_screenSize;

uniform float hueShift;
uniform float intensity;

//"in" varyings from our vertex shader
varying vec4 v_color;
varying vec2 v_texCoord;

void main() {
    //sample the texture
    //vec4 texColor = texture2D(u_texture, v_texCoord);
    vec4 color = texture2D(u_screenTexture, gl_FragCoord.xy / u_screenSize).rgba;
    vec4 oldColor = texture2D(u_screenTexture, gl_FragCoord.xy / u_screenSize).rgba;

    const vec4  kRGBToYPrime = vec4 (0.299, 0.587, 0.114, 0.0);
    const vec4  kRGBToI     = vec4 (0.596, -0.275, -0.321, 0.0);
    const vec4  kRGBToQ     = vec4 (0.212, -0.523, 0.311, 0.0);
    const vec4  kYIQToR   = vec4 (1.0, 0.956, 0.621, 0.0);
    const vec4  kYIQToG   = vec4 (1.0, -0.272, -0.647, 0.0);
    const vec4  kYIQToB   = vec4 (1.0, -1.107, 1.704, 0.0);

    // Convert to YIQ
    float   YPrime  = dot (color, kRGBToYPrime);
    float   I      = dot (color, kRGBToI);
    float   Q      = dot (color, kRGBToQ);

    gl_FragColor = color;
    // Calculate the hue and chroma
    float   hue     = atan (Q, I);
    float   chroma  = sqrt (I * I + Q * Q);

    // Make the user's adjustments
    hue += hueShift;

    // Convert back to YIQ
    Q = chroma * sin (hue);
    I = chroma * cos (hue);

    // Convert back to RGB
    vec4    yIQ   = vec4 (YPrime, I, Q, 0.0);
    color.r = dot (yIQ, kYIQToR);
    color.g = dot (yIQ, kYIQToG);
    color.b = dot (yIQ, kYIQToB);

    color.r=oldColor.r+(color.r-oldColor.r)*intensity;
    color.g=oldColor.g+(color.g-oldColor.g)*intensity;
    color.b=oldColor.b+(color.b-oldColor.b)*intensity;

    // Save the result
    gl_FragColor    = color;
}
