package com.DevMatrix.StationManagementService.domain.entity;

import java.util.Objects;

import jakarta.persistence.Embeddable;

@Embeddable
public class PostalCode {
    private String code;

      public PostalCode() {
        // Required by JPA
    }
    public PostalCode(String string){
         this.code = string;
    }

    public String getValue() {
        return code;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PostalCode)) return false;
        PostalCode that = (PostalCode) o;
        return Objects.equals(code, that.code);
    }

    @Override
    public int hashCode() {
        return Objects.hash(code);
    }
}
