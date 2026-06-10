package com.school.user.service;

import com.school.user.domain.Gender;
import com.school.user.domain.ParentProfile;
import com.school.user.domain.ParentStudentLink;
import com.school.user.domain.ProfileStatus;
import com.school.user.domain.StaffProfile;
import com.school.user.domain.StudentProfile;
import com.school.user.domain.TeacherProfile;
import com.school.user.domain.UserAddress;
import com.school.user.domain.UserProfile;
import com.school.user.domain.UserType;
import com.school.user.dto.request.AddressRequest;
import com.school.user.dto.request.CreateParentProfileRequest;
import com.school.user.dto.request.CreateStaffProfileRequest;
import com.school.user.dto.request.CreateStudentProfileRequest;
import com.school.user.dto.request.CreateTeacherProfileRequest;
import com.school.user.dto.request.LinkParentStudentRequest;
import com.school.user.dto.request.UpdateParentProfileRequest;
import com.school.user.dto.request.UpdateStaffProfileRequest;
import com.school.user.dto.request.UpdateStudentProfileRequest;
import com.school.user.dto.request.UpdateTeacherProfileRequest;
import com.school.user.dto.response.AddressResponse;
import com.school.user.dto.response.ParentProfileResponse;
import com.school.user.dto.response.ParentStudentLinkResponse;
import com.school.user.dto.response.StaffProfileResponse;
import com.school.user.dto.response.StudentProfileResponse;
import com.school.user.dto.response.TeacherProfileResponse;
import com.school.user.dto.response.UserProfileResponse;
import com.school.user.exception.DuplicateResourceException;
import com.school.user.exception.ResourceNotFoundException;
import com.school.user.repository.ParentProfileRepository;
import com.school.user.repository.ParentStudentLinkRepository;
import com.school.user.repository.StaffProfileRepository;
import com.school.user.repository.StudentProfileRepository;
import com.school.user.repository.TeacherProfileRepository;
import com.school.user.repository.UserProfileRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
public class UserProfileService {

    private final UserProfileRepository userProfileRepository;
    private final StudentProfileRepository studentProfileRepository;
    private final TeacherProfileRepository teacherProfileRepository;
    private final ParentProfileRepository parentProfileRepository;
    private final StaffProfileRepository staffProfileRepository;
    private final ParentStudentLinkRepository parentStudentLinkRepository;

    public UserProfileService(
            UserProfileRepository userProfileRepository,
            StudentProfileRepository studentProfileRepository,
            TeacherProfileRepository teacherProfileRepository,
            ParentProfileRepository parentProfileRepository,
            StaffProfileRepository staffProfileRepository,
            ParentStudentLinkRepository parentStudentLinkRepository
    ) {
        this.userProfileRepository = userProfileRepository;
        this.studentProfileRepository = studentProfileRepository;
        this.teacherProfileRepository = teacherProfileRepository;
        this.parentProfileRepository = parentProfileRepository;
        this.staffProfileRepository = staffProfileRepository;
        this.parentStudentLinkRepository = parentStudentLinkRepository;
    }

    @Transactional
    public UserProfileResponse createStudentProfile(CreateStudentProfileRequest request) {
        validateUniqueUser(request.authUserId(), request.username());

        UserProfile profile = buildUserProfile(
                request.authUserId(),
                request.username(),
                UserType.STUDENT,
                request.firstName(),
                request.middleName(),
                request.lastName(),
                request.displayName(),
                request.gender(),
                request.dateOfBirth(),
                request.primaryEmail(),
                request.primaryPhone(),
                request.profileImageUrl(),
                request.addresses()
        );

        UserProfile savedProfile = userProfileRepository.save(profile);

        StudentProfile studentProfile = new StudentProfile(
                UUID.randomUUID(),
                savedProfile,
                request.registrationNumber(),
                request.admissionNumber(),
                request.admissionDate(),
                request.bloodGroup(),
                request.category(),
                request.previousSchool()
        );
        studentProfileRepository.save(studentProfile);

        return mapToResponse(savedProfile);
    }

