/*
 * Copyright (c) 2020 the original author or authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing
 * permissions and limitations under the License.
 */

package com.exadel.frs.core.trainservice.controller;

import com.exadel.frs.core.trainservice.dto.Base64File;
import com.exadel.frs.core.trainservice.dto.FacesRecognitionResponseDto;
import com.exadel.frs.core.trainservice.dto.ProcessImageParams;
import com.exadel.frs.core.trainservice.service.FaceProcessService;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.Collections;

import static com.exadel.frs.commonservice.system.global.Constants.DET_PROB_THRESHOLD;
import static com.exadel.frs.core.trainservice.system.global.Constants.*;

import org.json.simple.JSONObject;

@RestController
@RequestMapping(API_V1)
@RequiredArgsConstructor
@Validated
public class RecognizeController {

    private final FaceProcessService recognitionService;
    private static final Logger logger = LogManager.getLogger(RecognizeController.class);

    @PostMapping(value = "/recognition/recognize", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public FacesRecognitionResponseDto recognize(
            @ApiParam(value = API_KEY_DESC, required = true)
            @RequestHeader(X_FRS_API_KEY_HEADER) final String apiKey,
            @ApiParam(value = IMAGE_FILE_DESC, required = true)
            @RequestParam final MultipartFile file,
            @ApiParam(value = LIMIT_DESC)
            @RequestParam(defaultValue = LIMIT_DEFAULT_VALUE, required = false)
            @Min(value = 0, message = LIMIT_MIN_DESC) final Integer limit,
            @ApiParam(value = PREDICTION_COUNT_DESC)
            @RequestParam(defaultValue = PREDICTION_COUNT_DEFAULT_VALUE, name = PREDICTION_COUNT_REQUEST_PARAM, required = false)
            @Min(value = 1, message = PREDICTION_COUNT_MIN_DESC) final Integer predictionCount,
            @ApiParam(value = DET_PROB_THRESHOLD_DESC)
            @RequestParam(value = DET_PROB_THRESHOLD, required = false) final Double detProbThreshold,
            @ApiParam(value = FACE_PLUGINS_DESC)
            @RequestParam(value = FACE_PLUGINS, required = false, defaultValue = "") final String facePlugins,
            @ApiParam(value = STATUS_DESC)
            @RequestParam(value = STATUS, required = false, defaultValue = STATUS_DEFAULT_VALUE) final Boolean status
    ) {
        ProcessImageParams processImageParams = ProcessImageParams
                .builder()
                .apiKey(apiKey)
                .file(file)
                .limit(limit)
                .detProbThreshold(detProbThreshold)
                .facePlugins(facePlugins)
                .status(status)
                .additionalParams(Collections.singletonMap(PREDICTION_COUNT, predictionCount))
                .build();

        return (FacesRecognitionResponseDto) recognitionService.processImage(processImageParams);
    }



    @PostMapping(value = "/recognition/recognize", consumes = MediaType.APPLICATION_JSON_VALUE)
    public FacesRecognitionResponseDto recognizeBase64(
            @ApiParam(value = API_KEY_DESC, required = true) @RequestHeader(X_FRS_API_KEY_HEADER) final String apiKey,
            @ApiParam(value = LIMIT_DESC) @RequestParam(defaultValue = LIMIT_DEFAULT_VALUE, required = false) @Min(value = 0, message = LIMIT_MIN_DESC) final Integer limit,
            @ApiParam(value = DET_PROB_THRESHOLD_DESC) @RequestParam(value = DET_PROB_THRESHOLD, required = false) final Double detProbThreshold,
            @ApiParam(value = FACE_PLUGINS_DESC) @RequestParam(value = FACE_PLUGINS, required = false, defaultValue = "") final String facePlugins,
            @ApiParam(value = STATUS_DESC) @RequestParam(value = STATUS, required = false, defaultValue = STATUS_DEFAULT_VALUE) final Boolean status,
            @ApiParam(value = PREDICTION_COUNT_DESC) @RequestParam(value = PREDICTION_COUNT_REQUEST_PARAM, required = false, defaultValue = PREDICTION_COUNT_DEFAULT_VALUE) @Min(value = 1, message = PREDICTION_COUNT_MIN_DESC) Integer predictionCount,
            @RequestBody @Valid Base64File request) {

        //logger.info("Printing request start");
        //logger.info(request.getContent().toString());
        logger.info("Printing request end at /recognize");

        ProcessImageParams processImageParams = ProcessImageParams
                .builder()
                .apiKey(apiKey)
                .imageBase64(request.getContent())
                .limit(limit)
                .detProbThreshold(detProbThreshold)
                .facePlugins(facePlugins)
                .status(status)
                .additionalParams(Collections.singletonMap(PREDICTION_COUNT, predictionCount))
                .build();

        return (FacesRecognitionResponseDto) recognitionService.processImage(processImageParams);
    }


    @PostMapping(value = "/recognition/facematch", consumes = MediaType.APPLICATION_JSON_VALUE)
    public FacesRecognitionResponseDto facematch(
            @ApiParam(value = API_KEY_DESC, required = true) @RequestHeader(X_FRS_API_KEY_HEADER) final String apiKey,
            @ApiParam(value = LIMIT_DESC) @RequestParam(defaultValue = LIMIT_DEFAULT_VALUE, required = false) @Min(value = 0, message = LIMIT_MIN_DESC) final Integer limit,
            @ApiParam(value = DET_PROB_THRESHOLD_DESC) @RequestParam(value = DET_PROB_THRESHOLD, required = false) final Double detProbThreshold,
            @ApiParam(value = FACE_PLUGINS_DESC) @RequestParam(value = FACE_PLUGINS, required = false, defaultValue = "") final String facePlugins,
            @ApiParam(value = STATUS_DESC) @RequestParam(value = STATUS, required = false, defaultValue = STATUS_DEFAULT_VALUE) final Boolean status,
            @ApiParam(value = PREDICTION_COUNT_DESC) @RequestParam(value = PREDICTION_COUNT_REQUEST_PARAM, required = false, defaultValue = PREDICTION_COUNT_DEFAULT_VALUE) @Min(value = 1, message = PREDICTION_COUNT_MIN_DESC) Integer predictionCount,
            @RequestBody @Valid Base64File request) {

        //logger.info("Printing request start");
        //logger.info(request.getContent().toString());
        logger.info("Printing request end at /facematch");

        logger.info("businessKey : " + request.getBusinessKey());
        logger.info("flag : " + request.getFlag());

        ProcessImageParams processImageParams = ProcessImageParams
                .builder()
                .apiKey(apiKey)
                .imageBase64(request.getContent())
                .limit(limit)
                .detProbThreshold(detProbThreshold)
                .facePlugins(facePlugins)
                .status(status)
                .additionalParams(Collections.singletonMap(PREDICTION_COUNT, predictionCount))
                .build();

        //return (FacesRecognitionResponseDto) recognitionService.processImage(processImageParams);
        FacesRecognitionResponseDto respDto = (FacesRecognitionResponseDto) recognitionService.processImage(processImageParams);
        logger.info("respDto -> " + respDto);

        return respDto;

    }

    @GetMapping(value = "/recognition/recognize/test", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<JSONObject> gettest(
            //@ApiParam(value = API_KEY_DESC, required = true) @RequestHeader(X_FRS_API_KEY_HEADER) final String apiKey,
            //@RequestHeader Map<String, String> headers,
            @Valid @RequestBody final JSONObject request) throws Exception {
        logger.info(">>>>>>>>>>>>>>>>>>>>>Inside RecognizeController gettest()");
        return posttest(request);
    }

    @PostMapping(value = "/recognition/recognize/test", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<JSONObject> posttest(
            //@ApiParam(value = API_KEY_DESC, required = true) @RequestHeader(X_FRS_API_KEY_HEADER) final String apiKey,
            //@RequestHeader Map<String, String> headers,
            @Valid @RequestBody final JSONObject request) throws Exception {



        logger.info(">>>>>>>>>>>>>>>>>>>>>Inside RecognizeController posttest()");
        String resp = request.get("value").toString();
        String imageId = request.get("imageId").toString();
        logger.info("received imageId -> "+imageId);
        JSONObject respJson = new JSONObject();
        respJson.put("response", resp+" >>>>>>>>>> is the request, hence returning back");
        return new ResponseEntity<>(respJson, HttpStatus.OK);
    }

}