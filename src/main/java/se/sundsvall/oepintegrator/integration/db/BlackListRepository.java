package se.sundsvall.oepintegrator.integration.db;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import se.sundsvall.oepintegrator.integration.db.model.BlackListEntity;
import se.sundsvall.oepintegrator.util.enums.InstanceType;

@CircuitBreaker(name = "blacklist-repository")
public interface BlackListRepository extends JpaRepository<BlackListEntity, String> {

	List<BlackListEntity> findByMunicipalityIdAndInstanceType(String municipalityId, InstanceType instanceType);
}
