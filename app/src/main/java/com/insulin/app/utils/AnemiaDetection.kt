package com.insulin.app.utils

import android.content.Context
import org.tensorflow.lite.DataType
import org.tensorflow.lite.Interpreter
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer
import java.io.FileInputStream
import java.nio.ByteBuffer
import java.nio.channels.FileChannel

class AnemiaDetection(private val context: Context) {

    private lateinit var interpreter: Interpreter

    // Fungsi untuk memuat model TFLite dari folder assets
    fun loadModel(modelPath: String) {
        try {
            val assetFileDescriptor = context.assets.openFd(modelPath)
            val fileInputStream = assetFileDescriptor.createInputStream()
            val fileChannel = fileInputStream.channel
            val startOffset = assetFileDescriptor.startOffset
            val declaredLength = assetFileDescriptor.declaredLength
            interpreter = Interpreter(fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength))
        } catch (e: Exception) {
            throw RuntimeException("Error loading model: $modelPath", e)
        }
    }

    // Fungsi untuk melakukan deteksi anemia
    fun detectAnemia(imageData: ByteBuffer): Float {
        // Input Tensor (shape: 1, 224, 224, 3)
        val inputBuffer = TensorBuffer.createFixedSize(intArrayOf(1, 224, 224, 3), DataType.FLOAT32)
        inputBuffer.loadBuffer(imageData)

        // Output Tensor (shape: 1, 1)
        val outputBuffer = TensorBuffer.createFixedSize(intArrayOf(1, 1), DataType.FLOAT32)

        // Jalankan model untuk inferensi
        interpreter.run(inputBuffer.buffer, outputBuffer.buffer.rewind())

        // Ambil hasil output (skor deteksi anemia)
        return outputBuffer.floatArray[0]
    }
}
