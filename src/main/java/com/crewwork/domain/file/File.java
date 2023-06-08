package com.crewwork.domain.file;

import com.crewwork.domain.BaseEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class File extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "file_id")
    private Long id;

    private String originName;
    private String storedName;
    private Long fileSize;

    @Builder
    private File(String originName, String storedName, Long fileSize) {
        this.originName = originName;
        this.storedName = storedName;
        this.fileSize = fileSize;
    }
}
