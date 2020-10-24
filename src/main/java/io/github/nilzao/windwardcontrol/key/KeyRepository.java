package io.github.nilzao.windwardcontrol.key;

import org.springframework.data.repository.CrudRepository;

public interface KeyRepository extends CrudRepository<Key, Long> {

    Key findByKey(String key);
}
