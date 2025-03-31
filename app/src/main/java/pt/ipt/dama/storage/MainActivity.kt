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
    private lateinit var txt2nome: EditText
    private lateinit var txt2idade: EditText

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

        // Shared Preferences (múltiplos ficheiros)
        // tarefa 2
        txt2nome = findViewById(R.id.editText2a)
        txt2idade = findViewById(R.id.editText2b)
        findViewById<Button>(R.id.btWrite2).setOnClickListener {
            escreveSharedPreferencesMulti()
            esconderTeclado(it)
        }
        findViewById<Button>(R.id.btRead2).setOnClickListener {
            lerSharedPrefencesMulti()
        }

    }


    // TAREFA 1

    /**
     * Escreve o conteúdo introduzido pelo utilizador, num único 'ficheiro',
     * nas Shared Preferences.
     */
    private fun escreveSharedPreferences() {
        val sharedPreferences = getPreferences(MODE_PRIVATE)
        val editor: SharedPreferences.Editor = sharedPreferences.edit()

        // colocar o conteúdo que o utilizador escreveu nas Shared Preferences
        // Os dados são sempre escritos no formato: 'Variável'+'valor'
        // usa-se o método putXXXXX, onde XXXXX significa String, Int, Double, etc.
        editor.putString("SPreferenceSimples", txt1.text.toString())
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
        val sharedPreferences = getPreferences(MODE_PRIVATE)
        // ler o conteúdo da Shared Preferences. Se não for possível a leitura, devolve um texto alternativo
        // estou a usar o método getString() porque guardei 'texto'. Outros tipos de dados,
        // implicam outros métodos
        val texto = sharedPreferences.getString("SPreferenceSimples", getString(R.string.txt_vazio))

        // usar os dados lidos
        val contextView = findViewById<View>(R.id.main)
        Snackbar.make(contextView, "Introduziu: $texto", Snackbar.LENGTH_SHORT).show()
    }


    // TAREFA 2

    /**
     * Escreve os dados introduzidos pelo utilizador em dois 'ficheiros'
     * na Shared Preferences
     */
    private fun escreveSharedPreferencesMulti() {
        val sharedPreferencesNomes = getSharedPreferences("nomes.dat", MODE_PRIVATE)
        val sharedPreferencesIdades = getSharedPreferences("idades.dat", MODE_PRIVATE)

        val editorNomes: SharedPreferences.Editor = sharedPreferencesNomes.edit()
        val editorIdades: SharedPreferences.Editor = sharedPreferencesIdades.edit()

        // colocar o conteúdo que o utilizador escreveu nas Shared Preferences
        // Os dados são sempre escritos no formato: 'Variável'+'valor'
        // usa-se o método putXXXXX, onde XXXXX significa String, Int, Double, etc.
        editorNomes.putString("NOME", txt2nome.text.toString())
        editorIdades.putInt("IDADE", txt2idade.text.toString().toIntOrNull() ?: 0) // se a Idade=NULL, coloca 0
        // guardar as alterações
        editorNomes.commit()  // eventualmente, <=>   editorNomes.apply()
        editorIdades.commit()

        // neste caso, vamos mostrar uma mensagem de 'conforto' ao utilizador
        Toast.makeText(this, getString(R.string.dados_guardados), Toast.LENGTH_SHORT).show()
    }


    /**
     * Lê os dados introduzidos pelo utilizador, e guardados em dois 'ficheiros'
     * na Shared Preferences
     */
    private fun lerSharedPrefencesMulti() {
        val sharedPreferencesNome = getSharedPreferences("nomes.dat",MODE_PRIVATE)
        val sharedPreferencesIdade = getSharedPreferences("idades.dat",MODE_PRIVATE)
        // ler o conteúdo da Shared Preferences. Se não for possível a leitura, devolve um texto alternativo
        // estou a usar o método getString() porque guardei 'texto'. Outros tipos de dados,
        // implicam outros métodos
        val nome = sharedPreferencesNome.getString("NOME", getString(R.string.txt_vazio))
        val idade = sharedPreferencesIdade.getInt("IDADE", 0)

        // usar os dados lidos
        val contextView = findViewById<View>(R.id.main)
        Snackbar.make(contextView, "Você chama-se ${nome.toString().uppercase()} e tem $idade anos!", Snackbar.LENGTH_SHORT).show()
    }


    /**
     * Esconder o teclado
     * @param view Referencia o contexto onde o teclado está visível
     */
    private fun esconderTeclado(view: View) {
        // hide the keyboard
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

}