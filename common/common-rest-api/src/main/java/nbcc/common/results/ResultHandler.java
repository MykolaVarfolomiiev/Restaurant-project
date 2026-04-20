package nbcc.common.results;

import nbcc.common.result.Result;
import nbcc.common.result.ValidatedResult;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ResultHandler {

    public static <T> ResponseEntity<Result<T>> handleResult(Result<T> result, HttpStatus successStatus) {
        return handleResult(result, successStatus, successStatus);
    }

    public static <T> ResponseEntity<Result<T>> handleResult(Result<T> result, HttpStatus successStatus, HttpStatus emptyStatus) {

        if (result.isError()) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        if (result.isEmpty()) {
            return new ResponseEntity<>(result, emptyStatus);
        }

        if (result.isSuccessful()) {
            return new ResponseEntity<>(result, successStatus);
        }

        return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
    }

    public static <T> ResponseEntity<ValidatedResult<T>> handleResult(ValidatedResult<T> result, HttpStatus successStatus) {

        return handleResult(result, successStatus, successStatus);
    }

    public static <T> ResponseEntity<ValidatedResult<T>> handleResult(ValidatedResult<T> result, HttpStatus successStatus, HttpStatus emptyStatus) {

        if (result.isError()) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        if (result.isInvalid()) {
            return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
        }

        if (result.isEmpty()) {
            return new ResponseEntity<>(result, emptyStatus);
        }

        if (result.isSuccessful()) {
            return new ResponseEntity<>(result, successStatus);
        }

        return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
    }
}
