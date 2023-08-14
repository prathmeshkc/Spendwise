package com.pcandroiddev.expensemanager.utils.pdf

import android.os.Environment
import android.util.Log
import com.itextpdf.text.*
import com.itextpdf.text.pdf.PdfPCell
import com.itextpdf.text.pdf.PdfPTable
import com.itextpdf.text.pdf.PdfWriter
import com.pcandroiddev.expensemanager.data.remote.TransactionResponse
import java.io.File
import java.io.FileOutputStream
import java.lang.Exception
import java.time.LocalDate
import java.time.format.DateTimeFormatter


class PDFService {

    private val TITLE_FONT = Font(Font.FontFamily.TIMES_ROMAN, 16f, Font.BOLD)
    private val HEADER_FONT = Font(Font.FontFamily.TIMES_ROMAN, 12f, Font.BOLD)
    private val TEXT_BODY_FONT = Font(Font.FontFamily.TIMES_ROMAN, 12f, Font.NORMAL)

    private lateinit var pdf: PdfWriter

    private fun generatePDFFileName(startDate: String, endDate: String): String {
        val dateFormat = DateTimeFormatter.ofPattern("yyyyMMdd")
        val startDateFormatted =
            LocalDate.parse(startDate, DateTimeFormatter.ofPattern("MMM dd, yyyy"))
        val endDateFormatted = LocalDate.parse(endDate, DateTimeFormatter.ofPattern("MMM dd, yyyy"))

        return "${dateFormat.format(startDateFormatted)}_${dateFormat.format(endDateFormatted)}_${System.currentTimeMillis()}.pdf"
    }

    private fun createFile(startDate: String, endDate: String): File {
        //Prepare file
        val fileName = generatePDFFileName(startDate, endDate)
        val path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        val file = File(path, fileName)
        if (!file.exists()) file.createNewFile()
        return file
    }

    private fun createDocument(): Document {
        //Create Document
        val document = Document()
        document.setMargins(24f, 24f, 32f, 32f)
        document.pageSize = PageSize.A4
        return document
    }

    private fun setupPdfWriter(document: Document, file: File) {
        pdf = PdfWriter.getInstance(document, FileOutputStream(file))
        pdf.setFullCompression()
        //Open the document
        document.open()
    }

    private fun createTable(column: Int, columnWidth: FloatArray): PdfPTable {
        val table = PdfPTable(column)
        table.widthPercentage = 100f
        table.setWidths(columnWidth)
        table.headerRows = 1
        table.defaultCell.verticalAlignment = Element.ALIGN_CENTER
        table.defaultCell.horizontalAlignment = Element.ALIGN_CENTER
        return table
    }

    private fun createCell(content: String, font: Font): PdfPCell {
        val cell = PdfPCell(Phrase(content, font))
        cell.horizontalAlignment = Element.ALIGN_CENTER
        cell.verticalAlignment = Element.ALIGN_MIDDLE
        //setup padding
        cell.setPadding(8f)
        cell.isUseAscender = true
        cell.paddingLeft = 4f
        cell.paddingRight = 4f
        cell.paddingTop = 8f
        cell.paddingBottom = 8f
        return cell
    }

    private fun addLineSpace(document: Document, number: Int) {
        for (i in 0 until number) {
            document.add(Paragraph(" "))
        }
    }

    private fun createParagraph(content: String, font: Font, alignment: Int): Paragraph {
        val paragraph = Paragraph(content, font)
        paragraph.firstLineIndent = 25f
        paragraph.alignment = alignment
        return paragraph
    }

    fun generatePDF(
        transactionList: List<TransactionResponse>,
        startDate: String,
        endDate: String,
        incomeAmt: String,
        expenseAmt: String,
        onError: (Exception) -> Unit,
        onSuccess: (File) -> Unit
    ) {
        //Define the document
        val file = createFile(startDate, endDate)
        val document = createDocument()

        //Setup PDF Writer
        setupPdfWriter(document, file)

        //Add Title
        document.add(
            createParagraph(
                "Statement for $startDate to $endDate",
                TITLE_FONT,
                Element.ALIGN_CENTER
            )
        )

        //Add Empty Line as necessary
        addLineSpace(document, 8)


        //Define Table
        val txnDateWidth = 1f
        val descriptionWidth = 1f
        val noteWidth = 1f
        val expenseWidth = 1f
        val incomeWidth = 1f

        val columnWidth = floatArrayOf(
            txnDateWidth,
            descriptionWidth,
            noteWidth,
            expenseWidth,
            incomeWidth
        )

        val table = createTable(5, columnWidth)
        //Table header (first row)
        val tableHeaderContent = listOf("Txn Date", "Description", "Note", "Expense", "Income")

        //write table header into table
        tableHeaderContent.forEach {
            //define a cell
            val cell = createCell(it, HEADER_FONT)
            //add our cell into our table
            table.addCell(cell)
        }

        transactionList.forEach { transactionResponse: TransactionResponse ->
            val txnDate = createCell(transactionResponse.transactionDate, TEXT_BODY_FONT)
            val description = createCell(transactionResponse.title, TEXT_BODY_FONT)
            val note = createCell(transactionResponse.note, TEXT_BODY_FONT)

            val transactionType = transactionResponse.transactionType
            val amount = transactionResponse.amount.toString()

            val expense: PdfPCell?
            val income: PdfPCell?

            if (transactionType.equals("EXPENSE")) {
                expense = createCell(amount, TEXT_BODY_FONT)
                income = createCell("\t", TEXT_BODY_FONT)
            } else if (transactionType.equals("INCOME")) {
                income = createCell(amount, TEXT_BODY_FONT)
                expense = createCell("\t", TEXT_BODY_FONT)
            } else {
                expense = createCell("\t", TEXT_BODY_FONT)
                income = createCell("\t", TEXT_BODY_FONT)
            }

            table.addCell(txnDate)
            table.addCell(description)
            table.addCell(note)
            table.addCell(expense)
            table.addCell(income)

        }

        document.add(table)
        document.close()

        try {
            pdf.close()
        } catch (ex: Exception) {
            Log.d(TAG, "generatePDF: ${ex.message}")
            onError(ex)
        } finally {
            onSuccess(file)
            Log.d(TAG, "generatePDF: PDF generated successfully!")
        }
    }


    companion object {
        const val TAG = "PDFService"
    }


}