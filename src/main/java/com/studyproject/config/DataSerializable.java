package com.studyproject.config;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter @Setter
@NoArgsConstructor
public class DataSerializable implements Serializable {

    private static final long serialVersionUID=1L;
    private String sourceId;
    private String itemId;

    public DataSerializable(String sourceId, String itemId){
        super();
        this.sourceId=sourceId;
        this.itemId=itemId;
    }
}
