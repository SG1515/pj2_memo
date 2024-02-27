package com.sparta.memo.entity;

import com.sparta.memo.dto.MemoRequestDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Memo {
    private Long id;
    private String username;
    private String contents;

    // requestDto로 가져온 데이터를 requestDto를 통해 가져와서(get)
    // 위 Memo 클래스 두개의 필드에 데이터를 넣어주면서 Memo객체를 만들어주는 생성자
    public Memo(MemoRequestDto requestDto) {
        this.username = requestDto.getUsername();
        this.contents = requestDto.getContents();
    }
}
