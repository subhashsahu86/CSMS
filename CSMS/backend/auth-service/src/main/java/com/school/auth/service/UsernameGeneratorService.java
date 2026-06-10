package com.school.auth.service;

import com.school.auth.domain.UserType;
import com.school.auth.domain.UsernameSequence;
import com.school.auth.repository.UsernameSequenceRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UsernameGeneratorService {

    private final UsernameSequenceRepository usernameSequenceRepository;

    public UsernameGeneratorService(UsernameSequenceRepository usernameSequenceRepository) {
        this.usernameSequenceRepository = usernameSequenceRepository;
    }

    @Transactional
    public String generate(UserType userType, int admissionYear) {
        UsernameSequence sequence = usernameSequenceRepository.findForUpdate(userType, admissionYear)
                .orElseGet(() -> new UsernameSequence(userType, admissionYear, 1L));

        long sequenceValue = sequence.nextAndIncrement();
        usernameSequenceRepository.save(sequence);

        return "%s%d%06d".formatted(userType.getPrefix(), admissionYear, sequenceValue);
    }
}
