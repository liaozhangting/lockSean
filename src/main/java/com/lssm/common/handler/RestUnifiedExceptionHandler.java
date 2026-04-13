package com.lssm.common.handler;


import com.lssm.common.contstant.RS;
import com.lssm.common.exception.ServiceException;
import com.lssm.utils.IPUtils;
import com.lssm.utils.Result;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import java.util.HashMap;
import java.util.Map;

/**
 * 统一异常处理
 */
@RestControllerAdvice
@Slf4j
public class RestUnifiedExceptionHandler {

	/**
	 * 服务异常
	 */
	@ExceptionHandler({ServiceException.class})
	public Result<String> handleServiceException(ServiceException ex) {
		return  Result.error(ex.getStatus(), ex.getMessage()) ;
	}

	/**
	 * 参数校验失败异常
	 */
	@ExceptionHandler({MethodArgumentNotValidException.class})
	public Result<Map<String,String>> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
		BindingResult bindingResult = ex.getBindingResult();
		Map<String, String> errors = new HashMap<>(8);
		for (FieldError fieldError : bindingResult.getFieldErrors()) {
			errors.put(fieldError.getField(), fieldError.getDefaultMessage());
		}
		return Result.error(RS.ILLEGAL_PARAMETER.status(),RS.ILLEGAL_PARAMETER.message(), errors);
	}

	/**
	 * 参数校验失败异常
	 */
	@ExceptionHandler({ConstraintViolationException.class})
	public Result handleConstraintViolationException(ConstraintViolationException ex) {
		log.info("{}：{}", RS.ILLEGAL_PARAMETER.message(), ex.getMessage());
		String[] messages = ex.getMessage().split(":");
		String msg = messages.length > 1 ? messages[1].trim() : "";
		return Result.error(RS.ILLEGAL_PARAMETER.status(), msg);
	}






	// 上传文件过大
	@ExceptionHandler(MaxUploadSizeExceededException.class)
	public Result<String> handleMaxUploadSizeExceededException(MaxUploadSizeExceededException ex) {
		log.info(RS.FILE_SIZE_LIMIT.message(), ex.getCause());
		return Result.error(RS.FILE_SIZE_LIMIT.status(), RS.FILE_SIZE_LIMIT.message());
	}

	// 非法参数
	@ExceptionHandler(NumberFormatException.class)
	public Result<String> handleNumberFormatException(NumberFormatException ex) {
		log.info("{}：{}", RS.ILLEGAL_PARAMETER.message(), ex.getMessage());
		return Result.error(RS.ILLEGAL_PARAMETER.status(), ex.getMessage());
	}

	// 最后处理
	@ExceptionHandler(Exception.class)
	public Result<String> handleException(Exception ex) {
		log.info("系统错误，路径：{}，错误：{}", IPUtils.getRequest().getServletPath(), ex.getMessage());
		ex.printStackTrace();
		return Result.error(RS.SYSTEM_ERROR.status(), RS.SYSTEM_ERROR.message());
	}

}
