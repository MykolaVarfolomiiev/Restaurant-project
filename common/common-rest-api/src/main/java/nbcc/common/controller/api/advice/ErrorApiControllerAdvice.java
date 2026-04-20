package nbcc.common.controller.api.advice;

import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ErrorApiControllerAdvice {

    private final Logger logger = LoggerFactory.getLogger(ErrorApiControllerAdvice.class);

    @ExceptionHandler(AuthorizationDeniedException.class)
    public ResponseEntity<String> exceptionHandler(AuthorizationDeniedException ex, HttpServletRequest request) {
        logger.info("Access Denied on uri {}: on method {} ", request.getRequestURI() , request.getMethod(), ex);
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> exceptionHandler(Exception ex, HttpServletRequest request) {
        logger.error("Unexpected Exception on uri {}: on method {} ", request.getRequestURI() , request.getMethod(), ex);
        return new ResponseEntity<>("An Unexpected Error has occurred",HttpStatus.INTERNAL_SERVER_ERROR);
    }
}