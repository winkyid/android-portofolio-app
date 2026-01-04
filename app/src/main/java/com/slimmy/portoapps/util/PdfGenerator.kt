package com.slimmy.portoapps.util

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Environment
import androidx.core.app.NotificationCompat
import androidx.core.content.FileProvider
import com.itextpdf.kernel.colors.ColorConstants
import com.itextpdf.kernel.pdf.PdfDocument
import com.itextpdf.kernel.pdf.PdfWriter
import com.itextpdf.kernel.pdf.action.PdfAction
import com.itextpdf.kernel.pdf.canvas.draw.SolidLine
import com.itextpdf.layout.Document
import com.itextpdf.layout.borders.Border
import com.itextpdf.layout.element.Cell
import com.itextpdf.layout.element.LineSeparator
import com.itextpdf.layout.element.Link
import com.itextpdf.layout.element.Paragraph
import com.itextpdf.layout.element.Tab
import com.itextpdf.layout.element.TabStop
import com.itextpdf.layout.element.Table
import com.itextpdf.layout.element.Text
import com.itextpdf.layout.properties.TabAlignment
import com.itextpdf.layout.properties.TextAlignment
import com.itextpdf.layout.properties.UnitValue
import com.slimmy.portoapps.R
import com.slimmy.portoapps.data.DatabaseHelper
import java.io.File
import java.io.FileOutputStream

class PdfGenerator(private val context: Context) {

    private val dbHelper = DatabaseHelper(context)
    private val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    private val channelId = "cv_download_channel"
    private val notificationId = 1001

