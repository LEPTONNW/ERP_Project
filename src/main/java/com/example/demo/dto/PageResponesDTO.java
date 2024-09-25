package com.example.demo.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.jaxb.SpringDataJaxb;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class PageResponesDTO<E> {
    private int page; //현재 페이지
    private int size; //한페이지에 보여줄 게시물수
    private int total;  //게시물총수, 검색시 총수량 변경
                        //end에서 마지막이니? 물어볼수 있음
                        //end는 boolean
    //시작페이지 번호
    private int start;

    //끝 페이지 번호
    private int end;

    //이전페이지 존재 여부
    private boolean prev;

    //다음페이지 존재 여부
    private boolean next;

    private List<E> dtoList; //목록에 대한 결과값, select * from table
                             //다른곳에서도 사용이 가능하도록 컬렉션 사용
                             //List<Board> List<Member> List<notice>
    @Builder(builderMethodName = "withAll")
    public PageResponesDTO(PageRequestDTO pageRequestDTO, List<E> dtoList, int total) {
        if(total <= 0) {
            return;
        }

        this.page = pageRequestDTO.getPage();
        this.size = pageRequestDTO.getSize();
        this.total = total;

        this.dtoList = dtoList;

        this.end = (int)(Math.ceil(this.page / 10.0)) * 10;
        this.start = this.end - 9;
        int last = (int) (Math.ceil(total/(double) size));
        this.end = end > last ? last : end;
        this.prev = this.start > 1;
        this.next = total > this.end * this.size;


    }
}
