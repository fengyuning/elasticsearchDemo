package com.pirate.esredisdemo.dao;

import com.pirate.esredisdemo.domain.EsAccount;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Component;

@Component
public interface EsAccountDao extends ElasticsearchRepository<EsAccount, Integer> {
}