    init {
        createNotificationChannel()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "CV Download",
                NotificationManager.IMPORTANCE_LOW
            ).apply {
                description = "Notifikasi untuk pengunduhan CV"
            }
            notificationManager.createNotificationChannel(channel)
        }
    }

    fun getAppDownloadFolder(): File {
        val appName = context.getString(context.applicationInfo.labelRes)
        val downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        val appFolder = File(downloadsDir, appName)
        if (!appFolder.exists()) {
            appFolder.mkdirs()
        }
        return appFolder
    }

    private fun addAtsSectionTitle(document: Document, title: String) {
        document.add(Paragraph(title)
            .setFontSize(10f) // Turun dari 12f
            .setBold()
            .setMarginTop(10f)
            .setMarginBottom(0f)
            .setTextAlignment(TextAlignment.LEFT))
        
        val line = LineSeparator(SolidLine(0.5f))
        line.setMarginTop(2f)
        line.setMarginBottom(4f)
        document.add(line)
    }

    fun generateCV(): File? {
        val notificationBuilder = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.ic_download)
            .setContentTitle("Mengunduh CV")
            .setContentText("Menyusun format ATS...")
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setOngoing(true)
            .setProgress(100, 0, true)

        notificationManager.notify(notificationId, notificationBuilder.build())

        return try {
            val appFolder = getAppDownloadFolder()
            val fileName = "CV_ATS_${System.currentTimeMillis()}.pdf"
            val file = File(appFolder, fileName)
            
            val pdfWriter = PdfWriter(FileOutputStream(file))
            val pdfDocument = PdfDocument(pdfWriter)
            val document = Document(pdfDocument)
            document.setMargins(40f, 40f, 40f, 40f)

            val profile = dbHelper.getProfile()
            
            // 1. HEADER
            profile?.let { p ->
                document.add(Paragraph(p.nama.uppercase())
                    .setFontSize(20f) // Turun dari 22f
                    .setBold()
                    .setTextAlignment(TextAlignment.CENTER)
                    .setMarginBottom(2f))
                
                val headerPara = Paragraph().setFontSize(7f).setTextAlignment(TextAlignment.CENTER).setMarginBottom(8f) // Turun dari 9f
                
                headerPara.add(Link(p.email, PdfAction.createURI("mailto:${p.email}")).setFontColor(ColorConstants.BLACK).setUnderline())
                headerPara.add(Text(" | "))
                val cleanPhone = p.phone.replace(Regex("[^0-9]"), "")
                headerPara.add(Link(p.phone, PdfAction.createURI("https://wa.me/$cleanPhone")).setFontColor(ColorConstants.BLACK).setUnderline())
                headerPara.add(Text(" | "))
                headerPara.add(Text(p.address))
                headerPara.add(Text("\n"))
                headerPara.add(Text("LinkedIn: "))
                headerPara.add(Link(p.linkedin, PdfAction.createURI(p.linkedin)).setFontColor(ColorConstants.BLACK))
                headerPara.add(Text(" | "))
                headerPara.add(Text("GitHub: "))
                headerPara.add(Link(p.github, PdfAction.createURI(p.github)).setFontColor(ColorConstants.BLACK))
                headerPara.add(Text(" | "))
                headerPara.add(Text("Website: "))
                headerPara.add(Link(p.website, PdfAction.createURI(p.website)).setFontColor(ColorConstants.BLACK))
                
                document.add(headerPara)

                // 2. DESKRIPSI
                addAtsSectionTitle(document, "DESKRIPSI SINGKAT")
                document.add(Paragraph(p.bio).setFontSize(8f).setTextAlignment(TextAlignment.JUSTIFIED).setMarginBottom(4f)) // Turun dari 10f

                // 3. BIODATA
                addAtsSectionTitle(document, "BIODATA PRIBADI")
                val bioTable = Table(UnitValue.createPointArray(floatArrayOf(130f, 10f, 350f)))
                bioTable.setBorder(Border.NO_BORDER).setMarginBottom(4f)
                val bioData = listOf(
                    "Tempat, Tanggal Lahir" to "${p.tempatLahir}, ${p.tanggalLahir}",
                    "Jenis Kelamin" to p.gender,
                    "Agama" to p.agama,
                    "Kewarganegaraan" to p.warganegara,
                    "Status Pernikahan" to p.status
                )
                bioData.forEach { (l, v) ->
                    bioTable.addCell(Cell().add(Paragraph(l).setFontSize(8f)).setBorder(Border.NO_BORDER).setPadding(0f))
                    bioTable.addCell(Cell().add(Paragraph(":").setFontSize(8f)).setBorder(Border.NO_BORDER).setPadding(0f))
                    bioTable.addCell(Cell().add(Paragraph(v).setFontSize(8f)).setBorder(Border.NO_BORDER).setPadding(0f))
                }
                document.add(bioTable)
            }

            // 4. PENGALAMAN
            val pengalaman = dbHelper.getPengalaman()
            if (pengalaman.isNotEmpty()) {
                addAtsSectionTitle(document, "PENGALAMAN KERJA")
                pengalaman.forEach { exp ->
                    val pTitle = Paragraph().setMarginBottom(0f)
                    pTitle.addTabStops(TabStop(515f, TabAlignment.RIGHT))
                    pTitle.add(Text("${exp.perusahaan} | ${exp.posisi}").setBold().setFontSize(9f)) // Turun dari 11f
                    pTitle.add(Tab())
                    pTitle.add(Text("(${exp.tahunMulai} - ${exp.tahunSelesai})").setFontSize(8f)) // Turun dari 10f
                    document.add(pTitle)
                    document.add(Paragraph(exp.deskripsi).setFontSize(8f).setItalic().setMarginBottom(2f))
                    exp.tasks.forEach { task ->
                        document.add(Paragraph("• $task").setFontSize(7f).setMarginLeft(10f).setMarginBottom(0f)) // Turun dari 9f
                    }
                    document.add(Paragraph("").setMarginBottom(6f))
                }
            }

            // 5. PENDIDIKAN
            val pendidikan = dbHelper.getPendidikan()
            if (pendidikan.isNotEmpty()) {
                addAtsSectionTitle(document, "RIWAYAT PENDIDIKAN")
                pendidikan.forEach { edu ->
                    val pEdu = Paragraph().setMarginBottom(0f)
                    pEdu.addTabStops(TabStop(515f, TabAlignment.RIGHT))
                    pEdu.add(Text("${edu.institusi} | ${edu.levelName}").setBold().setFontSize(9f))
                    pEdu.add(Tab())
                    pEdu.add(Text("(${edu.tahunMulai} - ${edu.tahunSelesai})").setFontSize(8f))
                    document.add(pEdu)
                    document.add(Paragraph(edu.jurusan).setFontSize(8f).setMarginBottom(2f))
                }
            }

            // 6. KEAHLIAN
            val skills = dbHelper.getSkills()
            if (skills.isNotEmpty()) {
                addAtsSectionTitle(document, "KEAHLIAN & KOMPETENSI")
                val groupedSkills = skills.groupBy { it.kategori }
                groupedSkills.forEach { (kategori, list) ->
                    val skillNames = list.joinToString(", ") { it.nama }
                    val p = Paragraph().setFontSize(8f).setMarginBottom(0f).setMultipliedLeading(1.0f) // Turun dari 10f
                    p.add(Text("$kategori: ").setBold())
                    p.add(Text(skillNames))
                    document.add(p)
                }
            }

            document.close()
            notificationBuilder.setContentTitle("Unduhan Selesai").setProgress(0, 0, false).setOngoing(false)
            notificationManager.notify(notificationId, notificationBuilder.build())
            file
        } catch (e: Exception) {
            notificationManager.cancel(notificationId)
            null
        }
    }

    fun shareCV(file: File) {
        val uri = FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", file)
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "application/pdf"
            putExtra(Intent.EXTRA_STREAM, uri)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
        context.startActivity(Intent.createChooser(intent, "Share CV"))
    }
}
