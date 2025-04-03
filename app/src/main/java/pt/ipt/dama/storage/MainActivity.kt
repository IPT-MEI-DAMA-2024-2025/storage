package pt.ipt.dama.storage

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Environment
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.snackbar.Snackbar
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.PrintStream
import java.util.Scanner

class MainActivity : AppCompatActivity() {

    // Shared Preferences
    private lateinit var txt1: EditText

    // Shared Preferences, múltiplos ficheiros
    private lateinit var txt2nome: EditText
    private lateinit var txt2idade: EditText

    // Cache
    private lateinit var txt3nome: EditText
    private lateinit var txt3idade: EditText

    // Internal Storage
    private lateinit var txt4nome: EditText
    private lateinit var txt4idade: EditText

    // External Storage
    private lateinit var txt5nome: EditText
    private lateinit var txt5idade: EditText

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

        // Cache
        // tarefa 3
        txt3nome = findViewById(R.id.editText3a)
        txt3idade = findViewById(R.id.editText3b)
        findViewById<Button>(R.id.btWrite3).setOnClickListener {
            escreveCache()
            esconderTeclado(it)
        }
        findViewById<Button>(R.id.btRead3).setOnClickListener {
            lerCache()
        }

        // Internal Storage
        // tarefa 4
        txt4nome = findViewById(R.id.editText4a)
        txt4idade = findViewById(R.id.editText4b)
        findViewById<Button>(R.id.btWrite4).setOnClickListener {
            escreveInternalStorage()
            esconderTeclado(it)
        }
        findViewById<Button>(R.id.btRead4).setOnClickListener {
            lerInternalStorage()
        }

        // External Storage
        // tarefa 5
        txt5nome = findViewById(R.id.editText5a)
        txt5idade = findViewById(R.id.editText5b)
        findViewById<Button>(R.id.btWrite5).setOnClickListener {
            // não esquecer!
            // Se a versão do Android for <= 18, HÁ NECESSIDADE De PEDIR
            // EXPLICITAMENTE AUTORIZAÇÃO AO UTILIZADOR PARA ACEDER AO
            // ARMAZENAMENTO EXTERNO
            escreveExternalStorage()
            esconderTeclado(it)
        }
        findViewById<Button>(R.id.btRead5).setOnClickListener {
            lerExternalStorage()
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
        editorIdades.putInt(
            "IDADE",
            txt2idade.text.toString().toIntOrNull() ?: 0
        ) // se a Idade=NULL, coloca 0
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
        val sharedPreferencesNome = getSharedPreferences("nomes.dat", MODE_PRIVATE)
        val sharedPreferencesIdade = getSharedPreferences("idades.dat", MODE_PRIVATE)
        // ler o conteúdo da Shared Preferences. Se não for possível a leitura, devolve um texto alternativo
        // estou a usar o método getString() porque guardei 'texto'. Outros tipos de dados,
        // implicam outros métodos
        val nome = sharedPreferencesNome.getString("NOME", getString(R.string.txt_vazio))
        val idade = sharedPreferencesIdade.getInt("IDADE", 0)

        // usar os dados lidos
        val contextView = findViewById<View>(R.id.main)
        Snackbar.make(
            contextView,
            "Você chama-se ${nome.toString().uppercase()} e tem $idade anos!",
            Snackbar.LENGTH_SHORT
        ).show()
    }


    // TAREFA 3
    /**
     * Escrever os valores introduzidos pelo utilizador na Cache
     */
    private fun escreveCache() {
        // endereço da pasta onde será gerado o ficheiro
        // associado à área de atuação da aplicação
        val directory: File = cacheDir
        // nome do ficheiro na Cache
        val file: File = File(directory, "dadosCache.txt")
        try {
            val fo: FileOutputStream = FileOutputStream(file)
            val ps: PrintStream = PrintStream(fo)
            ps.println(txt3nome.text)
            ps.println(txt3idade.text)
            ps.close()
            fo.close()

            // neste caso, vamos mostrar uma mensagem de 'conforto' ao utilizador
            Toast.makeText(this, getString(R.string.dados_guardados), Toast.LENGTH_SHORT).show()
        } catch (e: FileNotFoundException) {
            Toast.makeText(this, getString(R.string.escritaErradaCache), Toast.LENGTH_LONG).show()
        }
    }

