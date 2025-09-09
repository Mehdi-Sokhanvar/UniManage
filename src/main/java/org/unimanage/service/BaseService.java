package org.unimanage.service;

import org.unimanage.domain.BaseModel;
import java.io.Serializable;
import java.util.List;
import java.util.Optional;

public interface BaseService<DTO, ID> {
    DTO create(DTO dto);
    DTO getById(ID id);
    DTO update(ID id, DTO dto);
    void delete(ID id);
    List<DTO> getAll();
}