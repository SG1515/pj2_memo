package com.sparta.memo.controller;

import com.sparta.memo.dto.MemoRequestDto;
import com.sparta.memo.dto.MemoResponseDto;
import com.sparta.memo.entity.Memo;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class MemoController {

    // DB 대신 사용 Long -> Memo id , Memo 객체를 넣는 것은 데이터
    private final Map<Long, Memo> memoList = new HashMap<>();

    @PostMapping("/memos")
    public MemoResponseDto createMemo(@RequestBody MemoRequestDto requestDto) {
        // RequestDto -> Entity RequestDto 데이터베이스 저장
        // 기본생성자 밖에 없어서 만들어주기
        // alt + enter -> requestDto 생성자 생성 Memo
        // 클라이언트에서 받아온 requestDto 객체를 사용하여서 Memo변수들에 넣기
        Memo memo = new Memo(requestDto);

        // Memo Max ID Check
        // id값으로 Memo를 구분한다 중복 X
        // Db에서 가장 마지막 값을 구해서 +1를 구하면 max id를 만들 수 있음
        // memoList.keySet() Long(key=id) 값을 가져옴 그중에 가장 큰값 가져오기
        // 123이 있다면 3반환 3+1해서 최대값 만들기 없으면 1
        Long maxId = memoList.size() > 0 ? Collections.max(memoList.keySet()) + 1 : 1;
        memo.setId(maxId); //id에다가 만든 maxId 넣기

        // DB 저장
        memoList.put(memo.getId(), memo);

        // Entity -> ResponseDto 데이터를 Dto로 바꾸기
        MemoResponseDto memoResponseDto = new MemoResponseDto(memo);

        return memoResponseDto;
    }

    //메모가 여러개 일 수 있으니까 List형식으로 반환
    @GetMapping("/memos")
    public List<MemoResponseDto> getMemos() {
        // Map to List
        // values => Map<Long, Memo> memoList에서 Memo에 들어있는 모든 것을 가져옴
        // .stream() for문처럼 돌려줌
        // .map() Memo를 가지고 변환을 해줌 여기서는 MemoResponseDto 에서 생성자 객체로 만들어줌 -> ::new
        // .toList()_모아서 MemoResponseDto type의 List로 만들어줌
        // stream이 어려우면 for문으로 하기
        List<MemoResponseDto> responseDtoList = memoList.values().stream()
                .map(MemoResponseDto::new).toList();
        return responseDtoList;
    }

    // Update 구현하기
    // body에서 JSON형식으로 넘어오므로 @RequsetBody
    @PutMapping("/memos/{id}")
    public Long updateMemo(@PathVariable Long id, @RequestBody MemoRequestDto requestDto) {
        // 해당 메모가 DB에 존재하는 지 확인하기
        if (memoList.containsKey(id)) {
            // 해당 메모 가져오기
            Memo memo = memoList.get(id);

            // memo 수정 수정할 파라미터 requestDto
            memo.update(requestDto);
            return memo.getId();
        } else {
            throw new IllegalArgumentException("선택한 메모는 존재하지 않습니다");
        }
    }


    @DeleteMapping("/memos/{id}")
    public Long deleteMemo(@PathVariable Long id) {
        //  해당 메모가 DB에 존재하는지 확인
        if (memoList.containsKey(id)) {
            // 해당 메모 삭제하기
            memoList.remove(id);
            return id;

        } else {
            throw new IllegalArgumentException("선택한 메모는 존재하지 않습니다.");
        }
    }

}
