

uniform vec2 u_screenSize;
uniform float u_time;
uniform sampler2D u_tex0;
//#define LAYERS 200
//#define DEPTH .1
//#define WIDTH .8
//#define SPEED 1.5
// as uniforms
uniform int LAYERS;
uniform float DEPTH;
uniform float WIDTH;
uniform float SPEED;
uniform vec4 u_color;
uniform vec4 u_tint;
uniform float u_usetex;




// Fragment Shader: Snow
void main()
{
    const mat3 p = mat3(13.323122,23.5112,21.71123,21.1212,28.7312,11.9312,21.8112,14.7212,61.3934);
    vec2 uv = u_screenSize.xy + vec2(1.,u_screenSize.y/u_screenSize.x)*gl_FragCoord.xy / u_screenSize.xy;
    //get the color from the texture and add it to the accumulator
    vec3 acc = vec3(0.);

    acc = texture2D(u_tex0,gl_FragCoord.xy / u_screenSize.xy).rgb;
    acc = acc * u_usetex;

    float alpha = max(texture2D(u_tex0,gl_FragCoord.xy / u_screenSize.xy).a, 0.0);

        if (u_usetex == 0.0) {
            alpha = texture2D(u_tex0,gl_FragCoord.xy / u_screenSize.xy).a > 0.01 ? 1.0 : 0.0;
            alpha -= texture2D(u_tex0,gl_FragCoord.xy / u_screenSize.xy).a*3.0;
            acc += u_color.rgb;
        }

    acc *= u_tint.rgb;


    float dof = 5.*sin(u_time*.1);
    for (int i=0;i<LAYERS;i++) {
        float fi = float(i);
        vec2 q = uv*(1.+fi*DEPTH);
        q += vec2(q.y*(WIDTH*mod(fi*7.238917,1.)-WIDTH*.5),SPEED*u_time/(1.+fi*DEPTH*.03));
        vec3 n = vec3(floor(q),31.189+fi);
        vec3 m = floor(n)*.00001 + fract(n);
        vec3 mp = (31415.9+m)/fract(p*m);
        vec3 r = fract(mp);
        vec2 s = abs(mod(q,1.)-.5+.9*r.xy-.45);
        s += .01*abs(2.*fract(10.*q.yx)-1.);
        float d = .6*max(s.x-s.y,s.x+s.y)+max(s.x,s.y)-.01;
        float edge = .005+.05*min(.5*abs(fi-5.-dof),1.);
        acc += vec3(smoothstep(edge,-edge,d)*(r.x/(1.+.02*fi*DEPTH))) * u_color.rgb;
    }


    gl_FragColor = vec4(acc,alpha);
}
