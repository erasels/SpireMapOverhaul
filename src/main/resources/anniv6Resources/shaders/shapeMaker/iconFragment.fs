varying vec2 pos;                    // screen position <-1,+1>
                                // fragment output color
uniform sampler2D txr;          // texture to blu

uniform float alphaBonus;

//Shader should:
//transform all colors into same color except alpha depending on brightness (dark = more alpha)
//srcalpha = 0 means final alpha = 0 too
void main()
{
    vec4 selfColor = texture2D(txr,pos);
    float brightness = (selfColor.r + selfColor.g + selfColor.b)/3.;
    float alpha = selfColor.a * alphaBonus - brightness;
    gl_FragColor = vec4(1-brightness,1-brightness,1-brightness,selfColor.a);
}