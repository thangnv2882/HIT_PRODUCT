package com.example.product.base;

import com.example.product.dtos.ResponseDTO;
import com.example.product.utils.ResponseMessageEnum;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

public class BaseController<T> {
    public ResponseEntity<?> resSuccess(T data) {
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseDTO<T>(HttpStatus.OK.value(), ResponseMessageEnum.SUCCESS, data)
        );
    }
    public ResponseEntity<?> resListSuccess(List<T> list) {
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseDTO<>(HttpStatus.OK.value(), ResponseMessageEnum.SUCCESS, list)
        );
    }

}
