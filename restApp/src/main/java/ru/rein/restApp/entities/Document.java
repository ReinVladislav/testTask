package ru.rein.restApp.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import ru.rein.restApp.DTOs.DocumentDto;
import ru.rein.restApp.enums.DocumentType;

import java.time.LocalDate;


@Entity
@Table(name = "document")
@Data
@NoArgsConstructor
public class Document {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "series")
    private String series;
    @Column(name = "number")
    private String number;
    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private DocumentType type;
    @Column(name = "issueDate")
    private LocalDate issueDate;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "person_id", nullable = false)
    @ToString.Exclude
    private Person person;

    Document(DocumentDto documentDto){
        series = documentDto.getSeries();
        number = documentDto.getNumber();
        type = documentDto.getType();
        issueDate = documentDto.getIssueDate();
    }
}
