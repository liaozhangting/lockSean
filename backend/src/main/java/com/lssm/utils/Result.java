package com.lssm.utils;


import com.lssm.common.contstant.RS;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Map;

@Data
public class Result<T> implements Serializable {
    private static final long serialVersionUID = -637415113996231595L;

    private String code;//编码为"true"则视为成功，其他则视为失败
    private String msg;//错误信息
    private T data;//数据
    private int status;

    public static <T> Result<T> success(T data)
    {
        Result<T> result = new Result<>();
        result.code = "true";
        result.setData(data);
        return result;
    }
    public static <T> Result<T> success()
    {
        Result<T> result = new Result<>();
        result.code = "true";
        return result;
    }
    public static <T> Result<T> error(String msg)
    {
        Result<T> result = new Result<>();
        result.code = "false";
        result.setMsg(msg);
        return result;
    }
    public static <T> Result<T> error(int status,String msg, T  errors)
    {
        Result<T> result = new Result<>();
        result.code = "false";
        result.data =  errors;
        return result;
    }

    public static <T> Result<T> error(int status, String msg)
    {
        Result<T> result = new Result<>();
        result.code = "false";
        result.msg = msg;
        result.status = status;
        return result;
    }

}