    @Transactional
    public UserProfileResponse createTeacherProfile(CreateTeacherProfileRequest request) {
        validateUniqueUser(request.authUserId(), request.username());

        UserProfile profile = buildUserProfile(
                request.authUserId(),
                request.username(),
                UserType.TEACHER,
                request.firstName(),
                request.middleName(),
                request.lastName(),
                request.displayName(),
                request.gender(),
                request.dateOfBirth(),
                request.primaryEmail(),
                request.primaryPhone(),
                request.profileImageUrl(),
                request.addresses()
        );

        UserProfile savedProfile = userProfileRepository.save(profile);

        TeacherProfile teacherProfile = new TeacherProfile(
                UUID.randomUUID(),
                savedProfile,
                request.employeeCode(),
                request.qualification(),
                request.specialization(),
                request.joiningDate(),
                request.experienceYears()
        );
        teacherProfileRepository.save(teacherProfile);

        return mapToResponse(savedProfile);
    }

    @Transactional
    public UserProfileResponse createParentProfile(CreateParentProfileRequest request) {
        validateUniqueUser(request.authUserId(), request.username());

        UserProfile profile = buildUserProfile(
                request.authUserId(),
                request.username(),
                UserType.PARENT,
                request.firstName(),
                request.middleName(),
                request.lastName(),
                request.displayName(),
                request.gender(),
                request.dateOfBirth(),
                request.primaryEmail(),
                request.primaryPhone(),
                request.profileImageUrl(),
                request.addresses()
        );

        UserProfile savedProfile = userProfileRepository.save(profile);

        ParentProfile parentProfile = new ParentProfile(
                UUID.randomUUID(),
                savedProfile,
                request.occupation(),
                request.relationshipLabel(),
                request.alternatePhone()
        );
        parentProfileRepository.save(parentProfile);

        return mapToResponse(savedProfile);
    }

    @Transactional
    public UserProfileResponse createStaffProfile(CreateStaffProfileRequest request) {
        validateUniqueUser(request.authUserId(), request.username());

        UserProfile profile = buildUserProfile(
                request.authUserId(),
                request.username(),
                UserType.STAFF,
                request.firstName(),
                request.middleName(),
                request.lastName(),
                request.displayName(),
                request.gender(),
                request.dateOfBirth(),
                request.primaryEmail(),
                request.primaryPhone(),
                request.profileImageUrl(),
                request.addresses()
        );

        UserProfile savedProfile = userProfileRepository.save(profile);

        StaffProfile staffProfile = new StaffProfile(
                UUID.randomUUID(),
                savedProfile,
                request.employeeCode(),
                request.department(),
                request.designation(),
                request.joiningDate()
        );
        staffProfileRepository.save(staffProfile);

        return mapToResponse(savedProfile);
    }

