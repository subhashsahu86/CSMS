package com.school.user.service;

import com.school.user.domain.AddressType;
import com.school.user.domain.Gender;
import com.school.user.domain.ParentStudentLink;
import com.school.user.domain.ProfileStatus;
import com.school.user.domain.StudentProfile;
import com.school.user.domain.UserProfile;
import com.school.user.domain.UserType;
import com.school.user.dto.request.AddressRequest;
import com.school.user.dto.request.CreateStudentProfileRequest;
import com.school.user.dto.request.LinkParentStudentRequest;
import com.school.user.dto.response.ParentStudentLinkResponse;
import com.school.user.dto.response.UserProfileResponse;
import com.school.user.exception.DuplicateResourceException;
import com.school.user.repository.ParentProfileRepository;
import com.school.user.repository.ParentStudentLinkRepository;
import com.school.user.repository.StaffProfileRepository;
import com.school.user.repository.StudentProfileRepository;
import com.school.user.repository.TeacherProfileRepository;
import com.school.user.repository.UserProfileRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class UserProfileServiceTest {

    private UserProfileRepository userProfileRepository;
    private StudentProfileRepository studentProfileRepository;
    private TeacherProfileRepository teacherProfileRepository;
    private ParentProfileRepository parentProfileRepository;
    private StaffProfileRepository staffProfileRepository;
    private ParentStudentLinkRepository parentStudentLinkRepository;
    private UserProfileService userProfileService;

    @BeforeEach
    void setUp() {
        userProfileRepository = mock(UserProfileRepository.class);
        studentProfileRepository = mock(StudentProfileRepository.class);
        teacherProfileRepository = mock(TeacherProfileRepository.class);
        parentProfileRepository = mock(ParentProfileRepository.class);
        staffProfileRepository = mock(StaffProfileRepository.class);
        parentStudentLinkRepository = mock(ParentStudentLinkRepository.class);

        userProfileService = new UserProfileService(
                userProfileRepository,
                studentProfileRepository,
                teacherProfileRepository,
                parentProfileRepository,
                staffProfileRepository,
                parentStudentLinkRepository
        );
    }

    @Test
    void createsStudentProfileWithDefaultDisplayNameAndAddress() {
        AtomicReference<StudentProfile> savedStudentProfile = new AtomicReference<>();

        when(userProfileRepository.existsByAuthUserId(any())).thenReturn(false);
        when(userProfileRepository.existsByUsername("STU2026001001")).thenReturn(false);
        when(userProfileRepository.save(any(UserProfile.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(studentProfileRepository.save(any(StudentProfile.class))).thenAnswer(invocation -> {
            StudentProfile studentProfile = invocation.getArgument(0);
            savedStudentProfile.set(studentProfile);
            return studentProfile;
        });
        when(studentProfileRepository.findByUserProfileId(any())).thenAnswer(invocation -> Optional.of(savedStudentProfile.get()));

        CreateStudentProfileRequest request = new CreateStudentProfileRequest(
                UUID.randomUUID(),
                "STU2026001001",
                "Aarav",
                "Kumar",
                "Sharma",
                null,
                Gender.MALE,
                LocalDate.of(2012, 4, 12),
                null,
                "9876543210",
                null,
                "REG-2026-1001",
                "ADM-2026-1001",
                LocalDate.of(2026, 6, 1),
                "O+",
                "General",
                null,
                List.of(new AddressRequest(
                        AddressType.RESIDENTIAL,
                        "123 Main Street",
                        null,
                        "Mumbai",
                        "Maharashtra",
                        "400001",
                        "India"
                ))
        );

        UserProfileResponse response = userProfileService.createStudentProfile(request);

        assertThat(response.username()).isEqualTo("STU2026001001");
        assertThat(response.userType()).isEqualTo(UserType.STUDENT);
        assertThat(response.displayName()).isEqualTo("Aarav Kumar Sharma");
        assertThat(response.addresses()).hasSize(1);
        assertThat(response.studentProfile()).isNotNull();
        assertThat(response.studentProfile().registrationNumber()).isEqualTo("REG-2026-1001");
    }

    @Test
    void rejectsDuplicateAuthUserId() {
        UUID authUserId = UUID.randomUUID();
        when(userProfileRepository.existsByAuthUserId(authUserId)).thenReturn(true);

        CreateStudentProfileRequest request = new CreateStudentProfileRequest(
                authUserId,
                "STU2026001002",
                "Aarav",
                null,
                "Sharma",
                null,
                Gender.MALE,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                List.of()
        );

        assertThatThrownBy(() -> userProfileService.createStudentProfile(request))
                .isInstanceOf(DuplicateResourceException.class)
                .hasMessageContaining("Profile already exists for auth user ID");
    }

    @Test
    void linksParentAndStudent() {
        UserProfile parent = profile(UserType.PARENT, "PAR2026001001");
        UserProfile student = profile(UserType.STUDENT, "STU2026001003");

        when(userProfileRepository.findById(parent.getId())).thenReturn(Optional.of(parent));
        when(userProfileRepository.findById(student.getId())).thenReturn(Optional.of(student));
        when(parentStudentLinkRepository.save(any(ParentStudentLink.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ParentStudentLinkResponse response = userProfileService.linkParentAndStudent(new LinkParentStudentRequest(
                parent.getId(),
                student.getId(),
                "Father",
                true,
                true
        ));

        assertThat(response.parentUserProfileId()).isEqualTo(parent.getId());
        assertThat(response.studentUserProfileId()).isEqualTo(student.getId());
        assertThat(response.relationship()).isEqualTo("Father");
        assertThat(response.isPrimaryGuardian()).isTrue();
    }

    private UserProfile profile(UserType userType, String username) {
        return new UserProfile(
                UUID.randomUUID(),
                UUID.randomUUID(),
                username,
                userType,
                "Test",
                null,
                "User",
                "Test User",
                Gender.NOT_SPECIFIED,
                null,
                null,
                null,
                null,
                ProfileStatus.ACTIVE
        );
    }
}
