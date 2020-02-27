package com.example.demo;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
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
        Optional<Board> opBoard = boardRepository.findById(id);
        if(opBoard.isPresent()) {
            return new ResponseEntity<>(modelMapper.map(opBoard.get(), BoardDto.class), HttpStatus.OK);
        }
        else {
            ErrorResponse errorResponse = new ErrorResponse("잘못된 요청","데이터가 없습니다.");
            return new ResponseEntity<>(errorResponse,HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/board")
    ResponseEntity createBoard(@Valid @RequestBody Board board){
        return new ResponseEntity<>(boardRepository.save(board), HttpStatus.OK);
    }
}
