#version 420 core
//---------------------------------------------------------------------------


in vec2 pos;                    // screen position <-1,+1>
                                // fragment output color
uniform sampler2D txr;          // texture to blu

uniform float sizeX;
uniform float sizeY;
uniform float stepSize;
uniform float subDiv;
uniform float thresholdDivider;
uniform float darkening;
//---------------------------------------------------------------------------
void main()
    {

    vec2 xStep = vec2(1./sizeX, 0.) * stepSize;
    vec2 yStep = vec2(0.,1./sizeY) * stepSize;

    float unitSquared = stepSize*stepSize;
    float radius = subDiv*subDiv*unitSquared;
    float count = 0;
    vec4 result = vec4(0.,0.0,0.,0.);

    vec4 selfColor = texture(txr, pos);

    if (selfColor.a > 0) {
        if (darkening < 0)
        {
            result = vec4(1.,1.,1.,1.);
        }
        else
        {
            result = vec4(selfColor.r * darkening,selfColor.r * darkening,selfColor.r * darkening,1.);
        }
    } else {
        for (int i = -int(subDiv); i <= int(subDiv); i++) {
            for (int j = -int(subDiv); j <= int(subDiv); j++) {
                if (float(i*i)*unitSquared + float(j*j)*unitSquared < radius) {
                    vec4 color = texture(txr, pos + float(i)*xStep + float(j)*yStep);
                    count += color.a;
                }
            }
        }
    }
    if (count > subDiv*subDiv/thresholdDivider) result = vec4(1.,1.,1.,1.);

    gl_FragColor = result;
    }