    @Transactional
    public UserProfileResponse updateStudentProfile(UUID authUserId, UpdateStudentProfileRequest request) {
        UserProfile profile = userProfileRepository.findByAuthUserId(authUserId)
                .orElseThrow(() -> new ResourceNotFoundException("User profile not found for auth user ID: " + authUserId));

        if (profile.getUserType() != UserType.STUDENT) {
            throw new IllegalArgumentException("User type mismatch: expected STUDENT but was " + profile.getUserType());
        }

        updateUserProfileDetails(profile, request.firstName(), request.middleName(), request.lastName(),
                request.displayName(), request.gender(), request.dateOfBirth(), request.primaryEmail(),
                request.primaryPhone(), request.profileImageUrl(), request.addresses());

        StudentProfile studentProfile = studentProfileRepository.findByUserProfileId(profile.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Student profile details not found for profile: " + profile.getId()));

        studentProfile.update(
                request.registrationNumber(),
                request.admissionNumber(),
                request.admissionDate(),
                request.bloodGroup(),
                request.category(),
                request.previousSchool()
        );
        studentProfileRepository.save(studentProfile);

        UserProfile savedProfile = userProfileRepository.save(profile);
        return mapToResponse(savedProfile);
    }

    @Transactional
    public UserProfileResponse updateTeacherProfile(UUID authUserId, UpdateTeacherProfileRequest request) {
        UserProfile profile = userProfileRepository.findByAuthUserId(authUserId)
                .orElseThrow(() -> new ResourceNotFoundException("User profile not found for auth user ID: " + authUserId));

        if (profile.getUserType() != UserType.TEACHER) {
            throw new IllegalArgumentException("User type mismatch: expected TEACHER but was " + profile.getUserType());
        }

        updateUserProfileDetails(profile, request.firstName(), request.middleName(), request.lastName(),
                request.displayName(), request.gender(), request.dateOfBirth(), request.primaryEmail(),
                request.primaryPhone(), request.profileImageUrl(), request.addresses());

        TeacherProfile teacherProfile = teacherProfileRepository.findByUserProfileId(profile.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Teacher profile details not found for profile: " + profile.getId()));

        teacherProfile.update(
                request.employeeCode(),
                request.qualification(),
                request.specialization(),
                request.joiningDate(),
                request.experienceYears()
        );
        teacherProfileRepository.save(teacherProfile);

        UserProfile savedProfile = userProfileRepository.save(profile);
        return mapToResponse(savedProfile);
    }

    @Transactional
    public UserProfileResponse updateParentProfile(UUID authUserId, UpdateParentProfileRequest request) {
        UserProfile profile = userProfileRepository.findByAuthUserId(authUserId)
                .orElseThrow(() -> new ResourceNotFoundException("User profile not found for auth user ID: " + authUserId));

        if (profile.getUserType() != UserType.PARENT) {
            throw new IllegalArgumentException("User type mismatch: expected PARENT but was " + profile.getUserType());
        }

        updateUserProfileDetails(profile, request.firstName(), request.middleName(), request.lastName(),
                request.displayName(), request.gender(), request.dateOfBirth(), request.primaryEmail(),
                request.primaryPhone(), request.profileImageUrl(), request.addresses());

        ParentProfile parentProfile = parentProfileRepository.findByUserProfileId(profile.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Parent profile details not found for profile: " + profile.getId()));

        parentProfile.update(
                request.occupation(),
                request.relationshipLabel(),
                request.alternatePhone()
        );
        parentProfileRepository.save(parentProfile);

        UserProfile savedProfile = userProfileRepository.save(profile);
        return mapToResponse(savedProfile);
    }

    @Transactional
    public UserProfileResponse updateStaffProfile(UUID authUserId, UpdateStaffProfileRequest request) {
        UserProfile profile = userProfileRepository.findByAuthUserId(authUserId)
                .orElseThrow(() -> new ResourceNotFoundException("User profile not found for auth user ID: " + authUserId));

        if (profile.getUserType() != UserType.STAFF) {
            throw new IllegalArgumentException("User type mismatch: expected STAFF but was " + profile.getUserType());
        }

        updateUserProfileDetails(profile, request.firstName(), request.middleName(), request.lastName(),
                request.displayName(), request.gender(), request.dateOfBirth(), request.primaryEmail(),
                request.primaryPhone(), request.profileImageUrl(), request.addresses());

        StaffProfile staffProfile = staffProfileRepository.findByUserProfileId(profile.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Staff profile details not found for profile: " + profile.getId()));

        staffProfile.update(
                request.employeeCode(),
                request.department(),
                request.designation(),
                request.joiningDate()
        );
        staffProfileRepository.save(staffProfile);

        UserProfile savedProfile = userProfileRepository.save(profile);
        return mapToResponse(savedProfile);
    }

    @Transactional(readOnly = true)
    public UserProfileResponse getProfileByAuthUserId(UUID authUserId) {
        UserProfile profile = userProfileRepository.findByAuthUserId(authUserId)
                .orElseThrow(() -> new ResourceNotFoundException("User profile not found for auth user ID: " + authUserId));
        return mapToResponse(profile);
    }

    @Transactional(readOnly = true)
    public UserProfileResponse getProfileByUsername(String username) {
        UserProfile profile = userProfileRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User profile not found for username: " + username));
        return mapToResponse(profile);
    }

    @Transactional
    public ParentStudentLinkResponse linkParentAndStudent(LinkParentStudentRequest request) {
        UserProfile parent = userProfileRepository.findById(request.parentUserProfileId())
                .orElseThrow(() -> new ResourceNotFoundException("Parent profile not found: " + request.parentUserProfileId()));

        if (parent.getUserType() != UserType.PARENT) {
            throw new IllegalArgumentException("User " + request.parentUserProfileId() + " is not a PARENT");
        }

        UserProfile student = userProfileRepository.findById(request.studentUserProfileId())
                .orElseThrow(() -> new ResourceNotFoundException("Student profile not found: " + request.studentUserProfileId()));

        if (student.getUserType() != UserType.STUDENT) {
            throw new IllegalArgumentException("User " + request.studentUserProfileId() + " is not a STUDENT");
        }

        ParentStudentLink link = new ParentStudentLink(
                UUID.randomUUID(),
                parent,
                student,
                request.relationship(),
                request.isPrimaryGuardian() != null ? request.isPrimaryGuardian() : false,
                request.canReceiveNotifications() != null ? request.canReceiveNotifications() : true
        );

        ParentStudentLink savedLink = parentStudentLinkRepository.save(link);

        return new ParentStudentLinkResponse(
                savedLink.getId(),
                savedLink.getParentUserProfile().getId(),
                savedLink.getStudentUserProfile().getId(),
                savedLink.getRelationship(),
                request.isPrimaryGuardian() != null ? request.isPrimaryGuardian() : false,
                request.canReceiveNotifications() != null ? request.canReceiveNotifications() : true
        );
    }

    @Transactional(readOnly = true)
    public List<UserProfileResponse> getLinkedStudentsForParent(UUID parentProfileId) {
        UserProfile parent = userProfileRepository.findById(parentProfileId)
                .orElseThrow(() -> new ResourceNotFoundException("Parent profile not found: " + parentProfileId));

        if (parent.getUserType() != UserType.PARENT) {
            throw new IllegalArgumentException("User " + parentProfileId + " is not a PARENT");
        }

        List<ParentStudentLink> links = parentStudentLinkRepository.findByParentUserProfileId(parentProfileId);
        return links.stream()
                .map(link -> mapToResponse(link.getStudentUserProfile()))
                .toList();
    }

    @Transactional(readOnly = true)
    public List<UserProfileResponse> getLinkedParentsForStudent(UUID studentProfileId) {
        UserProfile student = userProfileRepository.findById(studentProfileId)
                .orElseThrow(() -> new ResourceNotFoundException("Student profile not found: " + studentProfileId));

        if (student.getUserType() != UserType.STUDENT) {
            throw new IllegalArgumentException("User " + studentProfileId + " is not a STUDENT");
        }

        List<ParentStudentLink> links = parentStudentLinkRepository.findByStudentUserProfileId(studentProfileId);
        return links.stream()
                .map(link -> mapToResponse(link.getParentUserProfile()))
                .toList();
    }

    private void validateUniqueUser(UUID authUserId, String username) {
        if (userProfileRepository.existsByAuthUserId(authUserId)) {
            throw new DuplicateResourceException("Profile already exists for auth user ID: " + authUserId);
        }
        if (userProfileRepository.existsByUsername(username)) {
            throw new DuplicateResourceException("Profile already exists for username: " + username);
        }
    }

    private UserProfile buildUserProfile(
            UUID authUserId,
            String username,
            UserType userType,
            String firstName,
            String middleName,
            String lastName,
            String displayName,
            Gender gender,
            LocalDate dateOfBirth,
            String primaryEmail,
            String primaryPhone,
            String profileImageUrl,
            List<AddressRequest> addressRequests
    ) {
        String finalDisplayName = displayName;
        if (finalDisplayName == null || finalDisplayName.trim().isEmpty()) {
            StringBuilder sb = new StringBuilder(firstName);
            if (middleName != null && !middleName.trim().isEmpty()) {
                sb.append(" ").append(middleName);
            }
            if (lastName != null && !lastName.trim().isEmpty()) {
                sb.append(" ").append(lastName);
            }
            finalDisplayName = sb.toString();
        }

        UserProfile profile = new UserProfile(
                UUID.randomUUID(),
                authUserId,
                username,
                userType,
                firstName,
                middleName,
                lastName,
                finalDisplayName,
                gender,
                dateOfBirth,
                primaryEmail,
                primaryPhone,
                profileImageUrl,
                ProfileStatus.ACTIVE
        );

        if (addressRequests != null) {
            for (AddressRequest addrReq : addressRequests) {
                UserAddress address = new UserAddress(
                        UUID.randomUUID(),
                        addrReq.addressType(),
                        addrReq.addressLine1(),
                        addrReq.addressLine2(),
                        addrReq.city(),
                        addrReq.state(),
                        addrReq.postalCode(),
                        addrReq.country()
                );
                profile.addAddress(address);
            }
        }

        return profile;
    }

    private void updateUserProfileDetails(
            UserProfile profile,
            String firstName,
            String middleName,
            String lastName,
            String displayName,
            Gender gender,
            LocalDate dateOfBirth,
            String primaryEmail,
            String primaryPhone,
            String profileImageUrl,
            List<AddressRequest> addressRequests
    ) {
        String finalDisplayName = displayName;
        if (finalDisplayName == null || finalDisplayName.trim().isEmpty()) {
            StringBuilder sb = new StringBuilder(firstName);
            if (middleName != null && !middleName.trim().isEmpty()) {
                sb.append(" ").append(middleName);
            }
            if (lastName != null && !lastName.trim().isEmpty()) {
                sb.append(" ").append(lastName);
            }
            finalDisplayName = sb.toString();
        }

        profile.update(
                firstName,
                middleName,
                lastName,
                finalDisplayName,
                gender,
                dateOfBirth,
                primaryEmail,
                primaryPhone,
                profileImageUrl,
                profile.getStatus()
        );

        profile.clearAddresses();

        if (addressRequests != null) {
            for (AddressRequest addrReq : addressRequests) {
                UserAddress address = new UserAddress(
                        UUID.randomUUID(),
                        addrReq.addressType(),
                        addrReq.addressLine1(),
                        addrReq.addressLine2(),
                        addrReq.city(),
                        addrReq.state(),
                        addrReq.postalCode(),
                        addrReq.country()
                );
                profile.addAddress(address);
            }
        }
    }

    private UserProfileResponse mapToResponse(UserProfile profile) {
        List<AddressResponse> addressResponses = profile.getAddresses().stream()
                .map(addr -> new AddressResponse(
                        addr.getId(),
                        addr.getAddressType(),
                        addr.getAddressLine1(),
                        addr.getAddressLine2(),
                        addr.getCity(),
                        addr.getState(),
                        addr.getPostalCode(),
                        addr.getCountry()
                ))
                .toList();

        StudentProfileResponse studentProfileRes = null;
        TeacherProfileResponse teacherProfileRes = null;
        ParentProfileResponse parentProfileRes = null;
        StaffProfileResponse staffProfileRes = null;

        if (profile.getUserType() == UserType.STUDENT) {
            studentProfileRes = studentProfileRepository.findByUserProfileId(profile.getId())
                    .map(p -> new StudentProfileResponse(
                            p.getId(),
                            p.getRegistrationNumber(),
                            p.getAdmissionNumber(),
                            p.getAdmissionDate(),
                            p.getBloodGroup(),
                            p.getCategory(),
                            p.getPreviousSchool()
                    )).orElse(null);
        } else if (profile.getUserType() == UserType.TEACHER) {
            teacherProfileRes = teacherProfileRepository.findByUserProfileId(profile.getId())
                    .map(p -> new TeacherProfileResponse(
                            p.getId(),
                            p.getEmployeeCode(),
                            p.getQualification(),
                            p.getSpecialization(),
                            p.getJoiningDate(),
                            p.getExperienceYears()
                    )).orElse(null);
        } else if (profile.getUserType() == UserType.PARENT) {
            parentProfileRes = parentProfileRepository.findByUserProfileId(profile.getId())
                    .map(p -> new ParentProfileResponse(
                            p.getId(),
                            p.getOccupation(),
                            p.getRelationshipLabel(),
                            p.getAlternatePhone()
                    )).orElse(null);
        } else if (profile.getUserType() == UserType.STAFF) {
            staffProfileRes = staffProfileRepository.findByUserProfileId(profile.getId())
                    .map(p -> new StaffProfileResponse(
                            p.getId(),
                            p.getEmployeeCode(),
                            p.getDepartment(),
                            p.getDesignation(),
                            p.getJoiningDate()
                    )).orElse(null);
        }

        return new UserProfileResponse(
                profile.getId(),
                profile.getAuthUserId(),
                profile.getUsername(),
                profile.getUserType(),
                profile.getFirstName(),
                profile.getMiddleName(),
                profile.getLastName(),
                profile.getDisplayName(),
                profile.getGender(),
                profile.getDateOfBirth(),
                profile.getPrimaryEmail(),
                profile.getPrimaryPhone(),
                profile.getProfileImageUrl(),
                profile.getStatus(),
                addressResponses,
                studentProfileRes,
                teacherProfileRes,
                parentProfileRes,
                staffProfileRes,
                profile.getCreatedAt(),
                profile.getUpdatedAt()
        );
    }
}
