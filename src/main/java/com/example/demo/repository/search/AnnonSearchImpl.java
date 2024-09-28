package com.example.demo.repository.search;

import com.example.demo.entity.Annon;
import com.example.demo.entity.QAnnon;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.JPQLQuery;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.List;

@Log4j2
public class AnnonSearchImpl extends QuerydslRepositorySupport implements  AnnonSearch {

    public AnnonSearchImpl(){
        super(Annon.class);
    }

    @Override
    public Page<Annon> searchAll(String[] types, String keyword, Pageable pageable) {

        QAnnon annon = QAnnon.annon;        //q도메인객체

        JPQLQuery<Annon> query = from(annon); //select * from board

        System.out.println(query);   //select * from board


        if (types != null && types.length > 0 && keyword != null) {
            BooleanBuilder booleanBuilder = new BooleanBuilder();
            for(String str  : types){

                switch (str){
                    case "t" :
                        booleanBuilder.or(annon.title.contains(keyword));
                        break;
                    case "c" :
                        booleanBuilder.or(annon.content.contains(keyword));
                        break;
                    case "w" :
                        booleanBuilder.or(annon.usersEntity.name.contains(keyword));
                        break;
                }
            }

            query.where(booleanBuilder);
        }




        System.out.println("where 문 추가" + query);

        query.where(annon.bno.gt(0L));

        //select * from board where (title = %keyword%  or content = %keyword%) and bno >= 0;

        //페이징처리
        this.getQuerydsl().applyPagination(pageable, query); //리미트 알려주기


        List<Annon> annonList =  query.fetch();         //실행

        //log.info("서치임플에서 찍혔니?");
        annonList.forEach(annon1 -> log.info(annon1));

        long count =  query.fetchCount(); //row수

        return new PageImpl<>(annonList, pageable, count);
    }


}
