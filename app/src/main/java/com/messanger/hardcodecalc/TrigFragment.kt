package com.messanger.hardcodecalc

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment

class TrigFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_trig, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val input = view.findViewById<EditText>(R.id.trigInput)
        val result1 = view.findViewById<TextView>(R.id.trigResult)
        val result2 = view.findViewById<TextView>(R.id.trigResult2)

        val btnTan = view.findViewById<Button>(R.id.btnTan)
        val btnCot = view.findViewById<Button>(R.id.btnCot)
        val btnSin = view.findViewById<Button>(R.id.btnSin)
        val btnCos = view.findViewById<Button>(R.id.btnCos)

        btnTan.setOnClickListener {
            val angle = input.text.toString().toDoubleOrNull()
            if (angle != null) {
                val rad = Math.toRadians(angle)
                val tanValue = Math.tan(rad)
                val formatted = String.format("%.5f", tanValue).replace(Regex("0+$"), "").replace(Regex("[.]$"), "")
                result1.text = "tan($angle) = $formatted"
            } else {
                result1.text = "Введите число"
            }
        }

        btnCot.setOnClickListener {
            val angle = input.text.toString().toDoubleOrNull()
            if (angle != null) {
                val rad = Math.toRadians(angle)
                val cosValue = Math.cos(rad)
                val sinValue = Math.sin(rad)
                if (Math.abs(sinValue) < 1e-10) {
                    result1.text = "ctg($angle) = не определено"
                } else {
                    val cotValue = cosValue / sinValue
                    val formatted = String.format("%.5f", cotValue).replace(Regex("0+$"), "").replace(Regex("[.]$"), "")
                    result1.text = "ctg($angle) = $formatted"
                }
            } else {
                result1.text = "Введите число"
            }
        }

        btnSin.setOnClickListener {
            val angle = input.text.toString().toDoubleOrNull()
            if (angle != null) {
                val rad = Math.toRadians(angle)
                val sinValue = Math.sin(rad)
                val formatted = String.format("%.10f", sinValue).replace(Regex("0+$"), "").replace(Regex("[.]$"), "")
                result2.text = "sin($angle) = $formatted"
            } else {
                result2.text = "Введите число"
            }
        }

        btnCos.setOnClickListener {
            val angle = input.text.toString().toDoubleOrNull()
            if (angle != null) {
                val rad = Math.toRadians(angle)
                val cosValue = Math.cos(rad)
                val formatted = String.format("%.10f", cosValue).replace(Regex("0+$"), "").replace(Regex("[.]$"), "")
                result2.text = "cos($angle) = $formatted"
            } else {
                result2.text = "Введите число"
            }
        }
    }
}
