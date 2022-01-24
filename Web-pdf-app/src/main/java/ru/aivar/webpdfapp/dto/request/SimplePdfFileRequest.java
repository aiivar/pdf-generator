package ru.aivar.webpdfapp.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SimplePdfFileRequest implements Serializable {

    private String firstName;
    private String lastName;

}
