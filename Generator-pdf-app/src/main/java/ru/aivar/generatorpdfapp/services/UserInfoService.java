package ru.aivar.generatorpdfapp.services;

public interface UserInfoService {
    void saveRecord(String email, byte[] content, String pdfType);
}
