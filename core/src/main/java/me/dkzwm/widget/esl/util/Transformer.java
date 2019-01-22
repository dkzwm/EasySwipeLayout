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
package me.dkzwm.widget.esl.util;

import android.content.Context;
import android.text.TextUtils;
import java.lang.reflect.Constructor;
import me.dkzwm.widget.esl.graphics.Drawer;

public class Transformer {
    private static final Class[] CONSTRUCTOR_PARAMS = new Class[] {Context.class};

    public static Drawer parseDrawer(Context context, String name) {
        if (TextUtils.isEmpty(name)) {
            return null;
        } else {
            String fullName;
            if (name.startsWith(".")) {
                fullName = context.getPackageName() + name;
            } else {
                fullName = name;
            }
            try {
                @SuppressWarnings("unchecked")
                Class<Drawer> clazz = (Class<Drawer>) context.getClassLoader().loadClass(fullName);
                Constructor<Drawer> constructor = clazz.getConstructor(CONSTRUCTOR_PARAMS);
                constructor.setAccessible(true);
                return constructor.newInstance(context);
            } catch (Exception e) {
                throw new RuntimeException("Could not inflate Painter subclass " + fullName, e);
            }
        }
    }
}
