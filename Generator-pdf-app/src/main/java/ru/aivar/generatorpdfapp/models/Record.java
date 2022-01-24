package ru.aivar.generatorpdfapp.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "generator_record")
public class Record {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime recordTimestamp;

    @ManyToOne
    @JoinColumn(name = "account_id", referencedColumnName = "id")
    private User account;

    @Lob
    private byte[] content;

    private String pdfType;

}
