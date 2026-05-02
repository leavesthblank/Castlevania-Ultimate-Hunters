#version 120

uniform sampler2D DiffuseSampler;

varying vec2 texCoord;
varying vec2 oneTexel;

uniform vec3 Gray = vec3(0.3, 0.59, 0.11);
uniform vec3 RedMatrix   = vec3(1.0, 0.0, 0.0);
uniform vec3 GreenMatrix = vec3(0.0, 1.0, 0.0);
uniform vec3 BlueMatrix  = vec3(0.0, 0.0, 1.0);
uniform vec3 Offset = vec3(0.0, 0.0, 0.0);
uniform vec3 ColorScale = vec3(1.0, 1.0, 1.0);

uniform float Time;
uniform vec2 Frequency;

vec3 hue(float h)
{
    float r = abs(h * 6.0 - 3.0) - 1.0;
    float g = 2 - abs(h * 6.0 - 2.0);
    float b = 2 - abs(h * 6.0 - 4.0);
    return clamp(vec3(r, g, b), 0.0, 1.0);
}

vec3 HSVtoRGB(vec3 hsv) {
    return ((hue(hsv.x) - 1.0) * hsv.y + 1.0) * hsv.z;
}

vec3 RGBtoHSV(vec3 rgb) {
    vec3 hsv = vec3(0.0);
    hsv.z = max(rgb.r, max(rgb.g, rgb.b));
    float min = min(rgb.r, min(rgb.g, rgb.b));
    float c = hsv.z - min;

    if (c != 0)
    {
        hsv.y = c / hsv.z;
        vec3 delta = (hsv.z - rgb) / c;
        delta.rgb -= delta.brg;
        delta.rg += vec2(2.0, 4.0);
        if (rgb.r >= hsv.z) {
            hsv.x = delta.b;
        } else if (rgb.g >= hsv.z) {
            hsv.x = delta.r;
        } else {
            hsv.x = delta.g;
        }
        hsv.x = fract(hsv.x / 6.0);
    }
    return hsv;
}

vec3 GetManipulatedColor(vec4 InTexel) {
    float xOffset = sin(texCoord.y * Frequency.x + Time * 3.1415926535 * 2.0) * 0.4;
    float yOffset = cos(texCoord.x * Frequency.y + Time * 3.1415926535 * 2.0) * 0.2;
    vec2 offset = vec2(xOffset, yOffset);
    vec3 hsv = RGBtoHSV(InTexel.rgb);
    hsv.x = fract(hsv.x + Time);
    vec3 OutColor = vec3(HSVtoRGB(hsv));
    return OutColor;
}

float ball_clipping_multipler(vec2 pos, vec2 center, float radius) {
    vec2 delta = pos - center;
    float distance = length(delta);
    if (distance <= radius) {
        return 1.0;
    } else {
        return 0.0;
    }
}

float boolrelu(float t, float t0, float t1) {
    float s = (t - t0) / (t1 - t0);
    if (s < 0.0) return 0.0;
    if (s > 1.0) return 1.0;
    return s;
}
/*
t: current time
t0-t1: expand
t1-t2: hold
t2-t3: collapse
*/
float ball_effect(vec2 pos, vec2 center, float radius, float t, float t0, float t1, float t2, float t3) {
    radius = radius * (boolrelu(t, t0, t1) - boolrelu(t, t2, t3));
    return ball_clipping_multipler(pos, center, radius);
}


vec3 grey() {
    vec4 InTexel = texture2D(DiffuseSampler, texCoord);

    // Color Matrix
    float RedValue = dot(InTexel.rgb, RedMatrix);
    float GreenValue = dot(InTexel.rgb, GreenMatrix);
    float BlueValue = dot(InTexel.rgb, BlueMatrix);
    vec3 OutColor = vec3(RedValue, GreenValue, BlueValue);

    // Offset & Scale
    OutColor = (OutColor * ColorScale) + Offset;

    // Saturation
    float Luma = dot(OutColor, Gray);
    vec3 Chroma = OutColor - Luma;
    return (Chroma * 0.2) + Luma;
}

void main() {
    vec4 InTexel = texture2D(DiffuseSampler, texCoord);
    vec3 OriginalColor = GetManipulatedColor(InTexel);
    vec3 greyscale = grey();

    float idk = ball_effect(
    texCoord,
    vec2(0.5, 0.01),
    1.5,
    Time,
    0.0, 0.05,
    0.55, 0.8
    );

    vec3 OutColor = /*InTexel.rgb*/  greyscale * (1 - idk) + OriginalColor * idk;
    gl_FragColor = vec4(OutColor, InTexel.a);
}