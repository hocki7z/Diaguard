package com.faltenreich.diaguard.util.export;

import android.content.Context;
import android.support.annotation.NonNull;

import com.faltenreich.diaguard.data.entity.Measurement;
import com.faltenreich.diaguard.util.Helper;
import com.faltenreich.diaguard.util.IFileListener;

import org.joda.time.DateTime;

import java.io.File;

/**
 * Created by Faltenreich on 21.10.2015.
 */
public class Export {

    public static final String PDF_MIME_TYPE = "application/pdf";

    public static final String CSV_MIME_TYPE = "text/csv";
    public static final char CSV_DELIMITER = ';';
    public static final String CSV_KEY_META = "meta";

    public static void exportPdf(@NonNull IFileListener listener, @NonNull DateTime dateStart, @NonNull DateTime dateEnd, @NonNull Measurement.Category[] categories) {
        PdfExport pdfExport = new PdfExport(dateStart, dateEnd, categories);
        pdfExport.setListener(listener);
        pdfExport.execute();
    }

    public static void exportCsv(@NonNull IFileListener listener, DateTime dateStart, DateTime dateEnd, Measurement.Category[] categories) {
        CsvExport csvExport = new CsvExport(dateStart, dateEnd, categories);
        csvExport.setListener(listener);
        csvExport.execute();
    }

    public static void importCsv(@NonNull IFileListener listener, @NonNull File file) {
        CsvImport csvImport = new CsvImport(file);
        csvImport.setListener(listener);
        csvImport.execute();
    }

    public static DateTime dateTimeFromCsv(String dateString) {
        return Helper.getDateTimeFormatExport().parseDateTime(dateString);
    }
}
