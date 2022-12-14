package com.github.kudr9tov.epaper.controllers;

import com.github.kudr9tov.epaper.controllers.payload.responses.ApiError;
import com.github.kudr9tov.epaper.controllers.payload.responses.Error;
import com.github.kudr9tov.epaper.controllers.payload.responses.ErrorCode;
import com.github.kudr9tov.epaper.exceptions.GeneralException;
import com.github.kudr9tov.epaper.exceptions.IllegalXmlFileStructureException;
import com.github.kudr9tov.epaper.exceptions.NewspaperNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.FileNotFoundException;
import java.util.IllegalFormatException;
import java.util.UUID;

@ControllerAdvice
public class GeneralAdviceController {
    private static final Logger LOGGER = LoggerFactory.getLogger(GeneralAdviceController.class);

    @ResponseBody
    @ExceptionHandler(GeneralException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiError generalExceptionHandler(Exception ex) {
        LOGGER.error(ex.getMessage(), ex);
        Error error = Error.builder()
                .code(ErrorCode.INTERNAL_ERROR)
                .message(ex.getLocalizedMessage())
                .build();

        return ApiError.builder()
                .requestId(UUID.randomUUID().toString())
                .error(error)
                .build();
    }

    @ResponseBody
    @ExceptionHandler(IllegalXmlFileStructureException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError illegalXmlFileStructureHandler(Exception ex) {
        LOGGER.error(ex.getMessage(), ex);
        Error error = Error.builder()
                .code(ErrorCode.FILE_NOT_VALID)
                .message(ex.getLocalizedMessage())
                .build();

        return ApiError.builder()
                .requestId(UUID.randomUUID().toString())
                .error(error)
                .build();
    }

    @ResponseBody
    @ExceptionHandler(FileNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiError fileNotFoundExceptionHandler(Exception ex) {
        LOGGER.error(ex.getMessage(), ex);
        Error error = Error.builder()
                .code(ErrorCode.FILE_NOT_FOUND)
                .message(ex.getLocalizedMessage())
                .build();

        return ApiError.builder()
                .requestId(UUID.randomUUID().toString())
                .error(error)
                .build();
    }

    @ResponseBody
    @ExceptionHandler(NewspaperNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiError newspaperNotFoundExceptionHandler(Exception ex) {
        LOGGER.error(ex.getMessage(), ex);
        Error error = Error.builder()
                .code(ErrorCode.NEWS_NOT_FOUND)
                .message(ex.getLocalizedMessage())
                .build();

        return ApiError.builder()
                .requestId(UUID.randomUUID().toString())
                .error(error)
                .build();
    }

    @ResponseBody
    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiError illegalArgumentExceptionHandler(Exception ex) {
        LOGGER.error(ex.getMessage(), ex);
        Error error = Error.builder()
                .code(ErrorCode.INTERNAL_ERROR)
                .message(ex.getLocalizedMessage())
                .build();

        return ApiError.builder()
                .requestId(UUID.randomUUID().toString())
                .error(error)
                .build();
    }

    @ResponseBody
    @ExceptionHandler(IllegalFormatException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError illegalFormatExceptionHandler(Exception ex) {
        LOGGER.error(ex.getMessage(), ex);
        Error error = Error.builder()
                .code(ErrorCode.BAD_REQUEST)
                .message(ex.getLocalizedMessage())
                .build();

        return ApiError.builder()
                .requestId(UUID.randomUUID().toString())
                .error(error)
                .build();
    }

    @ResponseBody
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiError globalExceptionHandler(Exception ex) {
        LOGGER.error(ex.getMessage(), ex);
        Error error = Error.builder()
                .code(ErrorCode.INTERNAL_ERROR)
                .message(ex.getLocalizedMessage())
                .build();

        return ApiError.builder()
                .requestId(UUID.randomUUID().toString())
                .error(error)
                .build();
    }
}
