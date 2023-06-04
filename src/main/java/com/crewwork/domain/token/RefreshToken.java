package com.crewwork.domain.token;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RefreshToken {
    @Id
    private String id;

    private String token;
    private String parentId;

    @Builder
    public RefreshToken(String id, String token, String parentId) {
        this.id = id;
        this.token = token;
        this.parentId = parentId;
    }
}
