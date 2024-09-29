package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageRequestDTO {
    //클라이언트로부터 입력받은 url및 파라미터 정보 controller에서 파라미터 수집

    @Builder.Default //들어오면 들어온값, 안들어오면 이값
    private int page = 1;

    @Builder.Default //들어오면 들어온값, 안들어오면 이값
    private int size = 10;

    private String type; //검색 종류 셀렉트박스에서 //t, c, w tc tw twc
                         //<option value = "t"> 제목</option>
                         //<option value = "c"> 제목</option>
    private String keyword; //검색어

    private String link; //링크를 만들어줌 ~~~ www.naver.com?bno=3&size=&page=3

    public String[] getTypes() { //셀렉트박스의 값에 따라 검색조건을 배열로 담는다
        if(type == null || type.isEmpty()) {
            return null;
        }
        return type.split("");
    }

    public Pageable getPageable(String... props) {
        // 페이지 인덱스가 0보다 작으면 0으로 설정
        int pageIndex = Math.max(this.page - 1, 0);
        return PageRequest.of(pageIndex, this.size, Sort.by(props).descending());
    }

    public String getLink(){

        if (link == null){
            StringBuffer buffer = new StringBuffer();

            //www.naver.com?page3&size=10&type="twc&Keyword=홍길동"

            buffer.append("page=" + this.page);
            buffer.append("&size=" + this.size);

            if (type != null && type.length() > 0){

                buffer.append("&type=" + type);

            }

            if (keyword != null){

                try {
                    buffer.append("&keyword=" + URLEncoder.encode(keyword, "UTF-8"));
                } catch (UnsupportedEncodingException e){

                }

            }

            link = buffer.toString();

        }

        return link;

    }
}
