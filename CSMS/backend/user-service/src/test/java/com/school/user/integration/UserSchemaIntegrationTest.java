package com.school.user.integration;

import com.school.user.domain.AddressType;
import com.school.user.domain.Gender;
import com.school.user.domain.ProfileStatus;
import com.school.user.domain.UserProfile;
import com.school.user.domain.UserType;
import com.school.user.dto.request.AddressRequest;
import com.school.user.dto.request.CreateParentProfileRequest;
import com.school.user.dto.request.CreateStaffProfileRequest;
import com.school.user.dto.request.CreateStudentProfileRequest;
import com.school.user.dto.request.CreateTeacherProfileRequest;
import com.school.user.dto.request.LinkParentStudentRequest;
import com.school.user.dto.request.UpdateStudentProfileRequest;
import com.school.user.dto.request.UpdateTeacherProfileRequest;
import com.school.user.dto.request.UpdateParentProfileRequest;
import com.school.user.dto.request.UpdateStaffProfileRequest;
import com.school.user.dto.response.ParentStudentLinkResponse;
import com.school.user.dto.response.UserProfileResponse;
import com.school.user.repository.UserProfileRepository;
import com.school.user.service.UserProfileService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers(disabledWithoutDocker = true)
@SpringBootTest
class UserSchemaIntegrationTest {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16")
            .withDatabaseName("user_db")
            .withUsername("user_user")
            .withPassword("user_password");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", () -> postgres.getJdbcUrl() + "&options=-c%20TimeZone=UTC");
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @Autowired
    private UserProfileRepository userProfileRepository;

    @Autowired
    private UserProfileService userProfileService;

    @Test
    void flywaySchemaSupportsSavingAndFindingUserProfile() {
        UUID authUserId = UUID.randomUUID();
        UserProfile profile = new UserProfile(
                UUID.randomUUID(),
                authUserId,
                "STU2026000100",
                UserType.STUDENT,
                "Aarav",
                null,
                "Sharma",
                "Aarav Sharma",
                Gender.MALE,
                LocalDate.of(2012, 4, 12),
                null,
                "9876543210",
                null,
                ProfileStatus.ACTIVE
        );

        userProfileRepository.save(profile);

        Optional<UserProfile> savedProfile = userProfileRepository.findByAuthUserId(authUserId);

        assertThat(savedProfile).isPresent();
        assertThat(savedProfile.get().getUsername()).isEqualTo("STU2026000100");
        assertThat(savedProfile.get().getUserType()).isEqualTo(UserType.STUDENT);
    }

    @Test
    void createAndRetrieveStudentProfileWithAddresses() {
        UUID authUserId = UUID.randomUUID();
        AddressRequest address = new AddressRequest(
                AddressType.RESIDENTIAL,
                "123 Main St",
                "Apt 4B",
                "Mumbai",
                "Maharashtra",
                "400001",
                "India"
        );

        CreateStudentProfileRequest request = new CreateStudentProfileRequest(
                authUserId,
                "STU2026000200",
                "Aarav",
                "Kumar",
                "Sharma",
                "Aarav Sharma",
                Gender.MALE,
                LocalDate.of(2010, 5, 20),
                "aarav@example.com",
                "9876543201",
                "http://example.com/image.jpg",
                "REG12345",
                "ADM54321",
                LocalDate.of(2026, 6, 1),
                "O+",
                "General",
                "Previous High School",
                List.of(address)
        );

        UserProfileResponse response = userProfileService.createStudentProfile(request);

        assertThat(response.id()).isNotNull();
        assertThat(response.authUserId()).isEqualTo(authUserId);
        assertThat(response.username()).isEqualTo("STU2026000200");
        assertThat(response.userType()).isEqualTo(UserType.STUDENT);
        assertThat(response.addresses()).hasSize(1);
        assertThat(response.addresses().get(0).addressLine1()).isEqualTo("123 Main St");
        assertThat(response.studentProfile()).isNotNull();
        assertThat(response.studentProfile().registrationNumber()).isEqualTo("REG12345");

        // Retrieve by authUserId
        UserProfileResponse fetchedByAuth = userProfileService.getProfileByAuthUserId(authUserId);
        assertThat(fetchedByAuth.username()).isEqualTo("STU2026000200");

        // Retrieve by username
        UserProfileResponse fetchedByUsername = userProfileService.getProfileByUsername("STU2026000200");
        assertThat(fetchedByUsername.studentProfile().admissionNumber()).isEqualTo("ADM54321");
    }

