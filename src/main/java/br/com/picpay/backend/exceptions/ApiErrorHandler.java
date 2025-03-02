package br.com.picpay.backend.exceptions;

import br.com.picpay.backend.exceptions.base.BadRequestException;
import br.com.picpay.backend.exceptions.base.CustomException;
import br.com.picpay.backend.exceptions.base.NotAuthorizedException;
import br.com.picpay.backend.exceptions.base.NotFoundException;
import jakarta.validation.ValidationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

// https://github.com/meysamhadeli/booking-microservices-java-spring-boot/blob/876cacfcb3600d00378c6bec6f10dfbdd68197e0/src/buildingblocks/src/main/java/buildingblocks/problemdetails/CustomProblemDetailsHandler.java#L37
@RestControllerAdvice
public class ApiErrorHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ProblemDetail> globalExceptionHandler(
            Exception ex,
            WebRequest request
    )
    {
        record ExceptionDetails(String detail, String title, HttpStatus status) {
        }

        ExceptionDetails details = switch (ex) {

            case NotAuthorizedException authEx -> new ExceptionDetails(
                    authEx.getMessage(),
                    authEx.getClass().getSimpleName(),
                    HttpStatus.UNAUTHORIZED
            );

            case ValidationException validationEx -> new ExceptionDetails(
                    validationEx.getMessage(),
                    validationEx.getClass().getSimpleName(),
                    HttpStatus.BAD_REQUEST
            );

            case BadRequestException badRequestEx -> new ExceptionDetails(
                    badRequestEx.getMessage(),
                    badRequestEx.getClass().getSimpleName(),
                    HttpStatus.BAD_REQUEST
            );

            case NotFoundException notFoundEx -> new ExceptionDetails(
                    notFoundEx.getMessage(),
                    notFoundEx.getClass().getSimpleName(),
                    HttpStatus.NOT_FOUND
            );

            default -> new ExceptionDetails(
                    ex.getMessage(),
                    ex.getClass().getSimpleName(),
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        };

        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                details.status(),
                details.detail()
        );

        return ResponseEntity.status(problemDetail.getStatus()).body(problemDetail);
    }
}