    /**
     * Ler os valores introduzidos pelo utilizador da Cache
     */
    private fun lerCache() {
        val directory: File = cacheDir // <=> getCacheDir()
        val file = File(directory, "dadosCache.txt")
        try {
            val fi = FileInputStream(file)
            val sc = Scanner(fi)
            val nome = sc.nextLine()
            val idade = sc.nextLine()
            sc.close()
            fi.close()

            // mostrar os dados
            val contextView = findViewById<View>(R.id.main)
            Snackbar.make(
                contextView,
                "Você chama-se ${nome.toString().uppercase()} e tem $idade anos!",
                Snackbar.LENGTH_LONG
            ).show()
        } catch (e: FileNotFoundException) {
            Toast.makeText(this, getString(R.string.cache_vazia), Toast.LENGTH_LONG).show()
        }

    }


    // TAREFA 4
    /**
     * Escrever os valores introduzidos pelo utilizador na Internal Storage
     */
    private fun escreveInternalStorage() {
        // endereço da pasta onde será gerado o ficheiro
        // associado à área de atuação da aplicação
        val directory: File = filesDir
        // nome do ficheiro na Internal Storage
        val file: File = File(directory, "dadosInternalStorage.txt")
        try {
            val fo: FileOutputStream = FileOutputStream(file)
            val ps: PrintStream = PrintStream(fo)
            ps.println(txt4nome.text)
            ps.println(txt4idade.text)
            ps.close()
            fo.close()

            // neste caso, vamos mostrar uma mensagem de 'conforto' ao utilizador
            Toast.makeText(this, getString(R.string.dados_guardados), Toast.LENGTH_SHORT).show()
        } catch (e: FileNotFoundException) {
            Toast.makeText(this, getString(R.string.escritaErradaCache), Toast.LENGTH_LONG).show()
        }
    }

    /**
     * Ler os valores introduzidos pelo utilizador da Internal Storage
     */
    private fun lerInternalStorage() {
        val directory: File = filesDir
        val file = File(directory, "dadosInternalStorage.txt")
        try {
            val fi = FileInputStream(file)
            val sc = Scanner(fi)
            val nome = sc.nextLine()
            val idade = sc.nextLine()
            sc.close()
            fi.close()

            // mostrar os dados
            val contextView = findViewById<View>(R.id.main)
            Snackbar.make(
                contextView,
                "Você chama-se ${
                    nome.toString().uppercase()
                } e tem $idade anos! - Internal Storage",
                Snackbar.LENGTH_LONG
            ).show()
        } catch (e: FileNotFoundException) {
            Toast.makeText(this, getString(R.string.internal_storage_vazia), Toast.LENGTH_LONG)
                .show()
        }

    }


// TAREFA 5
    /**
     * Escrever os valores introduzidos pelo utilizador na External Storage
     */
    private fun escreveExternalStorage() {
        // endereço da pasta onde será gerado o ficheiro
        // associado à área de atuação da aplicação
        val directory: File = getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)!!

        // nome do ficheiro na External Storage
        val file: File = File(directory, "dadosExternalStorage.txt")
        try {
            val fo: FileOutputStream = FileOutputStream(file)
            val ps: PrintStream = PrintStream(fo)
            ps.println(txt5nome.text)
            ps.println(txt5idade.text)
            ps.close()
            fo.close()

            // neste caso, vamos mostrar uma mensagem de 'conforto' ao utilizador
            Toast.makeText(this, getString(R.string.dados_guardados), Toast.LENGTH_SHORT).show()
        } catch (e: FileNotFoundException) {
            Toast.makeText(
                this,
                getString(R.string.escritaErradaExternalStorage),
                Toast.LENGTH_LONG
            ).show()
        }
    }

    /**
     * Ler os valores introduzidos pelo utilizador da External Storage
     */
    private fun lerExternalStorage() {
        val directory: File = Environment.getExternalStorageDirectory()
        val file = File(directory, "dadosExternalStorage.txt")
        try {
            val fi = FileInputStream(file)
            val sc = Scanner(fi)
            val nome = sc.nextLine()
            val idade = sc.nextLine()
            sc.close()
            fi.close()

            // mostrar os dados
            val contextView = findViewById<View>(R.id.main)
            Snackbar.make(
                contextView,
                "Você chama-se ${
                    nome.toString().uppercase()
                } e tem $idade anos! - External Storage",
                Snackbar.LENGTH_LONG
            ).show()
        } catch (e: FileNotFoundException) {
            Toast.makeText(this, getString(R.string.external_storage_vazia), Toast.LENGTH_LONG)
                .show()
        }

    }


    /**
     * Esconder o teclado
     * @param view Referencia o contexto onde o teclado está visível
     */
    private fun esconderTeclado(view: View) {
        // hide the keyboard
        val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

}