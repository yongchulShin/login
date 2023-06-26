package com.medicalip.login.domains.commons.advice;

import com.medicalip.login.domains.commons.advice.exception.*;
import com.medicalip.login.domains.commons.response.CommonResult;
import com.medicalip.login.domains.commons.service.ResponseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.servlet.http.HttpServletRequest;
import java.nio.file.AccessDeniedException;
import java.security.NoSuchAlgorithmException;

@Slf4j
@RequiredArgsConstructor
@RestControllerAdvice
public class ExceptionAdvice {
private final ResponseService responseService;
	
	private final MessageSource messageSource;
	
	@ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    protected CommonResult defaultException(HttpServletRequest request, Exception e) {
        // 예외 처리의 메시지를 MessageSource에서 가져오도록 수정
        return responseService.getFailResult(Integer.valueOf(getMessage("unKnown.code")), getMessage("unKnown.msg"));
    }
    @ExceptionHandler(CommonException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected CommonResult commonException(HttpServletRequest request, CommonException e) {
        // 예외 처리의 메시지를 MessageSource에서 가져오도록 수정
        return responseService.getFailResult(Integer.valueOf(getMessage("commonError.code")), getMessage("commonError.msg"));
    }
    @ExceptionHandler(UnauthorizedException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    protected CommonResult unauthorizedException(HttpServletRequest request, UnauthorizedException e) {
        // 예외 처리의 메시지를 MessageSource에서 가져오도록 수정
        return responseService.getFailResult(Integer.valueOf(getMessage("unauthorizedException.code")), getMessage("unauthorizedException.msg"));
    }
    @ExceptionHandler(CUserNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
    protected CommonResult userNotFoundException(HttpServletRequest request, CUserNotFoundException e) {
        // 예외 처리의 메시지를 MessageSource에서 가져오도록 수정
        return responseService.getFailResult(Integer.valueOf(getMessage("userNotFound.code")), getMessage("userNotFound.msg"));
    }
    @ExceptionHandler(UnauthorizedEmailException.class)
    @ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
    protected CommonResult unauthorizedEmailException(HttpServletRequest request, UnauthorizedEmailException e) {
        // 예외 처리의 메시지를 MessageSource에서 가져오도록 수정
        return responseService.getFailResult(Integer.valueOf(getMessage("unauthorizedEmail.code")), getMessage("unauthorizedEmail.msg"));
    }
    @ExceptionHandler(WithdrawException.class)
    @ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
    protected CommonResult withdrawException(HttpServletRequest request, WithdrawException e) {
        // 예외 처리의 메시지를 MessageSource에서 가져오도록 수정
        return responseService.getFailResult(Integer.valueOf(getMessage("withdrewUser.code")), getMessage("withdrewUser.msg"));
    }
    @ExceptionHandler(LoginNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
    protected CommonResult loginNotFoundException(HttpServletRequest request, LoginNotFoundException e) {
        // 예외 처리의 메시지를 MessageSource에서 가져오도록 수정
        return responseService.getFailResult(Integer.valueOf(getMessage("incorrectPW.code")), getMessage("incorrectPW.msg"));
    }
    @ExceptionHandler(ExistingUserException.class)
    @ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
    protected CommonResult existingUserException(HttpServletRequest request, ExistingUserException e) {
        // 예외 처리의 메시지를 MessageSource에서 가져오도록 수정
        return responseService.getFailResult(Integer.valueOf(getMessage("ExistingUserError.code")), getMessage("ExistingUserError.msg"));
    }
    @ExceptionHandler(RequestParameterException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected CommonResult requestParameterException(HttpServletRequest request, RequestParameterException e) {
        // 예외 처리의 메시지를 MessageSource에서 가져오도록 수정
        return responseService.getFailResult(Integer.valueOf(getMessage("requestParameterError.code")), getMessage("requestParameterError.msg"));
    }
    
    @ExceptionHandler(DuplicateValueInputException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	protected CommonResult duplicateValueInputException(HttpServletRequest request, DuplicateValueInputException e) {
	    // 예외 처리의 메시지를 MessageSource에서 가져오도록 수정
	    return responseService.getFailResult(Integer.valueOf(getMessage("duplicateValueInput.code")), getMessage("duplicateValueInput.msg"));
	}

    @ExceptionHandler(InvalidInputValueException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	protected CommonResult invalidInputValueException(HttpServletRequest request, InvalidInputValueException e) {
	    // 예외 처리의 메시지를 MessageSource에서 가져오도록 수정
	    return responseService.getFailResult(Integer.valueOf(getMessage("invalidInputValue.code")), getMessage("invalidInputValue.msg"));
	}
	@ExceptionHandler(UserExistsException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	protected CommonResult userExistsException(HttpServletRequest request, UserExistsException e) {
	    // 예외 처리의 메시지를 MessageSource에서 가져오도록 수정
	    return responseService.getFailResult(Integer.valueOf(getMessage("userExistsError.code")), getMessage("userExistsError.msg"));
	}
	
	@ExceptionHandler(NoSuchAlgorithmException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	protected CommonResult noSuchAlgorithmException(HttpServletRequest request, NoSuchAlgorithmException e) {
	    // 예외 처리의 메시지를 MessageSource에서 가져오도록 수정
	    return responseService.getFailResult(Integer.valueOf(getMessage("invalidInputValue.code")), getMessage("invalidInputValue.msg"));
	}
	@ExceptionHandler(HttpMessageNotReadableException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	protected CommonResult httpMessageNotReadableException(HttpServletRequest request, HttpMessageNotReadableException e) {
	    // 예외 처리의 메시지를 MessageSource에서 가져오도록 수정
	    return responseService.getFailResult(Integer.valueOf(getMessage("httpMessageNotReadable.code")), getMessage("httpMessageNotReadable.msg"));
	}
    @ExceptionHandler(FileStorageException.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	protected CommonResult fileStorageException(HttpServletRequest request, HttpMessageNotReadableException e) {
	    // 예외 처리의 메시지를 MessageSource에서 가져오도록 수정
	    return responseService.getFailResult(Integer.valueOf(getMessage("fileStorageError.code")), getMessage("fileStorageError.msg"));
	}

    /**
     * javax.validation.Valid or @Validated 으로 binding error 발생시 발생한다.
     * HttpMessageConverter 에서 등록한 HttpMessageConverter binding 못할경우 발생
     * 주로 @RequestBody, @RequestPart 어노테이션에서 발생
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected CommonResult handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.error("handleMethodArgumentNotValidException", e);
        return invalidInputTypeException();
    }
    /**
     * @ModelAttribut 으로 binding error 발생시 BindException 발생한다.
     * ref https://docs.spring.io/spring/docs/current/spring-framework-reference/web.html#mvc-ann-modelattrib-method-args
     */
    @ExceptionHandler(BindException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected CommonResult handleBindException(BindException e) {
        log.error("handleBindException", e);
        return invalidInputTypeException();
    }

    /**
     * enum type 일치하지 않아 binding 못할 경우 발생
     * 주로 @RequestParam enum으로 binding 못했을 경우 발생
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected CommonResult handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e) {
        log.error("handleMethodArgumentTypeMismatchException", e);
        return invalidInputTypeException();
    }

    /**
     * 지원하지 않은 HTTP method 호출 할 경우 발생
     */
	@ExceptionHandler(HttpRequestMethodNotSupportedException.class)
	@ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
	protected CommonResult handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
	    log.error("handleHttpRequestMethodNotSupportedException", e);
	    return invalidInputTypeException();
	}

    /**
     * Authentication 객체가 필요한 권한을 보유하지 않은 경우 발생합
     */
	@ExceptionHandler(AccessDeniedException.class)
	@ResponseStatus(HttpStatus.UNAUTHORIZED)
	protected CommonResult handleAccessDeniedException(AccessDeniedException e) {
	    log.error("handleAccessDeniedException", e);
	    return invalidInputTypeException();
	}

    private CommonResult invalidInputTypeException()
	{
		return responseService.getFailResult(Integer.valueOf(getMessage("invalidInputType.code")), getMessage("invalidInputType.msg"));
	}
    // code정보에 해당하는 메시지를 조회합니다.
    private String getMessage(String code) {
        return getMessage(code, null);
    }
    // code정보, 추가 argument로 현재 locale에 맞는 메시지를 조회합니다.
    private String getMessage(String code, Object[] args) {
        return messageSource.getMessage(code, args, LocaleContextHolder.getLocale());
    }
}
