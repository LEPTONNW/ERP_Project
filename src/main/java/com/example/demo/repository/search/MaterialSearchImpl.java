package com.example.demo.repository.search;

import com.example.demo.dto.MaterialDTO;

import com.example.demo.entity.MaterialEntity;
import com.example.demo.entity.QMaterialEntity;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQuery;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.List;
import java.util.stream.Collectors;

@Log4j2
public class MaterialSearchImpl extends QuerydslRepositorySupport implements MaterialSearch {
    public MaterialSearchImpl() { super(MaterialDTO.class);}

    private ModelMapper mapper = new ModelMapper();
    @Override
    public Page<MaterialDTO> searchAll(String[] types, String keyword, Pageable pageable) {

        QMaterialEntity materialEntity = QMaterialEntity.materialEntity;//도메인 객체
        JPQLQuery<MaterialEntity> query = from(materialEntity);
        System.out.println(query);//select * from mat
        if (types != null && types.length > 0 && keyword != null) {
            BooleanBuilder booleanBuilder = new BooleanBuilder();
            for(String str  : types){

                switch (str){
                    case "n" :
                        booleanBuilder.or(materialEntity.matName.contains(keyword));
                        break;
                    case "c" :
                        booleanBuilder.or(materialEntity.matCode.contains(keyword));
                        break;
                    case "bp" :
                        booleanBuilder.or(materialEntity.matBuyPlace.contains(keyword));
                        break;
                    case "bn" :
                        booleanBuilder.or(materialEntity.matBuyNum.contains(keyword));
                        break;
                }
            }

            query.where(booleanBuilder);
        }
       query.where(materialEntity.matNum.gt(0L));
        System.out.println("where문 추가"+query);
        this.getQuerydsl().applyPagination(pageable,query);

        List<MaterialEntity> materialEntityList = query.fetch();

        List<MaterialDTO> materialDTOList =
        materialEntityList.stream().map(materialEntity1 -> mapper.map(materialEntity1, MaterialDTO.class))
                .collect(Collectors.toList());

        long count = query.fetchCount();

        return  new PageImpl<>(materialDTOList,pageable,count);

    }
}
