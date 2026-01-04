package com.slimmy.portoapps.ui.filemanager

import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.pdf.PdfRenderer
import android.os.Bundle
import android.os.ParcelFileDescriptor
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.slimmy.portoapps.databinding.FragmentPdfViewerBinding
import java.io.File

class PdfViewerFragment : Fragment() {

    private var _binding: FragmentPdfViewerBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPdfViewerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val filePath = arguments?.getString("filePath") ?: return
        val file = File(filePath)
        
        binding.tvPdfName.text = file.name
        binding.btnBack.setOnClickListener {
            findNavController().navigateUp()
        }

        renderPdf(file)
    }

    private fun renderPdf(file: File) {
        try {
            val fileDescriptor = ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_ONLY)
            val renderer = PdfRenderer(fileDescriptor)
            
            for (i in 0 until renderer.pageCount) {
                val page = renderer.openPage(i)
                
                // Menggunakan resolusi tinggi dan background putih agar kertas terlihat terang
                val bitmap = Bitmap.createBitmap(page.width * 2, page.height * 2, Bitmap.Config.ARGB_8888)
                bitmap.eraseColor(Color.WHITE) // Pastikan background putih sebelum render
                
                page.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY)
                
                val imageView = ImageView(requireContext()).apply {
                    layoutParams = ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                    )
                    adjustViewBounds = true
                    setPadding(0, 0, 0, 20)
                    setImageBitmap(bitmap)
                    setBackgroundColor(Color.WHITE) // Tambahan visual
                }
                
                binding.pdfContainer.addView(imageView)
                page.close()
            }
            renderer.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}