package ru.aivar.webpdfapp.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.utils.SerializationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.aivar.webpdfapp.dto.request.ListPdfFileRequest;
import ru.aivar.webpdfapp.dto.request.SimplePdfFileRequest;
import ru.aivar.webpdfapp.dto.response.SimplePdfFileResponse;
import ru.aivar.webpdfapp.services.PdfGeneratorService;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/pdf")
public class PdfFilesController {

    @Autowired
    private PdfGeneratorService pdfGeneratorService;

    @PostMapping("/simple")
    public ResponseEntity<InputStreamResource> sendSimplePdfFileRequest(@RequestBody SimplePdfFileRequest simplePdfFileRequest) {
        try {
            return getPdfInputStreamResourceResponseEntity(pdfGeneratorService.generateSimplePdf(simplePdfFileRequest));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/list")
    public ResponseEntity<InputStreamResource> sendListPdfFileRequest(@RequestBody ListPdfFileRequest listPdfFileRequest){
        try {
            return getPdfInputStreamResourceResponseEntity(pdfGeneratorService.generateListPdf(listPdfFileRequest));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    private ResponseEntity<InputStreamResource> getPdfInputStreamResourceResponseEntity(byte[] content) throws IOException {
        InputStream inputStream = new ByteArrayInputStream(content);
        InputStreamResource inputStreamResource = new InputStreamResource(inputStream);

        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.setContentLength(inputStream.available());
        responseHeaders.setContentType(MediaType.APPLICATION_PDF);

        return new ResponseEntity<>(inputStreamResource, responseHeaders, HttpStatus.OK);
    }

}
