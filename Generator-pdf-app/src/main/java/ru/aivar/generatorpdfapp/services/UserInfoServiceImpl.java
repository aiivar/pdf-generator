package ru.aivar.generatorpdfapp.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.aivar.generatorpdfapp.models.Record;
import ru.aivar.generatorpdfapp.models.User;
import ru.aivar.generatorpdfapp.repositories.RecordRepository;
import ru.aivar.generatorpdfapp.repositories.UserRepository;

import java.time.LocalDateTime;

@Service
public class UserInfoServiceImpl implements UserInfoService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RecordRepository recordRepository;

    @Override
    public void saveRecord(String email, byte[] content, String pdfType) {
        User user;
        user = userRepository.findByEmail(email).orElseGet(() -> userRepository.save(User.builder().email(email).build()));

        Record record = Record.builder()
                .account(user)
                .content(content)
                .pdfType(pdfType)
                .recordTimestamp(LocalDateTime.now())
                .build();

        recordRepository.save(record);
    }
}
