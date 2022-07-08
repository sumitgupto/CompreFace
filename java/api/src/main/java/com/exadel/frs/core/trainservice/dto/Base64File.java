package com.exadel.frs.core.trainservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiParam;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

import static com.exadel.frs.core.trainservice.system.global.Constants.*;

@Data
@NoArgsConstructor
public class Base64File {

    @JsonProperty("file")
    @NotNull
    @ApiParam(value = IMAGE_WITH_ONE_FACE_DESC, required = true)
    private String content;

    @JsonProperty("imageUri")
    @ApiParam(value = IMAGE_URI, required = false)
    private String imageUri = "";


    @JsonProperty("businessKey")
    @NotNull
    @ApiParam(value = BUSINESS_KEY, required = true)
    private String businessKey;

    @JsonProperty("flag")
    @NotNull
    @ApiParam(value = ACTION_FLAG, required = true)
    private String flag;

    @JsonProperty("subjectName")
    @ApiParam(value = SUBJECT_NAME, required = false)
    private String subjectName;

}
