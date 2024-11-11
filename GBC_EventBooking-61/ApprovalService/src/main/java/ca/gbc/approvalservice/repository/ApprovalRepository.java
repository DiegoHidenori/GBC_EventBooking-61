package ca.gbc.approvalservice.repository;

import ca.gbc.approvalservice.model.Approval;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApprovalRepository extends JpaRepository<Approval, Long> {
}
