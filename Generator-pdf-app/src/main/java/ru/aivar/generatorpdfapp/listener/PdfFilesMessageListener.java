package ru.aivar.generatorpdfapp.listener;

import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.utils.SerializationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.aivar.generatorpdfapp.generator.JasperReportGeneratorUtil;
import ru.aivar.generatorpdfapp.services.UserInfoService;

import java.util.Map;

@Component
public class PdfFilesMessageListener {

    private final static String PDF_TYPE_SIMPLE = "simple";
    private final static String PDF_TYPE_LIST = "list";

    @Autowired
    private JasperReportGeneratorUtil jasperReportGeneratorUtil;

    @Autowired
    private UserInfoService userInfoService;

    @RabbitListener(queues = "#{pdfSimpleQueue.name}", containerFactory = "containerFactory")
    public Message onSimplePdfMessage(Message message){
        try {
            Map<String, Object> fileParams = (Map<String, Object>) SerializationUtils.deserialize(message.getBody());
            byte[] content = jasperReportGeneratorUtil.generateSimplePdfReport(fileParams);
            userInfoService.saveRecord((String) fileParams.get("email"), content, PDF_TYPE_SIMPLE);
            return new Message(content);
        } catch (Exception e){
            throw new AmqpRejectAndDontRequeueException(e);
        }
    }

    @RabbitListener(queues = "#{pdfListQueue.name}", containerFactory = "containerFactory")
    public Message onListPdfMessage(Message message){
        try {
            Map<String, Object> fileParams = (Map<String, Object>) SerializationUtils.deserialize(message.getBody());
            byte[] content = jasperReportGeneratorUtil.generateListPdfReport(fileParams);
            userInfoService.saveRecord((String) fileParams.get("email"), content, PDF_TYPE_LIST);
            return new Message(content);
        } catch (Exception e){
            throw new AmqpRejectAndDontRequeueException(e);
        }
    }

}
