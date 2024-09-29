package com.example.demo.repository.search;

import com.example.demo.entity.Board;
import com.example.demo.entity.QBoard;
import com.example.demo.service.BoardServiceImpl;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.JPQLQuery;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.List;

@Log4j2
public class BoardSearchImpl extends QuerydslRepositorySupport implements BoardSearch{
    //제목 내용, 작성자에 따라서 검색한다.
    //제목인지 내용인는 types에 따라서 그에 맞게 검색조건을 작성한다.
    //그러기위해서는 QuerydslRepositorySupport를 사용한다.
    //버전업에 따라 그래이들을 수정해준다.


    public BoardSearchImpl(){
        super(Board.class);
    }


    @Override
    public Page<Board> searchAll(String[] types, String keyword, Pageable pageable) {


        QBoard board = QBoard.board;        //q도메인객체


        JPQLQuery<Board> query = from(board); //select * from board

        System.out.println(query);   //select * from board


        //and, or등을 사용하는 객체
        //select * from board // 제목을 null   select * from baord where ssss = sss
        // %ddd% select * from board where title = '%'안녕'%
        //String[] types 만들어서 올꺼예요 , t c w title content writer
        // tc title = %keyword% or content = %keyword%

        if (types != null && types.length > 0 && keyword != null) {
            BooleanBuilder booleanBuilder = new BooleanBuilder();
            for(String str  : types){

                switch (str){
                    case "t" :
                        booleanBuilder.or(board.title.contains(keyword));
                        break;
                    case "c" :
                        booleanBuilder.or(board.content.contains(keyword));
                        break;
                    case "w" :
                        booleanBuilder.or(board.usersEntity.name.contains(keyword));
                        break;
                }
            }

            query.where(booleanBuilder);
        }




        System.out.println("where 문 추가" + query);

        query.where(board.bno.gt(0L));

        //select * from board where (title = %keyword%  or content = %keyword%) and bno >= 0;

        //페이징처리
        this.getQuerydsl().applyPagination(pageable, query); //리미트 알려주기


        List<Board> boardList =  query.fetch();         //실행

        log.info("서치임플에서 찍혔니?");
        boardList.forEach(board1 -> log.info(board1));

        long count =  query.fetchCount(); //row수

        return new PageImpl<>(boardList, pageable, count);
    }
}
