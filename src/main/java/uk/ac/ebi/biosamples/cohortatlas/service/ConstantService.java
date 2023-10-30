package uk.ac.ebi.biosamples.cohortatlas.service;

import org.springframework.stereotype.Service;
import uk.ac.ebi.biosamples.cohortatlas.model.Constant;
import uk.ac.ebi.biosamples.cohortatlas.repository.ConstantRepository;

import java.util.*;

@Service
public class ConstantService {
  private final ConstantRepository constantRepository;

  public ConstantService(ConstantRepository constantRepository ) {
    this.constantRepository = constantRepository;
  }

  public Constant saveConstant(Constant constant) {
    return constantRepository.save(constant);
  }
  public Map<String,List<String>> findAllFilters( ) {
    List<Constant> all = constantRepository.findAll();
    Map<String,List<String>> filters= new HashMap<>();
     for(Constant c : all) {
       if(filters.containsKey(c.getType())) {
         List<String> existingVals = filters.get(c.getType());
         existingVals.add(c.getValue());
         filters.put(c.getType(), existingVals);
       } else {
         filters.put(c.getType(), Collections.singletonList(c.getValue()));
       }
     }
    return filters;
  }

}