    @Test
    void createAndRetrieveTeacherProfile() {
        UUID authUserId = UUID.randomUUID();
        CreateTeacherProfileRequest request = new CreateTeacherProfileRequest(
                authUserId,
                "TCH2026000300",
                "Suresh",
                null,
                "Raina",
                "Suresh Raina",
                Gender.MALE,
                LocalDate.of(1985, 11, 27),
                "suresh@example.com",
                "9876543202",
                null,
                "EMP999",
                "M.Tech in CS",
                "Algorithms",
                LocalDate.of(2025, 7, 1),
                5,
                List.of()
        );

        UserProfileResponse response = userProfileService.createTeacherProfile(request);

        assertThat(response.id()).isNotNull();
        assertThat(response.teacherProfile()).isNotNull();
        assertThat(response.teacherProfile().employeeCode()).isEqualTo("EMP999");
        assertThat(response.teacherProfile().specialization()).isEqualTo("Algorithms");
    }

    @Test
    void createAndRetrieveParentProfile() {
        UUID authUserId = UUID.randomUUID();
        CreateParentProfileRequest request = new CreateParentProfileRequest(
                authUserId,
                "PAR2026000400",
                "Ramesh",
                null,
                "Sharma",
                "Ramesh Sharma",
                Gender.MALE,
                LocalDate.of(1975, 8, 15),
                "ramesh@example.com",
                "9876543203",
                null,
                "Business",
                "Father",
                "9876543204",
                List.of()
        );

        UserProfileResponse response = userProfileService.createParentProfile(request);

        assertThat(response.id()).isNotNull();
        assertThat(response.parentProfile()).isNotNull();
        assertThat(response.parentProfile().occupation()).isEqualTo("Business");
        assertThat(response.parentProfile().relationshipLabel()).isEqualTo("Father");
    }

    @Test
    void createAndRetrieveStaffProfile() {
        UUID authUserId = UUID.randomUUID();
        CreateStaffProfileRequest request = new CreateStaffProfileRequest(
                authUserId,
                "STF2026000500",
                "Sunita",
                null,
                "Williams",
                "Sunita Williams",
                Gender.FEMALE,
                LocalDate.of(1980, 2, 10),
                "sunita@example.com",
                "9876543205",
                null,
                "EMP888",
                "Administration",
                "Registrar",
                LocalDate.of(2026, 1, 15),
                List.of()
        );

        UserProfileResponse response = userProfileService.createStaffProfile(request);

        assertThat(response.id()).isNotNull();
        assertThat(response.staffProfile()).isNotNull();
        assertThat(response.staffProfile().employeeCode()).isEqualTo("EMP888");
        assertThat(response.staffProfile().designation()).isEqualTo("Registrar");
    }

