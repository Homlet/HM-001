varying vec4 color;
uniform sampler2D tex0;

void main()
{
	gl_FragColor = texture2D(tex0, gl_TexCoord[0].xy) * color;
}