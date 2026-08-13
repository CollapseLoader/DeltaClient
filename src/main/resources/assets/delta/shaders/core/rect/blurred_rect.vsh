#version 150

#moj_import <delta:common.glsl>

in vec3 Position;
in vec4 Color;
in vec2 UV0;

uniform mat4 ModelViewMat;
uniform mat4 ProjMat;

out vec2 FragCoord;
out vec4 FragColor;
out vec2 TexCoord;

void main() {
    FragCoord = rvertexcoord(gl_VertexID);
    FragColor = Color;
    TexCoord = UV0;

    gl_Position = ProjMat * ModelViewMat * vec4(Position, 1.0);
}