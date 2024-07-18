package ru.rein.restApp.entities;



import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import jakarta.persistence.*;


@Data
@NoArgsConstructor
@Entity
@Table(name = "answer_xml")
public class AnswerXml {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "xml", columnDefinition = "TEXT")
    private String xml;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "person_id", nullable = false)
    @ToString.Exclude
    private Person person;

    public AnswerXml(String xml, Person person) {
        this.xml = xml;
        this.person = person;
    }
}
