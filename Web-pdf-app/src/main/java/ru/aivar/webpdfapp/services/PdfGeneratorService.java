package ru.aivar.webpdfapp.services;

import ru.aivar.webpdfapp.dto.request.ListPdfFileRequest;
import ru.aivar.webpdfapp.dto.request.SimplePdfFileRequest;

public interface PdfGeneratorService {

    byte[] generateSimplePdf(SimplePdfFileRequest simplePdfFileRequest);
    byte[] generateListPdf(ListPdfFileRequest listPdfFileRequest);

}
