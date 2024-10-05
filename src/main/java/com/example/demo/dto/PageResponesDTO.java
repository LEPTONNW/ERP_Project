package com.example.demo.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PageResponesDTO<E> {
    private int page; // 현재 페이지
    private int size; // 한 페이지에 보여줄 게시물 수
    private int total; // 게시물 총 수
    private int totalPages; // 총 페이지 수
    private int start; // 시작 페이지 번호
    private int end; // 끝 페이지 번호
    private boolean prev; // 이전 페이지 존재 여부
    private boolean next; // 다음 페이지 존재 여부
    private boolean first; // 맨 처음 페이지 존재 여부
    private boolean last; // 맨 마지막 페이지 존재 여부

    private List<E> dtoList; // 목록에 대한 결과값

    @Builder(builderMethodName = "withAll")
    public PageResponesDTO(PageRequestDTO pageRequestDTO, List<E> dtoList, int total) {
        this.page = pageRequestDTO.getPage();
        this.size = pageRequestDTO.getSize();
        this.total = total;

        // 총 페이지 수 계산
        this.totalPages = (int) Math.ceil((double) total / size);
        this.dtoList = dtoList;

        this.end = (int) (Math.ceil((double) this.page / 10.0)) * 10;
        // 시작 페이지
        this.start = this.end - 9;
        // 마지막 페이지
        this.end = Math.min(this.end, totalPages);
        // 이전 페이지 존재 여부
        this.prev = this.start > 1;
        // 다음 페이지 존재 여부
        this.next = total > this.end * this.size;

        // 맨 처음 페이지 존재 여부
        this.first = this.page == 1;

        // 맨 마지막 페이지 존재 여부
        this.last = this.page == totalPages;
    }
}