    @Test
    void updateStudentProfileWithAddresses() {
        UUID authUserId = UUID.randomUUID();
        CreateStudentProfileRequest createRequest = new CreateStudentProfileRequest(
                authUserId,
                "STU2026000600",
                "OriginalFirstName",
                null,
                "LastName",
                "Original DisplayName",
                Gender.FEMALE,
                LocalDate.of(2011, 10, 10),
                "original@example.com",
                "9876543206",
                null,
                "REG666",
                "ADM666",
                LocalDate.of(2026, 6, 1),
                "B+",
                "General",
                null,
                List.of(new AddressRequest(AddressType.RESIDENTIAL, "1st St", null, "CityA", "StateA", "11111", "CountryA"))
        );

        userProfileService.createStudentProfile(createRequest);

        UpdateStudentProfileRequest updateRequest = new UpdateStudentProfileRequest(
                "UpdatedFirstName",
                "Middle",
                "LastName",
                "Updated DisplayName",
                Gender.FEMALE,
                LocalDate.of(2011, 10, 10),
                "updated@example.com",
                "9876543207",
                "http://example.com/new.jpg",
                "REG666-U",
                "ADM666-U",
                LocalDate.of(2026, 6, 2),
                "B-",
                "OBC",
                "Some Previous School",
                List.of(
                        new AddressRequest(AddressType.RESIDENTIAL, "2nd St", "Apt 2", "CityB", "StateB", "22222", "CountryB"),
                        new AddressRequest(AddressType.OFFICE, "3rd St", null, "CityC", "StateC", "33333", "CountryC")
                )
        );

        UserProfileResponse response = userProfileService.updateStudentProfile(authUserId, updateRequest);

        assertThat(response.firstName()).isEqualTo("UpdatedFirstName");
        assertThat(response.middleName()).isEqualTo("Middle");
        assertThat(response.displayName()).isEqualTo("Updated DisplayName");
        assertThat(response.primaryEmail()).isEqualTo("updated@example.com");
        assertThat(response.primaryPhone()).isEqualTo("9876543207");
        assertThat(response.profileImageUrl()).isEqualTo("http://example.com/new.jpg");

        assertThat(response.addresses()).hasSize(2);
        assertThat(response.addresses().get(0).addressLine1()).isEqualTo("2nd St");
        assertThat(response.addresses().get(1).addressLine1()).isEqualTo("3rd St");

        assertThat(response.studentProfile().registrationNumber()).isEqualTo("REG666-U");
        assertThat(response.studentProfile().admissionNumber()).isEqualTo("ADM666-U");
        assertThat(response.studentProfile().bloodGroup()).isEqualTo("B-");
        assertThat(response.studentProfile().category()).isEqualTo("OBC");
        assertThat(response.studentProfile().previousSchool()).isEqualTo("Some Previous School");
    }

    @Test
    void linkParentAndStudentSuccessfully() {
        // 1. Create Parent
        UserProfileResponse parent = userProfileService.createParentProfile(new CreateParentProfileRequest(
                UUID.randomUUID(),
                "PAR2026000700",
                "FatherName",
                null,
                "LastName",
                "Father LastName",
                Gender.MALE,
                null,
                "father@example.com",
                "9876543208",
                null,
                "Engineer",
                "Father",
                null,
                List.of()
        ));

        // 2. Create Student
        UserProfileResponse student = userProfileService.createStudentProfile(new CreateStudentProfileRequest(
                UUID.randomUUID(),
                "STU2026000800",
                "ChildName",
                null,
                "LastName",
                "Child LastName",
                Gender.MALE,
                null,
                "child@example.com",
                "9876543209",
                null,
                "REG777",
                "ADM777",
                null,
                null,
                null,
                null,
                List.of()
        ));

        // 3. Link them
        LinkParentStudentRequest linkRequest = new LinkParentStudentRequest(
                parent.id(),
                student.id(),
                "Father",
                true,
                true
        );

        ParentStudentLinkResponse linkResponse = userProfileService.linkParentAndStudent(linkRequest);

        assertThat(linkResponse.id()).isNotNull();
        assertThat(linkResponse.parentUserProfileId()).isEqualTo(parent.id());
        assertThat(linkResponse.studentUserProfileId()).isEqualTo(student.id());
        assertThat(linkResponse.relationship()).isEqualTo("Father");
        assertThat(linkResponse.isPrimaryGuardian()).isTrue();

        // 4. Verify bidirectional listing
        List<UserProfileResponse> linkedStudents = userProfileService.getLinkedStudentsForParent(parent.id());
        assertThat(linkedStudents).hasSize(1);
        assertThat(linkedStudents.get(0).id()).isEqualTo(student.id());

        List<UserProfileResponse> linkedParents = userProfileService.getLinkedParentsForStudent(student.id());
        assertThat(linkedParents).hasSize(1);
        assertThat(linkedParents.get(0).id()).isEqualTo(parent.id());
    }
}
