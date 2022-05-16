package com.challenge.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.time.LocalDateTime;

import static javax.persistence.GenerationType.IDENTITY;

@Data
@Table
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Message {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "message_id")
    private Long messageId;

    @OneToOne(optional = false)
    @JoinColumn(name = "contact_id", referencedColumnName = "contact_id")
    private Contact contact;

    private String body;

    @Column(name = "date_send")
    private LocalDateTime dateSend;
}
