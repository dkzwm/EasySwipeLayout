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
package me.dkzwm.widget.esl.util

import android.content.Context
import android.text.TextUtils
import me.dkzwm.widget.esl.graphics.Drawer

object Transformer {
    private val CONSTRUCTOR_PARAMS = arrayOf<Class<*>>(Context::class.java)

    @JvmStatic
    fun parseDrawer(context: Context, name: String?): Drawer? {
        return if (TextUtils.isEmpty(name)) {
            null
        } else {
            val fullName: String? = if (name?.startsWith(".") == true) context.packageName + name else name
            try {
                @Suppress("UNCHECKED_CAST")
                val clazz = context.classLoader.loadClass(fullName) as Class<Drawer>
                val constructor = clazz.getConstructor(*CONSTRUCTOR_PARAMS)
                constructor.isAccessible = true
                constructor.newInstance(context)
            } catch (e: Exception) {
                throw RuntimeException("Could not inflate Drawer subclass $fullName", e)
            }
        }
    }
}
