package ru.aivar.webpdfapp.services;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.utils.SerializationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import ru.aivar.webpdfapp.dto.request.ListPdfFileRequest;
import ru.aivar.webpdfapp.dto.request.SimplePdfFileRequest;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PdfGeneratorServiceImpl implements PdfGeneratorService {

    private final static String PDF_SIMPLE_TYPE_QUERY_KEY = "files.pdf.simple";
    private final static String PDF_LIST_TYPE_QUERY_KEY = "files.pdf.list";

    private final RabbitTemplate rabbitTemplate;

    @Autowired
    private TopicExchange topicExchange;

    @Override
    public byte[] generateSimplePdf(SimplePdfFileRequest simplePdfFileRequest) {
        Map<String, Serializable> map = new HashMap<>();
        map.put("firstName", simplePdfFileRequest.getFirstName());
        map.put("lastName", simplePdfFileRequest.getLastName());
        return getBytes(map, PDF_SIMPLE_TYPE_QUERY_KEY);
    }

    @Override
    public byte[] generateListPdf(ListPdfFileRequest listPdfFileRequest) {
        Map<String, Serializable> map = new HashMap<>();
        map.put("list", (Serializable) listPdfFileRequest.getList());
        return getBytes(map, PDF_LIST_TYPE_QUERY_KEY);
    }

    private byte[] getBytes(Map<String, Serializable> map, String pdfListTypeQueryKey) {
        map.put("email", SecurityContextHolder.getContext().getAuthentication().getName());
        byte[] formBytes = SerializationUtils.serialize(map);
        Optional<Message> messageOptional = Optional.ofNullable(rabbitTemplate.sendAndReceive(topicExchange.getName(), pdfListTypeQueryKey, new Message(formBytes)));
        return messageOptional.orElseThrow(IllegalArgumentException::new).getBody();
    }
}
