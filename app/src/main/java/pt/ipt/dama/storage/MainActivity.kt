package pt.ipt.dama.storage

import android.view.inputmethod.InputMethodManager
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {

    // Shared Preferences
    private lateinit var txt1: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Shared Preferences
        txt1 = findViewById(R.id.editText1)
        findViewById<Button>(R.id.btWrite1).setOnClickListener {
            escreveSharedPreferences()
            esconderTeclado(it)
        }
        findViewById<Button>(R.id.btRead1).setOnClickListener {
            lerSharedPrefences()
        }
    }

     /**
     * Escreve o conteúdo introduzido pelo utilizador, num único 'ficheiro',
     * nas Shared Preferences.
     */
    private fun escreveSharedPreferences() {
        val sharedPreferences=getPreferences(MODE_PRIVATE)
        val editor:SharedPreferences.Editor=sharedPreferences.edit()

        // colocar o conteúdo que o utilizador escreveu nas Shared Preferences
        // Os dados são sempre escritos no formato: 'Variável'+'valor'
        // usa-se o método putXXXXX, onde XXXXX significa String, Int, Double, etc.
        editor.putString("SPreferenceSimples",txt1.text.toString())
        // guardar as alterações
        editor.commit()  // eventualmente, <=>   editor.apply()

        // neste caso, vamos mostrar uma mensagem de 'conforto' ao utilizador
        Toast.makeText(this, getString(R.string.dados_guardados), Toast.LENGTH_SHORT).show()
    }

    /**
     * Lê o conteúdo introduzido pelo utilizador, dum único 'ficheiro',
     * guardado nas Shared Preferences.
     */
    private fun lerSharedPrefences() {
        val sharedPreferences=getPreferences(MODE_PRIVATE)
        // ler o conteúdo da Shared Preferences. Se não for possível a leitura, devolve um texto alternativo
        // estou a usar o método getString() porque guardei 'texto'. Outros tipos de dados,
        // implicam outros métodos
        val texto=sharedPreferences.getString("SPreferenceSimples",getString(R.string.txt_vazio))

        // usar os dados lidos
        val contextView = findViewById<View>(R.id.main)
        Snackbar.make(contextView,"Introduziu: $texto", Snackbar.LENGTH_SHORT).show()
    }




    /**
     * Esconder o teclado
     * @param view Referencia o contexto onde o teclado está visível
     */
    private fun esconderTeclado(view:View) {
        // hide the keyboard
        val imm=getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken,0)
    }

}