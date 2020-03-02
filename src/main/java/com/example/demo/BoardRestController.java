package com.example.demo;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.Param;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.lang.reflect.Field;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@RestController
public class BoardRestController {

    final private BoardRepository boardRepository;
    final private ModelMapper modelMapper;

    @Autowired
    public BoardRestController(BoardRepository boardRepository, ModelMapper modelMapper) {
        this.boardRepository = boardRepository;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/board")
    ResponseEntity board(@Param("id") int id){
        Optional<Board> opBoard = boardRepository.findByDeletedAndId(true,id);
        opBoard.orElseThrow(()-> new BadRequestException("데이터가 없습니다."));
        return new ResponseEntity<>(modelMapper.map(opBoard.get(), BoardDto.class), HttpStatus.OK);
    }

    @GetMapping("/boards")
    ResponseEntity boards(@PageableDefault(size = 3,sort = { "id" }, direction = Sort.Direction.DESC) Pageable pageable){
        //Pageable pageable = PageRequest.of(page,3, Sort.by("id").descending());
        Page<Board> boards = boardRepository.findAll(pageable);
        return new ResponseEntity<>(boards, HttpStatus.OK);
    }


    @PostMapping("/board")
    ResponseEntity createBoard(@Valid @RequestBody Board board, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            StringBuilder sb = new StringBuilder();
            for(FieldError e : bindingResult.getFieldErrors()){
                sb.append("[");
                sb.append(e.getField());
                sb.append("](은)는 ");
                sb.append(e.getDefaultMessage());
                sb.append(" 입력 된 변수는 [");
                sb.append(e.getRejectedValue());
                sb.append("] 입니다.\n");
            }
            throw new BadRequestException(sb.toString());
        }
        return new ResponseEntity<>(boardRepository.save(board), HttpStatus.CREATED);
    }

    @ExceptionHandler(BadRequestException.class)
    ResponseEntity badRequestExceptionHandler(BadRequestException e)
    {
        ErrorResponse errorResponse = new ErrorResponse("잘못된 요청", e.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
}
