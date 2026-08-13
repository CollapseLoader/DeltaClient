#version 150

in vec3 Position;
in vec2 UV0;
in vec4 Color;

uniform mat4 ModelViewMat;
uniform mat4 ProjMat;

out vec2 TexCoord;
out vec4 FragColor;
out float FragX;

void main() {
    TexCoord = UV0;
    FragColor = Color;
    FragX = Position.x;

    gl_Position = ProjMat * ModelViewMat * vec4(Position, 1.0);
}