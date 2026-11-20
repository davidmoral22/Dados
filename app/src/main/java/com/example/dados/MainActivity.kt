package com.example.dados

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    private lateinit var edtObjetivo: EditText
    private lateinit var imgCubilete: ImageView
    private lateinit var imgDado1: ImageView
    private lateinit var imgDado2: ImageView
    private lateinit var imgDado3: ImageView
    private lateinit var txtResultado: TextView

    private val handler = Handler(Looper.getMainLooper())
    private val imagenesDados = intArrayOf(
        R.drawable.dado1,
        R.drawable.dado2,
        R.drawable.dado3,
        R.drawable.dado4,
        R.drawable.dado5,
        R.drawable.dado6
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        edtObjetivo = findViewById(R.id.edtObjetivo)
        imgCubilete = findViewById(R.id.imgCubilete)
        imgDado1 = findViewById(R.id.imgDado1)
        imgDado2 = findViewById(R.id.imgDado2)
        imgDado3 = findViewById(R.id.imgDado3)
        txtResultado = findViewById(R.id.txtResultado)

        imgCubilete.setOnClickListener {
            iniciarTirada()
        }
    }

    private fun iniciarTirada() {
        val numeroObjetivo = edtObjetivo.text.toString().toIntOrNull()

        if (numeroObjetivo == null || numeroObjetivo !in 3..18) {
            Toast.makeText(this, "Introduce un número entre 3 y 18", Toast.LENGTH_SHORT).show()
            return
        }

        // Bloquea el cubilete mientras dura la tirada
        imgCubilete.isEnabled = false
        txtResultado.visibility = View.INVISIBLE

        val totalPasos = 8
        val intervaloMs = 150L

        for (paso in 0 until totalPasos) {
            handler.postDelayed({
                val valor1 = Random.nextInt(1, 7)
                val valor2 = Random.nextInt(1, 7)
                val valor3 = Random.nextInt(1, 7)

                imgDado1.setImageResource(imagenesDados[valor1 - 1])
                imgDado2.setImageResource(imagenesDados[valor2 - 1])
                imgDado3.setImageResource(imagenesDados[valor3 - 1])

                // En el último paso se calcula el resultado final y se reactiva el cubilete
                if (paso == totalPasos - 1) {
                    val suma = valor1 + valor2 + valor3
                    txtResultado.text = "Suma obtenida: $suma"
                    txtResultado.visibility = View.VISIBLE

                    imgCubilete.isEnabled = true

                    if (suma == numeroObjetivo) {
                        mostrarDialogoAcierto(numeroObjetivo, suma)
                    }
                }
            }, paso * intervaloMs)
        }
    }

    private fun mostrarDialogoAcierto(objetivo: Int, suma: Int) {
        AlertDialog.Builder(this)
            .setTitle("¡Acierto!")
            .setMessage("El programa ha sacado $suma y ha acertado tu número ($objetivo).")
            .setPositiveButton("Aceptar", null)
            .show()
    }
}