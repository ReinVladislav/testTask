package ru.rein.restApp.DTOs;

import lombok.Data;
import ru.rein.restApp.enums.DocumentType;

import java.time.LocalDate;

@Data
public class DocumentDto {

    private String series;
    private String number;
    private DocumentType type;
    private LocalDate issueDate;

}
