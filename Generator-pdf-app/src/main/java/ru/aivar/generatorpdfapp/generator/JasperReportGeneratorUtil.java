package ru.aivar.generatorpdfapp.generator;

import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimplePdfExporterConfiguration;
import net.sf.jasperreports.export.SimplePdfReportConfiguration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class JasperReportGeneratorUtil {

    @Value("classpath:reports/simplePdf.jrxml")
    public Resource jasperSimplePdfReport;

    @Value("classpath:reports/listPdf.jrxml")
    public Resource jasperListPdfReport;

    public byte[] generateSimplePdfReport(Map<String, Object> fileParams) throws IOException, JRException {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("firstName", fileParams.get("firstName"));
        parameters.put("lastName", fileParams.get("lastName"));

        JasperReport jasperReport = prepareJasperReport(jasperSimplePdfReport);
        JasperPrint jasperPrint = JasperFillManager.fillReport(
                jasperReport,
                parameters,
                new JREmptyDataSource()
        );

        return exportPdfReport(jasperPrint);
    }

    public byte[] generateListPdfReport(Map<String, Object> fileParams) throws JRException, IOException {
        List<String> names = (List<String>) fileParams.get("list");
        Map<String, Object> params = new HashMap<>();
        params.put("names", names);

        JasperReport jasperReport = prepareJasperReport(jasperListPdfReport);
        JasperPrint jasperPrint = JasperFillManager.fillReport(
                jasperReport,
                params,
                new JREmptyDataSource()
        );
        return exportPdfReport(jasperPrint);
    }

    private byte[] exportPdfReport(JasperPrint jasperPrint) throws JRException {
        JRPdfExporter exporter = new JRPdfExporter();
        exporter.setExporterInput(new SimpleExporterInput(jasperPrint)
        );
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(bos));

        SimplePdfReportConfiguration reportConfiguration = new SimplePdfReportConfiguration();
        reportConfiguration.setSizePageToContent(true);
        reportConfiguration.setForceLineBreakPolicy(false);

        SimplePdfExporterConfiguration exporterConfiguration = new SimplePdfExporterConfiguration();
        exporterConfiguration.setMetadataAuthor("AIVAR");
        exporterConfiguration.setEncrypted(true);
        exporterConfiguration.setAllowedPermissionsHint("PRINTING");

        exporter.setConfiguration(reportConfiguration);
        exporter.setConfiguration(exporterConfiguration);

        exporter.exportReport();

        return bos.toByteArray();
    }

    private JasperReport prepareJasperReport(Resource jasperReportResource) throws IOException, JRException {
        InputStream jasperInputStream = jasperReportResource.getInputStream();
        JasperDesign jasperDesign = JRXmlLoader.load(jasperInputStream);
        return JasperCompileManager.compileReport(jasperDesign);
    }

}
