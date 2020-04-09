package ru.apap26.utis

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences // Настройки
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*
import java.net.ConnectException
import java.net.Socket
import java.net.SocketTimeoutException

class MainActivity : AppCompatActivity() {
    val errorValue:String = "Sanyaaaa"
    val apiUrl:String = "http://127.0.0.1:8000/api"
    // Блок работы с настройками
    val APP_PREFERENCES:String = "settings" // Имя файла настроек
    val APP_PREFERENCES_LOGIN = "login" // Имя сторки содержащей логин
    val APP_PREFERENCES_PASSWORD = "password"
    lateinit var mSettings:SharedPreferences // Поля класса для последуещей инициализации в классе
    lateinit var login_save:String
    lateinit var password_save:String
    lateinit var editSettings:SharedPreferences.Editor // = mSettings.edit()

    override fun onCreate(savedInstanceState: Bundle?) {
        mSettings = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE) // Получаем настройки
        editSettings = mSettings.edit()
         // Это здесь не нужно))))
        if(mSettings.contains(APP_PREFERENCES_LOGIN) && mSettings.contains(APP_PREFERENCES_PASSWORD)){
            login_save = mSettings.getString(APP_PREFERENCES_LOGIN, errorValue).toString() // Почему же я вызываю метод конвертации к строке, у метода который возвращяет строку. Вопрос риторический
            password_save = mSettings.getString(APP_PREFERENCES_PASSWORD, errorValue).toString()
        }else{
            login_save = errorValue
            password_save = errorValue
        }
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if(login_save != errorValue && password_save != errorValue){
            login.setText(login_save)
            password.setText(password_save)
        }
        login_btn.setOnClickListener(::ClickOnLogin_btn)


        Log.d("F","Ff")

    }
    fun ClickOnLogin_btn(v:View){
        result.setText("")
        login_save = login.text.toString()
        password_save = password.text.toString()
        // Место для кода акунтификации
        Thread{
            // Сань, ты наверное спросишь, Что это за фигня
            // Це поток. Есть такое требование. Оно гласит: "Не посылай запросы в сеть в гланом потоке"
            // Если ты положишь болт на это требование, устройство в тебя плюнет исключением
            // NetworkOnUiThreadExceptions
            // Так что, делай по аналогии.
            val student = UTISAction(login_save, password_save)
            try {
                if (student.auth()) {
                    runOnUiThread {
                        editSettings.putString(APP_PREFERENCES_LOGIN, login_save)
                        editSettings.putString(APP_PREFERENCES_PASSWORD, password_save)
                        editSettings.apply()
                        var intent = Intent(this, UTIS::class.java)
                        startActivity(intent)
                    }
                } else {
                    runOnUiThread {
                        result.setText("НЕПРАВИЛЬНО, ПОПРОБУЙ ЕЩЕ РАЗ!")
                    }
                }
            }catch (e: ConnectException){
                runOnUiThread{
                    result.setText("Ошибка подключения к серверу")
                }
            }catch (e:SocketTimeoutException){
                runOnUiThread{
                    result.setText("Ошибка подключения к серверу")
                }
            }
        }.start()
        // Конец места для кода акунтефикации
    }
}
