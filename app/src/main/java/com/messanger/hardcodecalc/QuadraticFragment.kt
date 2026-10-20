package com.messanger.hardcodecalc

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment

class QuadraticFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_quadratic, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val etA = view.findViewById<EditText>(R.id.quadraticA)
        val etB = view.findViewById<EditText>(R.id.quadraticB)
        val etC = view.findViewById<EditText>(R.id.quadraticC)
        val tvD = view.findViewById<TextView>(R.id.quadraticD)
        val tvResult = view.findViewById<TextView>(R.id.quadraticResult)

        val btnSolve = view.findViewById<Button>(R.id.btnSolveQuadratic)

        btnSolve.setOnClickListener {
            val a = etA.text.toString().toDoubleOrNull()
            val b = etB.text.toString().toDoubleOrNull()
            val c = etC.text.toString().toDoubleOrNull()

            if (a == null || b == null || c == null) {
                tvD.text = "Заполните все поля"
                tvResult.text = ""
                return@setOnClickListener
            }

            if (a == 0.0) {
                tvD.text = "a не может быть 0"
                tvResult.text = ""
                return@setOnClickListener
            }

            val discriminant = b * b - 4 * a * c
            tvD.text = "D = $discriminant"

            when {
                discriminant > 0 -> {
                    val sqrtD = Math.sqrt(discriminant)
                    val x1 = (-b + sqrtD) / (2 * a)
                    val x2 = (-b - sqrtD) / (2 * a)
                    val formattedX1 = String.format("%.10f", x1).replace(Regex("0+$"), "").replace(Regex("[.]$"), "")
                    val formattedX2 = String.format("%.10f", x2).replace(Regex("0+$"), "").replace(Regex("[.]$"), "")
                    tvResult.text = "x₁ = $formattedX1\nx₂ = $formattedX2"
                }
                discriminant == 0.0 -> {
                    val x = -b / (2 * a)
                    val formattedX = String.format("%.10f", x).replace(Regex("0+$"), "").replace(Regex("[.]$"), "")
                    tvResult.text = "x = $formattedX"
                }
                else -> {
                    val realPart = -b / (2 * a)
                    val imagPart = Math.sqrt(-discriminant) / (2 * a)
                    val formattedReal = String.format("%.10f", realPart).replace(Regex("0+$"), "").replace(Regex("[.]$"), "")
                    val formattedImag = String.format("%.10f", imagPart).replace(Regex("0+$"), "").replace(Regex("[.]$"), "")
                    tvResult.text = "x₁ = $formattedReal + $formattedImag*i\nx₂ = $formattedReal - $formattedImag*i"
                }
            }
        }
    }
}
