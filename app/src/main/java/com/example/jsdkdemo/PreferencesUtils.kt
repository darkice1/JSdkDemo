package com.example.jsdkdemo

import android.content.Context
import android.content.SharedPreferences
import java.lang.reflect.InvocationTargetException
import java.lang.reflect.Method

object PreferencesUtils {
    var FILE_NAME = "jsdk_local"
    const val PID = "pid"
    const val WIDTH = "width"
    const val HEIGHT = "height"
    const val TYPE = "type"
    const val NORMAL_PID = "normal_pid"
    const val NORMAL_WIDTH = "normal_width"
    const val NORMAL_HEIGHT = "normal_height"
    const val SPLASH_PID = "splash_pid"
    const val SPLASH_WIDTH = "splash_width"
    const val SPLASH_HEIGHT = "splash_height"
    const val SELF_PID = "self_pid"
    const val SELF_WIDTH = "self_width"
    const val SELF_HEIGHT = "self_height"
    const val POP_PID = "pop_pid"
    const val POP_WIDTH = "pop_width"
    const val POP_HEIGHT = "pop_height"
    private var sSharedPreferences: SharedPreferences? = null
    fun init(context: Context) {
        sSharedPreferences = context.getSharedPreferences(FILE_NAME,
                Context.MODE_PRIVATE)
    }

    fun put(key: String?, `object`: Any) {
        val editor = sSharedPreferences!!.edit()
        if (`object` is String) {
            editor.putString(key, `object`)
        } else if (`object` is Int) {
            editor.putInt(key, `object`)
        } else if (`object` is Boolean) {
            editor.putBoolean(key, `object`)
        } else if (`object` is Float) {
            editor.putFloat(key, `object`)
        } else if (`object` is Long) {
            editor.putLong(key, `object`)
        } else {
            editor.putString(key, `object`.toString())
        }
        SharedPreferencesCompat.apply(editor)
    }

    operator fun get(key: String?, defaultObject: Any?): Any? {
        if (defaultObject is String) {
            return sSharedPreferences!!.getString(key, defaultObject as String?)
        } else if (defaultObject is Int) {
            return sSharedPreferences!!.getInt(key, (defaultObject as Int?)!!)
        } else if (defaultObject is Boolean) {
            return sSharedPreferences!!.getBoolean(key, (defaultObject as Boolean?)!!)
        } else if (defaultObject is Float) {
            return sSharedPreferences!!.getFloat(key, (defaultObject as Float?)!!)
        } else if (defaultObject is Long) {
            return sSharedPreferences!!.getLong(key, (defaultObject as Long?)!!)
        }
        return null
    }

    /**
     * 查询某个key是否已经存在
     *
     * @param key
     * @return
     */
    operator fun contains(key: String?): Boolean {
        return sSharedPreferences!!.contains(key)
    }

    /**
     * 移除某个key值已经对应的值
     *
     * @param key
     */
    fun remove(key: String?) {
        val editor = sSharedPreferences!!.edit()
        editor.remove(key)
        SharedPreferencesCompat.apply(editor)
    }

    /**
     * 清除所有数据
     */
    fun clearAll() {
        val editor = sSharedPreferences!!.edit()
        editor.clear()
        SharedPreferencesCompat.apply(editor)
    }

    /**
     * 创建一个解决SharedPreferencesCompat.apply方法的一个兼容类
     */
    private object SharedPreferencesCompat {
        private val sApplyMethod = findApplyMethod()

        /**
         * 反射查找apply的方法
         *
         * @return
         */
        private fun findApplyMethod(): Method? {
            try {
                val clz: Class<*> = SharedPreferences.Editor::class.java
                return clz.getMethod("apply")
            } catch (e: NoSuchMethodException) {
            }
            return null
        }

        /**
         * 如果找到则使用apply执行，否则使用commit
         *
         * @param editor
         */
        fun apply(editor: SharedPreferences.Editor) {
            try {
                if (sApplyMethod != null) {
                    sApplyMethod.invoke(editor)
                    return
                }
            } catch (e: IllegalArgumentException) {
            } catch (e: IllegalAccessException) {
            } catch (e: InvocationTargetException) {
            }
            editor.commit()
        }
    }
}