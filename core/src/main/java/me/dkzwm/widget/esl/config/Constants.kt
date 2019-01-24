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
package me.dkzwm.widget.esl.config

object Constants {
    const val DIRECTION_LEFT = 0x01
    const val DIRECTION_TOP = 0x01 shl 1
    const val DIRECTION_RIGHT = 0x01 shl 2
    const val DIRECTION_BOTTOM = 0x01 shl 3
    const val DIRECTION_HORIZONTAL = 0x05
    const  val DIRECTION_VERTICAL = 0x0A
    const val DIRECTION_ALL = 0x0F

    const val STYLE_NONE = 0
    const val STYLE_MIUI = 1
    const val STYLE_JIKE = 2
    const val STYLE_CUSTOM = 3

    const val DEFAULT_RESISTANCE = 3f
    const val DEFAULT_DURATION_OF_CLOSE = 500
}
