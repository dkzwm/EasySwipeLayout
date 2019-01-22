/*
 * MIT License
 *
 * Copyright (c) 2018 dkzwm
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package me.dkzwm.widget.esl.config;

public interface Constants {
    int DIRECTION_LEFT = 0x01;
    int DIRECTION_TOP = 0x01 << 1;
    int DIRECTION_RIGHT = 0x01 << 2;
    int DIRECTION_BOTTOM = 0x01 << 3;
    int DIRECTION_HORIZONTAL = 0x05;
    int DIRECTION_VERTICAL = 0x0A;
    int DIRECTION_ALL = 0x0F;

    int STYLE_NONE = 0;
    int STYLE_MIUI = 1;
    int STYLE_JIKE = 2;
    int STYLE_CUSTOM = 3;

    float DEFAULT_RESISTANCE = 0.35F;
    int DEFAULT_DURATION_OF_CLOSE = 500;
}
