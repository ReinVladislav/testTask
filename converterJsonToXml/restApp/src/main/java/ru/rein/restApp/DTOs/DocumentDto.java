package ru.rein.restApp.DTOs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.rein.restApp.enums.DocumentType;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DocumentDto {

    private String series;
    private String number;
    private DocumentType type;
    private LocalDate issueDate;

}
